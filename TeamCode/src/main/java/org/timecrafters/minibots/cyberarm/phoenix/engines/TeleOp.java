package org.timecrafters.minibots.cyberarm.phoenix.engines;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;
import org.timecrafters.minibots.cyberarm.phoenix.Robot;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Mentor Phoenix | TeleOp", group = "Mentor Phoenix")
public class TeleOp extends CyberarmEngine {
    private Robot robot;
    private TimeCraftersConfiguration configuration;
    final private String configurationName = "MentorPhoenix", actionsGroupName = "TeleOpStates";

    @Override
    public void setup() {
        configuration = new TimeCraftersConfiguration(configurationName);

        robot = new Robot(
                this,
                configuration
        );

        robot.imu.resetYaw();

        setupFromConfig(
                robot.getConfiguration(),
                "org.timecrafters.minibots.cyberarm.phoenix.states.teleop",
                robot,
                Robot.class,
                actionsGroupName);
    }

    @Override
    public void loop() {
        robot.update();

        super.loop();

        robot.standardTelemetry();
    }

    @Override
    public void stop() {
        robot.stop();

        super.stop();
    }
}
