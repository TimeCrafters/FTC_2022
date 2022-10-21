package org.timecrafters.Autonomous.Engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Autonomous.States.LowerArm;
import org.timecrafters.Autonomous.States.UpperArm;
import org.timecrafters.testing.states.PrototypeBot1;

@Autonomous (name = "Autonomous Test")

public class TestAutonomousEngine extends CyberarmEngine {
    PrototypeBot1 robot;

    @Override
    public void setup() {
        robot = new PrototypeBot1(this);

        addState(new UpperArm(robot, "TestAutonomous", "01-0"));
        addState(new LowerArm(robot, "TestAutonomous", "02-0"));


    }
}
