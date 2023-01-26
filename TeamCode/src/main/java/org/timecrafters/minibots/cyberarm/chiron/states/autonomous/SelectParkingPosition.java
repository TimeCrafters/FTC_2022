package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

public class SelectParkingPosition extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private final double timeInMS;
    private final boolean stateDisabled;

    public SelectParkingPosition(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        timeInMS = robot.getConfiguration().variable(groupName, actionName, "timeInMS").value();

        stateDisabled = !robot.getConfiguration().action(groupName, actionName).enabled;
    }

    @Override
    public void start() {
        int pos = engine.blackboardGetInt("parking_position");

        engine.setupFromConfig(
                robot.getConfiguration(),
                "org.timecrafters.minibots.cyberarm.chiron.states.autonomous",
                robot,
                Robot.class,
                "" + groupName + "_Parking_" + pos);
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
}
