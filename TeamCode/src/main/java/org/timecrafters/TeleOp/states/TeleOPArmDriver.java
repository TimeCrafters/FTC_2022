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
        Opportunity = 0;
        Endeavour = 10;


        gamepad2Checker = new GamepadChecker(engine, engine.gamepad2);
    }

    @Override
    public void exec() {
        if (robot.collectorDistance.getDistance(DistanceUnit.MM) < 275 && robot.collectorDistance.getDistance(DistanceUnit.MM) > 100) {
            Endeavour = 0;
            }

        if (robot.collectorDistance.getDistance(DistanceUnit.MM) < 550 && robot.collectorDistance.getDistance(DistanceUnit.MM) > 275) {
            Endeavour = 1;
        }

        }


    }