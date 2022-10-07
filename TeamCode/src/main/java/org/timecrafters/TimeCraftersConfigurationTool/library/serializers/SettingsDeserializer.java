package org.timecrafters.TimeCraftersConfigurationTool.library.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.timecrafters.TimeCraftersConfigurationTool.library.backend.Settings;

import java.lang.reflect.Type;

public class SettingsDeserializer implements JsonDeserializer<Settings> {
    @Override
    public Settings deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject data = jsonObject.get("data").getAsJsonObject();

        return new Settings(
                    data.get("hostname").getAsString(),
                    data.get("port").getAsInt(),
                    data.get("config").getAsString()
                );
    }
}
