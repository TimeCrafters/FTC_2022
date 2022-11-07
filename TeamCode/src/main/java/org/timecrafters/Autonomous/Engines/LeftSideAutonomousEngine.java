package org.timecrafters.Autonomous.Engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Autonomous.States.BottomArm;
import org.timecrafters.Autonomous.States.CollectorDistanceState;
import org.timecrafters.Autonomous.States.CollectorState;
import org.timecrafters.Autonomous.States.ConeIdentification;
import org.timecrafters.Autonomous.States.DriverParkPlaceState;
import org.timecrafters.Autonomous.States.DriverState;
import org.timecrafters.Autonomous.States.PathDecision;
import org.timecrafters.Autonomous.States.RotationState;
import org.timecrafters.Autonomous.States.TopArm;
import org.timecrafters.testing.states.PrototypeBot1;

@Autonomous (name = "Left Side")

public class LeftSideAutonomousEngine extends CyberarmEngine {
    PrototypeBot1 robot;

    @Override
    public void setup() {
        robot = new PrototypeBot1(this);
        addState(new ConeIdentification(robot, "LeftSideAutonomous", "00-0"));
        //drive to high pole
        addState(new DriverState(robot, "LeftSideAutonomous", "01-0"));
        //turn towards high pole
        addState(new RotationState(robot, "LeftSideAutonomous", "02-0"));
        //lift the upper arm
        addState(new TopArm(robot, "LeftSideAutonomous", "03-0"));
        //lift the lower arm
        addState(new BottomArm(robot, "LeftSideAutonomous", "04-0"));
        //drive forward
        addState(new DriverState(robot, "LeftSideAutonomous", "05-0"));
        //lower the bottom arm to get closer
        addState(new TopArm(robot, "LeftSideAutonomous", "06-0"));
        //make collector release the cone
        addState(new CollectorState(robot, "LeftSideAutonomous", "07-0"));
        //lift the lower arm to clear the pole
        addState(new TopArm(robot, "LeftSideAutonomous", "08-0"));
        //Drive Backwards
        addState(new DriverState(robot, "LeftSideAutonomous", "09-0"));
        // Rotate to either set up for cones to grab or park somewhere
        addState(new RotationState(robot, "LeftSideAutonomous", "12-0"));
        // lower the bottom arm so we dont fall over
        addState(new BottomArm(robot, "LeftSideAutonomous", "10-0"));
        // lower the upper arm so we dont fall over
        addState(new TopArm(robot, "LeftSideAutonomous", "11-0"));;
        //adjust arm height to cone.
        addState(new TopArm(robot, "LeftSideAutonomous", "13-0"));
        //drive towards stack of cones while collecting
        addState(new CollectorDistanceState(robot, "LeftSideAutonomous", "14-0"));
        //drive slightly back
        addState(new DriverState(robot, "LeftSideAutonomous", "15-0"));
        // face to -90
        addState(new RotationState(robot, "LeftSideAutonomous", "15-1"));
        //lift arm up
        addState(new TopArm(robot, "LeftSideAutonomous", "16-0"));
        // drive backwards too position
        addState(new DriverState(robot, "LeftSideAutonomous", "17-0"));
        // rotate
        addState(new RotationState(robot, "LeftSideAutonomous", "18-0"));

        addState(new DriverState(robot, "LeftSideAutonomous", "21-0"));
        // bring arm down.
        addState(new TopArm(robot, "LeftSideAutonomous", "22-0"));
        // get rid of cone
        addState(new CollectorState(robot, "LeftSideAutonomous", "23-0"));
        // lift arm up to clear
        addState(new TopArm(robot, "LeftSideAutonomous", "24-0"));
        // rotate towards stack of cones
        addState(new RotationState(robot, "LeftSideAutonomous", "28-0"));
        // Choose Parking Spot
        addState(new PathDecision(robot, "LeftSideAutonomous", "29-0"));
        // case 1 drive forward
        addState(new DriverParkPlaceState(robot, "LeftSideAutonomous", "29-1"));
        // case 2 drive forward
        addState(new DriverParkPlaceState(robot, "LeftSideAutonomous", "29-2"));
        // case 3 drive forward
        addState(new DriverParkPlaceState(robot, "LeftSideAutonomous", "29-3"));
        // zero out
        addState(new RotationState(robot, "LeftSideAutonomous", "30-0"));
        // Top Arm Down
        addState(new TopArm(robot, "LeftSideAutonomous", "28-1"));
        //Drive Backwards
        addState(new DriverState(robot, "LeftSideAutonomous", "30-1"));
    }

    @Override
    public void loop() {
        super.loop();

        telemetry.addData("BlackBoard Input", blackboard.get("parkPlace"));
    }
}
