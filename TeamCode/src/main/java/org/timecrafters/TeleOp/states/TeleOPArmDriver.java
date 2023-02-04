package org.timecrafters.TeleOp.states;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmState;
import org.cyberarm.engine.V2.GamepadChecker;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class TeleOPArmDriver extends CyberarmState {
    private final PhoenixBot1 robot;
    private long lastStepTime = 0, Spirit;
    private int CyclingArmUpAndDown = 0;
    private GamepadChecker gamepad2Checker;
    private int Opportunity, JunctionHeight, Ingenuity;
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
    private double ArmNeededPower;
    private int armMotorCollect = 0;
    private int armMotorLow = 240;
    private int armMotorMed = 380;
    private int armMotorHigh = 463;
    private int ArmMotorStepSize = 2;
    private int TargetPosition = 0;

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

        JunctionHeight = 0;
    }



@Override
public void exec() {

    double ratio = Math.abs(robot.ArmMotor.getCurrentPosition() - TargetPosition) / 400.0 + 0.1;


//        ArmNeededPower =  Math.abs((robot.ArmMotor.getTargetPosition() - robot.ArmMotor.getCurrentPosition()) / 920) + 0.25;
        armPower = ratio;
        robot.ArmMotor.setPower(armPower);

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

    if (JunctionHeight == 4 && System.currentTimeMillis() - lastStepTime >= 100) {
        lastStepTime = System.currentTimeMillis();
        robot.ArmMotor.setTargetPosition(armMotorHigh);
        if (robot.ArmMotor.getCurrentPosition() < armMotorHigh)/* <-- high level too low*/ {
            robot.ArmMotor.setTargetPosition(robot.ArmMotor.getCurrentPosition() + ArmMotorStepSize);
        }
        else if (robot.LowRiserLeft.getPosition() < servoHighLow && robot.ArmMotor.getCurrentPosition() >= armMotorLow)/* <-- low level too low*/ {
            robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
            robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
        }
        if (robot.ArmMotor.getCurrentPosition() >= armMotorHigh &&
                robot.LowRiserLeft.getPosition() >= servoHighLow) {
            JunctionHeight = 0;
        }
    }

    if (JunctionHeight == 3) {
        robot.ArmMotor.setTargetPosition(armMotorMed);
        if (robot.LowRiserLeft.getPosition() > servoMedLow + 5)/* <-- low level too high*/ {
            if (System.currentTimeMillis() - lastStepTime >= 100) {
                lastStepTime = System.currentTimeMillis();
                robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
            }
        }
        else if (robot.LowRiserLeft.getPosition() < servoMedLow - 5 && robot.ArmMotor.getCurrentPosition() >= armMotorLow)/* <-- low level too low*/ {
            if (System.currentTimeMillis() - lastStepTime >= 100) {
                lastStepTime = System.currentTimeMillis();
                robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
            }
        }
        else if (robot.LowRiserLeft.getPosition() <= servoMedLow &&
                robot.ArmMotor.getCurrentPosition() > armMotorMed)/* <-- high level too high*/ {
            if (System.currentTimeMillis() - lastStepTime >= 100) {
                lastStepTime = System.currentTimeMillis();
                robot.ArmMotor.setTargetPosition(robot.ArmMotor.getCurrentPosition() - ArmMotorStepSize);
            }
        }
        else if (robot.LowRiserLeft.getPosition() < servoMedLow &&
                robot.ArmMotor.getCurrentPosition() < armMotorMed)/* <-- high level too low*/ {
            if (System.currentTimeMillis() - lastStepTime >= 100) {
                lastStepTime = System.currentTimeMillis();
                robot.ArmMotor.setTargetPosition(robot.ArmMotor.getCurrentPosition() + ArmMotorStepSize);
            }
        }
        else if (robot.LowRiserLeft.getPosition() > servoMedLow - 5 &&
                robot.LowRiserLeft.getPosition() <= servoMedLow &&
                robot.ArmMotor.getCurrentPosition() >= armMotorMed) {
            JunctionHeight = 0;
        }
    }

    if (JunctionHeight == 2) {
        robot.ArmMotor.setTargetPosition(armMotorLow);
        if (robot.LowRiserLeft.getPosition() > servoLowLow + 5)/* <-- low level too high*/ {
            if (System.currentTimeMillis() - lastStepTime >= 100) {
                lastStepTime = System.currentTimeMillis();
                robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
            }
        }
        else if (robot.LowRiserLeft.getPosition() < servoLowLow - 5 && robot.ArmMotor.getCurrentPosition() >= armMotorLow)/* <-- low level too low*/ {
            if (System.currentTimeMillis() - lastStepTime >= 100) {
                lastStepTime = System.currentTimeMillis();
                robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + 0.05);
                robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + 0.05);
            }
        }
        else if (robot.LowRiserLeft.getPosition() <= servoLowLow &&
                robot.LowRiserLeft.getPosition() > servoLowLow - 5 &&
                robot.ArmMotor.getCurrentPosition() > armMotorLow)/* <-- high level too high*/ {
            if (System.currentTimeMillis() - lastStepTime >= 100) {
                lastStepTime = System.currentTimeMillis();
                robot.ArmMotor.setTargetPosition(robot.ArmMotor.getCurrentPosition() - ArmMotorStepSize);
            }
        }
        else if (robot.LowRiserLeft.getPosition() <= servoLowLow &&
                robot.ArmMotor.getCurrentPosition() < armMotorLow)/* <-- high level too low*/ {
            if (System.currentTimeMillis() - lastStepTime >= 100) {
                lastStepTime = System.currentTimeMillis();
                robot.ArmMotor.setTargetPosition(robot.ArmMotor.getCurrentPosition() + ArmMotorStepSize);
            }
        }
        else if (robot.LowRiserLeft.getPosition() > servoLowLow - 5 &&
                robot.LowRiserLeft.getPosition() <= servoLowLow &&
                robot.ArmMotor.getCurrentPosition() >= armMotorLow) {
            JunctionHeight = 0;
        }
    }

    if (JunctionHeight == 1) {
        robot.ArmMotor.setTargetPosition(armMotorCollect);
        if (robot.LowRiserLeft.getPosition() >= servoCollectLow)/* <-- low level too high*/ {
            if (System.currentTimeMillis() - lastStepTime >= 100) {
                lastStepTime = System.currentTimeMillis();
                robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - 0.05);
                robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - 0.05);
            }
        } else if (robot.LowRiserLeft.getPosition() <= servoCollectLow &&
                robot.ArmMotor.getCurrentPosition() > armMotorCollect)/* <-- high level too high*/ {
            if (System.currentTimeMillis() - lastStepTime >= 100) {
                lastStepTime = System.currentTimeMillis();
                robot.ArmMotor.setTargetPosition(robot.ArmMotor.getCurrentPosition() - ArmMotorStepSize);
            }
        } else if (robot.LowRiserLeft.getPosition() <= servoCollectLow &&
                robot.ArmMotor.getCurrentPosition() <= armMotorCollect) {
            JunctionHeight = 0;
        }
    }

