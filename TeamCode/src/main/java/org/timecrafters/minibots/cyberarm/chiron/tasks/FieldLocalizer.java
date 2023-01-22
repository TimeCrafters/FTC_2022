package org.timecrafters.minibots.cyberarm.chiron.tasks;

import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.minibots.cyberarm.chiron.Robot;
import org.timecrafters.minibots.cyberarm.chiron.tasks.field_localizer.Field;
import org.timecrafters.minibots.cyberarm.chiron.tasks.field_localizer.Obstacle;

public class FieldLocalizer extends CyberarmState {
    private static double posX, posY;
    private Robot robot;
    private Field field;

    private double deltaFrontLeft, deltaFrontRight, deltaBackLeft, deltaBackRight;
    private double displacementFrontLeft, displacementFrontRight, displacementBackLeft, displacementBackRight, displacementAverage;
    private double devFrontLeft, devFrontRight, devBackLeft, devBackRight;
    private double lastFrontLeft, lastFrontRight, lastBackLeft, lastBackRight;

    private double deltaXRotation, deltaYRotation, deltaX, deltaY;
    private double lastX, lastY, theta;

    private final double twoSqrtTwo = 2.0 / Math.sqrt(2.0);

    private double wheelDisplacementPerEncoderTickInInches;

    // Use this constructor to preserve displacement between opmodes
    public FieldLocalizer() {
    }

    public FieldLocalizer(double posX, double posY) {
        FieldLocalizer.posX = posX;
        FieldLocalizer.posY = posY;
    }

    public void standardSetup() {
        field = new Field(robot);

        wheelDisplacementPerEncoderTickInInches = robot.ticksToUnit(DistanceUnit.INCH, 1);

        lastFrontLeft  = 0;
        lastFrontRight = 0;
        lastBackLeft   = 0;
        lastBackRight  = 0;

        deltaFrontLeft  = 0;
        deltaFrontRight = 0;
        deltaBackLeft   = 0;
        deltaBackRight  = 0;

        displacementFrontLeft  = 0;
        displacementFrontRight = 0;
        displacementBackLeft   = 0;
        displacementBackRight  = 0;

        displacementAverage = 0;

        devFrontLeft  = 0;
        devFrontRight = 0;
        devBackLeft   = 0;
        devBackRight  = 0;

        deltaX = 0;
        deltaY = 0;
        deltaXRotation = 0;
        deltaYRotation = 0;

        // Preserve field position, if argument-less constructor is used
        lastX = FieldLocalizer.posX;
        lastY = FieldLocalizer.posY;

        // TODO: Preserve robot IMU offset between opmodes
        theta = robot.heading();
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    // FIXME: Return position in INCHES
    public Vector2d position() {
        return new Vector2d(posX, posY);
    }

    // FIXME: Return velocity in INCHES
    public Vector2d velocity() {
        return new Vector2d(deltaX, deltaY);
    }

    /**
     * Calculates direction vector to avoid collision with static field elements
     * @return Vector2d if collision projected or null of no correction needed
     */
    public Vector2d collisionAvoidanceDirection() {
        for (Obstacle o : field.getObstacles()) {
            // TODO: Calculate if robot will collide with obstacle
        }

        return null;
    }

    @Override
    public void exec() {
        // REF: https://www.bridgefusion.com/blog/2019/4/10/robot-localization-dead-reckoning-in-first-tech-challenge-ftc

        deltaFrontLeft  = robot.frontLeftDrive.getCurrentPosition() - lastFrontLeft;
        deltaFrontRight = robot.frontRightDrive.getCurrentPosition() - lastFrontRight;
        deltaBackLeft   = robot.backLeftDrive.getCurrentPosition() - lastBackLeft;
        deltaBackRight  = robot.backRightDrive.getCurrentPosition() - lastBackRight;

        displacementFrontLeft  = deltaFrontLeft * wheelDisplacementPerEncoderTickInInches;
        displacementFrontRight = deltaFrontRight * wheelDisplacementPerEncoderTickInInches;
        displacementBackLeft   = deltaBackLeft * wheelDisplacementPerEncoderTickInInches;
        displacementBackRight  = deltaBackRight * wheelDisplacementPerEncoderTickInInches;

        displacementAverage = (displacementFrontLeft + displacementFrontRight +
                displacementBackLeft + displacementBackRight) / 4.0; // motor count

        devFrontLeft  = displacementFrontLeft - displacementAverage;
        devFrontRight = displacementFrontRight - displacementAverage;
        devBackLeft   = displacementBackLeft - displacementAverage;
        devBackRight  = displacementBackRight - displacementAverage;

        deltaXRotation = (devFrontLeft + devFrontRight - devBackRight - devBackLeft) / twoSqrtTwo;
        deltaYRotation = (devFrontLeft - devFrontRight - devBackRight - devBackLeft) / twoSqrtTwo;

        theta = robot.heading();
        double sinTheta = Math.sin(theta);
        double cosineTheta = Math.cos(theta);

        deltaX = deltaXRotation * cosineTheta - deltaYRotation * sinTheta;
        deltaY = deltaYRotation * cosineTheta + deltaXRotation * sinTheta;

        posX += lastX + deltaX;
        posY += lastY + deltaY;

        lastFrontLeft  = robot.frontLeftDrive.getCurrentPosition();
        lastFrontRight = robot.frontRightDrive.getCurrentPosition();
        lastBackLeft   = robot.backLeftDrive.getCurrentPosition();
        lastBackRight  = robot.backRightDrive.getCurrentPosition();
    }
}
