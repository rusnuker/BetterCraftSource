// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketSelectAdvancementsTab implements Packet<INetHandlerPlayClient>
{
    @Nullable
    private ResourceLocation field_194155_a;
    
    public SPacketSelectAdvancementsTab() {
    }
    
    public SPacketSelectAdvancementsTab(@Nullable final ResourceLocation p_i47596_1_) {
        this.field_194155_a = p_i47596_1_;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.func_194022_a(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        if (buf.readBoolean()) {
            this.field_194155_a = buf.func_192575_l();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBoolean(this.field_194155_a != null);
        if (this.field_194155_a != null) {
            buf.func_192572_a(this.field_194155_a);
        }
    }
    
    @Nullable
    public ResourceLocation func_194154_a() {
        return this.field_194155_a;
    }
}
