package org.timecrafters.minibots.cyberarm;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.cyberarm.engine.V2.CyberarmEngine;

public class MecanumMinibot {
    public static final int TURN_LEFT = 0;
    public static final int TURN_RIGHT = 1;
    public static final int STRAFE_LEFT = 0;
    public static final int STRAFE_RIGHT = 1;

    private CyberarmEngine engine;

    public DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;

    public MecanumMinibot(CyberarmEngine engine) {
        this.engine = engine;

        setupDrivetrain();
    }

    private void setupDrivetrain() {
        frontLeftDrive = engine.hardwareMap.dcMotor.get("frontLeft");
        frontRightDrive = engine.hardwareMap.dcMotor.get("frontRight");
        backLeftDrive = engine.hardwareMap.dcMotor.get("backLeft");
        backRightDrive = engine.hardwareMap.dcMotor.get("backRight");

        frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
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
        if (direction == STRAFE_LEFT) {
            frontLeftDrive.setPower(power);
            frontRightDrive.setPower(-power);

            backLeftDrive.setPower(-power);
            backRightDrive.setPower(power);

        } else if (direction == STRAFE_RIGHT) {
            frontLeftDrive.setPower(-power);
            frontRightDrive.setPower(power);

            backLeftDrive.setPower(power);
            backRightDrive.setPower(-power);
        } else {
            throw new RuntimeException("Invalid direction for driveStrafe()");
        }
    }
}
