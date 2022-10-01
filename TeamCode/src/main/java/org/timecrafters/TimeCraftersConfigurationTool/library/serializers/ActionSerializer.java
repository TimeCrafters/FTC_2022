package org.timecrafters.TimeCraftersConfigurationTool.library.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Action;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Group;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Variable;

import java.lang.reflect.Type;

public class ActionSerializer implements JsonSerializer<Action> {
    @Override
    public JsonElement serialize(Action action, Type type, JsonSerializationContext context) {
        JsonObject container = new JsonObject();

        container.add("name", new JsonPrimitive(action.name));
        container.add("comment", new JsonPrimitive(action.comment));
        container.add("enabled", new JsonPrimitive(action.enabled));
        container.add("variables", context.serialize(action.getVariables().toArray(), Variable[].class));

        return container;
    }
}