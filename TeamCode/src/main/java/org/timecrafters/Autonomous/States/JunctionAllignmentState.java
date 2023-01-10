package org.timecrafters.Autonomous.States;

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



    public JunctionAllignmentState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.drivePower = robot.configuration.variable(groupName, actionName, "DrivePower").value();
        this.loopsTotal = robot.configuration.variable(groupName, actionName, "loopsTotal").value();
        this.rotationAmount = robot.configuration.variable(groupName, actionName, "rotationAmount").value();
        this.slop = robot.configuration.variable(groupName, actionName, "slop").value();

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





    }

    @Override
    public void start() {

    }

    @Override
    public void exec() {

        currentAngle = robot.imu.getAngularOrientation().firstAngle;


        if (stateDisabled){
            setHasFinished(true);
        } else {

            double leftDistance = robot.leftPoleDistance.getDistance(DistanceUnit.MM);
            double rightDistance = robot.rightPoleDistance.getDistance(DistanceUnit.MM);
            boolean rightInRange = rightDistance > 170 && rightDistance < 200;
            boolean leftInRange = leftDistance > 170 && leftDistance < 200;


            // The minimum Value that can be seen when in distance of the pole is 90.0 the maximum is 200.0

            if (loopsTotal >= 5){
                setHasFinished(true);
            } else if (runTime() - checkTime >= 250 ) {

                checkTime = runTime();
                loopsTotal = loopsTotal + 1;


            } else {

                if (rightInRange && leftInRange){
                    setHasFinished(true);
                }

                if (rightInRange && !leftInRange) {

                    // rotate 1 degree CW

                    targetAngle = currentAngle + rotationAmount;

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

                if (!rightInRange && !leftInRange){

                    setHasFinished(true);

                    }
                }
            }
        }
    }
