package org.timecrafters.minibots.cyberarm.states;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.MecanumMinibot;

public class MecanumMinibotTeleOpState extends CyberarmState {
    private final MecanumMinibot robot;
    private float speed;

    public MecanumMinibotTeleOpState(MecanumMinibot robot) {
        this.robot = robot;
    }

    @Override
    public void exec() {
        speed = 1.0f - engine.gamepad1.left_trigger;

        if (engine.gamepad1.y) {
            robot.driveAll(speed);
        } else if (engine.gamepad1.a) {
            robot.driveAll(-speed);
        } else if (engine.gamepad1.x) {
            robot.driveStrafe(MecanumMinibot.STRAFE_LEFT, speed);
        } else if (engine.gamepad1.b) {
            robot.driveStrafe(MecanumMinibot.STRAFE_RIGHT, speed);

        } else if (engine.gamepad1.left_bumper) {
            robot.driveTurn(MecanumMinibot.TURN_LEFT, speed);

        } else if (engine.gamepad1.right_bumper) {
            robot.driveTurn(MecanumMinibot.TURN_RIGHT, speed);
        } else {
            robot.driveStop();
        }
    }

    @Override
    public void buttonDown(Gamepad gamepad, String button) {
        super.buttonDown(gamepad, button);
    }

    @Override
    public void buttonUp(Gamepad gamepad, String button) {
        super.buttonUp(gamepad, button);
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("speed", speed);
    }
}
