package org.timecrafters.minibots.cyberarm.chiron.states.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class DrivetrainDriverControl extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private Gamepad controller;

    private boolean fieldCentricControl = true;
    private boolean invertRobotForward = false;
    private boolean robotSlowMode = false;

    public DrivetrainDriverControl(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        this.controller = engine.gamepad1;
    }

    @Override
    public void exec() {
        robot.update();

        move();

        automatics();
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("Run Time", runTime());
//        engine.telemetry.addData("LED Status Interval", lastLEDStatusAnimationTime);
        engine.telemetry.addData("Field Centric Control", fieldCentricControl);
        engine.telemetry.addData("Invert Robot Forward", invertRobotForward);
        engine.telemetry.addData("Robot Slow Mode", robotSlowMode);
    }

    // FIXME: replace .setPower with .setVelocity
    // REF: https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html
    private void move() {
        if (robot.automaticAntiTipActive || robot.hardwareFault) {
            return;
        }

        double maxSpeed = robot.tuningConfig("drivetrain_max_power").value();
        double slowSpeed = robot.tuningConfig("drivetrain_slow_power").value();
        double speedLimiter = robotSlowMode ? slowSpeed : maxSpeed;

        double y = (invertRobotForward ? controller.left_stick_y : -controller.left_stick_y) * speedLimiter;
        double x = ((invertRobotForward ? controller.left_stick_x : -controller.left_stick_x) * speedLimiter) * 1.1; // Counteract imperfect strafing
        double rx = -controller.right_stick_x * speedLimiter;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);


        double frontLeftPower = 0, frontRightPower = 0, backLeftPower = 0 , backRightPower = 0;

        if (fieldCentricControl) {
            double heading = robot.heading();
            double rotX = x * Math.cos(heading) - y * Math.sin(heading);
            double rotY = x * Math.sin(heading) + y * Math.cos(heading);

            frontLeftPower = (rotY + rotX + rx) / denominator;
            backLeftPower = (rotY - rotX - rx) / denominator;
            frontRightPower = (rotY - rotX + rx) / denominator;
            backRightPower = (rotY + rotX - rx) / denominator;

        } else {
            frontLeftPower = (y + x + rx) / denominator;
            backLeftPower = (y - x - rx) / denominator;
            frontRightPower = (y - x + rx) / denominator;
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

    private void automatics() {
        automaticAntiTip(); // NO OP
        automaticHardwareMonitor();
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
        }
    }

    @Override
    public void buttonDown(Gamepad gamepad, String button) {
        if (gamepad != controller) {
            return;
        }

        // DEBUG: Toggle hardware fault
        if (button.equals("guide")) {
            robot.hardwareFault = !robot.hardwareFault;
        }

        if (button.equals("pause")) {
            invertRobotForward = !invertRobotForward;
        }

        if (button.equals("start")) {
            fieldCentricControl = !fieldCentricControl;
        }

        if (button.equals("right_stick_button")) {
            robotSlowMode = !robotSlowMode;
        }
    }

    @Override
    public void buttonUp(Gamepad gamepad, String button) {
        if (gamepad != controller) {
            return;
        }
    }
}
