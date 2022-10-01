package org.timecrafters.TimeCraftersConfigurationTool.library.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Action;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Group;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Presets;

import java.lang.reflect.Type;

public class PresetsSerializer implements JsonSerializer<Presets> {
    @Override
    public JsonElement serialize(Presets presets, Type type, JsonSerializationContext context) {
        JsonObject container = new JsonObject();

        container.add("groups", context.serialize(presets.getGroups().toArray(), Group[].class));
        container.add("actions", context.serialize(presets.getActions().toArray(), Action[].class));

        return container;
    }
}