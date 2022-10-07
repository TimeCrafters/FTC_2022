package org.timecrafters.TimeCraftersConfigurationTool.library.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Configuration;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigurationDeserializer implements JsonDeserializer<Configuration> {
    @Override
    public Configuration deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject config = json.getAsJsonObject();

        SimpleDateFormat dateFormat = new SimpleDateFormat(Configuration.DATE_FORMAT);
        Date createdAt = new Date();
        Date updatedAt = new Date();
        try {
            createdAt = dateFormat.parse(config.get("created_at").getAsString());
            updatedAt = dateFormat.parse(config.get("updated_at").getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final int spec_version = config.get("spec_version").getAsInt();
        final int revision = config.get("revision").getAsInt();

        return new Configuration(createdAt, updatedAt, spec_version, revision);
    }
}
