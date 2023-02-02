package org.timecrafters.Autonomous.Engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Autonomous.States.CollectorDistanceState;
import org.timecrafters.Autonomous.States.CollectorState;
import org.timecrafters.Autonomous.States.DriverParkPlaceState;
import org.timecrafters.Autonomous.States.DriverState;
import org.timecrafters.Autonomous.States.BottomArm;
import org.timecrafters.Autonomous.States.PathDecision;
import org.timecrafters.Autonomous.States.RotationState;
import org.timecrafters.Autonomous.States.ServoCameraRotate;
import org.timecrafters.Autonomous.States.TopArm;
import org.timecrafters.TeleOp.states.PhoenixBot1;

@Autonomous (name = "Right ")
@Disabled

public class RightSideAutonomousEngine extends CyberarmEngine {
    PhoenixBot1 robot;

    @Override
    public void setup() {
        robot = new PhoenixBot1(this);
        addState(new ServoCameraRotate(robot, "RightSideAutonomous", "00-0"));
//        addState(new ConeIdentification(robot, "RightSideAutonomous", "00-1"));
        addState(new ServoCameraRotate(robot, "RightSideAutonomous", "00-2"));
        //drive to high pole
        addState(new DriverState(robot, "RightSideAutonomous", "01-0"));
        //turn towards high pole
        addState(new RotationState(robot, "RightSideAutonomous", "02-0"));
        // drive back
        addState(new DriverState(robot, "RightSideAutonomous", "02-1"));
        //lift the upper arm
        addState(new TopArm(robot, "RightSideAutonomous", "03-0"));
        //lift the lower arm
        addState(new BottomArm(robot, "RightSideAutonomous", "04-0"));
        //drive forward
        addState(new DriverState(robot, "RightSideAutonomous", "05-0"));
        // drive backward
        addState(new DriverState(robot, "RightSideAutonomous", "05-1"));
        //lower the bottom arm to get closer
        addState(new TopArm(robot, "RightSideAutonomous", "06-0"));
        //make collector release the cone
        addState(new CollectorState(robot, "RightSideAutonomous", "07-0"));
        //lift the lower arm to clear the pole
        addState(new TopArm(robot, "RightSideAutonomous", "08-0"));
        //Drive Backwards
        addState(new DriverState(robot, "RightSideAutonomous", "09-0"));
        // lower the bottom arm so we dont fall over
        addState(new BottomArm(robot, "RightSideAutonomous", "10-0"));
        // lower the upper arm so we dont fall over
        addState(new TopArm(robot, "RightSideAutonomous", "11-0"));;
        // Rotate to either set up for cones to grab or park somewhere
        addState(new RotationState(robot, "RightSideAutonomous", "12-0"));
        //adjust arm height to cone.
        addState(new TopArm(robot, "RightSideAutonomous", "13-0"));
        //drive towards stack of cones while collecting
        addState(new CollectorDistanceState(robot, "RightSideAutonomous", "14-0"));
        //drive slightly back
        addState(new DriverState(robot, "RightSideAutonomous", "15-0"));
        // face to -90
        addState(new RotationState(robot, "RightSideAutonomous", "15-1"));
        //lift arm up
        addState(new TopArm(robot, "RightSideAutonomous", "16-0"));
        // drive backwards too position
        addState(new DriverState(robot, "RightSideAutonomous", "17-0"));
        // rotate
        addState(new RotationState(robot, "RightSideAutonomous", "18-0"));
        // drive slightly towards junction
        addState(new DriverState(robot, "RightSideAutonomous", "18-1"));
        // drive away from junction
        addState(new DriverState(robot, "RightSideAutonomous", "18-2"));
        // bring arm down.
        addState(new TopArm(robot, "RightSideAutonomous", "19-0"));
        // get rid of cone
        addState(new CollectorState(robot, "RightSideAutonomous", "20-0"));
        // lift arm up to clear
        addState(new TopArm(robot, "RightSideAutonomous", "21-0"));
        // rotate towards stack of cones
        addState(new RotationState(robot, "RightSideAutonomous", "22-0"));
        // Choose Parking Spot
        addState(new PathDecision(robot, "RightSideAutonomous", "23-0"));
        // case 1 drive forward
        addState(new DriverParkPlaceState(robot, "RightSideAutonomous", "23-1"));
        // case 2 drive forward
        addState(new DriverParkPlaceState(robot, "RightSideAutonomous", "23-2"));
        // case 3 drive forward
        addState(new DriverParkPlaceState(robot, "RightSideAutonomous", "23-3"));
        // zero out
        addState(new RotationState(robot, "RightSideAutonomous", "24-0"));
        // Top Arm Down
        addState(new TopArm(robot, "RightSideAutonomous", "25-0"));
    }

    @Override
    public void loop() {
        super.loop();

        telemetry.addData("BlackBoard Input", blackboardGetString("parkPlace"));
    }
}
