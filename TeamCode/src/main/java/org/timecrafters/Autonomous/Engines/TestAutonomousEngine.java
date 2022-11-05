package org.timecrafters.Autonomous.Engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Autonomous.States.CollectorDistanceState;
import org.timecrafters.Autonomous.States.CollectorState;
import org.timecrafters.Autonomous.States.ConeIdentification;
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
        addState(new ConeIdentification(robot, "TestAutonomous", "00-0"));
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
        //drive towards stack of cones while collecting
        addState(new CollectorDistanceState(robot, "TestAutonomous", "14-0"));
        //drive slightly back
        addState(new DriverState(robot, "TestAutonomous", "15-0"));
        //lift arm up
        addState(new TopArm(robot, "TestAutonomous", "16-0"));
        // drive backwards too position
        addState(new DriverState(robot, "TestAutonomous", "17-0"));
        // rotate
        addState(new RotationState(robot, "TestAutonomous", "18-0"));
        //lift the upper arm
        addState(new TopArm(robot, "TestAutonomous", "19-0"));
        //lift the lower arm
        addState(new BottomArm(robot, "TestAutonomous", "20-0"));
        //drive forward to allign
        addState(new DriverState(robot, "TestAutonomous", "21-0"));
        // bring arm down.
        addState(new TopArm(robot, "TestAutonomous", "22-0"));
        // get rid of cone
        addState(new CollectorState(robot, "TestAutonomous", "23-0"));
        // lift arm up to clear
        addState(new TopArm(robot, "TestAutonomous", "24-0"));
        // drive back
        addState(new DriverState(robot, "TestAutonomous", "25-0"));
        // bring bottom arm down
        addState(new BottomArm(robot, "TestAutonomous", "26-0"));
        // bring top arm down
        addState(new TopArm(robot, "TestAutonomous", "27-0"));
        // rotate towards stack of cones
        addState(new RotationState(robot, "TestAutonomous", "28-0"));
    }

    @Override
    public void loop() {
        super.loop();

        telemetry.addData("BlackBoard Input", blackboard.get("parkPlace"));
    }
}
