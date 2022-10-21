package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.testing.states.PrototypeBot1;

public class LowerArm extends CyberarmState {

    PrototypeBot1 robot;
    double LowerRiserRightPos, LowerRiserLeftPos, AddedDistance;
    long time;
    long lastStepTime = 0;

    public LowerArm(PrototypeBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.LowerRiserLeftPos = robot.configuration.variable(groupName, actionName, "LowerRiserLeftPos").value();
        this.LowerRiserRightPos = robot.configuration.variable(groupName, actionName, "LowerRiserRightPos").value();
        this.time = robot.configuration.variable(groupName, actionName, "time").value();
        this.AddedDistance = robot.configuration.variable(groupName, actionName, "AddedDistance").value();

    }

    @Override
    public void exec() {
        if (robot.LowRiserLeft.getPosition() > LowerRiserLeftPos) {
            if (System.currentTimeMillis() - lastStepTime >= time) {
                lastStepTime = System.currentTimeMillis();
                robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - AddedDistance);
                robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - AddedDistance);
            }
        }
    }
}
