package org.timecrafters.testing.states;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.testing.engine.DynamicSetupEngine;

public class DynamicSetupState extends CyberarmState {
    private long delay;

    public DynamicSetupState(DynamicSetupEngine.Robot robot, String groupName, String actionName) {
        delay = robot.configuration.variable(groupName, actionName, "delay").value();
    }

    @Override
    public void exec() {
        if (runTime() >= delay) {
            setHasFinished(true);
        }
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("runTime", runTime());
        engine.telemetry.addData("delay", delay);
        engine.telemetry.addLine(progressBar(20, (runTime() / delay) * 100));
    }
}
