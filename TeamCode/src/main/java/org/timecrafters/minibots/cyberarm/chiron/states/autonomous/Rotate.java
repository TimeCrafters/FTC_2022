package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class Rotate extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private final double timeInMS, facing, targetFacing, targetVelocity, targetSlowVelocity, slowAngleCloseness, toleranceInDegrees;
    private final boolean stateDisabled, useShortestRotation, rotateRight;

    private double velocity;

    public Rotate(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        timeInMS = robot.getConfiguration().variable(groupName, actionName, "timeInMS").value();

        targetFacing = robot.getConfiguration().variable(groupName, actionName, "targetFacing").value();
        useShortestRotation = robot.getConfiguration().variable(groupName, actionName, "useShortestRotation").value();
        rotateRight = robot.getConfiguration().variable(groupName, actionName, "rotateRight").value();
        toleranceInDegrees = robot.getConfiguration().variable(groupName, actionName, "toleranceInDegrees").value();
        targetVelocity = robot.unitToTicks(DistanceUnit.INCH,
                robot.getConfiguration().variable(groupName, actionName, "targetVelocityInInches").value());
        targetSlowVelocity = robot.unitToTicks(DistanceUnit.INCH,
                robot.getConfiguration().variable(groupName, actionName, "targetSlowVelocityInInches").value());
        slowAngleCloseness = robot.getConfiguration().variable(groupName, actionName, "slowAngleCloseness").value();

        stateDisabled = !robot.getConfiguration().action(groupName, actionName).enabled;

        facing = (robot.initialFacing() + targetFacing + 360.0) % 360.0;

        velocity = targetVelocity;
    }

    @Override
    public void start() {
        engine.telemetry.speak("Rotate");
    }

    @Override
    public void telemetry() {
        engine.telemetry.addLine("Rotate");
        engine.telemetry.addData("Angle Diff", robot.angleDiff(robot.facing(), facing));
        engine.telemetry.addData("Target Facing/Angle", facing);
        engine.telemetry.addData("Provided Target Facing/Angle", targetFacing);
        engine.telemetry.addData("Use Shortest Rotation", useShortestRotation);
        engine.telemetry.addData("Rotate Right", rotateRight);
        engine.telemetry.addData("Tolerance In Degrees", toleranceInDegrees);
        engine.telemetry.addData("Target Velocity", targetVelocity);
        engine.telemetry.addData("Target Slow Velocity", targetSlowVelocity);
        engine.telemetry.addData("Slow Angle Closeness", slowAngleCloseness);
    }

    @Override
    public void exec() {
        if (stateDisabled) {
            stop();

            setHasFinished(true);

            return;
        }

        if (runTime() >= timeInMS) {
            stop();

            setHasFinished(true);

            return;
        }

        double currentDegrees = robot.facing();
        double diff = robot.angleDiff(currentDegrees, facing);
        velocity = targetVelocity;

        if (Math.abs(diff) <= toleranceInDegrees) {
            stop();

            setHasFinished(true);

            return;
        }

        if (Math.abs(diff) <= slowAngleCloseness) {
            velocity = targetSlowVelocity;
        }

        if (useShortestRotation) {
            if (diff < 0) {
                rotateLeft();
            } else {
                rotateRight();
            }
        } else {
            if (rotateRight) {
                rotateRight();
            } else {
                rotateLeft();
            }
        }
    }

    private void rotateLeft() {
        rotate(-1);
    }

    private void rotateRight() {
        rotate(1);
    }

    private void rotate(double multiplier) {
        robot.frontRightDrive.setVelocity(-velocity * multiplier);
        robot.backLeftDrive.setVelocity(velocity * multiplier);

        robot.backRightDrive.setVelocity(velocity * multiplier);
        robot.frontLeftDrive.setVelocity(-velocity * multiplier);
    }

    @Override
    public void stop() {
        robot.frontLeftDrive.setVelocity(0);
        robot.frontRightDrive.setVelocity(0);

        robot.backLeftDrive.setVelocity(0);
        robot.backRightDrive.setVelocity(0);
    }
}
