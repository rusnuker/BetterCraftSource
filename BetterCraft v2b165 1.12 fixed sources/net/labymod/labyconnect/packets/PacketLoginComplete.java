// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public class PacketLoginComplete extends Packet
{
    private String capeKey;
    
    public PacketLoginComplete(final String string) {
        this.capeKey = string;
    }
    
    public PacketLoginComplete() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.capeKey = buf.readString();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeString(this.capeKey);
    }
    
    public int getId() {
        return 2;
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public String getString() {
        return this.capeKey;
    }
}
