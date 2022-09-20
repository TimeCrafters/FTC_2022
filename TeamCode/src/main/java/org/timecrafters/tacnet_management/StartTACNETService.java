package org.timecrafters.tacnet_management;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

@TeleOp(name = "Start TACNET Service", group = "TACNET")
public class StartTACNETService extends OpMode {
    @Override
    public void init() {
        Context appContext = FtcRobotControllerActivity.getAppActivity().getApplicationContext();
        Intent tacnetIntent = new Intent("org.timecrafters.TimeCraftersConfigurationTool.tacnet.ACTION_START_SERVER");
        tacnetIntent.setPackage("org.timecrafters.TimeCraftersConfigurationTool");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appContext.startForegroundService(tacnetIntent);
        } else {
            appContext.startService(tacnetIntent);
        }
    }

    @Override
    public void loop() {
        stop();
    }
}
