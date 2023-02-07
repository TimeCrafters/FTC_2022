package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.Autonomous.TeleOp.states.PhoenixBot1;

public class CollectorState extends CyberarmState {

    private final PhoenixBot1 robot;
    private final boolean stateDisabled;
    private boolean collecting;
    private long duration;
    private long BeginningofActionTime;


    public CollectorState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;

        this.duration = robot.configuration.variable(groupName, actionName, "Duration").value();
        this.collecting = robot.configuration.variable(groupName, actionName, "Collecting").value();

        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;

    }


    @Override
    public void telemetry() {
        engine.telemetry.addData("Collector Distance", robot.collectorDistance.getDistance(DistanceUnit.MM));
    }

    @Override
    public void start() {
        BeginningofActionTime = System.currentTimeMillis();
    }

    @Override
    public void exec() {
        if (stateDisabled){
            setHasFinished(true);
            return;
        }

        if (System.currentTimeMillis() - BeginningofActionTime < duration) {
            if (collecting) {
                robot.collectorRight.setPower(1);
                robot.collectorLeft.setPower(1);
            } else if (collecting != true){
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


