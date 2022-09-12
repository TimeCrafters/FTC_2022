package org.timecrafters.minibots.cyberarm.states;

//adb connect 192.168.43.1

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.cyberarm.engine.V2.CyberarmState;

public class FieldOrientedDrive extends CyberarmState {

    MecanumRobot robot;
    BNO055IMU imu;

    public FieldOrientedDrive(MecanumRobot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//        parameters.loggingEnabled = true;
//        parameters.loggingTag     = "IMU";
        imu = engine.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;

        imu.initialize(parameters);

        robot.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
    }

    @Override
    public void exec() {

        robot.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.LIGHT_CHASE_BLUE);

        double y = -engine.gamepad1.left_stick_y; // Remember, this is reversed!
        double x = engine.gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = engine.gamepad1.right_stick_x;

        // Read inverse IMU heading, as the IMU heading is CW positive

        double botHeading = -imu.getAngularOrientation().firstAngle;

        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        robot.frontLeftDrive.setPower(frontLeftPower * .95);
        robot.backRightDrive.setPower(backLeftPower * .95);
        robot.frontRightDrive.setPower(frontRightPower * 1);
        robot.backRightDrive.setPower(backRightPower * 1);

    }
    }
