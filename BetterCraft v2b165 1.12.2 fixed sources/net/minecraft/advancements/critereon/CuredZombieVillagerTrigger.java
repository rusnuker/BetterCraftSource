// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class CuredZombieVillagerTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192186_a;
    private final Map<PlayerAdvancements, Listeners> field_192187_b;
    
    static {
        field_192186_a = new ResourceLocation("cured_zombie_villager");
    }
    
    public CuredZombieVillagerTrigger() {
        this.field_192187_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return CuredZombieVillagerTrigger.field_192186_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners curedzombievillagertrigger$listeners = this.field_192187_b.get(p_192165_1_);
        if (curedzombievillagertrigger$listeners == null) {
            curedzombievillagertrigger$listeners = new Listeners(p_192165_1_);
            this.field_192187_b.put(p_192165_1_, curedzombievillagertrigger$listeners);
        }
        curedzombievillagertrigger$listeners.func_192360_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners curedzombievillagertrigger$listeners = this.field_192187_b.get(p_192164_1_);
        if (curedzombievillagertrigger$listeners != null) {
            curedzombievillagertrigger$listeners.func_192358_b(p_192164_2_);
            if (curedzombievillagertrigger$listeners.func_192359_a()) {
                this.field_192187_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192187_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final EntityPredicate entitypredicate = EntityPredicate.func_192481_a(p_192166_1_.get("zombie"));
        final EntityPredicate entitypredicate2 = EntityPredicate.func_192481_a(p_192166_1_.get("villager"));
        return new Instance(entitypredicate, entitypredicate2);
    }
    
    public void func_192183_a(final EntityPlayerMP p_192183_1_, final EntityZombie p_192183_2_, final EntityVillager p_192183_3_) {
        final Listeners curedzombievillagertrigger$listeners = this.field_192187_b.get(p_192183_1_.func_192039_O());
        if (curedzombievillagertrigger$listeners != null) {
            curedzombievillagertrigger$listeners.func_192361_a(p_192183_1_, p_192183_2_, p_192183_3_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate field_192255_a;
        private final EntityPredicate field_192256_b;
        
        public Instance(final EntityPredicate p_i47459_1_, final EntityPredicate p_i47459_2_) {
            super(CuredZombieVillagerTrigger.field_192186_a);
            this.field_192255_a = p_i47459_1_;
            this.field_192256_b = p_i47459_2_;
        }
        
        public boolean func_192254_a(final EntityPlayerMP p_192254_1_, final EntityZombie p_192254_2_, final EntityVillager p_192254_3_) {
            return this.field_192255_a.func_192482_a(p_192254_1_, p_192254_2_) && this.field_192256_b.func_192482_a(p_192254_1_, p_192254_3_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192362_a;
        private final Set<Listener<Instance>> field_192363_b;
        
        public Listeners(final PlayerAdvancements p_i47460_1_) {
            this.field_192363_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192362_a = p_i47460_1_;
        }
        
        public boolean func_192359_a() {
            return this.field_192363_b.isEmpty();
        }
        
        public void func_192360_a(final Listener<Instance> p_192360_1_) {
            this.field_192363_b.add(p_192360_1_);
        }
        
        public void func_192358_b(final Listener<Instance> p_192358_1_) {
            this.field_192363_b.remove(p_192358_1_);
        }
        
        public void func_192361_a(final EntityPlayerMP p_192361_1_, final EntityZombie p_192361_2_, final EntityVillager p_192361_3_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192363_b) {
                if (listener.func_192158_a().func_192254_a(p_192361_1_, p_192361_2_, p_192361_3_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192362_a);
                }
            }
        }
    }
}
