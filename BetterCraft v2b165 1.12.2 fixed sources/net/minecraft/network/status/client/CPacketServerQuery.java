// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.status.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;
import net.minecraft.network.Packet;

public class CPacketServerQuery implements Packet<INetHandlerStatusServer>
{
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
    }
    
    @Override
    public void processPacket(final INetHandlerStatusServer handler) {
        handler.processServerQuery(this);
    }
}
