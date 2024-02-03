// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.user;

import com.mojang.authlib.GameProfile;
import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketPlayChangeOptions;
import java.util.Calendar;
import java.util.TimeZone;
import net.labymod.main.LabyMod;
import net.labymod.labyconnect.LabyConnect;
import net.labymod.labyconnect.ClientConnection;

public class ClientProfile
{
    private ClientConnection clientConnection;
    private LabyConnect chatClient;
    private long firstJoined;
    
    public ClientProfile(final LabyConnect chatClient, final ClientConnection clientConnection) {
        this.firstJoined = 0L;
        this.chatClient = chatClient;
        this.clientConnection = clientConnection;
    }
    
    public UserStatus getUserStatus() {
        return UserStatus.getById(LabyMod.getSettings().onlineStatus);
    }
    
    public void setUserStatus(final UserStatus userStatus) {
        LabyMod.getSettings().onlineStatus = userStatus.getId();
    }
    
    public TimeZone getTimeZone() {
        return Calendar.getInstance().getTimeZone();
    }
    
    public void sendSettingsToServer() {
        this.clientConnection.sendPacket(new PacketPlayChangeOptions(LabyMod.getSettings().showConnectedIp, this.getUserStatus(), this.getTimeZone()));
    }
    
    public ChatUser buildClientUser() {
        final GameProfile gameProfile = new GameProfile(LabyMod.getInstance().getPlayerUUID(), LabyMod.getInstance().getPlayerName());
        return new ChatUser(gameProfile, this.getUserStatus(), LabyMod.getSettings().motd, new ServerInfo((LabyMod.getInstance().getCurrentServerData() == null) ? "" : LabyMod.getInstance().getCurrentServerData().getIp(), (LabyMod.getInstance().getCurrentServerData() == null) ? 25565 : LabyMod.getInstance().getCurrentServerData().getPort()), 0, System.currentTimeMillis(), 0L, this.getTimeZone().getID(), System.currentTimeMillis(), this.firstJoined, this.chatClient.getFriends().size(), false);
    }
    
    public long getFirstJoined() {
        return this.firstJoined;
    }
    
    public void setFirstJoined(final long firstJoined) {
        this.firstJoined = firstJoined;
    }
}
