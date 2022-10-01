package org.timecrafters.TimeCraftersConfigurationTool.library.backend;

import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Action;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Configuration;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Group;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Presets;

import java.util.ArrayList;
import java.util.Date;

public class Config {
    private String name;
    private Configuration configuration;
    private ArrayList<Group> groups;
    private Presets presets;

    public Config(String name) {
        this.name = name;
        this.configuration = new Configuration(new Date(), new Date(), TAC.CONFIG_SPEC_VERSION, 0);
        groups = new ArrayList<>();
        presets = new Presets(new ArrayList<Group>(), new ArrayList<Action>());
    }

    public Config(Configuration configuration, ArrayList<Group> groups, Presets presets) {
        this.configuration = configuration;
        this.groups = groups;
        this.presets = presets;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Presets getPresets() {
        return presets;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }
}
