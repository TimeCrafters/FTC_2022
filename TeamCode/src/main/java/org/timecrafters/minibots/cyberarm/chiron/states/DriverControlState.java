package org.timecrafters.minibots.cyberarm.chiron.states;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class DriverControlState extends CyberarmState {
    private final Robot robot;
    private final GamepadChecker controller;
    private final double releaseConfirmationDelay;
    private double lastArmManualControlTime = 0, lastWristManualControlTime = 0, lastLEDStatusAnimationTime = 0;
    private boolean LEDStatusToggle = false;
    private boolean fieldCentricControl = true;

    public DriverControlState(Robot robot) {
        this.robot = robot;
        this.controller = new GamepadChecker(engine, engine.gamepad1);

        this.releaseConfirmationDelay = robot.tuningConfig("cone_release_confirmation_delay").value(); // ms
    }

    @Override
    public void exec() {
        robot.status = Robot.Status.OKAY;

        move();
        armManualControl();
        wristManualControl();

        automatics();

        controller.update();
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("Run Time", runTime());
        engine.telemetry.addData("Arm Interval", lastArmManualControlTime);
        engine.telemetry.addData("Wrist Interval", lastWristManualControlTime);
        engine.telemetry.addData("LED Status Interval", lastLEDStatusAnimationTime);
        engine.telemetry.addData("Field Centric Control", fieldCentricControl);
    }

    // FIXME: replace .setPower with .setVelocity
    // REF: https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html
    private void move() {
        if (robot.automaticAntiTipActive || robot.hardwareFault) {
            return;
        }

        double y = -engine.gamepad1.left_stick_y;
        double x = engine.gamepad1.left_stick_x * 1.1;
        double rx = engine.gamepad1.right_stick_x;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        double frontLeftPower = 0, frontRightPower = 0, backLeftPower = 0 , backRightPower = 0;

        if (fieldCentricControl) {
            double heading = -robot.heading();
            double rotX = x * Math.cos(heading) - y * Math.sin(heading);
            double rotY = x * Math.sin(heading) + y * Math.cos(heading);

            frontLeftPower = (rotY + rotX + rx) / denominator;
            backLeftPower = (rotY - rotX + rx) / denominator;
            frontRightPower = (rotY - rotX - rx) / denominator;
            backRightPower = (rotY + rotX - rx) / denominator;

        } else {
            frontLeftPower = (y + x + rx) / denominator;
            backLeftPower = (y - x + rx) / denominator;
            frontRightPower = (y - x - rx) / denominator;
            backRightPower = (y + x - rx) / denominator;
        }

        robot.frontLeftDrive.setPower(frontLeftPower);
        robot.frontRightDrive.setPower(frontRightPower);

        robot.backLeftDrive.setPower(backLeftPower);
        robot.backRightDrive.setPower(backRightPower);
    }

    private void stopDrive() {
        robot.backLeftDrive.setPower(0);
        robot.frontRightDrive.setPower(0);

        robot.frontLeftDrive.setPower(0);
        robot.backRightDrive.setPower(0);
    }

    private void armManualControl() {
        if (robot.hardwareFault) {
            return;
        }

        robot.status = Robot.Status.WARNING;

        double stepInterval = robot.tuningConfig("arm_manual_step_interval").value();
        int stepSize = robot.tuningConfig("arm_manual_step_size").value();

        if ((engine.gamepad1.left_trigger > 0 || engine.gamepad1.right_trigger > 0) && runTime() - lastWristManualControlTime >= stepInterval) {
            lastWristManualControlTime = runTime();

            if (engine.gamepad1.left_trigger > 0) { // Arm DOWN
                // robot.arm.setVelocity(5, AngleUnit.DEGREES);
                robot.arm.setTargetPosition(robot.arm.getCurrentPosition() - stepSize);

            } else if (engine.gamepad1.right_trigger > 0) { // Arm UP
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

        if ((engine.gamepad1.dpad_left || engine.gamepad1.dpad_right) && runTime() - lastArmManualControlTime >= stepInterval) {
            lastArmManualControlTime = runTime();

            if (engine.gamepad1.dpad_left) { // Wrist Left
                robot.wrist.setPosition(robot.wrist.getPosition() - stepSize);

            } else if (engine.gamepad1.dpad_right) { // Wrist Right
                robot.wrist.setPosition(robot.wrist.getPosition() + stepSize);
            }
        }
    }

    private void stopArm() {
        robot.arm.setPower(0);
    }

    private void automatics() {
        automaticWrist();
        automaticAntiTip(); // NO OP
        automaticHardwareMonitor();

        automaticLEDStatus();
    }

    private void automaticWrist() {
        if (robot.wristManuallyControlled) {
            return;
        }

        if (robot.ticksToAngle(robot.arm.getCurrentPosition()) >= 50) {
            robot.wrist.setPosition(robot.tuningConfig("wrist_deposit_position").value());
        } else {
            robot.wrist.setPosition(robot.tuningConfig("wrist_initial_position").value());
        }
    }

    // NO-OP
    private void automaticAntiTip() {
        // TODO: Take over drivetrain if robot starts to tip past a preconfigured point
        //       return control if past point of no-return or tipping is no longer a concern

        // TODO: Calculate motion inverse to the normal of current direction
    }

    private void automaticHardwareMonitor() {
        // Check that encoders are updating as expect, etc.

        // NOTE: Robot should prevent/halt all movement in the event of a fault
        // robot.hardwareFault = true;

        if (robot.hardwareFault) {
            robot.status = Robot.Status.DANGER;

            stopDrive();
            stopArm();
        }
    }

    private void automaticLEDStatus() {
        switch (robot.status) {
            case OKAY:
                robot.indicatorA.enableLed(false);
                robot.indicatorB.enableLed(false);
                break;

            case MONITORING:
                robot.indicatorA.enableLed(true);
                robot.indicatorB.enableLed(true);
                break;

            case WARNING:
                if (runTime() - lastLEDStatusAnimationTime >= 500){
                    lastLEDStatusAnimationTime = runTime();
                    LEDStatusToggle = !LEDStatusToggle;

                    robot.indicatorA.enableLed(LEDStatusToggle);
                    robot.indicatorA.enableLed(!LEDStatusToggle);
                }
                break;

            case DANGER:
                if (runTime() - lastLEDStatusAnimationTime >= 200){
                    lastLEDStatusAnimationTime = runTime();
                    LEDStatusToggle = !LEDStatusToggle;

                    robot.indicatorA.enableLed(LEDStatusToggle);
                    robot.indicatorA.enableLed(LEDStatusToggle);
                }
                break;
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
        if (gamepad != engine.gamepad1) {
            return;
        }

        // Gripper Control
        if (button.equals("left_bumper")) {
            gripperOpen();
        } else if (button.equals("right_bumper")) {
            gripperClosed();
        }

        // Wrist Control
        if (button.equals("dpad_down")) {
            robot.wristManuallyControlled = false;

            robot.wrist.setPosition(robot.tuningConfig("wrist_deposit_position").value());
        } else if (button.equals("dpad_up")) {
            robot.wristManuallyControlled = false;

            robot.wrist.setPosition(robot.tuningConfig("wrist_initial_position").value());
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
        if (gamepad != engine.gamepad1) {
            return;
        }

        if (button.equals("guide")) {
            robot.hardwareFault = !robot.hardwareFault;
        } else if (button.equals("start")) {
            fieldCentricControl = !fieldCentricControl;
        }
    }
}
