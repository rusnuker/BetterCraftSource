// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.world.WorldServer;
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

public class NetherTravelTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_193169_a;
    private final Map<PlayerAdvancements, Listeners> field_193170_b;
    
    static {
        field_193169_a = new ResourceLocation("nether_travel");
    }
    
    public NetherTravelTrigger() {
        this.field_193170_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return NetherTravelTrigger.field_193169_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners nethertraveltrigger$listeners = this.field_193170_b.get(p_192165_1_);
        if (nethertraveltrigger$listeners == null) {
            nethertraveltrigger$listeners = new Listeners(p_192165_1_);
            this.field_193170_b.put(p_192165_1_, nethertraveltrigger$listeners);
        }
        nethertraveltrigger$listeners.func_193484_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners nethertraveltrigger$listeners = this.field_193170_b.get(p_192164_1_);
        if (nethertraveltrigger$listeners != null) {
            nethertraveltrigger$listeners.func_193481_b(p_192164_2_);
            if (nethertraveltrigger$listeners.func_193482_a()) {
                this.field_193170_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_193170_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final LocationPredicate locationpredicate = LocationPredicate.func_193454_a(p_192166_1_.get("entered"));
        final LocationPredicate locationpredicate2 = LocationPredicate.func_193454_a(p_192166_1_.get("exited"));
        final DistancePredicate distancepredicate = DistancePredicate.func_193421_a(p_192166_1_.get("distance"));
        return new Instance(locationpredicate, locationpredicate2, distancepredicate);
    }
    
    public void func_193168_a(final EntityPlayerMP p_193168_1_, final Vec3d p_193168_2_) {
        final Listeners nethertraveltrigger$listeners = this.field_193170_b.get(p_193168_1_.func_192039_O());
        if (nethertraveltrigger$listeners != null) {
            nethertraveltrigger$listeners.func_193483_a(p_193168_1_.getServerWorld(), p_193168_2_, p_193168_1_.posX, p_193168_1_.posY, p_193168_1_.posZ);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final LocationPredicate field_193207_a;
        private final LocationPredicate field_193208_b;
        private final DistancePredicate field_193209_c;
        
        public Instance(final LocationPredicate p_i47574_1_, final LocationPredicate p_i47574_2_, final DistancePredicate p_i47574_3_) {
            super(NetherTravelTrigger.field_193169_a);
            this.field_193207_a = p_i47574_1_;
            this.field_193208_b = p_i47574_2_;
            this.field_193209_c = p_i47574_3_;
        }
        
        public boolean func_193206_a(final WorldServer p_193206_1_, final Vec3d p_193206_2_, final double p_193206_3_, final double p_193206_5_, final double p_193206_7_) {
            return this.field_193207_a.func_193452_a(p_193206_1_, p_193206_2_.xCoord, p_193206_2_.yCoord, p_193206_2_.zCoord) && this.field_193208_b.func_193452_a(p_193206_1_, p_193206_3_, p_193206_5_, p_193206_7_) && this.field_193209_c.func_193422_a(p_193206_2_.xCoord, p_193206_2_.yCoord, p_193206_2_.zCoord, p_193206_3_, p_193206_5_, p_193206_7_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_193485_a;
        private final Set<Listener<Instance>> field_193486_b;
        
        public Listeners(final PlayerAdvancements p_i47575_1_) {
            this.field_193486_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_193485_a = p_i47575_1_;
        }
        
        public boolean func_193482_a() {
            return this.field_193486_b.isEmpty();
        }
        
        public void func_193484_a(final Listener<Instance> p_193484_1_) {
            this.field_193486_b.add(p_193484_1_);
        }
        
        public void func_193481_b(final Listener<Instance> p_193481_1_) {
            this.field_193486_b.remove(p_193481_1_);
        }
        
        public void func_193483_a(final WorldServer p_193483_1_, final Vec3d p_193483_2_, final double p_193483_3_, final double p_193483_5_, final double p_193483_7_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_193486_b) {
                if (listener.func_192158_a().func_193206_a(p_193483_1_, p_193483_2_, p_193483_3_, p_193483_5_, p_193483_7_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_193485_a);
                }
            }
        }
    }
}
