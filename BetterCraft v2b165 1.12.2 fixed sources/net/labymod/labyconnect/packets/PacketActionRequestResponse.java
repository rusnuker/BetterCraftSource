// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.utils.Consumer;
import java.nio.charset.StandardCharsets;
import net.labymod.labyconnect.handling.PacketHandler;
import net.labymod.main.LabyMod;
import java.util.UUID;

public class PacketActionRequestResponse extends Packet
{
    private UUID uuid;
    private short actionId;
    private byte[] data;
    public LabyMod labyMod;
    
    public PacketActionRequestResponse() {
    }
    
    public PacketActionRequestResponse(final LabyMod labyMod, final UUID uuid, final short actionId, final byte[] data) {
        this.labyMod = labyMod;
        this.uuid = uuid;
        this.actionId = actionId;
        this.data = data;
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.uuid = UUID.fromString(buf.readString());
        this.actionId = buf.readShort();
        final int i = buf.readVarIntFromBuffer();
        if (i > 1024) {
            throw new RuntimeException("data array too big");
        }
        buf.readBytes(this.data = new byte[i]);
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeString(this.uuid.toString());
        buf.writeShort(this.actionId);
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
        switch (this.actionId) {
            case 2: {
                this.labyMod.getUserManager().updateUsersJson(this.uuid, new String(this.data, StandardCharsets.UTF_8), null);
                break;
            }
        }
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public short getActionId() {
        return this.actionId;
    }
    
    public byte[] getData() {
        return this.data;
    }
}
