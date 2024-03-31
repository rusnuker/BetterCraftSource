// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model;

import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBase;

public abstract class ModelAdapterIllager extends ModelAdapter
{
    public ModelAdapterIllager(final Class entityClass, final String name, final float shadowSize) {
        super(entityClass, name, shadowSize);
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelIllager)) {
            return null;
        }
        final ModelIllager modelillager = (ModelIllager)model;
        if (modelPart.equals("head")) {
            return modelillager.field_191217_a;
        }
        if (modelPart.equals("body")) {
            return modelillager.field_191218_b;
        }
        if (modelPart.equals("arms")) {
            return modelillager.field_191219_c;
        }
        if (modelPart.equals("left_leg")) {
            return modelillager.field_191221_e;
        }
        if (modelPart.equals("right_leg")) {
            return modelillager.field_191220_d;
        }
        if (modelPart.equals("nose")) {
            return modelillager.field_191222_f;
        }
        if (modelPart.equals("left_arm")) {
            return modelillager.field_191224_h;
        }
        return modelPart.equals("right_arm") ? modelillager.field_191223_g : null;
    }
}
