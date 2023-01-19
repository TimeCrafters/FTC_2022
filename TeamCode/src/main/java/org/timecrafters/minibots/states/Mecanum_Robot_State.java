package org.timecrafters.minibots.states;

import org.cyberarm.engine.V2.CyberarmState;

public class Mecanum_Robot_State extends CyberarmState {

    // adb connect 192.168.43.1

    private final MecanumRobot robot;
    private float maxSpeed = 1;
    private double halfSpeed = 0.5;

    public Mecanum_Robot_State(MecanumRobot robot) {
        this.robot = robot;
    }

    @Override
    public void exec() {

        if (engine.gamepad1.left_trigger > 0.5){

            if (engine.gamepad1.left_bumper) {

                robot.backRightDrive.setPower(halfSpeed);
                robot.frontRightDrive.setPower(-halfSpeed);
                robot.backLeftDrive.setPower(-halfSpeed);
                robot.frontLeftDrive.setPower(halfSpeed);

            } else if (engine.gamepad1.right_bumper) {

                robot.backRightDrive.setPower(-halfSpeed);
                robot.frontRightDrive.setPower(halfSpeed);
                robot.backLeftDrive.setPower(halfSpeed);
                robot.frontLeftDrive.setPower(-halfSpeed);

            } else if (engine.gamepad1.dpad_right && engine.gamepad1.dpad_up) {

                robot.frontLeftDrive.setPower(-halfSpeed);
                robot.backRightDrive.setPower(-halfSpeed);

            } else if (engine.gamepad1.dpad_left && engine.gamepad1.dpad_up) {

                robot.frontRightDrive.setPower(-halfSpeed);
                robot.backLeftDrive.setPower(-halfSpeed);

            } else if (engine.gamepad1.dpad_right && engine.gamepad1.dpad_down) {

                robot.backLeftDrive.setPower(halfSpeed);
                robot.frontRightDrive.setPower(halfSpeed);

            } else if (engine.gamepad1.dpad_left && engine.gamepad1.dpad_down) {

                robot.backRightDrive.setPower(halfSpeed);
                robot.frontLeftDrive.setPower(halfSpeed);

            }

            else {

                robot.backRightDrive.setPower(engine.gamepad1.right_stick_y * halfSpeed) ;
                robot.frontRightDrive.setPower(engine.gamepad1.right_stick_y * halfSpeed);
                robot.backLeftDrive.setPower(engine.gamepad1.left_stick_y * halfSpeed);
                robot.frontLeftDrive.setPower(engine.gamepad1.left_stick_y * halfSpeed);

            }
        }

        else {

            if (engine.gamepad1.left_bumper) {

                robot.backRightDrive.setPower(maxSpeed);
                robot.frontRightDrive.setPower(-maxSpeed);
                robot.backLeftDrive.setPower(-maxSpeed);
                robot.frontLeftDrive.setPower(maxSpeed);

            } else if (engine.gamepad1.right_bumper) {

                robot.backRightDrive.setPower(-maxSpeed);
                robot.frontRightDrive.setPower(maxSpeed);
                robot.backLeftDrive.setPower(maxSpeed);
                robot.frontLeftDrive.setPower(-maxSpeed);

            } else if (engine.gamepad1.dpad_right && engine.gamepad1.dpad_up) {

                robot.frontLeftDrive.setPower(-maxSpeed);
                robot.backRightDrive.setPower(-maxSpeed);

            } else if (engine.gamepad1.dpad_left && engine.gamepad1.dpad_up) {

                robot.frontRightDrive.setPower(-maxSpeed);
                robot.backLeftDrive.setPower(-maxSpeed);

            } else if (engine.gamepad1.dpad_right && engine.gamepad1.dpad_down) {

            robot.backLeftDrive.setPower(maxSpeed);
            robot.frontRightDrive.setPower(maxSpeed);

            } else if (engine.gamepad1.dpad_left && engine.gamepad1.dpad_down) {

                robot.backRightDrive.setPower(maxSpeed);
                robot.frontLeftDrive.setPower(maxSpeed);

            }  else {

                robot.backRightDrive.setPower(engine.gamepad1.right_stick_y * maxSpeed) ;
                robot.frontRightDrive.setPower(engine.gamepad1.right_stick_y * maxSpeed);
                robot.backLeftDrive.setPower(engine.gamepad1.left_stick_y * maxSpeed);
                robot.frontLeftDrive.setPower(engine.gamepad1.left_stick_y * maxSpeed);

            }

        }

    }
    }

