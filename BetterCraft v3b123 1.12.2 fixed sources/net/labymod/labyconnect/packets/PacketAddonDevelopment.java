// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.main.LabyMod;
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
        final int receiverCnt = buf.readShort();
        this.receivers = new UUID[receiverCnt];
        for (int i = 0; i < this.receivers.length; ++i) {
            this.receivers[i] = new UUID(buf.readLong(), buf.readLong());
        }
        this.key = buf.readString();
        final byte[] data = new byte[buf.readInt()];
        buf.readBytes(data);
        this.data = data;
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeLong(this.sender.getMostSignificantBits());
        buf.writeLong(this.sender.getLeastSignificantBits());
        buf.writeShort(this.receivers.length);
        UUID[] receivers;
        for (int length = (receivers = this.receivers).length, i = 0; i < length; ++i) {
            final UUID receiver = receivers[i];
            buf.writeLong(receiver.getMostSignificantBits());
            buf.writeLong(receiver.getLeastSignificantBits());
        }
        buf.writeString(this.key);
        buf.writeInt(this.data.length);
        buf.writeBytes(this.data);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        LabyMod.getInstance().getEventManager().callAddonDevelopmentPacket(this);
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
