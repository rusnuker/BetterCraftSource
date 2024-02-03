// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.hud;

import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.client.gui.ScaledResolution;
import java.io.IOException;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.util.Iterator;
import java.util.Optional;
import java.util.HashMap;
import net.minecraft.client.gui.GuiScreen;

public class HUDConfigScreen extends GuiScreen
{
    private HashMap<IRender, ScreenPosition> renderers;
    private Optional<IRender> selectedRenderer;
    private int prevX;
    private int prevY;
    private boolean clear;
    
    public HUDConfigScreen(final HUDManager hm, final boolean clear) {
        this.renderers = new HashMap<IRender, ScreenPosition>();
        this.selectedRenderer = Optional.empty();
        this.clear = clear;
        for (final IRender irender : hm.getRegisteredRenderers()) {
            if (irender.isEnabled()) {
                ScreenPosition screenposition = irender.load();
                if (screenposition == null) {
                    screenposition = ScreenPosition.fromAbsolute(0, 0);
                }
                this.adjustBounds(irender, screenposition);
                this.renderers.put(irender, screenposition);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Color color;
        if (this.clear) {
            color = new Color(33, 41, 48, 100);
        }
        else {
            color = new Color(33, 41, 48, 255);
        }
        Gui.drawRect(0, 0, HUDConfigScreen.width, HUDConfigScreen.height, color.getRGB());
        final float f = this.zLevel;
        this.zLevel = 200.0f;
        this.drawOutline(0, 0, HUDConfigScreen.width - 1, HUDConfigScreen.height - 1, Color.white.getRGB());
        for (final IRender irender : this.renderers.keySet()) {
            final ScreenPosition screenposition = this.renderers.get(irender);
            irender.renderDummy(screenposition);
            if (this.selectedRenderer.isPresent() && irender == this.selectedRenderer.get()) {
                this.drawOutline(screenposition.getAbsoluteX(), screenposition.getAbsoluteY(), irender.getWidth(), irender.getHeight(), new Color(255, 255, 255).getRGB());
            }
        }
        if (this.selectedRenderer.isPresent() && Mouse.isButtonDown(0)) {
            final ScreenResolution screenresolution = new ScreenResolution(this.mc);
            if (this.renderers.get(this.selectedRenderer.get()).getAbsoluteX() + this.selectedRenderer.get().getWidth() / 2 <= screenresolution.getCenterX() + 3 && this.renderers.get(this.selectedRenderer.get()).getAbsoluteX() + this.selectedRenderer.get().getWidth() / 2 >= screenresolution.getCenterX() - 3) {
                drawRectEkn(screenresolution.getCenterX(), 0.0, screenresolution.getCenterX() + 0.7, Minecraft.getMinecraft().displayHeight, Color.MAGENTA.getRGB());
            }
            if (this.renderers.get(this.selectedRenderer.get()).getAbsoluteY() + this.selectedRenderer.get().getHeight() / 2 <= screenresolution.getCenterY() + 3 && this.renderers.get(this.selectedRenderer.get()).getAbsoluteY() + this.selectedRenderer.get().getHeight() / 2 >= screenresolution.getCenterY() - 3) {
                drawRectEkn(0.0, screenresolution.getCenterY(), Minecraft.getMinecraft().displayWidth, screenresolution.getCenterY() + 0.5, Color.MAGENTA.getRGB());
            }
        }
        this.zLevel = f;
    }
    
    private void drawOutline(final int x, final int y, final int w, final int h, final int color) {
        this.drawHorizontalLineEkn(x, x + w, y, color);
        this.drawHorizontalLineEkn(x, x + w, y + h, color);
        this.drawVerticalLineEkn(x, y + h, y, color);
        this.drawVerticalLineEkn(x + w, y + h, y, color);
    }
    
    public void drawHorizontalLineEkn(double startX, double endX, final double y, final int color) {
        if (endX < startX) {
            final double d0 = startX;
            startX = endX;
            endX = d0;
        }
        drawRectEkn(startX, y, endX, y + 0.5, color);
    }
    
    public void drawVerticalLineEkn(final double x, double startY, double endY, final int color) {
        if (endY < startY) {
            final double d0 = startY;
            startY = endY;
            endY = d0;
        }
        drawRectEkn(x, startY, x + 0.5, endY, color);
    }
    
    public static void drawRectEkn(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double d0 = left;
            left = right;
            right = d0;
        }
        if (top < bottom) {
            final double d2 = top;
            top = bottom;
            bottom = d2;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.renderers.entrySet().forEach(entry -> entry.getKey().save(entry.getValue()));
            this.mc.displayGuiScreen(null);
        }
    }
    
    @Override
    protected void mouseClickMove(final int x, final int y, final int button, final long time) {
        if (this.selectedRenderer.isPresent()) {
            this.moveSelectedRendererby(x - this.prevX, y - this.prevY);
        }
        this.prevX = x;
        this.prevY = y;
    }
    
    private void moveSelectedRendererby(final int offsetX, final int offsetY) {
        final IRender irender = this.selectedRenderer.get();
        final ScreenPosition screenposition = this.renderers.get(irender);
        screenposition.setAbsolute(screenposition.getAbsoluteX() + offsetX, screenposition.getAbsoluteY() + offsetY);
        this.adjustBounds(irender, screenposition);
    }
    
    @Override
    public void onGuiClosed() {
        HUDManager.setPaused(false);
        for (final IRender irender : this.renderers.keySet()) {
            irender.save(this.renderers.get(irender));
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
    
    private void adjustBounds(final IRender renderer, final ScreenPosition screenpos) {
        final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        final int screenWidth = scaledresolution.getScaledWidth();
        final int screenHeight = scaledresolution.getScaledHeight();
        final int absoluteX = Math.max(0, Math.min(screenpos.getAbsoluteX(), Math.max(screenWidth - renderer.getWidth(), 0)));
        final int absoluteY = Math.max(0, Math.min(screenpos.getAbsoluteY(), Math.max(screenHeight - renderer.getHeight(), 0)));
        screenpos.setAbsolute(absoluteX, absoluteY);
    }
    
    @Override
    protected void mouseClicked(final int x, final int y, final int button) throws IOException {
        this.loadMouseOver(this.prevX = x, this.prevY = y);
    }
    
    private void loadMouseOver(final int x, final int y) {
        this.selectedRenderer = this.renderers.keySet().stream().filter(new MouseOverFinder(x, y)).findFirst();
    }
    
    private class MouseOverFinder implements Predicate<IRender>
    {
        private int mouseX;
        private int mouseY;
        
        public MouseOverFinder(final int x, final int y) {
            this.mouseX = x;
            this.mouseY = y;
        }
        
        @Override
        public boolean test(final IRender r) {
            final ScreenPosition screenposition = HUDConfigScreen.this.renderers.get(r);
            final int i = screenposition.getAbsoluteX();
            final int j = screenposition.getAbsoluteY();
            return this.mouseX >= i && this.mouseX <= i + r.getWidth() && this.mouseY >= j && this.mouseY <= j + r.getHeight();
        }
    }
}
