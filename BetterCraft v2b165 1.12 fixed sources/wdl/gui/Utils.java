// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import java.net.URI;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.Minecraft;

class Utils
{
    private static final Minecraft mc;
    private static final Logger logger;
    
    static {
        mc = Minecraft.getMinecraft();
        logger = LogManager.getLogger();
    }
    
    public static void drawGuiInfoBox(final String text, final int guiWidth, final int guiHeight, final int bottomPadding) {
        drawGuiInfoBox(text, 300, 100, guiWidth, guiHeight, bottomPadding);
    }
    
    public static void drawGuiInfoBox(final String text, final int infoBoxWidth, final int infoBoxHeight, final int guiWidth, final int guiHeight, final int bottomPadding) {
        if (text == null) {
            return;
        }
        final int infoX = guiWidth / 2 - infoBoxWidth / 2;
        final int infoY = guiHeight - bottomPadding - infoBoxHeight;
        int y = infoY + 5;
        Gui.drawRect(infoX, infoY, infoX + infoBoxWidth, infoY + infoBoxHeight, 2130706432);
        final List<String> lines = wordWrap(text, infoBoxWidth - 10);
        for (final String s : lines) {
            Utils.mc.fontRendererObj.drawString(s, infoX + 5, y, 16777215);
            y += Utils.mc.fontRendererObj.FONT_HEIGHT;
        }
    }
    
    public static List<String> wordWrap(String s, final int width) {
        s = s.replace("\r", "");
        final List<String> lines = Utils.mc.fontRendererObj.listFormattedStringToWidth(s, width);
        return lines;
    }
    
    public static void drawListBackground(final int topMargin, final int bottomMargin, final int top, final int left, final int bottom, final int right) {
        drawDarkBackground(top, left, bottom, right);
        drawBorder(topMargin, bottomMargin, top, left, bottom, right);
    }
    
    public static void drawDarkBackground(final int top, final int left, final int bottom, final int right) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        final Tessellator t = Tessellator.getInstance();
        final BufferBuilder b = t.getBuffer();
        Utils.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float textureSize = 32.0f;
        b.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        b.pos(0.0, bottom, 0.0).tex(0.0f / textureSize, bottom / textureSize).color(32, 32, 32, 255).endVertex();
        b.pos(right, bottom, 0.0).tex(right / textureSize, bottom / textureSize).color(32, 32, 32, 255).endVertex();
        b.pos(right, top, 0.0).tex(right / textureSize, top / textureSize).color(32, 32, 32, 255).endVertex();
        b.pos(left, top, 0.0).tex(left / textureSize, top / textureSize).color(32, 32, 32, 255).endVertex();
        t.draw();
    }
    
    public static void drawBorder(final int topMargin, final int bottomMargin, final int top, final int left, final int bottom, final int right) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        final byte padding = 4;
        Utils.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float textureSize = 32.0f;
        final Tessellator t = Tessellator.getInstance();
        final BufferBuilder b = t.getBuffer();
        final int upperBoxEnd = top + topMargin;
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        b.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        b.pos(left, upperBoxEnd, 0.0).tex(0.0, upperBoxEnd / textureSize).color(64, 64, 64, 255).endVertex();
        b.pos(right, upperBoxEnd, 0.0).tex(right / textureSize, upperBoxEnd / textureSize).color(64, 64, 64, 255).endVertex();
        b.pos(right, top, 0.0).tex(right / textureSize, top / textureSize).color(64, 64, 64, 255).endVertex();
        b.pos(left, top, 0.0).tex(0.0, top / textureSize).color(64, 64, 64, 255).endVertex();
        t.draw();
        final int lowerBoxStart = bottom - bottomMargin;
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        b.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        b.pos(left, bottom, 0.0).tex(0.0, bottom / textureSize).color(64, 64, 64, 255).endVertex();
        b.pos(right, bottom, 0.0).tex(right / textureSize, bottom / textureSize).color(64, 64, 64, 255).endVertex();
        b.pos(right, lowerBoxStart, 0.0).tex(right / textureSize, lowerBoxStart / textureSize).color(64, 64, 64, 255).endVertex();
        b.pos(left, lowerBoxStart, 0.0).tex(0.0, lowerBoxStart / textureSize).color(64, 64, 64, 255).endVertex();
        t.draw();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.disableTexture2D();
        b.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        b.pos(left, upperBoxEnd + padding, 0.0).tex(0.0, 1.0).color(0, 0, 0, 0).endVertex();
        b.pos(right, upperBoxEnd + padding, 0.0).tex(1.0, 1.0).color(0, 0, 0, 0).endVertex();
        b.pos(right, upperBoxEnd, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
        b.pos(left, upperBoxEnd, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
        t.draw();
        b.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        b.pos(left, lowerBoxStart, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
        b.pos(right, lowerBoxStart, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
        b.pos(right, lowerBoxStart - padding, 0.0).tex(1.0, 0.0).color(0, 0, 0, 0).endVertex();
        b.pos(left, lowerBoxStart - padding, 0.0).tex(0.0, 0.0).color(0, 0, 0, 0).endVertex();
        t.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }
    
    public static boolean isMouseOverTextBox(final int mouseX, final int mouseY, final GuiTextField textBox) {
        final int scaledX = mouseX - textBox.xPosition;
        final int scaledY = mouseY - textBox.yPosition;
        final int height = 20;
        return scaledX >= 0 && scaledX < textBox.getWidth() && scaledY >= 0 && scaledY < 20;
    }
    
    public static void openLink(final String path) {
        try {
            final Class<?> desktopClass = Class.forName("java.awt.Desktop");
            final Object desktop = desktopClass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            desktopClass.getMethod("browse", URI.class).invoke(desktop, new URI(path));
        }
        catch (final Throwable e) {
            Utils.logger.error("Couldn't open link", e);
        }
    }
    
    public static void drawStringWithShadow(final String s, final int x, final int y, final int color) {
        Utils.mc.fontRendererObj.drawStringWithShadow(s, (float)x, (float)y, color);
    }
}
