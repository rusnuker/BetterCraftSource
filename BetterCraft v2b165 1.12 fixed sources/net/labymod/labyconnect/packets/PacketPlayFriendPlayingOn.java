// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import net.labymod.labyconnect.user.ChatUser;

public class PacketPlayFriendPlayingOn extends Packet
{
    private ChatUser player;
    private String gameModeName;
    
    public PacketPlayFriendPlayingOn(final ChatUser player, final String gameModeName) {
        this.player = player;
        this.gameModeName = gameModeName;
    }
    
    public PacketPlayFriendPlayingOn() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.player = buf.readChatUser();
        this.gameModeName = buf.readString();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeChatUser(this.player);
        buf.writeString(this.gameModeName);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public String getGameModeName() {
        return this.gameModeName;
    }
    
    public ChatUser getPlayer() {
        return this.player;
    }
}
