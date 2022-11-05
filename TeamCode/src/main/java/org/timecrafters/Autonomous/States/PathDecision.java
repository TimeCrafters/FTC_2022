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

        if (placement != null) {
            if (placement.equals("2")) {
                engine.insertState(this, new DriverState(robot, groupName, "29-1"));
            } else if (placement.equals("3")) {
                engine.insertState(this, new DriverState(robot, groupName, "29-2"));
            }
        }
          else {
              engine.insertState(this, new DriverState(robot, groupName, "29-0"));
          }
        }

    }






