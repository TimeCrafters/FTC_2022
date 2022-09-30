package org.timecrafters.testing.states;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;

import java.lang.annotation.Target;
import java.util.Objects;

public class PrototypeTeleOPState extends CyberarmState {

    private final PrototypeBot1 robot;
    public boolean A;
    public boolean X;
    public boolean Y;
    private boolean bprev;
    private double drivePower = 1;
    private boolean UpDPad;
    private int armTargetPosition = 0, armCollectPosition = 125, armDeliverPosition = 400;
    private double collectorRiserPosition;

    public PrototypeTeleOPState(PrototypeBot1 robot) {
        this.robot = robot;
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("Arm Power", robot.armMotor.getPower());
        engine.telemetry.addData("Arm Target Position", robot.armMotor.getTargetPosition());
        engine.telemetry.addData("Arm Current Position", robot.armMotor.getCurrentPosition());
        engine.telemetry.addData("Wrist Current Position", robot.collectorWrist.getPosition());
    }

    @Override
    public void init() {
        robot.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.armMotor.setTargetPosition(armTargetPosition);
        robot.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.collectorWrist.setPosition(1);
        robot.RackRiserLeft.setPosition(0.5);
        robot.RackRiserRight.setPosition(0.5);
        robot.FrontRiserLeft.setPosition(1);
        robot.FrontRiserRight.setPosition(0);
//        ~RightRiser's always are setPosition'ed(1), ~LeftRisers always are setPosition'ed(0) or vice versa if wrong.

    }

    @Override
    public void exec() {

        //Gamepad Read

        A = engine.gamepad1.a;
        X = engine.gamepad1.x;
        Y = engine.gamepad1.y;
        UpDPad = engine.gamepad1.dpad_up;


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

        // As I programmed this and ran it, I realized everything was backwards
        // in direction so to fix that i either went in the robot object state and reversed
        // directions on drive motors or put a negative in behind the joystick power to reverse it.
        // I put negatives in to reverse it because it was the easiest at the moment.

        robot.frontLeftDrive.setPower(-frontLeftPower);
        robot.backLeftDrive.setPower(backLeftPower);
        robot.frontRightDrive.setPower(-frontRightPower);
        robot.backRightDrive.setPower(backRightPower);

        robot.armMotor.setPower(engine.gamepad2.left_stick_y * 0.5);


        if (engine.gamepad2.y) {

            robot.collectorLeft.setPower(0);
            robot.collectorRight.setPower(0);

        }

        if (engine.gamepad2.b) {

            robot.collectorLeft.setPower(1);
            robot.collectorRight.setPower(-1);

        }

        if (engine.gamepad2.x) {

            robot.collectorLeft.setPower(-1);
            robot.collectorRight.setPower(1);

        }

        if (engine.gamepad2.dpad_left) {

            robot.collectorWrist.setPosition(0.4);

        }

        if (engine.gamepad2.dpad_right) {

            robot.collectorWrist.setPosition(1);

        }

        if (engine.gamepad2.dpad_up) {

            robot.armMotor.setPower(1);
        } else if (engine.gamepad2.dpad_down) {
            robot.armMotor.setPower(0);

        }
//        int armTargetPosition = robot.armMotor.getCurrentPosition();

        if (engine.gamepad2.left_trigger > 0.1) {
            armTargetPosition += 1;
            if (armTargetPosition > 600) {
                armTargetPosition = 600;
            }
        }

        if (engine.gamepad2.right_trigger > 0.1) {
            armTargetPosition -= 1;
            if (armTargetPosition <= 0) {
                armTargetPosition = 0;

            }

        }

        if (engine.gamepad1.left_bumper) {

            armTargetPosition = armDeliverPosition;

        }

        if (engine.gamepad2.right_bumper) {

            armTargetPosition = armCollectPosition;

        }

        robot.armMotor.setTargetPosition(armTargetPosition);

        if (engine.gamepad1.a) {
            robot.RackRiserLeft.setPosition(0);
            robot.RackRiserRight.setPosition(1);
        }

        if (engine.gamepad1.x) {
            robot.RackRiserLeft.setPosition(1);
            robot.RackRiserRight.setPosition(0);
        }

        if (engine.gamepad2.right_stick_y > 0.1) {
            robot.RackRiserRight.setPosition(1);
            robot.RackRiserLeft.setPosition(-1);
        }

        if (engine.gamepad2.right_stick_y < -0.1) {
            robot.RackRiserRight.setPosition(-1);
            robot.RackRiserLeft.setPosition(1);
        }

        if (engine.gamepad2.left_stick_y > 0.1) {
            robot.FrontRiserLeft.setPosition(-1);
            robot.FrontRiserRight.setPosition(1);
        }

        if (engine.gamepad2.left_stick_y < -0.1) {
            robot.FrontRiserLeft.setPosition(1);
            robot.FrontRiserRight.setPosition(-1);
        }

        if (engine.gamepad2.right_bumper) {
            robot.RackRiserRight.setPosition(1);
            robot.RackRiserLeft.setPosition(0);
        }

        if (engine.gamepad2.left_bumper) {
            robot.FrontRiserRight.setPosition(0);
            robot.FrontRiserLeft.setPosition(1);
        }



    }
}