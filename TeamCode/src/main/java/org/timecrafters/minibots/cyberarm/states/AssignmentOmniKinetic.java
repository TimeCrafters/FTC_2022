package org.timecrafters.minibots.cyberarm.states;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmState;

public class AssignmentOmniKinetic extends CyberarmState {
    CRServo Wheel, Rack;
    DcMotor Three, Zero, Two, One;
    Servo Zygo;
    @Override
    public void init() {
        super.init();

        Wheel=engine.hardwareMap.crservo.get("Wheel");
        Rack=engine.hardwareMap.crservo.get("Rack");
        Three=engine.hardwareMap.dcMotor.get("Three");
        Zero=engine.hardwareMap.dcMotor.get("Zero");
        Two=engine.hardwareMap.dcMotor.get("Two");
        One=engine.hardwareMap.dcMotor.get("One");
        Zygo=engine.hardwareMap.servo.get("Zygo");
    }
    MecanumRobot robot;
            public AssignmentOmniKinetic (MecanumRobot robot) {this.robot = robot;}

    @Override
    public void exec() {

                if (engine.gamepad1.left_bumper) {

                    robot.frontLeftDrive.setPower(engine.gamepad1.left_stick_y * 1);
                    robot.frontRightDrive.setPower(engine.gamepad1.left_stick_y * -1);
                    robot.backLeftDrive.setPower(engine.gamepad1.left_stick_y * -1);
                    robot.backRightDrive.setPower(engine.gamepad1.left_stick_y * 1);

                }

                if (engine.gamepad1.right_bumper) {

                    robot.frontLeftDrive.setPower(engine.gamepad1.right_stick_y * -1);
                    robot.frontRightDrive.setPower(engine.gamepad1.right_stick_y * 1);
                    robot.backLeftDrive.setPower(engine.gamepad1.right_stick_y * 1);
                    robot.backRightDrive.setPower(engine.gamepad1.right_stick_y * -1);


                    }
                if (engine.gamepad1.dpad_up) {

                    Zygo.setPosition(1.0);

                }

                if (engine.gamepad1.dpad_down) {
                    Zygo.setPosition(0);
                }

                if (engine.gamepad1.a) {

                    engine.hardwareMap.led.get(String.valueOf(RevBlinkinLedDriver.BlinkinPattern.GREEN));

                }

                if (engine.gamepad1.b) {

                    engine.hardwareMap.led.get(String.valueOf(RevBlinkinLedDriver.BlinkinPattern.RED));

                }

                if (engine.gamepad1.x) {

                    engine.hardwareMap.led.get(String.valueOf(RevBlinkinLedDriver.BlinkinPattern.BLUE));

                }

                if (engine.gamepad1.y) {

                    engine.hardwareMap.led.get(String.valueOf(RevBlinkinLedDriver.BlinkinPattern.YELLOW));

                }

                Three.setPower(engine.gamepad1.left_stick_y * 1);
                Two.setPower(engine.gamepad1.left_stick_y * 1);
                One.setPower(engine.gamepad1.right_stick_y * 1);
                Zero.setPower(engine.gamepad1.right_stick_y * 1);


    }
}
