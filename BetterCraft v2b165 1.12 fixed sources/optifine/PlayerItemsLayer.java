// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class PlayerItemsLayer implements LayerRenderer
{
    private RenderPlayer renderPlayer;
    
    public PlayerItemsLayer(final RenderPlayer p_i75_1_) {
        this.renderPlayer = null;
        this.renderPlayer = p_i75_1_;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.renderEquippedItems(entitylivingbaseIn, scale, partialTicks);
    }
    
    protected void renderEquippedItems(final EntityLivingBase p_renderEquippedItems_1_, final float p_renderEquippedItems_2_, final float p_renderEquippedItems_3_) {
        if (Config.isShowCapes() && p_renderEquippedItems_1_ instanceof AbstractClientPlayer) {
            final AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)p_renderEquippedItems_1_;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableCull();
            final ModelBiped modelbiped = this.renderPlayer.getMainModel();
            PlayerConfigurations.renderPlayerItems(modelbiped, abstractclientplayer, p_renderEquippedItems_2_, p_renderEquippedItems_3_);
            GlStateManager.disableCull();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    public static void register(final Map p_register_0_) {
        final Set set = p_register_0_.keySet();
        boolean flag = false;
        for (final Object object : set) {
            final Object object2 = p_register_0_.get(object);
            if (object2 instanceof RenderPlayer) {
                final RenderPlayer renderplayer = (RenderPlayer)object2;
                ((RenderLivingBase<EntityLivingBase>)renderplayer).addLayer(new PlayerItemsLayer(renderplayer));
                flag = true;
            }
        }
        if (!flag) {
            Config.warn("PlayerItemsLayer not registered");
        }
    }
}
