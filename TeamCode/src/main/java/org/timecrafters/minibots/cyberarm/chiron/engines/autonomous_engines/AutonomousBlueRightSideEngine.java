package org.timecrafters.minibots.cyberarm.chiron.engines.autonomous_engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.timecrafters.minibots.cyberarm.chiron.engines.AutonomousEngine;

//@Autonomous(name = "CHIRON | BLUE Right Side", group = "CHIRON", preselectTeleOp = "CHIRON | TeleOp")
public class AutonomousBlueRightSideEngine extends AutonomousEngine {
    @Override
    public void setup() {
        actionsGroupName = "AutonomousRightSide";
        tuningGroupName = "Autonomous";
        tuningActionName = "Tuning_Blue_RightSide";

        super.setup();
    }
}
