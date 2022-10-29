package org.timecrafters.Autonomous.States;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.testing.states.PrototypeBot1;

public class CollectorDistanceState extends CyberarmState {

    private final PrototypeBot1 robot;

    @Override
    public void start() {

        robot.frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.collectorLeft.setPower(1);
        robot.collectorRight.setPower(1);
    }



    @Override
    public void exec() {
    if (robot.collectorDistance.getDistance(DistanceUnit.MM) > 60) {
        robot.frontRightDrive.setPower(0.15);
        robot.frontLeftDrive.setPower(0.15);
        robot.backRightDrive.setPower(0.15);
        robot.backLeftDrive.setPower(0.15);
    } else {
        robot.frontRightDrive.setPower(0);
        robot.frontLeftDrive.setPower(0);
        robot.backRightDrive.setPower(0);
        robot.backLeftDrive.setPower(0);
        robot.collectorRight.setPower(0);
        robot.collectorLeft.setPower(0);
        setHasFinished(true);

    }

    }
    public CollectorDistanceState(PrototypeBot1 robot, String groupName, String actionName) {
        this.robot = robot;
}


}
