package org.timecrafters.minibots.cyberarm.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.cyberarm.pickle_minibot;
import org.timecrafters.minibots.cyberarm.states.pickle_telescope_state;

@TeleOp (name = "pickle_minibot teleop", group = "minibot")
public class pickle_engine extends CyberarmEngine {
    pickle_minibot robot;

    @Override
    public void setup (){
        robot = new pickle_minibot(this);


        addState(new pickle_telescope_state(robot));
    }

}
