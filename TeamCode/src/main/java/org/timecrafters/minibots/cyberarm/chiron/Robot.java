package org.timecrafters.minibots.cyberarm.chiron;

import static org.timecrafters.minibots.cyberarm.chiron.Robot.Status;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Action;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Variable;
import org.timecrafters.minibots.cyberarm.chiron.tasks.FieldLocalizer;

public class Robot {
    public final DcMotorEx backLeftDrive, frontRightDrive, frontLeftDrive, backRightDrive, arm;
    public final ServoImplEx gripper, wrist;
    public final IMU imu;
    public final ColorSensor indicatorA, indicatorB;

    public final double imuAngleOffset;
    public boolean wristManuallyControlled = false;
    public boolean automaticAntiTipActive = false;
    public boolean hardwareFault = false;

    public Status status = Status.OKAY;

    public enum ArmPosition {
        COLLECT,
        GROUND,
        LOW,
        MEDIUM,
        HIGH

    }

    public enum Status {
        OKAY,
        MONITORING,
        WARNING,
        DANGER
    }

    private final CyberarmEngine engine;
    private final TimeCraftersConfiguration configuration;
    private final FieldLocalizer fieldLocalizer;
    private final double radius, diameter;

    private final double wheelRadius, gearRatio;
    private final int ticksPerRevolution;

    private boolean LEDStatusToggle = false;
    private double lastLEDStatusAnimationTime = 0;

