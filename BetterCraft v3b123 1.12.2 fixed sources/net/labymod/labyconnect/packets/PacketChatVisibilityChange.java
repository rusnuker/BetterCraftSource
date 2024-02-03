// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public class PacketChatVisibilityChange extends Packet
{
    private boolean visible;
    
    @Override
    public void read(final PacketBuf buf) {
        this.visible = buf.readBoolean();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeBoolean(this.visible);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public boolean isVisible() {
        return this.visible;
    }
}
