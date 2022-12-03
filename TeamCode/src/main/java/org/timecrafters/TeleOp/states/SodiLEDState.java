package org.timecrafters.TeleOp.states;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.cyberarm.engine.V2.CyberarmState;

public class SodiLEDState extends CyberarmState {
    
    RevBlinkinLedDriver LEDs;

    @Override
    public void init() {
        LEDs = engine.hardwareMap.get(RevBlinkinLedDriver.class, "lights");
        LEDs.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
    }

    @Override
    public void exec() {

        if (engine.gamepad1.a) {

            LEDs.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);

        }
        else if (engine.gamepad1.dpad_up) {

            LEDs.setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_LIGHT_CHASE);

        }
        if (engine.gamepad1.x) {

            LEDs.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);

        }
        else if (engine.gamepad1.dpad_up) {

            LEDs.setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_LIGHT_CHASE);

        }
        if (engine.gamepad1.y) {

            LEDs.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);

        }
        else if (engine.gamepad1.dpad_up) {

            LEDs.setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_LIGHT_CHASE);

        }
        if (engine.gamepad1.b) {

            LEDs.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);

        }
        else if (engine.gamepad1.dpad_up) {

            LEDs.setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_LIGHT_CHASE);

        }
    }
}
