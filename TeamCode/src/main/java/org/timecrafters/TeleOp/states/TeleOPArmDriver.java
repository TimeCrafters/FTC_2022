package org.timecrafters.TeleOp.states;

import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class TeleOPArmDriver extends CyberarmState {
    private final PhoenixBot1 robot;
    private long lastStepTime = 0;
    private int CyclingArmUpAndDown = 0;
    private GamepadChecker gamepad2Checker;
    private int Opportunity, Endeavour;
    private double drivePower;
    private double MinimalPower = 0.25, topServoOffset = -0.05, lowServoOffset = -0.05;
    private double servoCollectLow = 0.40; //Low servos, A button
    private double servoCollectHigh = 0.40; //High servos, A button
    private double servoLowLow = 0.5; //Low servos, X button
    private double servoLowHigh = 0.75; //High servos, X button
    private double servoMedLow = 0.5; //Low servos, B button
    private double servoMedHigh = 0.9; //High servos, B button
    private double servoHighLow = 0.8; //Low servos, Y button
    private double servoHighHigh = 0.9; //High servos, Y button

    public TeleOPArmDriver(PhoenixBot1 robot) {
        this.robot = robot;
    }

    @Override
    public void telemetry() {
        engine.telemetry.addLine("Arm Driver:");
//        engine.telemetry.addData("High Riser Right Position", robot.HighRiserRight.getPosition());
//        engine.telemetry.addData("High Riser Left Position", robot.HighRiserLeft.getPosition());
        engine.telemetry.addData("Low Riser Right Position", robot.LowRiserRight.getPosition());
        engine.telemetry.addData("Low Riser Left Position", robot.LowRiserLeft.getPosition());
    }

    @Override
    public void init() {
//        robot.HighRiserLeft.setDirection(Servo.Direction.REVERSE);
//        robot.HighRiserRight.setDirection(Servo.Direction.FORWARD);
        robot.LowRiserLeft.setDirection(Servo.Direction.FORWARD);
        robot.LowRiserRight.setDirection(Servo.Direction.REVERSE);
        robot.LowRiserLeft.setPosition(0.45);
        robot.LowRiserRight.setPosition(0.45);
//        robot.HighRiserLeft.setPosition(0.45);
//        robot.HighRiserRight.setPosition(0.45);
        Opportunity = 0;
        Endeavour = 0;


        gamepad2Checker = new GamepadChecker(engine, engine.gamepad2);
    }

    @Override
    public void exec() {

        if (engine.gamepad2.y) {
            Endeavour = 4;
        }
        if (engine.gamepad2.b) {
            Endeavour = 3;
        }
        if (engine.gamepad2.x) {
            Endeavour = 2;
        }
        if (engine.gamepad2.a) {
            Endeavour = 1;
        }

        if (Endeavour == 4) {
//            if (robot.HighRiserLeft.getPosition() < servoHighHigh - 0.01)/* <-- high level too low*/ {
//                if (System.currentTimeMillis() - lastStepTime >= 100) {
//                    lastStepTime = System.currentTimeMillis();
//                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
//                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
//                }
        }
        if (robot.LowRiserLeft.getPosition() < servoHighLow - 0.01)/* <-- low level too low*/ {
            if (System.currentTimeMillis() - lastStepTime >= 100) {
                lastStepTime = System.currentTimeMillis();
                robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
            }

//            if (robot.HighRiserLeft.getPosition() >= servoHighHigh &&
//                    robot.LowRiserLeft.getPosition() >= servoHighLow) {
//                Endeavour = 0;
//            }
        }

        if (Endeavour == 3) {
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
//            if (robot.LowRiserLeft.getPosition() <= servoMedLow + 0.01 &&
//                    robot.HighRiserLeft.getPosition() > servoMedHigh + 0.01)/* <-- high level too high*/ {
//                if (System.currentTimeMillis() - lastStepTime >= 100) {
//                    lastStepTime = System.currentTimeMillis();
//                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - 0.05);
//                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - 0.05);
//                }
//            }
//            if (robot.LowRiserLeft.getPosition() < servoMedLow + 0.01 &&
//                    robot.HighRiserLeft.getPosition() < servoMedHigh - 0.01)/* <-- high level too low*/ {
//                if (System.currentTimeMillis() - lastStepTime >= 100) {
//                    lastStepTime = System.currentTimeMillis();
//                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
//                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
//                }
//            }
//            if (robot.LowRiserLeft.getPosition() > servoMedLow - 0.01 &&
//                    robot.LowRiserLeft.getPosition() <= servoMedLow &&
//                    robot.HighRiserLeft.getPosition() > servoMedHigh - 0.01 &&
//                    robot.HighRiserLeft.getPosition() <= servoMedHigh) {
//                Endeavour = 0;
//            }
//        }

            if (Endeavour == 2) {
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
//            if (robot.LowRiserLeft.getPosition() <= servoLowLow + 0.01 &&
//                    robot.LowRiserLeft.getPosition() > servoLowLow - 0.01 &&
//                    robot.HighRiserLeft.getPosition() > servoLowHigh)/* <-- high level too high*/ {
//                if (System.currentTimeMillis() - lastStepTime >= 100) {
//                    lastStepTime = System.currentTimeMillis();
//                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - 0.05);
//                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - 0.05);
//                }
//            }
//            if (robot.LowRiserLeft.getPosition() <= servoLowLow + 0.01 &&
//                    robot.HighRiserLeft.getPosition() < servoLowHigh - 0.01)/* <-- high level too low*/ {
//                if (System.currentTimeMillis() - lastStepTime >= 100) {
//                    lastStepTime = System.currentTimeMillis();
//                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + 0.05);
//                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + 0.05);
//                }
//            }
//            if (robot.LowRiserLeft.getPosition() > servoLowLow - 0.01 &&
//                    robot.LowRiserLeft.getPosition() <= servoLowLow + 0.01 &&
//                    robot.HighRiserLeft.getPosition() > servoLowHigh - 0.01 &&
//                    robot.HighRiserLeft.getPosition() <=  servoLowHigh + 0.01) {
//                Endeavour = 0;
//            }
//        }

                if (Endeavour == 1) {
                    if (robot.LowRiserLeft.getPosition() >= servoCollectLow + 0.01)/* <-- low level too high*/ {
                        if (System.currentTimeMillis() - lastStepTime >= 100) {
                            lastStepTime = System.currentTimeMillis();
                            robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                            robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
                        }
//            } else if (robot.LowRiserLeft.getPosition() <= servoCollectLow &&
//                    robot.HighRiserLeft.getPosition() > servoCollectHigh)/* <-- high level too high*/ {
//                if (System.currentTimeMillis() - lastStepTime >= 100) {
//                    lastStepTime = System.currentTimeMillis();
//                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - 0.05);
//                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - 0.05);
//                }
//            } else if (robot.LowRiserLeft.getPosition() <= servoCollectLow + 0.01 &&
//                    robot.HighRiserLeft.getPosition() <= servoCollectHigh) {
//                Endeavour = 0;
//            }
                    }

                }
            }
        }
    }
}