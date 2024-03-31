// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api;

import net.labymod.labyconnect.packets.PacketAddonDevelopment;
import java.util.UUID;
import net.labymod.user.UserManager;
import net.labymod.utils.ServerData;
import net.labymod.labyconnect.ClientConnection;
import net.labymod.labyconnect.LabyConnect;
import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketAddonMessage;
import net.labymod.main.LabyMod;

public class LabyModAPI
{
    public static final String LABYMOD_API_CHANNEL = "LMC";
    private LabyMod labyMod;
    private boolean crosshairHidden;
    
    public LabyModAPI(final LabyMod labyMod) {
        this.labyMod = labyMod;
    }
    
    public void sendAddonMessage(final String key, final byte[] data) {
        final LabyConnect labyconnect = this.labyMod.getLabyConnect();
        final ClientConnection clientconnection = labyconnect.getClientConnection();
        if (clientconnection != null && labyconnect.isOnline()) {
            clientconnection.sendPacket(new PacketAddonMessage(key, data));
        }
    }
    
    public void sendAddonMessage(final String key, final String json) {
        final LabyConnect labyconnect = this.labyMod.getLabyConnect();
        final ClientConnection clientconnection = labyconnect.getClientConnection();
        if (clientconnection != null && labyconnect.isOnline()) {
            clientconnection.sendPacket(new PacketAddonMessage(key, json));
        }
    }
    
    public ServerData getCurrentServer() {
        return this.labyMod.getCurrentServerData();
    }
    
    public boolean isCurrentlyPlayingOn(final String address) {
        return this.labyMod.getCurrentServerData() != null && this.labyMod.getCurrentServerData().getIp().toLowerCase().contains(address.toLowerCase());
    }
    
    public boolean isIngame() {
        return this.labyMod.isInGame();
    }
    
    public UserManager getUserManager() {
        return this.labyMod.getUserManager();
    }
    
    public LabyConnect getLabyModChatClient() {
        return this.labyMod.getLabyConnect();
    }
    
    public UUID getPlayerUUID() {
        return this.labyMod.getPlayerUUID();
    }
    
    public String getPlayerUsername() {
        return this.labyMod.getPlayerName();
    }
    
    public void updateCurrentGamemode(final String gamemodeName) {
        this.labyMod.getLabyConnect().updatePlayingOnServerState(gamemodeName);
    }
    
    public void sendAddonDevelopmentPacket(final PacketAddonDevelopment packetAddonDevelopment) {
        this.labyMod.getLabyConnect().getClientConnection().sendPacket(packetAddonDevelopment);
    }
    
    public boolean isCrosshairHidden() {
        return this.crosshairHidden;
    }
    
    public void setCrosshairHidden(final boolean crosshairHidden) {
        this.crosshairHidden = crosshairHidden;
    }
}
