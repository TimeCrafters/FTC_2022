package org.timecrafters.Autonomous.States;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.TeleOp.states.PhoenixBot1;

public class DriverStateWithOdometer extends CyberarmState {
    private final boolean stateDisabled;
    PhoenixBot1 robot;
    private double RampUpDistance;
    private double RampDownDistance;
    private double maximumTolerance;
    private float direction;
    private boolean targetAchieved = false;
    private double CurrentPosition;
    public final double WHEEL_CIRCUMFERENCE = 7.42108499;
    public final int COUNTS_PER_REVOLUTION = 8192;
    public double MINIMUM_POWER = 0.25;
    public final double distanceMultiplier;
    public double startOfRampUpRight;
    public double startOfRampDownRight;
    public double startOfRampUpLeft;
    public double startOfRampDownLeft;
    public double endOfRampUpRight;
    public double endOfRampDownRight;
    public double endOfRampUpLeft;
    public double endOfRampDownLeft;

    public DriverStateWithOdometer(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.targetDrivePower = robot.configuration.variable(groupName, actionName, "targetDrivePower").value();
        this.traveledDistance = robot.configuration.variable(groupName, actionName, "traveledDistance").value();
        this.RampUpDistance = robot.configuration.variable(groupName, actionName, "RampUpDistance").value();
        this.RampDownDistance = robot.configuration.variable(groupName, actionName, "RampDownDistance").value();
        this.maximumTolerance = robot.configuration.variable(groupName, actionName, "maximumTolerance").value();
        this.direction = robot.configuration.variable(groupName, actionName, "direction").value();
        this.distanceMultiplier = robot.configuration.variable(groupName, actionName, "distanceMultiplier").value();

        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;
    }

    private double drivePower, targetDrivePower;
    private int traveledDistance;

    @Override
    public void start() {

        robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.OdometerEncoderRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.OdometerEncoderLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        traveledDistance = (int) ((traveledDistance * (COUNTS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE)) * distanceMultiplier);
        RampUpDistance = (int) ((RampUpDistance * (COUNTS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE)) * distanceMultiplier);
        RampDownDistance = (int) ((RampDownDistance * (COUNTS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE)) * distanceMultiplier);
        maximumTolerance = (int) ((maximumTolerance * (COUNTS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE)) * distanceMultiplier);


        if (drivePower > 0) {
            startOfRampUpRight = robot.OdometerEncoderRight.getCurrentPosition();
            endOfRampUpRight = robot.OdometerEncoderRight.getCurrentPosition() + RampUpDistance;
            startOfRampDownRight = robot.OdometerEncoderRight.getCurrentPosition() + traveledDistance - RampDownDistance;
            endOfRampDownRight = robot.OdometerEncoderRight.getCurrentPosition() + traveledDistance;

            startOfRampUpLeft = robot.OdometerEncoderLeft.getCurrentPosition();
            endOfRampUpLeft = robot.OdometerEncoderLeft.getCurrentPosition() + RampUpDistance;
            startOfRampDownLeft = robot.OdometerEncoderLeft.getCurrentPosition() + traveledDistance - RampDownDistance;
            endOfRampDownLeft = robot.OdometerEncoderLeft.getCurrentPosition() + traveledDistance;
        } else {

            startOfRampUpRight = robot.OdometerEncoderRight.getCurrentPosition();
            endOfRampUpRight = robot.OdometerEncoderRight.getCurrentPosition() - RampUpDistance;
            startOfRampDownRight = robot.OdometerEncoderRight.getCurrentPosition() - traveledDistance + RampDownDistance;
            endOfRampDownRight = robot.OdometerEncoderRight.getCurrentPosition() - traveledDistance;

            startOfRampUpLeft = robot.OdometerEncoderLeft.getCurrentPosition();
            endOfRampUpLeft = robot.OdometerEncoderLeft.getCurrentPosition() - RampUpDistance;
            startOfRampDownLeft = robot.OdometerEncoderLeft.getCurrentPosition() - traveledDistance + RampDownDistance;
            endOfRampDownLeft = robot.OdometerEncoderLeft.getCurrentPosition() - traveledDistance;

        }

    }

