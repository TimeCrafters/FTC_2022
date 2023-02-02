package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import com.qualcomm.robotcore.util.Range;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class Move extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private final double tolerance, facing, targetVelocity, minimumVelocity, timeInMS, easeInDistance, easeOutDistance;
    private final int targetDistance;

    private final double maxVelocity;
    private double speed;

    private int leftDistanceAlreadyTravelled, rightDistanceAlreadyTravelled, leftTravelledDistance, rightTravelledDistance;
    private final boolean stateDisabled;

    private double velocity;
    private double ratio;
    private boolean stopped = false, correcting = false, correctingLeft = false;

    public Move(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        targetDistance = robot.unitToTicks(DistanceUnit.INCH, robot.getConfiguration().variable(groupName, actionName, "targetDistanceInInches").value());
        tolerance = robot.unitToTicks(DistanceUnit.INCH, robot.getConfiguration().variable(groupName, actionName, "toleranceInInches").value());
        facing = robot.getConfiguration().variable(groupName, actionName, "facing").value();
        targetVelocity = robot.unitToTicks(DistanceUnit.INCH, robot.getConfiguration().variable(groupName, actionName, "targetVelocityInInches").value());
        minimumVelocity = robot.unitToTicks(DistanceUnit.INCH, robot.getConfiguration().variable(groupName, actionName, "minimumVelocityInInches").value());
        timeInMS = robot.getConfiguration().variable(groupName, actionName, "timeInMS").value();
        stateDisabled = !robot.getConfiguration().action(groupName, actionName).enabled;

        easeInDistance = robot.unitToTicks(DistanceUnit.INCH, robot.getConfiguration().variable(groupName, actionName, "easeInDistanceInInches").value());
        easeOutDistance = robot.unitToTicks(DistanceUnit.INCH, robot.getConfiguration().variable(groupName, actionName, "easeOutDistanceInInches").value());

        maxVelocity = robot.unitToTicks(DistanceUnit.INCH, robot.tuningConfig("drivetrain_max_velocity_in_inches").value());
        speed = 0.0;
    }

    @Override
    public void start() {
        // TODO: Use a dead wheel for this
        leftDistanceAlreadyTravelled = -robot.frontRightDrive.getCurrentPosition();
        rightDistanceAlreadyTravelled = -robot.backLeftDrive.getCurrentPosition();
    }

    @Override
    public void exec() {
        if (stateDisabled) {
            setHasFinished(true);

            return;
        }

        if (runTime() >= timeInMS) {
            stop();

            setHasFinished(true);

            return;
        }

        leftTravelledDistance = -robot.frontRightDrive.getCurrentPosition() - leftDistanceAlreadyTravelled;
        rightTravelledDistance = -robot.backLeftDrive.getCurrentPosition() - rightDistanceAlreadyTravelled;

        if (robot.isBetween(leftTravelledDistance, targetDistance - tolerance, targetDistance + tolerance) ||
                robot.isBetween(rightTravelledDistance, targetDistance - tolerance, targetDistance + tolerance)) {
            correctOrientation();
        } else {
            moveDirectional();
        }
    }

    @Override
    public void telemetry() {
        engine.telemetry.addLine("Move");
        engine.telemetry.addData("Ratio", ratio);
        engine.telemetry.addData("Minimum Velocity", minimumVelocity);
        engine.telemetry.addData("Target Velocity", targetVelocity);
        engine.telemetry.addData("Velocity", velocity);
        engine.telemetry.addData("Target Distance", targetDistance);
        engine.telemetry.addData("Travelled Distance", leftTravelledDistance);
        engine.telemetry.addData("Distance Already Travelled", leftDistanceAlreadyTravelled);
        engine.telemetry.addData("Travel Forward", targetDistance > leftTravelledDistance);
        engine.telemetry.addData("Left Drive Error", engine.blackboardGetInt("left_drive_error"));
        engine.telemetry.addData("Right Drive Error", engine.blackboardGetInt("right_drive_error"));
    }

    private void moveDirectional() {
        double travelledDistance = leftTravelledDistance;
        ratio = 1.0;

        if (Math.abs(travelledDistance) < easeInDistance) {
            ratio = Math.abs(travelledDistance) / easeInDistance;
        } else if (Math.abs(travelledDistance) > Math.abs(targetDistance) - easeOutDistance) {
            ratio = (Math.abs(targetDistance) - Math.abs(travelledDistance)) / easeOutDistance;
        } else {
            ratio = 1.0;
        }

        ratio = Range.clip(ratio, 0.0, 1.0);

        velocity = robot.lerp(minimumVelocity, targetVelocity, ratio);

        if (targetDistance > travelledDistance) {
            robot.frontRightDrive.setVelocity(-velocity);
            robot.backLeftDrive.setVelocity(-velocity);
        } else {
            robot.frontRightDrive.setVelocity(velocity);
            robot.backLeftDrive.setVelocity(velocity);
        }
    }

    private void moveOmni() {
        if (Math.abs(speed) > maxVelocity) {
            speed = speed < 0 ? -maxVelocity : maxVelocity;
        }

        speed = 1.0;

        double forwardSpeed = -1.0; //distanceInInches < 0 ? -1.0 : 1.0;
        double rightSpeed   = 0.0;
        double rotateRightSpeed = 0.0;

        if (!robot.isBetween((robot.facing() + robot.turnRate()), robot.facing() - 1, robot.facing() + 1)) {
            if (robot.facing() < facing) {
                rotateRightSpeed = -0.5;
            } else {
                rotateRightSpeed = 0.5;
            }
        }

        rotateRightSpeed = 0;

        // FIXME: targetSpeed * speed[Limiter] will cause infinitesimal power issues
        double y = -forwardSpeed * speed;
        double x = (-rightSpeed * speed) * 1.1; // Counteract imperfect strafing. TODO: make tunable
        double rx = -rotateRightSpeed * speed;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);


        double frontLeftPower, frontRightPower, backLeftPower, backRightPower;

        double heading = robot.heading();
        double rotX = x * Math.cos(heading) - y * Math.sin(heading);
        double rotY = x * Math.sin(heading) + y * Math.cos(heading);

        frontLeftPower  = (rotY + rotX + rx) / denominator;
        backLeftPower   = (rotY - rotX - rx) / denominator;
        frontRightPower = (rotY - rotX + rx) / denominator;
        backRightPower  = (rotY + rotX - rx) / denominator;

        robot.frontLeftDrive.setVelocity(frontLeftPower * targetVelocity);
        robot.frontRightDrive.setVelocity(frontRightPower * targetVelocity);

        robot.backLeftDrive.setVelocity(backLeftPower * targetVelocity);
        robot.backRightDrive.setVelocity(backRightPower * targetVelocity);
    }

    private void correctOrientation() {
        if (!stopped && Math.abs(robot.backLeftDrive.getVelocity()) > 0 && Math.abs(robot.frontRightDrive.getVelocity()) > 0) {
            stop();
        } else {
            stopped = true;

            if (!correcting) {
                correcting = true;

                if (leftTravelledDistance > rightTravelledDistance) {
                    correctingLeft = true;
                } else if (leftTravelledDistance < rightTravelledDistance) {
                    correctingLeft = false;
                }
            }

            if (correctingLeft) {
                robot.frontRightDrive.setVelocity(robot.unitToTicks(DistanceUnit.INCH, 1));

                if (leftTravelledDistance <= rightTravelledDistance) {
                    stop();

                    setHasFinished(true);
                }
            } else {
                robot.backLeftDrive.setVelocity(robot.unitToTicks(DistanceUnit.INCH, 1));

                if (leftTravelledDistance >= rightTravelledDistance) {
                    stop();

                    setHasFinished(true);
                }
            }
        }
    }

    @Override
    public void stop() {
        robot.backLeftDrive.setVelocity(0); robot.frontLeftDrive.setPower(0);
        robot.frontRightDrive.setVelocity(0); robot.frontRightDrive.setPower(0);

        robot.frontLeftDrive.setVelocity(0); robot.backLeftDrive.setPower(0);
        robot.backRightDrive.setVelocity(0); robot.backRightDrive.setPower(0);

        engine.blackboardSet("left_drive_error", (int)(targetDistance - leftTravelledDistance));
        engine.blackboardSet("right_drive_error", (int)(targetDistance - rightTravelledDistance));
    }
}
