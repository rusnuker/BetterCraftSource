// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core;

import net.minecraft.client.multiplayer.ServerData;

public class ServerPingerData
{
    private String ipAddress;
    private long timePinged;
    private String serverName;
    private String motd;
    private int currentPlayers;
    private int maxPlayers;
    private String base64EncodedIconData;
    public int version;
    public String gameVersion;
    private String playerList;
    public long pingToServer;
    private boolean pinging;
    
    public ServerPingerData(final String ipAddress, final long timePinged) {
        this.motd = "";
        this.gameVersion = "";
        this.playerList = "";
        this.pingToServer = -2L;
        this.pinging = false;
        this.ipAddress = ipAddress;
        this.timePinged = timePinged;
    }
    
    public ServerData toMCServerData() {
        return new ServerData(this.serverName, this.ipAddress, false);
    }
    
    public String getIpAddress() {
        return this.ipAddress;
    }
    
    public long getTimePinged() {
        return this.timePinged;
    }
    
    public String getServerName() {
        return this.serverName;
    }
    
    public String getMotd() {
        return this.motd;
    }
    
    public int getCurrentPlayers() {
        return this.currentPlayers;
    }
    
    public int getMaxPlayers() {
        return this.maxPlayers;
    }
    
    public String getBase64EncodedIconData() {
        return this.base64EncodedIconData;
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public String getGameVersion() {
        return this.gameVersion;
    }
    
    public String getPlayerList() {
        return this.playerList;
    }
    
    public long getPingToServer() {
        return this.pingToServer;
    }
    
    public boolean isPinging() {
        return this.pinging;
    }
    
    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public void setTimePinged(final long timePinged) {
        this.timePinged = timePinged;
    }
    
    public void setServerName(final String serverName) {
        this.serverName = serverName;
    }
    
    public void setMotd(final String motd) {
        this.motd = motd;
    }
    
    public void setCurrentPlayers(final int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }
    
    public void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
    
    public void setBase64EncodedIconData(final String base64EncodedIconData) {
        this.base64EncodedIconData = base64EncodedIconData;
    }
    
    public void setVersion(final int version) {
        this.version = version;
    }
    
    public void setGameVersion(final String gameVersion) {
        this.gameVersion = gameVersion;
    }
    
    public void setPlayerList(final String playerList) {
        this.playerList = playerList;
    }
    
    public void setPingToServer(final long pingToServer) {
        this.pingToServer = pingToServer;
    }
    
    public void setPinging(final boolean pinging) {
        this.pinging = pinging;
    }
}
