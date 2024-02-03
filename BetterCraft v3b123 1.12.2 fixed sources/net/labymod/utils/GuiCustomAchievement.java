// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.RenderHelper;
import net.labymod.main.LabyMod;
import net.labymod.main.ModTextures;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiCustomAchievement extends Gui
{
    private Minecraft mc;
    private int width;
    private int height;
    private String achievementTitle;
    private String achievementDescription;
    private long notificationTime;
    private GameProfile gameProfile;
    private String imageURL;
    
    public GuiCustomAchievement(final Minecraft mc) {
        this.mc = mc;
    }
    
    public void displayAchievement(final String title, final String description) {
        this.achievementTitle = title;
        this.achievementDescription = description;
        this.notificationTime = Minecraft.getSystemTime();
        this.gameProfile = null;
        this.imageURL = null;
    }
    
    public void displayAchievement(final GameProfile gameProfile, final String title, final String description) {
        this.achievementTitle = title;
        this.achievementDescription = description;
        this.notificationTime = Minecraft.getSystemTime();
        this.gameProfile = gameProfile;
        this.imageURL = null;
    }
    
    public void displayAchievement(final String imageURL, final String title, final String description) {
        this.achievementTitle = title;
        this.achievementDescription = description;
        this.notificationTime = Minecraft.getSystemTime();
        this.imageURL = imageURL;
        this.gameProfile = null;
    }
    
    private void updateAchievementWindowScale() {
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        this.width = this.mc.displayWidth;
        this.height = this.mc.displayHeight;
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        this.width = scaledresolution.getScaledWidth();
        this.height = scaledresolution.getScaledHeight();
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, this.width, this.height, 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
    }
    
    public void updateAchievementWindow() {
        if (this.notificationTime != 0L) {
            final double d0 = (Minecraft.getSystemTime() - this.notificationTime) / 3000.0;
            if (d0 < 0.0 || d0 > 1.0) {
                this.notificationTime = 0L;
                return;
            }
            this.updateAchievementWindowScale();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            double d2 = d0 * 2.0;
            if (d2 > 1.0) {
                d2 = 2.0 - d2;
            }
            d2 *= 4.0;
            d2 = 1.0 - d2;
            if (d2 < 0.0) {
                d2 = 0.0;
            }
            d2 *= d2;
            d2 *= d2;
            final int i = this.width - 160;
            final int j = 0 - (int)(d2 * 36.0);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableTexture2D();
            this.mc.getTextureManager().bindTexture(ModTextures.ACHIEVEMENT);
            GlStateManager.disableLighting();
            this.drawTexturedModalRect(i, j, 96, 202, 160, 32);
            final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
            final int startX = i + ((this.gameProfile == null && this.imageURL == null) ? 8 : 30);
            final String title = draw.trimStringToWidth(this.achievementTitle, this.width - startX - 10);
            final List<String> descriptions = draw.listFormattedStringToWidth(this.achievementDescription, (int)((this.width - startX - 10) / 0.7), 2);
            if (descriptions.size() == 1) {
                draw.drawString(draw.fontRenderer, title, startX, j + 7, -256);
                draw.drawString(this.achievementDescription, startX, j + 5 + 13, 0.7);
            }
            else {
                draw.drawString(draw.fontRenderer, title, startX, j + 5, -256);
                int y = j + 14 - 7;
                for (final String line : descriptions) {
                    final DrawUtils drawUtils = draw;
                    final String text = line;
                    final double x = startX;
                    y += 7;
                    drawUtils.drawString(text, x, y, 0.75);
                }
            }
            if (this.gameProfile != null) {
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.disableLighting();
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableColorMaterial();
                GlStateManager.disableLighting();
                GlStateManager.color(1.0f, 1.0f, 1.0f);
                draw.drawPlayerHead(this.gameProfile, i + 8, j + 8, 16);
            }
            if (this.imageURL != null) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                LabyMod.getInstance().getDrawUtils().drawImageUrl(this.imageURL, i + 8, j + 8, 255.0, 255.0, 16.0, 16.0);
            }
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
        }
    }
    
    public void clearAchievements() {
        this.notificationTime = 0L;
        this.gameProfile = null;
        this.imageURL = null;
    }
}
