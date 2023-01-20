package org.timecrafters.minibots.cyberarm.chiron.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

@TeleOp(name = "CHIRON | Diagnostics", group = "CHIRON")
public class ConfigureAndTestHardwareEngine extends CyberarmEngine {
    private enum Stage {
        ACKNOWLEDGE,
        ENCODER,
        DIRECTION,
        COMPLETE
    }

    private Robot robot;

    @Override
    public void setup() {
        robot = new Robot(this, new TimeCraftersConfiguration("CHIRON"));

        addState(new MotorSetupState("Left Drive", robot.leftDrive, "left_drive_direction_forward", robot));
        addState(new MotorSetupState("Right Drive", robot.rightDrive, "right_drive_direction_forward", robot));

        addState(new MotorSetupState("Front Drive", robot.frontDrive, "front_drive_direction_forward", robot));
        addState(new MotorSetupState("Back Drive", robot.backDrive, "back_drive_direction_forward", robot));

        addState(new MotorSetupState("Lift Drive", robot.arm, "lift_drive_direction_forward", robot));
    }

    @Override
    public void loop() {
        super.loop();

        robot.standardTelemetry();
    }

    private class MotorSetupState extends CyberarmState {
        private final DcMotorEx motor;
        private final String name, direction_key;

        private Stage stage = Stage.ACKNOWLEDGE;

        private final GamepadChecker controller;
        private String message = "???";

        public MotorSetupState(String name, DcMotorEx motor, String direction_key, Robot robot) {
            this.name = name;
            this.motor = motor;
            this.direction_key = direction_key;

            controller = new GamepadChecker(engine, engine.gamepad1);

            this.motor.setPower(0.0);
            this.motor.setDirection(robot.hardwareConfig(direction_key).value() ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        }

        @Override
        public void exec() {
            switch (stage) {
                case ACKNOWLEDGE:
                    handleAcknowledgement();
                    break;
                case ENCODER:
                    handleEncoder();
                    break;
                case DIRECTION:
                    handleDirection();
                    break;
                default:
                    setHasFinished(true);
            }

            controller.update();
        }

        @Override
        public void telemetry() {
            engine.telemetry.addLine();
            engine.telemetry.addLine("Configuring " + name);
            engine.telemetry.addLine(message);
            engine.telemetry.addLine();
            engine.telemetry.addData("DIRECTION KEY", direction_key);
            engine.telemetry.addData("STAGE", stage);
        }

        private void handleAcknowledgement() {
            message = "Press 'A' if it is safe to drive: " + name;
        }

        private void handleEncoder() {
            if (motor.getPower() == 0) {
                message = "Press 'A' if motor has an encoder.\nPress 'X' if there is no encoder.";
            } else {
                message = "Press 'A' if encoder position is changing.";
            }
        }

        private void handleDirection() {
            message = "Press 'A' to complete. Press 'X' to toggle direction.";

            motor.setPower(0.25);
        }

        // INPUT
        private void handleAcknowledgementInput(String button) {
            if (button.equals("a")) {
                stage = Stage.ENCODER;
            }
        }

        private void handleEncoderInput(String button) {
            if (button.equals("a")) {
                if (motor.getPower() == 0) {
                    motor.setPower(0.25);
                } else {
                    motor.setPower(0);
                    stage = Stage.DIRECTION;
                }
            }

            if (button.equals("x")) {
                stage = Stage.DIRECTION;
            }
        }

        private void handleDirectionInput(String button) {
            if (button.equals("a")) {
                motor.setPower(0.0);
                stage = Stage.COMPLETE;
            }

            if (button.equals("x")) {
                if (motor.getDirection() == DcMotorSimple.Direction.FORWARD) {
                    motor.setDirection(DcMotorSimple.Direction.REVERSE);
                } else {
                    motor.setDirection(DcMotorSimple.Direction.FORWARD);
                }
            }
        }

        @Override
        public void buttonDown(Gamepad gamepad, String button) {
            switch (stage) {
                case ACKNOWLEDGE:
                    handleAcknowledgementInput(button);
                    break;
                case ENCODER:
                    handleEncoderInput(button);
                    break;
                case DIRECTION:
                    handleDirectionInput(button);
                    break;
            }
        }
    }
}
