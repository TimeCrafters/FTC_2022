package org.timecrafters.minibots.cyberarm.chiron.engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;
import org.timecrafters.minibots.cyberarm.chiron.Robot;
import org.timecrafters.minibots.cyberarm.chiron.tasks.FieldLocalizer;

@Autonomous(name = "CHIRON | RED Left Side", group = "CHIRON", preselectTeleOp = "CHIRON | TeleOp")
public class AutonomousRedLeftSideEngine extends CyberarmEngine {
    private Robot robot;
    private FieldLocalizer fieldLocalizer;
    private TimeCraftersConfiguration configuration;

    @Override
    public void setup() {
        configuration = new TimeCraftersConfiguration("CHIRON");

        fieldLocalizer = new FieldLocalizer(
                configuration.variable("Autonomous", "Tuning_Red_LeftSide", "starting_position_x_in_inches").value(),
                configuration.variable("Autonomous", "Tuning_Red_LeftSide", "starting_position_y_in_inches").value()
        );

        robot = new Robot(
                this,
                configuration,
                fieldLocalizer
        );

        addTask(fieldLocalizer);

        setupFromConfig(
                robot.getConfiguration(),
                "org.timecrafters.minibots.cyberarm.chiron.states.autonomous",
                robot,
                Robot.class,
                "AutonomousRedLeftSide");
    }
}
