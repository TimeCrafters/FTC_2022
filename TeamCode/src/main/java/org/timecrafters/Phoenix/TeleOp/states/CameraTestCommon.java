package org.timecrafters.Phoenix.TeleOp.states;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

public class CameraTestCommon {

    private static final String TENSORFLOW_MODEL_ASSET = "model_20221009_154335.tflite";
    private static final String[] TENSORFLOW_MODEL_LABELS = {
            "1 dalek",
            "2 steve",
            "3 banner"
    };

    public TFObjectDetector tensorflow;
    private List<Recognition> tensorflowRecognitions = new ArrayList<>();

    private final CyberarmEngine engine;

    public CameraTestCommon(CyberarmEngine engine) {
        this.engine = engine;
        initTensorflow();
    }


    private void initTensorflow() {
        int tfodMonitorViewId = engine.hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", engine.hardwareMap.appContext.getPackageName());

        TFObjectDetector.Parameters parameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        parameters.minResultConfidence = 0.8f;
        parameters.isModelTensorFlow2 = true;
        parameters.inputSize = 320;

        tensorflow.loadModelFromAsset(TENSORFLOW_MODEL_ASSET, TENSORFLOW_MODEL_LABELS);
    }

    public void activateTensorflow() {
        tensorflow.activate();
    }

    public List<Recognition> tensorflowDetections() {
        List<Recognition> updateRecognitions = tensorflow.getUpdatedRecognitions();

        if (updateRecognitions != null) {
            tensorflowRecognitions = updateRecognitions;
        }

        return tensorflowRecognitions;
    }

    public void deactivateTensorflow() {
        tensorflow.deactivate();
    }
}
