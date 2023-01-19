package org.timecrafters.minibots;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmEngine;

public class pickle_minibot_general {
    private CyberarmEngine engine;

    public DcMotor pLeftFront, pRightFront;
    public DcMotor pLeftBack, pRightBack;
    public CRServo pServoArch, pServoCarousel, pServoElevate;
    public Servo pServoRotate, pServoGrab;

    public pickle_minibot_general(CyberarmEngine engine){
        this.engine=engine;

        pLeftFront = engine.hardwareMap.dcMotor.get("frontLeft");
        pRightFront = engine.hardwareMap.dcMotor.get("frontRight");
        pLeftBack = engine.hardwareMap.dcMotor.get("backLeft");
        pRightBack = engine.hardwareMap.dcMotor.get("backRight");
        pServoArch = engine.hardwareMap.crservo.get("arch");//dpad up and down
        pServoCarousel = engine.hardwareMap.crservo.get("carousel");// bumper
        pServoElevate = engine.hardwareMap.crservo.get("elevate");// left and right dpad
        pServoRotate = engine.hardwareMap.servo.get("rotate"); // triggers >:(
        pServoGrab = engine.hardwareMap.servo.get("grab");
    }
}
