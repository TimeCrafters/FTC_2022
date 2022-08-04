package org.timecrafters.minibots.cyberarm.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.cyberarm.states.MecanumRobot;
import org.timecrafters.minibots.cyberarm.states.Mecanum_Fancy_Drive_State;

@TeleOp(name = "Fancy Drive TeleOp")

public class Mecanum_fancy_drive_TeleOp extends CyberarmEngine {
    MecanumRobot robot;

    @Override
    public void setup() {

        robot = new MecanumRobot(this);

        addState(new Mecanum_Fancy_Drive_State(robot));

    }
}
