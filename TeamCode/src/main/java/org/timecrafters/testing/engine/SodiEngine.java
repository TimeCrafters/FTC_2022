package org.timecrafters.testing.engine;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.testing.states.LaserState;
import org.timecrafters.testing.states.SodiLEDState;
import org.timecrafters.testing.states.SodiState;

@TeleOp(name = "Wheel")
public class SodiEngine extends CyberarmEngine {
    @Override
    public void setup() {
//        addState(new SodiState());
//        addState(new SodiLEDState());
        addState(new LaserState());
    }
}
