package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.testing.states.PrototypeBot1;

public class DriverState extends CyberarmState {
    PrototypeBot1 robot;
    public DriverState(PrototypeBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.drivePower = robot.configuration.variable(groupName, actionName, "drivePower").value();
        this.traveledDistance = robot.configuration.variable(groupName, actionName, "traveledDistance").value();

    }

    private float RobotRotation;
    private double drivePower;
    private int RobotPosition,RobotStartingPosition,traveledDistance;

    @Override
    public void exec() {

        if (RobotPosition - RobotStartingPosition < traveledDistance){
            drivePower = 1;
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
        }

    }
}
