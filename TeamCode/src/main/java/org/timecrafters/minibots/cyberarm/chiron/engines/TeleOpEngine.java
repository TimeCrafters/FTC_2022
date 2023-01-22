package org.timecrafters.minibots.cyberarm.chiron.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;
import org.timecrafters.minibots.cyberarm.chiron.Robot;
import org.timecrafters.minibots.cyberarm.chiron.states.teleop.DrivetrainDriverControl;
import org.timecrafters.minibots.cyberarm.chiron.tasks.FieldLocalizer;

@TeleOp(name = "CHIRON | TeleOp", group = "CHIRON")
public class TeleOpEngine extends CyberarmEngine {
    private Robot robot;
    @Override
    public void setup() {
        this.robot = new Robot(this, new TimeCraftersConfiguration("CHIRON"), new FieldLocalizer());

        addTask(robot.getFieldLocalizer());

        setupFromConfig(
                robot.getConfiguration(),
                "org.timecrafters.minibots.cyberarm.chiron.states.teleop",
                robot,
                Robot.class,
                "TeleOpStates");
    }

    @Override
    public void loop() {
        super.loop();

        robot.standardTelemetry();
    }
}
