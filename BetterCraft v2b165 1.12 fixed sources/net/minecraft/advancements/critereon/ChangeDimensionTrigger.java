// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DimensionType;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class ChangeDimensionTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_193144_a;
    private final Map<PlayerAdvancements, Listeners> field_193145_b;
    
    static {
        field_193144_a = new ResourceLocation("changed_dimension");
    }
    
    public ChangeDimensionTrigger() {
        this.field_193145_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return ChangeDimensionTrigger.field_193144_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners changedimensiontrigger$listeners = this.field_193145_b.get(p_192165_1_);
        if (changedimensiontrigger$listeners == null) {
            changedimensiontrigger$listeners = new Listeners(p_192165_1_);
            this.field_193145_b.put(p_192165_1_, changedimensiontrigger$listeners);
        }
        changedimensiontrigger$listeners.func_193233_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners changedimensiontrigger$listeners = this.field_193145_b.get(p_192164_1_);
        if (changedimensiontrigger$listeners != null) {
            changedimensiontrigger$listeners.func_193231_b(p_192164_2_);
            if (changedimensiontrigger$listeners.func_193232_a()) {
                this.field_193145_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_193145_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final DimensionType dimensiontype = p_192166_1_.has("from") ? DimensionType.func_193417_a(JsonUtils.getString(p_192166_1_, "from")) : null;
        final DimensionType dimensiontype2 = p_192166_1_.has("to") ? DimensionType.func_193417_a(JsonUtils.getString(p_192166_1_, "to")) : null;
        return new Instance(dimensiontype, dimensiontype2);
    }
    
    public void func_193143_a(final EntityPlayerMP p_193143_1_, final DimensionType p_193143_2_, final DimensionType p_193143_3_) {
        final Listeners changedimensiontrigger$listeners = this.field_193145_b.get(p_193143_1_.func_192039_O());
        if (changedimensiontrigger$listeners != null) {
            changedimensiontrigger$listeners.func_193234_a(p_193143_2_, p_193143_3_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        @Nullable
        private final DimensionType field_193191_a;
        @Nullable
        private final DimensionType field_193192_b;
        
        public Instance(@Nullable final DimensionType p_i47475_1_, @Nullable final DimensionType p_i47475_2_) {
            super(ChangeDimensionTrigger.field_193144_a);
            this.field_193191_a = p_i47475_1_;
            this.field_193192_b = p_i47475_2_;
        }
        
        public boolean func_193190_a(final DimensionType p_193190_1_, final DimensionType p_193190_2_) {
            return (this.field_193191_a == null || this.field_193191_a == p_193190_1_) && (this.field_193192_b == null || this.field_193192_b == p_193190_2_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_193235_a;
        private final Set<Listener<Instance>> field_193236_b;
        
        public Listeners(final PlayerAdvancements p_i47476_1_) {
            this.field_193236_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_193235_a = p_i47476_1_;
        }
        
        public boolean func_193232_a() {
            return this.field_193236_b.isEmpty();
        }
        
        public void func_193233_a(final Listener<Instance> p_193233_1_) {
            this.field_193236_b.add(p_193233_1_);
        }
        
        public void func_193231_b(final Listener<Instance> p_193231_1_) {
            this.field_193236_b.remove(p_193231_1_);
        }
        
        public void func_193234_a(final DimensionType p_193234_1_, final DimensionType p_193234_2_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_193236_b) {
                if (listener.func_192158_a().func_193190_a(p_193234_1_, p_193234_2_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_193235_a);
                }
            }
        }
    }
}
