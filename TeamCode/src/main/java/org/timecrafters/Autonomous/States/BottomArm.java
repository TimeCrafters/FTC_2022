package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.testing.states.PrototypeBot1;

public class BottomArm extends CyberarmState {

    PrototypeBot1 robot;
    double LowerRiserRightPos, LowerRiserLeftPos, AddedDistance;
    long time;
    long lastStepTime = 0;
    boolean up;

    public BottomArm(PrototypeBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.LowerRiserLeftPos = robot.configuration.variable(groupName, actionName, "LowerRiserLeftPos").value();
        this.LowerRiserRightPos = robot.configuration.variable(groupName, actionName, "LowerRiserRightPos").value();
        this.time = robot.configuration.variable(groupName, actionName, "time").value();
        this.AddedDistance = robot.configuration.variable(groupName, actionName, "AddedDistance").value();

    }

    @Override
    public void start() {
        up = robot.LowRiserLeft.getPosition() < LowerRiserLeftPos;
    }

    @Override
    public void exec() {

        if (System.currentTimeMillis() - lastStepTime >= time) {
            lastStepTime = System.currentTimeMillis();

            if (robot.LowRiserLeft.getPosition() < LowerRiserLeftPos && up) {

                robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() + AddedDistance);
                robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() + AddedDistance);

            } else if (robot.LowRiserLeft.getPosition() > LowerRiserLeftPos && !up) {

                robot.LowRiserLeft.setPosition(robot.LowRiserLeft.getPosition() - AddedDistance);
                robot.LowRiserRight.setPosition(robot.LowRiserRight.getPosition() - AddedDistance);

            } else {
                setHasFinished(true);
            }
        }
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("LowerRiserRightPos",LowerRiserRightPos);
        engine.telemetry.addData("LowerRiserLeftPos",LowerRiserLeftPos);
        engine.telemetry.addData("AddedDistance",AddedDistance);
        engine.telemetry.addData("left servo", robot.LowRiserLeft.getPosition());
        engine.telemetry.addData("right servo", robot.LowRiserRight.getPosition());
        engine.telemetry.addData("time",time);
    }
}
