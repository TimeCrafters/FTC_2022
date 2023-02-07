package org.timecrafters.Autonomous.Engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Autonomous.States.BottomArm;
import org.timecrafters.Autonomous.States.CollectorDistanceState;
import org.timecrafters.Autonomous.States.CollectorState;
import org.timecrafters.Autonomous.States.ConeIdentification;
import org.timecrafters.Autonomous.States.DriverParkPlaceState;
import org.timecrafters.Autonomous.States.DriverStateWithOdometer;
import org.timecrafters.Autonomous.States.JunctionAllignmentAngleState;
import org.timecrafters.Autonomous.States.JunctionAllignmentDistanceState;
import org.timecrafters.Autonomous.States.PathDecision;
import org.timecrafters.Autonomous.States.RotationState;
import org.timecrafters.Autonomous.States.ServoCameraRotate;
import org.timecrafters.Autonomous.States.TopArm;
import org.timecrafters.Autonomous.TeleOp.states.PhoenixBot1;

//@Autonomous (name = "left 2 cone auto")

public class LeftTwoConeAutonomousEngine extends CyberarmEngine {
    PhoenixBot1 robot;

    @Override
    public void setup() {
        robot = new PhoenixBot1(this);

        // 1 Rotate camera down at the signal
        addState(new ServoCameraRotate(robot, "LeftTwoCone", "01-0"));

        // 2 Scan custom image
        addState(new ConeIdentification(robot, "LeftTwoCone", "02-0"));

        // 3 Rotate Camera up, out of the way so it doesn't crash into stuff
        addState(new ServoCameraRotate(robot, "LeftTwoCone", "03-0"));

        // 4 Drive to the tall Pole (not all the way) while raising upper arm, this will be parallel
        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "04-0"));
//        addParallelStateToLastState(new TopArm(robot, "LeftTwoCone", "04-1"));
        addState(new TopArm(robot, "LeftTwoCone", "04-1"));
        // 6 Raise lower arm while slowly driving at the junction (parallel state)
        addState(new BottomArm(robot, "LeftTwoCone", "05-0"));

        // 6 Turn Towards and look for junction with sensor
        addState(new RotationState(robot, "LeftTwoCone", "06-0"));

        // 6-1 drive forward or backwards if needed this is customizable so we can adapt
        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "06-1"));

        // 6-3 align to junction with rotation or skip if it looks like it won't be able to
        addState(new JunctionAllignmentDistanceState(robot, "LeftTwoCone", "06-3"));
        addState(new JunctionAllignmentAngleState(robot, "LeftTwoCone", "06-4"));

        //pause
        addState(new BottomArm(robot, "LeftTwoCone", "06-2"));


        // 7 Drop bottom arm down on the junction to place cone
        addState(new BottomArm(robot, "LeftTwoCone", "07-0"));
        addParallelStateToLastState(new TopArm(robot, "LeftTwoCone", "07-1"));

        // 7-1 drive back slightly
        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "07-2"));

        // 8 Drop cone as soon as arm is in position
        addState(new CollectorState(robot, "LeftTwoCone", "08-0"));
        // 8-1 drive back to lose contact
        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "08-1"));
        // 8-1 realign to old angle
        addState(new RotationState(robot, "LeftTwoCone", "08-2"));

        // 9 Raise bottom arm to clear junction
        addParallelStateToLastState(new BottomArm(robot, "LeftTwoCone", "09-0"));
        addParallelStateToLastState(new TopArm(robot, "LeftTwoCone", "09-1"));

//        // 10 Back up and bring lower arm down (parallel state)
        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "10-0"));
        addParallelStateToLastState(new BottomArm(robot, "LeftTwoCone", "10-1"));

        // 11 Rotate towards stack
        addState(new RotationState(robot, "LeftTwoCone", "11-0"));
//
        // 12 Bring upper arm to the correct position for the top cone on stack (check with distance sensor)
        addState(new TopArm(robot, "LeftTwoCone", "12-0"));

        // drive forward at the stack without sensor
        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "12-1"));

        // turn and align at stack
        addState(new RotationState(robot, "LeftTwoCone", "12-2"));

