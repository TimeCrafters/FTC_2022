package org.timecrafters.TimeCraftersConfigurationTool.library.backend;

public class Settings {
    public String hostname, config;
    public int port;

    public Settings(String hostname, int port, String config) {
        this.hostname = hostname;
        this.port = port;
        this.config = config;
    }
}
