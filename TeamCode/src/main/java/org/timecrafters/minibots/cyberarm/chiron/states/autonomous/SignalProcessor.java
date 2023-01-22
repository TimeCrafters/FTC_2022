package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class SignalProcessor extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    public SignalProcessor(Robot robot, String groupName, String actionName) {
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
