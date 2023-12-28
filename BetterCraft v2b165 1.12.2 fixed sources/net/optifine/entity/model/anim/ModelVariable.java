// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;

public class ModelVariable implements IExpression
{
    private String name;
    private ModelRenderer modelRenderer;
    private EnumModelVariable enumModelVariable;
    
    public ModelVariable(final String name, final ModelRenderer modelRenderer, final EnumModelVariable enumModelVariable) {
        this.name = name;
        this.modelRenderer = modelRenderer;
        this.enumModelVariable = enumModelVariable;
    }
    
    @Override
    public float eval() {
        return this.getValue();
    }
    
    public float getValue() {
        return this.enumModelVariable.getFloat(this.modelRenderer);
    }
    
    public void setValue(final float value) {
        this.enumModelVariable.setFloat(this.modelRenderer, value);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
