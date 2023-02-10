package org.timecrafters.Autonomous.TeleOp.states;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;

public class TeleOPArmDriver extends CyberarmState {
    private final PhoenixBot1 robot;
    private long lastStepTime = 0, Spirit;
    private int CyclingArmUpAndDown = 0;
    private GamepadChecker gamepad2Checker;
    private int Opportunity, JunctionHeight, Ingenuity;
    private double drivePower, armPower;
    private double MinimalPower = 0.25, topServoOffset = -0.05, lowServoOffset = -0.05;
    private double servoCollect = 0.45; //Low servos, A button
    //    private double servoCollectHigh = 0.40; //High servos, A button
    private double servoLow = 0.45; //Low servos, X button
    //    private double servoLowHigh = 0.75; //High servos, X button
    private double servoMed = 0.45; //Low servos, B button
    //    private double servoMedHigh = 0.9; //High servos, B button
    private double servoHigh = 0.8; //Low servos, Y button
    //    private double servoHighHigh = 0.9; //High servos, Y button
    private double ArmNeededPower;
    private int armMotorCollect = -100;
    private int armMotorLow = 280;
    private int armMotorMed = 430;
    private int armMotorHigh = 510;
    private int ArmMotorStepSize = 2;
    private int TargetPosition = 0, OverrideTarget = -64;

    public TeleOPArmDriver(PhoenixBot1 robot) {
        this.robot = robot;
    }

    @Override
    public void telemetry() {
        engine.telemetry.addLine("Arm Driver:");
        engine.telemetry.addData("Arm Motor Power", robot.ArmMotor.getPower());
        engine.telemetry.addData("Arm Motor Encoder Position", robot.ArmMotor.getCurrentPosition());
        engine.telemetry.addData("Low Riser Right Position", robot.LowRiserRight.getPosition());
        engine.telemetry.addData("Low Riser Left Position", robot.LowRiserLeft.getPosition());
        engine.telemetry.addData("Arm Motor TargetPosition", robot.ArmMotor.getTargetPosition());
        engine.telemetry.addData("Target Junction Height", JunctionHeight);
    }

    @Override
    public void init() {
        robot.LowRiserLeft.setDirection(Servo.Direction.FORWARD);
        robot.LowRiserRight.setDirection(Servo.Direction.REVERSE);
        robot.LowRiserLeft.setPosition(0.45);
        robot.LowRiserRight.setPosition(0.45);
        robot.ArmMotor.setTargetPosition(0);
        robot.ArmMotor.setPower(0.5);
        robot.ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.ArmMotor.setTargetPositionTolerance(5);
        armPower = 0.4;

        JunctionHeight = 0;
    }

    @Override
    public void exec() {

        double ratio = Math.abs(robot.ArmMotor.getCurrentPosition() - TargetPosition) / 400.0 + 0.1;


//        ArmNeededPower =  Math.abs((robot.ArmMotor.getTargetPosition() - robot.ArmMotor.getCurrentPosition()) / 920) + 0.25;
//        robot.ArmMotor.setTargetPosition(TargetPosition);
//        robot.ArmMotor.setPower(armPower);

        if (engine.gamepad2.y) {
            JunctionHeight = 4;
            TargetPosition = armMotorHigh;
        }
        if (engine.gamepad2.b) {
            JunctionHeight = 3;
            TargetPosition = armMotorMed;
        }
        if (engine.gamepad2.x) {
            JunctionHeight = 2;
            TargetPosition = armMotorLow;
        }
        if (engine.gamepad2.a) {
            JunctionHeight = 1;
            TargetPosition = armMotorCollect;
        }

        if (JunctionHeight == 4) {
            if (robot.LowRiserLeft.getPosition() <= servoHigh && robot.ArmMotor.getTargetPosition() != armMotorHigh) {
                robot.ArmMotor.setPower(0);
                robot.ArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.ArmMotor.setPower(armPower);
                robot.ArmMotor.setTargetPosition(armMotorHigh);
                robot.ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if (robot.ArmMotor.getCurrentPosition() >= armMotorLow && robot.LowRiserLeft.getPosition() < servoHigh) {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
                }
            } else if (robot.ArmMotor.getTargetPosition() == armMotorHigh && robot.LowRiserLeft.getPosition() == servoHigh) {
                JunctionHeight = 0;
            }
        }

        if (JunctionHeight == 3) {
            if (robot.LowRiserLeft.getPosition() <= servoMed && robot.ArmMotor.getTargetPosition() != armMotorMed) {
                robot.ArmMotor.setPower(0);
                robot.ArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.ArmMotor.setPower(armPower);
                robot.ArmMotor.setTargetPosition(armMotorMed);
                robot.ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if (robot.ArmMotor.getCurrentPosition() >= armMotorLow && robot.LowRiserLeft.getPosition() < servoMed) {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
                }
            } else if (robot.LowRiserLeft.getPosition() > servoMed) {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
                }
            } else if (robot.ArmMotor.getTargetPosition() == armMotorMed && robot.LowRiserLeft.getPosition() == servoMed) {
                JunctionHeight = 0;
            }
        }

