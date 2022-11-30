package org.timecrafters.testing.states;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;

public class TeleOPTankDriver extends CyberarmState {

    private final PhoenixBot1 robot;
    private double drivePower = 1;
    private double RobotRotation;
    private double RotationTarget, DeltaRotation;
    private double MinimalPower = 0.2;
    private GamepadChecker gamepad1Checker;
    public TeleOPTankDriver(PhoenixBot1 robot) {
        this.robot = robot;
    }

    @Override
    public void telemetry() {
        engine.telemetry.addLine("Tank Driver");
        engine.telemetry.addData("IMU", robot.imu.getAngularOrientation().firstAngle);
        engine.telemetry.addData("Drive Power", drivePower);
        engine.telemetry.addData("Delta Rotation", DeltaRotation);
    }

    @Override
    public void init() {
        gamepad1Checker = new GamepadChecker(engine, engine.gamepad1);
    }

    @Override
    public void exec() {
        if (engine.gamepad1.right_trigger > 0) {
            drivePower = engine.gamepad1.right_trigger;
            robot.backLeftDrive.setPower(drivePower);
            robot.backRightDrive.setPower(drivePower);
            robot.frontLeftDrive.setPower(drivePower);
            robot.frontRightDrive.setPower(drivePower);
        }

        if (engine.gamepad1.left_trigger > 0) {
            drivePower = engine.gamepad1.left_trigger;
            robot.backLeftDrive.setPower(-drivePower);
            robot.backRightDrive.setPower(-drivePower);
            robot.frontLeftDrive.setPower(-drivePower);
            robot.frontRightDrive.setPower(-drivePower);
        }

        if (Math.abs(engine.gamepad1.left_stick_y) > 0.1) {
            drivePower = engine.gamepad1.left_stick_y;
            robot.backRightDrive.setPower(drivePower * 0.95);
            robot.frontRightDrive.setPower(drivePower * 0.95);
        }

        if (Math.abs(engine.gamepad1.right_stick_y) > 0.1) {
            drivePower = engine.gamepad1.right_stick_y;
            robot.backLeftDrive.setPower(drivePower);
            robot.frontLeftDrive.setPower(drivePower);
        }

        if (engine.gamepad1.right_trigger < 0.1 &&
                engine.gamepad1.left_trigger < 0.1 &&
                !engine.gamepad1.y &&
                !engine.gamepad1.x &&
                !engine.gamepad1.a &&
                !engine.gamepad1.b &&
                !engine.gamepad1.dpad_left &&
                !engine.gamepad1.dpad_right &&
                Math.abs (engine.gamepad1.left_stick_y) < 0.1 &&
                Math.abs(engine.gamepad1.right_stick_y) < 0.1) {
            drivePower = 0;
            robot.backLeftDrive.setPower(-drivePower);
            robot.backRightDrive.setPower(-drivePower);
            robot.frontLeftDrive.setPower(-drivePower);
            robot.frontRightDrive.setPower(-drivePower);
        }

        if (engine.gamepad1.a) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = 180;
            CalculateDeltaRotation();
            if (RobotRotation < 0 && RobotRotation > -179) {
                drivePower = (1 * DeltaRotation/180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            else if (RobotRotation > 0) {
                drivePower = (-1 * DeltaRotation/180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            else if (RobotRotation <= -179 || RobotRotation >= 179) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }

        if (engine.gamepad1.y) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = 0;
            CalculateDeltaRotation();
            if (RobotRotation < -1) {
                drivePower = (-1 * DeltaRotation/180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > 1) {
                drivePower = (1 * DeltaRotation/180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > -1 && RobotRotation < 1) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }
        if (engine.gamepad1.dpad_left) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = -45;
            CalculateDeltaRotation();
            if (RobotRotation > -45 && RobotRotation <= 135) {//CCW
                drivePower = (-1 * DeltaRotation/180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < -45 || RobotRotation > 136) {//CW
                drivePower = (1 * DeltaRotation/180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < -44 && RobotRotation > -46) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }

        if (engine.gamepad1.x) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = -90;
            CalculateDeltaRotation();
            if (RobotRotation < 90 && RobotRotation < -89) {//CCW
                drivePower = (-1 * DeltaRotation/180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower * 0.95);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower * 0.95);
            }
            if (RobotRotation > 90 || RobotRotation < -91) {//CW
                drivePower = (1 * DeltaRotation/180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > -91 && RobotRotation < -89) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }

        if (engine.gamepad1.dpad_right) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = 45;
            CalculateDeltaRotation();
            if (RobotRotation > -135 && RobotRotation < 44) {//CCW
                drivePower = (-1 * DeltaRotation/180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < -135 || RobotRotation < 46) {//CW
                drivePower = (1 * DeltaRotation/180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < 46 && RobotRotation > 44) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }

        if (engine.gamepad1.b) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = 90;
            CalculateDeltaRotation();
            if (RobotRotation > -90 && RobotRotation < 89) {//CCW
                drivePower = (-1 * DeltaRotation/180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < -90 || RobotRotation > 91) {//CW
                drivePower = (1 * DeltaRotation/180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < 91 && RobotRotation > 89) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }

        gamepad1Checker.update();
    }
    public void CalculateDeltaRotation() {
        if (RotationTarget >= 0 && RobotRotation >= 0) {
            DeltaRotation = Math.abs(RotationTarget - RobotRotation);
        }
        else if (RotationTarget <= 0 && RobotRotation <= 0) {
            DeltaRotation = Math.abs(RotationTarget - RobotRotation);
        }
        else if (RotationTarget >= 0 && RobotRotation <= 0) {
            DeltaRotation = Math.abs(RotationTarget + RobotRotation);
        }
        else if (RotationTarget <=0 && RobotRotation >= 0) {
            DeltaRotation = Math.abs(RobotRotation + RotationTarget);
        }
    }
    @Override
    public void buttonUp(Gamepad gamepad, String button) {
        if (engine.gamepad1 == gamepad && button.equals("start")) {
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

            parameters.mode = BNO055IMU.SensorMode.IMU;
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.loggingEnabled = false;

            robot.imu.initialize(parameters);
        }
    }

}
