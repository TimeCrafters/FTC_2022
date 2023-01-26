package org.timecrafters.minibots.cyberarm.chiron.states.autonomous;

import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.timecrafters.minibots.cyberarm.chiron.Robot;

import java.util.List;

public class SignalProcessor extends CyberarmState {
    private final Robot robot;
    private final String groupName, actionName;

    private final double timeInMS, minConfidence;
    private final int fallbackPosition;
    private final boolean stateDisabled;

    public SignalProcessor(Robot robot, String groupName, String actionName) {
        this.robot = robot;
        this.groupName = groupName;
        this.actionName = actionName;

        timeInMS = robot.getConfiguration().variable(groupName, actionName, "timeInMS").value();
        minConfidence = robot.getConfiguration().variable(groupName, actionName, "minConfidence").value();
        fallbackPosition = robot.getConfiguration().variable(groupName, actionName, "fallbackPosition").value();

        stateDisabled = !robot.getConfiguration().action(groupName, actionName).enabled;
    }

    @Override
    public void start() {
        engine.blackboardSet("parking_position", fallbackPosition);

        robot.getTfod().activate();
    }

    @Override
    public void exec() {
        if (stateDisabled) {
            stop();

            return;
        }

        if (runTime() >= timeInMS) {
            stop();

            return;
        }

        if (robot.getTfod() != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = robot.getTfod().getUpdatedRecognitions();

            if (updatedRecognitions != null) {
                for (Recognition recognition : updatedRecognitions) {
                    switch (recognition.getLabel()) {
                        case "1 Bolt":
                            engine.blackboardSet("parking_position", 1);

                            break;
                        case "2 Bulb":
                            engine.blackboardSet("parking_position", 2);

                            break;
                        case "3 Panel":
                            engine.blackboardSet("parking_position", 3);

                            break;
                    }
                }
            }
        }
    }

    @Override
    public void telemetry() {
        if (robot.getTfod() != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = robot.getTfod().getUpdatedRecognitions();

            if (updatedRecognitions != null) {
                engine.telemetry.addData("# Objects Detected", updatedRecognitions.size());

                // step through the list of recognitions and display image position/size information for each one
                // Note: "Image number" refers to the randomized image orientation/number
                for (Recognition recognition : updatedRecognitions) {
                    double col = (recognition.getLeft() + recognition.getRight()) / 2 ;
                    double row = (recognition.getTop()  + recognition.getBottom()) / 2 ;
                    double width  = Math.abs(recognition.getRight() - recognition.getLeft()) ;
                    double height = Math.abs(recognition.getTop()  - recognition.getBottom()) ;

                    engine.telemetry.addData(""," ");
                    engine.telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 );
                    engine.telemetry.addData("- Position (Row/Col)","%.0f / %.0f", row, col);
                    engine.telemetry.addData("- Size (Width/Height)","%.0f / %.0f", width, height);
                }
            }
        }
    }

    @Override
    public void stop() {
        setHasFinished(true);

        robot.getTfod().deactivate();
    }
}
