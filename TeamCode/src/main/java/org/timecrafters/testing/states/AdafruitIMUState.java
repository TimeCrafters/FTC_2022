package org.timecrafters.testing.states;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.ImuOrientationOnRobot;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class AdafruitIMUState extends CyberarmState {
    IMU imu;
    @Override
    public void init() {
        imu = engine.hardwareMap.get(IMU.class, "adafruit_imu");
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("PITCH", imu.getRobotYawPitchRollAngles().getPitch(AngleUnit.DEGREES));
        engine.telemetry.addData("ROLL", imu.getRobotYawPitchRollAngles().getRoll(AngleUnit.DEGREES));
        engine.telemetry.addData("YAW", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
    }

    @Override
    public void exec() {

    }
}