        if (JunctionHeight == 2) {
            if (robot.LowRiserLeft.getPosition() <= servoLow && robot.ArmMotor.getTargetPosition() != armMotorLow) {
                robot.ArmMotor.setPower(0);
                robot.ArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.ArmMotor.setPower(armPower);
                robot.ArmMotor.setTargetPosition(armMotorLow);
                robot.ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if (robot.ArmMotor.getCurrentPosition() >= armMotorLow && robot.LowRiserLeft.getPosition() < servoLow) {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
                }
            } else if (robot.LowRiserLeft.getPosition() > servoLow) {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
                }
            } else if (robot.ArmMotor.getTargetPosition() == armMotorLow && robot.LowRiserLeft.getPosition() == servoLow) {
                JunctionHeight = 0;
            }
        }

        if (JunctionHeight == 1) {
            if (robot.LowRiserLeft.getPosition() <= servoCollect && robot.ArmMotor.getTargetPosition() != armMotorCollect) {
                robot.ArmMotor.setPower(0);
                robot.ArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.ArmMotor.setPower(armPower);
                robot.ArmMotor.setTargetPosition(armMotorCollect);
                robot.ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if (robot.LowRiserLeft.getPosition() > servoCollect) {
                if (System.currentTimeMillis() - lastStepTime >= 100) {
                    lastStepTime = System.currentTimeMillis();
                    robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                    robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
                }
            } else if (robot.ArmMotor.getTargetPosition() == armMotorCollect && robot.LowRiserLeft.getPosition() == servoCollect) {
                JunctionHeight = 0;
            }
        }

        if (engine.gamepad2.left_bumper) {
            if (robot.ArmMotor.getTargetPosition() != OverrideTarget) {
                OverrideTarget = robot.ArmMotor.getCurrentPosition() - 80;
                robot.ArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.ArmMotor.setPower(armPower);
                robot.ArmMotor.setTargetPosition(OverrideTarget);
                robot.ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
        }

        if (engine.gamepad2.right_bumper) {
            if (robot.ArmMotor.getTargetPosition() != OverrideTarget) {
                OverrideTarget = robot.ArmMotor.getCurrentPosition() + 80;
                robot.ArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.ArmMotor.setPower(armPower);
                robot.ArmMotor.setTargetPosition(OverrideTarget);
                robot.ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
        }

        if (engine.gamepad2.dpad_left) {
            robot.collectorRight.setPower(-1);
            robot.collectorLeft.setPower(-1);
        } else if (engine.gamepad2.dpad_right) {
            robot.collectorLeft.setPower(1);
            robot.collectorRight.setPower(1);
        } else {
            robot.collectorLeft.setPower(0);
            robot.collectorRight.setPower(0);
        }

    }

    @Override
    public void buttonUp(Gamepad gamepad, String button) {
        if (engine.gamepad2 == gamepad) {
            if (button.equals("right_bumper")) {
                OverrideTarget = -64;
            } else if (button.equals("left_bumper")) {
                OverrideTarget = -64;
            }
        }
    }

    @Override
    public void buttonDown(Gamepad gamepad, String button) {
        if (engine.gamepad2 == gamepad) {
            if (button.equals("right_bumper")) {

            } else if (button.equals("left_bumper")) {

            }
        }
    }
}