// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public class PacketActionPlayResponse extends Packet
{
    private short requestId;
    private boolean allowed;
    private String reason;
    
    public PacketActionPlayResponse() {
    }
    
    public PacketActionPlayResponse(final boolean allowed) {
        this.allowed = allowed;
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.requestId = buf.readShort();
        if (!(this.allowed = buf.readBoolean())) {
            this.reason = buf.readString();
        }
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeShort(this.requestId);
        buf.writeBoolean(this.allowed);
        if (!this.allowed) {
            buf.writeString(this.reason);
        }
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
    }
    
    public boolean isAllowed() {
        return this.allowed;
    }
    
    public short getRequestId() {
        return this.requestId;
    }
    
    public String getReason() {
        return this.reason;
    }
}
