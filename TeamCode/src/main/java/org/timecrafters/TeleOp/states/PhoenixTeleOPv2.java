package org.timecrafters.TeleOp.states;

import org.cyberarm.engine.V2.CyberarmState;

public class PhoenixTeleOPv2 extends CyberarmState {
    private double drivePower = 1;
    PhoenixBot1 robot;
    public PhoenixTeleOPv2(PhoenixBot1 robot) {
        this.robot = robot;
    }

    @Override
    public void start() {
        addParallelState(new TeleOPArmDriver(robot));
        addParallelState(new TeleOPTankDriver(robot));
        double y = -engine.gamepad1.left_stick_y; // Remember, this is reversed!
        double x = engine.gamepad1.left_stick_x;
        double rx = engine.gamepad1.right_stick_x;

        double backLeftPower = (y - x + rx);
        double backRightPower = (y + x - rx);
        double frontLeftPower = (y + x + rx);
        double frontRightPower = (y - x - rx);

        robot.frontLeftDrive.setPower(frontLeftPower * drivePower);
        robot.backLeftDrive.setPower(backLeftPower * drivePower);
        robot.frontRightDrive.setPower(frontRightPower * drivePower);
        robot.backRightDrive.setPower(backRightPower * drivePower);

    }

    @Override
    public void init() {

    }

    @Override
    public void exec() {
        if (engine.gamepad1.left_stick_x > 0.1) {

        }

    }
}
