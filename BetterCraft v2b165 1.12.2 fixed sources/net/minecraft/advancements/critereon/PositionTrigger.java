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
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class PositionTrigger implements ICriterionTrigger<Instance>
{
    private final ResourceLocation field_192217_a;
    private final Map<PlayerAdvancements, Listeners> field_192218_b;
    
    public PositionTrigger(final ResourceLocation p_i47432_1_) {
        this.field_192218_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
        this.field_192217_a = p_i47432_1_;
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return this.field_192217_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners positiontrigger$listeners = this.field_192218_b.get(p_192165_1_);
        if (positiontrigger$listeners == null) {
            positiontrigger$listeners = new Listeners(p_192165_1_);
            this.field_192218_b.put(p_192165_1_, positiontrigger$listeners);
        }
        positiontrigger$listeners.func_192510_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners positiontrigger$listeners = this.field_192218_b.get(p_192164_1_);
        if (positiontrigger$listeners != null) {
            positiontrigger$listeners.func_192507_b(p_192164_2_);
            if (positiontrigger$listeners.func_192508_a()) {
                this.field_192218_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192218_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final LocationPredicate locationpredicate = LocationPredicate.func_193454_a(p_192166_1_);
        return new Instance(this.field_192217_a, locationpredicate);
    }
    
    public void func_192215_a(final EntityPlayerMP p_192215_1_) {
        final Listeners positiontrigger$listeners = this.field_192218_b.get(p_192215_1_.func_192039_O());
        if (positiontrigger$listeners != null) {
            positiontrigger$listeners.func_193462_a(p_192215_1_.getServerWorld(), p_192215_1_.posX, p_192215_1_.posY, p_192215_1_.posZ);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final LocationPredicate field_193205_a;
        
        public Instance(final ResourceLocation p_i47544_1_, final LocationPredicate p_i47544_2_) {
            super(p_i47544_1_);
            this.field_193205_a = p_i47544_2_;
        }
        
        public boolean func_193204_a(final WorldServer p_193204_1_, final double p_193204_2_, final double p_193204_4_, final double p_193204_6_) {
            return this.field_193205_a.func_193452_a(p_193204_1_, p_193204_2_, p_193204_4_, p_193204_6_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192511_a;
        private final Set<Listener<Instance>> field_192512_b;
        
        public Listeners(final PlayerAdvancements p_i47442_1_) {
            this.field_192512_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192511_a = p_i47442_1_;
        }
        
        public boolean func_192508_a() {
            return this.field_192512_b.isEmpty();
        }
        
        public void func_192510_a(final Listener<Instance> p_192510_1_) {
            this.field_192512_b.add(p_192510_1_);
        }
        
        public void func_192507_b(final Listener<Instance> p_192507_1_) {
            this.field_192512_b.remove(p_192507_1_);
        }
        
        public void func_193462_a(final WorldServer p_193462_1_, final double p_193462_2_, final double p_193462_4_, final double p_193462_6_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192512_b) {
                if (listener.func_192158_a().func_193204_a(p_193462_1_, p_193462_2_, p_193462_4_, p_193462_6_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192511_a);
                }
            }
        }
    }
}
