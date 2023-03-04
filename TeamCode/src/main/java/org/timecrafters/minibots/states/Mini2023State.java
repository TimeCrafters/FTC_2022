package org.timecrafters.minibots.states;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.engines.Mini2023Engine;

public class Mini2023State extends CyberarmState {
    private final Mini2023Bot robot;

    public double driveSpeed, orbitSpeed;

    public Mini2023State(Mini2023Bot robot) {this.robot = robot;}

@Override
public void init() {
    driveSpeed = 0;
    orbitSpeed = 0;
    robot.servoA.setPower(0);
    robot.servoB.setPower(0);
    robot.servoC.setPower(0);
    robot.Opportunity.setPower(driveSpeed);
    robot.Spirit.setPower(driveSpeed);

}

@Override
public void exec() {

    if (Math.abs(engine.gamepad1.left_stick_y) > 0.1 && Math.abs(engine.gamepad1.left_stick_x) < 0.1) {
        orbitSpeed = engine.gamepad2.left_stick_y;
        robot.Opportunity.setPower(driveSpeed);
        robot.Spirit.setPower(driveSpeed);
    }

    if (Math.abs(engine.gamepad1.right_stick_x) > 0.1) {
        orbitSpeed = engine.gamepad1.right_stick_x * 0.75;
        robot.servoA.setPower(orbitSpeed);
        robot.servoB.setPower(orbitSpeed);
        robot.servoC.setPower(orbitSpeed);
    } else {
        orbitSpeed = 0;
    }



}
}
