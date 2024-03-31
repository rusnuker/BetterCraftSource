// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import net.labymod.labyconnect.user.ChatUser;

public class PacketLoginTime extends Packet
{
    private ChatUser player;
    private long dateJoined;
    private long lastOnline;
    
    public PacketLoginTime(final ChatUser player, final long dateJoined, final long lastOnline) {
        this.player = player;
        this.dateJoined = dateJoined;
        this.lastOnline = lastOnline;
    }
    
    public PacketLoginTime() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.player = buf.readChatUser();
        this.dateJoined = buf.readLong();
        this.lastOnline = buf.readLong();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeChatUser(this.player);
        buf.writeLong(this.dateJoined);
        buf.writeLong(this.lastOnline);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public long getDateJoined() {
        return this.dateJoined;
    }
    
    public long getLastOnline() {
        return this.lastOnline;
    }
    
    public ChatUser getPlayer() {
        return this.player;
    }
}
