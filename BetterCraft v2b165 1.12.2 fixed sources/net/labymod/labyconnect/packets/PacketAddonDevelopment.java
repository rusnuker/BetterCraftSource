// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import net.labymod.utils.GZIPCompression;
import java.util.UUID;

public class PacketAddonDevelopment extends Packet
{
    private UUID sender;
    private UUID[] receivers;
    private String key;
    private byte[] data;
    
    public PacketAddonDevelopment(final UUID sender, final String key, final byte[] data) {
        this.sender = sender;
        this.key = key;
        this.data = GZIPCompression.compress(data);
        this.receivers = new UUID[0];
    }
    
    public PacketAddonDevelopment(final UUID sender, final UUID[] receivers, final String key, final byte[] data) {
        this.sender = sender;
        this.receivers = receivers;
        this.key = key;
        this.data = GZIPCompression.compress(data);
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.sender = new UUID(buf.readLong(), buf.readLong());
        final short i = buf.readShort();
        this.receivers = new UUID[i];
        for (int j = 0; j < this.receivers.length; ++j) {
            this.receivers[j] = new UUID(buf.readLong(), buf.readLong());
        }
        this.key = buf.readString();
        final byte[] abyte = new byte[buf.readInt()];
        buf.readBytes(abyte);
        this.data = abyte;
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeLong(this.sender.getMostSignificantBits());
        buf.writeLong(this.sender.getLeastSignificantBits());
        buf.writeShort(this.receivers.length);
        UUID[] receivers;
        for (int length = (receivers = this.receivers).length, i = 0; i < length; ++i) {
            final UUID uuid = receivers[i];
            buf.writeLong(uuid.getMostSignificantBits());
            buf.writeLong(uuid.getLeastSignificantBits());
        }
        buf.writeString(this.key);
        buf.writeInt(this.data.length);
        buf.writeBytes(this.data);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
    }
    
    public byte[] getData() {
        return GZIPCompression.decompress(this.data);
    }
    
    public UUID getSender() {
        return this.sender;
    }
    
    public UUID[] getReceivers() {
        return this.receivers;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public PacketAddonDevelopment() {
    }
}
