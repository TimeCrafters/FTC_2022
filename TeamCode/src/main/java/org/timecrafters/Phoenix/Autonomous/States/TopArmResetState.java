package org.timecrafters.Phoenix.Autonomous.States;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.Phoenix.PhoenixBot1;

public class TopArmResetState extends CyberarmState {
    private final PhoenixBot1 robot;
    private double targetPower;
    private int targetPosition;
    private int tolerance;
    private long lastMeasuredTime;
    private long pausingTime;


    public TopArmResetState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.targetPosition = robot.configuration.variable(groupName, actionName, "targetPosition").value();
        this.targetPower = robot.configuration.variable(groupName, actionName, "targetPower").value();
        this.tolerance = robot.configuration.variable(groupName, actionName, "tolerance").value();
        this.pausingTime = robot.configuration.variable(groupName, actionName, "pausingTime").value();
    }

    @Override
    public void start() {
        robot.ArmMotor.setTargetPosition(targetPosition);
        robot.ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.ArmMotor.setPower(targetPower);
        lastMeasuredTime = System.currentTimeMillis();

    }

    @Override
    public void exec() {

        if (System.currentTimeMillis() - lastMeasuredTime > pausingTime) {

            robot.ArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.ArmMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            robot.ArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.ArmMotor.setTargetPosition(0);
            robot.ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.ArmMotor.setPower(0.2);
            setHasFinished(true);
        }
    }
}
