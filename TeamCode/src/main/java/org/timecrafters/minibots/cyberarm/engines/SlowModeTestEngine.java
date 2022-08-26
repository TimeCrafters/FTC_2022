package org.timecrafters.minibots.cyberarm.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.cyberarm.states.MecanumRobot;
import org.timecrafters.minibots.cyberarm.states.SlowModeTestPingPong;

@TeleOp (name = "Slow Mode Test")

public class SlowModeTestEngine extends CyberarmEngine {
    MecanumRobot robot;

    @Override
    public void setup() {

        robot = new MecanumRobot(this);

        addState(new SlowModeTestPingPong(robot));

    }
}
