package org.timecrafters.minibots.cyberarm.phoenix;

import android.annotation.SuppressLint;
import android.util.Log;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Action;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Variable;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class Robot {
    private static final String TAG = "Phoenix | Robot";
    public final DcMotorEx backLeftDrive, frontRightDrive, frontLeftDrive, backRightDrive, arm;
    public final PositionalServoController leftRiserServoController, rightRiserServoController;
    public final Servo cameraServo;
    public final CRServo collectorLeftServo, collectorRightServo;
//    public final ServoImplEx gripper;
    public final IMU imu;
    public LynxModule expansionHub;

    public final double imuAngleOffset, initialFacing;
    public boolean armManuallyControlled = false;
    public boolean automaticAntiTipActive = false;
    public boolean hardwareFault = false;
    public String hardwareFaultMessage = "";

    private Status status = Status.OKAY, lastStatus = Status.OKAY;
    private final CopyOnWriteArrayList<Status> reportedStatuses = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<String, Double> motorVelocityError = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Double> motorVelocityLastTiming = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Double> motorTargetVelocity = new ConcurrentHashMap<>();
    private ArmPosition armTargetPosition;

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
    private TimeCraftersConfiguration configuration;
    private final double radius, diameter;

    private final double wheelRadius, wheelGearRatio, armGearRatio;
    private final int wheelTicksPerRevolution, armTicksPerRevolution;

    private final VuforiaLocalizer vuforia;
    private final TFObjectDetector tfod;

    private boolean LEDStatusToggle = false;
    private double lastLEDStatusAnimationTime = 0;

    public Robot(CyberarmEngine engine, TimeCraftersConfiguration configuration) {
        this.engine = engine;
        this.configuration = configuration;

        radius = tuningConfig("field_localizer_robot_radius").value();
        diameter = radius * 2;

        imuAngleOffset = hardwareConfig("imu_angle_offset").value();

        wheelRadius = tuningConfig("wheel_radius").value();
        wheelGearRatio = tuningConfig("wheel_gear_ratio").value();
        wheelTicksPerRevolution = tuningConfig("wheel_ticks_per_revolution").value();

        armGearRatio = tuningConfig("arm_gear_ratio").value();
        armTicksPerRevolution = tuningConfig("arm_ticks_per_revolution").value();

        // FIXME: Rename motors in configuration
        // Define hardware
        backLeftDrive = engine.hardwareMap.get(DcMotorEx.class, "Back Left");     // MOTOR PORT: 2 - CONTROL HUB
        frontRightDrive = engine.hardwareMap.get(DcMotorEx.class, "Front Right"); // MOTOR PORT: 1 - CONTROL HUB

        frontLeftDrive = engine.hardwareMap.get(DcMotorEx.class, "Front Left");   // MOTOR PORT: 3 - CONTROL HUB
        backRightDrive = engine.hardwareMap.get(DcMotorEx.class, "Back Right");   // MOTOR PORT: 0 - CONTROL HUB

        // FIXME: Rename lift_drive to arm in hardware config
        arm = engine.hardwareMap.get(DcMotorEx.class, "ArmMotor");          // MOTOR PORT: 2 - EXPANSION HUB

//        gripper = engine.hardwareMap.get(ServoImplEx.class, "gripper");       // SERVO PORT: ?

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
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armTargetPosition = ArmPosition.COLLECT;

        //      ZERO POWER BEHAVIOR
        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //      MOTOR POWER
        arm.setPower(tuningConfig("arm_automatic_power").value());

        //   SERVOS
        Servo leftRiser = engine.hardwareMap.servo.get("LowRiserLeft");
        leftRiser.setDirection(Servo.Direction.FORWARD);
        Servo rightRiser = engine.hardwareMap.servo.get("LowRiserRight");
        rightRiser.setDirection(Servo.Direction.REVERSE);

        //      NOTES: 428.57142858° per second = no load speed of 60° per 0.14s at 6V, see: https://docs.revrobotics.com/duo-build/actuators/servos/smart-robot-servo#mechanical-specifications
        leftRiserServoController = new PositionalServoController(
                leftRiser,
                5.0,
                428.57142858,
                270.0
        ); // SERVER PORT: ? - ? HUB

        rightRiserServoController = new PositionalServoController(
                rightRiser,
                5.0,
                428.57142858,
                270.0
        ); // SERVER PORT: ? - ? HUB

        cameraServo = engine.hardwareMap.servo.get("Camera Servo"); // SERVER PORT: ? - ? HUB

        collectorLeftServo = engine.hardwareMap.crservo.get("Collector Left");   // SERVER PORT: ? - ? HUB
        collectorRightServo = engine.hardwareMap.crservo.get("Collector Right"); // SERVER PORT: ? - ? HUB

        //      SERVO DIRECTIONS
        cameraServo.setDirection(Servo.Direction.REVERSE);
        collectorLeftServo.setDirection(DcMotorSimple.Direction.REVERSE);
        collectorRightServo.setDirection(DcMotorSimple.Direction.FORWARD);

        //  SENSORS
        //      IMU
        IMU.Parameters parameters = new IMU.Parameters(
            new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
            )
        );

        imu.initialize(parameters);

        // Preserve our "initial" facing, since we transform it from zero.
        initialFacing = facing();

        // BulkRead from Hubs
        for (LynxModule hub : engine.hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);

            if (!hub.isParent() && expansionHub == null) {
                expansionHub = hub;
            }
        }

        // Set LED pattern
        if (expansionHub != null) {
            expansionHub.setPattern(ledPatternOkay());
        }

        // Webcam
        vuforia = initVuforia();
        tfod = initTfod();

        // Drive Encoder Error Setup
        engine.blackboardSet("left_drive_error", 0);
        engine.blackboardSet("right_drive_error", 0);
    }

    private VuforiaLocalizer initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = hardwareConfig("vuforia_license_key").value();
        parameters.cameraName = engine.hardwareMap.get(WebcamName.class, "Webcam 1");

         return ClassFactory.getInstance().createVuforia(parameters);
    }

    private TFObjectDetector initTfod() {
        int tfodMonitorViewId = engine.hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", engine.hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        TFObjectDetector tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        tfod.loadModelFromAsset("PowerPlay.tflite", "1 Bolt", "2 Bulb", "3 Panel");

        return tfod;
    }

    public void reloadConfig() {
        String name = configuration.getConfig().getName();
        configuration = new TimeCraftersConfiguration(name);
    }

    public void standardTelemetry() {
        engine.telemetry.addLine();

        // STATUS
        engine.telemetry.addLine("DATA");
        engine.telemetry.addData("      Robot Status", status);
        engine.telemetry.addData("      Hardware Fault", hardwareFault);
        engine.telemetry.addData("      Hardware Fault Message", hardwareFaultMessage);
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

        // Motor Target Positions
        engine.telemetry.addLine("Motor Target Positions");
        engine.telemetry.addData("      Front Left Drive", "%d (%8.2f in)", frontLeftDrive.getTargetPosition(), ticksToUnit(DistanceUnit.INCH, frontLeftDrive.getTargetPosition()));
        engine.telemetry.addData("      Front Right Drive", "%d (%8.2f in)", frontRightDrive.getTargetPosition(), ticksToUnit(DistanceUnit.INCH, frontRightDrive.getTargetPosition()));

        engine.telemetry.addData("      Back Left Drive", "%d (%8.2f in)", backLeftDrive.getTargetPosition(), ticksToUnit(DistanceUnit.INCH, backLeftDrive.getTargetPosition()));
        engine.telemetry.addData("      Back Right Drive", "%d (%8.2f in)", backRightDrive.getTargetPosition(), ticksToUnit(DistanceUnit.INCH, backRightDrive.getTargetPosition()));

        engine.telemetry.addLine();

        engine.telemetry.addData("      Arm", "%d (%8.2f degrees)", arm.getTargetPosition(), ticksToAngle(arm.getTargetPosition()));

        engine.telemetry.addLine();

        // Motor Velocity
        engine.telemetry.addLine("Motor Velocity");
        engine.telemetry.addData("      Front Left Drive", "%8.2f (%8.2f in)", frontLeftDrive.getVelocity(), ticksToUnit(DistanceUnit.INCH, (int) frontLeftDrive.getVelocity()));
        engine.telemetry.addData("      Front Right Drive", "%8.2f (%8.2f in)", frontRightDrive.getVelocity(), ticksToUnit(DistanceUnit.INCH, (int) frontRightDrive.getVelocity()));

        engine.telemetry.addData("      Back Left Drive", "%8.2f (%8.2f in)", backLeftDrive.getVelocity(), ticksToUnit(DistanceUnit.INCH, (int) backLeftDrive.getVelocity()));
        engine.telemetry.addData("      Back Right Drive", "%8.2f (%8.2f in)", backRightDrive.getVelocity(), ticksToUnit(DistanceUnit.INCH, (int) backRightDrive.getVelocity()));

        engine.telemetry.addLine();

        engine.telemetry.addData("      Arm", "%8.2f (%8.2f degrees)", arm.getVelocity(), ticksToAngle((int)arm.getVelocity()));

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

        // Servo Positions
        engine.telemetry.addLine("Servo Positions");
        engine.telemetry.addData("      Left Riser (Est)/(Target)/(Blind)", "%.4f [error: %.4f] / %.4f / %.4f", leftRiserServoController.getEstimatedPosition(), leftRiserServoController.getError(), leftRiserServoController.getTargetPosition(), leftRiserServoController.getServo().getPosition());
        engine.telemetry.addData("      Right Riser (Est)/(Target)/(Blind)", "%.4f [error: %.4f] / %.4f / %.4f", rightRiserServoController.getEstimatedPosition(), rightRiserServoController.getError(), rightRiserServoController.getTargetPosition(), rightRiserServoController.getServo().getPosition());
        engine.telemetry.addData("      Camera (Blind)", cameraServo.getPosition());

        engine.telemetry.addLine();

        // Continuous Servo Powers
        engine.telemetry.addLine("Servo Powers");
        engine.telemetry.addData("      Collector Left", collectorLeftServo.getPower());
        engine.telemetry.addData("      Collector Right", collectorRightServo.getPower());

        engine.telemetry.addLine();

        // Servo Directions
        engine.telemetry.addLine("Servo Directions");
        engine.telemetry.addData("      Left Riser", leftRiserServoController.getServo().getDirection());
        engine.telemetry.addData("      Right Riser", rightRiserServoController.getServo().getDirection());
        engine.telemetry.addData("      Camera", cameraServo.getDirection());
        engine.telemetry.addData("      Collector Left", collectorLeftServo.getDirection());
        engine.telemetry.addData("      Collector Right", collectorRightServo.getDirection());

        engine.telemetry.addLine();

        // Sensors / IMU
        engine.telemetry.addLine("IMU");
        engine.telemetry.addData("      Facing (Degrees)", facing());
        engine.telemetry.addData("      Heading (Radians)", heading());
        engine.telemetry.addData("      Turn Rate", turnRate());
        engine.telemetry.addData("      Angle Offset (Degrees)", imuAngleOffset);

        engine.telemetry.addLine();
    }

    private ArrayList<Blinker.Step> ledPatternStandby() {
        final ArrayList<Blinker.Step> steps = new ArrayList<>();

        steps.add(new Blinker.Step(0x008000, 750, TimeUnit.MILLISECONDS));
        steps.add(new Blinker.Step(0x005000, 750, TimeUnit.MILLISECONDS));
        steps.add(new Blinker.Step(0x002000, 750, TimeUnit.MILLISECONDS));
        steps.add(new Blinker.Step(0x001000, 250, TimeUnit.MILLISECONDS));

        return steps;
    }

    private ArrayList<Blinker.Step> ledPatternOkay() {
        final ArrayList<Blinker.Step> steps = new ArrayList<>();

        steps.add(new Blinker.Step(0x00aa00, 500, TimeUnit.MILLISECONDS));
        steps.add(new Blinker.Step(0x00aa44, 500, TimeUnit.MILLISECONDS));

        return steps;
    }

    private ArrayList<Blinker.Step> ledPatternMonitoring() {
        final ArrayList<Blinker.Step> steps = new ArrayList<>();

        steps.add(new Blinker.Step(0xaaaaaa, 500, TimeUnit.MILLISECONDS));
        steps.add(new Blinker.Step(0x000000, 250, TimeUnit.MILLISECONDS));

        return steps;
    }

    private ArrayList<Blinker.Step> ledPatternWarning() {
        final ArrayList<Blinker.Step> steps = new ArrayList<>();

        steps.add(new Blinker.Step(0xffff00, 500, TimeUnit.MILLISECONDS));
        steps.add(new Blinker.Step(0xff8800, 500, TimeUnit.MILLISECONDS));

        return steps;
    }

    private ArrayList<Blinker.Step> ledPatternDanger() {
        final ArrayList<Blinker.Step> steps = new ArrayList<>();

        steps.add(new Blinker.Step(0xff0000, 250, TimeUnit.MILLISECONDS));
        steps.add(new Blinker.Step(0x000000, 100, TimeUnit.MILLISECONDS));

        return steps;
    }

    public void reportStatus(Status status) {
        reportedStatuses.add(status);
    }

    public void update() {
        for (LynxModule hub : engine.hardwareMap.getAll(LynxModule.class)) {
            hub.clearBulkCache();
        }

        double voltage = getVoltage();
        if (voltage < 9.75) {
            reportStatus(Status.DANGER);
            hardwareFaultMessage = "Battery voltage to low! (" + voltage + " volts)";

            hardwareFault = true;
        }

        status = Status.OKAY;
        for (Status s : reportedStatuses) {
            if (s.ordinal() > status.ordinal()) {
                status = s;
            }
        }

        reportedStatuses.clear();

        if (status != lastStatus) {
            lastStatus = status;

            if (expansionHub != null) {
                if (lastStatus == Status.OKAY) { expansionHub.setPattern(ledPatternOkay()); }
                if (lastStatus == Status.MONITORING) { expansionHub.setPattern(ledPatternMonitoring()); }
                if (lastStatus == Status.WARNING) { expansionHub.setPattern(ledPatternWarning()); }
                if (lastStatus == Status.DANGER) { expansionHub.setPattern(ledPatternDanger()); }
            }
        }

        manageArmAndRiserServos();
    }

    public void stop() {
        if (expansionHub != null) {
            expansionHub.setPattern(ledPatternStandby());
        }
    }

    public void armPosition(ArmPosition position) {
        if (hardwareFault) {
            return;
        }

        armTargetPosition = position;

        reportStatus(Status.WARNING);
    }

    private void manageArmAndRiserServos() {
        boolean lowerServos = true;

        switch (armTargetPosition) {
            case COLLECT:
                if (areRiserServosInLoweredPosition()) {
                    arm.setTargetPosition(angleToTicks(tuningConfig("arm_position_angle_collect").value()));
                }
                break;

//            case GROUND:
//                if (areRiserServosInLoweredPosition()) {
//                    arm.setTargetPosition(angleToTicks(tuningConfig("arm_position_angle_ground").value()));
//                }
//                break;

            case GROUND: // DISABLE GROUND JUNCTION SUPPORT FOR NOW
            case LOW:
                if (areRiserServosInLoweredPosition()) {
                    arm.setTargetPosition(angleToTicks(tuningConfig("arm_position_angle_low").value()));
                }
                break;

            case MEDIUM:
                if (areRiserServosInLoweredPosition()) {
                    arm.setTargetPosition(angleToTicks(tuningConfig("arm_position_angle_medium").value()));
                }
                break;

            case HIGH:
                double angleHigh = tuningConfig("arm_position_angle_high").value();
                arm.setTargetPosition(angleToTicks(angleHigh));

                if (arm.getCurrentPosition() >= angleToTicks(angleHigh - 10.0)) {
                    lowerServos = false;
                }
                break;

            default:
                throw new RuntimeException("Unexpected arm position!");
        }

        if (lowerServos) {
            leftRiserServoController.setTargetPosition(0.45);
            rightRiserServoController.setTargetPosition(0.45);
        } else {
            leftRiserServoController.setTargetPosition(0.8);
            rightRiserServoController.setTargetPosition(0.8);
        }

        leftRiserServoController.update();
        rightRiserServoController.update();
    }

    private boolean areRiserServosInLoweredPosition() {
        return leftRiserServoController.getEstimatedPosition() < 0.5 && rightRiserServoController.getEstimatedPosition() < 0.5;
    }

    // Adapted from: https://github.com/gosu/gosu/blob/980d64de2ce52e4b16fdd5cb9c9e11c8bbb80671/src/Math.cpp#L38
    public double angleDiff(double from, double to) {
        double value = (to - from + 180);

        double fmod = (value - 0.0) % (360.0 - 0.0);

        return (fmod < 0 ? fmod + 360.0 : fmod +  0.0) - 180;
    }

    public double lerp(double min, double max, double t)
    {
        return min + (max - min) * t;
    }

    public Status getStatus() { return status; }

    public double getRadius() { return radius; }

    public double getDiameter() { return diameter; }

    public double getVoltage() {
        return engine.hardwareMap.voltageSensor.iterator().next().getVoltage();
    }

    public TFObjectDetector getTfod() { return tfod; }

    public VuforiaLocalizer getVuforia() { return vuforia; }

    public TimeCraftersConfiguration getConfiguration() { return configuration; }

    // For: Drive Wheels
    public int unitToTicks(DistanceUnit unit, double distance) {
        double fI = (wheelGearRatio * wheelTicksPerRevolution) / (wheelRadius * 2 * Math.PI * (wheelGearRatio * wheelTicksPerRevolution) / (wheelGearRatio * wheelTicksPerRevolution));

        double inches = unit.toInches(unit.fromUnit(unit, distance));

        double ticks = fI * inches;

        return (int)ticks;
    }

    // For: Drive Wheels
    public double ticksToUnit(DistanceUnit unit, int ticks) {
        // Convert to inches, then to unit.
        double inches = wheelRadius * 2 * Math.PI * ticks / (wheelGearRatio * wheelTicksPerRevolution);

        return unit.fromUnit(DistanceUnit.INCH, inches);
    }

    // For: Arm
    public int angleToTicks(double angle) {
        double d = (armGearRatio * armTicksPerRevolution) / 360.0;

        // Casting to float so that the int version of Math.round is used.
        return Math.round((float)d * (float)angle);
    }

    // For: Arm
    public double ticksToAngle(int ticks) {
        double oneDegree = 360.0 / (armGearRatio * armTicksPerRevolution);

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

    @SuppressLint("NewApi")
    public void controlMotorPIDF(DcMotorEx motor, String motorName, double targetVelocity, double feedForward) {
        Action action = configuration.action("Robot", "Tuning_PIDF_" + motorName);
        double proportional = 0, integral = 0, derivative = 0;

        for (Variable v : action.getVariables()) {
            switch (v.name.trim()) {
                case "proportional":
                    proportional = v.value();
                    break;
                case "integral":
                    integral = v.value();
                    break;
                case "derivative":
                    derivative = v.value();
                    break;
            }
        }

        double interval = (engine.getRuntime() - motorVelocityLastTiming.getOrDefault(motorName, 0.0));

        double distance = motor.getTargetPosition() - motor.getCurrentPosition();

        if (Math.abs(distance) < Math.abs(targetVelocity)) {
            if ((targetVelocity < 0 && distance > 0) || (targetVelocity > 0 && distance < 0)) {
                targetVelocity = -distance;
            } else {
                targetVelocity = distance;
            }
        }

        double error = targetVelocity - motor.getVelocity();
        double deltaError = error - motorVelocityError.getOrDefault(motorName, 0.0);

        double kIntegral = error * interval;
        double kDerivative = deltaError / interval;

        double kp = proportional * error;
        double ki = integral * kIntegral;
        double kd = derivative * kDerivative;

        motorVelocityError.put(motorName, error);
        motorVelocityLastTiming.put(motorName, engine.getRuntime());

        double newTargetVelocity = kp + ki + kd + targetVelocity;

        motor.setVelocity(newTargetVelocity);

        Log.d(TAG, "Interval: " + interval + "ms, Error: " + error + " ticks, deltaError: " + deltaError + " ticks, distance: " +
                distance + " ticks, kIntegral: " + kIntegral + ", kDerivative: " + kDerivative + ", proportional: " + proportional +
                ", integral: " + integral + ", derivative: " + derivative + ", kp: " + kp + ", ki: " + ki + ", kd: " + kd +
                ", targetVelocity: " + targetVelocity + " ticks, new Target Velocity: " + newTargetVelocity + " ticks, + motorVelocity: " + motor.getVelocity() + " ticks.");
    }

    @SuppressLint("NewApi")
    public void controlArmMotor(double targetVelocity) {
//        double time = System.currentTimeMillis();
//        double newTargetVelocity = motorTargetVelocity.getOrDefault("Arm", targetVelocity);
//        double lastTiming = motorVelocityLastTiming.getOrDefault("Arm", time);
//        double deltaTime = (time - lastTiming) * 0.001;
//
//        double distanceToTarget = arm.getTargetPosition() - arm.getCurrentPosition();
//        double adjustedTargetVelocity = Math.abs(distanceToTarget) < targetVelocity ? Math.abs(distanceToTarget) : targetVelocity;
//
//        double error = adjustedTargetVelocity - arm.getVelocity();
//        double kp = 0.9;
//
//        newTargetVelocity += error * kp * deltaTime;
//
//        motorTargetVelocity.put("Arm", newTargetVelocity);
//        motorVelocityLastTiming.put("Arm", time);

//        arm.setVelocity(newTargetVelocity);

        arm.setPower(tuningConfig("arm_automatic_power").value());
    }

    public double initialFacing() {
        return initialFacing;
    }

    public double facing() {
        double imuDegrees = -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

        return (((imuDegrees + 360.0) % 360.0) + imuAngleOffset) % 360.0;
    }

    public double heading() {
        return AngleUnit.normalizeRadians(-facing() * Math.PI / 180.0);
//        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    public double turnRate() {
        return imu.getRobotAngularVelocity(AngleUnit.DEGREES).yRotationRate; // NOTE: UNTESTED
    }

    public boolean isBetween(double value, double min, double max) {
        return value >= min && value <= max;
    }
}
