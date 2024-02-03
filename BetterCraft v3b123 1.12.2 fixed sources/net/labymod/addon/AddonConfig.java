// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addon;

import com.google.gson.JsonObject;

public class AddonConfig
{
    private JsonObject config;
    
    public AddonConfig() {
        this.config = new JsonObject();
    }
    
    public JsonObject getConfig() {
        return this.config;
    }
}
