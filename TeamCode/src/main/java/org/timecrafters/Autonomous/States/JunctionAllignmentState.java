package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.TeleOp.states.PhoenixBot1;

public class JunctionAllignmentState extends CyberarmState {
    private final boolean stateDisabled;
    PhoenixBot1 robot;
    private double TargetSensorDistance;
    private final String targetedJunction;
    private final double drivePower;

    public JunctionAllignmentState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.drivePower = robot.configuration.variable(groupName, actionName, "DrivePower").value();
        this.targetedJunction = robot.configuration.variable(groupName, actionName, "targetedJunction").value();
        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;
    }

    @Override
    public void exec() {

        if (stateDisabled){
            setHasFinished(true);
        } else {

            double leftDistance = robot.leftPoleDistance.getDistance(DistanceUnit.MM);
            double rightDistance = robot.rightPoleDistance.getDistance(DistanceUnit.MM);


            // TODO: 12/11/2022 Make sure these are the correct values for the distance from low, mid, and high junctions!!!
            switch (targetedJunction) {
                case "low":
                    TargetSensorDistance = 90.0;
                    break;
                case "mid":
                    TargetSensorDistance = 135.0;
                    break;
                case "high":
                    TargetSensorDistance = 200.0;
                    break;
            }
                // the state is finished if the distance sensors are at the correct distance.
                if ((leftDistance > TargetSensorDistance -2 || leftDistance < TargetSensorDistance + 2) && (rightDistance > TargetSensorDistance -2 || rightDistance < TargetSensorDistance + 2)) {
                    robot.frontLeftDrive.setPower(0);
                    robot.frontRightDrive.setPower(0);
                    robot.backLeftDrive.setPower(0);
                    robot.backRightDrive.setPower(0);
                    setHasFinished(true);
                    }
                // the robot is lined up but needs to drive forward till the robot is at the specified distance
                else if (leftDistance > TargetSensorDistance && rightDistance > TargetSensorDistance){
                    robot.frontLeftDrive.setPower(drivePower);
                    robot.frontRightDrive.setPower(drivePower);
                    robot.backLeftDrive.setPower(drivePower);
                    robot.backRightDrive.setPower(drivePower);
                    }
                // the robot is lined up but needs to drive backward till the robot is at the specified distance
                else if (leftDistance < TargetSensorDistance && rightDistance < TargetSensorDistance){
                    robot.frontLeftDrive.setPower(-drivePower);
                    robot.frontRightDrive.setPower(-drivePower);
                    robot.backLeftDrive.setPower(-drivePower);
                    robot.backRightDrive.setPower(-drivePower);
                    }
                // the robot is going to rotate CCW until a distance is met
                else if (leftDistance > TargetSensorDistance && rightDistance < TargetSensorDistance) {
                    robot.frontLeftDrive.setPower(-drivePower);
                    robot.frontRightDrive.setPower(drivePower);
                    robot.backLeftDrive.setPower(-drivePower);
                    robot.backRightDrive.setPower(drivePower);
                    }
                // the robot is going to rotate CW until a distance is met
                else if (leftDistance < TargetSensorDistance && rightDistance > TargetSensorDistance) {
                    robot.frontLeftDrive.setPower(drivePower);
                    robot.frontRightDrive.setPower(-drivePower);
                    robot.backLeftDrive.setPower(drivePower);
                    robot.backRightDrive.setPower(-drivePower);
                    }
                // The right sensor is aligned but the robot must rotate CW with only the left side powered
                else if (leftDistance < TargetSensorDistance && (rightDistance > TargetSensorDistance -2 || rightDistance < TargetSensorDistance + 2)) {
                    robot.frontLeftDrive.setPower(drivePower);
                    robot.frontRightDrive.setPower(0);
                    robot.backLeftDrive.setPower(drivePower);
                    robot.backRightDrive.setPower(0);
                    }
                // The right sensor is aligned but the robot must rotate rotate CCW with only the left side powered
                else if (leftDistance > TargetSensorDistance && (rightDistance > TargetSensorDistance -2 || rightDistance < TargetSensorDistance + 2)) {
                    robot.frontLeftDrive.setPower(-drivePower);
                    robot.frontRightDrive.setPower(0);
                    robot.backLeftDrive.setPower(-drivePower);
                    robot.backRightDrive.setPower(0);
                    }
                // The left sensor is aligned but the robot must rotate CW with only the right side powered
                else if ((leftDistance > TargetSensorDistance -2 || leftDistance < TargetSensorDistance + 2) && rightDistance < TargetSensorDistance) {
                    robot.frontLeftDrive.setPower(0);
                    robot.frontRightDrive.setPower(-drivePower);
                    robot.backLeftDrive.setPower(0);
                    robot.backRightDrive.setPower(-drivePower);
                    }
                // The left sensor is aligned but the robot must rotate CCW with only the right side powered
                else if ((leftDistance > TargetSensorDistance -2 || leftDistance < TargetSensorDistance + 2) && rightDistance > TargetSensorDistance) {
                    robot.frontLeftDrive.setPower(0);
                    robot.frontRightDrive.setPower(drivePower);
                    robot.backLeftDrive.setPower(0);
                    robot.backRightDrive.setPower(drivePower);
                    }
        }
    }
}
