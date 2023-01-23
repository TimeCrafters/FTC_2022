package org.timecrafters.minibots;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.engines.Common;

@Disabled
@TeleOp (name = "light test")

public class Engine extends CyberarmEngine {

    Common robot;

    @Override
    public void setup() {

        robot = new Common(this);

        addState(new State(robot));



    }
}
