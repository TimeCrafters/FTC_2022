package org.timecrafters.TimeCraftersConfigurationTool.library.backend.config;

import java.util.ArrayList;

public class Action {
    public String name, comment;
    public boolean enabled;
    private ArrayList<Variable> variables;

    public Action(String name, String comment, boolean enabled, ArrayList<Variable> variables) {
        this.name = name;
        this.comment = comment;
        this.enabled = enabled;
        this.variables = variables;
    }

    public ArrayList<Variable> getVariables() { return variables; }
}
