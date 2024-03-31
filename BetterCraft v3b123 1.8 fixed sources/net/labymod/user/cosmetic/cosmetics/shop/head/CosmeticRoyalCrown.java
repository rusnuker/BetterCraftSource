// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.head;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.labymod.user.cosmetic.util.CosmeticData;
import java.awt.Color;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticRoyalCrown extends CosmeticRenderer<CosmeticRoyalCrownData>
{
    public static final int ID = 28;
    private ModelRenderer base;
    private ModelRenderer diamond;
    private ModelRenderer pillow;
    private ModelRenderer scaffolding;
    private ModelRenderer tip;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 30;
        final int height = 16;
        final float crownScale = 0.02f;
        this.base = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(30, 16);
        this.base.setTextureOffset(4, 0).addBox(-4.0f, 0.0f, -5.0f, 8, 2, 1, 0.02f);
        this.base.setTextureOffset(0, 0).addBox(-5.0f, -2.0f, -5.0f, 1, 4, 1, 0.02f);
        this.base.setTextureOffset(0, 5).addBox(-4.0f, -1.0f, -5.0f, 1, 1, 1, 0.02f);
        this.base.setTextureOffset(0, 5).addBox(3.0f, -1.0f, -5.0f, 1, 1, 1, 0.02f);
        this.base.setTextureOffset(4, 5).addBox(-1.5f, -1.0f, -5.0f, 3, 1, 1, 0.02f);
        this.base.setTextureOffset(0, 5).addBox(-0.5f, -2.0f, -5.0f, 1, 1, 1, 0.02f);
        this.base.isHidden = true;
        (this.diamond = new ModelRenderer(modelCosmetics, 12, 5).setTextureSize(30, 16)).addBox(-0.5f, -0.0f, -6.0f, 1, 1, 1, 0.02f);
        this.diamond.rotateAngleZ = 0.8f;
        this.diamond.rotationPointZ = 0.5f;
        this.diamond.rotationPointX = 0.4f;
        this.diamond.isHidden = true;
        this.pillow = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(30, 16);
        this.pillow.setTextureOffset(0, 7).addBox(-3.5f, 0.0f, -3.5f, 7, 2, 7, 0.02f);
        this.pillow.isHidden = true;
        this.scaffolding = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(30, 16);
        this.scaffolding.setTextureOffset(22, 0).addBox(0.5f, 0.0f, -0.5f, 3, 1, 1, 0.02f);
        this.scaffolding.setTextureOffset(26, 2).addBox(-4.5f, 0.5f, -0.51f, 1, 1, 1, 0.02f);
        this.scaffolding.isHidden = true;
        this.tip = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(30, 16);
        this.tip.setTextureOffset(22, 4).addBox(-0.5f, -2.5f, -0.5f, 1, 3, 1, 0.02f);
        this.tip.setTextureOffset(22, 2).addBox(-1.5f, -1.8f, -0.5f, 1, 1, 1, 0.02f);
        this.tip.isHidden = true;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.base.showModel = invisible;
        this.diamond.showModel = invisible;
        this.pillow.showModel = invisible;
        this.scaffolding.showModel = invisible;
        this.tip.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticRoyalCrownData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        final float scaleUp = 1.085f;
        GlStateManager.scale(1.085f, 1.085f, 1.085f);
        if (entityIn.isSneaking()) {
            final float m = entityIn.rotationPitch * -7.0E-4f;
            GlStateManager.translate(0.0, 0.06f - Math.abs(m) + 0.02, m);
        }
        GlStateManager.pushMatrix();
        final Color pillowColor = cosmeticData.getPillowColor();
        if (pillowColor == null) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            GL11.glColor4f(pillowColor.getRed() / 255.0f, pillowColor.getGreen() / 255.0f, pillowColor.getBlue() / 255.0f, 0.5f);
        }
        GlStateManager.translate(0.0f, -0.55f, 0.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_CROWN);
        this.pillow.isHidden = false;
        this.pillow.render(0.0571f);
        GlStateManager.scale(0.8, 0.8, 0.8);
        GlStateManager.translate(0.0, -0.05, 0.0);
        this.pillow.render(0.0571f);
        this.pillow.isHidden = true;
        GlStateManager.popMatrix();
        for (int i = 0; i < 4; ++i) {
            GlStateManager.pushMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.rotate((float)(90 * i), 0.0f, 1.0f, 0.0f);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0, -0.6753, 0.0);
            this.scaffolding.isHidden = false;
            this.scaffolding.render(0.0571f);
            this.scaffolding.isHidden = true;
            this.tip.isHidden = false;
            GlStateManager.scale(0.5, 0.5, 0.5);
            this.tip.render(0.0571f);
            this.tip.isHidden = true;
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0, -0.4753, 0.0);
            this.base.isHidden = false;
            this.base.render(0.0571f);
            this.base.isHidden = true;
            this.diamond.isHidden = false;
            this.diamond.rotateAngleZ = 0.8f;
            this.diamond.rotationPointZ = 0.6f;
            this.diamond.rotationPointX = 0.4f;
            GlStateManager.translate(-0.22f, 0.0f, 0.0f);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.translate(0.128f, 0.03f, 0.0f);
            if (cosmeticData.getKingOfItem() != null && i == 2) {
                for (int d = 0; d < 3; ++d) {
                    if (d != 1 || i == 2) {
                        double scaleEmerald = 0.07000000029802322;
                        double distance = 4.199999809265137;
                        if (d == 1) {
                            scaleEmerald = 0.10000000149011612;
                            distance = 3.0;
                        }
                        GlStateManager.pushMatrix();
                        GlStateManager.scale(scaleEmerald, -scaleEmerald, scaleEmerald);
                        GlStateManager.translate(0.05999999865889549, -0.30000001192092896, distance);
                        if (d == 1) {
                            GlStateManager.translate(0.0f, 0.2f, 0.0f);
                        }
                        Minecraft.getMinecraft().getItemRenderer().renderItem((EntityLivingBase)entityIn, cosmeticData.getKingOfItem(), ItemCameraTransforms.TransformType.FIXED);
                        GlStateManager.enableDepth();
                        GlStateManager.enableLighting();
                        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_CROWN);
                        GlStateManager.popMatrix();
                    }
                    GlStateManager.translate(0.088f, 0.0f, 0.0f);
                }
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            this.diamond.isHidden = true;
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 28;
    }
    
    @Override
    public String getCosmeticName() {
        return "Royal Crown";
    }
    
    @Override
    public float getNameTagHeight() {
        return 0.15f;
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticRoyalCrownData extends CosmeticData
    {
        private ItemStack kingOfItem;
        private Color pillowColor;
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.kingOfItem = new ItemStack(Item.getItemById(Integer.parseInt(data[0])));
            if (data.length > 1) {
                this.pillowColor = Color.decode("#" + data[1]);
            }
        }
        
        public ItemStack getKingOfItem() {
            return this.kingOfItem;
        }
        
        public Color getPillowColor() {
            return this.pillowColor;
        }
    }
}
