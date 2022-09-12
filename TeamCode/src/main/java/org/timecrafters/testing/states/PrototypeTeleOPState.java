package org.timecrafters.testing.states;

import org.cyberarm.engine.V2.CyberarmState;

public class PrototypeTeleOPState extends CyberarmState {

    private final PrototypeBot1 robot;
    public boolean A;
    public boolean X;
    public boolean Y;
    private boolean bprev;
    private double drivePower = 1;

    public PrototypeTeleOPState(PrototypeBot1 robot) {
        this.robot = robot;
    }

    @Override
    public void exec() {

        //Gamepad Read

        A = engine.gamepad1.a;
        X = engine.gamepad1.x;
        Y = engine.gamepad1.y;


        //drive speed toggle

        boolean b = engine.gamepad1.b;

        if (b && !bprev) {

            if (drivePower == 1) {
                drivePower = 0.5;
            } else {
                drivePower = 1;
            }
        }

        //Drivetrain Variables
        double y = -engine.gamepad1.left_stick_y; // Remember, this is reversed! because of the negative
        double x = engine.gamepad1.right_stick_x * 1.1; // Counteract imperfect strafing
        double rx = engine.gamepad1.left_stick_x;

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
        robot.backLeftDrive.setPower(backLeftPower);
        robot.frontRightDrive.setPower(-frontRightPower);
        robot.backRightDrive.setPower(backRightPower);
        robot.armMotor.setPower(engine.gamepad1.left_stick_y * 0.5);

        if (engine.gamepad1.y) {

            robot.collectorLeft.setPower(0);
            robot.collectorRight.setPower(0);

        }

        if (engine.gamepad1.a) {

            robot.collectorLeft.setPower(1);
            robot.collectorRight.setPower(-1);

        }

        if (engine.gamepad1.x) {

            robot.collectorLeft.setPower(-1);
            robot.collectorRight.setPower(1);

        }

    }
}