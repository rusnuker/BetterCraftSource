// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import java.util.UUID;

public class PacketActionRequest extends Packet
{
    private UUID uuid;
    
    public PacketActionRequest() {
    }
    
    public PacketActionRequest(final UUID uuid) {
        this.uuid = uuid;
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.uuid = UUID.fromString(buf.readString());
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeString(this.uuid.toString());
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
}
