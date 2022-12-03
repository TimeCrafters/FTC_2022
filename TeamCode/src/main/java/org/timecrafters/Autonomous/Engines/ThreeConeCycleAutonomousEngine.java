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
import org.timecrafters.Autonomous.States.ServoCameraRotate;
import org.timecrafters.Autonomous.States.TopArm;
import org.timecrafters.TeleOp.states.PhoenixBot1;

@Autonomous (name = "Right Side")

public class ThreeConeCycleAutonomousEngine extends CyberarmEngine {
    PhoenixBot1 robot;

    @Override
    public void setup() {
        robot = new PhoenixBot1(this);

        // 1 Rotate camera down at the signal
        addState(new ServoCameraRotate(robot, "ThreeConeCycleAutonomousRight", "01-0"));
        // 2 Scan custom image
        addState(new ConeIdentification(robot, "ThreeConeCycleAutonomousRight", "02-0"));
        // 3 Rotate Camera up, out of the way so it doesn't crash into stuff
        addState(new ServoCameraRotate(robot, "ThreeConeCycleAutonomousRight", "03-0"));
        // 4 Drive to the tall Pole (not all the way) while raising upper arm, this will be parallel

        // 5 Turn Towards and look for junction with sensor

        // 6 Raise lower arm while slowly driving at the junction

        // 7 Drop top arm down on the junction to place cone

        // 8 Drop cone as soon as arm is in position

        // 9 Raise arm to clear junction

        // 10 Back up and bring lower arm down (parallel state)

        // 11 Bring upper arm to the correct position for the top cone on stack (check with distance sensor)

        // 12 Rotate towards stack (this might be parallel with last step)

        // 13 Drive at stack while collecting and check to see when we grab it

        // 14 Back up and raise arm (in parallel state)

        // 15 Drive All the way back to the medium Junction and raise upper arm (parallel state)

        // 16 Rotate and use sensor to find junction

        // 17 Drive Towards Junction (This is optional, idk if this is needed atm)

        // 18 Bring upper arm down

        // 19 Drop cone
        addState(new CollectorState(robot, "ThreeConeCycleAutonomousRight", "17-0"));

        // 20 Bring upper arm up

        // 21 Drive away from Junction (this is optional and only used if we use the drive forward from earlier)

        // 22 Drop the Upper arm to the position of the new top cone / 4th cone and check with sensor and start driving fast to get to the stack (this is a parallel state)

        // 23 Drive slower at the stack and run the collector to grab a 2nd cone off of the stack
        addState(new CollectorDistanceState(robot, "ThreeConeCycleAutonomousRight", "21-0"));

        // 24 Drive Back and lift up all the way to position for the low junction (parallel state)

        // 25 Drive back faster after the cone is fully off of the stack

        // 26 Turn and look for the low junction with the distance sensor and align

        // 27 Drive forward / backwards if you need to. (check with distance sensor)

        // 28 Bring Upper arm down on junction

        // 29 Let go of cone right after arm is in position

        // 30 Raise arm as soon as the cone is dropped

        // 31 Back up / go forward (optional, only needed if we drove forwards or backwards to align to low junction

        // 32 Rotate towards Stack of cones

        // 33 Decide which path after scanning image from earlier

        // 34 Drive backwards, forwards, or stay put

        // 35 Rotate towards alliance terminal

    }

    @Override
    public void loop() {
        super.loop();

        telemetry.addData("BlackBoard Input", blackboard.get("parkPlace"));
    }
}
