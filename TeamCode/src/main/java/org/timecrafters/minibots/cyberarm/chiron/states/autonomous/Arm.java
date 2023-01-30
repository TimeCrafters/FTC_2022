package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class Arm extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private final double targetVelocity, timeInMS;
    private final int tolerance, halfTolerance, position, manualPosition;
    private final boolean positionManually, stateDisabled;
    private final String positionLookupLabel;

    public Arm(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        targetVelocity = robot.angleToTicks(
                robot.getConfiguration().variable(groupName, actionName, "targetVelocityInDegreesPerSecond").value());
        tolerance = robot.angleToTicks(
                robot.getConfiguration().variable(groupName, actionName, "toleranceInDegrees").value());
        timeInMS = robot.getConfiguration().variable(groupName, actionName, "timeInMS").value();

        positionLookupLabel = robot.getConfiguration().variable(groupName, actionName, "positionLookupLabel").value();
        positionManually = robot.getConfiguration().variable(groupName, actionName, "positionManually").value();
        manualPosition = robot.angleToTicks(
                robot.getConfiguration().variable(groupName, actionName, "manualTargetAngle").value());

        stateDisabled = !robot.getConfiguration().action(groupName, actionName).enabled;

        halfTolerance = Math.round(tolerance / 2.0f);

        if (positionManually) {
            position = manualPosition;
        } else {
            position = robot.angleToTicks(robot.tuningConfig(positionLookupLabel).value());
        }
    }

    @Override
    public void start() {
        robot.arm.setTargetPosition(position);
        robot.arm.setTargetPositionTolerance(tolerance);
        robot.arm.setVelocity(targetVelocity);
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

        int currentPosition = robot.arm.getCurrentPosition();
        if (robot.isBetween(currentPosition, currentPosition - halfTolerance, currentPosition + halfTolerance)) {
            setHasFinished(true);
        }
    }
}
