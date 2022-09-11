package org.timecrafters.minibots.cyberarm.states;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmEngine;

public class MecanumRobot {

    private CyberarmEngine engine;

    public DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;

    public RevBlinkinLedDriver ledDriver;

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
        ledDriver = engine.hardwareMap.get(RevBlinkinLedDriver.class, "lights");

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
