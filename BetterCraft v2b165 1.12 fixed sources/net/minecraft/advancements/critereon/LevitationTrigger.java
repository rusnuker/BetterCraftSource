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
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class LevitationTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_193164_a;
    private final Map<PlayerAdvancements, Listeners> field_193165_b;
    
    static {
        field_193164_a = new ResourceLocation("levitation");
    }
    
    public LevitationTrigger() {
        this.field_193165_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return LevitationTrigger.field_193164_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners levitationtrigger$listeners = this.field_193165_b.get(p_192165_1_);
        if (levitationtrigger$listeners == null) {
            levitationtrigger$listeners = new Listeners(p_192165_1_);
            this.field_193165_b.put(p_192165_1_, levitationtrigger$listeners);
        }
        levitationtrigger$listeners.func_193449_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners levitationtrigger$listeners = this.field_193165_b.get(p_192164_1_);
        if (levitationtrigger$listeners != null) {
            levitationtrigger$listeners.func_193446_b(p_192164_2_);
            if (levitationtrigger$listeners.func_193447_a()) {
                this.field_193165_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_193165_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final DistancePredicate distancepredicate = DistancePredicate.func_193421_a(p_192166_1_.get("distance"));
        final MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(p_192166_1_.get("duration"));
        return new Instance(distancepredicate, minmaxbounds);
    }
    
    public void func_193162_a(final EntityPlayerMP p_193162_1_, final Vec3d p_193162_2_, final int p_193162_3_) {
        final Listeners levitationtrigger$listeners = this.field_193165_b.get(p_193162_1_.func_192039_O());
        if (levitationtrigger$listeners != null) {
            levitationtrigger$listeners.func_193448_a(p_193162_1_, p_193162_2_, p_193162_3_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final DistancePredicate field_193202_a;
        private final MinMaxBounds field_193203_b;
        
        public Instance(final DistancePredicate p_i47571_1_, final MinMaxBounds p_i47571_2_) {
            super(LevitationTrigger.field_193164_a);
            this.field_193202_a = p_i47571_1_;
            this.field_193203_b = p_i47571_2_;
        }
        
        public boolean func_193201_a(final EntityPlayerMP p_193201_1_, final Vec3d p_193201_2_, final int p_193201_3_) {
            return this.field_193202_a.func_193422_a(p_193201_2_.xCoord, p_193201_2_.yCoord, p_193201_2_.zCoord, p_193201_1_.posX, p_193201_1_.posY, p_193201_1_.posZ) && this.field_193203_b.func_192514_a((float)p_193201_3_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_193450_a;
        private final Set<Listener<Instance>> field_193451_b;
        
        public Listeners(final PlayerAdvancements p_i47572_1_) {
            this.field_193451_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_193450_a = p_i47572_1_;
        }
        
        public boolean func_193447_a() {
            return this.field_193451_b.isEmpty();
        }
        
        public void func_193449_a(final Listener<Instance> p_193449_1_) {
            this.field_193451_b.add(p_193449_1_);
        }
        
        public void func_193446_b(final Listener<Instance> p_193446_1_) {
            this.field_193451_b.remove(p_193446_1_);
        }
        
        public void func_193448_a(final EntityPlayerMP p_193448_1_, final Vec3d p_193448_2_, final int p_193448_3_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_193451_b) {
                if (listener.func_192158_a().func_193201_a(p_193448_1_, p_193448_2_, p_193448_3_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_193450_a);
                }
            }
        }
    }
}
