package org.timecrafters.Phoenix.TeleOp.engine;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Phoenix.TeleOp.states.SteeringDriveExperiment;
import org.timecrafters.Phoenix.PhoenixBot1;

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
