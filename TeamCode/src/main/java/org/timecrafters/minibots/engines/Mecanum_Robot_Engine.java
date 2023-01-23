package org.timecrafters.minibots.engines;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.states.MecanumRobot;
import org.timecrafters.minibots.states.Mecanum_Robot_State;

@Disabled
@TeleOp(name = "Mecanum Robot TeleOp")

public class Mecanum_Robot_Engine extends CyberarmEngine {
    MecanumRobot robot;

    @Override
    public void setup() {

        robot = new MecanumRobot(this);

        addState(new Mecanum_Robot_State(robot));

    }
}
