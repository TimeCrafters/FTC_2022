package org.timecrafters.testing.engine;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.testing.states.PrototypeBot1;
import org.timecrafters.testing.states.PrototypeTeleOPState;

@TeleOp (name = "PrototypeTeleOP")

public class PrototypeTeleOP extends CyberarmEngine {

    PrototypeBot1 robot;

    @Override
    public void setup() {

        robot = new PrototypeBot1(this);

        addState(new PrototypeTeleOPState(robot));
    }
}
