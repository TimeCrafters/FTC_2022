package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.testing.states.PrototypeBot1;

public class RotationState extends CyberarmState {
    private final boolean stateDisabled;
    PrototypeBot1 robot;
    public RotationState(PrototypeBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.drivePower = robot.configuration.variable(groupName, actionName, "drivePower").value();
        this.targetRotation = robot.configuration.variable(groupName, actionName, "targetRotation").value();
        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;


    }

    private double drivePower;
    private float targetRotation;
    float RobotRotation;
    private double RotationTarget, DeltaRotation;
    private double RotationDirectionMinimum;


    public void CalculateDeltaRotation() {
        if (RotationTarget >= 0 && RobotRotation >= 0) {
            DeltaRotation = Math.abs(RotationTarget - RobotRotation);
        }
        else if (RotationTarget <= 0 && RobotRotation <= 0) {
            DeltaRotation = Math.abs(RotationTarget - RobotRotation);
        }
        else if (RotationTarget >= 0 && RobotRotation <= 0) {
            DeltaRotation = Math.abs(RotationTarget + RobotRotation);
        }
        else if (RotationTarget <=0 && RobotRotation >= 0) {
            DeltaRotation = Math.abs(RobotRotation + RotationTarget);
        }
    }

    @Override
    public void exec() {
        if (stateDisabled){
            setHasFinished(true);
            return;
        }

        RobotRotation = robot.imu.getAngularOrientation().firstAngle;
        RotationTarget = targetRotation;
        CalculateDeltaRotation();
        if (drivePower < 0){
            RotationDirectionMinimum = -0.3;
        } else {
            RotationDirectionMinimum = 0.3;
        }
        drivePower = (drivePower * DeltaRotation/180) + RotationDirectionMinimum;
        if (RobotRotation <= targetRotation -3 || RobotRotation >= targetRotation + 3) {
            robot.backLeftDrive.setPower(-drivePower);
            robot.backRightDrive.setPower(drivePower);
            robot.frontLeftDrive.setPower(-drivePower);
            robot.frontRightDrive.setPower(drivePower);
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
        engine.telemetry.addData("Robot IMU Rotation", RobotRotation);
        engine.telemetry.addData("Robot Target Rotation", targetRotation);
        engine.telemetry.addData("Drive Power", drivePower);

    }
}
