// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class ConstructBeaconTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192181_a;
    private final Map<PlayerAdvancements, Listeners> field_192182_b;
    
    static {
        field_192181_a = new ResourceLocation("construct_beacon");
    }
    
    public ConstructBeaconTrigger() {
        this.field_192182_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return ConstructBeaconTrigger.field_192181_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners constructbeacontrigger$listeners = this.field_192182_b.get(p_192165_1_);
        if (constructbeacontrigger$listeners == null) {
            constructbeacontrigger$listeners = new Listeners(p_192165_1_);
            this.field_192182_b.put(p_192165_1_, constructbeacontrigger$listeners);
        }
        constructbeacontrigger$listeners.func_192355_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners constructbeacontrigger$listeners = this.field_192182_b.get(p_192164_1_);
        if (constructbeacontrigger$listeners != null) {
            constructbeacontrigger$listeners.func_192353_b(p_192164_2_);
            if (constructbeacontrigger$listeners.func_192354_a()) {
                this.field_192182_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192182_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(p_192166_1_.get("level"));
        return new Instance(minmaxbounds);
    }
    
    public void func_192180_a(final EntityPlayerMP p_192180_1_, final TileEntityBeacon p_192180_2_) {
        final Listeners constructbeacontrigger$listeners = this.field_192182_b.get(p_192180_1_.func_192039_O());
        if (constructbeacontrigger$listeners != null) {
            constructbeacontrigger$listeners.func_192352_a(p_192180_2_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final MinMaxBounds field_192253_a;
        
        public Instance(final MinMaxBounds p_i47373_1_) {
            super(ConstructBeaconTrigger.field_192181_a);
            this.field_192253_a = p_i47373_1_;
        }
        
        public boolean func_192252_a(final TileEntityBeacon p_192252_1_) {
            return this.field_192253_a.func_192514_a((float)p_192252_1_.func_191979_s());
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192356_a;
        private final Set<Listener<Instance>> field_192357_b;
        
        public Listeners(final PlayerAdvancements p_i47374_1_) {
            this.field_192357_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192356_a = p_i47374_1_;
        }
        
        public boolean func_192354_a() {
            return this.field_192357_b.isEmpty();
        }
        
        public void func_192355_a(final Listener<Instance> p_192355_1_) {
            this.field_192357_b.add(p_192355_1_);
        }
        
        public void func_192353_b(final Listener<Instance> p_192353_1_) {
            this.field_192357_b.remove(p_192353_1_);
        }
        
        public void func_192352_a(final TileEntityBeacon p_192352_1_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192357_b) {
                if (listener.func_192158_a().func_192252_a(p_192352_1_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192356_a);
                }
            }
        }
    }
}
