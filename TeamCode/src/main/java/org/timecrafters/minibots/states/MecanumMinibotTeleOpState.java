package org.timecrafters.minibots.states;

import com.qualcomm.robotcore.hardware.Gamepad;


import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.MecanumMinibot;


public class MecanumMinibotTeleOpState extends CyberarmState {
    private final MecanumMinibot robot;
    private float speed;

    public MecanumMinibotTeleOpState(MecanumMinibot robot) {
        this.robot = robot;
    }

    @Override
    public void exec() {

        /* ............................................................................ drive */
        speed = 1.0f - engine.gamepad1.left_trigger;

        if (engine.gamepad1.y) {
            robot.driveAll(speed);
        } else if (engine.gamepad1.a) {
            robot.driveAll(-speed);
        } else if (engine.gamepad1.x) {
            robot.driveStrafe(MecanumMinibot.STRAFE_LEFT, speed);
        } else if (engine.gamepad1.b) {
            robot.driveStrafe(MecanumMinibot.STRAFE_RIGHT, speed);

        } else if (engine.gamepad1.left_bumper) {
            robot.driveTurn(MecanumMinibot.TURN_LEFT, speed);

        } else if (engine.gamepad1.right_bumper) {
            robot.driveTurn(MecanumMinibot.TURN_RIGHT, speed);
        } else {
            robot.driveStop();
        }
        /* ............................................................................ elevator */
        if(engine.gamepad1.dpad_up ||
                engine.gamepad2.y){
            robot.pServoElevate.setPower(1.0);
        }
        else if(engine.gamepad1.dpad_down ||
                engine.gamepad2.a){
            robot.pServoElevate.setPower(-1.0);
        }
        else{
            robot.pServoElevate.setPower((0.0));
        }

        /* ............................................................................ arch */
        if(Math.abs(engine.gamepad1.left_stick_y)>0.1 ||
                Math.abs(engine.gamepad2.left_stick_y)>0.1){
            robot.pServoArch.setPower(engine.gamepad1.left_stick_y + engine.gamepad2.left_stick_y);
        }
        else{
            robot.pServoArch.setPower((0.0));
        }

        /* ............................................................................ rotate */
        if(engine.gamepad1.dpad_right ||
                engine.gamepad2.dpad_right){ // up position
            robot.pServoRotate.setPosition(0.0);
        }
        if(engine.gamepad1.dpad_left ||
                engine.gamepad2.dpad_left){ // down position
            robot.pServoRotate.setPosition(0.70);
        }

        /* ............................................................................ grab */
        if(engine.gamepad1.left_stick_x < -0.5 || engine.gamepad1.right_stick_x < -0.5 ||
                engine.gamepad2.x){ // in
            robot.pServoGrab.setPosition(0.9);
        }
        if(engine.gamepad1.back ||
                engine.gamepad2.back){ // small out
            robot.pServoGrab.setPosition(0.50);
        }
        if(engine.gamepad1.left_stick_x > 0.5 || engine.gamepad1.right_stick_x > 0.5 ||
                engine.gamepad2.b){ // big out
            robot.pServoGrab.setPosition(0.0);
        }
        /* ............................................................................ carousel */
        if(Math.abs(engine.gamepad1.left_trigger)>0.1){
            robot.pServoCarousel.setPower(engine.gamepad1.left_trigger);
        }
        else if(Math.abs(engine.gamepad1.right_trigger)>0.1){
            robot.pServoCarousel.setPower(-engine.gamepad1.right_trigger);
        }
        else{
            robot.pServoCarousel.setPower((0.0));
        }
    }

    @Override
    public void buttonDown(Gamepad gamepad, String button) {
        super.buttonDown(gamepad, button);
    }

    @Override
    public void buttonUp(Gamepad gamepad, String button) {
        super.buttonUp(gamepad, button);
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("speed", speed);
    }
}
