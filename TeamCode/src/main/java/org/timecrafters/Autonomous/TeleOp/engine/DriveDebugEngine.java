package org.timecrafters.Autonomous.TeleOp.engine;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Autonomous.TeleOp.states.PhoenixBot1;

public class DriveDebugEngine extends CyberarmEngine {

    PhoenixBot1 robot;

    @Override
    public void setup() {
        robot = new PhoenixBot1(this);

    }
}
