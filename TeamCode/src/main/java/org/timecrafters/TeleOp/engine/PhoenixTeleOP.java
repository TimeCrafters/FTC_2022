package org.timecrafters.TeleOp.engine;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.TeleOp.states.PhoenixBot1;

@TeleOp (name = "APhoenixTeleOP")

public class PhoenixTeleOP extends CyberarmEngine {

    PhoenixBot1 robot;

    @Override
    public void setup() {

        robot = new PhoenixBot1(this);

//        addState(new PhoenixTeleOPState(robot));
    }
}
