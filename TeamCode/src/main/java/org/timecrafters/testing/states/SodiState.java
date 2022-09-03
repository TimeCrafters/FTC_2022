package org.timecrafters.testing.states;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmState;

public class SodiState extends CyberarmState {
    CRServo Wheel, Rack;
    DcMotor ForePort, ForeStar, HindPort, HindStar;
    Servo Zygo;
    @Override
    public void init() {
        super.init();

        Wheel=engine.hardwareMap.crservo.get("Wheel");
        Rack=engine.hardwareMap.crservo.get("Rack");
        ForePort=engine.hardwareMap.dcMotor.get("ForePort");
        ForeStar=engine.hardwareMap.dcMotor.get("ForeStar");
        HindPort=engine.hardwareMap.dcMotor.get("HindPort");
        HindStar=engine.hardwareMap.dcMotor.get("HindStar");
        Zygo=engine.hardwareMap.servo.get("Zygo");
    }

    @Override
    public void exec() {
        if (engine.gamepad1.x) {
            Wheel.setPower(1.0);
        } else if(engine.gamepad1.b) {
            Wheel.setPower(-1.0);
    } else {
        Wheel.setPower(0);
    }
    if (engine.gamepad1.y) {
        Rack.setPower(1.0);
    } else if(engine.gamepad1.a) {
        Rack.setPower(-1.0);
    } else {
        Rack.setPower(0);
    }
        ForePort.setPower(engine.gamepad1.left_stick_y * 1);
        HindPort.setPower(engine.gamepad1.left_stick_y * 1);
        ForeStar.setPower(engine.gamepad1.right_stick_y * 1);
        HindStar.setPower(engine.gamepad1.right_stick_y * 1);
        if (engine.gamepad1.left_bumper) {
            Zygo.setPosition(1.0);
        } else {
            Zygo.setPosition(0);
        }
    }
}
