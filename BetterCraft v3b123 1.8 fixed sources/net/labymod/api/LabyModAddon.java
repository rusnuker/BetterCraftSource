// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api;

import java.io.File;
import net.labymod.addon.AddonLoader;
import java.util.UUID;
import java.util.ArrayList;
import net.labymod.addon.About;
import net.labymod.addon.AddonConfig;
import net.labymod.utils.manager.ConfigManager;
import com.google.gson.JsonObject;
import net.labymod.settings.elements.SettingsElement;
import java.util.List;

public abstract class LabyModAddon
{
    private List<SettingsElement> subSettings;
    private JsonObject config;
    private ConfigManager<AddonConfig> configManager;
    public About about;
    public LabyModAPI api;
    
    public LabyModAddon() {
        this.subSettings = new ArrayList<SettingsElement>();
    }
    
    public abstract void onEnable();
    
    @Deprecated
    public void onDisable() {
    }
    
    public abstract void loadConfig();
    
    protected abstract void fillSettings(final List<SettingsElement> p0);
    
    public void init(final String addonName, final UUID uuid) {
        this.about = new About(uuid, addonName);
        this.configManager = new ConfigManager<AddonConfig>(new File(AddonLoader.getConfigDirectory(), String.valueOf(addonName) + ".json"), AddonConfig.class);
        this.config = this.configManager.getSettings().getConfig();
        this.loadConfig();
        this.fillSettings(this.subSettings);
        this.about.loaded = true;
    }
    
    public void saveConfig() {
        if (this.configManager != null) {
            this.configManager.save();
        }
    }
    
    public LabyModAPI getApi() {
        return this.api;
    }
    
    public void onRenderPreview(final int mouseX, final int mouseY, final float partialTicks) {
    }
    
    public void onMouseClickedPreview(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    public List<SettingsElement> getSubSettings() {
        return this.subSettings;
    }
    
    public JsonObject getConfig() {
        return this.config;
    }
}
