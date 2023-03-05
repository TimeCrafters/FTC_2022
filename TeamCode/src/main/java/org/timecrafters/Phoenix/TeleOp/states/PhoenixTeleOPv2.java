package org.timecrafters.Phoenix.TeleOp.states;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.Phoenix.PhoenixBot1;

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


    }

    @Override
    public void init() {

    }

    @Override
    public void exec() {

        double y = -engine.gamepad1.left_stick_y; // Remember, this is reversed!
        double x = engine.gamepad1.left_stick_x;
        double rx = engine.gamepad1.right_stick_x;

        double BLPower = (y - x + rx);
        double BRPower = (y + x - rx);
        double FLPower = (y + x + rx);
        double FRPower = (y - x - rx);

        robot.frontLeftDrive.setPower(FLPower * drivePower);
        robot.backLeftDrive.setPower(BLPower * drivePower);
        robot.frontRightDrive.setPower(FRPower * drivePower);
        robot.backRightDrive.setPower(BRPower * drivePower);

        if (engine.gamepad1.left_stick_x > 0.1) {
            robot.backLeftDrive.setPower(BLPower);
            robot.backRightDrive.setPower(BRPower);
            robot.frontLeftDrive.setPower(FLPower);
            robot.frontRightDrive.setPower(FRPower);


        }
    }
}
