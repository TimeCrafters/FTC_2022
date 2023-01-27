package org.timecrafters.minibots.cyberarm.chiron.states.teleop;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class ArmDriverControl extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private Gamepad controller;

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
    }

    private void armManualControl() {
        if (robot.hardwareFault) {
            return;
        }

        double armVelocity = robot.tuningConfig("arm_velocity_in_degrees_per_second").value();
        double armManualPower = robot.tuningConfig("arm_manual_power").value();
        double armAutomaticPower = robot.tuningConfig("arm_automatic_power").value();

        if ((controller.left_trigger > 0 || controller.right_trigger > 0)) {
            robot.armManuallyControlled = true;

            robot.reportStatus(Robot.Status.WARNING);

            robot.arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            if (controller.left_trigger > 0) { // Arm DOWN
                robot.arm.setPower(-armManualPower * Math.sqrt(controller.left_trigger));

            } else if (controller.right_trigger > 0) { // Arm UP
                robot.arm.setPower(armManualPower * Math.sqrt(controller.right_trigger));
            }
        }

        if (robot.armManuallyControlled && controller.left_trigger == 0 && controller.right_trigger == 0) {
            robot.armManuallyControlled = false;

            robot.arm.setPower(armAutomaticPower);

            robot.arm.setTargetPosition(robot.arm.getCurrentPosition());

            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    private void wristManualControl() {
        if (robot.hardwareFault) {
            return;
        }

        double stepPower = robot.tuningConfig("wrist_manual_step_power").value();

        if ((controller.dpad_left || controller.dpad_right)) {
            robot.wristManuallyControlled = true;

            if (controller.dpad_left) { // Wrist Left
                robot.wrist.setPower(stepPower);
            }
            if (controller.dpad_right) { // Wrist Right
                robot.wrist.setPower(-stepPower);
            }
        }

        if (robot.wristManuallyControlled && !controller.dpad_left && !controller.dpad_right) {
            robot.wrist.setPower(0);
        }
    }

    private void stopArm() {
        robot.arm.setVelocity(0);
        robot.arm.setPower(0);

        // RUN_TO_POSITION seems to override power we request
        robot.arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void automatics() {
        if (!robot.hardwareFault) {
//            automaticWrist();
            automaticArmVelocity();
        }

        automaticHardwareMonitor();
    }

    private void automaticWrist() {
        if (robot.wristManuallyControlled) {
            return;
        }

        double angle = robot.tuningConfig("wrist_auto_rotate_angle").value();

        if (robot.ticksToAngle(robot.arm.getCurrentPosition()) >= angle) {
            robot.wrist.setPower(robot.tuningConfig("wrist_up_power").value());
        } else {
            robot.wrist.setPower(robot.tuningConfig("wrist_down_power").value());
        }
    }

    private void automaticArmVelocity() {
        if (robot.armManuallyControlled) {
            return;
        }

        robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // robot.controlMotorPIDF(
        //        robot.arm,
        //        "Arm",
        //        robot.angleToTicks(robot.tuningConfig("arm_velocity_in_degrees_per_second").value()),
        //        12.0 / robot.getVoltage());

        robot.controlArmMotor(
                robot.angleToTicks(robot.tuningConfig("arm_velocity_in_degrees_per_second").value())
        );
    }

    private void automaticHardwareMonitor() {
        if (robot.hardwareFault) {
            robot.reportStatus(Robot.Status.DANGER);

            stopArm();
        }
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
        if (button.equals("right_bumper")) {
            robot.gripperClosed();
        } else if (button.equals("left_bumper")) {
            robot.gripperOpen();
        }

        // Wrist Control
        if (button.equals("dpad_up")) {
            robot.wristPosition(Robot.WristPosition.UP);
        }

        if (button.equals("dpad_down")) {
            robot.wristPosition(Robot.WristPosition.DOWN);
        }

        // Automatic Arm Control
        switch (button) {
            case "a":
                robot.armPosition(Robot.ArmPosition.COLLECT);
                break;
            case "x":
                robot.armPosition(Robot.ArmPosition.GROUND);
                break;
            case "b":
                robot.armPosition(Robot.ArmPosition.LOW);
                break;
            case "y":
                robot.armPosition(Robot.ArmPosition.MEDIUM);
                break;
        }
    }

    @Override
    public void buttonUp(Gamepad gamepad, String button) {
        if (gamepad != controller) {
            return;
        }
    }
}
