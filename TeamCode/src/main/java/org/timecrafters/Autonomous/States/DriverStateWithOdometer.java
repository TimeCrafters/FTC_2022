package org.timecrafters.Autonomous.States;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.TeleOp.states.PhoenixBot1;

public class DriverStateWithOdometer extends CyberarmState {
    private final boolean stateDisabled;
    PhoenixBot1 robot;
    private int RampUpDistance;
    private int RampDownDistance;
    private int maximumTolerance;
    public DriverStateWithOdometer(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.targetDrivePower = robot.configuration.variable(groupName, actionName, "targetDrivePower").value();
        this.traveledDistance = robot.configuration.variable(groupName, actionName, "traveledDistance").value();
        this.RampUpDistance = robot.configuration.variable(groupName, actionName, "RampUpDistance").value();
        this.RampDownDistance = robot.configuration.variable(groupName, actionName, "RampDownDistance").value();
        this.maximumTolerance = robot.configuration.variable(groupName, actionName, "maximumTolerance").value();

        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;
    }
    private double drivePower, targetDrivePower;
    private int traveledDistance;

    @Override
    public void start() {
        robot.frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void exec() {
        if (stateDisabled) {
            setHasFinished(true);
            return;
        }

        double CurrentPosition = robot.OdometerEncoder.getCurrentPosition();
        double delta = traveledDistance - Math.abs(CurrentPosition);

        if (Math.abs(CurrentPosition) <= RampUpDistance){
            // ramping up
           drivePower = (Math.abs((float)CurrentPosition) / RampUpDistance) + 0.25;
        }
        else if (Math.abs(CurrentPosition) >= delta){
            // ramping down
            drivePower = ((delta / RampDownDistance) + 0.25);
            } else {
            // middle ground
            drivePower = targetDrivePower;
        }

        if (Math.abs(drivePower) > Math.abs(targetDrivePower)){
            // This is limiting drive power to the targeted drive power
            drivePower = targetDrivePower;
        }

        if (targetDrivePower < 0 && drivePower != targetDrivePower) {
            drivePower = drivePower * -1;
        }

        if (Math.abs(CurrentPosition) < traveledDistance - maximumTolerance){
            robot.backLeftDrive.setPower(drivePower);
            robot.backRightDrive.setPower(drivePower);
            robot.frontLeftDrive.setPower(drivePower);
            robot.frontRightDrive.setPower(drivePower);

        }
        else if (Math.abs(CurrentPosition) > traveledDistance + maximumTolerance) {
            robot.backLeftDrive.setPower(targetDrivePower * -0.25);
            robot.backRightDrive.setPower(targetDrivePower * -0.25);
            robot.frontLeftDrive.setPower(targetDrivePower * -0.25);
            robot.frontRightDrive.setPower(targetDrivePower * -0.25);
        } else {
            robot.backLeftDrive.setPower(0);
            robot.backRightDrive.setPower(0);
            robot.frontLeftDrive.setPower(0);
            robot.frontRightDrive.setPower(0);
            setHasFinished(true);
        }


    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("frontRightDrive", robot.frontRightDrive.getCurrentPosition());
        engine.telemetry.addData("frontLeftDrive", robot.frontLeftDrive.getCurrentPosition());
        engine.telemetry.addData("BackRightDrive", robot.backRightDrive.getCurrentPosition());
        engine.telemetry.addData("BackLeftDrive", robot.backLeftDrive.getCurrentPosition());
        engine.telemetry.addData("Odometer", robot.OdometerEncoder.getCurrentPosition());

        engine.telemetry.addData("drivePower", drivePower);
        engine.telemetry.addData("targetDrivePower", targetDrivePower);

        engine.telemetry.addData("traveledDistance", traveledDistance);
        engine.telemetry.addData("RampUpDistance", RampUpDistance);
        engine.telemetry.addData("RampDownDistance", RampDownDistance);

    }
}
