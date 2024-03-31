// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import java.util.UUID;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketSpectate implements Packet<INetHandlerPlayServer>
{
    private UUID id;
    
    public CPacketSpectate() {
    }
    
    public CPacketSpectate(final UUID uniqueIdIn) {
        this.id = uniqueIdIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.id = buf.readUuid();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeUuid(this.id);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.handleSpectate(this);
    }
    
    @Nullable
    public Entity getEntity(final WorldServer worldIn) {
        return worldIn.getEntityFromUuid(this.id);
    }
}
