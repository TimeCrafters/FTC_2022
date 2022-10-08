package org.timecrafters.testing.states;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmEngine;

public class PrototypeBot1 {

    public Servo LowRiserLeft, LowRiserRight, HighRiserLeft, HighRiserRight;
    private final CyberarmEngine engine;

        public DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;

        public CRServo collectorLeft, collectorRight;

//        public Servo collectorWrist;

        public PrototypeBot1(CyberarmEngine engine) {
            this.engine = engine;

            setupRobot();
        }

        private void setupRobot () {

            //motors configuration
            frontLeftDrive = engine.hardwareMap.dcMotor.get("Front Left");
            frontRightDrive = engine.hardwareMap.dcMotor.get("Front Right");
            backRightDrive = engine.hardwareMap.dcMotor.get("Back Left");
            backLeftDrive = engine.hardwareMap.dcMotor.get("Back Right");
//            armMotor = engine.hardwareMap.dcMotor.get("Arm Motor");

            // servo configuration
            collectorLeft = engine.hardwareMap.crservo.get("Collector Left");
            collectorRight = engine.hardwareMap.crservo.get("Collector Right");
//            collectorWrist = engine.hardwareMap.servo.get("Collector Wrist");
            LowRiserLeft = engine.hardwareMap.servo.get("LowRiserLeft");
            LowRiserRight = engine.hardwareMap.servo.get("LowRiserRight");
            HighRiserLeft = engine.hardwareMap.servo.get("HighRiserLeft");
            HighRiserRight = engine.hardwareMap.servo.get("HighRiserRight");

            //motors direction and encoders
            frontLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            backLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//            armMotor.setDirection(DcMotorSimple.Direction.FORWARD);
//            armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }
