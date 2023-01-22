package org.timecrafters.minibots.cyberarm.chiron.states.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class ArmDriverControl extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private Gamepad controller;

    private double lastArmManualControlTime = 0, lastWristManualControlTime = 0;

    private final double gripperOpenConfirmationDelay;
    private double gripperReleaseTriggeredTime = 0;

    public ArmDriverControl(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        this.controller = engine.gamepad1;

        this.gripperOpenConfirmationDelay = robot.tuningConfig("gripper_open_confirmation_delay").value(); // ms
    }

    @Override
    public void exec() {
        armManualControl();
        wristManualControl();

        automatics();
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("Arm Interval", lastArmManualControlTime);
        engine.telemetry.addData("Wrist Interval", lastWristManualControlTime);
    }

    private void armManualControl() {
        if (robot.hardwareFault) {
            return;
        }

        robot.status = Robot.Status.WARNING;

        double stepInterval = robot.tuningConfig("arm_manual_step_interval").value();
        int stepSize = robot.tuningConfig("arm_manual_step_size").value();

        if ((controller.left_trigger > 0 || controller.right_trigger > 0) && runTime() - lastWristManualControlTime >= stepInterval) {
            lastWristManualControlTime = runTime();

            if (controller.left_trigger > 0) { // Arm DOWN
                // robot.arm.setVelocity(5, AngleUnit.DEGREES);
                robot.arm.setTargetPosition(robot.arm.getCurrentPosition() - stepSize);

            } else if (controller.right_trigger > 0) { // Arm UP
                robot.arm.setTargetPosition(robot.arm.getCurrentPosition() + stepSize);
            }
        }

        // FIXME: Detect when the triggers have been released and park arm at the current position
    }

    private void wristManualControl() {
        if (robot.hardwareFault) {
            return;
        }

        double stepInterval = robot.tuningConfig("wrist_manual_step_interval").value();
        double stepSize = robot.tuningConfig("wrist_manual_step_size").value();

        if ((controller.dpad_left || controller.dpad_right) && runTime() - lastArmManualControlTime >= stepInterval) {
            lastArmManualControlTime = runTime();

            if (controller.dpad_left) { // Wrist Left
                robot.wrist.setPosition(robot.wrist.getPosition() - stepSize);

            } else if (controller.dpad_right) { // Wrist Right
                robot.wrist.setPosition(robot.wrist.getPosition() + stepSize);
            }
        }
    }

    private void stopArm() {
        robot.arm.setVelocity(0);
    }

    private void automatics() {
        automaticWrist();

        automaticHardwareMonitor();
    }

    private void automaticWrist() {
        if (robot.wristManuallyControlled) {
            return;
        }

        double angle = robot.tuningConfig("wrist_auto_rotate_angle").value();

        if (robot.ticksToAngle(robot.arm.getCurrentPosition()) >= angle) {
            robot.wrist.setPosition(robot.tuningConfig("wrist_deposit_position").value());
        } else {
            robot.wrist.setPosition(robot.tuningConfig("wrist_collect_position").value());
        }
    }

    private void automaticHardwareMonitor() {
        if (robot.hardwareFault) {
            robot.status = Robot.Status.DANGER;

            stopArm();
        } else {
            robot.arm.setVelocity(
                    robot.angleToTicks(robot.tuningConfig("arm_velocity_in_degrees_per_second").value()));
        }
    }

    private void armPosition(Robot.ArmPosition position) {
        if (robot.hardwareFault) {
            return;
        }

        robot.status = Robot.Status.WARNING;

        switch (position) {
            case COLLECT:
                robot.arm.setTargetPosition(robot.angleToTicks(robot.tuningConfig("arm_position_angle_collect").value()));
                break;

            case GROUND:
                robot.arm.setTargetPosition(robot.angleToTicks(robot.tuningConfig("arm_position_angle_ground").value()));
                break;

            case LOW:
                robot.arm.setTargetPosition(robot.angleToTicks(robot.tuningConfig("arm_position_angle_low").value()));
                break;

            case MEDIUM:
                robot.arm.setTargetPosition(robot.angleToTicks(robot.tuningConfig("arm_position_angle_medium").value()));
                break;

            case HIGH:
                robot.arm.setTargetPosition(robot.angleToTicks(robot.tuningConfig("arm_position_angle_high").value()));
                break;

            default:
                throw new RuntimeException("Unexpected arm position!");
        }
    }

    private void gripperOpen() {
        robot.gripper.setPosition(robot.tuningConfig("gripper_open_position").value());
    }

    private void gripperClosed() {
        robot.gripper.setPosition(robot.tuningConfig("gripper_closed_position").value());
    }

    @Override
    public void buttonDown(Gamepad gamepad, String button) {
        // Swap controlling gamepad
        if (gamepad == engine.gamepad2 && button.equals("guide")) {
            controller = controller == engine.gamepad1 ? engine.gamepad2 : engine.gamepad1;
        }

        if (gamepad != controller) {
            return;
        }

        // Gripper Control
        if (button.equals("left_bumper")) {
            gripperReleaseTriggeredTime = runTime();
        } else if (button.equals("right_bumper")) {
            gripperClosed();
        }

        // Wrist Control
        if (button.equals("dpad_down")) {
            robot.wristManuallyControlled = false;

            robot.wrist.setPosition(robot.tuningConfig("wrist_deposit_position").value());
        } else if (button.equals("dpad_up")) {
            robot.wristManuallyControlled = false;

            robot.wrist.setPosition(robot.tuningConfig("wrist_collect_position").value());
        }

        // Automatic Arm Control
        if (button.equals("a")) {
            armPosition(Robot.ArmPosition.COLLECT);
        } else if (button.equals("x")) {
            armPosition(Robot.ArmPosition.GROUND);
        } else if (button.equals("b")) {
            armPosition(Robot.ArmPosition.LOW);
        } else if (button.equals("y")) {
            armPosition(Robot.ArmPosition.MEDIUM);
        }
    }

    @Override
    public void buttonUp(Gamepad gamepad, String button) {
        if (gamepad != controller) {
            return;
        }

        // Gripper Control - Require confirmation before opening gripper
        if (button.equals("left_bumper") && runTime() - gripperReleaseTriggeredTime >= gripperOpenConfirmationDelay) {
            gripperOpen();
        }
    }
}
