package org.timecrafters.Autonomous.States;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.TeleOp.states.PhoenixBot1;

public class JunctionAllignmentState extends CyberarmState {
    private final boolean stateDisabled;
    PhoenixBot1 robot;
    private final double drivePower;
    private int loopsTotal;
    private float rotationAmount;
    private float currentAngle;
    private float targetAngle;
    private float slop;
    private double checkTime = 250;
    private int traveledDistance;
    private int loopsCurrent = 0;
    private double minDistance;
    private double maxDistance;
    private String whereAmI = "init";
    private int driveLoops;
    private boolean finishedEnabled;


    public JunctionAllignmentState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.drivePower = robot.configuration.variable(groupName, actionName, "DrivePower").value();
        this.loopsTotal = robot.configuration.variable(groupName, actionName, "loopsTotal").value();
        this.rotationAmount = robot.configuration.variable(groupName, actionName, "rotationAmount").value();
        this.slop = robot.configuration.variable(groupName, actionName, "slop").value();
        this.traveledDistance = robot.configuration.variable(groupName, actionName, "traveled distance").value();
        this.minDistance = robot.configuration.variable(groupName, actionName, "minDistance").value();
        this.maxDistance = robot.configuration.variable(groupName, actionName, "maxDistance").value();
        this.finishedEnabled = robot.configuration.variable(groupName, actionName, "finishedEnabled").value();


        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("right sensor distance", robot.rightPoleDistance.getDistance(DistanceUnit.MM));
        engine.telemetry.addData("left sensor distance", robot.leftPoleDistance.getDistance(DistanceUnit.MM));
        engine.telemetry.addData("front right power", robot.frontRightDrive.getPower());
        engine.telemetry.addData("front left power", robot.frontLeftDrive.getPower());
        engine.telemetry.addData("back left power", robot.backLeftDrive.getPower());
        engine.telemetry.addData("back right power", robot.backRightDrive.getPower());
        engine.telemetry.addData("target angle", targetAngle);
        engine.telemetry.addData("current angle", currentAngle);
        engine.telemetry.addData("drive power", drivePower);
        engine.telemetry.addData("traveled distance", traveledDistance);
        engine.telemetry.addData("Loops", loopsCurrent);
        engine.telemetry.addData("where am i??", whereAmI);
        engine.telemetry.addData("time", runTime() - checkTime);






    }

    @Override
    public void start() {

        robot.frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.OdometerEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.OdometerEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        checkTime = System.currentTimeMillis() - 250;

        driveLoops = 0;

    }

    @Override
    public void exec() {

        currentAngle = robot.imu.getAngularOrientation().firstAngle;


        if (stateDisabled){
            setHasFinished(true);
        } else {

            double leftDistance = robot.leftPoleDistance.getDistance(DistanceUnit.MM);
            double rightDistance = robot.rightPoleDistance.getDistance(DistanceUnit.MM);
            boolean rightInRange = rightDistance > minDistance && rightDistance < maxDistance;
            boolean leftInRange = leftDistance > minDistance && leftDistance < maxDistance;


            // The minimum Value that can be seen when in distance of the pole is 90.0 the maximum is 200.0

            if (loopsCurrent >= loopsTotal){
                setHasFinished(finishedEnabled);
            } else if (System.currentTimeMillis() - checkTime >= 250 ) {

                whereAmI = "post 250 ms";

                checkTime = runTime();

                loopsCurrent = loopsCurrent + 1;

                if (rightInRange && leftInRange){
                    setHasFinished(finishedEnabled);
                }

                if (rightInRange && !leftInRange) {

                    // rotate 1 degree CW

                    targetAngle = currentAngle + rotationAmount;

                    if (targetAngle + slop < currentAngle && targetAngle - slop > currentAngle) {

                        robot.frontRightDrive.setPower(0);
                        robot.backRightDrive.setPower(0);
                        robot.backLeftDrive.setPower(0);
                        robot.frontLeftDrive.setPower(0);

                        setHasFinished(finishedEnabled);

                    } else {

                        if (targetAngle - slop < currentAngle) {

                            robot.frontRightDrive.setPower(-drivePower);
                            robot.backRightDrive.setPower(-drivePower);
                            robot.backLeftDrive.setPower(drivePower);
                            robot.frontLeftDrive.setPower(drivePower);

                        } else if (targetAngle + slop > currentAngle) {

                            robot.frontRightDrive.setPower(drivePower);
                            robot.backRightDrive.setPower(drivePower);
                            robot.backLeftDrive.setPower(-drivePower);
                            robot.frontLeftDrive.setPower(-drivePower);

                        }

                    }

                }
                if (!rightInRange && leftInRange) {

                    // rotate 1 degree CCW

                    targetAngle = currentAngle - rotationAmount;

                    if (targetAngle + slop < currentAngle && targetAngle - slop > currentAngle) {

                        robot.frontRightDrive.setPower(0);
                        robot.backRightDrive.setPower(0);
                        robot.backLeftDrive.setPower(0);
                        robot.frontLeftDrive.setPower(0);

                    } else {

                        if (targetAngle - slop < currentAngle) {

                            robot.frontRightDrive.setPower(-drivePower);
                            robot.backRightDrive.setPower(-drivePower);
                            robot.backLeftDrive.setPower(drivePower);
                            robot.frontLeftDrive.setPower(drivePower);

                        } else if (targetAngle + slop > currentAngle) {

                            robot.frontRightDrive.setPower(drivePower);
                            robot.backRightDrive.setPower(drivePower);
                            robot.backLeftDrive.setPower(-drivePower);
                            robot.frontLeftDrive.setPower(-drivePower);

                        }

                    }

                }

                if (!rightInRange && !leftInRange && driveLoops < 2){

                    if (Math.abs(robot.OdometerEncoder.getCurrentPosition()) < traveledDistance) {
                        robot.backLeftDrive.setPower(drivePower);
                        robot.backRightDrive.setPower(drivePower);
                        robot.frontLeftDrive.setPower(drivePower);
                        robot.frontRightDrive.setPower(drivePower);

                        driveLoops += 1;
                    }

                    } else {

                        robot.backLeftDrive.setPower(0);
                        robot.backRightDrive.setPower(0);
                        robot.frontLeftDrive.setPower(0);
                        robot.frontRightDrive.setPower(0);

                        robot.OdometerEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        robot.OdometerEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                        if (driveLoops == 2) {
                            loopsCurrent = loopsTotal;
                        }


                    }
                }
            }
        }
    }
