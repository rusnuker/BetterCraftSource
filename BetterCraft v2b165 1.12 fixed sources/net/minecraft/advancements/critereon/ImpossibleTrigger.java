// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import net.minecraft.advancements.ICriterionInstance;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class ImpossibleTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192205_a;
    
    static {
        field_192205_a = new ResourceLocation("impossible");
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return ImpossibleTrigger.field_192205_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        return new Instance();
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        public Instance() {
            super(ImpossibleTrigger.field_192205_a);
        }
    }
}
