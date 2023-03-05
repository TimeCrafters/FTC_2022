package org.timecrafters.Phoenix.Autonomous.States;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.timecrafters.Phoenix.PhoenixBot1;

public class DriverParkPlaceState extends CyberarmState {
    private final boolean stateDisabled;
    PhoenixBot1 robot;
    private int RampUpDistance;
    private int RampDownDistance;
    private String intendedPlacement;
    public final double WHEEL_CIRCUMFERENCE = 7.42108499;
    public final double COUNTS_PER_REVOLUTION = 8192;
    private double maximumTolerance;
    private float direction;
    private boolean targetAchieved = false;
    public double startOfRampUpRight;
    public double startOfRampDownRight;
    public double startOfRampUpLeft;
    public double startOfRampDownLeft;
    public double endOfRampUpRight;
    public double endOfRampDownRight;
    public double endOfRampUpLeft;
    public double endOfRampDownLeft;
    public int driveStage;
    public float currentAngle;
    public double currentHorizontalEncoder;




    public DriverParkPlaceState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.targetDrivePower = robot.configuration.variable(groupName, actionName, "targetDrivePower").value();
        this.traveledDistance = robot.configuration.variable(groupName, actionName, "traveledDistance").value();
        this.RampUpDistance = robot.configuration.variable(groupName, actionName, "RampUpDistance").value();
        this.RampDownDistance = robot.configuration.variable(groupName, actionName, "RampDownDistance").value();
        this.maximumTolerance = robot.configuration.variable(groupName, actionName, "maximumTolerance").value();
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

        traveledDistance = (int) ((traveledDistance * (COUNTS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE)) * robot.DISTANCE_MULTIPLIER);
        RampUpDistance = (int) ((RampUpDistance * (COUNTS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE)) * robot.DISTANCE_MULTIPLIER);
        RampDownDistance = (int) ((RampDownDistance * (COUNTS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE)) * robot.DISTANCE_MULTIPLIER);
        maximumTolerance = (int) ((maximumTolerance * (COUNTS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE)) * robot.DISTANCE_MULTIPLIER);

        if (targetDrivePower > 0) {
            startOfRampUpRight = robot.OdometerEncoderRight.getCurrentPosition() - 100;
            endOfRampUpRight = robot.OdometerEncoderRight.getCurrentPosition() + RampUpDistance;
            startOfRampDownRight = robot.OdometerEncoderRight.getCurrentPosition() + traveledDistance - RampDownDistance;
            endOfRampDownRight = robot.OdometerEncoderRight.getCurrentPosition() + traveledDistance;

            startOfRampUpLeft = robot.OdometerEncoderLeft.getCurrentPosition() - 100;
            endOfRampUpLeft = robot.OdometerEncoderLeft.getCurrentPosition() + RampUpDistance;
            startOfRampDownLeft = robot.OdometerEncoderLeft.getCurrentPosition() + traveledDistance - RampDownDistance;
            endOfRampDownLeft = robot.OdometerEncoderLeft.getCurrentPosition() + traveledDistance;

        } else {

            startOfRampUpRight = robot.OdometerEncoderRight.getCurrentPosition() + 100;
            endOfRampUpRight = robot.OdometerEncoderRight.getCurrentPosition() - RampUpDistance;
            startOfRampDownRight = robot.OdometerEncoderRight.getCurrentPosition() - traveledDistance + RampDownDistance;
            endOfRampDownRight = robot.OdometerEncoderRight.getCurrentPosition() - traveledDistance;

            startOfRampUpLeft = robot.OdometerEncoderLeft.getCurrentPosition() + 100;
            endOfRampUpLeft = robot.OdometerEncoderLeft.getCurrentPosition() - RampUpDistance;
            startOfRampDownLeft = robot.OdometerEncoderLeft.getCurrentPosition() - traveledDistance + RampDownDistance;
            endOfRampDownLeft = robot.OdometerEncoderLeft.getCurrentPosition() - traveledDistance;

        }

