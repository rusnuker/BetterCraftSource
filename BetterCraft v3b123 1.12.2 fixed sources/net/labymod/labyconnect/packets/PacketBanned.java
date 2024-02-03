// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public class PacketBanned extends Packet
{
    private String reason;
    private long until;
    
    public PacketBanned(final String reason, final long until) {
        this.reason = reason;
        this.until = until;
    }
    
    public PacketBanned() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.reason = buf.readString();
        this.until = buf.readLong();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeString(this.reason);
        buf.writeLong(this.until);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public String getReason() {
        return this.reason;
    }
    
    public long getUntil() {
        return this.until;
    }
}
