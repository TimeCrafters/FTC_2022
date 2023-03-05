package org.timecrafters.Phoenix.Autonomous.States;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.Phoenix.PhoenixBot1;

public class CollectorDistanceState extends CyberarmState {

    private final PhoenixBot1 robot;
    private int traveledDistance;
    private int RampUpDistance;
    private int RampDownDistance;
    private double targetDrivePower;
    private double lastMeasuredTime;
    private double currentDistance;
    private double LastDistanceRead;
    private double distanceDelta;
    private double debugRunTime;
    private String debugStatus = "?";
    private boolean inRange = false;
    private float collectTime;
    private double inRangeTime;
    private boolean stateDisabled;
    private double distanceLimit;
    private long maximumLookTime;
    private long startOfSequencerTime;
    public final double WHEEL_CIRCUMFERENCE = 7.42108499;
    public final double COUNTS_PER_REVOLUTION = 8192;


    public CollectorDistanceState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.targetDrivePower = robot.configuration.variable(groupName, actionName, "targetDrivePower").value();
        this.traveledDistance = robot.configuration.variable(groupName, actionName, "traveledDistance").value();
        this.RampUpDistance = robot.configuration.variable(groupName, actionName, "RampUpDistance").value();
        this.RampDownDistance = robot.configuration.variable(groupName, actionName, "RampDownDistance").value();
        this.collectTime = robot.configuration.variable(groupName, actionName, "collectTime").value();
        this.distanceLimit = robot.configuration.variable(groupName, actionName, "distanceLimit").value();
        this.maximumLookTime = robot.configuration.variable(groupName, actionName, "maximumLookTime").value();
        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;


    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("Time left", System.currentTimeMillis() - startOfSequencerTime);

        engine.telemetry.addData("frontRightDrive", robot.frontRightDrive.getCurrentPosition());
        engine.telemetry.addData("frontLeftDrive", robot.frontLeftDrive.getCurrentPosition());
        engine.telemetry.addData("BackRightDrive", robot.backRightDrive.getCurrentPosition());
        engine.telemetry.addData("BackLeftDrive", robot.backLeftDrive.getCurrentPosition());

        engine.telemetry.addData("traveledDistance", traveledDistance);
        engine.telemetry.addData("RampUpDistance", RampUpDistance);
        engine.telemetry.addData("RampDownDistance", RampDownDistance);

        engine.telemetry.addData("targetDrivePower", targetDrivePower);

        engine.telemetry.addLine();
        engine.telemetry.addData("Current Sensor", robot.collectorDistance.getDistance(DistanceUnit.MM));
        engine.telemetry.addData("last Distance", LastDistanceRead);
        engine.telemetry.addLine();

        engine.telemetry.addData("distanceDelta", distanceDelta);
        engine.telemetry.addData("DEBUG RunTime", debugRunTime);
        engine.telemetry.addData("DEBUG Status", debugStatus);

    }

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


        robot.collectorLeft.setPower(1);
        robot.collectorRight.setPower(1);

        lastMeasuredTime = System.currentTimeMillis();
        startOfSequencerTime = System.currentTimeMillis();
        LastDistanceRead = robot.collectorDistance.getDistance(DistanceUnit.MM);

        traveledDistance = (int) ((traveledDistance * (COUNTS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE)) * robot.DISTANCE_MULTIPLIER);


    }



    @Override
    public void exec() {
        if (stateDisabled || System.currentTimeMillis() - startOfSequencerTime > maximumLookTime) {
            robot.collectorLeft.setPower(0);
            robot.collectorRight.setPower(0);
            robot.frontRightDrive.setPower(0);
            robot.frontLeftDrive.setPower(0);
            robot.backRightDrive.setPower(0);
            robot.backLeftDrive.setPower(0);
            setHasFinished(true);
        }

        if (System.currentTimeMillis() - lastMeasuredTime > 150) {
            // checking to see if time is greater than 150 milliseconds
            lastMeasuredTime = System.currentTimeMillis();

            currentDistance = robot.collectorDistance.getDistance(DistanceUnit.MM);

            distanceDelta = LastDistanceRead - currentDistance;

            if (distanceDelta >= -15.0 || currentDistance > 500) {
                // I am moving forward
                // and im close too my target.
                LastDistanceRead = currentDistance;
                debugRunTime = runTime();
                debugStatus = "Driving Towards Cone";
            } else {
                // I have stopped
                debugStatus = "Nothing Collected";
                robot.collectorLeft.setPower(0);
                robot.collectorRight.setPower(0);
                robot.frontRightDrive.setPower(0);
                robot.frontLeftDrive.setPower(0);
                robot.backRightDrive.setPower(0);
                robot.backLeftDrive.setPower(0);

                setHasFinished(true);
            }
        }

        if (robot.collectorDistance.getDistance(DistanceUnit.MM) > distanceLimit) {

            double delta = traveledDistance - Math.abs(robot.OdometerEncoderRight.getCurrentPosition());

            robot.backLeftDrive.setPower(targetDrivePower);
            robot.backRightDrive.setPower(targetDrivePower);
            robot.frontLeftDrive.setPower(targetDrivePower);
            robot.frontRightDrive.setPower(targetDrivePower);

        } else {

            robot.frontRightDrive.setPower(0);
            robot.frontLeftDrive.setPower(0);
            robot.backRightDrive.setPower(0);
            robot.backLeftDrive.setPower(0);

            if (!inRange){
                inRange = true;
                inRangeTime = runTime();
            } else {

                if (runTime() - inRangeTime >= collectTime){
                    robot.collectorRight.setPower(0);
                    robot.collectorLeft.setPower(0);
                    robot.frontRightDrive.setPower(0);
                    robot.frontLeftDrive.setPower(0);
                    robot.backRightDrive.setPower(0);
                    robot.backLeftDrive.setPower(0);
                    setHasFinished(true);
                }
            }

                }

            }

        }

