// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public class PacketHelloPing extends Packet
{
    private long a;
    
    public PacketHelloPing() {
    }
    
    public PacketHelloPing(final long a) {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.a = buf.readLong();
        buf.readInt();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeLong(this.a);
        buf.writeInt(21);
    }
    
    public int getId() {
        return 0;
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
}
