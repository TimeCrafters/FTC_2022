package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.Autonomous.TeleOp.states.PhoenixBot1;

public class TopArm extends CyberarmState {

    private final boolean stateDisabled;
    PhoenixBot1 robot;
    double UpperRiserRightPos, UpperRiserLeftPos, AddedDistance;
    long time;
    long lastStepTime = 0;
    boolean up;
    boolean directPosition;

    public TopArm(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.UpperRiserLeftPos = robot.configuration.variable(groupName, actionName, "UpperRiserLeftPos").value();
        this.UpperRiserRightPos = robot.configuration.variable(groupName, actionName, "UpperRiserRightPos").value();
        this.time = robot.configuration.variable(groupName, actionName, "time").value();
        this.AddedDistance = robot.configuration.variable(groupName, actionName, "AddedDistance").value();
        this.directPosition = robot.configuration.variable(groupName, actionName, "directPosition").value();

        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;

    }

    @Override
    public void start() {
        up = robot.ArmMotor.getCurrentPosition() < UpperRiserLeftPos;
    }

    @Override
    public void exec() {
        if (stateDisabled){
            setHasFinished(true);
            return;
        }

        if (directPosition) {
//            robot.HighRiserLeft.setPosition(UpperRiserLeftPos);
//            robot.HighRiserRight.setPosition(UpperRiserRightPos);

            if (runTime() >= time){
                setHasFinished(true);
            }
        } else {
            if (System.currentTimeMillis() - lastStepTime >= time) {
                lastStepTime = System.currentTimeMillis();

//                if (robot.HighRiserLeft.getPosition() < UpperRiserLeftPos && up) {
//
//                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() + AddedDistance);
//                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() + AddedDistance);
//
//                }
//                 else if (robot.HighRiserLeft.getPosition() > UpperRiserLeftPos && !up) {
//
//                    robot.HighRiserLeft.setPosition(robot.HighRiserLeft.getPosition() - AddedDistance);
//                    robot.HighRiserRight.setPosition(robot.HighRiserRight.getPosition() - AddedDistance);
//
//                } else {
//                    setHasFinished(true);
//                }
            }
        }
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("UpperRiserRightPos",UpperRiserRightPos);
        engine.telemetry.addData("UpperRiserLeftPos",UpperRiserLeftPos);
        engine.telemetry.addData("AddedDistance",AddedDistance);
        engine.telemetry.addData("time",time);



    }
}
