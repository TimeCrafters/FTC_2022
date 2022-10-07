package org.timecrafters.TimeCraftersConfigurationTool.library.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.timecrafters.TimeCraftersConfigurationTool.library.backend.Config;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Configuration;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Group;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Presets;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigDeserializer implements JsonDeserializer<Config> {
    @Override
    public Config deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject data = jsonObject.get("data").getAsJsonObject();

        Configuration configuration = context.deserialize(jsonObject.get("config"), Configuration.class);
        Group[] groupsArray = context.deserialize(data.get("groups"), Group[].class);
        List<Group> groupsList = Arrays.asList(groupsArray);
        ArrayList<Group> groups = new ArrayList<>(groupsList);

        Presets presets = context.deserialize(data.get("presets"), Presets.class);

        return new Config(configuration, groups, presets);
    }
}