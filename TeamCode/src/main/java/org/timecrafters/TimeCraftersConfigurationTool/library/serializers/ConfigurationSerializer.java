package org.timecrafters.TimeCraftersConfigurationTool.library.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Configuration;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Variable;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

public class ConfigurationSerializer implements JsonSerializer<Configuration> {
    @Override
    public JsonElement serialize(Configuration configuration, Type type, JsonSerializationContext context) {
        JsonObject container = new JsonObject();

        SimpleDateFormat dateFormat = new SimpleDateFormat(Configuration.DATE_FORMAT);

        container.add("created_at", new JsonPrimitive(dateFormat.format(configuration.createdAt)));
        container.add("updated_at", new JsonPrimitive(dateFormat.format(configuration.updatedAt)));
        container.add("spec_version", new JsonPrimitive(configuration.getSpecVersion()));
        container.add("revision", new JsonPrimitive(configuration.revision));

        return container;
    }
}