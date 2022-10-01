package org.timecrafters.TimeCraftersConfigurationTool.library.backend.config;

import java.util.Date;

public class Configuration {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    public Date createdAt, updatedAt;
    private int specVersion;
    public int revision;

    public Configuration(Date createdAt, Date updatedAt, int specVersion, int revision) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.specVersion = specVersion;
        this.revision = revision;
    }

    public int getSpecVersion() { return specVersion; }
}