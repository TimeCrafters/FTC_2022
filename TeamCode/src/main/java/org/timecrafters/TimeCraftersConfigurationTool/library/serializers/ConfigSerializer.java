package org.timecrafters.TimeCraftersConfigurationTool.library.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.timecrafters.TimeCraftersConfigurationTool.library.backend.Config;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Action;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Configuration;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Group;

import java.lang.reflect.Type;

public class ConfigSerializer implements JsonSerializer<Config> {
    @Override
    public JsonElement serialize(Config config, Type type, JsonSerializationContext context) {
        JsonObject container = new JsonObject();
        JsonObject result = new JsonObject();
        JsonObject presets = new JsonObject();
        container.add("config", context.serialize(config.getConfiguration(), Configuration.class));
        result.add("groups", context.serialize(config.getGroups().toArray(), Group[].class));

        presets.add("groups", context.serialize(config.getPresets().getGroups().toArray(), Group[].class));
        presets.add("actions", context.serialize(config.getPresets().getActions().toArray(), Action[].class));

        result.add("presets", presets);
        container.add("data", result);

        return container;
    }
}