    @Override
    public void exec() {


        if (stateDisabled) {
            setHasFinished(true);
            return;
        }

        double RightCurrentPosition = robot.OdometerEncoderRight.getCurrentPosition();
        double LeftCurrentPosition = robot.OdometerEncoderLeft.getCurrentPosition();

        // ramping up
        if (RightCurrentPosition >= startOfRampUpRight && RightCurrentPosition <= endOfRampUpRight &&
                LeftCurrentPosition >= startOfRampUpLeft && LeftCurrentPosition <= endOfRampUpLeft) {

            if (targetDrivePower > 0) {
                drivePower = (targetDrivePower - MINIMUM_POWER) * ((RightCurrentPosition - startOfRampUpRight) / RampUpDistance) + MINIMUM_POWER;
            } else {
                drivePower = (targetDrivePower + MINIMUM_POWER) * ((startOfRampUpRight - RightCurrentPosition) / RampUpDistance) - MINIMUM_POWER;
            }
        }

        // Driving Normal
        else if (RightCurrentPosition >= endOfRampUpRight && RightCurrentPosition <= startOfRampDownRight &&
                LeftCurrentPosition >= endOfRampUpLeft && LeftCurrentPosition <= startOfRampDownLeft) {

            drivePower = targetDrivePower;

        }

        // Ramping down
        else if (RightCurrentPosition >= startOfRampDownRight && RightCurrentPosition <= endOfRampDownRight &&
                LeftCurrentPosition >= startOfRampDownLeft && LeftCurrentPosition <= endOfRampDownLeft) {
            if (targetDrivePower > 0) {
                drivePower = (targetDrivePower + MINIMUM_POWER) * ((startOfRampDownRight - RightCurrentPosition) / RampDownDistance) - MINIMUM_POWER;
            } else {
                drivePower = (targetDrivePower - MINIMUM_POWER) * ((RightCurrentPosition - startOfRampDownRight) / RampDownDistance) + MINIMUM_POWER;
            }
        }
            if (Math.abs(drivePower) > Math.abs(targetDrivePower)) {
                // This is limiting drive power to the targeted drive power
                drivePower = targetDrivePower;
            }

            if (targetDrivePower < 0 && drivePower > 0) {
                drivePower = drivePower * -1;
            }

            if (LeftCurrentPosition - traveledDistance < maximumTolerance || Math.abs(RightCurrentPosition - traveledDistance) < maximumTolerance) {
                if (targetAchieved) {
                    drivePower = drivePower * 0.15;

                    if (Math.abs(drivePower) < 0.15) {
                        if (drivePower < 0) {
                            drivePower = -0.15;
                        } else {
                            drivePower = 0.15;
                        }
                    }
                }
                robot.backLeftDrive.setPower(drivePower * robot.VEER_COMPENSATION_DBL);
                robot.backRightDrive.setPower(drivePower);
                robot.frontLeftDrive.setPower(drivePower * robot.VEER_COMPENSATION_DBL);
                robot.frontRightDrive.setPower(drivePower);

            } else if (Math.abs(LeftCurrentPosition) > traveledDistance + maximumTolerance || Math.abs(RightCurrentPosition) > traveledDistance + maximumTolerance) {
                targetAchieved = true;

                drivePower = targetDrivePower * -0.15;

                if (Math.abs(drivePower) < 0.15) {
                    if (drivePower < 0) {
                        drivePower = -0.15;
                    } else {
                        drivePower = 0.15;
                    }
                }

                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(drivePower);


            } else {

                if (Math.abs(LeftCurrentPosition) > Math.abs(RightCurrentPosition)) {

                    if (Math.abs(Math.abs(LeftCurrentPosition) - Math.abs(RightCurrentPosition)) < 20) {
                        drivePower = 0;
                    } else {
                        drivePower = 0.15;
                        robot.backLeftDrive.setPower(-drivePower);
                        robot.backRightDrive.setPower(drivePower);
                        robot.frontLeftDrive.setPower(-drivePower);
                        robot.frontRightDrive.setPower(drivePower);
                    }
                }

                if (Math.abs(LeftCurrentPosition) < Math.abs(RightCurrentPosition)) {

                    if (Math.abs(LeftCurrentPosition) == Math.abs(RightCurrentPosition)) {
                        drivePower = 0;
                    } else {
                        drivePower = 0.15;
                        robot.backLeftDrive.setPower(drivePower);
                        robot.backRightDrive.setPower(-drivePower);
                        robot.frontLeftDrive.setPower(drivePower);
                        robot.frontRightDrive.setPower(-drivePower);
                    }
                } else {
                    robot.backLeftDrive.setPower(0);
                    robot.backRightDrive.setPower(0);
                    robot.frontLeftDrive.setPower(0);
                    robot.frontRightDrive.setPower(0);
                    setHasFinished(true);
                }
            }

//
        }

        @Override
        public void telemetry () {
            engine.telemetry.addData("frontRightDrive", robot.frontRightDrive.getCurrentPosition());
            engine.telemetry.addData("frontLeftDrive", robot.frontLeftDrive.getCurrentPosition());
            engine.telemetry.addData("BackRightDrive", robot.backRightDrive.getCurrentPosition());
            engine.telemetry.addData("BackLeftDrive", robot.backLeftDrive.getCurrentPosition());
            engine.telemetry.addData("frontRightDrive", robot.frontRightDrive.getPower());
            engine.telemetry.addData("frontLeftDrive", robot.frontLeftDrive.getPower());
            engine.telemetry.addData("BackRightDrive", robot.backRightDrive.getPower());
            engine.telemetry.addData("BackLeftDrive", robot.backLeftDrive.getPower());
            engine.telemetry.addData("Odometer", robot.OdometerEncoderRight.getCurrentPosition());
            engine.telemetry.addData("imu 1 angle", robot.imu.getAngularOrientation().firstAngle);
            engine.telemetry.addData("imu 2 angle", robot.imu.getAngularOrientation().secondAngle);
            engine.telemetry.addData("imu 3 angle", robot.imu.getAngularOrientation().thirdAngle);

            engine.telemetry.addData("Target Achieved", targetAchieved);


            engine.telemetry.addData("drivePower", drivePower);
            engine.telemetry.addData("targetDrivePower", targetDrivePower);

            engine.telemetry.addData("traveledDistance", traveledDistance);
            engine.telemetry.addData("RampUpDistance", RampUpDistance);
            engine.telemetry.addData("RampDownDistance", RampDownDistance);

            Log.i("TELEMETRY", "imu 1 angle:: " + robot.imu.getAngularOrientation().firstAngle);
            Log.i("TELEMETRY", "imu 2 angle:: " + robot.imu.getAngularOrientation().secondAngle);
            Log.i("TELEMETRY", "imu 3 angle:: " + robot.imu.getAngularOrientation().thirdAngle);

        }
    }

