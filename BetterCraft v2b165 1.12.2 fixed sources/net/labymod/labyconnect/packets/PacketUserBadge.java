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
        final int i = buf.readVarIntFromBuffer();
        this.uuids = new UUID[i];
        for (int j = 0; j < i; ++j) {
            this.uuids[j] = new UUID(buf.readLong(), buf.readLong());
        }
        final byte[] abyte = new byte[i];
        buf.readBytes(abyte);
        this.ranks = abyte;
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
