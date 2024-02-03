// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import net.labymod.labyconnect.user.ChatUser;
import net.labymod.labyconnect.user.ChatRequest;

public class PacketPlayDenyFriendRequest extends Packet
{
    private ChatRequest denied;
    
    public PacketPlayDenyFriendRequest(final ChatRequest denied) {
        this.denied = denied;
    }
    
    public PacketPlayDenyFriendRequest() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.denied = (ChatRequest)buf.readChatUser();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeChatUser(this.denied);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public ChatRequest getDenied() {
        return this.denied;
    }
}
