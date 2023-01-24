package org.timecrafters.testing.states;

import org.cyberarm.drivers.EncoderAdafruitP4991;
import org.cyberarm.engine.V2.CyberarmState;

public class AdafruitEncoderState extends CyberarmState {
    EncoderAdafruitP4991 encoder;
    int position = 0;

    @Override
    public void init() {
        encoder = engine.hardwareMap.get(EncoderAdafruitP4991.class, "encoder");
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("Encoder Position",position);
    }

    @Override
    public void exec() {
        position =  encoder.getCurrentPosition();
    }
}
