// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.event;

import net.labymod.user.cosmetic.util.CosmeticData;
import org.lwjgl.opengl.GL11;
import net.labymod.core.LabyModCore;
import java.awt.Color;
import net.labymod.main.ModTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticAntlers extends CosmeticRenderer<CosmeticAntlersData>
{
    public static final int ID = 10;
    private ModelRenderer antler;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        (this.antler = new ModelRenderer(modelCosmetics).setTextureSize(4, 8)).setRotationPoint(-3.0f, -7.5f, -1.0f);
        this.antler.addBox(0.0f, 0.0f, 0.0f, 1, 5, 1);
        this.antler.rotateAngleX = 3.1415927f;
        this.antler.rotateAngleZ = -0.8f;
        final ModelRenderer firstTip = new ModelRenderer(modelCosmetics).setTextureSize(4, 8);
        firstTip.setRotationPoint(0.0f, 5.0f, 0.0f);
        firstTip.addBox(0.0f, 0.0f, 0.0f, 1, 5, 1);
        firstTip.rotateAngleZ = -0.6f;
        firstTip.rotateAngleX = -0.2f;
        this.antler.addChild(firstTip);
        final ModelRenderer secondTip = new ModelRenderer(modelCosmetics).setTextureSize(4, 8);
        secondTip.setRotationPoint(0.0f, 3.0f, 0.0f);
        secondTip.addBox(0.0f, 0.0f, 0.0f, 1, 5, 1);
        secondTip.rotateAngleZ = 0.2f;
        secondTip.rotateAngleX = 0.4f;
        this.antler.addChild(secondTip);
        final ModelRenderer firstHook = new ModelRenderer(modelCosmetics).setTextureSize(4, 8);
        firstHook.setRotationPoint(0.0f, 5.0f, 0.0f);
        firstHook.addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
        firstHook.rotateAngleZ = -0.8f;
        firstHook.rotateAngleX = 0.4f;
        firstTip.addChild(firstHook);
        final ModelRenderer secondHook = new ModelRenderer(modelCosmetics).setTextureSize(4, 8);
        secondHook.setRotationPoint(0.0f, 5.0f, 0.0f);
        secondHook.addBox(0.0f, 0.0f, 0.0f, 1, 3, 1);
        secondHook.rotateAngleZ = -0.8f;
        secondHook.rotateAngleX = -0.4f;
        secondTip.addChild(secondHook);
        final ModelRenderer mainHook = new ModelRenderer(modelCosmetics).setTextureSize(4, 8);
        mainHook.setRotationPoint(0.0f, 2.0f, 0.0f);
        mainHook.addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
        mainHook.rotateAngleZ = -0.6f;
        mainHook.rotateAngleX = -0.2f;
        this.antler.addChild(mainHook);
        final ModelRenderer besideHook = new ModelRenderer(modelCosmetics).setTextureSize(4, 8);
        besideHook.setRotationPoint(0.0f, 2.0f, 0.0f);
        besideHook.addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
        besideHook.rotateAngleZ = 0.6f;
        besideHook.rotateAngleX = 0.2f;
        secondTip.addChild(besideHook);
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.antler.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticAntlersData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        GlStateManager.pushMatrix();
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, 0.06, 0.0);
        }
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        final ModelRenderer antler = this.bindTextureAndColor(null, ModTextures.COSMETIC_ANTLER, this.antler);
        for (int i = 0; i < 2; ++i) {
            GlStateManager.enableCull();
            antler.isHidden = false;
            antler.render(scale);
            antler.isHidden = true;
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            if (i == 0) {
                LabyModCore.getRenderImplementation().cullFaceFront();
            }
        }
        LabyModCore.getRenderImplementation().cullFaceBack();
        GL11.glColor3d(1.0, 1.0, 1.0);
        GlStateManager.disableCull();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 10;
    }
    
    @Override
    public String getCosmeticName() {
        return "Antlers";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    @Override
    public float getNameTagHeight() {
        return 0.5f;
    }
    
    public static class CosmeticAntlersData extends CosmeticData
    {
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) {
        }
    }
}
