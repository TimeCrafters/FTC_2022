package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.Autonomous.TeleOp.states.PhoenixBot1;

public class PathDecision extends CyberarmState {
    PhoenixBot1 robot;
    private String groupName;


    public PathDecision (PhoenixBot1 robot, String groupName, String actionName){
        this.robot = robot;
        this.groupName = groupName;

    }

    @Override
    public void exec() {
        String placement = engine.blackboardGetString("parkPlace");
        setHasFinished(true);
        }

    }