        driveStage = 0;
    }

    @Override
    public void exec() {
        if (stateDisabled) {
            setHasFinished(true);
            return;
        }
        String placement = engine.blackboardGetString("parkPlace");
        if (placement == null || !placement.equals(intendedPlacement)) {

            setHasFinished(true);

        } else {

                double RightCurrentPosition = robot.OdometerEncoderRight.getCurrentPosition();
                double LeftCurrentPosition = robot.OdometerEncoderLeft.getCurrentPosition();

                // Driving Forward
                if (targetDrivePower > 0 && driveStage == 0) {

                    // ramping up
                    if ((RightCurrentPosition >= startOfRampUpRight && RightCurrentPosition <= endOfRampUpRight) ||
                            (LeftCurrentPosition >= startOfRampUpLeft && LeftCurrentPosition <= endOfRampUpLeft)) {

                        drivePower = (targetDrivePower - robot.DRIVETRAIN_MINIMUM_POWER) *
                                (Math.abs(RightCurrentPosition - startOfRampUpRight) / RampUpDistance) + robot.DRIVETRAIN_MINIMUM_POWER;

                    }

                    // Driving Normal
                    else if ((RightCurrentPosition >= endOfRampUpRight && RightCurrentPosition <= startOfRampDownRight) ||
                            (LeftCurrentPosition >= endOfRampUpLeft && LeftCurrentPosition <= startOfRampDownLeft)) {

                        drivePower = targetDrivePower;

                    }

                    // Ramping down going forward
                    else if ((RightCurrentPosition >= startOfRampDownRight && RightCurrentPosition <= endOfRampDownRight) ||
                            (LeftCurrentPosition >= startOfRampDownLeft && LeftCurrentPosition <= endOfRampDownLeft)) {
                        drivePower = (targetDrivePower - robot.DRIVETRAIN_MINIMUM_POWER) *
                                (Math.abs( RightCurrentPosition - endOfRampDownRight) / RampDownDistance) + robot.DRIVETRAIN_MINIMUM_POWER;

                    } else if (driveStage == 0){
                        driveStage = 1;
                        robot.frontRightDrive.setPower(0);
                        robot.frontLeftDrive.setPower(0);
                        robot.backRightDrive.setPower(0);
                        robot.backLeftDrive.setPower(0);
                    }
                }

                // Driving Backwards .................................................................................................................................Backwards
                if (targetDrivePower < 0 && driveStage == 0) {

                    // ramping up
                    if ((RightCurrentPosition <= startOfRampUpRight && RightCurrentPosition >= endOfRampUpRight) ||
                            (LeftCurrentPosition <= startOfRampUpLeft && LeftCurrentPosition >= endOfRampUpLeft)) {

                        drivePower = (targetDrivePower + robot.DRIVETRAIN_MINIMUM_POWER) *
                                (Math.abs(startOfRampUpRight - RightCurrentPosition) / RampUpDistance) - robot.DRIVETRAIN_MINIMUM_POWER;

                    }

                    // Driving Normal
                    else if ((RightCurrentPosition <= endOfRampUpRight && RightCurrentPosition >= startOfRampDownRight) ||
                            (LeftCurrentPosition <= endOfRampUpLeft && LeftCurrentPosition >= startOfRampDownLeft)) {

                        drivePower = targetDrivePower;

                    }

                    // Ramping down going backward
                    else if ((RightCurrentPosition <= startOfRampDownRight && RightCurrentPosition >= endOfRampDownRight) ||
                            (LeftCurrentPosition <= startOfRampDownLeft && LeftCurrentPosition >= endOfRampDownLeft)) {

                        drivePower = (targetDrivePower + robot.DRIVETRAIN_MINIMUM_POWER) *
                                (Math.abs( RightCurrentPosition - endOfRampDownRight) / RampDownDistance) - robot.DRIVETRAIN_MINIMUM_POWER;
                    } else if (driveStage == 0){
                        driveStage = 1;
                        robot.frontRightDrive.setPower(0);
                        robot.frontLeftDrive.setPower(0);
                        robot.backRightDrive.setPower(0);
                        robot.backLeftDrive.setPower(0);

                    }

                    // end of ramp down
                }

                // Forwards distance adjust...............................................................................................................................STAGE 1
                if (driveStage == 1 && targetDrivePower > 0) {

                    if (LeftCurrentPosition < (endOfRampDownLeft - maximumTolerance) &&
                            RightCurrentPosition < (endOfRampDownRight - maximumTolerance)) {

                        drivePower = robot.DRIVETRAIN_MINIMUM_POWER;

                    } else if (LeftCurrentPosition > (endOfRampDownLeft + maximumTolerance) &&
                            RightCurrentPosition > (endOfRampDownRight + maximumTolerance)) {

                        drivePower = -robot.DRIVETRAIN_MINIMUM_POWER;

                    } else {
                        driveStage = 2;
                        robot.frontRightDrive.setPower(0);
                        robot.frontLeftDrive.setPower(0);
                        robot.backRightDrive.setPower(0);
                        robot.backLeftDrive.setPower(0);
                    }
                }

                // backwards distance adjust
                if (driveStage == 1 && targetDrivePower < 0) {

                    if (LeftCurrentPosition > (endOfRampDownLeft + maximumTolerance) &&
                            RightCurrentPosition > (endOfRampDownRight + maximumTolerance)) {

                        drivePower = -robot.DRIVETRAIN_MINIMUM_POWER;

                    } else if (LeftCurrentPosition < (endOfRampDownLeft - maximumTolerance) &&
                            RightCurrentPosition < (endOfRampDownRight - maximumTolerance)) {

                        drivePower = robot.DRIVETRAIN_MINIMUM_POWER;

                    } else {
                        driveStage = 2;
                        robot.frontRightDrive.setPower(0);
                        robot.frontLeftDrive.setPower(0);
                        robot.backRightDrive.setPower(0);
                        robot.backLeftDrive.setPower(0);
                    }
                }

                if (driveStage == 0 || driveStage == 1) {
                    robot.frontRightDrive.setPower(drivePower);
                    robot.frontLeftDrive.setPower(drivePower * robot.VEER_COMPENSATION_DBL);
                    robot.backRightDrive.setPower(drivePower);
                    robot.backLeftDrive.setPower(drivePower * robot.VEER_COMPENSATION_DBL);
                }
                // Heading adjustment
                if (driveStage == 2 || driveStage == 4) {

                    currentAngle = (float) robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

                    if (currentAngle - direction > robot.ROTATION_TOLERANCE) {

                        robot.frontRightDrive.setPower(-robot.ROTATION_MINIMUM_POWER  );
                        robot.frontLeftDrive.setPower(robot.ROTATION_MINIMUM_POWER );
                        robot.backRightDrive.setPower(-robot.ROTATION_MINIMUM_POWER  );
                        robot.backLeftDrive.setPower(robot.ROTATION_MINIMUM_POWER  );

                    }
                    else if (currentAngle - direction < -robot.ROTATION_TOLERANCE) {

                        robot.frontRightDrive.setPower(robot.ROTATION_MINIMUM_POWER);
                        robot.frontLeftDrive.setPower(-robot.ROTATION_MINIMUM_POWER);
                        robot.backRightDrive.setPower(robot.ROTATION_MINIMUM_POWER);
                        robot.backLeftDrive.setPower(-robot.ROTATION_MINIMUM_POWER);

                    } else {
                        robot.frontRightDrive.setPower(0);
                        robot.frontLeftDrive.setPower(0);
                        robot.backRightDrive.setPower(0);
                        robot.backLeftDrive.setPower(0);

                        driveStage ++;
                    }
                }

                // ...........................................................................................................................................Strafe Adjustment
                if ( driveStage == 3 ){

                    currentHorizontalEncoder = robot.OdometerEncoderHorizontal.getCurrentPosition();
                    if (currentHorizontalEncoder > 200){

                        robot.frontRightDrive.setPower(-robot.STRAFE_MINIMUM_POWER );
                        robot.frontLeftDrive.setPower(robot.STRAFE_MINIMUM_POWER );
                        robot.backRightDrive.setPower(robot.STRAFE_MINIMUM_POWER );
                        robot.backLeftDrive.setPower(-robot.STRAFE_MINIMUM_POWER );

                    }
                    else if (currentHorizontalEncoder < -200){

                        robot.frontRightDrive.setPower(robot.STRAFE_MINIMUM_POWER );
                        robot.frontLeftDrive.setPower(-robot.STRAFE_MINIMUM_POWER );
                        robot.backRightDrive.setPower(-robot.STRAFE_MINIMUM_POWER );
                        robot.backLeftDrive.setPower(robot.STRAFE_MINIMUM_POWER );

                    } else {

                        robot.frontRightDrive.setPower(0);
                        robot.frontLeftDrive.setPower(0);
                        robot.backRightDrive.setPower(0);
                        robot.backLeftDrive.setPower(0);

                        driveStage = 4;

                    }

                if (driveStage == 5) {
                    setHasFinished(true);
                }
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
        engine.telemetry.addData("BackLeftDrive", robot.OdometerEncoderRight.getCurrentPosition());

        engine.telemetry.addData("drivePower", drivePower);
        engine.telemetry.addData("targetDrivePower", targetDrivePower);

        engine.telemetry.addData("traveledDistance", traveledDistance);
        engine.telemetry.addData("RampUpDistance", RampUpDistance);
        engine.telemetry.addData("RampDownDistance", RampDownDistance);

    } // end of telemetry
} // end of class
