package org.timecrafters.Autonomous.States;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.TeleOp.states.PhoenixBot1;

public class CollectorDistanceState extends CyberarmState {

    private final PhoenixBot1 robot;
    private int traveledDistance;
    private int RampUpDistance;
    private int RampDownDistance;
    private double drivePower;
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


    public CollectorDistanceState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.targetDrivePower = robot.configuration.variable(groupName, actionName, "targetDrivePower").value();
        this.traveledDistance = robot.configuration.variable(groupName, actionName, "traveledDistance").value();
        this.RampUpDistance = robot.configuration.variable(groupName, actionName, "RampUpDistance").value();
        this.RampDownDistance = robot.configuration.variable(groupName, actionName, "RampDownDistance").value();
        this.collectTime = robot.configuration.variable(groupName, actionName, "collectTime").value();
        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;


    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("frontRightDrive", robot.frontRightDrive.getCurrentPosition());
        engine.telemetry.addData("frontLeftDrive", robot.frontLeftDrive.getCurrentPosition());
        engine.telemetry.addData("BackRightDrive", robot.backRightDrive.getCurrentPosition());
        engine.telemetry.addData("BackLeftDrive", robot.backLeftDrive.getCurrentPosition());
        engine.telemetry.addData("BackLeftDrive", robot.OdometerEncoderRight.getCurrentPosition());
        engine.telemetry.addLine();

        engine.telemetry.addData("traveledDistance", traveledDistance);
        engine.telemetry.addData("RampUpDistance", RampUpDistance);
        engine.telemetry.addData("RampDownDistance", RampDownDistance);

        engine.telemetry.addLine();

        engine.telemetry.addData("drivePower", drivePower);
        engine.telemetry.addData("targetDrivePower", targetDrivePower);

        engine.telemetry.addLine();
        engine.telemetry.addData("Distance Sensor", robot.collectorDistance.getDistance(DistanceUnit.MM));
        engine.telemetry.addData("Current Distance", currentDistance);
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
        robot.OdometerEncoderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.OdometerEncoderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.OdometerEncoderHorizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.OdometerEncoderRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.OdometerEncoderLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.OdometerEncoderHorizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        robot.collectorLeft.setPower(1);
        robot.collectorRight.setPower(1);

        lastMeasuredTime = System.currentTimeMillis();
        LastDistanceRead = robot.collectorDistance.getDistance(DistanceUnit.MM);



    }



    @Override
    public void exec() {
        if (stateDisabled){
            robot.frontRightDrive.setPower(0);
            robot.frontLeftDrive.setPower(0);
            robot.backRightDrive.setPower(0);
            robot.backLeftDrive.setPower(0);
            setHasFinished(true);
            return;
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

                return;
            }
        }

        if (robot.collectorDistance.getDistance(DistanceUnit.MM) > 70) {
            double delta = traveledDistance - Math.abs(robot.OdometerEncoderRight.getCurrentPosition());

            if (Math.abs(robot.OdometerEncoderRight.getCurrentPosition()) <= RampUpDistance) {
                // ramping up
                drivePower = (Math.abs((float) robot.OdometerEncoderRight.getCurrentPosition()) / RampUpDistance) + 0.15;
            } else if (Math.abs(robot.OdometerEncoderRight.getCurrentPosition()) >= delta) {
                // ramping down
                drivePower = ((delta / RampDownDistance) + 0.15);
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

            if (Math.abs(robot.OdometerEncoderRight.getCurrentPosition()) < traveledDistance) {
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(drivePower);
            }
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