//
        // 13 Drive at stack while collecting and check to see when we grab it
        addState(new CollectorDistanceState(robot, "LeftTwoCone", "13-0"));


//
//        // 14 Back up and raise arm
        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "14-0"));
        addState(new TopArm(robot, "LeftTwoCone", "14-1"));

        // 14-2 align perpendicular too the wall
        addState(new RotationState(robot, "LeftTwoCone", "14-2"));
//
        // 15 Drive All the way back to the tall Junction and raise upper arm (parallel state)
        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "15-0"));
//
        // 16 Rotate and use sensor to find junction
        addState(new RotationState(robot, "LeftTwoCone", "16-0"));
//
        // 17 Drive Towards Junction (This is optional, idk if this is needed atm)
        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "17-0"));

//        // 18 Bring upper arm down
        addState(new TopArm(robot, "LeftTwoCone", "18-0"));
//
//        // 19 Drop cone
        addState(new CollectorState(robot, "LeftTwoCone", "19-0"));

        addParallelStateToLastState(new TopArm(robot, "LeftTwoCone", "19-1"));

//
//        // 20 Bring upper arm up
//        addState(new TopArm(robot, "LeftTwoCone", "20-0"));
//
//        // 21 Drive away from Junction (this is optional and only used if we use the drive forward from earlier)
        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "21-0"));
//
//        // 22 Drop the Upper arm to the position of the new top cone / 4th cone and check with sensor and start driving fast to get to the stack (this is a parallel state)
//        addState(new TopArm(robot, "LeftTwoCone", "22-0"));
//
//        // 23 Drive slower at the stack and run the collector to grab a 2nd cone off of the stack
//        addState(new CollectorDistanceState(robot, "LeftTwoCone", "23-0"));
//
//        // 24 Drive Back and lift up all the way to position for the low junction
//        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "24-0"));
//        addState(new TopArm(robot, "LeftTwoCone", "24-1"));
//
//        // 25 Drive back faster after the cone is fully off of the stack
//        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "25-0"));
//
//        // 26 Turn and look for the low junction with the distance sensor and align
//        addState(new RotationState(robot, "LeftTwoCone", "26-0"));
//
//        // 27 Drive forward / backwards if you need to. (check with distance sensor)
//        addState(new JunctionAllignmentState(robot, "LeftTwoCone", "26-1"));
//
//        // 28 Bring Upper arm down on junction
//        addState(new TopArm(robot, "LeftTwoCone", "28-0"));
//
//        // 29 Let go of cone right after arm is in position
//        addState(new CollectorState(robot, "LeftTwoCone", "29-0"));
//
//        // 30 Raise arm as soon as the cone is dropped
//        addState(new TopArm(robot, "LeftTwoCone", "30-0"));
//
//        // 31 Back up / go forward (optional, only needed if we drove forwards or backwards to align to low junction
//        addState(new DriverStateWithOdometer(robot, "LeftTwoCone", "31-0"));
//
//        // 32 Rotate towards Stack of cones
        addState(new RotationState(robot, "LeftTwoCone", "32-0"));
//
//        // 33 Decide which path after scanning image from earlier
        addState(new PathDecision(robot, "LeftTwoCone", "33-0"));
//
//        // 34 Drive backwards, forwards, or stay put
        addState(new DriverParkPlaceState(robot, "LeftTwoCone", "34-1"));
        addState(new DriverParkPlaceState(robot, "LeftTwoCone", "34-2"));
        addState(new DriverParkPlaceState(robot, "LeftTwoCone", "34-3"));
//
//        // 35 Rotate towards alliance terminal
        addState(new RotationState(robot, "LeftTwoCone", "35-0"));

        addState(new TopArm(robot, "RightTwoCone", "36-0"));


    }

    @Override
    public void loop() {
        super.loop();

        telemetry.addData("BlackBoard Input", blackboardGetString("parkPlace"));
    }
}
