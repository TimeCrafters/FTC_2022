package org.timecrafters.Autonomous.Engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Autonomous.States.BottomArm;
import org.timecrafters.Autonomous.States.CollectorDistanceState;
import org.timecrafters.Autonomous.States.CollectorState;
import org.timecrafters.Autonomous.States.ConeIdentification;
import org.timecrafters.Autonomous.States.DriverParkPlaceState;
import org.timecrafters.Autonomous.States.DriverStateWithOdometer;
import org.timecrafters.Autonomous.States.PathDecision;
import org.timecrafters.Autonomous.States.RotationState;
import org.timecrafters.Autonomous.States.ServoCameraRotate;
import org.timecrafters.Autonomous.States.TopArm;
import org.timecrafters.TeleOp.states.PhoenixBot1;

@Autonomous(name = "Right Side")
public class RightStateAutoEngine extends CyberarmEngine {
    PhoenixBot1 robot;

    @Override
    public void setup() {
        robot = new PhoenixBot1(this);

        // driving towards Low
        addState(new DriverStateWithOdometer(robot, "Right State Auto", "02-0"));
        // rotate towards low
        addState(new RotationState(robot, "Right State Auto", "02-1"));
        // drive forwards or backwards to adjust to pole
        addState(new DriverStateWithOdometer(robot, "Right State Auto", "03-0"));
        // counteract distance driven
        addState(new DriverStateWithOdometer(robot, "Right State Auto", "04-0"));
        // rotate towards opposing alliance
        addState(new RotationState(robot, "Right State Auto", "04-1"));
        // drive to stack column
        addState(new DriverStateWithOdometer(robot, "Right State Auto", "05-0"));
        // rotate at stack
        addState(new RotationState(robot, "Right State Auto", "05-1"));
        // drive at stack
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "06-0"));







    }
}
