// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import net.labymod.labyconnect.user.ChatUser;
import java.util.ArrayList;
import net.labymod.labyconnect.user.ChatRequest;
import java.util.List;

public class PacketLoginRequest extends Packet
{
    private List<ChatRequest> requesters;
    
    public PacketLoginRequest(final List<ChatRequest> requesters) {
        this.requesters = requesters;
    }
    
    public PacketLoginRequest() {
    }
    
    public List<ChatRequest> getRequests() {
        return this.requesters;
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.requesters = new ArrayList<ChatRequest>();
        for (int i = buf.readInt(), j = 0; j < i; ++j) {
            this.requesters.add((ChatRequest)buf.readChatUser());
        }
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeInt(this.getRequests().size());
        for (int i = 0; i < this.getRequests().size(); ++i) {
            buf.writeChatUser(this.getRequests().get(i));
        }
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
}
