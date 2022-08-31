package org.timecrafters.minibots.cyberarm;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.engines.Common;

public class State extends CyberarmState {

    private final Common robot;

    public State (Common robot) { this.robot = robot; }

    @Override
    public void init() {
        robot.leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
    }

    @Override
    public void exec() {

        if (engine.gamepad1.a) {

            robot.leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);

        }
        else if (engine.gamepad1.dpad_up) {

            robot.leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_LIGHT_CHASE);

        }
        if (engine.gamepad1.x) {

            robot.leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);

        }
        else if (engine.gamepad1.dpad_up) {

            robot.leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_LIGHT_CHASE);

        }
        if (engine.gamepad1.y) {

            robot.leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);

        }
        else if (engine.gamepad1.dpad_up) {

        robot.leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_LIGHT_CHASE);

    }
        if (engine.gamepad1.b) {

            robot.leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);

        }
        else if (engine.gamepad1.dpad_up) {

            robot.leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_LIGHT_CHASE);

        }
    }
}
