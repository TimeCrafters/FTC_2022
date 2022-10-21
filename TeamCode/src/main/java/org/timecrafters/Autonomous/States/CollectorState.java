package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.testing.states.PrototypeBot1;

public class CollectorState extends CyberarmState {

    private final PrototypeBot1 robot;
    private boolean collecting;
    private long duration;
    private long BeginningofActionTime;


    public CollectorState(PrototypeBot1 robot, String groupName, String actionName) {
        this.robot = robot;

        this.duration = robot.configuration.variable(groupName, actionName, "Duration").value();
        this.collecting = robot.configuration.variable(groupName, actionName, "Collecting").value();
    }


    @Override
    public void telemetry() {
        engine.telemetry.addData("High Riser Right Position", robot.HighRiserRight.getPosition());
        engine.telemetry.addData("High Riser Left Position", robot.HighRiserLeft.getPosition());
    }

    @Override
    public void start() {
        BeginningofActionTime = System.currentTimeMillis();
    }

    @Override
    public void exec() {

        if (System.currentTimeMillis() - BeginningofActionTime < duration) {
            if (collecting) {
                robot.collectorRight.setPower(1);
                robot.collectorLeft.setPower(1);
            } else {
                robot.collectorRight.setPower(-1);
                robot.collectorLeft.setPower(-1);

            }

        } else {
            robot.collectorLeft.setPower(0);
            robot.collectorRight.setPower(0);
            setHasFinished(true);
        }



    }

    }


