package org.timecrafters.minibots.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.states.Mini2023Bot;
import org.timecrafters.minibots.states.Mini2023State;

@TeleOp (name= "2023Mini")

public class Mini2023Engine extends CyberarmEngine {

    Mini2023Bot robot;

    @Override
    public void setup() {

        robot = new Mini2023Bot(this);
        addState(new Mini2023State(robot));

    }
}
