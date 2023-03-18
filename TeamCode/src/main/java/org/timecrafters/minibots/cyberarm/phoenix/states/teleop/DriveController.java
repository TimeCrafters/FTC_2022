package org.timecrafters.minibots.cyberarm.phoenix.states.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.minibots.cyberarm.phoenix.Robot;

public class DriveController extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private final Gamepad controller;
    private boolean robotSlowMode;

    public DriveController(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        this.controller = engine.gamepad1;

        this.robotSlowMode = true;
    }

    @Override
    public void exec() {
        move();
    }

    @Override
    public void stop() {
        stopDrive();
    }

    @Override
    public void buttonDown(Gamepad gamepad, String button) {
        if (gamepad != controller) {
            return;
        }

        // DEBUG: Toggle hardware fault
        if (button.equals("guide")) {
            robot.hardwareFault = !robot.hardwareFault;

            if (robot.hardwareFault) {
                robot.hardwareFaultMessage = "Manually triggered.";
            } else {
                robot.hardwareFaultMessage = "";
            }
        }

        if (button.equals("right_stick_button")) {
            robotSlowMode = !robotSlowMode;
        }

        if (button.equals("left_stick_button") && robot.hardwareFault) {
            robot.imu.resetYaw();
        }
    }

    // REF: https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html
    private void move() {
        if (robot.automaticAntiTipActive || robot.hardwareFault) {
            return;
        }

        double x = -controller.left_stick_x;
        double y = -controller.left_stick_y;

        // Improve control?
        if (y < 0) {
            y = -Math.sqrt(-y);
        } else {
            y = Math.sqrt(y);
        }

        if (x < 0) {
            x = -Math.sqrt(-x) * 1.1; // Counteract imperfect strafing;
        } else {
            x = Math.sqrt(x) * 1.1; // Counteract imperfect strafing;
        }

        double rx = -controller.right_stick_x;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);


        double frontLeftPower = 0, frontRightPower = 0, backLeftPower = 0 , backRightPower = 0;

        double heading = robot.heading();
        double rotX = x * Math.cos(heading) - y * Math.sin(heading);
        double rotY = x * Math.sin(heading) + y * Math.cos(heading);

        frontLeftPower = (rotY + rotX + rx) / denominator;
        backLeftPower = (rotY - rotX - rx) / denominator;
        frontRightPower = (rotY - rotX + rx) / denominator;
        backRightPower = (rotY + rotX - rx) / denominator;

        double maxVelocity = robot.unitToTicks(DistanceUnit.INCH, robot.tuningConfig("drivetrain_max_velocity_in_inches").value());
        double slowVelocity = robot.unitToTicks(DistanceUnit.INCH, robot.tuningConfig("drivetrain_slow_velocity_in_inches").value());
        double velocity = robotSlowMode ? slowVelocity : maxVelocity;

        // Power is treated as a ratio here
        robot.frontLeftDrive.setVelocity(frontLeftPower * velocity);
        robot.frontRightDrive.setVelocity(frontRightPower * velocity);

        robot.backLeftDrive.setVelocity(backLeftPower * velocity);
        robot.backRightDrive.setVelocity(backRightPower * velocity);
    }

    private void stopDrive() {
        robot.backLeftDrive.setVelocity(0); robot.backLeftDrive.setPower(0);
        robot.frontRightDrive.setVelocity(0); robot.frontRightDrive.setPower(0);

        robot.frontLeftDrive.setVelocity(0); robot.frontLeftDrive.setPower(0);
        robot.backRightDrive.setVelocity(0); robot.backRightDrive.setPower(0);
    }
}
