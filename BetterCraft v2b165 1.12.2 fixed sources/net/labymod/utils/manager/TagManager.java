// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.manager;

import java.util.HashMap;
import java.util.Iterator;
import net.labymod.utils.ModUtils;
import java.util.Map;
import java.io.File;

public class TagManager
{
    private static ConfigManager<TagConfig> configManager;
    
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
        if (!message.contains("\"clickEvent\"")) {
            for (final Map.Entry<String, String> entry : TagManager.configManager.getSettings().getTags().entrySet()) {
                if (!message.toLowerCase().contains(entry.getKey().toLowerCase())) {
                    continue;
                }
                return message.replaceAll("(?i)" + entry.getKey(), ModUtils.translateAlternateColorCodes('&', entry.getValue()));
            }
        }
        return null;
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
