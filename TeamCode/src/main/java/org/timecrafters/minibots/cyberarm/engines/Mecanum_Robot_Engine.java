package org.timecrafters.minibots.cyberarm.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.cyberarm.states.MecanumRobot;
import org.timecrafters.minibots.cyberarm.states.Mecanum_Robot_State;

@TeleOp(name = "Mecanum Robot TeleOp")

public class Mecanum_Robot_Engine extends CyberarmEngine {
    MecanumRobot robot;

    @Override
    public void setup() {

        robot = new MecanumRobot(this);

        addState(new Mecanum_Robot_State(robot));

    }
}
