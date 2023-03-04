package org.timecrafters.minibots.states;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;
import org.timecrafters.minibots.engines.Mini2023Engine;

public class Mini2023Bot {

    private final Mini2023Engine engine;
    public TimeCraftersConfiguration configuration;

        public DcMotor Opportunity, Spirit, Ingenuity;
        public CRServo servoA, servoB, servoC;

    public Mini2023Bot(Mini2023Engine engine) {
        this.engine = engine;
        setupRobot();
    }

    private void setupRobot() {

        Opportunity = engine.hardwareMap.get(DcMotor.class, "Left Wheel");
        Spirit = engine.hardwareMap.get(DcMotor.class, "Right Wheel");

        servoA = engine.hardwareMap.get(CRServo.class, "Servo 1");
        servoB = engine.hardwareMap.get(CRServo.class, "Servo 2");
        servoC = engine.hardwareMap.get(CRServo.class, "Servo 3");



//        configuration = new TimeCraftersConfiguration("2023 Mini");

    }
}