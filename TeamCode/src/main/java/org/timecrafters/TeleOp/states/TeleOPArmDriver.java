package org.timecrafters.TeleOp.states;

import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;

public class TeleOPArmDriver extends CyberarmState {
    private final PhoenixBot1 robot;
    private long lastStepTime = 0;
    private int CyclingArmUpAndDown = 0;
    private GamepadChecker gamepad2Checker;

    public TeleOPArmDriver(PhoenixBot1 robot) {
        this.robot = robot;
    }

    @Override
    public void telemetry() {
        engine.telemetry.addLine("Arm Driver:");
        engine.telemetry.addData("High Riser Right Position", robot.HighRiserRight.getPosition());
        engine.telemetry.addData("High Riser Left Position", robot.HighRiserLeft.getPosition());
        engine.telemetry.addData("Low Riser Right Position", robot.LowRiserRight.getPosition());
        engine.telemetry.addData("Low Riser Left Position", robot.LowRiserLeft.getPosition());
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


        gamepad2Checker = new GamepadChecker(engine, engine.gamepad2);
    }

    @Override
    public void exec() {
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

        gamepad2Checker.update();

    }
}
