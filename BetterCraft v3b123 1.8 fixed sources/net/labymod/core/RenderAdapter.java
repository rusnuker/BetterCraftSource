// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface RenderAdapter
{
    ResourceLocation getOptionsBackground();
    
    ResourceLocation getInventoryBackground();
    
    ResourceLocation getButtonsTexture();
    
    ResourceLocation getIcons();
    
    void drawActivePotionEffects(final double p0, final double p1, final ResourceLocation p2);
    
    void cullFaceBack();
    
    void cullFaceFront();
    
    void renderItemIntoGUI(final ItemStack p0, final double p1, final double p2);
    
    void renderItemOverlayIntoGUI(final ItemStack p0, final double p1, final double p2, final String p3);
    
    void renderEntity(final RenderManager p0, final Entity p1, final double p2, final double p3, final double p4, final float p5, final float p6, final boolean p7);
}
