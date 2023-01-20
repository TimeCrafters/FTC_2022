package org.timecrafters.minibots.cyberarm.chiron.states;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class DriverControlState extends CyberarmState {
    private final Robot robot;
    private final GamepadChecker controller;
    private final double releaseConfirmationDelay;
    private double lastLiftManualControlTime = 0, lastWristManualControlTime = 0, lastLEDStatusAnimationTime = 0;
    private boolean LEDStatusToggle = false;

    public DriverControlState(Robot robot) {
        this.robot = robot;
        this.controller = new GamepadChecker(engine, engine.gamepad1);

        this.releaseConfirmationDelay = robot.tuningConfig("cone_release_confirmation_delay").value(); // ms
    }

    @Override
    public void exec() {
        double forwardSpeed = engine.gamepad1.left_stick_y * -1;
        double rightSpeed = engine.gamepad1.right_stick_x;
        double forwardAngle = robot.facing();

        robot.status = Robot.Status.OKAY;

        move(forwardAngle, forwardSpeed, rightSpeed);
        liftManualControl();
        wristManualControl();

        automatics();

        controller.update();
    }

    // FIXME: replace .setPower with .setVelocity
    private void move(double forwardAngle, double forwardSpeed, double rightSpeed) {
        if (robot.automaticAntiTipActive || robot.hardwareFault) {
            return;
        }

        if (rightSpeed == 0 && forwardSpeed != 0) { // DRIVE STRAIGHT
            robot.leftDrive.setPower(forwardSpeed);
            robot.rightDrive.setPower(forwardSpeed);

            robot.frontDrive.setPower(0);
            robot.backDrive.setPower(0);

        } else if (rightSpeed != 0 && forwardSpeed == 0) { // TURN IN PLACE
            robot.leftDrive.setPower(rightSpeed);
            robot.rightDrive.setPower(-rightSpeed);

            robot.frontDrive.setPower(rightSpeed);
            robot.backDrive.setPower(-rightSpeed);

        } else if (rightSpeed != 0 && forwardSpeed != 0) { // ANGLE DRIVE
            // TODO
            stopDrive();
        } else {
            stopDrive();
        }
    }

    private void stopDrive() {
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);

        robot.frontDrive.setPower(0);
        robot.backDrive.setPower(0);
    }

    private void liftManualControl() {
        if (robot.hardwareFault) {
            return;
        }

        robot.status = Robot.Status.WARNING;

        double stepInterval = robot.tuningConfig("lift_manual_step_interval").value();
        int stepSize = robot.tuningConfig("lift_manual_step_size").value();

        if ((engine.gamepad1.left_trigger > 0 || engine.gamepad1.right_trigger > 0) && runTime() - lastWristManualControlTime >= stepInterval) {
            lastWristManualControlTime = runTime();

            if (engine.gamepad1.left_trigger > 0) { // Lift DOWN
                // robot.liftDrive.setVelocity(5, AngleUnit.DEGREES);
                robot.liftDrive.setTargetPosition(robot.leftDrive.getTargetPosition() - stepSize);

            } else if (engine.gamepad1.right_trigger > 0) { // Lift UP
                robot.liftDrive.setTargetPosition(robot.leftDrive.getTargetPosition() + stepSize);
            }
        }

        // FIXME: Detect when the triggers have been released and park lift arm at the current position
    }

    private void wristManualControl() {
        if (robot.hardwareFault) {
            return;
        }

        double stepInterval = robot.tuningConfig("wrist_manual_step_interval").value();
        double stepSize = robot.tuningConfig("wrist_manual_step_size").value();

        if ((engine.gamepad1.dpad_left || engine.gamepad1.dpad_right) && runTime() - lastLiftManualControlTime >= stepInterval) {
            lastLiftManualControlTime = runTime();

            if (engine.gamepad1.dpad_left) { // Wrist Left
                robot.wrist.setPosition(robot.wrist.getPosition() - stepSize);

            } else if (engine.gamepad1.dpad_right) { // Wrist Right
                robot.wrist.setPosition(robot.wrist.getPosition() + stepSize);
            }
        }
    }

    private void stopLift() {
        robot.liftDrive.setPower(0);
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

        if (robot.ticksToAngle(robot.liftDrive.getCurrentPosition()) >= 50) {
            robot.wrist.setPosition(robot.hardwareConfig("wrist_deposit_position").value());
        } else {
            robot.wrist.setPosition(robot.hardwareConfig("wrist_initial_position").value());
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
            stopLift();
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

    private void liftPosition(Robot.LiftPosition position) {
        if (robot.hardwareFault) {
            return;
        }

        robot.status = Robot.Status.WARNING;

        switch (position) {
            case COLLECT:
                robot.liftDrive.setTargetPosition(robot.angleToTicks(120));
                break;

            case GROUND:
                robot.liftDrive.setTargetPosition(robot.angleToTicks(100));
                break;

            case LOW:
                robot.liftDrive.setTargetPosition(robot.angleToTicks(80));
                break;

            case MEDIUM:
                robot.liftDrive.setTargetPosition(robot.angleToTicks(35));
                break;

            case HIGH:
                robot.liftDrive.setTargetPosition(robot.angleToTicks(15));
                break;

            default:
                throw new RuntimeException("Unexpected lift position!");
        }
    }

    private void gripperOpen() {
        robot.gripper.setPosition(robot.hardwareConfig("gripper_open_position").value());
    }

    private void gripperClosed() {
        robot.gripper.setPosition(robot.hardwareConfig("gripper_closed_position").value());
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

            robot.wrist.setPosition(robot.hardwareConfig("wrist_deposit_position").value());
        } else if (button.equals("dpad_up")) {
            robot.wristManuallyControlled = false;

            robot.wrist.setPosition(robot.hardwareConfig("wrist_initial_position").value());
        }

        // Automatic Lift Control
        if (button.equals("a")) {
            liftPosition(Robot.LiftPosition.COLLECT);
        } else if (button.equals("x")) {
            liftPosition(Robot.LiftPosition.GROUND);
        } else if (button.equals("b")) {
            liftPosition(Robot.LiftPosition.LOW);
        } else if (button.equals("y")) {
            liftPosition(Robot.LiftPosition.MEDIUM);
        }
    }

    @Override
    public void buttonUp(Gamepad gamepad, String button) {
        if (gamepad != engine.gamepad1) {
            return;
        }
    }
}
