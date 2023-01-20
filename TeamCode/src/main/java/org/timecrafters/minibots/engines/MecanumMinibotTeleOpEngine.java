package org.timecrafters.minibots.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.MecanumMinibot;
import org.timecrafters.minibots.states.MecanumMinibotTeleOpState;

@TeleOp(name = "MecanumMinibot TeleOp", group = "minibot")
public class MecanumMinibotTeleOpEngine extends CyberarmEngine {
    MecanumMinibot robot;

    @Override
    public void setup() {
        robot = new MecanumMinibot(this);

        addState(new MecanumMinibotTeleOpState(robot));
    }
}
