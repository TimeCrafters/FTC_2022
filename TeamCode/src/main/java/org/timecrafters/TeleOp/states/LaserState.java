package org.timecrafters.TeleOp.states;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.minibots.states.MecanumRobot;

public class LaserState extends CyberarmState {
    Rev2mDistanceSensor laser;
    RevTouchSensor Tack;
    RevTouchSensor Mag;
    ColorSensor Speck;

    MecanumRobot robot;

    public LaserState(MecanumRobot robot) {
        this.robot = robot;
    }

    public LaserState() {

    }

    @Override
    public void init() {
        laser = engine.hardwareMap.get(Rev2mDistanceSensor.class, "Zappy");
        Tack = engine.hardwareMap.get(RevTouchSensor.class, "Pokey");
        Mag = engine.hardwareMap.get(RevTouchSensor.class, "Electro");
        Speck = engine.hardwareMap.get(ColorSensor.class, "Chroma");
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("Laser Distance", laser.getDistance(DistanceUnit.CM));
        engine.telemetry.addData("The Feels", Tack.isPressed());
        engine.telemetry.addData("Eel", Mag.isPressed());
        engine.telemetry.addData("Pretty Colors", ", Red " + Speck.red() + ", Green " + Speck.green() + ", Blue " + Speck.blue());
    }

    @Override
    public void exec() {

        if (Tack.isPressed() & !Mag.isPressed()) {
            robot.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
        } else {
            robot.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
        }
    }
}