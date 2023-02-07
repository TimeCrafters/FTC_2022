package org.timecrafters.Autonomous.Engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Autonomous.States.DriverStateWithOdometer;
import org.timecrafters.Autonomous.States.RotationState;
import org.timecrafters.Autonomous.States.TopArmv2;
import org.timecrafters.Autonomous.TeleOp.states.PhoenixBot1;

@Autonomous(name = "Right Side")
public class RightStateAutoEngine extends CyberarmEngine {
    PhoenixBot1 robot;

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

//        addState(new TopArmv2(robot, "Right State Auto", "06-0"));

//        // driving forward to low junction
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "02-0"));
//        // rotate left towards low junction
//        addState(new RotationState(robot, "Right State Auto", "02-1"));
//        // driving forward/ backwards to adjust
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "03-0"));
//        // drive forwards or backwards to adjust to pole
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "03-0"));
//        // counteract distance driven
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "04-0"));
//        // rotate towards opposing alliance
//        addState(new RotationState(robot, "Right State Auto", "04-1"));
//        // drive to stack column
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "05-0"));
//        // rotate at stack
//        addState(new RotationState(robot, "Right State Auto", "05-1"));
//        // drive at stack
//        addState(new DriverStateWithOdometer(robot, "Right State Auto", "06-0"));







    }
}
