package org.timecrafters.minibots.cyberarm;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.cyberarm.engine.V2.CyberarmEngine;

public class pickle_minibot {
    private CyberarmEngine engine;

    public DcMotor pLeftFront;

    public pickle_minibot (CyberarmEngine engine){
        this.engine=engine;

        pLeftFront = engine.hardwareMap.dcMotor.get("frontLeft");
    }
}
