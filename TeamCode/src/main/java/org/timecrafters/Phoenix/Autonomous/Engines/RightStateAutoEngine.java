package org.timecrafters.Phoenix.Autonomous.Engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Phoenix.PhoenixBot1;

@Autonomous(name = "Right Side", group = "A Phoenix", preselectTeleOp = "APhoenixTeleOP")
public class RightStateAutoEngine extends CyberarmEngine {
    PhoenixBot1 robot;

    @Override
    public void loop() {
        super.loop();

        telemetry.addData("BlackBoard Input", blackboardGetString("parkPlace"));
    }

    @Override
    public void setup() {
        robot = new PhoenixBot1(this);
        robot.imu.resetYaw();

        setupFromConfig(
                robot.configuration,
                "org.timecrafters.Autonomous.States",
                robot,
                PhoenixBot1.class,
                "Right State Auto");

////        addState(new TopArmv2(robot, "Right State Auto", "06-0"));
//
////        // forward to low junction ..........................................................................(I have PreLoaded Cone)
////        addState(new DriverStateWithOdometer(robot, "Right State Auto", "02-0"));
//
//        // rotate left towards low junction (angle = 45, direction = CCW) (I have PreLoaded Cone)
//        addState(new RotationState(robot, "Right State Auto", "02-1"));
//
//        // driving forward / backwards to adjust (I have PreLoaded Cone)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "03-0"));
//
//        // counteract distance driven .........................................................................(I'm going for 2nd cone)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "04-0"));
//
//        // rotate towards opposing alliance (angle = 0, direction = CW) (I'm going for 2nd cone)
//        addState(new RotationState(robot, "Right State Auto", "05-0"));
//
//        // drive to tall junction (to adjust to cone stack)  (I'm going for 2nd cone)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "06-0"));
//
//        // rotate at stack (angle = -90, direction = CW) (I'm going for 2nd cone)
//        addState(new RotationState(robot, "Right State Auto", "07-0"));
//
//        // drive at stack (I'm going for 2nd cone)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "08-0"));
//
//
//        // drive away from stack slightly....................................................................... (I have a 2nd cone)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "09-0"));
//
//        //drive away to low (I have a 2nd cone)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "10-0"));
//
//        // rotate at low junction (angle = -135, direction = CW) (I have a 2nd cone)
//        addState(new RotationState(robot, "Right State Auto", "11-0"));
//
//        // driving forward / backwards to adjust (I have a 2nd cone)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "12-0"));
//
//        // counteract distance driven .......................................................................(I'm going for 3rd cone)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "13-0"));
//
//        // rotate at stack (angle = -90, direction = CCW) (I'm going for 3rd cone)
//        addState(new RotationState(robot, "Right State Auto", "14-0"));
//
//        // drive at stack (I'm going for 3rd cone)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "15-0"));
//
//        // drive away from stack slightly................................................................... (I have a 3rd and final cone)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "16-0"));
//
//        //drive away to mid (I have a 3rd and final cone)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "17-0"));
//
//        // rotate at mid junction (angle = -135, direction = CW) (I have a 3rd and final cone)
//        addState(new RotationState(robot, "Right State Auto", "18-0"));
//
//        // driving forward / backwards to adjust (I have a 3rd and final cone)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "19-0"));
//
//        // counteract distance driven.............................................................................. (I'm parking)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "20-0"));
//
//        // rotate at opposing alliance (angle = 0, direction = CCW) (I'm parking)
//        addState(new RotationState(robot, "Right State Auto", "21-0"));
//
//        // Drive back one tile (I'm parking)
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "22-0"));
//
//        // rotate towards stack side (-90 CW) (I'm parking)
//        addState(new RotationState(robot, "Right State Auto", "23-0"));
//
//        // Choose Parking Spot (I'm parking)
//        addState(new PathDecision(robot, "RightSideAutonomous", "24-0"));
//
//        // case 1 drive forward (I'm parking)
//        addState(new DriverParkPlaceState(robot, "RightSideAutonomous", "24-1"));
//
//        // case 2 drive forward (I'm parking)
//        addState(new DriverParkPlaceState(robot, "RightSideAutonomous", "24-2"));
//
//        // case 3 drive forward (I'm parking)
//        addState(new DriverParkPlaceState(robot, "RightSideAutonomous", "24-3"));
//
//        // rotate towards opposing alliance (angle = 0, direction = CCW) (I'm parking)
//        addState(new RotationState(robot, "RightSideAutonomous", "25-0"));










    }
}
