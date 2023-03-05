package org.timecrafters.Phoenix.Autonomous.States;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.Phoenix.PhoenixBot1;

public class TopArmv2 extends CyberarmState {
    private final PhoenixBot1 robot;
    private double targetPower;
    private int targetPosition;
    private int tolerance;


    public TopArmv2(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.targetPosition = robot.configuration.variable(groupName, actionName, "targetPosition").value();
        this.targetPower = robot.configuration.variable(groupName, actionName, "targetPower").value();
        this.tolerance = robot.configuration.variable(groupName, actionName, "tolerance").value();
    }

    @Override
    public void start() {
        robot.ArmMotor.setTargetPosition(targetPosition);
        robot.ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.ArmMotor.setPower(targetPower);

    }

    @Override
    public void exec() {

        if (robot.ArmMotor.getTargetPosition() == targetPosition) {
            setHasFinished(true);
        }
    }
}
