package org.timecrafters.minibots.cyberarm.chiron.engines.autonomous_engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.timecrafters.minibots.cyberarm.chiron.engines.AutonomousEngine;

@Autonomous(name = "CHIRON | RED Left Side", group = "CHIRON", preselectTeleOp = "CHIRON | TeleOp")
public class AutonomousRedLeftSideEngine extends AutonomousEngine {
    @Override
    public void setup() {
       actionsGroupName = "AutonomousLeftSide";
        tuningGroupName = "Autonomous";
        tuningActionName = "Tuning_Red_LeftSide";

        super.setup();
    }
}
