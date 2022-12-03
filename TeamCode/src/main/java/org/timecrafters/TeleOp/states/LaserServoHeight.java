package org.timecrafters.TeleOp.states;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class LaserServoHeight extends CyberarmState {
    Servo leftUpLift, leftDownLift, rightUpLift, rightDownLift;
    Rev2mDistanceSensor CollectorHeight;
    double TargetHeight, Delay, LastChecked;
    @Override
    public void init() {
        leftUpLift = engine.hardwareMap.servo.get("LU-Lift");
        rightUpLift = engine.hardwareMap.servo.get("RU-Lift");
        CollectorHeight = engine.hardwareMap.get(Rev2mDistanceSensor.class, "CollectorHeight");
        TargetHeight = 380;
        Delay = 150;
        LastChecked = -1;
        leftUpLift.setDirection(Servo.Direction.REVERSE);
        rightUpLift.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public void exec() {
        if(runTime() - LastChecked >= Delay) {
            LastChecked = runTime();

            if (CollectorHeight.getDistance(DistanceUnit.MM) < TargetHeight) {
                leftUpLift.setPosition(leftUpLift.getPosition()+0.05);
                rightUpLift.setPosition(rightUpLift.getPosition()+0.05);
            }
        }
    }
}
