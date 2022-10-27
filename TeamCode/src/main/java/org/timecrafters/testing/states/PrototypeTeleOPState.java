package org.timecrafters.testing.states;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.Vuforia;

import org.cyberarm.engine.V2.CyberarmState;

public class PrototypeTeleOPState extends CyberarmState {

    private final PrototypeBot1 robot;
    public boolean A;
    public boolean X;
    public boolean Y;
    private boolean bprev; // sticky key variable for the gamepad
    private double drivePower = 1;
    private boolean UpDPad;
    private double collectorRiserPosition;
    private boolean raiseHighRiser = true;
    private long lastStepTime = 0, BeginningofActionTime;
    private boolean raiseLowRiser = true;
    private double speed = 1; // used for the normal speed while driving
    private double slowSpeed = 0.5; // used for slow mode speed while driving
    private int CyclingArmUpAndDown = 0;
    private int DriveForwardAndBack, AutoChain;
    private int RobotPosition, RobotStartingPosition;
    private double RobotRotation;

    public PrototypeTeleOPState(PrototypeBot1 robot) {
        this.robot = robot;
    }


    @Override
    public void telemetry() {
        engine.telemetry.addData("High Riser Right Position", robot.HighRiserRight.getPosition());
        engine.telemetry.addData("High Riser Left Position", robot.HighRiserLeft.getPosition());
        engine.telemetry.addData("Low Riser Right Position", robot.LowRiserRight.getPosition());
        engine.telemetry.addData("Low Riser Left Position", robot.LowRiserLeft.getPosition());
        engine.telemetry.addData("AutoChain", AutoChain);
        engine.telemetry.addData("IMU", robot.imu.getAngularOrientation().firstAngle);
//        engine.telemetry.addData("Cycling Arm up / Down Case", CyclingArmUpAndDown);

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

    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void exec() {

        //Gamepad Read

        A = engine.gamepad1.a;
        X = engine.gamepad1.x;
        Y = engine.gamepad1.y;
        UpDPad = engine.gamepad1.dpad_up;

        //drive speed toggle
//
//        boolean b = engine.gamepad1.b;
//
//        if (b && !bprev) {
//            bprev = true;
//            if (drivePower == speed) {
//                drivePower = slowSpeed;
//            } else {
//                drivePower = speed;
//            }
//        }
//        if (!b){
//            bprev = false;
//        }

//        //Drivetrain Variables
//        double y = -engine.gamepad1.left_stick_y; // Remember, this is reversed! because of the negative
//        double x = engine.gamepad1.right_stick_x * 1.1; // Counteract imperfect strafing
//        double rx = engine.gamepad1.left_stick_x;
//
//        // Denominator is the largest motor power (absolute value) or 1
//        // This ensures all the powers maintain the same ratio, but only when
//        // at least one is out of the range [-1, 1]
//
//        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
//        double frontLeftPower = (y + x + rx) / denominator;
//        double backLeftPower = (y - x + rx) / denominator;
//        double frontRightPower = (y - x - rx) / denominator;
//        double backRightPower = (y + x - rx) / denominator;
//
//        robot.frontLeftDrive.setPower(-frontLeftPower * speed);
//        robot.backLeftDrive.setPower(backLeftPower * speed);
//        robot.frontRightDrive.setPower(-frontRightPower * speed);
//        robot.backRightDrive.setPower(backRightPower * speed);

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
            if (RobotRotation < 0) {
                drivePower = 0.9;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > 0) {
                drivePower = -0.9;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < -177 || RobotRotation > 177) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }

        if (engine.gamepad1.y) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            if (RobotRotation < -3) {
                drivePower = -0.9;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > 3) {
                drivePower = 0.9;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > -3 && RobotRotation < 3) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }
        if (engine.gamepad1.x) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            if (RobotRotation > -45 && RobotRotation < 132) {//CCW
                drivePower = -0.9;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < -45 || RobotRotation > 138) {//CW
                drivePower = 0.9;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < 138 && RobotRotation > 132) {
                drivePower = 0;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
        }
        if (engine.gamepad1.b) {
            RobotRotation = robot.imu.getAngularOrientation().firstAngle;
            if (RobotRotation < 45 && RobotRotation > -132) {//CCW
                drivePower = 0.9;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation > 45 || RobotRotation < -138) {//CW
                drivePower = -0.9;
                robot.backLeftDrive.setPower(drivePower);
                robot.backRightDrive.setPower(-drivePower);
                robot.frontLeftDrive.setPower(drivePower);
                robot.frontRightDrive.setPower(-drivePower);
            }
            if (RobotRotation < -132 && RobotRotation > -138) {
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
            if (robot.HighRiserLeft.getPosition() < 0.9) {
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
            if (robot.HighRiserLeft.getPosition() < 1) {
                if (System.currentTimeMillis() - lastStepTime >= 150) {
                    lastStepTime = System.currentTimeMillis();
                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
                }
            }

            if (robot.LowRiserLeft.getPosition() < 0.75 && robot.HighRiserLeft.getPosition() == 1) {
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



//
//        }
//
//        if (engine.gamepad2.b) {
//
//            robot.collectorLeft.setPower(1);
//            robot.collectorRight.setPower(-1);
//
//        }
//
//        if (engine.gamepad2.x) {
//
//            robot.collectorLeft.setPower(-1);
//            robot.collectorRight.setPower(1);
//
//        }
////
////        }
////
////        if (engine.gamepad2.right_stick_y < -0.1) {
////            robot.LowRiserRight.setPosition(0.6);
////            robot.LowRiserLeft.setPosition(0.6);
////        }
////
////        if (engine.gamepad2.right_stick_y > 0.1) {
////            robot.LowRiserRight.setPosition(0.45);
////            robot.LowRiserLeft.setPosition(0.45);
////        }
//
////        if (engine.gamepad2.start) {
////            if (System.currentTimeMillis() - lastStepTime >= 150) {
////                lastStepTime = System.currentTimeMillis();
////
////                if (raiseHighRiser) {
////                    if (robot.HighRiserLeft.getPosition() >= 1) {
////                        if (raiseLowRiser) {
////                            raiseHighRiser = false;
////                        }
////                    } else {
////                        robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
////                        robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
////                    }
////                } else {
////                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - 0.035);
////                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - 0.035);
////
////                    if (robot.HighRiserLeft.getPosition() <= 0.45) {
////                        raiseHighRiser = true;
////                    }
////                }
////            }
////        }
//        // SPENCER____________________________________________________________________
//        if (engine.gamepad1.start) {
//            RobotPosition = robot.backRightDrive.getCurrentPosition();
//
//            switch (DriveForwardAndBack) {
//
//                case 0:
//                    RobotStartingPosition = RobotPosition;
//                  drivePower = 1;
//
//                  DriveForwardAndBack += 1;
//                    break;
//
//                case 1:
//                    if (RobotPosition - RobotStartingPosition < 6250){
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(drivePower);
//                    } else
//                    {
//                        DriveForwardAndBack += 1;
//                        drivePower = -1;
//                    }
//                    break;
//                case 2:
//                    if (RobotPosition - RobotStartingPosition >= 0) {
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(drivePower);
//                    } else
//                    {
//                        DriveForwardAndBack += 1;
//                    }
//                    break;
//                case 3:
//                    if (robot.imu.getAngularOrientation().firstAngle > -90) {
////                        *+90 degrees is counterclockwise, -90 is clockwise*
//                        drivePower = 0.4;
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(-drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(-drivePower);
//
//                    } else
//                    {
//                        DriveForwardAndBack += 1;
//                    }
//                    break;
//                case 4:
//                    robot.backLeftDrive.setPower(0);
//                    robot.backRightDrive.setPower(0);
//                    robot.frontLeftDrive.setPower(0);
//                    robot.frontRightDrive.setPower(0);
//                    break;
//
//
//            } // switch ending
//            } // if gamepad 1 start ending
//        else {
//
////            robot.backLeftDrive.setPower(0);
////            robot.backRightDrive.setPower(0);
////            robot.frontLeftDrive.setPower(0);
////            robot.frontRightDrive.setPower(0);
//
//        }
//
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

//        if (engine.gamepad2.left_stick_y > 0.1) {
//            robot.HighRiserLeft.setPosition(0.5);
//            robot.HighRiserRight.setPosition(-0.5);
//        }
//
//        if (engine.gamepad2.left_stick_y < -0.1) {
//            robot.HighRiserLeft.setPosition(-1);
//            robot.HighRiserRight.setPosition(1);
//        }

//        if (engine.gamepad2.right_bumper) {
//            robot.LowRiserRight.setPosition(1);
//            robot.LowRiserLeft.setPosition(0);
//        }

//        if (engine.gamepad2.left_bumper) {
//            robot.HighRiserRight.setPosition(0);
//            robot.HighRiserLeft.setPosition(1);
//        }
//        For raising, high risers ALWAYS raise first, for lowering, low risers ALWAYS lower first.

//        if (engine.gamepad2.back) {
//            RobotPosition = robot.backRightDrive.getCurrentPosition();
//
//            switch (AutoChain) {
//
//                case 0://Initialize
//                    RobotStartingPosition = RobotPosition;
//                    AutoChain += 1;
//                    break;
//
//                case 1://Drive 1 square forward
//                    if (RobotPosition - RobotStartingPosition < 2500){
//                        drivePower = 1;
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(drivePower);
//                    } else
//                    {
//                        AutoChain += 1;
//                    }
//                    break;
//
//                case 2://Rotate Counterclockwise for 45 degrees
//                        RobotRotation = robot.imu.getAngularOrientation().firstAngle;
//                    if (RobotRotation <= 45) {
//                        drivePower = 0.4;
//                        robot.backLeftDrive.setPower(-drivePower);
//                        robot.backRightDrive.setPower(drivePower);
//                        robot.frontLeftDrive.setPower(-drivePower);
//                        robot.frontRightDrive.setPower(drivePower);
//                    } else
//                    {
//                        AutoChain += 1;
//                        drivePower = 0;
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(drivePower);
//                    }
//                    break;
//
//                case 3://Raise upper arm fully
//                    if (robot.HighRiserLeft.getPosition() < 1) {
//                        if (System.currentTimeMillis() - lastStepTime >= 150) {
//                            lastStepTime = System.currentTimeMillis();
//                        robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
//                        robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
//                        }
//                    } else {
//                        AutoChain +=1;
//                    }
//                    break;
//
//                case 4://Raise lower arm fully
//                    if (robot.LowRiserLeft.getPosition() < 1) {
//                        if (System.currentTimeMillis() - lastStepTime >= 150) {
//                            lastStepTime = System.currentTimeMillis();
//                            robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
//                            robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
//                        }
//                    } else {
//                        AutoChain += 1;
//
//                    }
//                    break;
//
//                case 5://initialize for moving forward
//                    RobotStartingPosition = robot.backRightDrive.getCurrentPosition();
//                    AutoChain += 1;
//                    break;
//
//                case 6://Drive forward 1/4 square
//                    if (RobotPosition - RobotStartingPosition < 1200){
//                        drivePower = 1;
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(drivePower);
//                    } else
//                    {
//                        drivePower = 0;
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(drivePower);
//                        AutoChain += 1;
//                    }
//                    break;
//
//                case 7://Lower low arm fully
//                    if (robot.LowRiserLeft.getPosition() > 0.5) {
//                        if (System.currentTimeMillis() - lastStepTime >= 150) {
//                            lastStepTime = System.currentTimeMillis();
//                            robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
//                            robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
//
//                        }
//                    }else
//                        {
//                            AutoChain += 1;
//                        }
//                        break;
//
//                case 8:
//                    BeginningofActionTime = System.currentTimeMillis();
//                    AutoChain += 1;
//
//                    break;
//                case 9://Eject
//                    if (System.currentTimeMillis() - BeginningofActionTime < 2000) {
//                            robot.collectorRight.setPower(-1);
//                            robot.collectorLeft.setPower(1);
//
//                    } else {
//                        robot.collectorLeft.setPower(0);
//                        robot.collectorRight.setPower(0);
//                        AutoChain += 1;
//                    }
//                    break;
//
//                case 10://Raise low arm
//                     if (robot.LowRiserLeft.getPosition() < 1) {
//                         robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
//                         robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
//                     } else {
//                         AutoChain += 1;
//                     }
//                     break;
//
//                case 11://Initialize backup
//                    RobotStartingPosition = RobotPosition;
//                    AutoChain += 1;
//                    break;
//
//                case 12://Drive backwards 1/4 square
//                    if (RobotPosition - RobotStartingPosition > -1200){
//                        drivePower = -1;
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(drivePower);
//                    } else
//                    {
//                        drivePower = 0;
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(drivePower);
//                        AutoChain += 1;
//                    }
//                    break;
//
//                case 13://Turn 45 degrees clockwise
//                    RobotRotation = robot.imu.getAngularOrientation().firstAngle;
//                    if (RobotRotation > 0) {
//                        drivePower = 0.4;
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(-drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(-drivePower);
//                    } else
//                    {
//                        AutoChain += 1;
//                        drivePower = 0;
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(drivePower);
//                    }
//                    break;
//
//                case 14://Initialize
//                    RobotStartingPosition = RobotPosition;
//                    AutoChain += 1;
//                    break;
//
//                case 15://Drive 1 square forward
//                    if (RobotPosition - RobotStartingPosition < 2500){
//                        drivePower = 1;
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(drivePower);
//                    } else
//                    {
//                        AutoChain = 999;
//                        drivePower = 0;
//                        robot.backLeftDrive.setPower(drivePower);
//                        robot.backRightDrive.setPower(drivePower);
//                        robot.frontLeftDrive.setPower(drivePower);
//                        robot.frontRightDrive.setPower(drivePower);
//                    }
//                    break;
//
//                case  999:
//
//                            break;


        }


    }
