package org.timecrafters.Autonomous.TeleOp.engine;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Autonomous.TeleOp.states.PhoenixBot1;
import org.timecrafters.Autonomous.TeleOp.states.SteeringDriveExperiment;

@Disabled
@TeleOp(name = "Steering Drive Test")
public class SterringDriveEngine extends CyberarmEngine {
    public SterringDriveEngine(PhoenixBot1 robot) {
        this.robot = robot;
    }
    PhoenixBot1 robot;
    @Override
    public void setup() {
        robot = new PhoenixBot1(this);
        addState(new SteeringDriveExperiment(robot));
    }
}
