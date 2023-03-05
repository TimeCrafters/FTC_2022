package org.timecrafters.Phoenix.TeleOp.states;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.timecrafters.Phoenix.PhoenixBot1;

public class TeleOPTankDriver extends CyberarmState {

    private final PhoenixBot1 robot;
    private long lastStepTime = 0;
    private double drivePower = 0.3;
    private double RobotRotation;
    private double RotationTarget, DeltaRotation;
    private double MinimalPower = 0.2;
    private int DeltaOdometerR, Spirit;
    private GamepadChecker gamepad1Checker;

    public TeleOPTankDriver(PhoenixBot1 robot) {
        this.robot = robot;
    }

    @Override
    public void telemetry() {
        engine.telemetry.addLine("Tank Driver");
        engine.telemetry.addData("IMU", robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        engine.telemetry.addData("Drive Power", drivePower);
        engine.telemetry.addData("Delta Rotation", DeltaRotation);
    }

    @Override
    public void init() {

    }

    @Override
    public void exec() {

        double y = -engine.gamepad1.left_stick_y; // Remember, this is reversed! because of the negative //ORLY?
        double x = engine.gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = engine.gamepad1.right_stick_x;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;


        double heading = -robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double rotX = x * Math.cos(heading) - y * Math.sin(heading);
        double rotY = x * Math.sin(heading) + y * Math.cos(heading);

        frontLeftPower = (rotY + rotX + rx) / denominator;
        backLeftPower = (rotY - rotX + rx) / denominator;
        frontRightPower = (rotY - rotX - rx) / denominator;
        backRightPower = (rotY + rotX - rx) / denominator;

        robot.backLeftDrive.setPower(backLeftPower * drivePower);
        robot.backRightDrive.setPower(backRightPower * drivePower);
        robot.frontLeftDrive.setPower(frontLeftPower * drivePower);
        robot.frontRightDrive.setPower(frontRightPower * drivePower);



//        if (Math.abs(engine.gamepad1.left_stick_y) > 0.1 && Math.abs(engine.gamepad1.left_stick_x) < 0.1) {
//            drivePower = engine.gamepad1.left_stick_y;
//            robot.backLeftDrive.setPower(drivePower);
//            robot.backRightDrive.setPower(drivePower);
//            robot.frontLeftDrive.setPower(drivePower);
//            robot.frontRightDrive.setPower(drivePower);
//        }
//
//        if (Math.abs(engine.gamepad1.left_stick_x) > 0.1 && Math.abs(engine.gamepad1.left_stick_y) < 0.1) {
//            drivePower = engine.gamepad1.left_stick_x;
//            robot.backLeftDrive.setPower(drivePower);
//            robot.backRightDrive.setPower(-drivePower);
//            robot.frontLeftDrive.setPower(-drivePower);
//            robot.frontRightDrive.setPower(drivePower);
//        }
//
//        if (Math.abs(engine.gamepad1.left_stick_x) > 0.1 && Math.abs(engine.gamepad1.left_stick_y) > 0.1) {
//            robot.frontLeftDrive.setPower(frontLeftPower * drivePower);
//            robot.backLeftDrive.setPower(backLeftPower * drivePower);
//            robot.frontRightDrive.setPower(frontRightPower * drivePower);
//            robot.backRightDrive.setPower(backRightPower * drivePower);
//        }
//
//        if (Math.abs(engine.gamepad1.right_stick_x) > 0.1) {
//            drivePower = engine.gamepad1.right_stick_x;
//            robot.backLeftDrive.setPower(-drivePower);
//            robot.backRightDrive.setPower(drivePower);
//            robot.frontLeftDrive.setPower(-drivePower);
//            robot.frontRightDrive.setPower(drivePower);
//        }
//
//        if (engine.gamepad1.left_stick_y == 0 && engine.gamepad1.left_stick_x == 0 && engine.gamepad1.right_stick_x == 0) {
//            drivePower = 0;
//            robot.backLeftDrive.setPower(drivePower);
//            robot.backRightDrive.setPower(drivePower);
//            robot.frontLeftDrive.setPower(drivePower);
//            robot.frontRightDrive.setPower(drivePower);
//        }



    }

    public void CalculateDeltaRotation() {
        if (RotationTarget >= 0 && RobotRotation >= 0) {
            DeltaRotation = Math.abs(RotationTarget - RobotRotation);
        } else if (RotationTarget <= 0 && RobotRotation <= 0) {
            DeltaRotation = Math.abs(RotationTarget - RobotRotation);
        } else if (RotationTarget >= 0 && RobotRotation <= 0) {
            DeltaRotation = Math.abs(RotationTarget + RobotRotation);
        } else if (RotationTarget <= 0 && RobotRotation >= 0) {
            DeltaRotation = Math.abs(RobotRotation + RotationTarget);
        }
    }

    public void getDeltaOdometerR() {
        Spirit = robot.OdometerEncoderRight.getCurrentPosition();
        if (System.currentTimeMillis() - lastStepTime >= 1000) {
            lastStepTime = System.currentTimeMillis();
            DeltaOdometerR = robot.OdometerEncoderRight.getCurrentPosition() - Spirit;
        }
    }

    @Override
    public void buttonUp(Gamepad gamepad, String button) {
        if (engine.gamepad1 == gamepad && button.equals("start")) {

            robot.imu.resetYaw();
        }
        if (engine.gamepad1 == gamepad && button.equals("y")) {
            drivePower = 1;
        }
        if (engine.gamepad1 == gamepad && button.equals("a")) {
            drivePower = 0.5;
        }
    }

}