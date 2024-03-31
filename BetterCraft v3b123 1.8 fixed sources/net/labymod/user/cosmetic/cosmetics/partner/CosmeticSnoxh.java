// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.partner;

import net.labymod.user.User;
import net.labymod.user.cosmetic.util.CosmeticData;
import net.labymod.core.WorldRendererAdapter;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.labymod.core.LabyModCore;
import net.minecraft.client.renderer.Tessellator;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticSnoxh extends CosmeticRenderer<CosmeticSnoxhData>
{
    public static final int ID = 32;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticSnoxhData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        final int minColor = Math.min(cosmeticData.getBrightness(), 0);
        final int maxColor = cosmeticData.getBrightness();
        final float animation = (minColor + (float)Math.abs(Math.cos(tickValue / 30.0f) * (maxColor - minColor))) / 255.0f;
        final int brightness = 2;
        GlStateManager.pushMatrix();
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        if (entityIn.isSneaking()) {
            double sneakRotate = entityIn.rotationPitch / 70000.0;
            sneakRotate = Math.max(0.0, sneakRotate);
            GlStateManager.translate(0.0, 0.062, 0.0);
            GlStateManager.translate(0.0, sneakRotate, -sneakRotate);
        }
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.0625, 0.0625, 0.06);
        GlStateManager.translate(-4.0, -8.0, -4.17);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_SNOXH_EYE);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 772, 1, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.disableTexture2D();
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10242, 10496);
        GL11.glTexParameteri(3553, 10243, 10496);
        final int x = cosmeticData.getX();
        final int y = cosmeticData.getY();
        final int width = cosmeticData.getWidth();
        final int height = cosmeticData.getHeight();
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        GlStateManager.disableLighting();
        final boolean overlappingLeft = x + width - 1 == 3;
        for (int i = 0; i < 2; ++i) {
            for (int b = 0; b < 2; ++b) {
                if (i == 0) {
                    if (!cosmeticData.isRightVisible()) {
                        continue;
                    }
                }
                else if (!cosmeticData.isLeftVisible()) {
                    continue;
                }
                this.renderGlowingBorder((i == 0) ? x : (8 - x - width), y, width, height, cosmeticData.getColor(), animation, overlappingLeft, i == 0);
            }
            GlStateManager.translate(0.0, 0.0, -0.01);
        }
        Minecraft.getMinecraft().entityRenderer.enableLightmap();
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
    
    private void renderGlowingBorder(final int x, final int y, final int width, final int height, final Color color, final float alpha, final boolean overlapping, final boolean isLeftSide) {
        final float r = color.getRed() / 255.0f;
        final float g = color.getGreen() / 255.0f;
        final float b = color.getBlue() / 255.0f;
        final float middleAlpha = 0.4f;
        final float radius = 0.6f;
        final float basis = 0.0f;
        final float middleLeft = x + 0.0f;
        final float middleTop = y + 0.0f;
        final float middleRight = x + width - 0.0f;
        final float middleBottom = y + height - 0.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        this.drawGradient(middleLeft, middleTop - 0.6f, 0.0f, middleRight, middleTop - 0.6f, 0.0f, middleRight, middleTop, alpha, middleLeft, middleTop, alpha, r, g, b);
        this.drawGradient(middleRight, middleTop, alpha, middleRight + 0.6f, middleTop, 0.0f, middleRight + 0.6f, middleBottom, 0.0f, middleRight, middleBottom, alpha, r, g, b);
        this.drawGradient(middleLeft, middleTop, alpha, middleLeft - 0.6f, middleTop, 0.0f, middleLeft - 0.6f, middleBottom, 0.0f, middleLeft, middleBottom, alpha, r, g, b);
        this.drawGradient(middleLeft, middleBottom + 0.6f, 0.0f, middleRight, middleBottom + 0.6f, 0.0f, middleRight, middleBottom, alpha, middleLeft, middleBottom, alpha, r, g, b);
        this.drawGradient(middleLeft, middleTop, 0.4f, middleRight, middleTop, 0.4f, middleRight, middleBottom, 0.4f, middleLeft, middleBottom, 0.4f, r, g, b);
        final double cornerRadiusWidth = 0.6000000238418579 * Math.cos(Math.toRadians(45.0));
        final double cornerRadiusHeight = 0.6000000238418579 * Math.sin(Math.toRadians(45.0));
        final double lowCornerRadiusWidth = 0.6000000238418579 * Math.cos(Math.toRadians(22.5));
        final double lowCornerRadiusHeight = 0.6000000238418579 * Math.sin(Math.toRadians(22.5));
        final double highCornerRadiusWidth = 0.6000000238418579 * Math.cos(Math.toRadians(67.5));
        final double highCornerRadiusHeight = 0.6000000238418579 * Math.sin(Math.toRadians(67.5));
        Label_0545: {
            if (overlapping) {
                if (isLeftSide) {
                    if (!overlapping) {
                        break Label_0545;
                    }
                }
                else if (overlapping) {
                    break Label_0545;
                }
            }
            this.drawGradient(middleLeft - 0.6f, middleTop, 0.0f, middleLeft - lowCornerRadiusWidth, middleTop - lowCornerRadiusHeight, 0.0f, middleLeft - cornerRadiusWidth, middleTop - cornerRadiusHeight, 0.0f, middleLeft, middleTop, alpha, r, g, b);
            this.drawGradient(middleLeft - cornerRadiusWidth, middleTop - cornerRadiusHeight, 0.0f, middleLeft - highCornerRadiusWidth, middleTop - highCornerRadiusHeight, 0.0f, middleLeft, middleTop - 0.6f, 0.0f, middleLeft, middleTop, alpha, r, g, b);
        }
        Label_0679: {
            if (overlapping) {
                if (isLeftSide) {
                    if (overlapping) {
                        break Label_0679;
                    }
                }
                else if (!overlapping) {
                    break Label_0679;
                }
            }
            this.drawGradient(middleRight + 0.6f, middleTop, 0.0f, middleRight + lowCornerRadiusWidth, middleTop - lowCornerRadiusHeight, 0.0f, middleRight + cornerRadiusWidth, middleTop - cornerRadiusHeight, 0.0f, middleRight, middleTop, alpha, r, g, b);
            this.drawGradient(middleRight + cornerRadiusWidth, middleTop - cornerRadiusHeight, 0.0f, middleRight + highCornerRadiusWidth, middleTop - highCornerRadiusHeight, 0.0f, middleRight, middleTop - 0.6f, 0.0f, middleRight, middleTop, alpha, r, g, b);
        }
        Label_0813: {
            if (overlapping) {
                if (isLeftSide) {
                    if (!overlapping) {
                        break Label_0813;
                    }
                }
                else if (overlapping) {
                    break Label_0813;
                }
            }
            this.drawGradient(middleLeft - 0.6f, middleBottom, 0.0f, middleLeft - lowCornerRadiusWidth, middleBottom + lowCornerRadiusHeight, 0.0f, middleLeft - cornerRadiusWidth, middleBottom + cornerRadiusHeight, 0.0f, middleLeft, middleBottom, alpha, r, g, b);
            this.drawGradient(middleLeft - cornerRadiusWidth, middleBottom + cornerRadiusHeight, 0.0f, middleLeft - highCornerRadiusWidth, middleBottom + highCornerRadiusHeight, 0.0f, middleLeft, middleBottom + 0.6f, 0.0f, middleLeft, middleBottom, alpha, r, g, b);
        }
        Label_0947: {
            if (overlapping) {
                if (isLeftSide) {
                    if (overlapping) {
                        break Label_0947;
                    }
                }
                else if (!overlapping) {
                    break Label_0947;
                }
            }
            this.drawGradient(middleRight + 0.6f, middleBottom, 0.0f, middleRight + lowCornerRadiusWidth, middleBottom + lowCornerRadiusHeight, 0.0f, middleRight + cornerRadiusWidth, middleBottom + cornerRadiusHeight, 0.0f, middleRight, middleBottom, alpha, r, g, b);
            this.drawGradient(middleRight + cornerRadiusWidth, middleBottom + cornerRadiusHeight, 0.0f, middleRight + highCornerRadiusWidth, middleBottom + highCornerRadiusHeight, 0.0f, middleRight, middleBottom + 0.6f, 0.0f, middleRight, middleBottom, alpha, r, g, b);
        }
        tessellator.draw();
    }
    
    private void drawGradient(final double x1, final double y1, final float a1, final double x2, final double y2, final float a2, final double x3, final double y3, final float a3, final double x4, final double y4, final float a4, final float r, final float g, final float b) {
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        worldrenderer.pos(x1, y1, 0.0).tex(1.0, 1.0).color(r, g, b, a1).endVertex();
        worldrenderer.pos(x2, y2, 0.0).tex(1.0, 1.0).color(r, g, b, a2).endVertex();
        worldrenderer.pos(x3, y3, 0.0).tex(1.0, 1.0).color(r, g, b, a3).endVertex();
        worldrenderer.pos(x4, y4, 0.0).tex(1.0, 1.0).color(r, g, b, a4).endVertex();
    }
    
    @Override
    public int getCosmeticId() {
        return 32;
    }
    
    @Override
    public String getCosmeticName() {
        return "Snoxh Eyes";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticSnoxhData extends CosmeticData
    {
        private int x;
        private int y;
        private int width;
        private int height;
        private Color color;
        private boolean leftVisible;
        private boolean rightVisible;
        private int brightness;
        
        public CosmeticSnoxhData() {
            this.x = 1;
            this.y = 4;
            this.width = 2;
            this.height = 1;
            this.color = Color.WHITE;
            this.leftVisible = true;
            this.rightVisible = true;
            this.brightness = 120;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            Exception exception = null;
            try {
                this.x = Integer.parseInt(data[0]);
                this.y = Integer.parseInt(data[1]);
                this.width = Integer.parseInt(data[2]);
                this.height = Integer.parseInt(data[3]);
                this.leftVisible = (Integer.parseInt(data[4]) == 1);
                this.rightVisible = (Integer.parseInt(data[5]) == 1);
                this.color = Color.decode("#" + data[6]);
                this.brightness = Integer.parseInt(data[7]);
            }
            catch (final Exception e) {
                exception = e;
            }
            this.x = Math.min(this.x, 7);
            this.y = Math.min(this.y, 7);
            this.width = Math.min(this.width, 4);
            this.height = Math.min(this.height, 4);
            this.x = Math.max(this.x, 0);
            this.y = Math.max(this.y, 0);
            this.width = Math.max(this.width, 1);
            this.height = Math.max(this.height, 1);
            this.brightness = Math.min(this.brightness, 160);
            this.brightness = Math.max(this.brightness, 0);
            if (exception != null) {
                throw exception;
            }
        }
        
        @Override
        public void init(final User user) {
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
        
        public int getWidth() {
            return this.width;
        }
        
        public int getHeight() {
            return this.height;
        }
        
        public Color getColor() {
            return this.color;
        }
        
        public boolean isLeftVisible() {
            return this.leftVisible;
        }
        
        public boolean isRightVisible() {
            return this.rightVisible;
        }
        
        public int getBrightness() {
            return this.brightness;
        }
    }
}
