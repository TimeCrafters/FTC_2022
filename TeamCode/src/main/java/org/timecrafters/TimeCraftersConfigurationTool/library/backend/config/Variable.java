package org.timecrafters.TimeCraftersConfigurationTool.library.backend.config;

import android.util.Log;

import java.util.Arrays;

public class Variable {
    public String name;
    private String value;

    public Variable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String rawValue() {
        return value;
    }

    public <T> T value() {
        return valueOf(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    static public <T> T valueOf(String value) {
        String[] split = value.split("x", 2);
//        Log.d("Variable", "valueOf split: " + Arrays.toString(split));

        switch (split[0]) {
            case "B": {
                return (T) Boolean.valueOf(split[(split.length-1)]);
            }
            case "D": {
                return (T) Double.valueOf(split[(split.length-1)]);
            }
            case "F": {
                return (T) Float.valueOf(split[(split.length-1)]);
            }
            case "I": {
                return (T) Integer.valueOf(split[(split.length-1)]);
            }
            case "L": {
                return (T) Long.valueOf(split[(split.length-1)]);
            }
            case "S": {
                String string = "";
                int i = 0;
                for(String str : split) {
                    if (i == 0) { i++; continue; }

                    string += str;
                }
                return (T) string;
            }
            default: {
                return null;
            }
        }
    }

    static public String typeOf(String value) {
        String[] split = value.split("x");

        switch (split[0]) {
            case "B": {
                return "Boolean";
            }
            case "D": {
                return "Double";
            }
            case "F": {
                return "Float";
            }
            case "I": {
                return "Integer";
            }
            case "L": {
                return "Long";
            }
            case "S": {
                return "String";
            }
            default: {
                return "=!UNKNOWN!=";
            }
        }
    }
}