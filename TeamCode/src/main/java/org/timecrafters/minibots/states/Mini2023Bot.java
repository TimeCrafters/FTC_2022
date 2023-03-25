package org.timecrafters.minibots.states;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;
import org.timecrafters.minibots.engines.Mini2023Engine;

public class Mini2023Bot {

    private final Mini2023Engine engine;
    public TimeCraftersConfiguration configuration;

        public DcMotor Opportunity, Spirit; //Don't ask. Just don't.
        public CRServo servoA, servoB, servoC; //Just be thankful the servos have 'normal' names.
        public IMU imu;
        public ModernRoboticsI2cRangeSensor hazcam;

    public Mini2023Bot(Mini2023Engine engine) {
        this.engine = engine;
        setupRobot();
    }

    private void setupRobot() {

        Spirit = engine.hardwareMap.get(DcMotor.class, "Left Wheel");
        Opportunity = engine.hardwareMap.get(DcMotor.class, "Right Wheel");

        servoA = engine.hardwareMap.get(CRServo.class, "Servo 1");
        servoB = engine.hardwareMap.get(CRServo.class, "Servo 2");
        servoC = engine.hardwareMap.get(CRServo.class, "Servo 3");

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        ));

        this.imu = engine.hardwareMap.get(IMU.class, "imu");
        imu.initialize(parameters);



//        configuration = new TimeCraftersConfiguration("2023 Mini");

    }
}
