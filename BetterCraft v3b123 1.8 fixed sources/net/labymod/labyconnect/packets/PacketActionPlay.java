// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import java.beans.ConstructorProperties;
import net.labymod.labyconnect.handling.PacketHandler;

public class PacketActionPlay extends Packet
{
    private short requestId;
    private short actionType;
    private byte[] data;
    
    @Override
    public void read(final PacketBuf buf) {
        this.requestId = buf.readShort();
        this.actionType = buf.readShort();
        final int length = buf.readVarIntFromBuffer();
        if (length > 1024) {
            throw new RuntimeException("data array too big");
        }
        buf.readBytes(this.data = new byte[length]);
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeShort(this.requestId);
        buf.writeShort(this.actionType);
        if (this.data == null) {
            buf.writeVarIntToBuffer(0);
        }
        else {
            buf.writeVarIntToBuffer(this.data.length);
            buf.writeBytes(this.data);
        }
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
    }
    
    public short getRequestId() {
        return this.requestId;
    }
    
    public short getActionType() {
        return this.actionType;
    }
    
    public byte[] getData() {
        return this.data;
    }
    
    @ConstructorProperties({ "requestId", "actionType", "data" })
    public PacketActionPlay(final short requestId, final short actionType, final byte[] data) {
        this.requestId = requestId;
        this.actionType = actionType;
        this.data = data;
    }
    
    public PacketActionPlay() {
    }
}
