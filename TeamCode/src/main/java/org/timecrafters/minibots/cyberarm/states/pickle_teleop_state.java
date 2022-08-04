package org.timecrafters.minibots.cyberarm.states;

import org.cyberarm.engine.V2.CyberarmState;
import org.timecrafters.minibots.cyberarm.pickle_minibot_general;

public class pickle_teleop_state extends CyberarmState {
    private final pickle_minibot_general robot;
    public pickle_teleop_state(pickle_minibot_general robot){this.robot = robot;}

    @Override
    public void exec() {
        double dDrivePowerY, dDrivePowerX, dRotatePowerX, dRotatePowerY;
        dDrivePowerY =  engine.gamepad1.left_stick_y;
        dDrivePowerX = engine.gamepad1.left_stick_x;
        dRotatePowerY =  engine.gamepad1.right_stick_y;
        dRotatePowerX = engine.gamepad1.right_stick_x;
        if (dDrivePowerY > .1 || dDrivePowerY < -.1 || dDrivePowerX > .1 || dDrivePowerX < -.1) {
            robot.pLeftFront.setPower(dDrivePowerY + dDrivePowerX);
            robot.pRightFront.setPower(dDrivePowerY - dDrivePowerX);
            robot.pRightBack.setPower(dDrivePowerY + dDrivePowerX);
            robot.pLeftBack.setPower(dDrivePowerY - dDrivePowerX);
        }
        else if ( dRotatePowerY > .1 || dRotatePowerY < -.1 || dRotatePowerX > .1 || dRotatePowerX < -.1){
            robot.pLeftFront.setPower(dRotatePowerY + dRotatePowerX);
            robot.pRightFront.setPower(-dRotatePowerY - dRotatePowerX);
            robot.pRightBack.setPower(-dRotatePowerY - dRotatePowerX);
            robot.pLeftBack.setPower(dRotatePowerY + dRotatePowerX);
        }
        else {
            robot.pLeftFront.setPower(0);
            robot.pRightFront.setPower(0);
            robot.pRightBack.setPower(0);
            robot.pLeftBack.setPower(0);
        }

       if (engine.gamepad1.dpad_up){
           robot.pServoArch.setPower(1);

       }
       else if (engine.gamepad1.dpad_down){
           robot.pServoArch.setPower(-1);

       }
       else {
           robot.pServoArch.setPower(0);
       }
       if (engine.gamepad1.left_bumper){
           robot.pServoCarousel.setPower(1);
       }
       else if (engine.gamepad1.right_bumper) {
           robot.pServoCarousel.setPower(-1);
       }
        else {
           robot.pServoCarousel.setPower(0);
        }
        if (engine.gamepad1.dpad_left){
            robot.pServoElevate.setPower(1);

        }else if (engine.gamepad1.dpad_right){
            robot.pServoElevate.setPower(-1);

        } else {
            robot.pServoElevate.setPower(0);
        }
        if (engine.gamepad1.left_trigger>.1){
            robot.pServoRotate.setPosition(.7);// full down
        }
        else if (engine.gamepad1.right_trigger>.1){
            robot.pServoRotate.setPosition(0);//full up

        }if (engine.gamepad1.a){
            robot.pServoGrab.setPosition(1);

        }else if (engine.gamepad1.b){
            robot.pServoGrab.setPosition(0);
        }
    }

    @Override
    public void exac() {

    }

}

