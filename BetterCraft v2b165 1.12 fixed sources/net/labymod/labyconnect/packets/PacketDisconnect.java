// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public class PacketDisconnect extends Packet
{
    private String reason;
    
    public PacketDisconnect() {
    }
    
    public PacketDisconnect(final String reason) {
        this.reason = reason;
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.reason = buf.readString();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        if (this.getReason() == null) {
            buf.writeString("Client Error");
        }
        else {
            buf.writeString(this.getReason());
        }
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public String getReason() {
        return this.reason;
    }
}
