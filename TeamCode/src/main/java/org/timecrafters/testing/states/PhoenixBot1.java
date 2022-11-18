package org.timecrafters.testing.states;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.timecrafters.TimeCraftersConfigurationTool.library.TimeCraftersConfiguration;

public class PhoenixBot1 {

    private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";

    private static final String[] LABELS = {
            "1 Bolt",
            "2 Bulb",
            "3 Panel"
    };

    private static final String VUFORIA_KEY =
            "Abmu1jv/////AAABmYzrcgDEi014nv+wD6PkEPVnOlV2pI3S9sGUMMR/X7hF72x20rP1JcVtsU0nI6VK0yUlYbCSA2k+yMo4hQmPDBvrqeqAgXKa57ilPhW5e1cB3BEevP+9VoJ9QYFhKA3JJTiuFS50WQeuFy3dp0gOPoqHL3XClRFZWbhzihyNnLXgXlKiq+i5GbfONECucQU2DgiuuxYlCaeNdUHl1X5C2pO80zZ6y7PYAp3p0ciXJxqfBoVAklhd69avaAE5Z84ctKscvcbxCS16lq81X7XgIFjshLoD/vpWa300llDG83+Y777q7b5v7gsUCZ6FiuK152Rd272HLuBRhoTXAt0ug9Baq5cz3sn0sAIEzSHX1nah";

    public VuforiaLocalizer vuforia;

    public TFObjectDetector tfod;

    public Servo LowRiserLeft, LowRiserRight, HighRiserLeft, HighRiserRight, CameraServo;
    private final CyberarmEngine engine;

    public Rev2mDistanceSensor collectorDistance;

        public DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;

        public CRServo collectorLeft, collectorRight;

         public BNO055IMU imu;

         public TimeCraftersConfiguration configuration;

         public AdafruitI2cColorSensor AdafruitEncoder;


        public PhoenixBot1(CyberarmEngine engine) {
            this.engine = engine;

            initVuforia();
            initTfod();
            setupRobot();
        }

        private void setupRobot () {
            collectorDistance = engine.hardwareMap.get(Rev2mDistanceSensor.class, "collectorDistance");

            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

            parameters.mode = BNO055IMU.SensorMode.IMU;
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.loggingEnabled = false;

            this.imu = engine.hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);

            imu.startAccelerationIntegration(new Position(), new Velocity(), 10);

            configuration = new TimeCraftersConfiguration("Phoenix");
            AdafruitEncoder = engine.hardwareMap.AdafruitI2cColorSensor.get("adafruit");

            //motors configuration
            frontLeftDrive = engine.hardwareMap.dcMotor.get("Front Left");
            frontRightDrive = engine.hardwareMap.dcMotor.get("Front Right");
            backRightDrive = engine.hardwareMap.dcMotor.get("Back Right");
            backLeftDrive = engine.hardwareMap.dcMotor.get("Back Left");

            // servo configuration

            //Camera Servo
            CameraServo = engine.hardwareMap.servo.get("Camera Servo");
            CameraServo.setDirection(Servo.Direction.REVERSE);

            // Collector
            collectorLeft = engine.hardwareMap.crservo.get("Collector Left");
            collectorRight = engine.hardwareMap.crservo.get("Collector Right");

            collectorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            collectorRight.setDirection(DcMotorSimple.Direction.FORWARD);

            // Arm
            LowRiserLeft = engine.hardwareMap.servo.get("LowRiserLeft");
            LowRiserRight = engine.hardwareMap.servo.get("LowRiserRight");
            HighRiserLeft = engine.hardwareMap.servo.get("HighRiserLeft");
            HighRiserRight = engine.hardwareMap.servo.get("HighRiserRight");

            //motors direction and encoders

            frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            frontRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            backRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            HighRiserLeft.setDirection(Servo.Direction.REVERSE);
            HighRiserRight.setDirection(Servo.Direction.FORWARD);
            LowRiserLeft.setDirection(Servo.Direction.FORWARD);
            LowRiserRight.setDirection(Servo.Direction.REVERSE);

            LowRiserLeft.setPosition(0.35);
            LowRiserRight.setPosition(0.35);
            HighRiserLeft.setPosition(0.45);
            HighRiserRight.setPosition(0.45);

            CameraServo.setPosition(0);

        }

            private void initVuforia(){
                /*
                 * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
                 */
                int cameraMonitorViewId = engine.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", engine.hardwareMap.appContext.getPackageName());
                VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

                parameters.vuforiaLicenseKey = VUFORIA_KEY;
                parameters.cameraName = engine.hardwareMap.get(WebcamName.class, "Webcam 1");


                //  Instantiate the Vuforia engine
                vuforia = ClassFactory.getInstance().createVuforia(parameters);
            }

            private void initTfod() {
                int tfodMonitorViewId = engine.hardwareMap.appContext.getResources().getIdentifier(
                        "tfodMonitorViewId", "id", engine.hardwareMap.appContext.getPackageName());
                TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
                tfodParameters.minResultConfidence = 0.75f;
                tfodParameters.isModelTensorFlow2 = true;
                tfodParameters.inputSize = 300;
                tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
                tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
            }

}
