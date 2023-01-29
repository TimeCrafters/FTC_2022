package org.timecrafters.TeleOp.states;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class TeleOPArmDriver extends CyberarmState {
    private final PhoenixBot1 robot;
    private long lastStepTime = 0;
    private int CyclingArmUpAndDown = 0;
    private GamepadChecker gamepad2Checker;
    private int Opportunity, Endeavour, Peanut;
    private double drivePower, armPower;
    private double MinimalPower = 0.25, topServoOffset = -0.05, lowServoOffset = -0.05;
    private double servoCollectLow = 0.40; //Low servos, A button
//    private double servoCollectHigh = 0.40; //High servos, A button
    private double servoLowLow = 0.5; //Low servos, X button
//    private double servoLowHigh = 0.75; //High servos, X button
    private double servoMedLow = 0.5; //Low servos, B button
//    private double servoMedHigh = 0.9; //High servos, B button
    private double servoHighLow = 0.8; //Low servos, Y button
//    private double servoHighHigh = 0.9; //High servos, Y button
    private int armMotorCollect = 0;
    private int armMotorLow = 100;
    private int armMotorMed = 1000;
    private int armMotorHigh = 1600;

    public TeleOPArmDriver(PhoenixBot1 robot) {
        this.robot = robot;
    }

    @Override
    public void telemetry() {
        engine.telemetry.addLine("Arm Driver:");
        engine.telemetry.addData("Arm Motor Power", robot.ArmMotor.getPower());
        engine.telemetry.addData("Low Riser Right Position", robot.LowRiserRight.getPosition());
        engine.telemetry.addData("Low Riser Left Position", robot.LowRiserLeft.getPosition());
    }

    @Override
    public void init() {
        robot.ArmMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.LowRiserLeft.setDirection(Servo.Direction.FORWARD);
        robot.LowRiserRight.setDirection(Servo.Direction.REVERSE);
        robot.LowRiserLeft.setPosition(0.45);
        robot.LowRiserRight.setPosition(0.45);
        robot.ArmMotor.setPower(0);
        robot.armMotorEncoder.setPower(0);
        Opportunity = 0;
        Endeavour = 0;



        gamepad2Checker = new GamepadChecker(engine, engine.gamepad2);
    }

@Override
public void exec() { //CHANGE ALL THE ENDEAVOUR TARGETS FROM SERVO POSITIONS TO MM HEIGHTS!!

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
            if (robot.armMotorEncoder.getCurrentPosition() < armMotorHigh - 5)/* <-- high level too low*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.ArmMotor.setPower(0.5);
                }
            }
            if (robot.LowRiserLeft.getPosition() < servoHighLow - 5)/* <-- low level too low*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
                }
            }
            if (robot.armMotorEncoder.getCurrentPosition() >= armMotorHigh &&
                    robot.LowRiserLeft.getPosition() >= servoHighLow) {
                Endeavour = 0;
            }
        }

        if (Endeavour == 3) {
            if (robot.LowRiserLeft.getPosition() > servoMedLow + 5)/* <-- low level too high*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() < servoMedLow - 5)/* <-- low level too low*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() <= servoMedLow + 5 &&
                    robot.armMotorEncoder.getCurrentPosition() > armMotorMed + 5)/* <-- high level too high*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.ArmMotor.setPower(-0.5);
                }
            }
            if (robot.LowRiserLeft.getPosition() < servoMedLow + 5 &&
                    robot.armMotorEncoder.getCurrentPosition() < armMotorMed - 5)/* <-- high level too low*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.ArmMotor.setPower(0.5);
                }
            }
            if (robot.LowRiserLeft.getPosition() > servoMedLow - 5 &&
                    robot.LowRiserLeft.getPosition() <= servoMedLow &&
                    robot.armMotorEncoder.getCurrentPosition() > armMotorMed - 5) {
                Endeavour = 0;
            }
        }

        if (Endeavour == 2) {
            if (robot.LowRiserLeft.getPosition() > servoLowLow + 5)/* <-- low level too high*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() < servoLowLow - 5)/* <-- low level too low*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
                }
            }
            if (robot.LowRiserLeft.getPosition() <= servoLowLow + 5 &&
                    robot.LowRiserLeft.getPosition() > servoLowLow - 5 &&
                    robot.armMotorEncoder.getCurrentPosition() > armMotorLow)/* <-- high level too high*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.ArmMotor.setPower(-0.5);
                }
            }
            if (robot.LowRiserLeft.getPosition() <= servoLowLow + 5 &&
                    robot.armMotorEncoder.getCurrentPosition() < armMotorLow - 5)/* <-- high level too low*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.ArmMotor.setPower(0.5);
                }
            }
            if (robot.LowRiserLeft.getPosition() > servoLowLow - 5 &&
                    robot.LowRiserLeft.getPosition() <= servoLowLow + 5 &&
                    robot.armMotorEncoder.getCurrentPosition() > armMotorLow - 5) {
                Endeavour = 0;
            }
        }

        if (Endeavour == 1) {
            if (robot.LowRiserLeft.getPosition() >= servoCollectLow + 5)/* <-- low level too high*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
                }
            } else if (robot.LowRiserLeft.getPosition() <= servoCollectLow &&
                    robot.armMotorEncoder.getCurrentPosition() > armMotorCollect)/* <-- high level too high*/ {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.ArmMotor.setPower(-0.5);
                }
            } else if (robot.LowRiserLeft.getPosition() <= servoCollectLow + 5 &&
                    robot.armMotorEncoder.getCurrentPosition() <= armMotorCollect) {
                Endeavour = 0;
            }
        }

        if (engine.gamepad2.dpad_left && Peanut != 1) {
            Peanut = 1;
        }

        if (engine.gamepad2.dpad_right && Peanut != 2) {
            Peanut = 2;
        }

        if (engine.gamepad2.dpad_left && Peanut == 1 || engine.gamepad2.dpad_right && Peanut == 2) {
            robot.collectorLeft.setPower(0);
            robot.collectorRight.setPower(0);
        }

        if (Peanut == 1) {
            robot.collectorRight.setPower(1);
            robot.collectorLeft.setPower(-1);
        }

        if (Peanut == 2) {
            robot.collectorLeft.setPower(1);
            robot.collectorRight.setPower(-1);
        }

        if (engine.gamepad2.right_trigger > 0.1) {
            armPower = engine.gamepad2.right_trigger;
            robot.ArmMotor.setPower(armPower);
        } else if (engine.gamepad2.left_trigger > 0.1) {
            armPower = -(engine.gamepad2.left_trigger);
            robot.ArmMotor.setPower(armPower);
        }

    }
}