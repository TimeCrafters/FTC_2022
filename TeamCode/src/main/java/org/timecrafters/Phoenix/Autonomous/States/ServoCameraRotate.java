package org.timecrafters.Phoenix.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.Phoenix.PhoenixBot1;

public class ServoCameraRotate extends CyberarmState {
    private final boolean stateDisabled;
    PhoenixBot1 robot;
    private double ServoPosition;

    public ServoCameraRotate(PhoenixBot1 robot, String groupName, String actionName) {
        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;
        this.robot = robot;
        this.ServoPosition = robot.configuration.variable(groupName, actionName, "ServoPosition").value();

    }

    @Override
    public void exec() {
        if (stateDisabled) {
            setHasFinished(true);
        } else {
            robot.CameraServo.setPosition(ServoPosition);
            setHasFinished(true);
    }
    }
}
