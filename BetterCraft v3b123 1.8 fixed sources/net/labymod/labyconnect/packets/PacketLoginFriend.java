// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import java.util.Collection;
import java.util.ArrayList;
import net.labymod.labyconnect.user.ChatUser;
import java.util.List;

public class PacketLoginFriend extends Packet
{
    private List<ChatUser> friends;
    
    public PacketLoginFriend(final List<ChatUser> friends) {
        this.friends = friends;
    }
    
    public PacketLoginFriend() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        final List<ChatUser> players = new ArrayList<ChatUser>();
        for (int a = buf.readInt(), i = 0; i < a; ++i) {
            players.add(buf.readChatUser());
        }
        (this.friends = new ArrayList<ChatUser>()).addAll(players);
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeInt(this.getFriends().size());
        for (int i = 0; i < this.getFriends().size(); ++i) {
            final ChatUser p = this.getFriends().get(i);
            buf.writeChatUser(p);
        }
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public List<ChatUser> getFriends() {
        return this.friends;
    }
}
