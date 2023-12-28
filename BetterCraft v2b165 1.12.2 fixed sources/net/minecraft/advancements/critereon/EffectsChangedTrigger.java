// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class EffectsChangedTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_193154_a;
    private final Map<PlayerAdvancements, Listeners> field_193155_b;
    
    static {
        field_193154_a = new ResourceLocation("effects_changed");
    }
    
    public EffectsChangedTrigger() {
        this.field_193155_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return EffectsChangedTrigger.field_193154_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners effectschangedtrigger$listeners = this.field_193155_b.get(p_192165_1_);
        if (effectschangedtrigger$listeners == null) {
            effectschangedtrigger$listeners = new Listeners(p_192165_1_);
            this.field_193155_b.put(p_192165_1_, effectschangedtrigger$listeners);
        }
        effectschangedtrigger$listeners.func_193431_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners effectschangedtrigger$listeners = this.field_193155_b.get(p_192164_1_);
        if (effectschangedtrigger$listeners != null) {
            effectschangedtrigger$listeners.func_193429_b(p_192164_2_);
            if (effectschangedtrigger$listeners.func_193430_a()) {
                this.field_193155_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_193155_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final MobEffectsPredicate mobeffectspredicate = MobEffectsPredicate.func_193471_a(p_192166_1_.get("effects"));
        return new Instance(mobeffectspredicate);
    }
    
    public void func_193153_a(final EntityPlayerMP p_193153_1_) {
        final Listeners effectschangedtrigger$listeners = this.field_193155_b.get(p_193153_1_.func_192039_O());
        if (effectschangedtrigger$listeners != null) {
            effectschangedtrigger$listeners.func_193432_a(p_193153_1_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final MobEffectsPredicate field_193196_a;
        
        public Instance(final MobEffectsPredicate p_i47545_1_) {
            super(EffectsChangedTrigger.field_193154_a);
            this.field_193196_a = p_i47545_1_;
        }
        
        public boolean func_193195_a(final EntityPlayerMP p_193195_1_) {
            return this.field_193196_a.func_193472_a(p_193195_1_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_193433_a;
        private final Set<Listener<Instance>> field_193434_b;
        
        public Listeners(final PlayerAdvancements p_i47546_1_) {
            this.field_193434_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_193433_a = p_i47546_1_;
        }
        
        public boolean func_193430_a() {
            return this.field_193434_b.isEmpty();
        }
        
        public void func_193431_a(final Listener<Instance> p_193431_1_) {
            this.field_193434_b.add(p_193431_1_);
        }
        
        public void func_193429_b(final Listener<Instance> p_193429_1_) {
            this.field_193434_b.remove(p_193429_1_);
        }
        
        public void func_193432_a(final EntityPlayerMP p_193432_1_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_193434_b) {
                if (listener.func_192158_a().func_193195_a(p_193432_1_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_193433_a);
                }
            }
        }
    }
}
