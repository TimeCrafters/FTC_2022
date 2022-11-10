package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.testing.states.PhoenixBot1;

public class RotationState extends CyberarmState {
    private final boolean stateDisabled;
    PhoenixBot1 robot;
    public RotationState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.drivePower = robot.configuration.variable(groupName, actionName, "DrivePower").value();
        this.targetRotation = robot.configuration.variable(groupName, actionName, "targetRotation").value();
        drivePowerVariable = drivePower;
        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;


    }

    private double drivePower;
    private float targetRotation;
    float RobotRotation;
    private double RotationTarget;
    private double RotationDirectionMinimum;
    private String debugStatus = "?";
    private double drivePowerVariable;


    @Override
    public void exec() {
        if (stateDisabled){
            setHasFinished(true);
            return;
            } // end of if

        RobotRotation = robot.imu.getAngularOrientation().firstAngle;

        if (Math.abs(Math.abs(targetRotation) - Math.abs(RobotRotation)) < 20){
            drivePowerVariable = 0.3 * drivePower;
            debugStatus = "Rotate Slow";
        } // end of if
        else {
            drivePowerVariable = drivePower * 0.75;
            debugStatus = "Rotate Fast";
        }

        if (RobotRotation >= targetRotation + 1){
            drivePowerVariable = Math.abs(drivePowerVariable);
        } else {
            drivePowerVariable = -1 * Math.abs(drivePowerVariable);
        }

        if (RobotRotation <= targetRotation -1 || RobotRotation >= targetRotation + 1) {
            robot.backLeftDrive.setPower(drivePowerVariable);
            robot.backRightDrive.setPower(-drivePowerVariable);
            robot.frontLeftDrive.setPower(drivePowerVariable);
            robot.frontRightDrive.setPower(-drivePowerVariable);
        } else
        {
            robot.backLeftDrive.setPower(0);
            robot.backRightDrive.setPower(0);
            robot.frontLeftDrive.setPower(0);
            robot.frontRightDrive.setPower(0);
            setHasFinished(true);
        }

    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("DEBUG Status", debugStatus);

        engine.telemetry.addLine();

        engine.telemetry.addData("Robot IMU Rotation", RobotRotation);
        engine.telemetry.addData("Robot Target Rotation", targetRotation);
        engine.telemetry.addData("Drive Power", drivePowerVariable);

    }
}
