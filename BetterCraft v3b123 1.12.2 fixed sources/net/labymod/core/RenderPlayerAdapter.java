// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.mojang.RenderPlayerHook;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;

public interface RenderPlayerAdapter
{
    String[] getSkinMapNames();
    
    LayerRenderer[] getLayerRenderers(final RenderPlayer p0);
    
    void renderName(final RenderPlayerHook.RenderPlayerCustom p0, final AbstractClientPlayer p1, final double p2, final double p3, final double p4);
    
    RenderPlayerHook.RenderPlayerCustom getRenderPlayer(final RenderManager p0, final boolean p1);
}
