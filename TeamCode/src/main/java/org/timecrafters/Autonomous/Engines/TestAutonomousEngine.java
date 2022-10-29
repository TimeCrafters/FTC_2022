package org.timecrafters.Autonomous.Engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Autonomous.States.CollectorDistanceState;
import org.timecrafters.Autonomous.States.CollectorState;
import org.timecrafters.Autonomous.States.DriverState;
import org.timecrafters.Autonomous.States.BottomArm;
import org.timecrafters.Autonomous.States.RotationState;
import org.timecrafters.Autonomous.States.TopArm;
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
        addState(new TopArm(robot, "TestAutonomous", "03-0"));
        //lift the lower arm
        addState(new BottomArm(robot, "TestAutonomous", "04-0"));
        //drive forward
        addState(new DriverState(robot, "TestAutonomous", "05-0"));
        //lower the bottom arm to get closer
        addState(new TopArm(robot, "TestAutonomous", "06-0"));
        //make collector release the cone
        addState(new CollectorState(robot, "TestAutonomous", "07-0"));
        //lift the lower arm to clear the pole
        addState(new TopArm(robot, "TestAutonomous", "08-0"));
        //Drive Backwards
        addState(new DriverState(robot, "TestAutonomous", "09-0"));
        // lower the bottom arm so we dont fall over
        addState(new BottomArm(robot, "TestAutonomous", "10-0"));
        // lower the upper arm so we dont fall over
        addState(new TopArm(robot, "TestAutonomous", "11-0"));;
        // Rotate to either set up for cones to grab or park somewhere
        addState(new RotationState(robot, "TestAutonomous", "12-0"));
        //adjust arm height to cone.
        addState(new TopArm(robot, "TestAutonomous", "13-0"));
        //drive towards stack of cones
        addState(new CollectorDistanceState(robot, "", ""));
//        DriverState driverState = new DriverState(robot, "TestAutonomous", "14-0");
//        addState(driverState);
//        //collect next cone
//        driverState.addParallelState(new CollectorState(robot, "TestAutonomous", "15-0"));









    }
}
