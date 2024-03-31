// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public class PacketKick extends Packet
{
    private String cause;
    
    public PacketKick(final String cause) {
        this.cause = cause;
    }
    
    public PacketKick() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.cause = buf.readString();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeString(this.getReason());
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public String getReason() {
        return this.cause;
    }
}
