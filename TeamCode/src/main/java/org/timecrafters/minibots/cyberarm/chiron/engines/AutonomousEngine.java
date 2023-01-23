package org.timecrafters.minibots.cyberarm.chiron.engines;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;
import org.timecrafters.minibots.cyberarm.chiron.Robot;
import org.timecrafters.minibots.cyberarm.chiron.tasks.FieldLocalizer;

public class AutonomousEngine extends CyberarmEngine {
    protected Robot robot;
    protected FieldLocalizer fieldLocalizer;
    protected TimeCraftersConfiguration configuration;

    protected String configurationName = "CHIRON";
    protected String actionsGroupName;
    protected String tuningGroupName;
    protected String tuningActionName;

    @Override
    public void setup() {
        configuration = new TimeCraftersConfiguration(configurationName);

        fieldLocalizer = new FieldLocalizer(
                configuration.variable(tuningGroupName, tuningActionName, "starting_position_x_in_inches").value(),
                configuration.variable(tuningGroupName, tuningActionName, "starting_position_y_in_inches").value()
        );

        robot = new Robot(
                this,
                configuration,
                fieldLocalizer
        );

        robot.imu.resetYaw();

        addTask(fieldLocalizer);

        setupFromConfig(
                robot.getConfiguration(),
                "org.timecrafters.minibots.cyberarm.chiron.states.autonomous",
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
