package org.timecrafters.TeleOp.states;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;

public class TeleOPTankDriver extends CyberarmState {

    private final PhoenixBot1 robot;
    private long lastStepTime = 0;
    private double drivePower = 0.3;
    private double RobotRotation;
    private double currentDriveCommand;
    private double RotationTarget, DeltaRotation;
    private double MinimalPower = 0.2;
    private int DeltaOdometerR, Endeavour, Spirit;
    private boolean FreeSpirit;
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
        FreeSpirit = false;
    }

    @Override
    public void exec() {

        if (drivePower > 0.2) {
            if (System.currentTimeMillis() - lastStepTime >= 2000 && DeltaOdometerR < 4096) {
                lastStepTime = System.currentTimeMillis();
                FreeSpirit = true;
            }
        }

        if (FreeSpirit) {
            getCurrentDriveCommand();
            drivePower = -currentDriveCommand;
            robot.backLeftDrive.setPower(drivePower);
            robot.backRightDrive.setPower(drivePower);
            robot.frontLeftDrive.setPower(drivePower);
            robot.frontRightDrive.setPower(drivePower);
        }

        if (Math.abs(engine.gamepad1.left_stick_y) > 0.1 && !FreeSpirit) {
            drivePower = engine.gamepad1.left_stick_y;
            robot.backLeftDrive.setPower(drivePower);
            robot.backRightDrive.setPower(drivePower);
            robot.frontLeftDrive.setPower(drivePower);
            robot.frontRightDrive.setPower(drivePower);
        }

        if (Math.abs(engine.gamepad1.right_stick_x) > 0.1) {
            drivePower = engine.gamepad1.right_stick_x;
            robot.backLeftDrive.setPower(drivePower);
            robot.backRightDrive.setPower(-drivePower);
            robot.frontLeftDrive.setPower(drivePower);
            robot.frontRightDrive.setPower(-drivePower);
        }


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
        Spirit = robot.OdometerEncoder.getCurrentPosition();
        if (System.currentTimeMillis() - lastStepTime >= 1000) {
            lastStepTime = System.currentTimeMillis();
            DeltaOdometerR = robot.OdometerEncoder.getCurrentPosition() - Spirit;
        }
    }

    public void getCurrentDriveCommand() {
        if (Math.abs(engine.gamepad1.left_stick_y) > 0.1) {
            currentDriveCommand = engine.gamepad1.left_stick_y;
        } else if (Math.abs(engine.gamepad1.left_stick_x) > 0.1) {
            currentDriveCommand = engine.gamepad1.left_stick_x;
        } else if (Math.abs(engine.gamepad1.right_stick_x) > 0.1) {
            currentDriveCommand = engine.gamepad1.right_stick_x;
        } else if (Math.abs(engine.gamepad1.right_stick_y) > 0.1) {
            currentDriveCommand = engine.gamepad1.right_stick_y;
        } else if (Math.abs(engine.gamepad1.left_stick_y) > 0.1 && Math.abs(engine.gamepad1.left_stick_x) > 0.1 && Math.abs(engine.gamepad1.right_stick_x) > 0.1 && Math.abs(engine.gamepad1.right_stick_y) > 0.1) {
            currentDriveCommand = 0;
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