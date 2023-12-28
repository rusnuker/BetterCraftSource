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
import com.google.gson.JsonSyntaxException;
import net.minecraft.potion.PotionType;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class BrewedPotionTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192176_a;
    private final Map<PlayerAdvancements, Listeners> field_192177_b;
    
    static {
        field_192176_a = new ResourceLocation("brewed_potion");
    }
    
    public BrewedPotionTrigger() {
        this.field_192177_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return BrewedPotionTrigger.field_192176_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners brewedpotiontrigger$listeners = this.field_192177_b.get(p_192165_1_);
        if (brewedpotiontrigger$listeners == null) {
            brewedpotiontrigger$listeners = new Listeners(p_192165_1_);
            this.field_192177_b.put(p_192165_1_, brewedpotiontrigger$listeners);
        }
        brewedpotiontrigger$listeners.func_192349_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners brewedpotiontrigger$listeners = this.field_192177_b.get(p_192164_1_);
        if (brewedpotiontrigger$listeners != null) {
            brewedpotiontrigger$listeners.func_192346_b(p_192164_2_);
            if (brewedpotiontrigger$listeners.func_192347_a()) {
                this.field_192177_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192177_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        PotionType potiontype = null;
        if (p_192166_1_.has("potion")) {
            final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(p_192166_1_, "potion"));
            if (!PotionType.REGISTRY.containsKey(resourcelocation)) {
                throw new JsonSyntaxException("Unknown potion '" + resourcelocation + "'");
            }
            potiontype = PotionType.REGISTRY.getObject(resourcelocation);
        }
        return new Instance(potiontype);
    }
    
    public void func_192173_a(final EntityPlayerMP p_192173_1_, final PotionType p_192173_2_) {
        final Listeners brewedpotiontrigger$listeners = this.field_192177_b.get(p_192173_1_.func_192039_O());
        if (brewedpotiontrigger$listeners != null) {
            brewedpotiontrigger$listeners.func_192348_a(p_192173_2_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final PotionType field_192251_a;
        
        public Instance(@Nullable final PotionType p_i47398_1_) {
            super(BrewedPotionTrigger.field_192176_a);
            this.field_192251_a = p_i47398_1_;
        }
        
        public boolean func_192250_a(final PotionType p_192250_1_) {
            return this.field_192251_a == null || this.field_192251_a == p_192250_1_;
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192350_a;
        private final Set<Listener<Instance>> field_192351_b;
        
        public Listeners(final PlayerAdvancements p_i47399_1_) {
            this.field_192351_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192350_a = p_i47399_1_;
        }
        
        public boolean func_192347_a() {
            return this.field_192351_b.isEmpty();
        }
        
        public void func_192349_a(final Listener<Instance> p_192349_1_) {
            this.field_192351_b.add(p_192349_1_);
        }
        
        public void func_192346_b(final Listener<Instance> p_192346_1_) {
            this.field_192351_b.remove(p_192346_1_);
        }
        
        public void func_192348_a(final PotionType p_192348_1_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192351_b) {
                if (listener.func_192158_a().func_192250_a(p_192348_1_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192350_a);
                }
            }
        }
    }
}
