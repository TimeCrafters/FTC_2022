package org.timecrafters.minibots.cyberarm.engines;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.cyberarm.engine.V2.CyberarmState;

@TeleOp(name = "TasksEngine", group = "testing")
public class TasksEngine extends CyberarmEngine {
    @Override
    public void setup() {
        addTask(new CyberarmState() {
            @Override
            public void init() {
                engine.blackboardSet("counter", 0);
                engine.blackboardSet("string", "I IS STRING");
                engine.blackboardSet("boolean", true);
            }

            @Override
            public void exec() {
            }

            @Override
            public void telemetry() {
                engine.telemetry.addData("TASK 1", engine.blackboardGetInt("counter"));
                engine.telemetry.addData("TASK 1", "I am a task!");
            }
        });

        addTask(new CyberarmState() {
            int lastCount = 0, blackboardLastCount = 0;
            int count = 0;
            double lastRuntime = 0;
            @Override
            public void exec() {
                if (runTime() - lastRuntime >= 1000.0) {
                    lastRuntime = runTime();
                    lastCount = count;
                    blackboardLastCount = engine.blackboardGetInt("counter");
                }
                engine.blackboardSet("counter", engine.blackboardGetInt("counter") + 1);
                count++;
            }

            @Override
            public void telemetry() {
                engine.telemetry.addData("TASK 2", engine.blackboardGetString("string"));
                engine.telemetry.addData("TASK 2", engine.blackboardGetBoolean("boolean"));
                engine.telemetry.addData("TASK 2", engine.blackboardGetInt("counter") - blackboardLastCount);
                engine.telemetry.addData("TASK 2", count - lastCount);
                engine.telemetry.addData("TASK 2", engine.blackboardGetString("string_NULL") == null);
            }
        });

        addState(new CyberarmState() {
            @Override
            public void exec() {
                if (runTime() >= 10_000) {
                    setHasFinished(true);
                }
            }
        });

        addParallelStateToLastState(new CyberarmState() {
            @Override
            public void exec() {
            }

            @Override
            public void telemetry() {
                engine.telemetry.addData("Parallel state 1", "Hello There");
            }
        });
    }
}
