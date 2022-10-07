package org.timecrafters.TimeCraftersConfigurationTool.library.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Variable;

import java.lang.reflect.Type;

public class VariableSerializer implements JsonSerializer<Variable> {
    @Override
    public JsonElement serialize(Variable variable, Type type, JsonSerializationContext context) {
        JsonObject container = new JsonObject();

        container.add("name", new JsonPrimitive(variable.name));
        container.add("value", new JsonPrimitive(variable.rawValue()));

        return container;
    }
}