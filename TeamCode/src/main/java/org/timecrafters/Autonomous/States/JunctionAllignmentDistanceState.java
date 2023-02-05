package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.Autonomous.TeleOp.states.PhoenixBot1;

public class JunctionAllignmentDistanceState extends CyberarmState {
    private final boolean stateDisabled;
    PhoenixBot1 robot;
    private final double drivePower;
    private int loopsTotal;
    private float slop;
    private double checkTime = 250;
    private int traveledDistance;
    private int loopsCurrent;
    private double minDistance;
    private double maxDistance;
    private String whereAmI = "init";
    private int driveLoops;
    private boolean finishedEnabled;
    double leftDistance;
    double rightDistance;
    boolean rightInRange;
    boolean leftInRange;


    public JunctionAllignmentDistanceState(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        this.drivePower = robot.configuration.variable(groupName, actionName, "DrivePower").value();
        this.loopsTotal = robot.configuration.variable(groupName, actionName, "loopsTotal").value();
        this.slop = robot.configuration.variable(groupName, actionName, "slop").value();
        this.traveledDistance = robot.configuration.variable(groupName, actionName, "traveled distance").value();
        this.minDistance = robot.configuration.variable(groupName, actionName, "minDistance").value();
        this.maxDistance = robot.configuration.variable(groupName, actionName, "maxDistance").value();
        this.finishedEnabled = robot.configuration.variable(groupName, actionName, "finishedEnabled").value();


        this.stateDisabled = !robot.configuration.action(groupName, actionName).enabled;
    }

    @Override
    public void telemetry() {
        engine.telemetry.addData("right sensor distance", robot.rightPoleDistance.getDistance(DistanceUnit.MM));
        engine.telemetry.addData("left sensor distance", robot.leftPoleDistance.getDistance(DistanceUnit.MM));
        engine.telemetry.addData("front right power", robot.frontRightDrive.getPower());
        engine.telemetry.addData("front left power", robot.frontLeftDrive.getPower());
        engine.telemetry.addData("back left power", robot.backLeftDrive.getPower());
        engine.telemetry.addData("back right power", robot.backRightDrive.getPower());
        engine.telemetry.addData("drive power", drivePower);
        engine.telemetry.addData("traveled distance", traveledDistance);
        engine.telemetry.addData("Loops", loopsCurrent);
        engine.telemetry.addData("where am i??", whereAmI);
        engine.telemetry.addData("time", System.currentTimeMillis() - checkTime);


    }

    @Override
    public void start() {

//        robot.frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.OdometerEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//
//        robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        robot.backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        robot.backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        robot.OdometerEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        checkTime = System.currentTimeMillis() - 500;


        loopsCurrent = 0;

    }

    @Override
    public void exec() {


        if (stateDisabled) {
            setHasFinished(true);
        } else {

            if (loopsCurrent >= loopsTotal ) {

                whereAmI = "exceeded loops";

                robot.backLeftDrive.setPower(0);
                robot.backRightDrive.setPower(0);
                robot.frontLeftDrive.setPower(0);
                robot.frontRightDrive.setPower(0);
                setHasFinished(finishedEnabled);

            } else if (System.currentTimeMillis() - checkTime >= 100) {

                checkTime = System.currentTimeMillis();

                loopsCurrent += 1;

                leftDistance = robot.leftPoleDistance.getDistance(DistanceUnit.MM);
                rightDistance = robot.rightPoleDistance.getDistance(DistanceUnit.MM);
                rightInRange = (rightDistance >= minDistance) && (rightDistance <= maxDistance);
                leftInRange = (leftDistance >= minDistance) && (leftDistance <= maxDistance);

                if (rightInRange || leftInRange) {

                    whereAmI = "in range";

                    robot.backLeftDrive.setPower(0);
                    robot.backRightDrive.setPower(0);
                    robot.frontLeftDrive.setPower(0);
                    robot.frontRightDrive.setPower(0);
                    setHasFinished(finishedEnabled);

                }

                else if (!rightInRange && !leftInRange) {

                    whereAmI = "Out of range";

//                    if (Math.abs(robot.OdometerEncoder.getCurrentPosition()) < traveledDistance) {
                        robot.backLeftDrive.setPower(drivePower);
                        robot.backRightDrive.setPower(drivePower);
                        robot.frontLeftDrive.setPower(drivePower);
                        robot.frontRightDrive.setPower(drivePower);

//                    }

                }




            }
        }
    }
}
