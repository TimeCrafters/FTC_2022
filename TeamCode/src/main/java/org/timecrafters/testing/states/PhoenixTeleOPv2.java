package org.timecrafters.testing.states;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.cyberarm.engine.V2.CyberarmState;

public class PhoenixTeleOPv2 extends CyberarmState {
    PhoenixBot1 robot;
    public PhoenixTeleOPv2(PhoenixBot1 robot) {
        this.robot = robot;
    }

    @Override
    public void start() {
        addParallelState(new TeleOPArmDriver(robot));
        addParallelState(new TeleOPTankDriver(robot));

    }

    @Override
    public void exec() {

    }
}
