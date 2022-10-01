package org.timecrafters.TimeCraftersConfigurationTool.library.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Action;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Group;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Variable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionDeserializer implements JsonDeserializer<Action> {
    @Override
    public Action deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        final String name = jsonObject.get("name").getAsString();
        final String comment = jsonObject.get("comment").getAsString();
        final boolean enabled = jsonObject.get("enabled").getAsBoolean();
        Variable[] variablesArray = context.deserialize(jsonObject.get("variables"), Variable[].class);

        List<Variable> variablesList = Arrays.asList(variablesArray);
        ArrayList<Variable> variables = new ArrayList<>(variablesList);

        return new Action(name, comment, enabled, variables);
    }
}