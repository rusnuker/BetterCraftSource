// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public class PacketPlayRequestRemove extends Packet
{
    private String playerName;
    
    public PacketPlayRequestRemove(final String playerName) {
        this.playerName = playerName;
    }
    
    public PacketPlayRequestRemove() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.playerName = buf.readString();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeString(this.playerName);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public String getPlayerName() {
        return this.playerName;
    }
}
