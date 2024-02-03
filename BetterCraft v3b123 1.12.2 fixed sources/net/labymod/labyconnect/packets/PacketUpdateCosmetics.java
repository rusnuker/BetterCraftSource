// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import java.beans.ConstructorProperties;
import net.labymod.labyconnect.handling.PacketHandler;

public class PacketUpdateCosmetics extends Packet
{
    private String json;
    
    public PacketUpdateCosmetics() {
        this.json = null;
    }
    
    @Override
    public void read(final PacketBuf buf) {
        final boolean hasJsonString = buf.readBoolean();
        if (hasJsonString) {
            this.json = buf.readString();
        }
    }
    
    @Override
    public void write(final PacketBuf buf) {
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    @ConstructorProperties({ "json" })
    public PacketUpdateCosmetics(final String json) {
        this.json = null;
        this.json = json;
    }
    
    public String getJson() {
        return this.json;
    }
}
