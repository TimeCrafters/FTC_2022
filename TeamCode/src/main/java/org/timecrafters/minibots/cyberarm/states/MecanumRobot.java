package org.timecrafters.minibots.cyberarm.states;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmEngine;

public class MecanumRobot {

    private CyberarmEngine engine;

    public DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive, shooterWheel, collectorRight;

    public Servo ballBlocker;

    public RevBlinkinLedDriver ledDriver;
    public Rev2mDistanceSensor ballPositionSensor;

    public MecanumRobot(CyberarmEngine engine) {
        this.engine = engine;

        setupRobot();
    }

    private void setupRobot() {

        //motors configuration
        frontLeftDrive = engine.hardwareMap.dcMotor.get("front left");
        frontRightDrive = engine.hardwareMap.dcMotor.get("front right");
        backRightDrive = engine.hardwareMap.dcMotor.get("back left");
        backLeftDrive = engine.hardwareMap.dcMotor.get("back right");
        shooterWheel = engine.hardwareMap.dcMotor.get("shooter wheel");
        collectorRight = engine.hardwareMap.dcMotor.get("collector motor");
        ledDriver = engine.hardwareMap.get(RevBlinkinLedDriver.class, "lights");

        //motors direction and encoders
        frontLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        backLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        // Sensor Configuration

        ballPositionSensor = engine.hardwareMap.get(Rev2mDistanceSensor.class, "ball position");

        //Servo configuration
        ballBlocker = engine.hardwareMap.servo.get("ball blocker");
    }
    }
