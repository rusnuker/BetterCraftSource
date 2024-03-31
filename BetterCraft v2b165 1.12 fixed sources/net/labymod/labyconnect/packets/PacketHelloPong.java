// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public class PacketHelloPong extends Packet
{
    private long a;
    
    public PacketHelloPong() {
    }
    
    public PacketHelloPong(final long a) {
        this.a = a;
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.a = buf.readLong();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeLong(this.a);
    }
    
    public int getId() {
        return 1;
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
}
