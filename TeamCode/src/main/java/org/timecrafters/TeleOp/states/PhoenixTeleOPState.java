package org.timecrafters.TeleOp.states;

import android.annotation.SuppressLint;
import android.widget.Switch;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class PhoenixTeleOPState extends CyberarmState {

    private final PhoenixBot1 robot;
    private double drivePower = 1;
    private long lastStepTime = 0;
    private double RobotRotation;
    private double RotationTarget, DeltaRotation;
    private double MinimalPower = 0.25, topServoOffset = -0.05, lowServoOffset = -0.05;
    private double servoCollectLow = 0.40; //Low servos, A button
    private double servoCollectHigh = 0.40; //High servos, A button
    private double servoLowLow = 0.5; //Low servos, X button
    private double servoLowHigh = 0.75; //High servos, X button
    private double servoMedLow = 0.5; //Low servos, B button
    private double servoMedHigh = 0.9; //High servos, B button
    private double servoHighLow = 0.8; //Low servos, Y button
    private double servoHighHigh = 0.9; //High servos, Y button
    private GamepadChecker gamepad1Checker, gamepad2Checker;
    private int OCD;

    public PhoenixTeleOPState(PhoenixBot1 robot) {
        this.robot = robot;
    }

    public void CalculateDeltaRotation() {
        if (RotationTarget >= 0 && RobotRotation >= 0) {
            DeltaRotation = Math.abs(RotationTarget - RobotRotation);
        } else if (RotationTarget <= 0 && RobotRotation <= 0) {
            DeltaRotation = Math.abs(RotationTarget - RobotRotation);
        } else if (RotationTarget >= 0 && RobotRotation <= 0) {
            DeltaRotation = (RotationTarget + Math.abs(RobotRotation));
        } else if (RotationTarget <= 0 && RobotRotation >= 0) {
            DeltaRotation = (RobotRotation + Math.abs(RotationTarget));
        }
    }


    @Override
    public void telemetry() {
        engine.telemetry.addData("High Riser Right Position", robot.HighRiserRight.getPosition());
        engine.telemetry.addData("High Riser Left Position", robot.HighRiserLeft.getPosition());
        engine.telemetry.addData("Low Riser Right Position", robot.LowRiserRight.getPosition());
        engine.telemetry.addData("Low Riser Left Position", robot.LowRiserLeft.getPosition());
        engine.telemetry.addData("IMU", robot.imu.getAngularOrientation().firstAngle);
        engine.telemetry.addData("Drive Power", drivePower);
        engine.telemetry.addData("Delta Rotation", DeltaRotation);
        engine.telemetry.addData("Cone Distance", robot.collectorDistance.getDistance(DistanceUnit.MM));
//        engine.telemetry.addData("Collector Height", robot.downSensor.getDistance(DistanceUnit.MM));
        engine.telemetry.addData("Left Pole Distance", robot.leftPoleDistance.getDistance(DistanceUnit.MM));
        engine.telemetry.addData("Right Pole Distance", robot.rightPoleDistance.getDistance(DistanceUnit.MM));
        engine.telemetry.addData("Odometer Encoder, Right", robot.OdometerEncoder.getCurrentPosition());
        engine.telemetry.addData("Odometer Encoder, Left", robot.OdometerEncoderLeft.getCurrentPosition());
    }

    @Override
    public void init() {
        robot.HighRiserLeft.setDirection(Servo.Direction.REVERSE);
        robot.HighRiserRight.setDirection(Servo.Direction.FORWARD);
        robot.LowRiserLeft.setDirection(Servo.Direction.FORWARD);
        robot.LowRiserRight.setDirection(Servo.Direction.REVERSE);
        robot.LowRiserLeft.setPosition(0.45);
        robot.LowRiserRight.setPosition(0.45 + lowServoOffset);
        robot.HighRiserLeft.setPosition(0.45);
        robot.HighRiserRight.setPosition(0.45 + topServoOffset);
        robot.OdometerEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.OdometerEncoderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        gamepad1Checker = new GamepadChecker(engine, engine.gamepad1);
        gamepad2Checker = new GamepadChecker(engine, engine.gamepad2);
        OCD = 0;
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void exec() {

        if (engine.gamepad1.start && !engine.gamepad1.a) {
            robot.OdometerEncoderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.OdometerEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        if (engine.gamepad1.right_trigger > 0) {
            drivePower = engine.gamepad1.right_trigger;
            robot.backLeftDrive.setPower(drivePower);
            robot.backRightDrive.setPower(drivePower - 0.05);
            robot.frontLeftDrive.setPower(drivePower);
            robot.frontRightDrive.setPower(drivePower - 0.05);
        } else if (engine.gamepad1.left_trigger > 0) {
            drivePower = engine.gamepad1.left_trigger;
            robot.backLeftDrive.setPower(-drivePower);
            robot.backRightDrive.setPower(-drivePower + 0.05);
            robot.frontLeftDrive.setPower(-drivePower);
            robot.frontRightDrive.setPower(-drivePower + 0.05);
        }

        if (Math.abs(engine.gamepad1.left_stick_y) > 0.05) {
            drivePower = engine.gamepad1.left_stick_y * 0.75;
            robot.backRightDrive.setPower(drivePower);
            robot.frontRightDrive.setPower(drivePower);
        }

        if (Math.abs(engine.gamepad1.right_stick_y) > 0.05) {
            drivePower = engine.gamepad1.right_stick_y * 0.75;
            robot.backLeftDrive.setPower(drivePower);
            robot.frontLeftDrive.setPower(drivePower);
        }

        if (engine.gamepad1.right_trigger < 0.1 &&
                engine.gamepad1.left_trigger < 0.1 &&
                !engine.gamepad1.y &&
                !engine.gamepad1.x &&
                !engine.gamepad1.a &&
                !engine.gamepad1.b &&
                !engine.gamepad1.dpad_left &&
                !engine.gamepad1.dpad_right &&
                Math.abs(engine.gamepad1.left_stick_y) < 0.1 &&
                Math.abs(engine.gamepad1.right_stick_y) < 0.1) {
            drivePower = 0;
            robot.backLeftDrive.setPower(drivePower);
            robot.backRightDrive.setPower(drivePower);
            robot.frontLeftDrive.setPower(drivePower);
            robot.frontRightDrive.setPower(drivePower);
        }

        if (engine.gamepad1.dpad_right) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = 90;
            CalculateDeltaRotation();
            if (RobotRotation > -90 && RobotRotation < 89) {//CCW
                drivePower = (-0.95 * DeltaRotation / 180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > 91 || RobotRotation < -90) {//CW
                drivePower = (0.95 * DeltaRotation / 180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < 91 && RobotRotation > 89) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(drivePower);
            }
        }

        if (engine.gamepad1.dpad_left) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = -90;
            CalculateDeltaRotation();
            if (RobotRotation > -89 && RobotRotation <= 90) {//CW
                drivePower = (0.95 * DeltaRotation / 180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > 90 || RobotRotation < -91) {//CCW
                drivePower = (-0.95 * DeltaRotation / 180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < 91 && RobotRotation > 89) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(drivePower);
            }
        }

        if (engine.gamepad1.y) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = 180;
            CalculateDeltaRotation();
            if (RobotRotation < 0 && RobotRotation > -179) {
                drivePower = (0.95 * DeltaRotation / 180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            } else if (RobotRotation >= 0) {
                drivePower = (-0.95 * DeltaRotation / 180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            } else if (RobotRotation <= -179 || RobotRotation >= 179) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }

        if (engine.gamepad1.a && !engine.gamepad1.start) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = 0;
            CalculateDeltaRotation();
            if (RobotRotation < -1) {
                drivePower = (-0.95 * DeltaRotation / 180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > 1) {
                drivePower = (0.95 * DeltaRotation / 180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > -1 && RobotRotation < 1) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }

        if (engine.gamepad1.x) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = -45;
            CalculateDeltaRotation();
            if (RobotRotation < -46 || RobotRotation > 135) {//CCW
                drivePower = (-0.95 * DeltaRotation / 180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > -44 && RobotRotation < 135) {//CW
                drivePower = (0.95 * DeltaRotation / 180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > -46 && RobotRotation < -44) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(drivePower);
            }
        }

        if (engine.gamepad1.b) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = 45;
            CalculateDeltaRotation();
            if (RobotRotation > -135 && RobotRotation < 44) {//CCW
                drivePower = (-0.95 * DeltaRotation / 180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < -135 || RobotRotation > 46) {//CW
                drivePower = (0.95 * DeltaRotation / 180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < 46 && RobotRotation > 44) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(drivePower);
            }
        }

        if (engine.gamepad2.dpad_left) {
            robot.collectorLeft.setPower(-1);
            robot.collectorRight.setPower(-1);
        } else if (engine.gamepad2.dpad_right) {
            robot.collectorLeft.setPower(1);
            robot.collectorRight.setPower(1);
        } else {
            robot.collectorLeft.setPower(0);
            robot.collectorRight.setPower(0);
        }

        if (engine.gamepad2.right_bumper) {
            OCD = 0;
            if (robot.HighRiserLeft.getPosition() < 0.9 - Math.abs(topServoOffset)) {
                if (System.currentTimeMillis() - lastStepTime >= 120) {
                    lastStepTime = System.currentTimeMillis();
                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
                }
            }
        }

        if (engine.gamepad2.left_bumper) {
            OCD = 0;
            if (robot.HighRiserLeft.getPosition() > 0.4) {
                if (System.currentTimeMillis() - lastStepTime >= 120) {
                    lastStepTime = System.currentTimeMillis();
                    OCD = 0;
                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - 0.05);
                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - 0.05);
                }
            }
        }

        if (engine.gamepad2.a) {
            OCD = 1;
        }

        if (engine.gamepad2.x) {
            OCD = 2;
        }

        if (engine.gamepad2.b && !engine.gamepad2.start) {
            OCD = 3;
        }

        if (engine.gamepad2.y) {
            OCD = 4;
        }

        if (OCD == 1) { //Ground junction/Collect
            if (robot.LowRiserLeft.getPosition() >= servoCollectLow + 0.01)/* <-- low level too high*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
                }
            } else if (robot.LowRiserLeft.getPosition() <= servoCollectLow &&
                        robot.HighRiserLeft.getPosition() > servoCollectHigh)/* <-- high level too high*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - 0.05);
                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - 0.05);
                }
            } else if (robot.LowRiserLeft.getPosition() <= servoCollectLow + 0.01 &&
                        robot.HighRiserLeft.getPosition() <= servoCollectHigh) {
                OCD = 0;
            }
        }

        if (OCD == 2) { //low junction
            if (robot.LowRiserLeft.getPosition() > servoLowLow + 0.01)/* <-- low level too high*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() < servoLowLow - 0.01)/* <-- low level too low*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() <= servoLowLow + 0.01 &&
                robot.LowRiserLeft.getPosition() > servoLowLow - 0.01 &&
                robot.HighRiserLeft.getPosition() > servoLowHigh)/* <-- high level too high*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - 0.05);
                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() <= servoLowLow + 0.01 &&
                robot.HighRiserLeft.getPosition() < servoLowHigh - 0.01)/* <-- high level too low*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() > servoLowLow - 0.01 &&
                robot.LowRiserLeft.getPosition() <= servoLowLow + 0.01 &&
                robot.HighRiserLeft.getPosition() > servoLowHigh - 0.01 &&
                robot.HighRiserLeft.getPosition() <=  servoLowHigh + 0.01) {
                OCD = 0;
            }
        }

        if (OCD == 3) { // Medium junction
            if (robot.LowRiserLeft.getPosition() > servoMedLow + 0.01)/* <-- low level too high*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() < servoMedLow - 0.01)/* <-- low level too low*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() <= servoMedLow + 0.01 &&
                robot.HighRiserLeft.getPosition() > servoMedHigh + 0.01)/* <-- high level too high*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - 0.05);
                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() < servoMedLow + 0.01 &&
                robot.HighRiserLeft.getPosition() < servoMedHigh - 0.01)/* <-- high level too low*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() > servoMedLow - 0.01 &&
                robot.LowRiserLeft.getPosition() <= servoMedLow &&
                robot.HighRiserLeft.getPosition() > servoMedHigh - 0.01 &&
                robot.HighRiserLeft.getPosition() <= servoMedHigh) {
                OCD = 0;
            }
        }

        if (OCD == 4) { // High Junction
            if (robot.HighRiserLeft.getPosition() < servoHighHigh - 0.01)/* <-- high level too low*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() < servoHighLow - 0.01)/* <-- low level too low*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
                }
            }
            if (robot.HighRiserLeft.getPosition() >= servoHighHigh &&
                robot.LowRiserLeft.getPosition() >= servoHighLow) {
                OCD = 0;
            }
        }

        gamepad1Checker.update();
        gamepad2Checker.update();
    }


    @Override
    public void buttonUp(Gamepad gamepad, String button) {
        if (engine.gamepad1 == gamepad && button.equals("start")) {
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

            parameters.mode = BNO055IMU.SensorMode.IMU;
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.loggingEnabled = false;

            robot.imu.initialize(parameters);
        }
    }
//    public double downSensor() {
//        double Distance, Distance_1, Distance_2, Distance_3, Distance_4, Distance_5;
//        Distance_1 = robot.downSensor.getDistance(DistanceUnit.MM);
//        Distance_2 = robot.downSensor.getDistance(DistanceUnit.MM);
//        Distance_3 = robot.downSensor.getDistance(DistanceUnit.MM);
//        Distance_4 = robot.downSensor.getDistance(DistanceUnit.MM);
//        Distance_5 = robot.downSensor.getDistance(DistanceUnit.MM);
//        Distance = (Distance_1 + Distance_2 + Distance_3 + Distance_4 + Distance_5)/5;
//        return Distance;

    }

