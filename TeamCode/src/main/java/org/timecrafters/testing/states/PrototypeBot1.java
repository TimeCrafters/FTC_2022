package org.timecrafters.testing.states;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.cyberarm.engine.V2.CyberarmEngine;

public class PrototypeBot1 {

        private CyberarmEngine engine;

        public DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive, armMotor;

        public CRServo collectorLeft, collectorRight;

        public PrototypeBot1(CyberarmEngine engine) {
            this.engine = engine;

            setupRobot();
        }

        private void setupRobot () {

            //motors configuration
            frontLeftDrive = engine.hardwareMap.dcMotor.get("front left");
            frontRightDrive = engine.hardwareMap.dcMotor.get("front right");
            backRightDrive = engine.hardwareMap.dcMotor.get("back left");
            backLeftDrive = engine.hardwareMap.dcMotor.get("back right");
            armMotor = engine.hardwareMap.dcMotor.get("arm motor");

            // servo configuration
            collectorLeft = engine.hardwareMap.crservo.get("collector left");
            collectorRight = engine.hardwareMap.crservo.get("collector right");

            //motors direction and encoders
            frontLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            backLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            armMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }
