package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class Wrist extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private final double timeInMS;
    private final boolean stateDisabled, positionUp;

    public Wrist(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        timeInMS = robot.getConfiguration().variable(groupName, actionName, "timeInMS").value();
        positionUp = robot.getConfiguration().variable(groupName, actionName, "positionUp").value();

        stateDisabled = !robot.getConfiguration().action(groupName, actionName).enabled;
    }

    @Override
    public void start() {
        if (positionUp) {
            robot.wristPosition(Robot.WristPosition.UP);
        } else {
            robot.wristPosition(Robot.WristPosition.DOWN);
        }
    }

    @Override
    public void exec() {
        if (stateDisabled) {
            setHasFinished(true);

            return;
        }

        if (runTime() >= timeInMS) {
            stop();

            setHasFinished(true);

            return;
        }
    }

    @Override
    public void stop() {
        robot.wrist.setPower(0);
    }
}
