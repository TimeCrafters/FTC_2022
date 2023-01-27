package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class Gripper extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private final boolean positionManually, stateDisabled;
    private final String positionLookupLabel;
    private final double manualPosition, timeInMS, position;

    public Gripper(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        timeInMS = robot.getConfiguration().variable(groupName, actionName, "timeInMS").value();

        positionLookupLabel = robot.getConfiguration().variable(groupName, actionName, "positionLookupLabel").value();
        positionManually = robot.getConfiguration().variable(groupName, actionName, "positionManually").value();
        manualPosition = robot.getConfiguration().variable(groupName, actionName, "manualPosition").value();

        stateDisabled = !robot.getConfiguration().action(groupName, actionName).enabled;

        if (positionManually) {
            position = manualPosition;
        } else {
            position = robot.tuningConfig(positionLookupLabel).value();
        }
    }

    @Override
    public void start() {
        robot.gripper.setPosition(position);
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
