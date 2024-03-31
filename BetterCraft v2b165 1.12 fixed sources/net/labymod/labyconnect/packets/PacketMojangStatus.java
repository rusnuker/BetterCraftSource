// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public class PacketMojangStatus extends Packet
{
    @Override
    public void read(final PacketBuf buf) {
        buf.readInt();
        buf.readString();
    }
    
    @Override
    public void write(final PacketBuf buf) {
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
}
