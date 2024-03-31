// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public class PacketServerMessage extends Packet
{
    private String message;
    
    public PacketServerMessage(final String message) {
        this.message = message;
    }
    
    public PacketServerMessage() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.message = buf.readString();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeString(this.message);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public String getMessage() {
        return this.message;
    }
}
