package org.timecrafters.testing.states;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;

public class PrototypeBot1 {

    public Servo LowRiserLeft, LowRiserRight, HighRiserLeft, HighRiserRight;
    private final CyberarmEngine engine;

        public DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;

        public CRServo collectorLeft, collectorRight;

         public BNO055IMU imu;

         public TimeCraftersConfiguration configuration;

//        public Servo collectorWrist;

        public PrototypeBot1(CyberarmEngine engine) {
            this.engine = engine;

            setupRobot();
        }

        private void setupRobot () {
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

            parameters.mode = BNO055IMU.SensorMode.IMU;
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.loggingEnabled = false;

            this.imu = engine.hardwareMap.get(BNO055IMU.class,"imu");
            imu.initialize(parameters);

            imu.startAccelerationIntegration(new Position(), new Velocity(), 10);

            configuration = new TimeCraftersConfiguration("Phoenix");

            //motors configuration
            frontLeftDrive = engine.hardwareMap.dcMotor.get("Front Left");
            frontRightDrive = engine.hardwareMap.dcMotor.get("Front Right");
            backRightDrive = engine.hardwareMap.dcMotor.get("Back Right");
            backLeftDrive = engine.hardwareMap.dcMotor.get("Back Left");

            // servo configuration

            // Collector
            collectorLeft = engine.hardwareMap.crservo.get("Collector Left");
            collectorRight = engine.hardwareMap.crservo.get("Collector Right");

            // Arm
            LowRiserLeft = engine.hardwareMap.servo.get("LowRiserLeft");
            LowRiserRight = engine.hardwareMap.servo.get("LowRiserRight");
            HighRiserLeft = engine.hardwareMap.servo.get("HighRiserLeft");
            HighRiserRight = engine.hardwareMap.servo.get("HighRiserRight");

            //motors direction and encoders

            frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            frontRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            backRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }
