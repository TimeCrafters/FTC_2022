package org.timecrafters.TeleOp.engine;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.TeleOp.states.PhoenixBot1;
import org.timecrafters.TeleOp.states.PhoenixTeleOPState;
import org.timecrafters.TeleOp.states.TeleOPArmDriver;
import org.timecrafters.TeleOp.states.TeleOPTankDriver;

@TeleOp (name = "APhoenixTeleOP")

public class PhoenixTeleOP extends CyberarmEngine {

    PhoenixBot1 robot;

    @Override
    public void setup() {

        robot = new PhoenixBot1(this);
        addState(new TeleOPArmDriver(robot));
        addParallelStateToLastState(new TeleOPTankDriver(robot));
    }
}
