package org.timecrafters.minibots.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.states.MecanumRobot;
import org.timecrafters.minibots.states.PingPongState;

@TeleOp (name = "Sodi PingPong")

public class PingPongEngine extends CyberarmEngine {

    MecanumRobot robot;

    @Override
    public void setup() {

        robot = new MecanumRobot(this);

        addState(new PingPongState(robot));


    }
}
