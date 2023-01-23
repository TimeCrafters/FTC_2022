package org.timecrafters.minibots.engines;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.states.FieldOrientedDrive;
import org.timecrafters.minibots.states.MecanumRobot;

@Disabled
@TeleOp(name = "Field Oriented Drive")

public class FieldOrientedEngine extends CyberarmEngine {

    MecanumRobot robot;
    @Override
    public void setup() {

        robot = new MecanumRobot(this);

        addState(new FieldOrientedDrive(robot));

    }
}

