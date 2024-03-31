// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.Minecraft;

public class AnimationUtils
{
    protected Minecraft mc;
    public int x;
    public int y;
    public int fromX;
    public int fromY;
    public int toX;
    public int toY;
    public int speed;
    public boolean XtoHigherThanXFrom;
    public boolean YtoHigherThanYFrom;
    
    public AnimationUtils(final int fromX, final int fromY, final int toX, final int toY, final int speed) {
        this.mc = Minecraft.getMinecraft();
        this.XtoHigherThanXFrom = true;
        this.YtoHigherThanYFrom = true;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.x = fromX;
        this.y = fromY;
        this.speed = speed;
        this.XtoHigherThanXFrom = (toX > fromX);
        this.YtoHigherThanYFrom = (toY > fromY);
    }
    
    public AnimationUtils(final int fromX, final int fromY, final int toX, final int toY) {
        this.mc = Minecraft.getMinecraft();
        this.XtoHigherThanXFrom = true;
        this.YtoHigherThanYFrom = true;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.x = fromX;
        this.y = fromY;
        this.speed = 1;
        this.XtoHigherThanXFrom = (toX > fromX);
        this.YtoHigherThanYFrom = (toY > fromY);
    }
    
    public AnimationUtils(final GuiButton button, final int toX, final int toY, final int speed) {
        this(button.xPosition, button.yPosition, toX, toY, speed);
    }
    
    public AnimationUtils(final GuiButton button, final int toX, final int toY) {
        this(button.xPosition, button.yPosition, toX, toY);
    }
    
    public AnimationUtils(final int fromX, final int fromY, final GuiButton button, final int speed) {
        this(fromX, fromY, button.xPosition, button.yPosition, speed);
    }
    
    public AnimationUtils(final int fromX, final int fromY, final GuiButton button) {
        this(fromX, fromY, button.xPosition, button.yPosition);
    }
    
    public void drawAnimatedButton(final GuiButton button) {
        button.xPosition = this.nextX();
        button.yPosition = this.nextY();
    }
    
    public void drawAnimatedText(final String text, final int color, final boolean shadow) {
        this.nextX();
        this.nextY();
        if (shadow) {
            this.mc.fontRendererObj.drawStringWithShadow(text, (float)this.x, (float)this.y, color);
        }
        else {
            this.mc.fontRendererObj.drawString(text, this.x, this.y, color);
        }
    }
    
    public int nextX() {
        if (this.XtoHigherThanXFrom) {
            if (this.x < this.toX) {
                this.x = ((this.x + this.speed < this.toX) ? (this.x += this.speed) : this.toX);
            }
        }
        else if (this.x > this.toX) {
            this.x = ((this.x + this.speed < this.toX) ? (--this.x) : this.toX);
        }
        return this.x;
    }
    
    public int nextY() {
        if (this.YtoHigherThanYFrom) {
            if (this.y < this.toY) {
                this.y = ((this.y + this.speed < this.toY) ? (++this.y) : this.toY);
            }
        }
        else if (this.y > this.toY) {
            this.y = ((this.y + this.speed < this.toY) ? (--this.y) : this.toY);
        }
        return this.y;
    }
}
