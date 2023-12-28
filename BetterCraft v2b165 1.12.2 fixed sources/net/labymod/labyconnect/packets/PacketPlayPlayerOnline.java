// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import net.labymod.labyconnect.user.ChatUser;

public class PacketPlayPlayerOnline extends Packet
{
    private ChatUser newOnlinePlayer;
    
    public PacketPlayPlayerOnline(final ChatUser newOnlinePlayer) {
        this.newOnlinePlayer = newOnlinePlayer;
    }
    
    public PacketPlayPlayerOnline() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.newOnlinePlayer = buf.readChatUser();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeChatUser(this.newOnlinePlayer);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public ChatUser getPlayer() {
        return this.newOnlinePlayer;
    }
}
