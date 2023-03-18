package org.timecrafters.minibots.cyberarm.phoenix.states.teleop;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Action;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Variable;
import org.timecrafters.minibots.cyberarm.phoenix.Robot;

public class ArmController extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;
    private final Gamepad controller;
    private PIDController pidController;
    private double p = 0, i = 0, d = 0, f = 0;
    private final double ticksInDegree = 700 / 180;

    public ArmController(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        this.controller = engine.gamepad1;

        pidController = new PIDController(p, i, d);
    }

    @Override
    public void exec() {
        Action action = robot.getConfiguration().action("Robot", "Tuning_PIDF_Arm");

        for (Variable v : action.getVariables()) {
            switch (v.name.trim()) {
                case "proportional":
                    p = v.value();
                    break;
                case "integral":
                    i = v.value();
                    break;
                case "derivative":
                    d = v.value();
                    break;
                case "feed": // feedback
                    f = v.value();
                    break;
            }
        }

        pidController.setPID(p, i, d);
        int armPos = robot.arm.getCurrentPosition();
        double pid = pidController.calculate(armPos, robot.arm.getTargetPosition());
        double ff = Math.cos(Math.toRadians(robot.arm.getTargetPosition() / ticksInDegree)) * f;

        double power = pid + ff;

        robot.arm.setPower(power);
    }

    @Override
    public void buttonDown(Gamepad gamepad, String button) {
        if (gamepad != controller) {
            return;
        }

        switch (button) {
            case "guide":
                robot.reloadConfig();
                break;

            // Arm control
            case "a":
                robot.armPosition(Robot.ArmPosition.COLLECT);
                break;
            case "x":
                robot.armPosition(Robot.ArmPosition.LOW); // DISABLED GROUND JUNCTION
                break;
            case "b":
                robot.armPosition(Robot.ArmPosition.MEDIUM);
                break;
            case "y":
                robot.armPosition(Robot.ArmPosition.HIGH);
                break;
            // Manual stepping arm
            case "left_bumper":
                robot.arm.setTargetPosition(
                        robot.arm.getCurrentPosition() - robot.angleToTicks(5.0)
                );
                break;
            case "right_bumper":
                robot.arm.setTargetPosition(
                        robot.arm.getCurrentPosition() + robot.angleToTicks(5.0)
                );
                break;

            // Collector control
            case "dpad_up":
                robot.collectorLeftServo.setPower(1);
                robot.collectorRightServo.setPower(1);
                break;
            case "dpad_down":
                robot.collectorLeftServo.setPower(-1);
                robot.collectorRightServo.setPower(-1);
        }
    }

    @Override
    public void buttonUp(Gamepad gamepad, String button) {
        if (gamepad != controller) {
            return;
        }

        switch (button) {
            // Collector control
            case "dpad_up":
            case "dpad_down":
                robot.collectorLeftServo.setPower(0);
                robot.collectorRightServo.setPower(0);
                break;
        }
    }
}
