// 
// Decompiled by Procyon v0.6.0
// 

package org.javapluginapi.team.api;

public interface Plugin
{
    public static final ThreadLocal<PluginDescription> descriptionFile = new ThreadLocal<PluginDescription>();
    
    void onEnable();
    
    void onDisable();
    
    default void setDescriptionFile(final PluginDescription descriptionFile) {
        if (Plugin.descriptionFile.get() != null) {
            return;
        }
        Plugin.descriptionFile.set(descriptionFile);
    }
    
    default PluginDescription getDescriptionFile() {
        return Plugin.descriptionFile.get();
    }
}
