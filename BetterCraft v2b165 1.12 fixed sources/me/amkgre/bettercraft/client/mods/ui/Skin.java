// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ui;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Skin extends Gui
{
    private static Minecraft mc;
    private static Entity currentEntity;
    
    static {
        Skin.mc = Minecraft.getMinecraft();
    }
    
    public static void render() {
        drawEntityOnScreen(GuiScreen.width - 30, GuiScreen.height - 100, 30, 10.0f, 10.0f, (Skin.currentEntity != null && Skin.currentEntity instanceof EntityLivingBase && !Skin.currentEntity.isDead) ? ((EntityLivingBase)Skin.currentEntity) : Skin.mc.player);
    }
    
    public static void drawEntityOnScreen(final int posX, final int posY, final int scale, final float mouseX, final float mouseY, final EntityLivingBase ent) {
        final double entitySize = ent.getEntityBoundingBox().maxY - ent.getEntityBoundingBox().minY;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        final double size = (ent instanceof EntityIronGolem) ? 0.8 : ((ent instanceof EntityGiantZombie) ? 0.2 : ((entitySize >= 3.9) ? 0.3 : ((entitySize >= 2.0) ? 0.6 : 1.0)));
        GL11.glScaled(size, size, size);
        GlStateManager.translate((entitySize >= 2.0) ? ((GuiScreen.width - 30) / size) : ((double)(GuiScreen.width - 30)), (entitySize >= 2.0) ? ((GuiScreen.height - 100 - ((ent instanceof EntityGhast) ? 20 : ((ent instanceof EntityGiantZombie) ? -10 : 0))) / size) : ((double)(GuiScreen.height - 100 - ((ent instanceof EntityGhast) ? 20 : ((ent instanceof EntityGiantZombie) ? -10 : 0)))), 50.0);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        final float f = ent.renderYawOffset;
        final float f2 = ent.rotationYaw;
        final float f3 = ent.rotationPitch;
        final float f4 = ent.prevRotationYawHead;
        final float f5 = ent.rotationYawHead;
        ent.isInIndicator = true;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(mouseY / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f2;
        ent.rotationPitch = f3;
        ent.prevRotationYawHead = f4;
        ent.rotationYawHead = f5;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        Gui.drawRect(0, 0, 0, 0, -1);
    }
    
    public static void setCurrentEntity(final Entity currentEntity) {
        Skin.currentEntity = currentEntity;
    }
}
