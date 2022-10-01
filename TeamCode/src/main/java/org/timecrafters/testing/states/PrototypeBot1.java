package org.timecrafters.testing.states;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;

public class PrototypeBot1 {

    public Servo RackRiserLeft, RackRiserRight, FrontRiserLeft, FrontRiserRight;
    private final CyberarmEngine engine;

        public DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive, armMotor;

        public CRServo collectorLeft, collectorRight;

        public Servo collectorWrist;

        public TimeCraftersConfiguration configuration;

        public PrototypeBot1(CyberarmEngine engine) {
            this.engine = engine;

            setupRobot();
        }

        private void setupRobot () {
            // TACNET configuration
            configuration = new TimeCraftersConfiguration("POWERPLAY");

            //motors configuration
            frontLeftDrive = engine.hardwareMap.dcMotor.get("front left");
            frontRightDrive = engine.hardwareMap.dcMotor.get("front right");
            backRightDrive = engine.hardwareMap.dcMotor.get("back left");
            backLeftDrive = engine.hardwareMap.dcMotor.get("back right");
            armMotor = engine.hardwareMap.dcMotor.get("arm motor");

            // servo configuration
            collectorLeft = engine.hardwareMap.crservo.get("collector left");
            collectorRight = engine.hardwareMap.crservo.get("collector right");
            collectorWrist = engine.hardwareMap.servo.get("collector wrist");
            RackRiserLeft = engine.hardwareMap.servo.get("RackRiserLeft");
            RackRiserRight = engine.hardwareMap.servo.get("RackRiserRight");
            FrontRiserLeft = engine.hardwareMap.servo.get("FrontRiserLeft");
            FrontRiserRight = engine.hardwareMap.servo.get("FrontRiserRight");

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
