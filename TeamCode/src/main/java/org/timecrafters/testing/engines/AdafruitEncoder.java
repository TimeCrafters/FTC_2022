package org.timecrafters.testing.engines;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.testing.states.AdafruitEncoderState;

@TeleOp(name = "Adafruit Encoder")
@Disabled
public class AdafruitEncoder extends CyberarmEngine {
    @Override
    public void setup() {
        addState(new AdafruitEncoderState());
    }
}
