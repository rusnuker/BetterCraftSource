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
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class UsedEnderEyeTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192242_a;
    private final Map<PlayerAdvancements, Listeners> field_192243_b;
    
    static {
        field_192242_a = new ResourceLocation("used_ender_eye");
    }
    
    public UsedEnderEyeTrigger() {
        this.field_192243_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return UsedEnderEyeTrigger.field_192242_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners usedendereyetrigger$listeners = this.field_192243_b.get(p_192165_1_);
        if (usedendereyetrigger$listeners == null) {
            usedendereyetrigger$listeners = new Listeners(p_192165_1_);
            this.field_192243_b.put(p_192165_1_, usedendereyetrigger$listeners);
        }
        usedendereyetrigger$listeners.func_192546_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners usedendereyetrigger$listeners = this.field_192243_b.get(p_192164_1_);
        if (usedendereyetrigger$listeners != null) {
            usedendereyetrigger$listeners.func_192544_b(p_192164_2_);
            if (usedendereyetrigger$listeners.func_192545_a()) {
                this.field_192243_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192243_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(p_192166_1_.get("distance"));
        return new Instance(minmaxbounds);
    }
    
    public void func_192239_a(final EntityPlayerMP p_192239_1_, final BlockPos p_192239_2_) {
        final Listeners usedendereyetrigger$listeners = this.field_192243_b.get(p_192239_1_.func_192039_O());
        if (usedendereyetrigger$listeners != null) {
            final double d0 = p_192239_1_.posX - p_192239_2_.getX();
            final double d2 = p_192239_1_.posZ - p_192239_2_.getZ();
            usedendereyetrigger$listeners.func_192543_a(d0 * d0 + d2 * d2);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final MinMaxBounds field_192289_a;
        
        public Instance(final MinMaxBounds p_i47449_1_) {
            super(UsedEnderEyeTrigger.field_192242_a);
            this.field_192289_a = p_i47449_1_;
        }
        
        public boolean func_192288_a(final double p_192288_1_) {
            return this.field_192289_a.func_192513_a(p_192288_1_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192547_a;
        private final Set<Listener<Instance>> field_192548_b;
        
        public Listeners(final PlayerAdvancements p_i47450_1_) {
            this.field_192548_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192547_a = p_i47450_1_;
        }
        
        public boolean func_192545_a() {
            return this.field_192548_b.isEmpty();
        }
        
        public void func_192546_a(final Listener<Instance> p_192546_1_) {
            this.field_192548_b.add(p_192546_1_);
        }
        
        public void func_192544_b(final Listener<Instance> p_192544_1_) {
            this.field_192548_b.remove(p_192544_1_);
        }
        
        public void func_192543_a(final double p_192543_1_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192548_b) {
                if (listener.func_192158_a().func_192288_a(p_192543_1_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192547_a);
                }
            }
        }
    }
}
