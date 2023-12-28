// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.example;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import net.montoyo.mcef.api.IBrowser;
import net.minecraft.client.gui.GuiScreen;

public class ScreenCfg extends GuiScreen
{
    private IBrowser browser;
    private int width;
    private int height;
    private int x;
    private int y;
    private int offsetX;
    private int offsetY;
    private boolean dragging;
    private boolean resizing;
    private boolean drawSquare;
    
    public ScreenCfg(final IBrowser b, final String vId) {
        this.width = 320;
        this.height = 180;
        this.x = 10;
        this.y = 10;
        this.offsetX = 0;
        this.offsetY = 0;
        this.dragging = false;
        this.resizing = false;
        this.drawSquare = true;
        this.browser = b;
        if (vId != null) {
            b.loadURL("https://www.youtube.com/embed/" + vId + "?autoplay=1");
        }
        b.resize(this.width, this.height);
    }
    
    @Override
    public void handleInput() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == 1) {
                this.drawSquare = false;
                ExampleMod.INSTANCE.hudBrowser = this;
                this.browser.injectMouseMove(-10, -10, 0, true);
                this.mc.displayGuiScreen(null);
                return;
            }
        }
        while (Mouse.next()) {
            final int btn = Mouse.getEventButton();
            final boolean pressed = Mouse.getEventButtonState();
            final int sx = Mouse.getEventX();
            final int sy = this.mc.displayHeight - Mouse.getEventY();
            if (btn == 1 && pressed && sx >= this.x && sy >= this.y && sx < this.x + this.width && sy < this.y + this.height) {
                this.browser.injectMouseMove(sx - this.x, sy - this.y, 0, false);
                this.browser.injectMouseButton(sx - this.x, sy - this.y, 0, 1, true, 1);
                this.browser.injectMouseButton(sx - this.x, sy - this.y, 0, 1, false, 1);
            }
            else if (this.dragging) {
                if (btn == 0 && !pressed) {
                    this.dragging = false;
                }
                else {
                    this.x = sx + this.offsetX;
                    this.y = sy + this.offsetY;
                }
            }
            else if (this.resizing) {
                if (btn == 0 && !pressed) {
                    this.resizing = false;
                    this.browser.resize(this.width, this.height);
                }
                else {
                    final int w = sx - this.x;
                    final int h = sy - this.y;
                    if (w < 32 || h < 18) {
                        continue;
                    }
                    if (h >= w) {
                        final double dw = h * 1.7777777777777777;
                        this.width = (int)dw;
                        this.height = h;
                    }
                    else {
                        final double dh = w * 0.5625;
                        this.width = w;
                        this.height = (int)dh;
                    }
                }
            }
            else if (pressed && btn == 0 && sx >= this.x && sy >= this.y && sx < this.x + this.width && sy < this.y + this.height) {
                this.dragging = true;
                this.offsetX = this.x - sx;
                this.offsetY = this.y - sy;
            }
            else {
                if (!pressed || btn != 0 || sx < this.x + this.width || sy < this.y + this.height || sx >= this.x + this.width + 10 || sy >= this.y + this.height + 10) {
                    continue;
                }
                this.resizing = true;
            }
        }
    }
    
    @Override
    public void drawScreen(final int i1, final int i2, final float f) {
        GL11.glDisable(2929);
        GL11.glEnable(3553);
        this.browser.draw(this.unscaleX(this.x), this.unscaleY(this.height + this.y), this.unscaleX(this.width + this.x), this.unscaleY(this.y));
        if (this.drawSquare) {
            final Tessellator t = Tessellator.getInstance();
            final BufferBuilder vb = t.getBuffer();
            vb.begin(2, DefaultVertexFormats.POSITION_COLOR);
            vb.pos(this.unscaleX(this.x + this.width), this.unscaleY(this.y + this.height), 0.0).color(255, 255, 255, 255).endVertex();
            vb.pos(this.unscaleX(this.x + this.width + 10), this.unscaleY(this.y + this.height), 0.0).color(255, 255, 255, 255).endVertex();
            vb.pos(this.unscaleX(this.x + this.width + 10), this.unscaleY(this.y + this.height + 10), 0.0).color(255, 255, 255, 255).endVertex();
            vb.pos(this.unscaleX(this.x + this.width), this.unscaleY(this.y + this.height + 10), 0.0).color(255, 255, 255, 255).endVertex();
            t.draw();
        }
        GL11.glEnable(2929);
    }
    
    public double unscaleX(final int x) {
        return x / (double)this.mc.displayWidth * GuiScreen.width;
    }
    
    public double unscaleY(final int y) {
        return y / (double)this.mc.displayHeight * GuiScreen.height;
    }
}
