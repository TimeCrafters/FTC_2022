package org.timecrafters.testing.states;

import android.annotation.SuppressLint;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.Vuforia;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;

public class PhoenixTeleOPState extends CyberarmState {

    private final PhoenixBot1 robot;
    private double drivePower = 1;
    private long lastStepTime = 0;
    private int CyclingArmUpAndDown = 0;
    private double RobotRotation;
    private double RotationTarget, DeltaRotation;
    private double MinimalPower = 0.2;
    private GamepadChecker gamepad1Checker, gamepad2Checker;

    public PhoenixTeleOPState(PhoenixBot1 robot) {
        this.robot = robot;
    }

    public void CalculateDeltaRotation() {
        if (RotationTarget >= 0 && RobotRotation >= 0) {
            DeltaRotation = Math.abs(RotationTarget - RobotRotation);
        }
        else if (RotationTarget <= 0 && RobotRotation <= 0) {
            DeltaRotation = Math.abs(RotationTarget - RobotRotation);
        }
        else if (RotationTarget >= 0 && RobotRotation <= 0) {
            DeltaRotation = Math.abs(RotationTarget + RobotRotation);
        }
        else if (RotationTarget <=0 && RobotRotation >= 0) {
            DeltaRotation = Math.abs(RobotRotation + RotationTarget);
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
    }

    @Override
    public void init() {
        robot.HighRiserLeft.setDirection(Servo.Direction.REVERSE);
        robot.HighRiserRight.setDirection(Servo.Direction.FORWARD);
        robot.LowRiserLeft.setDirection(Servo.Direction.FORWARD);
        robot.LowRiserRight.setDirection(Servo.Direction.REVERSE);
        robot.LowRiserLeft.setPosition(0.45);
        robot.LowRiserRight.setPosition(0.45);
        robot.HighRiserLeft.setPosition(0.45);
        robot.HighRiserRight.setPosition(0.45);


        gamepad1Checker = new GamepadChecker(engine, engine.gamepad1);
        gamepad2Checker = new GamepadChecker(engine, engine.gamepad2);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void exec() {

        if (engine.gamepad1.right_trigger > 0) {
            drivePower = engine.gamepad1.right_trigger;
            robot.backLeftDrive.setPower(drivePower);
            robot.backRightDrive.setPower(drivePower);
            robot.frontLeftDrive.setPower(drivePower);
            robot.frontRightDrive.setPower(drivePower);
        }

        if (engine.gamepad1.left_trigger > 0) {
            drivePower = engine.gamepad1.left_trigger;
            robot.backLeftDrive.setPower(-drivePower);
            robot.backRightDrive.setPower(-drivePower);
            robot.frontLeftDrive.setPower(-drivePower);
            robot.frontRightDrive.setPower(-drivePower);
        }

        if (Math.abs(engine.gamepad1.left_stick_y) > 0.1) {
            drivePower = engine.gamepad1.left_stick_y;
            robot.backRightDrive.setPower(drivePower);
            robot.frontRightDrive.setPower(drivePower);
        }

        if (Math.abs(engine.gamepad1.right_stick_y) > 0.1) {
            drivePower = engine.gamepad1.right_stick_y;
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
                Math.abs (engine.gamepad1.left_stick_y) < 0.1 &&
                Math.abs(engine.gamepad1.right_stick_y) < 0.1) {
            drivePower = 0;
            robot.backLeftDrive.setPower(-drivePower);
            robot.backRightDrive.setPower(-drivePower);
            robot.frontLeftDrive.setPower(-drivePower);
            robot.frontRightDrive.setPower(-drivePower);
        }

        if (engine.gamepad1.a) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = 180;
            CalculateDeltaRotation();
            if (RobotRotation < 0 && RobotRotation > -179) {
                drivePower = (1 * DeltaRotation/180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            else if (RobotRotation > 0) {
                drivePower = (-1 * DeltaRotation/180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            else if (RobotRotation <= -179 || RobotRotation >= 179) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }

        if (engine.gamepad1.y) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = 0;
            CalculateDeltaRotation();
            if (RobotRotation < -1) {
                drivePower = (-1 * DeltaRotation/180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > 1) {
                drivePower = (1 * DeltaRotation/180) + MinimalPower;
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
        if (engine.gamepad1.dpad_left) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = 135;
            CalculateDeltaRotation();
            if (RobotRotation > -45 && RobotRotation < 134) {//CCW
                drivePower = (-1 * DeltaRotation/180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < -45 || RobotRotation > 136) {//CW
                drivePower = (1 * DeltaRotation/180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < 136 && RobotRotation > 134) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }

        if (engine.gamepad1.x) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = 90;
            CalculateDeltaRotation();
            if (RobotRotation > -90 && RobotRotation < 89) {//CCW
                drivePower = (-1 * DeltaRotation/180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < -90 || RobotRotation > 91) {//CW
                drivePower = (1 * DeltaRotation/180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < 91 && RobotRotation > 89) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }

        if (engine.gamepad1.dpad_right) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = -135;
            CalculateDeltaRotation();
            if (RobotRotation < 45 && RobotRotation > -134) {//CCW
                drivePower = (1 * DeltaRotation/180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > 45 || RobotRotation < -136) {//CW
                drivePower = (-1 * DeltaRotation/180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < -134 && RobotRotation > -136) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }

        if (engine.gamepad1.b) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            RotationTarget = -90;
            CalculateDeltaRotation();
            if (RobotRotation < 90 && RobotRotation > -89) {//CCW
                drivePower = (1 * DeltaRotation/180) + MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > 90 || RobotRotation < -91) {//CW
                drivePower = (-1 * DeltaRotation/180) - MinimalPower;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < -89 && RobotRotation > -91) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
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

        if (engine.gamepad2.dpad_up) {
            if (robot.HighRiserLeft.getPosition() < 1.0) {
                if (System.currentTimeMillis() - lastStepTime >= 150) {
                    lastStepTime = System.currentTimeMillis();
                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
                }
            }
        }

        if (engine.gamepad2.dpad_down) {
            if (robot.HighRiserLeft.getPosition() > 0.45) {
                if (System.currentTimeMillis() - lastStepTime >= 150) {
                    lastStepTime = System.currentTimeMillis();
                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - 0.05);
                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - 0.05);
                }
            }
        }

        if (engine.gamepad2.y) {
            if (robot.HighRiserLeft.getPosition() < 0.9) {
                if (System.currentTimeMillis() - lastStepTime >= 150) {
                    lastStepTime = System.currentTimeMillis();
                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
                }
            }

            if (robot.LowRiserLeft.getPosition() < 0.75 && robot.HighRiserLeft.getPosition() > 0.7) {
                if (System.currentTimeMillis() - lastStepTime >= 150) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
                }
            }
        }//end of y

            if (engine.gamepad2.a) {
                if (robot.HighRiserLeft.getPosition() > 0.45 && robot.LowRiserLeft.getPosition() < 0.5) {
                    if (System.currentTimeMillis() - lastStepTime >= 150) {
                        lastStepTime = System.currentTimeMillis();
                        robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - 0.05);
                        robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - 0.05);
                    }
                }

                if (robot.LowRiserLeft.getPosition() > 0.45) {
                    if (System.currentTimeMillis() - lastStepTime >= 150) {
                        lastStepTime = System.currentTimeMillis();
                        robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                        robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
                    }
                }
            }//end of a

        if (engine.gamepad2.back) {
            robot.backLeftDrive.setPower(1);
            robot.backRightDrive.setPower(1);
            robot.frontLeftDrive.setPower(1);
            robot.frontRightDrive.setPower(1);
            if (System.currentTimeMillis() - lastStepTime >= 1500) {
                robot.backLeftDrive.setPower(0);
                robot.backRightDrive.setPower(0);
                robot.frontLeftDrive.setPower(0);
                robot.frontRightDrive.setPower(0);
            }
            if (System.currentTimeMillis() - lastStepTime >= 150) {
                if (robot.HighRiserLeft.getPosition() < 1) {
                    if (System.currentTimeMillis() - lastStepTime >= 150) {
                        lastStepTime = System.currentTimeMillis();
                        robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
                        robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
                    }
                }
            }
            if (System.currentTimeMillis() - lastStepTime >= 150) {
                if (robot.LowRiserLeft.getPosition() < 1 && robot.HighRiserLeft.getPosition() == 1) {
                    if (System.currentTimeMillis() - lastStepTime >= 150) {
                        lastStepTime = System.currentTimeMillis();
                        robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                        robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
                    }
                }
            }
            if (System.currentTimeMillis() >= 250) {
                robot.backLeftDrive.setPower(1);
                robot.backRightDrive.setPower(1);
                robot.frontLeftDrive.setPower(1);
                robot.frontRightDrive.setPower(1);
                if (System.currentTimeMillis() - lastStepTime >= 250) {
                    robot.backLeftDrive.setPower(0);
                    robot.backRightDrive.setPower(0);
                    robot.frontLeftDrive.setPower(0);
                    robot.frontRightDrive.setPower(0);
                }
            }

        }



     if (engine.gamepad2.start) {

         if (System.currentTimeMillis() - lastStepTime >= 150) {
             lastStepTime = System.currentTimeMillis();

             switch (CyclingArmUpAndDown) {

                 // upper arm up
                 case 0:
                     if (robot.HighRiserLeft.getPosition() < 1) {
                         robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
                         robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
                     } else {
                         CyclingArmUpAndDown = CyclingArmUpAndDown + 1;
                     }
                     break;

                 // lower arm up
                 case 1:
                     if (robot.LowRiserLeft.getPosition() < 1) {
                         robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                         robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
                     } else {
                         CyclingArmUpAndDown = CyclingArmUpAndDown + 1;
                     }
                     break;

                 // lower arm down
                 case 2:
                     if (robot.LowRiserLeft.getPosition() >= 0.44) {
                         robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                         robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
                     } else {
                         CyclingArmUpAndDown = CyclingArmUpAndDown + 1;
                     }
                     break;

                 // upper arm down
                 case 3:
                     if (robot.HighRiserLeft.getPosition() >= 0.45) {
                         robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - 0.05);
                         robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - 0.05);
                     } else {
                         CyclingArmUpAndDown = 0;
                     }
                     break;

                 default:
                     break;

             } // end of switch
         }// end of time if statement
     }// end of start button press



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
}
