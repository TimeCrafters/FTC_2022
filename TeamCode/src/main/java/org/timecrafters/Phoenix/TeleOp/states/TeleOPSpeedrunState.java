package org.timecrafters.Phoenix.TeleOp.states;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.Phoenix.PhoenixBot1;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;

public class TeleOPSpeedrunState extends CyberarmState {
    PhoenixBot1 robot;
    double power;
    int distance;

    public TeleOPSpeedrunState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        TimeCraftersConfiguration configuration = new TimeCraftersConfiguration();

        this.power = configuration.variable(groupName, actionName, "Power").value();
        this.distance = configuration.variable(groupName, actionName, "Distance").value();
    }

    @Override
    public void start() {
        this.robot.backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.robot.backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void exec() {
        robot.backLeftDrive.setPower(power);
        robot.backRightDrive.setPower(power);
        if (Math.abs(robot.backLeftDrive.getCurrentPosition()) > Math.abs(distance)) {
            power = 0;
            robot.backLeftDrive.setPower(power);
            robot.backRightDrive.setPower(power);
            setHasFinished(true);
        }
    }
}
