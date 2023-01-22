package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class Move extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private final double positionXInInches, positionYInInches, toleranceInInches, facing, targetVelocity, timeInMS;

    private final double maxSpeed;
    private double speed;

    private int distanceAlreadyTravelled;
    private final boolean stateDisabled;

    public Move(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        positionXInInches = robot.getConfiguration().variable(groupName, actionName, "positionXInInches").value();
        positionYInInches = robot.getConfiguration().variable(groupName, actionName, "positionYInInches").value();
        toleranceInInches = robot.getConfiguration().variable(groupName, actionName, "toleranceInInches").value();
        facing = robot.getConfiguration().variable(groupName, actionName, "facing").value();
        targetVelocity = robot.getConfiguration().variable(groupName, actionName, "targetVelocity").value();
        timeInMS = robot.getConfiguration().variable(groupName, actionName, "timeInMS").value();
        stateDisabled = !robot.getConfiguration().action(groupName, actionName).enabled;

        maxSpeed = robot.tuningConfig("drivetrain_max_power").value();
        speed = 0.0;
    }

    @Override
    public void start() {
        // TODO: Use a dead wheel for this
        distanceAlreadyTravelled = robot.frontLeftDrive.getCurrentPosition();
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

        // TODO: Double check maths
//        int travelledDistance = (robot.frontLeftDrive.getCurrentPosition() - distanceAlreadyTravelled);
//        int targetDistance = robot.unitToTicks(DistanceUnit.INCH, distanceInInches);
//        int tolerance = robot.unitToTicks(DistanceUnit.INCH, toleranceInInches);
//
//        if (robot.isBetween(targetDistance, travelledDistance - tolerance, travelledDistance + tolerance)) {
//            stop();
//
//            setHasFinished(true);
//
//            return;
//        }

        move();
    }

    private void move() {
        if (Math.abs(speed) > maxSpeed) {
            speed = speed < 0 ? -maxSpeed : maxSpeed;
        }

        double forwardSpeed = 1.0; //distanceInInches < 0 ? -1.0 : 1.0;
        double rightSpeed   = 0.0;
        double rotateRightSpeed = 0.0;

        if (!robot.isBetween((robot.facing() + robot.turnRate()), robot.facing() - 1, robot.facing() + 1)) {
            if (robot.facing() < facing) {
                rotateRightSpeed = -0.5;
            } else {
                rotateRightSpeed = 0.5;
            }
        }

        // FIXME: targetSpeed * speed[Limiter] will cause infinitesimal power issues
        double y = -forwardSpeed * speed;
        double x = (-rightSpeed * speed) * 1.1; // Counteract imperfect strafing. TODO: make tunable
        double rx = -rotateRightSpeed * speed;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);


        double frontLeftPower = 0, frontRightPower = 0, backLeftPower = 0 , backRightPower = 0;

        double heading = robot.heading();
        double rotX = x * Math.cos(heading) - y * Math.sin(heading);
        double rotY = x * Math.sin(heading) + y * Math.cos(heading);

        frontLeftPower  = (rotY + rotX + rx) / denominator;
        backLeftPower   = (rotY - rotX - rx) / denominator;
        frontRightPower = (rotY - rotX + rx) / denominator;
        backRightPower  = (rotY + rotX - rx) / denominator;

        robot.frontLeftDrive.setPower(frontLeftPower);
        robot.frontRightDrive.setPower(frontRightPower);

        robot.backLeftDrive.setPower(backLeftPower);
        robot.backRightDrive.setPower(backRightPower);
    }

    @Override
    public void stop() {
        robot.frontLeftDrive.setPower(0);
        robot.frontRightDrive.setPower(0);

        robot.backLeftDrive.setPower(0);
        robot.backRightDrive.setPower(0);
    }
}
