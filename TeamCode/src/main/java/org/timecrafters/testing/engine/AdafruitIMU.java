package org.timecrafters.testing.engine;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.testing.states.AdafruitIMUState;
@TeleOp (name = "Adafruit IMU")
public class AdafruitIMU extends CyberarmEngine {
    @Override
    public void setup() {
        addState(new AdafruitIMUState());
    }
}
