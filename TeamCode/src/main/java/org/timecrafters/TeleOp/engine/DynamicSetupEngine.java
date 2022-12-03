package org.timecrafters.TeleOp.engine;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;

@TeleOp(name = "DynamicSetupEngine")
public class DynamicSetupEngine extends CyberarmEngine {
    public class Robot {
        public TimeCraftersConfiguration configuration;

        public Robot() {
            configuration = new TimeCraftersConfiguration();
        }
    }

    @Override
    public void setup() {
        this.showStateChildrenListInTelemetry = true;
        Robot robot = new Robot();

        setupFromConfig(
            robot.configuration,
            "org.timecrafters.testing.states",
            robot,
            Robot.class,
            "LeftAutonomous"
        );
    }
}
