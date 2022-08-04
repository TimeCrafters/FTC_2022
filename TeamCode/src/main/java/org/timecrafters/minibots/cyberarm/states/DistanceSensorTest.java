package org.timecrafters.minibots.cyberarm.states;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class DistanceSensorTest extends CyberarmState {

    private final MecanumRobot robot;
    public String ballAmount;
    public DistanceSensorTest(MecanumRobot robot) {
        this.robot = robot;
    }

    @Override
    public void exec() {

        if (robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 5.0
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 5.1
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 5.2
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 5.3
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 5.4
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 5.5
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 5.6
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 5.7
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 5.8
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 5.9
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 6.0
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 6.1
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 6.2
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 6.3
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 6.4
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 6.5
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 6.6
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 6.7
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 6.8
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 6.9
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 7.0
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 7.1
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 7.2
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 7.3
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 7.4
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 7.5) {

            ballAmount = "3 balls";

        }


        if (robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 10.0
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 10.1
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 10.2
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 10.3
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 10.4
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 10.5
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 10.6
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 10.7
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 10.8
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 10.9
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 11.0
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 11.1
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 11.2
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 11.3
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 11.4
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 11.5
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 11.6
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 11.7
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 11.8
                || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 11.9) {

            ballAmount = "2 balls";

        }


        if (robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 14.0
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 14.1
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 14.2
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 14.3
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 14.4
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 14.5
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 14.6
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 14.7
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 14.8
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 14.9
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 15.0
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 15.1
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 15.2
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 15.3
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 15.4
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 15.5
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 15.6
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 15.7
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 15.8
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 15.9
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 16.0
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 16.1
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 16.2
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 16.3
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 16.4
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 16.5
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 16.6
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 16.7
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 16.8
        || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 16.9) {

            ballAmount = "1 ball";

        }


            if (robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 19.0
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 19.1
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 19.2
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 19.3
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 19.4
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 19.5
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 19.6
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 19.7
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 19.8
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 19.9
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 20.0
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 20.1
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 20.2
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 20.3
                    || robot.ballPositionSensor.getDistance(DistanceUnit.CM) == 20.4){

            ballAmount = "no balls";

        }
        }

    @Override
    public void telemetry() {

        engine.telemetry.addData("distance", robot.ballPositionSensor.getDistance(DistanceUnit.CM));
        engine.telemetry.addData("Amount", ballAmount);

    }
}
