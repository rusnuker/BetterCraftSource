// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class PlayerItemRenderer
{
    private int attachTo;
    private ModelRenderer modelRenderer;
    
    public PlayerItemRenderer(final int p_i74_1_, final ModelRenderer p_i74_2_) {
        this.attachTo = 0;
        this.modelRenderer = null;
        this.attachTo = p_i74_1_;
        this.modelRenderer = p_i74_2_;
    }
    
    public ModelRenderer getModelRenderer() {
        return this.modelRenderer;
    }
    
    public void render(final ModelBiped p_render_1_, final float p_render_2_) {
        final ModelRenderer modelrenderer = PlayerItemModel.getAttachModel(p_render_1_, this.attachTo);
        if (modelrenderer != null) {
            modelrenderer.postRender(p_render_2_);
        }
        this.modelRenderer.render(p_render_2_);
    }
}
