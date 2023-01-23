package org.timecrafters.minibots.cyberarm.chiron.engines.autonomous_engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.timecrafters.minibots.cyberarm.chiron.engines.AutonomousEngine;

//@Autonomous(name = "CHIRON | RED Right Side", group = "CHIRON", preselectTeleOp = "CHIRON | TeleOp")
public class AutonomousRedRightSideEngine extends AutonomousEngine {
    @Override
    public void setup() {
        actionsGroupName = "AutonomousRedRightSide";
        tuningGroupName = "Autonomous";
        tuningActionName = "Tuning_Red_RightSide";

        super.setup();
    }
}
