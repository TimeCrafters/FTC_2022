package org.timecrafters.TimeCraftersConfigurationTool.library.backend.config;


import java.util.ArrayList;

public class Presets {
    private ArrayList<Group> groups;
    private ArrayList<Action> actions;

    public Presets(ArrayList<Group> groups, ArrayList<Action> actions) {
        this.groups = groups;
        this.actions = actions;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
}