package org.timecrafters.Autonomous.States;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.TeleOp.states.PhoenixBot1;

public class DriverParkPlaceState extends CyberarmState {
    private final boolean stateDisabled;
    PhoenixBot1 robot;
    private int RampUpDistance;
    private int RampDownDistance;
    private String intendedPlacement;

    public DriverParkPlaceState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.targetDrivePower = robot.configuration.variable(groupName, actionName, "targetDrivePower").value();
        this.traveledDistance = robot.configuration.variable(groupName, actionName, "traveledDistance").value();
        this.RampUpDistance = robot.configuration.variable(groupName, actionName, "RampUpDistance").value();
        this.RampDownDistance = robot.configuration.variable(groupName, actionName, "RampDownDistance").value();
        this.intendedPlacement = robot.configuration.variable(groupName, actionName, "intendedPlacement").value();
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
        String placement = engine.blackboardGetString("parkPlace");
        if (placement != null) {
            if (!placement.equals(intendedPlacement)){
                    setHasFinished(true);
            }
            if (placement.equals(intendedPlacement)) {
                double delta = traveledDistance - Math.abs(robot.OdometerEncoder.getCurrentPosition());

                if (Math.abs(robot.OdometerEncoder.getCurrentPosition()) <= RampUpDistance) {
                    // ramping up
                    drivePower = (Math.abs((float) robot.OdometerEncoder.getCurrentPosition()) / RampUpDistance) + 0.25;
                } else if (Math.abs(robot.OdometerEncoder.getCurrentPosition()) >= delta) {
                    // ramping down
                    drivePower = ((delta / RampDownDistance) + 0.25);
                } else {
                    // middle ground
                    drivePower = targetDrivePower;
                }

                if (Math.abs(drivePower) > Math.abs(targetDrivePower)) {
                    // This is limiting drive power to the targeted drive power
                    drivePower = targetDrivePower;
                }

                if (targetDrivePower < 0 && drivePower != targetDrivePower) {
                    drivePower = drivePower * -1;
                }

                if (Math.abs(robot.OdometerEncoder.getCurrentPosition()) < traveledDistance) {
                    robot.backLeftDrive.setPower(drivePower);
                    robot.backRightDrive.setPower(drivePower);
                    robot.frontLeftDrive.setPower(drivePower);
                    robot.frontRightDrive.setPower(drivePower);
                } else {
                    robot.backLeftDrive.setPower(0);
                    robot.backRightDrive.setPower(0);
                    robot.frontLeftDrive.setPower(0);
                    robot.frontRightDrive.setPower(0);
                    setHasFinished(true);
                } // else ending
            } // placement equals if statement
//            setHasFinished(true);
        } // end of placement doesn't equal null

    } // end of exec

    @Override
    public void telemetry() {
        engine.telemetry.addData("Position", intendedPlacement);
        engine.telemetry.addData("frontRightDrive", robot.frontRightDrive.getCurrentPosition());
        engine.telemetry.addData("frontLeftDrive", robot.frontLeftDrive.getCurrentPosition());
        engine.telemetry.addData("BackRightDrive", robot.backRightDrive.getCurrentPosition());
        engine.telemetry.addData("BackLeftDrive", robot.backLeftDrive.getCurrentPosition());
        engine.telemetry.addData("BackLeftDrive", robot.OdometerEncoder.getCurrentPosition());

        engine.telemetry.addData("drivePower", drivePower);
        engine.telemetry.addData("targetDrivePower", targetDrivePower);

        engine.telemetry.addData("traveledDistance", traveledDistance);
        engine.telemetry.addData("RampUpDistance", RampUpDistance);
        engine.telemetry.addData("RampDownDistance", RampDownDistance);

    } // end of telemetry
} // end of class
