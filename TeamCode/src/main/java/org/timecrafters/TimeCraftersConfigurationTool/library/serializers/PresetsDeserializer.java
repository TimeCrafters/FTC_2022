package org.timecrafters.TimeCraftersConfigurationTool.library.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Action;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Group;
import org.timecrafters.TimeCraftersConfigurationTool.library.backend.config.Presets;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PresetsDeserializer implements JsonDeserializer<Presets> {
    @Override
    public Presets deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Group[] GroupsArray = context.deserialize(jsonObject.get("groups"), Group[].class);
        Action[] actionsArray = context.deserialize(jsonObject.get("actions"), Action[].class);

        List<Group> groupsList = Arrays.asList(GroupsArray);
        ArrayList<Group> groups = new ArrayList<>(groupsList);
        List<Action> actionsList = Arrays.asList(actionsArray);
        ArrayList<Action> actions = new ArrayList<>(actionsList);

        return new Presets(groups, actions);
    }
}