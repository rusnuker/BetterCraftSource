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
    private UserStatus status;
    private LabyMod labyMod;
    
    public ClientProfile(final LabyConnect chatClient, final ClientConnection clientConnection) {
        this.firstJoined = 0L;
        this.status = UserStatus.ONLINE;
        this.labyMod = chatClient.labyMod;
        this.chatClient = chatClient;
        this.clientConnection = clientConnection;
    }
    
    public UserStatus getUserStatus() {
        return this.status;
    }
    
    public void setUserStatus(final UserStatus status) {
        this.status = status;
    }
    
    public TimeZone getTimeZone() {
        return Calendar.getInstance().getTimeZone();
    }
    
    public void sendSettingsToServer() {
        this.clientConnection.sendPacket(new PacketPlayChangeOptions(true, this.getUserStatus(), this.getTimeZone()));
    }
    
    public ChatUser buildClientUser() {
        final GameProfile gameprofile = new GameProfile(this.labyMod.getPlayerUUID(), this.labyMod.getPlayerName());
        return new ChatUser(gameprofile, this.getUserStatus(), this.labyMod.getMotd(), new ServerInfo((this.labyMod.getCurrentServerData() == null) ? "" : this.labyMod.getCurrentServerData().getIp(), (this.labyMod.getCurrentServerData() == null) ? 25565 : this.labyMod.getCurrentServerData().getPort()), 0, System.currentTimeMillis(), this.getTimeZone().getID(), System.currentTimeMillis(), this.firstJoined, this.chatClient.getFriends().size(), false);
    }
    
    public long getFirstJoined() {
        return this.firstJoined;
    }
    
    public void setFirstJoined(final long firstJoined) {
        this.firstJoined = firstJoined;
    }
}
