package org.timecrafters.minibots.cyberarm.chiron.states.teleop;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class DrivetrainDriverControl extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;
    private final double robotCentricRotation;

    private Gamepad controller;

    private boolean fieldCentricControl = true;
    private boolean invertRobotForward = false;
    private boolean robotSlowMode = false;

    public DrivetrainDriverControl(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        this.controller = engine.gamepad1;

        this.robotCentricRotation = robot.tuningConfig("robot_centric_rotation").value();
    }

    @Override
    public void exec() {
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

    // REF: https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html
    private void move() {
        if (robot.automaticAntiTipActive || robot.hardwareFault) {
            return;
        }

        double y = invertRobotForward ? controller.left_stick_y : -controller.left_stick_y;
        double x = (invertRobotForward && !fieldCentricControl ? controller.left_stick_x : -controller.left_stick_x);

        if (!fieldCentricControl) {
            Vector2d v = new Vector2d(x, y).rotated(robotCentricRotation);

            x = v.getX();
            y = v.getY();
        }

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
            robot.reportStatus(Robot.Status.DANGER);

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

            if (robot.hardwareFault) {
                robot.hardwareFaultMessage = "Manually triggered.";
            } else {
                robot.hardwareFaultMessage = "";
            }
        }

        if (button.equals("back")) {
            invertRobotForward = !invertRobotForward;
        }

        if (button.equals("start")) {
            fieldCentricControl = !fieldCentricControl;
        }

        if (button.equals("right_stick_button")) {
            robotSlowMode = !robotSlowMode;
        }

        if (button.equals("left_stick_button") && robot.hardwareFault) {
            robot.imu.resetYaw();
        }
    }

    @Override
    public void buttonUp(Gamepad gamepad, String button) {
        if (gamepad != controller) {
            return;
        }
    }
}
