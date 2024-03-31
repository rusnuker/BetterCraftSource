// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.mojang;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.lenni0451.eventapi.manager.ASMEventManager;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.renderer.entity.RenderPlayer;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import java.util.Map;
import net.labymod.utils.ReflectionHelper;
import net.labymod.core.LabyModCore;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderPlayerHook
{
    public RenderPlayerHook() {
        try {
            final Field skinMapField = ReflectionHelper.findField(RenderManager.class, LabyModCore.getRenderPlayerImplementation().getSkinMapNames());
            final Map<String, RenderPlayer> skinMap = (Map<String, RenderPlayer>)skinMapField.get(Minecraft.getMinecraft().getRenderManager());
            skinMap.put("default", LabyModCore.getRenderPlayerImplementation().getRenderPlayer(Minecraft.getMinecraft().getRenderManager(), false));
            skinMap.put("slim", LabyModCore.getRenderPlayerImplementation().getRenderPlayer(Minecraft.getMinecraft().getRenderManager(), true));
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public abstract static class RenderPlayerCustom extends RenderPlayer
    {
        public RenderPlayerCustom(final RenderManager renderManager, final boolean slim) {
            super(renderManager, slim);
            this.layerRenderers.clear();
            ASMEventManager.register(this.mainModel = new ModelCosmetics(0.0f, slim));
            LayerRenderer[] layerRenderers;
            for (int length = (layerRenderers = LabyModCore.getRenderPlayerImplementation().getLayerRenderers(this)).length, i = 0; i < length; ++i) {
                final LayerRenderer layerRenderer = layerRenderers[i];
                ((RendererLivingEntity<EntityLivingBase>)this).addLayer(layerRenderer);
            }
        }
        
        public abstract boolean canRenderTheName(final AbstractClientPlayer p0);
        
        public abstract void renderLabel(final AbstractClientPlayer p0, final double p1, final double p2, final double p3, final String p4, final float p5, final double p6);
    }
}
