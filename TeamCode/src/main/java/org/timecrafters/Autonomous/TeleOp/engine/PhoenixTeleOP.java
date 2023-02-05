package org.timecrafters.Autonomous.TeleOp.engine;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Autonomous.TeleOp.states.PhoenixBot1;
import org.timecrafters.Autonomous.TeleOp.states.TeleOPTankDriver;
import org.timecrafters.Autonomous.TeleOp.states.TeleOPArmDriver;

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
