package org.timecrafters.testing.states;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class LaserState extends CyberarmState {
    Rev2mDistanceSensor laser;
    RevTouchSensor Tack;
    RevTouchSensor Mag;
    ColorSensor Speck;


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
        engine.telemetry.addData("Pretty Colors", ", Red " + Speck.red() + ", Green " + Speck.green() + ", Blue " + Speck.blue()  );
    }

    @Override
    public void exec() {

    }
}
