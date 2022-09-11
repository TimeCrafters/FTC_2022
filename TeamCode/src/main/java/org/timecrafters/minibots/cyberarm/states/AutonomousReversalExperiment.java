package org.timecrafters.minibots.cyberarm.states;

import org.cyberarm.engine.V2.CyberarmState;

public class AutonomousReversalExperiment extends CyberarmState {

    MecanumRobot robot;
    public AutonomousReversalExperiment (MecanumRobot robot) {
        this.robot = robot;
    }

    @Override
    public void exec() {

    if (engine.gamepad1.dpad_up) {



    }

    }
}