//    if (engine.gamepad2.dpad_left && Ingenuity != 1) {
//        Ingenuity = 1;
//    }
//
//    if (engine.gamepad2.dpad_right && Ingenuity != 2) {
//        Ingenuity = 2;
//    }
//
//    if (Ingenuity == 1) {
//        robot.collectorRight.setPower(-1);
//        robot.collectorLeft.setPower(-1);
//    }
//
//    if (Ingenuity == 2) {
//        robot.collectorLeft.setPower(1);
//        robot.collectorRight.setPower(1);
//    }

    if (engine.gamepad2.right_trigger > 0.1) {
        JunctionHeight = 0;
        if (System.currentTimeMillis() - lastStepTime >= 100 && robot.ArmMotor.getCurrentPosition() < armMotorHigh + 5) {
            lastStepTime = System.currentTimeMillis();
            robot.ArmMotor.setTargetPosition(robot.ArmMotor.getCurrentPosition() + 1);
            robot.ArmMotor.setPower(armPower);
        } else if (engine.gamepad2.left_trigger > 0.1) {
            JunctionHeight = 0;
            if (System.currentTimeMillis() - lastStepTime >= 100 && robot.ArmMotor.getCurrentPosition() > armMotorCollect - 5) {
                lastStepTime = System.currentTimeMillis();
                robot.ArmMotor.setTargetPosition(robot.ArmMotor.getCurrentPosition() - 1);
                robot.ArmMotor.setPower(armPower);
            }
        }

    }
}

    @Override
    public void buttonDown(Gamepad gamepad, String button) {
        if (engine.gamepad2 == gamepad) {
            if (button.equals("dpad_left")) {
                robot.collectorRight.setPower(-1);
                robot.collectorLeft.setPower(-1);
            } else if (button.equals("dpad_right")) {
                robot.collectorLeft.setPower(1);
                robot.collectorRight.setPower(1);
            }
        }

    }
    public void buttonUp(Gamepad gamepad, String button) {
        if (engine.gamepad2 == gamepad) {
            if (button.equals("dpad_left")) {
                robot.collectorRight.setPower(0);
                robot.collectorLeft.setPower(0);
            } else if (button.equals("dpad_right")) {
                robot.collectorLeft.setPower(0);
                robot.collectorRight.setPower(0);
            }
        }

    }
}