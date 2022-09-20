package org.timecrafters.tacnet_management;

import android.content.Context;
import android.content.Intent;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

@TeleOp(name = "Halt TACNET Service", group = "TACNET")
public class HaltTACNETService extends OpMode {
    @Override
    public void init() {
        Context appContext = FtcRobotControllerActivity.getAppActivity().getApplicationContext();
        Intent tacnetIntent = new Intent("org.timecrafters.TimeCraftersConfigurationTool.tacnet.ACTION_START_SERVER");
        tacnetIntent.setPackage("org.timecrafters.TimeCraftersConfigurationTool");

        appContext.stopService(tacnetIntent);
    }

    @Override
    public void loop() {
        stop();
    }
}
