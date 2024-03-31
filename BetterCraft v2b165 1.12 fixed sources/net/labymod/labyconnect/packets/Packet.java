// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public abstract class Packet
{
    public abstract void read(final PacketBuf p0);
    
    public abstract void write(final PacketBuf p0);
    
    public abstract void handle(final PacketHandler p0);
}
