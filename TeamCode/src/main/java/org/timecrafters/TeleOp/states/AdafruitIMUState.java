package org.timecrafters.TeleOp.states;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;

import org.cyberarm.engine.V2.CyberarmState;

public class AdafruitIMUState extends CyberarmState {
    AdafruitBNO055IMU imu;
    @Override
    public void init() {
        imu = engine.hardwareMap.get(AdafruitBNO055IMU.class, "adafruit_imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        imu.initialize(parameters);
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("First Angle", imu.getAngularOrientation().firstAngle);
        engine.telemetry.addData("Second Angle", imu.getAngularOrientation().secondAngle);
        engine.telemetry.addData("Third Angle", imu.getAngularOrientation().thirdAngle);
    }

    @Override
    public void exec() {

    }
}
