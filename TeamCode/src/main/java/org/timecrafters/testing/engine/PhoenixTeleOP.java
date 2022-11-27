package org.timecrafters.testing.engine;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.testing.states.PhoenixBot1;
import org.timecrafters.testing.states.PhoenixTeleOPState;

@TeleOp (name = "APhoenixTeleOP")

public class PhoenixTeleOP extends CyberarmEngine {

    PhoenixBot1 robot;

    @Override
    public void setup() {

        robot = new PhoenixBot1(this);

        addState(new PhoenixTeleOPState(robot));
    }
}
