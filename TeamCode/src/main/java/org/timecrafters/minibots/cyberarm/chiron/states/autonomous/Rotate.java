package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class Rotate extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private final double timeInMS, facing, targetFacing, targetVelocity, toleranceInDegrees;
    private final boolean stateDisabled, useShortestRotation, rotateRight;

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

        stateDisabled = !robot.getConfiguration().action(groupName, actionName).enabled;

        facing = (robot.facing() + targetFacing + 360.0) % 360.0;
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
        double diff = robot.angleDiff(facing, currentDegrees);

        if (Math.abs(diff) <= toleranceInDegrees) {
            stop();

            setHasFinished(true);

            return;
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
        robot.frontRightDrive.setVelocity(targetVelocity * multiplier);
        robot.backLeftDrive.setVelocity(targetVelocity * multiplier);

        robot.backRightDrive.setVelocity(targetVelocity * multiplier);
        robot.frontLeftDrive.setVelocity(targetVelocity * multiplier);
    }

    @Override
    public void stop() {
        robot.frontLeftDrive.setVelocity(0);
        robot.frontRightDrive.setVelocity(0);

        robot.backLeftDrive.setVelocity(0);
        robot.backRightDrive.setVelocity(0);
    }
}
