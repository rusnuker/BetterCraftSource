// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketResourcePackStatus implements Packet<INetHandlerPlayServer>
{
    private Action action;
    
    public CPacketResourcePackStatus() {
    }
    
    public CPacketResourcePackStatus(final Action p_i47156_1_) {
        this.action = p_i47156_1_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.action = buf.readEnumValue(Action.class);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.action);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.handleResourcePackStatus(this);
    }
    
    public enum Action
    {
        SUCCESSFULLY_LOADED("SUCCESSFULLY_LOADED", 0), 
        DECLINED("DECLINED", 1), 
        FAILED_DOWNLOAD("FAILED_DOWNLOAD", 2), 
        ACCEPTED("ACCEPTED", 3);
        
        private Action(final String s, final int n) {
        }
    }
}
