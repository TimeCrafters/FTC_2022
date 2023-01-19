package org.timecrafters.minibots.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.timecrafters.minibots.pickle_minibot_general;
import org.timecrafters.minibots.states.pickle_teleop_state;

@TeleOp (name = "pickle_minibot teleop", group = "minibot")
public class pickle_engine extends CyberarmEngine {
    pickle_minibot_general robot;

    @Override
    public void setup (){
        robot = new pickle_minibot_general(this);
        addState(new pickle_teleop_state(robot));
    }

}
