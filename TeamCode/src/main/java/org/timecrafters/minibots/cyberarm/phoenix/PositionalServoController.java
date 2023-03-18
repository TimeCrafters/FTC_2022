package org.timecrafters.minibots.cyberarm.phoenix;

import com.qualcomm.robotcore.hardware.Servo;

public class PositionalServoController {
    final private Servo servo;
    final private double targetDegreesPerSecond, servoDegreesPerSecond, servoRangeInDegrees;

    private double lastEstimatedPosition, estimatedPosition, workingPosition, targetPosition;
    private long lastUpdatedAt;
    private boolean travelling = false;

    public PositionalServoController(Servo servo, double targetDegreesPerSecond, double servoDegreesPerSecond, double servoRangeInDegrees) {
        this.servo = servo;
        this.targetDegreesPerSecond = targetDegreesPerSecond;
        this.servoDegreesPerSecond = servoDegreesPerSecond;
        this.servoRangeInDegrees = servoRangeInDegrees;

        this.workingPosition = servo.getPosition();
        this.estimatedPosition = workingPosition;
        this.lastEstimatedPosition = estimatedPosition;
        this.lastUpdatedAt = System.currentTimeMillis();
    }

    public Servo getServo() {
        return servo;
    }

    public void update() {
        double error = targetPosition - estimatedPosition;
        double delta = (System.currentTimeMillis() - lastUpdatedAt) / 1000.0;
        double estimatedTravel = servoDegreesPerSecond * delta;
        double targetTravel = targetDegreesPerSecond * delta;

        if (targetTravel < estimatedTravel) {
            estimatedTravel = targetTravel;
        }

        if (travelling) {
            this.lastEstimatedPosition = estimatedPosition;

            if (error < 0.0) {
                this.estimatedPosition -= estimatedTravel;
            } else {
                this.estimatedPosition += estimatedTravel;
            }
        }

        if (error < 0.0 - estimatedTravel) {
            workingPosition -= estimatedTravel;
            travelling = true;
        } else if (error > 0.0 + estimatedTravel) {
            workingPosition += estimatedTravel;
            travelling = true;
        } else {
            travelling = false;
        }

        servo.setPosition(workingPosition);
        this.lastUpdatedAt = System.currentTimeMillis();
    }

    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;
    }

    public double getEstimatedPosition() {
        return estimatedPosition;
    }

    public double getEstimatedAngle() {
        return estimatedPosition * servoRangeInDegrees;
    }

    private double lerp(double min, double max, double t)
    {
        return min + (max - min) * t;
    }
}
