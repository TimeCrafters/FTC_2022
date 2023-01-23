package org.timecrafters.TeleOp.engine;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.cyberarm.engine.V2.GamepadChecker;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;
import org.timecrafters.TeleOp.states.PhoenixBot1;
import org.timecrafters.TeleOp.states.TeleOPSpeedrunState;

@Disabled
@TeleOp (name = "Speedrun Engine")
public class SpeedrunEngine extends CyberarmEngine {
    GamepadChecker gamepadChecker;
    PhoenixBot1 robot;

    @Override
    public void setup() {
        TimeCraftersConfiguration configuration = new TimeCraftersConfiguration();
        robot = new PhoenixBot1(this);

        gamepadChecker = new GamepadChecker(this, this.gamepad1);

//        setupFromConfig(
//                configuration,
//                "org.timecrafters.testing.states",
//                robot,
//                PhoenixBot1.class,
//                "SpeedRun"
//        );

//        addState(new TeleOPSpeedrunState(robot, "SpeedRun", "00-0"));
//        addState(new TeleOPSpeedrunState(robot, "SpeedRun", "01-0"));
    }

    @Override
    public void loop() {
        super.loop();

        gamepadChecker.update();
    }

    @Override
    protected void buttonUp(Gamepad gamepad, String button) {
        if (gamepad1 == gamepad) {
            if (button.equals("a")) {
                addState(new TeleOPSpeedrunState(robot, "SpeedRun", "00-0"));
            } else if (button.equals("y")) {
                addState(new TeleOPSpeedrunState(robot, "SpeedRun", "01-0"));
            }
        }
    }
}
