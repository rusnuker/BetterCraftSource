// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.servermanager;

import net.labymod.core.LabyModCore;
import io.netty.buffer.Unpooled;
import net.labymod.utils.Consumer;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.api.permissions.Permissions;
import java.util.List;
import net.minecraft.client.multiplayer.ServerData;
import java.util.Map;
import net.minecraft.network.PacketBuffer;
import net.labymod.api.events.PluginMessageEvent;
import net.labymod.api.events.TabListEvent;
import net.labymod.main.LabyMod;
import net.labymod.api.LabyModAddon;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class Server
{
    protected JsonParser jsonParser;
    private String name;
    private String[] addressNames;
    protected int kills;
    private JsonObject config;
    private LabyModAddon bindedAddon;
    
    public Server(final String name, final String... addressNames) {
        this.jsonParser = new JsonParser();
        this.name = name;
        this.addressNames = addressNames;
        LabyMod.getInstance().getEventManager().register(new TabListEvent() {
            @Override
            public void onUpdate(final Type type, final String formattedText, final String unformattedText) {
                if (!LabyMod.getInstance().getServerManager().isServer(Server.this.getClass())) {
                    return;
                }
                try {
                    Server.this.handleTabInfoMessage(type, formattedText, unformattedText);
                }
                catch (final Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        LabyMod.getInstance().getEventManager().register(new PluginMessageEvent() {
            @Override
            public void receiveMessage(final String channelName, final PacketBuffer packetBuffer) {
                try {
                    Server.this.handlePluginMessage(channelName, packetBuffer);
                }
                catch (final Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        final Map<String, JsonObject> list = LabyMod.getInstance().getServerManager().getServerConfigManager().getSettings().getServerConfigs();
        if (list.keySet().contains(name)) {
            this.config = list.get(name);
        }
        else {
            this.config = new JsonObject();
            LabyMod.getInstance().getServerManager().getConfig().getServerConfigs().put(name, this.config);
            LabyMod.getInstance().getServerManager().getServerConfigManager().save();
        }
        this.initConfig(this.config);
        this.loadConfig();
    }
    
    public abstract void onJoin(final ServerData p0);
    
    public abstract ChatDisplayAction handleChatMessage(final String p0, final String p1) throws Exception;
    
    public abstract void handlePluginMessage(final String p0, final PacketBuffer p1) throws Exception;
    
    public abstract void handleTabInfoMessage(final TabListEvent.Type p0, final String p1, final String p2) throws Exception;
    
    public void reset() {
        this.kills = 0;
    }
    
    public void draw() {
    }
    
    public void addModuleLines(final List<DisplayLine> lines) {
    }
    
    public void loopSecond() {
    }
    
    public boolean isAllowed(final Permissions.Permission permission) {
        return permission.isDefaultEnabled();
    }
    
    @Deprecated
    protected void initConfig(final JsonObject config) {
    }
    
    public void loadConfig() {
    }
    
    public void saveConfig() {
        LabyMod.getInstance().getServerManager().getConfig().getServerConfigs().put(this.name, this.config);
        LabyMod.getInstance().getServerManager().getServerConfigManager().save();
    }
    
    public abstract void fillSubSettings(final List<SettingsElement> p0);
    
    public void bindAddon(final LabyModAddon labyModAddon) {
        this.bindedAddon = labyModAddon;
        if (this.bindedAddon != null) {
            this.fillSubSettings(this.bindedAddon.getSubSettings());
        }
    }
    
    public boolean getBooleanAttribute(final String attribute, final boolean defaultValue) {
        if (!this.config.has(attribute)) {
            this.config.addProperty(attribute, defaultValue);
        }
        return this.config.get(attribute).getAsBoolean();
    }
    
    public void setBooleanAttribute(final String attribute, final boolean value) {
        this.config.addProperty(attribute, value);
    }
    
    public void sendPluginMessage(final String channelName, final Consumer<PacketBuffer> packetBufferConsumer) {
        final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBufferConsumer.accept(packetBuffer);
        LabyModCore.getMinecraft().sendPluginMessage(channelName, packetBuffer);
    }
    
    public String getName() {
        return this.name;
    }
    
    public String[] getAddressNames() {
        return this.addressNames;
    }
    
    public JsonObject getConfig() {
        return this.config;
    }
    
    public LabyModAddon getBindedAddon() {
        return this.bindedAddon;
    }
    
    public static class DisplayLine
    {
        private String key;
        
        public String getKey() {
            return this.key;
        }
    }
}
