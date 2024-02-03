// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.servermanager;

import java.util.Iterator;
import net.lenni0451.eventapi.events.EventTarget;
import net.labymod.support.util.Debug;
import net.labymod.main.LabyMod;
import net.labymod.core.LabyModCore;
import net.minecraft.client.Minecraft;
import me.nzxtercode.bettercraft.client.events.PlayerTickEvent;
import net.labymod.api.LabyModAddon;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import net.minecraft.client.multiplayer.ServerData;
import net.labymod.api.permissions.Permissions;
import java.util.Map;
import java.util.List;
import net.labymod.utils.manager.ConfigManager;

public class ServerManager
{
    private final ConfigManager<ServerConfig> serverConfigManager;
    private final ServerConfig config;
    private final List<Server> servers;
    private final Map<Permissions.Permission, Boolean> permissionMap;
    private Server currentServer;
    private ServerData prevServer;
    private long lastSecond;
    
    public ServerManager() {
        this.servers = new ArrayList<Server>();
        this.permissionMap = new HashMap<Permissions.Permission, Boolean>();
        this.lastSecond = 0L;
        this.serverConfigManager = new ConfigManager<ServerConfig>(new File("LabyMod/", "servers.json"), ServerConfig.class);
        this.config = this.serverConfigManager.getSettings();
    }
    
    public void init() {
    }
    
    public void registerServerSupport(final LabyModAddon labyModAddon, final Server server) {
        server.bindAddon(labyModAddon);
        this.servers.add(server);
    }
    
    @EventTarget
    public void handleEvent(final PlayerTickEvent event) {
        final ServerData currentServerData = Minecraft.getMinecraft().getCurrentServerData();
        if (currentServerData != null && LabyModCore.getMinecraft().getConnection() != null && this.prevServer != currentServerData) {
            this.prevServer = currentServerData;
            this.updateServer(currentServerData.serverIP);
            if (this.currentServer != null) {
                this.currentServer.onJoin(currentServerData);
            }
            LabyMod.getInstance().onJoinServer(currentServerData);
        }
        else if (currentServerData == null && this.prevServer != null) {
            this.reset();
            this.updateServer(null);
            this.prevServer = null;
            Debug.log(Debug.EnumDebugMode.MINECRAFT, "Disconnected from server");
        }
        if (this.lastSecond < System.currentTimeMillis()) {
            this.lastSecond = System.currentTimeMillis() + 1000L;
            if (this.currentServer != null) {
                this.currentServer.loopSecond();
            }
        }
    }
    
    public ChatDisplayAction handleChatMessage(final String clean, final String formatted) {
        try {
            return (this.currentServer == null) ? ChatDisplayAction.NORMAL : this.currentServer.handleChatMessage(clean, formatted);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return ChatDisplayAction.NORMAL;
        }
    }
    
    public void updateServer(final String ip) {
        if (ip == null) {
            this.currentServer = null;
            return;
        }
        for (final Server server : this.servers) {
            String[] addressNames;
            for (int length = (addressNames = server.getAddressNames()).length, i = 0; i < length; ++i) {
                final String addresses = addressNames[i];
                if (ip.toLowerCase().contains(addresses.toLowerCase())) {
                    this.currentServer = server;
                    return;
                }
            }
        }
    }
    
    public void reset() {
        this.permissionMap.clear();
        for (final Server server : this.servers) {
            server.reset();
        }
    }
    
    public void draw() {
        if (this.currentServer == null) {
            return;
        }
        this.currentServer.draw();
    }
    
    public boolean isServer(final Class<? extends Server> serverClass) {
        return this.currentServer != null && this.currentServer.getClass().equals(serverClass);
    }
    
    public boolean isAllowed(final Permissions.Permission permission) {
        if (Minecraft.getMinecraft().isSingleplayer()) {
            return true;
        }
        if (this.currentServer == null) {
            Boolean allowed = this.permissionMap.get(permission);
            if (allowed == null) {
                allowed = permission.isDefaultEnabled();
            }
            return allowed;
        }
        return this.currentServer.isAllowed(permission);
    }
    
    public ConfigManager<ServerConfig> getServerConfigManager() {
        return this.serverConfigManager;
    }
    
    public ServerConfig getConfig() {
        return this.config;
    }
    
    public List<Server> getServers() {
        return this.servers;
    }
    
    public Map<Permissions.Permission, Boolean> getPermissionMap() {
        return this.permissionMap;
    }
    
    public Server getCurrentServer() {
        return this.currentServer;
    }
    
    public void setPrevServer(final ServerData prevServer) {
        this.prevServer = prevServer;
    }
}
