package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class Gripper extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    public Gripper(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;
    }

    @Override
    public void exec() {
        // FIXME: NO OP
        setHasFinished(true);
    }
}
