package org.timecrafters.TeleOp.engine;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmEngine;

public class PhoenixWingEngine {

    public Servo LowRiserLeft, LowRiserRight, HighRiserLeft, HighRiserRight;
    private final CyberarmEngine engine;

    public DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;

    public CRServo collectorLeft, collectorRight;

    public BNO055IMU imu;

//        public Servo collectorWrist;

    public PhoenixWingEngine(CyberarmEngine engine) {
        this.engine = engine;

        setupRobot();
    }
    private void setupRobot() {

    }
}
