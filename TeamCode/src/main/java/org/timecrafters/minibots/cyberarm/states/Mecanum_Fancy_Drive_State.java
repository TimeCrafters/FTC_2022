package org.timecrafters.minibots.cyberarm.states;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


public class Mecanum_Fancy_Drive_State extends CyberarmState {

    private final MecanumRobot robot;
    public String ballAmount;
    public boolean A;
    public boolean X;
    public boolean Y;
    private double drivePower = 1;
    private boolean bprev;


    public Mecanum_Fancy_Drive_State(MecanumRobot robot) {
        this.robot = robot;
    }
    @Override
    public void init() {
        robot.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
    }
    @Override
    public void exec() {

        //Gamepad Read

        A = engine.gamepad1.a;
        X = engine.gamepad1.x;
        Y = engine.gamepad1.y;

        //Drivetrain Variables
        double y = -engine.gamepad1.left_stick_y; // Remember, this is reversed! because of the negative
        double x = engine.gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = engine.gamepad1.right_stick_x;

        //toggle for drive speed
        boolean b = engine.gamepad1.b;
        if (b && !bprev) {
            if (drivePower == 1) {
                drivePower = 0.5;
            } else {
                drivePower = 1;
            }
        }
        bprev = b;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        // As I programed this and ran it, I realized everything was backwards
        // in direction so to fix that i either went in the robot object state and reversed
        // directions on drive motors or put a negative in behind the joystick power to reverse it.
        // I put negatives in to reverse it because it was the easiest at the moment.

        robot.frontLeftDrive.setPower(-frontLeftPower * drivePower);
        robot.backLeftDrive.setPower(-backLeftPower * drivePower);
        robot.frontRightDrive.setPower(-frontRightPower * drivePower);
        robot.backRightDrive.setPower(-backRightPower * drivePower);

        //-----------------------------------------------------------------
        // Shooter program

        if (A) {
            robot.shooterWheel.setPower(1);
            robot.collectorRight.setPower(0);
        } if (Y) {
            robot.shooterWheel.setPower(0);
            robot.collectorRight.setPower(1);
        } if (X) {
            robot.shooterWheel.setPower(0);
            robot.collectorRight.setPower(0);
        }

        // Ball blocker Servo Program
        // This says if the trigger is pressed down 50% or more the servo brings the blocker down
        if (engine.gamepad1.left_trigger >= 0.5) {
            robot.ballBlocker.setPosition(0.45);
        }
        // this else statement says if nothing is pressed then it stays in the up position
        else {
            robot.ballBlocker.setPosition(0.52);
        }

        //--------------------------------------------------------------------------------
        // ball Amount Sensor Program

        if (robot.ballPositionSensor.getDistance(DistanceUnit.CM) < 9) {
            ballAmount = "3 balls";
        } else if (robot.ballPositionSensor.getDistance(DistanceUnit.CM) < 13){
            ballAmount = "2 balls";
        } else if (robot.ballPositionSensor.getDistance(DistanceUnit.CM) < 17){
            ballAmount = "1 balls";
        } else {
            ballAmount = "no  balls";
        }



        //-------------------------------------------------------------------------------------------------------------------
        // LIGHT CONTROLS

        if (robot.ballBlocker.getPosition() == 0.45 && robot.shooterWheel.getPower() == 1) {
            robot.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.STROBE_RED);
        } else if (robot.shooterWheel.getPower() == 1) {
            robot.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
        } else if (ballAmount.equals("3 balls")) {
            robot.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
        } else if (ballAmount.equals("2 balls")) {
            robot.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.ORANGE);
        } else if (ballAmount.equals("1 balls")) {
            robot.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
        } else {
            robot.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
        }
    }

//------------------------------------------------------------------------------------------------------------------------------------
// Telemetry Data


    @Override
    public void telemetry() {
        engine.telemetry.addData("Distance", robot.ballPositionSensor.getDistance(DistanceUnit.CM));
        engine.telemetry.addData("Amount", ballAmount);
        engine.telemetry.addData("Speed", drivePower);
        engine.telemetry.addData("FrontLeftEncoder", robot.frontLeftDrive.getCurrentPosition());
        engine.telemetry.addData("FrontRightEncoder", robot.frontRightDrive.getCurrentPosition());
        engine.telemetry.addData("BackLeftEncoder", robot.backLeftDrive.getCurrentPosition());
        engine.telemetry.addData("BackRightEncoder", robot.backRightDrive.getCurrentPosition());

    }
    @Override
    public void exac() {

    }
}


