package org.timecrafters.Autonomous.States;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.timecrafters.Autonomous.TeleOp.states.PhoenixBot1;

import java.util.List;

public class ConeIdentification extends CyberarmState {
    PhoenixBot1 robot;
    private int time;
    private float minimumConfidence;
    private int ParkPlace;
    long initTime;

    public ConeIdentification(PhoenixBot1 robot, String groupName, String actionName) {
        this.robot = robot;
        minimumConfidence = robot.configuration.variable(groupName, actionName, "Minimum Confidence").value();
        time = robot.configuration.variable(groupName, actionName, "time").value();
    }

    @Override
    public void init() {
        engine.blackboardSet("parkPlace", "1");
        robot.tfod.activate();
        initTime = System.currentTimeMillis();
    }

    @Override
    public void telemetry() {
        if (robot.tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = robot.tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                engine.telemetry.addData("# Objects Detected", updatedRecognitions.size());

                // step through the list of recognitions and display image position/size information for each one
                // Note: "Image number" refers to the randomized image orientation/number
                for (Recognition recognition : updatedRecognitions) {
                    double col = (recognition.getLeft() + recognition.getRight()) / 2 ;
                    double row = (recognition.getTop()  + recognition.getBottom()) / 2 ;
                    double width  = Math.abs(recognition.getRight() - recognition.getLeft()) ;
                    double height = Math.abs(recognition.getTop()  - recognition.getBottom()) ;

                    engine.telemetry.addData("Label", recognition.getLabel());
                    engine.telemetry.addData(""," ");
                    engine.telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 );
                    engine.telemetry.addData("- Position (Row/Col)","%.0f / %.0f", row, col);
                    engine.telemetry.addData("- Size (Width/Height)","%.0f / %.0f", width, height);

                    if (recognition.getLabel().equals("#2")) {
                        engine.telemetry.addData("#2", engine.blackboardSet("parkPlace", "2"));
                    } else if (recognition.getLabel().equals("#3")) {
                        engine.telemetry.addData("#3",engine.blackboardSet("parkPlace", "3"));
                    } else {
                        engine.telemetry.addData("#1", engine.blackboardSet("parkPlace", "1"));
                    }
                }
            }
        }
    }

    @Override
    public void start() {
        long startTime = System.currentTimeMillis() - initTime;
        if (startTime < time){
            time = (int) (time - startTime);
        }
        else {
            time = 0;
        }
    }

    @Override
    public void exec() {


        if (robot.tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = robot.tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                // step through the list of recognitions and display image position/size information for each one
                // Note: "Image number" refers to the randomized image orientation/number
                float bestConfidence = 0;

                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getConfidence() >= minimumConfidence && recognition.getConfidence() > bestConfidence) {
                        bestConfidence = recognition.getConfidence();

                        if (recognition.getLabel().equals("#2")) {
                            engine.blackboardSet("parkPlace", "2");
                        } else if (recognition.getLabel().equals("#3")) {
                            engine.blackboardSet("parkPlace", "3");

                        } else {
                            engine.blackboardSet("parkPlace", "1");
                        }
                    }
                }
            }
        }

        if (runTime() >= time){
            setHasFinished(true);
        }

    }
}
