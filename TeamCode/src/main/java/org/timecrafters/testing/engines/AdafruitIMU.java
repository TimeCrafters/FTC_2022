package org.timecrafters.testing.engines;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.testing.states.AdafruitIMUState;
@Disabled
@TeleOp (name = "Adafruit IMU")
public class AdafruitIMU extends CyberarmEngine {
    @Override
    public void setup() {
        addState(new AdafruitIMUState());
    }
}
