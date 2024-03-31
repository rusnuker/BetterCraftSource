// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.manager;

import java.util.HashMap;
import net.labymod.core.LabyModCore;
import net.labymod.api.events.MessageModifyChatEvent;
import net.labymod.main.LabyMod;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.Iterator;
import net.labymod.utils.ModUtils;
import java.util.regex.Pattern;
import java.util.Map;
import net.labymod.api.permissions.Permissions;
import java.io.File;
import com.google.gson.JsonParser;

public class TagManager
{
    private static ConfigManager<TagConfig> configManager;
    private static JsonParser jsonParser;
    
    static {
        TagManager.jsonParser = new JsonParser();
    }
    
    public static void init() {
        TagManager.configManager = new ConfigManager<TagConfig>(new File("LabyMod/", "tags.json"), TagConfig.class);
    }
    
    public static void save() {
        TagManager.configManager.save();
    }
    
    public static ConfigManager<TagConfig> getConfigManager() {
        return TagManager.configManager;
    }
    
    public static String getTaggedMessage(final String message) {
        if (!Permissions.isAllowed(Permissions.Permission.TAGS)) {
            return null;
        }
        try {
            for (final Map.Entry<String, String> tagEntry : TagManager.configManager.getSettings().getTags().entrySet()) {
                if (message.toLowerCase().contains(tagEntry.getKey().toLowerCase())) {
                    final String regex = "(?i)" + tagEntry.getKey();
                    final Pattern pattern = Pattern.compile(regex);
                    final String replacement = ModUtils.translateAlternateColorCodes('&', tagEntry.getValue());
                    if ((message.startsWith("[") || message.startsWith("{")) && (message.endsWith("]") || message.endsWith("}"))) {
                        final JsonElement element = TagManager.jsonParser.parse(message);
                        replaceObject(element, pattern, replacement);
                        return element.toString();
                    }
                    return pattern.matcher(message).replaceAll(replacement);
                }
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
        return null;
    }
    
    private static void replaceObject(final JsonElement element, final Pattern pattern, final String replacement) {
        if (element.isJsonArray()) {
            final JsonArray jsonArray = element.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); ++i) {
                replaceObject(jsonArray.get(i), pattern, replacement);
            }
        }
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has("extra")) {
                replaceObject(object.get("extra"), pattern, replacement);
            }
            if (object.has("with")) {
                replaceObject(object.get("with"), pattern, replacement);
            }
            if (object.has("text")) {
                String text = object.get("text").getAsString();
                text = pattern.matcher(text).replaceAll(replacement);
                object.addProperty("text", text);
            }
        }
    }
    
    public static Object tagComponent(Object chatComponent) {
        for (final MessageModifyChatEvent a : LabyMod.getInstance().getEventManager().getMessageModifyChat()) {
            chatComponent = a.onModifyChatMessage(chatComponent);
        }
        if (getConfigManager() == null || getConfigManager().getSettings().getTags().isEmpty() || !Permissions.isAllowed(Permissions.Permission.TAGS)) {
            return chatComponent;
        }
        return LabyModCore.getMinecraft().getTaggedChatComponent(chatComponent);
    }
    
    public static class TagConfig
    {
        private Map<String, String> tags;
        
        public TagConfig() {
            this.tags = new HashMap<String, String>();
        }
        
        public Map<String, String> getTags() {
            return this.tags;
        }
    }
}
