// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import net.labymod.labyconnect.user.ChatUser;

public class PacketPlayFriendRemove extends Packet
{
    private ChatUser toRemove;
    
    public PacketPlayFriendRemove(final ChatUser toRemove) {
        this.toRemove = toRemove;
    }
    
    public PacketPlayFriendRemove() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.toRemove = buf.readChatUser();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeChatUser(this.toRemove);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public ChatUser getToRemove() {
        return this.toRemove;
    }
}
