// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.util;

import net.minecraft.client.model.ModelBase;
import net.labymod.utils.Consumer;
import net.minecraft.client.model.ModelRenderer;

public class ModelRendererHook extends ModelRenderer
{
    private Consumer<ModelRendererHook> hook;
    private float scale;
    
    public ModelRendererHook(final ModelBase model) {
        super(model);
    }
    
    public ModelRendererHook(final ModelBase model, final int texOffX, final int texOffY) {
        super(model, texOffX, texOffY);
    }
    
    @Override
    public void render(final float scale) {
        this.scale = scale;
        this.hook.accept(this);
    }
    
    public void renderSuper() {
        super.render(this.scale);
    }
    
    public void setHook(final Consumer<ModelRendererHook> hook) {
        this.hook = hook;
    }
    
    public float getScale() {
        return this.scale;
    }
}
