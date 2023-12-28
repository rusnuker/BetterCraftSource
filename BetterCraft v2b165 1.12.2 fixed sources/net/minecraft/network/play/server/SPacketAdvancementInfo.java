// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import com.google.common.collect.Sets;
import net.minecraft.network.PacketBuffer;
import java.util.Iterator;
import com.google.common.collect.Maps;
import java.util.Collection;
import net.minecraft.advancements.AdvancementProgress;
import java.util.Set;
import net.minecraft.advancements.Advancement;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketAdvancementInfo implements Packet<INetHandlerPlayClient>
{
    private boolean field_192605_a;
    private Map<ResourceLocation, Advancement.Builder> field_192606_b;
    private Set<ResourceLocation> field_192607_c;
    private Map<ResourceLocation, AdvancementProgress> field_192608_d;
    
    public SPacketAdvancementInfo() {
    }
    
    public SPacketAdvancementInfo(final boolean p_i47519_1_, final Collection<Advancement> p_i47519_2_, final Set<ResourceLocation> p_i47519_3_, final Map<ResourceLocation, AdvancementProgress> p_i47519_4_) {
        this.field_192605_a = p_i47519_1_;
        this.field_192606_b = (Map<ResourceLocation, Advancement.Builder>)Maps.newHashMap();
        for (final Advancement advancement : p_i47519_2_) {
            this.field_192606_b.put(advancement.func_192067_g(), advancement.func_192075_a());
        }
        this.field_192607_c = p_i47519_3_;
        this.field_192608_d = (Map<ResourceLocation, AdvancementProgress>)Maps.newHashMap((Map<?, ?>)p_i47519_4_);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.func_191981_a(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.field_192605_a = buf.readBoolean();
        this.field_192606_b = (Map<ResourceLocation, Advancement.Builder>)Maps.newHashMap();
        this.field_192607_c = (Set<ResourceLocation>)Sets.newLinkedHashSet();
        this.field_192608_d = (Map<ResourceLocation, AdvancementProgress>)Maps.newHashMap();
        for (int i = buf.readVarIntFromBuffer(), j = 0; j < i; ++j) {
            final ResourceLocation resourcelocation = buf.func_192575_l();
            final Advancement.Builder advancement$builder = Advancement.Builder.func_192060_b(buf);
            this.field_192606_b.put(resourcelocation, advancement$builder);
        }
        for (int i = buf.readVarIntFromBuffer(), k = 0; k < i; ++k) {
            final ResourceLocation resourcelocation2 = buf.func_192575_l();
            this.field_192607_c.add(resourcelocation2);
        }
        for (int i = buf.readVarIntFromBuffer(), l = 0; l < i; ++l) {
            final ResourceLocation resourcelocation3 = buf.func_192575_l();
            this.field_192608_d.put(resourcelocation3, AdvancementProgress.func_192100_b(buf));
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBoolean(this.field_192605_a);
        buf.writeVarIntToBuffer(this.field_192606_b.size());
        for (final Map.Entry<ResourceLocation, Advancement.Builder> entry : this.field_192606_b.entrySet()) {
            final ResourceLocation resourcelocation = entry.getKey();
            final Advancement.Builder advancement$builder = entry.getValue();
            buf.func_192572_a(resourcelocation);
            advancement$builder.func_192057_a(buf);
        }
        buf.writeVarIntToBuffer(this.field_192607_c.size());
        for (final ResourceLocation resourcelocation2 : this.field_192607_c) {
            buf.func_192572_a(resourcelocation2);
        }
        buf.writeVarIntToBuffer(this.field_192608_d.size());
        for (final Map.Entry<ResourceLocation, AdvancementProgress> entry2 : this.field_192608_d.entrySet()) {
            buf.func_192572_a(entry2.getKey());
            entry2.getValue().func_192104_a(buf);
        }
    }
    
    public Map<ResourceLocation, Advancement.Builder> func_192603_a() {
        return this.field_192606_b;
    }
    
    public Set<ResourceLocation> func_192600_b() {
        return this.field_192607_c;
    }
    
    public Map<ResourceLocation, AdvancementProgress> func_192604_c() {
        return this.field_192608_d;
    }
    
    public boolean func_192602_d() {
        return this.field_192605_a;
    }
}
