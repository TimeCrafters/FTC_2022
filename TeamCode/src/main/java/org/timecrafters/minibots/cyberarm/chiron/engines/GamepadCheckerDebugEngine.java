package org.timecrafters.minibots.cyberarm.chiron.engines;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.cyberarm.engine.V2.CyberarmEngine;
import org.cyberarm.engine.V2.CyberarmState;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;

//@TeleOp(name = "CHIRON | Gamepad Checker Debug", group = "CHIRON")
public class GamepadCheckerDebugEngine extends CyberarmEngine {
    @Override
    public void setup() {
        addState(new CyberarmState() {
            private AndroidTextToSpeech speech;

            @Override
            public void init() {
                Log.d(TAG, "INITIALING STATE");
                speech = new AndroidTextToSpeech();
                speech.initialize();
            }

            @Override
            public void exec() {
            }

            @Override
            public void telemetry() {
                engine.telemetry.addData("Run Time", runTime());
            }

            @Override
            public void buttonDown(Gamepad gamepad, String button) {
                if (gamepad != engine.gamepad1) {
                    return;
                }

                if (button.equals("guide")) {
                    speech.speak("GUIDE");
                }
            }
        });
    }
}
