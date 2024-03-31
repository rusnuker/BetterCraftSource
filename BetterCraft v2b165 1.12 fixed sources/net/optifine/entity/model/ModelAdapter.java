// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBase;

public abstract class ModelAdapter
{
    private Class entityClass;
    private String name;
    private float shadowSize;
    
    public ModelAdapter(final Class entityClass, final String name, final float shadowSize) {
        this.entityClass = entityClass;
        this.name = name;
        this.shadowSize = shadowSize;
    }
    
    public Class getEntityClass() {
        return this.entityClass;
    }
    
    public String getName() {
        return this.name;
    }
    
    public float getShadowSize() {
        return this.shadowSize;
    }
    
    public abstract ModelBase makeModel();
    
    public abstract ModelRenderer getModelRenderer(final ModelBase p0, final String p1);
    
    public abstract IEntityRenderer makeEntityRender(final ModelBase p0, final float p1);
}