    public Robot(CyberarmEngine engine, TimeCraftersConfiguration configuration, FieldLocalizer fieldLocalizer) {
        this.engine = engine;
        this.configuration = configuration;
        this.fieldLocalizer = fieldLocalizer;

        radius = tuningConfig("field_localizer_robot_radius").value();
        diameter = radius * 2;

        imuAngleOffset = hardwareConfig("imu_angle_offset").value();

        wheelRadius = tuningConfig("wheel_radius").value();
        gearRatio = tuningConfig("wheel_gear_ratio").value();
        ticksPerRevolution = tuningConfig("wheel_ticks_per_revolution").value();


        // FIXME: Rename motors in configuration
        // Define hardware
        backLeftDrive = engine.hardwareMap.get(DcMotorEx.class, "left_drive");    // MOTOR PORT: ?
        frontRightDrive = engine.hardwareMap.get(DcMotorEx.class, "right_drive"); // MOTOR PORT: ?

        frontLeftDrive = engine.hardwareMap.get(DcMotorEx.class, "front_drive");  // MOTOR PORT: ?
        backRightDrive = engine.hardwareMap.get(DcMotorEx.class, "back_drive");   // MOTOR PORT: ?

        // FIXME: Rename lift_drive to arm in hardware config
        arm = engine.hardwareMap.get(DcMotorEx.class, "lift_drive");          // MOTOR PORT: ?

        gripper = engine.hardwareMap.get(ServoImplEx.class, "gripper");       // SERVO PORT: ?
        wrist = engine.hardwareMap.get(ServoImplEx.class, "wrist");           // SERVO PORT: ?

        indicatorA = engine.hardwareMap.colorSensor.get("indicator_A"); // I2C
        indicatorB = engine.hardwareMap.colorSensor.get("indicator_B"); // I2C

        imu = engine.hardwareMap.get(IMU.class, "imu");


        // Configure hardware
        //   MOTORS
        //      DIRECTION
        frontLeftDrive.setDirection(hardwareConfig("front_left_drive_direction_forward").value() ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        frontRightDrive.setDirection(hardwareConfig("front_right_drive_direction_forward").value() ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        backLeftDrive.setDirection(hardwareConfig("back_left_drive_direction_forward").value() ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        backRightDrive.setDirection(hardwareConfig("back_right_drive_direction_forward").value() ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        arm.setDirection(hardwareConfig("arm_direction_forward").value() ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        //      RUNMODE
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm.setTargetPosition(0);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //      ZERO POWER BEHAVIOR
        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //      MOTOR POWER
        arm.setVelocity(
                angleToTicks(tuningConfig("arm_velocity_in_degrees_per_second").value()));

        //   SERVOS (POSITIONAL)
        //      Gripper
        gripper.setDirection(hardwareConfig("gripper_direction_forward").value() ? Servo.Direction.FORWARD : Servo.Direction.REVERSE);
        gripper.setPosition(tuningConfig("gripper_initial_position").value());

        //      Wrist
        wrist.setDirection(hardwareConfig("wrist_direction_forward").value() ? Servo.Direction.FORWARD : Servo.Direction.REVERSE);
        wrist.setPosition(tuningConfig("wrist_initial_position").value());

        //   SENSORS
        //      COLOR SENSORS
        indicatorA.enableLed(false);
        indicatorB.enableLed(false);

        //      IMU
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                    RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));

        imu.initialize(parameters);

        // INITIALIZE AFTER EVERYTHING ELSE to prevent use before set crashes
        this.fieldLocalizer.setRobot(this);
        this.fieldLocalizer.standardSetup();
    }

    public void standardTelemetry() {
        engine.telemetry.addLine();

        // STATUS
        engine.telemetry.addLine("DATA");
        engine.telemetry.addData("      Hardware Fault", hardwareFault);
        engine.telemetry.addLine();

        // Motor Powers
        engine.telemetry.addLine("Motor Powers");
        engine.telemetry.addData("      Front Left Drive", frontLeftDrive.getPower());
        engine.telemetry.addData("      Front Right Drive", frontRightDrive.getPower());

        engine.telemetry.addData("      Back Left Drive", backLeftDrive.getPower());
        engine.telemetry.addData("      Back Right Drive", backRightDrive.getPower());

        engine.telemetry.addLine();

        engine.telemetry.addData("      Arm", arm.getPower());

        engine.telemetry.addLine();

        // Motor Positions
        engine.telemetry.addLine("Motor Positions");
        engine.telemetry.addData("      Front Left Drive", "%d (%8.2f in)", frontLeftDrive.getCurrentPosition(), ticksToUnit(DistanceUnit.INCH, frontLeftDrive.getCurrentPosition()));
        engine.telemetry.addData("      Front Right Drive", "%d (%8.2f in)", frontRightDrive.getCurrentPosition(), ticksToUnit(DistanceUnit.INCH, frontRightDrive.getCurrentPosition()));

        engine.telemetry.addData("      Back Left Drive", "%d (%8.2f in)", backLeftDrive.getCurrentPosition(), ticksToUnit(DistanceUnit.INCH, backLeftDrive.getCurrentPosition()));
        engine.telemetry.addData("      Back Right Drive", "%d (%8.2f in)", backRightDrive.getCurrentPosition(), ticksToUnit(DistanceUnit.INCH, backRightDrive.getCurrentPosition()));

        engine.telemetry.addLine();

        engine.telemetry.addData("      Arm", "%d (%8.2f degrees)", arm.getCurrentPosition(), ticksToAngle(arm.getCurrentPosition()));

        engine.telemetry.addLine();

        // Motor Currents
        engine.telemetry.addLine("Motor Currents (AMPS)");
        engine.telemetry.addData("      Front Left Drive", frontLeftDrive.getCurrent(CurrentUnit.AMPS));
        engine.telemetry.addData("      Front Right Drive", frontRightDrive.getCurrent(CurrentUnit.AMPS));

        engine.telemetry.addData("      Back Left Drive", backLeftDrive.getCurrent(CurrentUnit.AMPS));
        engine.telemetry.addData("      Back Right Drive", backRightDrive.getCurrent(CurrentUnit.AMPS));

        engine.telemetry.addLine();

        engine.telemetry.addData("      Arm", arm.getCurrent(CurrentUnit.AMPS));

        engine.telemetry.addLine();

        // Motor Directions
        engine.telemetry.addLine("Motor Directions");
        engine.telemetry.addData("      Front Left Drive", frontLeftDrive.getDirection());
        engine.telemetry.addData("      Front Right Drive", frontRightDrive.getDirection());

        engine.telemetry.addData("      Back Left Drive", backLeftDrive.getDirection());
        engine.telemetry.addData("      Back Right Drive", backRightDrive.getDirection());

        engine.telemetry.addLine();

        engine.telemetry.addData("      Arm", arm.getDirection());

        engine.telemetry.addLine();

        // Motor Target Positions
        engine.telemetry.addLine("Motor Target Positions");
        engine.telemetry.addData("      Front Left Drive", frontLeftDrive.getTargetPosition());
        engine.telemetry.addData("      Front Right Drive", frontRightDrive.getTargetPosition());

        engine.telemetry.addData("      Back Left Drive", backLeftDrive.getTargetPosition());
        engine.telemetry.addData("      Back Right Drive", backRightDrive.getTargetPosition());

        engine.telemetry.addLine();

        engine.telemetry.addData("      Arm", arm.getTargetPosition());

        // Motor Velocity
        engine.telemetry.addLine("Motor Velocity");
        engine.telemetry.addData("      Front Left Drive", frontLeftDrive.getVelocity());
        engine.telemetry.addData("      Front Right Drive", frontRightDrive.getVelocity());

        engine.telemetry.addData("      Back Left Drive", backLeftDrive.getVelocity());
        engine.telemetry.addData("      Back Right Drive", backRightDrive.getVelocity());

        engine.telemetry.addLine();

        engine.telemetry.addData("      Arm", arm.getVelocity());

        engine.telemetry.addLine();

        // Servos
        engine.telemetry.addLine("Servos");
        engine.telemetry.addData("      Gripper Direction", gripper.getDirection());
        engine.telemetry.addData("      Gripper Position", gripper.getPosition());
        engine.telemetry.addData("      Gripper Enabled", gripper.isPwmEnabled());
        engine.telemetry.addLine();
        engine.telemetry.addData("      Wrist Direction", wrist.getDirection());
        engine.telemetry.addData("      Wrist Position", wrist.getPosition());
        engine.telemetry.addData("      Wrist Enabled", wrist.isPwmEnabled());

        engine.telemetry.addLine();

        // Sensors / IMU
        engine.telemetry.addLine("IMU");
        engine.telemetry.addData("      Facing (Degrees)", facing());
        engine.telemetry.addData("      Heading (Radians)", heading());
        engine.telemetry.addData("      Turn Rate", turnRate());

        engine.telemetry.addLine();

        // Field Localizer
        engine.telemetry.addLine("Field Localizer");

        Vector2d pos = fieldLocalizer.position();
        Vector2d vel = fieldLocalizer.velocity();
        engine.telemetry.addData("      Position X", pos.getX());
        engine.telemetry.addData("      Position Y", pos.getY());
        engine.telemetry.addData("      Velocity X", vel.getX());
        engine.telemetry.addData("      Velocity Y", vel.getY());

        engine.telemetry.addLine();
    }

    public void update() {
        status = Status.OKAY;

        // TODO: Handle status priority; That is, store reported statuses and select the "worst" one as status

        automaticLEDStatus();
    }

    private void automaticLEDStatus() {
        switch (status) {
            case OKAY:
                indicatorA.enableLed(false);
                indicatorB.enableLed(false);
                break;

            case MONITORING:
                indicatorA.enableLed(true);
                indicatorB.enableLed(true);
                break;

            case WARNING:
                if (System.currentTimeMillis() - lastLEDStatusAnimationTime >= 500){
                    lastLEDStatusAnimationTime = System.currentTimeMillis();
                    LEDStatusToggle = !LEDStatusToggle;

                    indicatorA.enableLed(LEDStatusToggle);
                    indicatorA.enableLed(!LEDStatusToggle);
                }
                break;

            case DANGER:
                if (System.currentTimeMillis() - lastLEDStatusAnimationTime >= 200){
                    lastLEDStatusAnimationTime = System.currentTimeMillis();
                    LEDStatusToggle = !LEDStatusToggle;

                    indicatorA.enableLed(LEDStatusToggle);
                    indicatorA.enableLed(LEDStatusToggle);
                }
                break;
        }
    }

    public double getRadius() { return radius; }

    public double getDiameter() { return diameter; }

    public TimeCraftersConfiguration getConfiguration() { return configuration; }

    public FieldLocalizer getFieldLocalizer() { return fieldLocalizer; }

    // For: Drive Wheels
    public int unitToTicks(DistanceUnit unit, double distance) {
        double fI = (gearRatio * ticksPerRevolution) / (wheelRadius * 2 * Math.PI * (gearRatio * ticksPerRevolution) / (gearRatio * ticksPerRevolution));

        double inches = unit.toInches(unit.fromUnit(unit, distance)); // NOTE: UNTESTED

        double ticks = fI * inches;

        return (int)ticks; // NOTE: UNTESTED
    }

    // For: Drive Wheels
    public double ticksToUnit(DistanceUnit unit, int ticks) {
        // Convert to inches, then to unit.
        double inches = wheelRadius * 2 * Math.PI * ticks / (gearRatio * ticksPerRevolution);

        return unit.fromUnit(DistanceUnit.INCH, inches); // NOTE: UNTESTED
    }

    // For: Arm
    public int angleToTicks(double angle) {
        int ticksPerRevolution = tuningConfig("arm_ticks_per_revolution").value();

        double d = ticksPerRevolution / 360.0;

        // Casting to float so that the int version of Math.round is used.
        return Math.round((float)d * (float)angle);
    }

    // For: Arm
    public double ticksToAngle(int ticks) {
        int ticksPerRevolution = tuningConfig("arm_ticks_per_revolution").value();

        double oneDegree = 360.0 / ticksPerRevolution;

        return oneDegree * ticks;
    }

    public Variable hardwareConfig(String variableName) {
        Action hardwareConfiguration = configuration.action("Robot", "Hardware");

        for (Variable v : hardwareConfiguration.getVariables()) {
            if (variableName.trim().equals(v.name)) {
                return v;
            }
        }

        throw new RuntimeException("Failed to find variable with name: " + variableName + " in group: Robot, action: Hardware");
    }

    public Variable tuningConfig(String variableName) {
        Action action = configuration.action("Robot", "Tuning");

        for (Variable v : action.getVariables()) {
            if (variableName.trim().equals(v.name)) {
                return v;
            }
        }

        throw new RuntimeException("Failed to find variable with name: " + variableName + " in group: Robot, action: Tuning");
    }

    public double facing() {
        double imuDegrees = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

        return (imuDegrees + imuAngleOffset + 360.0) % 360.0;
    }

    public double heading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    public double turnRate() {
        return imu.getRobotAngularVelocity(AngleUnit.DEGREES).yRotationRate; // NOTE: UNTESTED
    }

    public boolean isBetween(double value, double min, double max) {
        return value >= min && value <= max;
    }
}
