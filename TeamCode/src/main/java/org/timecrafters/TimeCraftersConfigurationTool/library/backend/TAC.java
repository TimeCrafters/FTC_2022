package org.timecrafters.TimeCraftersConfigurationTool.library.backend;

import android.os.Environment;

import java.io.File;

public class TAC {
    // TODO: Update filesystem handling
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "TimeCrafters_Configuration_Tool",
                               CONFIGS_PATH = ROOT_PATH + File.separator + "/configs",
                               SETTINGS_PATH = ROOT_PATH + File.separator + "settings.json";

    public static final int CONFIG_SPEC_VERSION = 2;
}
