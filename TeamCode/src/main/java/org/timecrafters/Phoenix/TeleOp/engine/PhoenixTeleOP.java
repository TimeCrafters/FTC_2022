package org.timecrafters.Phoenix.TeleOp.engine;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Phoenix.PhoenixBot1;
import org.timecrafters.Phoenix.TeleOp.states.TeleOPArmDriver;
import org.timecrafters.Phoenix.TeleOp.states.TeleOPTankDriver;

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
