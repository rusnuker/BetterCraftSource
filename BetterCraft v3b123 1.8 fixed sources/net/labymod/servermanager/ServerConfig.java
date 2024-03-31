// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.servermanager;

import java.util.HashMap;
import com.google.gson.JsonObject;
import java.util.Map;

public class ServerConfig
{
    private Map<String, JsonObject> serverConfigs;
    
    public ServerConfig() {
        this.serverConfigs = new HashMap<String, JsonObject>();
    }
    
    public Map<String, JsonObject> getServerConfigs() {
        return this.serverConfigs;
    }
}
