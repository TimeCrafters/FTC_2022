package org.timecrafters.minibots.cyberarm.chiron;

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

public class Robot {
    public final DcMotorEx leftDrive, rightDrive, frontDrive, backDrive, arm;
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

    public Robot(CyberarmEngine engine, TimeCraftersConfiguration configuration) {
        this.engine = engine;
        this.configuration = configuration;
        imuAngleOffset = hardwareConfig("imu_angle_offset").value();

        // Define hardware
        leftDrive = engine.hardwareMap.get(DcMotorEx.class, "left_drive");    // MOTOR PORT: ?
        rightDrive = engine.hardwareMap.get(DcMotorEx.class, "right_drive");  // MOTOR PORT: ?

        frontDrive = engine.hardwareMap.get(DcMotorEx.class, "front_drive");  // MOTOR PORT: ?
        backDrive = engine.hardwareMap.get(DcMotorEx.class, "back_drive");    // MOTOR PORT: ?

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
        leftDrive.setDirection(hardwareConfig("left_drive_direction_forward").value() ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        rightDrive.setDirection(hardwareConfig("right_drive_direction_forward").value() ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        frontDrive.setDirection(hardwareConfig("front_drive_direction_forward").value() ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        backDrive.setDirection(hardwareConfig("back_drive_direction_forward").value() ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        arm.setDirection(hardwareConfig("arm_drive_direction_forward").value() ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        //      RUNMODE
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); frontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); backDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm.setTargetPosition(0);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //      ZERO POWER BEHAVIOR
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //      MOTOR POWER
        arm.setPower(0.35);

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
                    RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));

        imu.initialize(parameters);
    }

    public void standardTelemetry() {
        engine.telemetry.addLine();

        // STATUS
        engine.telemetry.addLine("DATA");
        engine.telemetry.addData("      Hardware Fault", hardwareFault);
        engine.telemetry.addLine();

        // Motor Powers
        engine.telemetry.addLine("Motor Powers");
        engine.telemetry.addData("  Left Drive", leftDrive.getPower());
        engine.telemetry.addData("  Right Drive", rightDrive.getPower());

        engine.telemetry.addData("  Front Drive", frontDrive.getPower());
        engine.telemetry.addData("  Back Drive", backDrive.getPower());

        engine.telemetry.addLine();

        engine.telemetry.addData("  Arm", arm.getPower());

        engine.telemetry.addLine();

        // Motor Positions
        engine.telemetry.addLine("Motor Positions");
        engine.telemetry.addData("  Left Drive", "%d (%2f in)", leftDrive.getCurrentPosition(), ticksToUnit(DistanceUnit.INCH, leftDrive.getCurrentPosition()));
        engine.telemetry.addData("  Right Drive", "%d (%2f in)", rightDrive.getCurrentPosition(), ticksToUnit(DistanceUnit.INCH, rightDrive.getCurrentPosition()));

        engine.telemetry.addData("  Front Drive", "%d (%2f in)", frontDrive.getCurrentPosition(), ticksToUnit(DistanceUnit.INCH, frontDrive.getCurrentPosition()));
        engine.telemetry.addData("  Back Drive", "%d (%2f in)", backDrive.getCurrentPosition(), ticksToUnit(DistanceUnit.INCH, backDrive.getCurrentPosition()));

        engine.telemetry.addLine();

        engine.telemetry.addData("  Arm", "%d (%2f degrees)", arm.getCurrentPosition(), ticksToAngle(arm.getCurrentPosition()));

        engine.telemetry.addLine();

        // Motor Currents
        engine.telemetry.addLine("Motor Currents (AMPS)");
        engine.telemetry.addData("  Left Drive", leftDrive.getCurrent(CurrentUnit.AMPS));
        engine.telemetry.addData("  Right Drive", rightDrive.getCurrent(CurrentUnit.AMPS));

        engine.telemetry.addData("  Front Drive", frontDrive.getCurrent(CurrentUnit.AMPS));
        engine.telemetry.addData("  Back Drive", backDrive.getCurrent(CurrentUnit.AMPS));

        engine.telemetry.addLine();

        engine.telemetry.addData("  Arm", arm.getCurrent(CurrentUnit.AMPS));

        engine.telemetry.addLine();

        // Motor Directions
        engine.telemetry.addLine("Motor Directions");
        engine.telemetry.addData("      Left Drive", leftDrive.getDirection());
        engine.telemetry.addData("      Right Drive", rightDrive.getDirection());

        engine.telemetry.addData("      Front Drive", frontDrive.getDirection());
        engine.telemetry.addData("      Back Drive", backDrive.getDirection());

        engine.telemetry.addLine();

        engine.telemetry.addData("      Arm", arm.getDirection());

        engine.telemetry.addLine();

        // Motor Target Positions
        engine.telemetry.addLine("Motor Target Positions");
        engine.telemetry.addData("      Left Drive", leftDrive.getTargetPosition());
        engine.telemetry.addData("      Right Drive", rightDrive.getTargetPosition());

        engine.telemetry.addData("      Front Drive", frontDrive.getTargetPosition());
        engine.telemetry.addData("      Back Drive", backDrive.getTargetPosition());

        engine.telemetry.addLine();

        engine.telemetry.addData("      Arm", arm.getTargetPosition());

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
        engine.telemetry.addData("      Facing", facing());
        engine.telemetry.addData("      Turn Rate", turnRate());
    }

    public TimeCraftersConfiguration getConfiguration() {
        return configuration;
    }

    // For: Drive Wheels
    public int unitToTicks(DistanceUnit unit, double distance) {
        double inches = unit.toInches(unit.fromUnit(unit, distance)); // NOTE: UNTESTED

        // FIXME: This should be stored as a presudo constant at initialization
        double wheelRadius = tuningConfig("wheel_radius").value();
        double gearRatio = tuningConfig("wheel_gear_ratio").value();
        double ticksPerRevolution = tuningConfig("wheel_ticks_per_revolution").value();

        return 0;
    }

    // For: Drive Wheels
    public double ticksToUnit(DistanceUnit unit, int ticks) {
        return 0;
    }

    // For: Arm
    public int angleToTicks(double angle) {
        return 0;
    }

    // For: Arm
    public double ticksToAngle(int ticks) {
        return 0;
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

    // TODO: Convert to 360 degree range with +90 degrees being on the RIGHT
    public double facing() {
        // FIXME: Apply imuAngleOffset
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    public double turnRate() {
        return imu.getRobotAngularVelocity(AngleUnit.DEGREES).yRotationRate; // NOTE: UNTESTED
    }
}
