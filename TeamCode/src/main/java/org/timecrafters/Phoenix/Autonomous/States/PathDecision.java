package org.timecrafters.Phoenix.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.Phoenix.PhoenixBot1;

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

        if (placement.equals("1")) {

            addState(new DriverStateWithOdometer(robot, "" + groupName + " Parking", "Park 1"));
        }

        else if (placement.equals("3")){
            addState(new DriverStateWithOdometer(robot, "" + groupName + " Parking", "Park 3"));

        }

        setHasFinished(true);
        }

    }






