package org.timecrafters.Phoenix.Autonomous.Engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.Phoenix.PhoenixBot1;

@Autonomous(name = "Left Side", group = "A Phoenix", preselectTeleOp = "APhoenixTeleOP")
public class LeftStateAutoEngine extends CyberarmEngine {
    PhoenixBot1 robot;

    @Override
    public void loop() {
        super.loop();

        telemetry.addData("BlackBoard Input", blackboardGetString("parkPlace"));
    }

    @Override
    public void setup() {
        robot = new PhoenixBot1(this);
        robot.imu.resetYaw();

        setupFromConfig(
                robot.configuration,
                "org.timecrafters.Autonomous.States",
                robot,
                PhoenixBot1.class,
                "Left State Auto");

    }
}
