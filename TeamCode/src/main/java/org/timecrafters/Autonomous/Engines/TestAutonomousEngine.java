package org.timecrafters.Autonomous.Engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Autonomous.States.CollectorState;
import org.timecrafters.Autonomous.States.DriverState;
import org.timecrafters.Autonomous.States.LowerArm;
import org.timecrafters.Autonomous.States.RotationState;
import org.timecrafters.Autonomous.States.UpperArm;
import org.timecrafters.testing.states.PrototypeBot1;

@Autonomous (name = "Autonomous Test")

public class TestAutonomousEngine extends CyberarmEngine {
    PrototypeBot1 robot;

    @Override
    public void setup() {
        robot = new PrototypeBot1(this);

        //drive to high pole
        addState(new DriverState(robot, "TestAutonomous", "01-0"));
        //turn towards high pole
        addState(new RotationState(robot, "TestAutonomous", "02-0"));
        //lift the upper arm
        addState(new UpperArm(robot, "TestAutonomous", "03-0"));
        //lift the lower arm
        addState(new LowerArm(robot, "TestAutonomous", "04-0"));
        //drive forward
        addState(new DriverState(robot, "TestAutonomous", "05-0"));
        //lower the bottom arm to get closer
        addState(new LowerArm(robot, "TestAutonomous", "06-0"));
        //make collector release the cone
        addState(new CollectorState(robot, "TestAutnomous", "07-0"));
        //lift the lower arm to clear the pole
        addState(new LowerArm(robot, "TestAutonomous", "08-0"));
        //Drive Backwards
        addState(new DriverState(robot, "TestAutonomous", "09-0"));
        // Rotate to either set up for cones to grab or park somewhere
        addState(new RotationState(robot, "TestAutonomous", "10-0"));
        // lower the bottom arm so we dont fall over
        addState(new LowerArm(robot, "TestAutonomous", "11-0"));
        // lower the upper arm so we dont fall over
        addState(new UpperArm(robot, "TestAutonomous", "12-0"));
        //either dont move if your in the right zone otherwise drive forward a specific variable if we aren't already in the right spot
        addState(new DriverState(robot, "TestAutonomous", "13-0"));








    }
}
