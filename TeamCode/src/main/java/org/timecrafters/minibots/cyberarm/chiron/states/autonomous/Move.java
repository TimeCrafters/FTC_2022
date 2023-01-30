package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import com.qualcomm.robotcore.util.Range;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class Move extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private final double targetDistance, tolerance, facing, targetVelocity, minimumVelocity, timeInMS, easeInDistance, easeOutDistance;

    private final double maxVelocity;
    private double speed;

    private int distanceAlreadyTravelled;
    private final boolean stateDisabled;

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
        distanceAlreadyTravelled = robot.frontRightDrive.getCurrentPosition();
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

        int travelledDistance = -robot.frontRightDrive.getCurrentPosition() - distanceAlreadyTravelled;

        if (robot.isBetween(travelledDistance, targetDistance - tolerance, targetDistance + tolerance)) {
            stop();

            setHasFinished(true);

            return;
        }

        moveDirectional(travelledDistance);
    }

    private void moveDirectional(double travelledDistance) {
        double velocity;
        double ratio = 1.0;

        if (Math.abs(travelledDistance) < easeInDistance) {
            ratio = travelledDistance / easeInDistance;
        } else if (Math.abs(travelledDistance) > Math.abs(targetDistance) - easeOutDistance) {
            ratio = (Math.abs(targetDistance) - Math.abs(travelledDistance)) / easeOutDistance;
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

    @Override
    public void stop() {
        robot.backLeftDrive.setVelocity(0); robot.frontLeftDrive.setPower(0);
        robot.frontRightDrive.setVelocity(0); robot.frontRightDrive.setPower(0);

        robot.frontLeftDrive.setVelocity(0); robot.backLeftDrive.setPower(0);
        robot.backRightDrive.setVelocity(0); robot.backRightDrive.setPower(0);
    }
}
