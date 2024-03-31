// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18;

import net.labymod.core.WorldRendererAdapter;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.labymod.main.LabyMod;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.core.LabyModCore;
import net.minecraft.util.ResourceLocation;
import net.labymod.core.RenderAdapter;
import net.minecraft.client.gui.Gui;

public class RenderImplementation extends Gui implements RenderAdapter
{
    private static final ResourceLocation buttonTextures;
    private static ResourceLocation inventoryBackground;
    
    static {
        RenderImplementation.inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
        buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    }
    
    @Override
    public ResourceLocation getOptionsBackground() {
        return Gui.optionsBackground;
    }
    
    @Override
    public ResourceLocation getInventoryBackground() {
        return RenderImplementation.inventoryBackground;
    }
    
    @Override
    public ResourceLocation getButtonsTexture() {
        return RenderImplementation.buttonTextures;
    }
    
    @Override
    public ResourceLocation getIcons() {
        return Gui.icons;
    }
    
    @Override
    public void drawActivePotionEffects(final double guiLeft, final double guiTop, final ResourceLocation inventoryBackground) {
        final double i = guiLeft - 124.0;
        double j = guiTop;
        final Collection<PotionEffect> collection = LabyModCore.getMinecraft().getPlayer().getActivePotionEffects();
        if (!collection.isEmpty()) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            int l = 33;
            if (collection.size() > 5) {
                l = 132 / (collection.size() - 1);
            }
            for (final PotionEffect potioneffect : LabyModCore.getMinecraft().getPlayer().getActivePotionEffects()) {
                final Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(inventoryBackground);
                this.drawTexturedModalRect((float)i, (float)j, 0, 166, 140, 32);
                if (potion.hasStatusIcon()) {
                    final int i2 = potion.getStatusIconIndex();
                    this.drawTexturedModalRect((float)i + 6.0f, (float)j + 7.0f, 0 + i2 % 8 * 18, 198 + i2 / 8 * 18, 18, 18);
                }
                String s1 = I18n.format(potion.getName(), new Object[0]);
                if (potioneffect.getAmplifier() == 1) {
                    s1 = String.valueOf(s1) + " " + I18n.format("enchantment.level.2", new Object[0]);
                }
                else if (potioneffect.getAmplifier() == 2) {
                    s1 = String.valueOf(s1) + " " + I18n.format("enchantment.level.3", new Object[0]);
                }
                else if (potioneffect.getAmplifier() == 3) {
                    s1 = String.valueOf(s1) + " " + I18n.format("enchantment.level.4", new Object[0]);
                }
                LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(s1, (float)i + 10.0f + 18.0f, (float)j + 6.0f, 16777215);
                final String s2 = Potion.getDurationString(potioneffect);
                LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(s2, (float)i + 10.0f + 18.0f, (float)j + 6.0f + 10.0f, 8355711);
                j += l;
            }
        }
    }
    
    @Override
    public void cullFaceBack() {
        GlStateManager.cullFace(1029);
    }
    
    @Override
    public void cullFaceFront() {
        GlStateManager.cullFace(1028);
    }
    
    @Override
    public void renderItemIntoGUI(final ItemStack stack, final double x, final double y) {
        final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        final IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
        GlStateManager.pushMatrix();
        textureManager.bindTexture(TextureMap.locationBlocksTexture);
        textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.setupGuiTransform(x, y, ibakedmodel.isGui3d());
        ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GUI);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ibakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        textureManager.bindTexture(TextureMap.locationBlocksTexture);
        textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }
    
    private void setupGuiTransform(final double xPosition, final double yPosition, final boolean isGui3d) {
        GlStateManager.translate((float)xPosition, (float)yPosition, 100.0f + this.zLevel);
        GlStateManager.translate(8.0f, 8.0f, 0.0f);
        GlStateManager.scale(1.0f, 1.0f, -1.0f);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        if (isGui3d) {
            GlStateManager.scale(40.0f, 40.0f, 40.0f);
            GlStateManager.rotate(210.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.enableLighting();
        }
        else {
            GlStateManager.scale(64.0f, 64.0f, 64.0f);
            GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.disableLighting();
        }
    }
    
    @Override
    public void renderItemOverlayIntoGUI(final ItemStack stack, final double xPosition, final double yPosition, final String text) {
        if (stack != null) {
            if (stack.stackSize != 1 || text != null) {
                String s = (text == null) ? String.valueOf(stack.stackSize) : text;
                if (text == null && stack.stackSize < 1) {
                    s = EnumChatFormatting.RED + String.valueOf(stack.stackSize);
                }
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableBlend();
                LabyMod.getInstance().getDrawUtils().drawString(s, xPosition + 19.0 - 2.0 - LabyModCore.getMinecraft().getFontRenderer().getStringWidth(s), yPosition + 6.0 + 3.0);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
            if (stack.isItemDamaged()) {
                final int j = (int)Math.round(13.0 - stack.getItemDamage() * 13.0 / stack.getMaxDamage());
                final int i = (int)Math.round(255.0 - stack.getItemDamage() * 255.0 / stack.getMaxDamage());
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                this.drawItemTexture(xPosition + 2.0, yPosition + 13.0, 13.0, 2.0, 0, 0, 0, 255);
                this.drawItemTexture(xPosition + 2.0, yPosition + 13.0, 12.0, 1.0, (255 - i) / 4, 64, 0, 255);
                this.drawItemTexture(xPosition + 2.0, yPosition + 13.0, j, 1.0, 255 - i, i, 0, 255);
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
    }
    
    @Override
    public void renderEntity(final RenderManager renderManager, final Entity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final boolean p_147939_10_) {
        renderManager.doRenderEntity(entity, x, y, z, entityYaw, partialTicks, p_147939_10_);
    }
    
    private void drawItemTexture(final double x, final double y, final double z, final double offset, final int red, final int green, final int blue, final int alpha) {
        final WorldRendererAdapter worldRenderer = LabyModCore.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(x + 0.0, y + 0.0, 0.0).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(x + 0.0, y + offset, 0.0).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(x + z, y + offset, 0.0).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(x + z, y + 0.0, 0.0).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }
}
