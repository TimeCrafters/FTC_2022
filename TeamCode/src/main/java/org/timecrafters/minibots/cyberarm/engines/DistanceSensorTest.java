package org.timecrafters.minibots.cyberarm.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.cyberarm.states.DemoPingPongState;
import org.timecrafters.minibots.cyberarm.states.MecanumRobot;

@TeleOp( name = "Distance Sensor Test")

public class DistanceSensorTest extends CyberarmEngine {

    @Override
    public void setup() {

            MecanumRobot robot;

            robot = new MecanumRobot(this);

            addState(new org.timecrafters.minibots.cyberarm.states.DistanceSensorTest(robot));

    }


}
