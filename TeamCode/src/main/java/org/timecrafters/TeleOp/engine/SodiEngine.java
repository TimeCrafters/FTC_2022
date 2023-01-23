package org.timecrafters.TeleOp.engine;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.TeleOp.states.LaserState;

@Disabled
@TeleOp(name = "Wheel")
public class SodiEngine extends CyberarmEngine {
    @Override
    public void setup() {
//        addState(new SodiState());
//        addState(new SodiLEDState());
        addState(new LaserState());
    }
}
