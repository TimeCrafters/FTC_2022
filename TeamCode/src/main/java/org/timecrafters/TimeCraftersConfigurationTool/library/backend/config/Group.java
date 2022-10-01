package org.timecrafters.TimeCraftersConfigurationTool.library.backend.config;

import java.util.ArrayList;

public class Group {
    public String name;
    private ArrayList<Action> actions;

    public Group(String name, ArrayList<Action> actions) {
        this.name = name;
        this.actions = actions;
    }

    public static boolean nameIsUnique(ArrayList<Group> groups, String name) {
        for (Group group: groups) {
            if (group.name.equals(name)) {
                return false;
            }
        }

        return true;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
}
