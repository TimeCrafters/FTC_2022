package org.timecrafters.TeleOp.states;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;

public class TeleOPTankDriver extends CyberarmState {

    private final PhoenixBot1 robot;
    private double drivePower = 0.3;
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
