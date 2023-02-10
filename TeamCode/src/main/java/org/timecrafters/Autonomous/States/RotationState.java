package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.timecrafters.Autonomous.TeleOp.states.PhoenixBot1;

public class RotationState extends CyberarmState {
    private final boolean stateDisabled;
    PhoenixBot1 robot;

    public RotationState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.drivePower = robot.configuration.variable(groupName, actionName, "DrivePower").value();
        this.targetRotation = robot.configuration.variable(groupName, actionName, "targetRotation").value();
        this.ClockWiseRotation = robot.configuration.variable(groupName, actionName, "ClockWiseRotation").value();
        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;


    }

    private double drivePower;
    private float targetRotation;
    float CurrentRotation;
    private String debugStatus = "?";
    private double drivePowerVariable;
    private double leftCompensator;
    private double RightCompensator;
    private boolean ClockWiseRotation;
    private int RotationStage;
    private double rotationDirection;
    private long lastStepTime = 0;

    @Override
    public void start() {

        leftCompensator = robot.OdometerEncoderLeft.getCurrentPosition();
        RightCompensator = robot.OdometerEncoderRight.getCurrentPosition();

        RotationStage = 0;
    }

    @Override
    public void exec() {
        if (stateDisabled) {
            setHasFinished(true);
            return;
        } //


        CurrentRotation = (float) robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

        if (RotationStage == 0) {

            drivePowerVariable = ((Math.abs(CurrentRotation - targetRotation) / 90) * (drivePower - robot.ROTATION_MINIMUM_POWER)) + robot.ROTATION_MINIMUM_POWER;

            if (ClockWiseRotation) {
                rotationDirection = 1;
            } else {
                rotationDirection = -1;
            }


            robot.backLeftDrive.setPower(drivePowerVariable * rotationDirection);
            robot.backRightDrive.setPower(-drivePowerVariable * rotationDirection);
            robot.frontLeftDrive.setPower(drivePowerVariable * rotationDirection);
            robot.frontRightDrive.setPower(-drivePowerVariable * rotationDirection);

            if (Math.abs(Math.abs(CurrentRotation) - Math.abs(targetRotation)) <= robot.ROTATION_TOLERANCE &&
                    (RotationStage == 0) &&
                    (CurrentRotation - targetRotation <= robot.ROTATION_TOLERANCE)) {
                RotationStage = 1;
                lastStepTime = System.currentTimeMillis();
            }
        }

        if (RotationStage == 1) {
            robot.backLeftDrive.setPower(0);
            robot.backRightDrive.setPower(0);
            robot.frontLeftDrive.setPower(0);
            robot.frontRightDrive.setPower(0);
            if (System.currentTimeMillis() - lastStepTime >= robot.PAUSE_ON_ROTATION) {
                RotationStage = 2;
            }
        }

        if (RotationStage == 2) {

            if (CurrentRotation - targetRotation > robot.ROTATION_TOLERANCE) {
                // CW

                robot.frontRightDrive.setPower(-robot.ROTATION_MINIMUM_POWER);
                robot.frontLeftDrive.setPower(robot.ROTATION_MINIMUM_POWER);
                robot.backRightDrive.setPower(-robot.ROTATION_MINIMUM_POWER);
                robot.backLeftDrive.setPower(robot.ROTATION_MINIMUM_POWER);

                lastStepTime = System.currentTimeMillis();

            } else if (CurrentRotation - targetRotation < -robot.ROTATION_TOLERANCE) {
                // CCW

                robot.frontRightDrive.setPower(robot.ROTATION_MINIMUM_POWER);
                robot.frontLeftDrive.setPower(-robot.ROTATION_MINIMUM_POWER);
                robot.backRightDrive.setPower(robot.ROTATION_MINIMUM_POWER);
                robot.backLeftDrive.setPower(-robot.ROTATION_MINIMUM_POWER);

                lastStepTime = System.currentTimeMillis();

            } else {
                robot.frontRightDrive.setPower(0);
                robot.frontLeftDrive.setPower(0);
                robot.backRightDrive.setPower(0);
                robot.backLeftDrive.setPower(0);

                if (System.currentTimeMillis() - lastStepTime >= robot.PAUSE_ON_ROTATION) {
                    RotationStage = 3;
                }
            }
        }

        if (RotationStage == 3) {
            RotationStage ++;
            setHasFinished(true);
            }
        }



    @Override
    public void telemetry() {
        engine.telemetry.addData("DEBUG Status", debugStatus);

        engine.telemetry.addLine();

        engine.telemetry.addData("Robot IMU Rotation", CurrentRotation);
        engine.telemetry.addData("Robot Target Rotation", targetRotation);
        engine.telemetry.addData("Drive Power", drivePowerVariable);
        engine.telemetry.addData("front right power", robot.frontRightDrive.getPower());
        engine.telemetry.addData("front left power", robot.frontLeftDrive.getPower());
        engine.telemetry.addData("back left power", robot.backLeftDrive.getPower());
        engine.telemetry.addData("back right power", robot.backRightDrive.getPower());
        engine.telemetry.addData("RotationStage", RotationStage);

    }
}
