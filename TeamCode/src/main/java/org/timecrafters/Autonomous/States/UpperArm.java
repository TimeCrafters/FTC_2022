package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.testing.states.PrototypeBot1;

public class UpperArm extends CyberarmState {

    PrototypeBot1 robot;
    double UpperRiserRightPos, UpperRiserLeftPos, AddedDistance;
    long time;
    long lastStepTime = 0;

    public UpperArm(PrototypeBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.UpperRiserLeftPos = robot.configuration.variable(groupName, actionName, "LowerRiserLeftPos").value();
        this.UpperRiserRightPos = robot.configuration.variable(groupName, actionName, "LowerRiserRightPos").value();
        this.time = robot.configuration.variable(groupName, actionName, "time").value();
        this.AddedDistance = robot.configuration.variable(groupName, actionName, "AddedDistance").value();
    }

    @Override
    public void exec() {
        if (robot.HighRiserLeft.getPosition() > UpperRiserLeftPos) {
            if (System.currentTimeMillis() - lastStepTime >= time) {
                lastStepTime = System.currentTimeMillis();
                robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - AddedDistance);
                robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - AddedDistance);
            } else {
                setHasFinished(true);

            }
        }
    }
}
