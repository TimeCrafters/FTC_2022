package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.testing.states.PrototypeBot1;

public class RotationState extends CyberarmState {
    PrototypeBot1 robot;
    public RotationState(PrototypeBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.drivePower = robot.configuration.variable(groupName, actionName, "drivePower").value();
//        this.RobotRotation = robot.configuration.variable(groupName, actionName, "RobotRotation").value();
    }

    private float RobotRotation;
    private double drivePower;

    @Override
    public void exec() {


        RobotRotation = robot.imu.getAngularOrientation().firstAngle;
        if (RobotRotation <= 45) {
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
        }

    }
}
