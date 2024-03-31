// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import java.awt.Color;
import net.minecraft.entity.Entity;

public abstract class CosmeticRenderer<CosmeticData>
{
    public abstract int getCosmeticId();
    
    public abstract String getCosmeticName();
    
    public abstract boolean isOfflineAvailable();
    
    public abstract void addModels(final ModelCosmetics p0, final float p1);
    
    public abstract void setInvisible(final boolean p0);
    
    public abstract void render(final ModelCosmetics p0, final Entity p1, final CosmeticData p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final boolean p9);
    
    public float getNameTagHeight() {
        return 0.0f;
    }
    
    public void setRotationAngles(final ModelCosmetics modelCosmetics, final float movementFactor, final float walkingSpeed, final float tickValue, final float var4, final float var5, final float var6, final Entity entityIn) {
    }
    
    public void onTick() {
    }
    
    protected ModelRenderer bindTextureAndColor(final Color color, final ResourceLocation resourceLocation, final ModelRenderer model) {
        return this.bindTextureAndColor((color == null) ? 0 : color.getRGB(), resourceLocation, model);
    }
    
    protected ModelRenderer bindTextureAndColor(final int color, final ResourceLocation resourceLocation, final ModelRenderer model) {
        final int red = color >> 16 & 0xFF;
        final int green = color >> 8 & 0xFF;
        final int blue = color >> 0 & 0xFF;
        GL11.glColor4f(red / 255.0f, green / 255.0f, blue / 255.0f, 1.0f);
        if (resourceLocation != null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        }
        return model;
    }
}
