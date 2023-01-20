package org.timecrafters.minibots.engines;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.states.MecanumRobot;
import org.timecrafters.minibots.states.Mecanum_Fancy_Drive_State;
@Disabled
@TeleOp(name = "Fancy Drive TeleOp")

public class Mecanum_fancy_drive_TeleOp extends CyberarmEngine {
    MecanumRobot robot;

    @Override
    public void setup() {

        robot = new MecanumRobot(this);

        addState(new Mecanum_Fancy_Drive_State(robot));

    }
}
