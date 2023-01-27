package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class Arm extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private final double targetVelocity, timeInMS;
    private final int tolerance, targetPosition;
    private final boolean stateDisabled;

    public Arm(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        targetVelocity = robot.angleToTicks(
                robot.getConfiguration().variable(groupName, actionName, "targetVelocityInDegreesPerSecond").value());
        tolerance = robot.angleToTicks(
                robot.getConfiguration().variable(groupName, actionName, "toleranceInDegrees").value());
        targetPosition = robot.angleToTicks(
                robot.getConfiguration().variable(groupName, actionName, "targetAngle").value());
        timeInMS = robot.getConfiguration().variable(groupName, actionName, "timeInMS").value();

        stateDisabled = !robot.getConfiguration().action(groupName, actionName).enabled;
    }

    @Override
    public void start() {
        robot.arm.setTargetPosition(targetPosition);
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
    }
}
