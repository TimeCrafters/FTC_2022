package org.timecrafters.minibots.cyberarm.states;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.pickle_minibot;

public class pickle_telescope_state extends CyberarmState {
    private final pickle_minibot robot;
    public pickle_telescope_state(pickle_minibot robot){this.robot = robot;}

    @Override
    public void exec() {
        robot.pLeftFront.setPower(1);

    }

    @Override
    public void exac() {

    }
}

