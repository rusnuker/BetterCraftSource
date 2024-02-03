// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.cosmetics;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiCosmetic extends GuiScreen
{
    public GuiScreen before;
    
    public GuiCosmetic(final GuiScreen screen) {
        this.before = screen;
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1337, GuiCosmetic.width - 93, GuiCosmetic.height - 23, 90, 20, "Back"));
        int index = 0;
        int x = 0;
        for (int y = 0; index < CosmeticInstance.getCosmetics().size(); ++index, y += 25) {
            final CosmeticBase cosmetic = CosmeticInstance.getCosmetics().get(index);
            this.buttonList.add(new GuiButton(index, 3 + x, 3 + y, 98, 20, String.format("%s%s%s", '§', cosmetic.isEnabled() ? 'a' : 'c', StringUtils.join((Object[])StringUtils.splitByCharacterTypeCamelCase(cosmetic.getClass().getSimpleName().replace("Cosmetic", "").replaceAll("\\d+", "")), " "))));
            if (y + 56 >= GuiCosmetic.height) {
                y = -25;
                x += 101;
            }
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1337) {
            this.mc.displayGuiScreen(this.before);
        }
        for (int index = 0; index < CosmeticInstance.getCosmetics().size(); ++index) {
            final CosmeticBase cosmetic = CosmeticInstance.getCosmetics().get(index);
            if (button.id == index) {
                cosmetic.setEnabledState(!cosmetic.isEnabled());
                button.displayString = String.format("%s%s%s", '§', cosmetic.isEnabled() ? 'a' : 'c', StringUtils.join((Object[])StringUtils.splitByCharacterTypeCamelCase(cosmetic.getClass().getSimpleName().replace("Cosmetic", "").replaceAll("\\d+", "")), " "));
            }
        }
        CosmeticInstance.sendCosmetics();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        drawEntityOnScreen(GuiCosmetic.width - 50, GuiCosmetic.height - 40, 30, GuiCosmetic.width + 51 - (float)mouseX, GuiCosmetic.height + 75 - 50 - (float)mouseY, this.mc.thePlayer);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public static void drawEntityOnScreen(final int posX, final int posY, final int scale, final float mouseX, final float mouseY, final EntityLivingBase entity) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 100.0f);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        final float var5 = entity.prevRenderYawOffset;
        final float var6 = entity.renderYawOffset;
        final float var7 = entity.rotationYaw;
        final float var8 = entity.rotationPitch;
        final float var9 = entity.prevRotationYawHead;
        final float var10 = entity.rotationYawHead;
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(260.0f + mouseX, 0.0f, 1.0f, 0.0f);
        entity.renderYawOffset = (float)Math.atan(mouseX / 40.0f);
        entity.rotationYaw = (float)Math.atan(mouseX / 40.0f) * 40.0f;
        entity.rotationPitch = (float)Math.atan(mouseY / 40.0f);
        entity.rotationYawHead = -(float)Math.atan(mouseX / 40.0f) * 10.0f;
        entity.prevRotationYawHead = -(float)Math.atan(mouseX / 40.0f);
        entity.prevRenderYawOffset = -(float)Math.atan(mouseX / 40.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager var11 = Minecraft.getMinecraft().getRenderManager();
        var11.setPlayerViewY(180.0f);
        var11.setRenderShadow(false);
        var11.renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        var11.setRenderShadow(true);
        entity.renderYawOffset = var6;
        entity.rotationYaw = var7;
        entity.rotationPitch = var8;
        entity.prevRenderYawOffset = var5;
        entity.prevRotationYawHead = var9;
        entity.rotationYawHead = var10;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}
