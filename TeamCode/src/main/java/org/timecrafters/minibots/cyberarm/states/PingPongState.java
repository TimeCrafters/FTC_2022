package org.timecrafters.minibots.cyberarm.states;

import org.cyberarm.engine.V2.CyberarmState;

public class PingPongState extends CyberarmState {

    private final MecanumRobot robot;
    public PingPongState(MecanumRobot robot) {
        this.robot = robot;
    }
    @Override
    public void exec() {

        if (engine.gamepad1.left_bumper) {
            robot.frontLeftDrive.setPower(1);
            robot.backLeftDrive.setPower(-1);
            robot.backRightDrive.setPower(1);
            robot.frontRightDrive.setPower(-1);
        }
        else if (engine.gamepad1.right_bumper) {
            robot.frontLeftDrive.setPower(-1);
            robot.backLeftDrive.setPower(1);
            robot.frontRightDrive.setPower(1);
            robot.backRightDrive.setPower(-1);
        }

        else {
            robot.backLeftDrive.setPower(engine.gamepad1.left_stick_y);
            robot.frontLeftDrive.setPower(engine.gamepad1.left_stick_y);
            robot.backRightDrive.setPower(engine.gamepad1.right_stick_y);
            robot.frontRightDrive.setPower(engine.gamepad1.right_stick_y);
        }

    }
}
