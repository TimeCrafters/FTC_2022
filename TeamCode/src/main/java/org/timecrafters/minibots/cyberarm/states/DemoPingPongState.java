package org.timecrafters.minibots.cyberarm.states;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;

public class DemoPingPongState extends CyberarmState {

    private final MecanumRobot robot;
    private boolean A;
    private boolean B;
    private boolean X;
    private boolean Y;

    public DemoPingPongState(MecanumRobot robot) {
        this.robot = robot;
    }

    @Override
    public void exec() {

        //Gamepad Read

        A = engine.gamepad2.a;
        B = engine.gamepad2.b;
        X = engine.gamepad2.x;
        Y = engine.gamepad2.y;

        Gamepad gamepad = engine.gamepad1;
        if (engine.gamepad2.right_trigger != 0) {
            gamepad = engine.gamepad2;
        }

        double y = -gamepad.left_stick_y; // Remember, this is reversed! because of the negative
        double x = gamepad.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad.right_stick_x;

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

        robot.frontLeftDrive.setPower(-frontLeftPower);
        robot.backLeftDrive.setPower(-backLeftPower);
        robot.frontRightDrive.setPower(-frontRightPower);
        robot.backRightDrive.setPower(-backRightPower);

//--------------------------------------------------------------------------------------------------------------------------------
// GamePad 2 Controls (GamePad 1 Has no control with these)

        if (A){
            robot.shooterWheel.setPower(1);
            robot.collectorRight.setPower(0);
        }

        if (Y){
            robot.shooterWheel.setPower(0);
            robot.collectorRight.setPower(1);
        }

        if (X){
            robot.shooterWheel.setPower(0);
            robot.collectorRight.setPower(0);
        }

        // Ball blocker Servo Program
        // This says if the trigger is pressed down 50% or more the servo brings the blocker down
        if (engine.gamepad2.left_trigger >= 0.5){
            robot.ballBlocker.setPosition(0.45);
        }
        // this else statement says if nothing is pressed then it stays in the up position
        else {
            robot.ballBlocker.setPosition(0.52);


        }
    }
}

