// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import java.util.UUID;

public class PacketUserBadge extends Packet
{
    private UUID[] uuids;
    private byte[] ranks;
    
    public PacketUserBadge(final UUID[] uuids) {
        this.uuids = uuids;
    }
    
    @Override
    public void read(final PacketBuf buf) {
        final int size = buf.readVarIntFromBuffer();
        this.uuids = new UUID[size];
        for (int i = 0; i < size; ++i) {
            this.uuids[i] = new UUID(buf.readLong(), buf.readLong());
        }
        final byte[] bytes = new byte[size];
        buf.readBytes(bytes);
        this.ranks = bytes;
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeVarIntToBuffer(this.uuids.length);
        for (int i = 0; i < this.uuids.length; ++i) {
            final UUID uuid = this.uuids[i];
            buf.writeLong(uuid.getMostSignificantBits());
            buf.writeLong(uuid.getLeastSignificantBits());
        }
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public UUID[] getUuids() {
        return this.uuids;
    }
    
    public byte[] getRanks() {
        return this.ranks;
    }
    
    public PacketUserBadge() {
    }
}
