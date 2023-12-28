// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import net.labymod.labyconnect.user.ServerInfo;
import net.labymod.labyconnect.user.ChatUser;

public class PacketPlayFriendStatus extends Packet
{
    private ChatUser player;
    private ServerInfo playerInfo;
    
    public PacketPlayFriendStatus(final ChatUser player, final ServerInfo playerInfo) {
        this.player = player;
        this.playerInfo = playerInfo;
    }
    
    public PacketPlayFriendStatus() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.player = buf.readChatUser();
        this.playerInfo = buf.readServerInfo();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeChatUser(this.player);
        buf.writeServerInfo(this.playerInfo);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public ChatUser getPlayer() {
        return this.player;
    }
    
    public ServerInfo getPlayerInfo() {
        return this.playerInfo;
    }
}
