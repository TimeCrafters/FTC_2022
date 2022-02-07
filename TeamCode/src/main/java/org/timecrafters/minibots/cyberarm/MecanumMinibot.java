package org.timecrafters.minibots.cyberarm;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmEngine;

public class MecanumMinibot {
    public static final int TURN_LEFT = 0;
    public static final int TURN_RIGHT = 1;
    public static final int STRAFE_LEFT = 0;
    public static final int STRAFE_RIGHT = 1;

    private CyberarmEngine engine;

    public DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;
    public Servo  pServoRotate, pServoGrab;
    public CRServo pServoArch, pServoElevate, pServoCarousel;

    public MecanumMinibot(CyberarmEngine engine) {
        this.engine = engine;

        setupDrivetrain();
        setupServos();
    }

    private void setupDrivetrain() {
        frontLeftDrive                          = engine.hardwareMap.dcMotor.get("frontLeft");  //2
        frontRightDrive                         = engine.hardwareMap.dcMotor.get("frontRight"); //3
        backLeftDrive                           = engine.hardwareMap.dcMotor.get("backLeft");   //1
        backRightDrive                          = engine.hardwareMap.dcMotor.get("backRight");  //0

        frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    private void setupServos(){
        pServoArch                              = engine.hardwareMap.crservo.get("arch");     //0
        pServoElevate                           = engine.hardwareMap.crservo.get("elevate");  //2
        pServoRotate                            = engine.hardwareMap.servo.get("rotate");   //3
        pServoGrab                              = engine.hardwareMap.servo.get("grab");     //4
        pServoCarousel                          = engine.hardwareMap.crservo.get("carousel"); //5

        pServoRotate.scaleRange(0.0, 1.0);
        pServoGrab.scaleRange(0.0, 1.0);

        pServoArch.setDirection(DcMotorSimple.Direction.FORWARD);
        pServoElevate.setDirection(DcMotorSimple.Direction.REVERSE);
        pServoRotate.setDirection(Servo.Direction.FORWARD);
        pServoGrab.setDirection(Servo.Direction.FORWARD);
        pServoCarousel.setDirection(DcMotorSimple.Direction.FORWARD);

        pServoArch.setPower(0.0);
        pServoElevate.setPower(0.0);
        pServoRotate.setPosition(0.70);
        pServoGrab.setPosition(0.9);
        pServoCarousel.setPower(0.0);

    }

    public void driveAll(double power) {
        frontLeftDrive.setPower(power);
        frontRightDrive.setPower(power);
        backLeftDrive.setPower(power);
        backRightDrive.setPower(power);
    }

    public void driveStop() {
        driveAll(0);
    }

    public void driveTurn(int direction, double power) {
        if (direction == TURN_LEFT) {
            frontLeftDrive.setPower(-power);
            backLeftDrive.setPower(-power);

            frontRightDrive.setPower(power);
            backRightDrive.setPower(power);

        } else if (direction == TURN_RIGHT) {
            frontLeftDrive.setPower(power);
            backLeftDrive.setPower(power);

            frontRightDrive.setPower(-power);
            backRightDrive.setPower(-power);
        } else {
            throw new RuntimeException("Invalid direction for driveTurn()");
        }
    }

    public void driveStrafe(int direction, double power) {
        double dStrafeRatioBack=1.0;
        double dStrafeRatioFront=0.5;

        if (direction == STRAFE_LEFT) {
            frontLeftDrive.setPower(power*dStrafeRatioFront);
            frontRightDrive.setPower(-power*dStrafeRatioFront);

            backLeftDrive.setPower(-power*dStrafeRatioBack);
            backRightDrive.setPower(power*dStrafeRatioBack);

        } else if (direction == STRAFE_RIGHT) {
            frontLeftDrive.setPower(-power*dStrafeRatioFront);
            frontRightDrive.setPower(power*dStrafeRatioFront);

            backLeftDrive.setPower(power*dStrafeRatioBack);
            backRightDrive.setPower(-power*dStrafeRatioBack);
        } else {
            throw new RuntimeException("Invalid direction for driveStrafe()");
        }
    }
}
