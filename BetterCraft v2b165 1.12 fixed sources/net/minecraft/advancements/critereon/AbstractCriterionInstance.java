// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionInstance;

public class AbstractCriterionInstance implements ICriterionInstance
{
    private final ResourceLocation field_192245_a;
    
    public AbstractCriterionInstance(final ResourceLocation p_i47465_1_) {
        this.field_192245_a = p_i47465_1_;
    }
    
    @Override
    public ResourceLocation func_192244_a() {
        return this.field_192245_a;
    }
    
    @Override
    public String toString() {
        return "AbstractCriterionInstance{criterion=" + this.field_192245_a + '}';
    }
}
