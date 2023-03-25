package org.timecrafters.minibots.states;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;

public class Mini2023State extends CyberarmState {
    private final Mini2023Bot robot;

    public double lThrust, rThrust, orbitSpeed;

    public double deltaServo;

    public Mini2023State(Mini2023Bot robot) {this.robot = robot;}

    public double getDeltaServo() {

        return deltaServo;
    }

    @Override public void telemetry() {
        engine.telemetry.addData("Right Drive Power", robot.Opportunity.getPower());
        engine.telemetry.addData("Left Drive Power", robot.Spirit.getPower());
}

@Override
public void init() {
    lThrust = 0;
    rThrust = 0;
    orbitSpeed = 0;
    robot.servoA.setPower(orbitSpeed);
    robot.servoB.setPower(orbitSpeed);
    robot.servoC.setPower(orbitSpeed);
    robot.Spirit.setPower(lThrust);
    robot.Opportunity.setPower(rThrust);
}

@Override
public void exec() {

    if (Math.abs(engine.gamepad1.left_stick_y) > 0.1) {
        lThrust = engine.gamepad1.left_stick_y * 0.5;
        robot.Spirit.setPower(lThrust);
    } else {
        lThrust = 0;
        robot.Spirit.setPower(lThrust);
    }

    if (Math.abs(engine.gamepad1.right_stick_y) > 0.1) {
        rThrust = engine.gamepad1.right_stick_y * 0.5;
        robot.Opportunity.setPower(rThrust);
    } else {
        rThrust = 0;
        robot.Opportunity.setPower(rThrust);
    }

    if (engine.gamepad1.right_trigger > 0.1) {
        orbitSpeed = engine.gamepad1.right_trigger * 0.5;
        robot.servoA.setPower(orbitSpeed);
        robot.servoB.setPower(orbitSpeed);
        robot.servoC.setPower(orbitSpeed);
    } else if (engine.gamepad1.left_trigger > 0.1) {
        orbitSpeed = engine.gamepad1.left_trigger * 0.5;
        robot.servoA.setPower(orbitSpeed);
        robot.servoB.setPower(orbitSpeed);
        robot.servoC.setPower(orbitSpeed);
    } else {
        orbitSpeed = 0;
        robot.servoA.setPower(orbitSpeed);
        robot.servoB.setPower(orbitSpeed);
        robot.servoC.setPower(orbitSpeed);
    }



}
}
