package org.timecrafters.minibots.cyberarm.engines;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.cyberarm.engine.V2.CyberarmEngine;

public class Common {

    private CyberarmEngine engine;

    public RevBlinkinLedDriver leds;

    public Common (CyberarmEngine engine) {

        this.engine = engine;

        setupRobot ();

    }

     private void setupRobot () {

        leds = engine.hardwareMap.get(RevBlinkinLedDriver.class, "lights");

     }

}
