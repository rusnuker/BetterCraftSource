// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.user.User;
import net.labymod.utils.Consumer;
import java.nio.charset.StandardCharsets;
import net.labymod.main.LabyMod;
import net.labymod.core.LabyModCore;
import net.labymod.labyconnect.handling.PacketHandler;
import java.util.UUID;

public class PacketActionRequestResponse extends Packet
{
    private UUID uuid;
    private short actionId;
    private byte[] data;
    
    public PacketActionRequestResponse() {
    }
    
    public PacketActionRequestResponse(final UUID uuid, final short actionId, final byte[] data) {
        this.uuid = uuid;
        this.actionId = actionId;
        this.data = data;
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.uuid = UUID.fromString(buf.readString());
        this.actionId = buf.readShort();
        final int length = buf.readVarIntFromBuffer();
        if (length > 1024) {
            throw new RuntimeException("data array too big");
        }
        buf.readBytes(this.data = new byte[length]);
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
            case 1: {
                if (LabyModCore.getMinecraft().getPlayer() != null && LabyModCore.getMinecraft().getPlayer().getUniqueID().equals(this.uuid)) {
                    break;
                }
                LabyMod.getInstance().getEmoteRegistry().handleEmote(this.uuid, this.data);
                break;
            }
            case 2: {
                LabyMod.getInstance().getUserManager().updateUsersJson(this.uuid, new String(this.data, StandardCharsets.UTF_8), null);
                break;
            }
            case 3: {
                if (LabyModCore.getMinecraft().getPlayer() != null && LabyModCore.getMinecraft().getPlayer().getUniqueID().equals(this.uuid)) {
                    break;
                }
                final User user = LabyMod.getInstance().getUserManager().getUser(this.uuid);
                LabyMod.getInstance().getStickerRegistry().handleSticker(user, LabyMod.getInstance().getStickerRegistry().bytesToShort(this.data));
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
