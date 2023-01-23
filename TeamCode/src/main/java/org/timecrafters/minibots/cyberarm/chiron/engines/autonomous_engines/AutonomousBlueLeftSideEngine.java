package org.timecrafters.minibots.cyberarm.chiron.engines.autonomous_engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.timecrafters.minibots.cyberarm.chiron.engines.AutonomousEngine;

//@Autonomous(name = "CHIRON | BLUE Left Side", group = "CHIRON", preselectTeleOp = "CHIRON | TeleOp")
public class AutonomousBlueLeftSideEngine extends AutonomousEngine {
    @Override
    public void setup() {
        actionsGroupName = "AutonomousBlueLeftSide";
        tuningGroupName = "Autonomous";
        tuningActionName = "Tuning_Blue_LeftSide";

        super.setup();
    }
}
