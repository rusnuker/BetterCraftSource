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
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class EntityHurtPlayerTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192201_a;
    private final Map<PlayerAdvancements, Listeners> field_192202_b;
    
    static {
        field_192201_a = new ResourceLocation("entity_hurt_player");
    }
    
    public EntityHurtPlayerTrigger() {
        this.field_192202_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return EntityHurtPlayerTrigger.field_192201_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners entityhurtplayertrigger$listeners = this.field_192202_b.get(p_192165_1_);
        if (entityhurtplayertrigger$listeners == null) {
            entityhurtplayertrigger$listeners = new Listeners(p_192165_1_);
            this.field_192202_b.put(p_192165_1_, entityhurtplayertrigger$listeners);
        }
        entityhurtplayertrigger$listeners.func_192477_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners entityhurtplayertrigger$listeners = this.field_192202_b.get(p_192164_1_);
        if (entityhurtplayertrigger$listeners != null) {
            entityhurtplayertrigger$listeners.func_192475_b(p_192164_2_);
            if (entityhurtplayertrigger$listeners.func_192476_a()) {
                this.field_192202_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192202_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final DamagePredicate damagepredicate = DamagePredicate.func_192364_a(p_192166_1_.get("damage"));
        return new Instance(damagepredicate);
    }
    
    public void func_192200_a(final EntityPlayerMP p_192200_1_, final DamageSource p_192200_2_, final float p_192200_3_, final float p_192200_4_, final boolean p_192200_5_) {
        final Listeners entityhurtplayertrigger$listeners = this.field_192202_b.get(p_192200_1_.func_192039_O());
        if (entityhurtplayertrigger$listeners != null) {
            entityhurtplayertrigger$listeners.func_192478_a(p_192200_1_, p_192200_2_, p_192200_3_, p_192200_4_, p_192200_5_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final DamagePredicate field_192264_a;
        
        public Instance(final DamagePredicate p_i47438_1_) {
            super(EntityHurtPlayerTrigger.field_192201_a);
            this.field_192264_a = p_i47438_1_;
        }
        
        public boolean func_192263_a(final EntityPlayerMP p_192263_1_, final DamageSource p_192263_2_, final float p_192263_3_, final float p_192263_4_, final boolean p_192263_5_) {
            return this.field_192264_a.func_192365_a(p_192263_1_, p_192263_2_, p_192263_3_, p_192263_4_, p_192263_5_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192479_a;
        private final Set<Listener<Instance>> field_192480_b;
        
        public Listeners(final PlayerAdvancements p_i47439_1_) {
            this.field_192480_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192479_a = p_i47439_1_;
        }
        
        public boolean func_192476_a() {
            return this.field_192480_b.isEmpty();
        }
        
        public void func_192477_a(final Listener<Instance> p_192477_1_) {
            this.field_192480_b.add(p_192477_1_);
        }
        
        public void func_192475_b(final Listener<Instance> p_192475_1_) {
            this.field_192480_b.remove(p_192475_1_);
        }
        
        public void func_192478_a(final EntityPlayerMP p_192478_1_, final DamageSource p_192478_2_, final float p_192478_3_, final float p_192478_4_, final boolean p_192478_5_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192480_b) {
                if (listener.func_192158_a().func_192263_a(p_192478_1_, p_192478_2_, p_192478_3_, p_192478_4_, p_192478_5_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192479_a);
                }
            }
        }
    }
}
