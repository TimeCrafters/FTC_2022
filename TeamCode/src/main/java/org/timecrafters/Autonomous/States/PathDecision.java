package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.testing.states.PrototypeBot1;

public class PathDecision extends CyberarmState {
    PrototypeBot1 robot;
    private String groupName;


    public PathDecision (PrototypeBot1 robot, String groupName, String actionName){
        this.robot = robot;
        this.groupName = groupName;

    }

    @Override
    public void exec() {
        String placement = engine.blackboard.get("parkPlace");
        setHasFinished(true);
        }

    }






