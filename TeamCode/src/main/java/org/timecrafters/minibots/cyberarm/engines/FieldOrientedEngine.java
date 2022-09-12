package org.timecrafters.minibots.cyberarm.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.states.FieldOrientedDrive;
import org.timecrafters.minibots.cyberarm.states.MecanumRobot;

@TeleOp(name = "Field Oriented Drive")

public class FieldOrientedEngine extends CyberarmEngine {

    MecanumRobot robot;
    @Override
    public void setup() {

        robot = new MecanumRobot(this);

        addState(new FieldOrientedDrive(robot));

    }
}

