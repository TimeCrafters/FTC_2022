package org.timecrafters.TeleOp.states;

import org.cyberarm.engine.V2.CyberarmState;


public class SteeringDriveExperiment extends CyberarmState {

    private final PhoenixBot1 robot;
    public boolean A;
    public boolean X;
    public boolean Y;
    private double drivePower = 1;
    private boolean bprev;


    public SteeringDriveExperiment(PhoenixBot1 robot) {
        this.robot = robot;
    }

    @Override
    public void exec() {

        //Gamepad Read

        A = engine.gamepad1.a;
        X = engine.gamepad1.x;
        Y = engine.gamepad1.y;

        //Drivetrain Variables
        double y = -engine.gamepad1.left_stick_y; // Remember, this is reversed! because of the negative
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

        double denominator = Math.max(Math.abs(y) + Math.abs(rx), 1);
        double frontLeftPower = (y + rx) / denominator;
        double backLeftPower = (y + rx) / denominator;
        double frontRightPower = (y - rx) / denominator;
        double backRightPower = (y - rx) / denominator;

        robot.frontLeftDrive.setPower(frontLeftPower * drivePower);
        robot.backLeftDrive.setPower(backLeftPower * drivePower);
        robot.frontRightDrive.setPower(frontRightPower * drivePower);
        robot.backRightDrive.setPower(backRightPower * drivePower);


//------------------------------------------------------------------------------------------------------------------------------------
    }
}


