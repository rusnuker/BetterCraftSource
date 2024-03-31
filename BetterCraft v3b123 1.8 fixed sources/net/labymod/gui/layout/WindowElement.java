// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.layout;

import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.gui.Gui;

public abstract class WindowElement<T extends WindowLayout> extends Gui
{
    public static final boolean SHOW_GRID = false;
    protected DrawUtils draw;
    protected T layout;
    protected int left;
    protected int top;
    protected int right;
    protected int bottom;
    protected boolean mouseOver;
    
    public WindowElement(final T layout) {
        this.layout = layout;
        this.draw = LabyMod.getInstance().getDrawUtils();
    }
    
    public WindowElement<T> construct(final double left, final double top, final double right, final double bottom) {
        this.left = (int)left;
        this.top = (int)top;
        this.right = (int)right;
        this.bottom = (int)bottom;
        this.init(this.layout.getButtonList(), this.left, this.top, this.right, this.bottom);
        return this;
    }
    
    protected abstract void init(final List<GuiButton> p0, final int p1, final int p2, final int p3, final int p4);
    
    public void draw(final int mouseX, final int mouseY) {
        this.mouseOver = (mouseX > this.left && mouseX < this.right && mouseY > this.top && mouseY < this.bottom);
    }
    
    public abstract boolean mouseClicked(final int p0, final int p1, final int p2);
    
    public abstract void mouseClickMove(final int p0, final int p1);
    
    public abstract void mouseReleased(final int p0, final int p1, final int p2);
    
    public abstract void mouseInput();
    
    public abstract void actionPerformed(final GuiButton p0);
    
    public abstract void keyTyped(final char p0, final int p1);
    
    public abstract void updateScreen();
    
    public DrawUtils getDraw() {
        return this.draw;
    }
    
    public T getLayout() {
        return this.layout;
    }
    
    public int getLeft() {
        return this.left;
    }
    
    public int getTop() {
        return this.top;
    }
    
    public int getRight() {
        return this.right;
    }
    
    public int getBottom() {
        return this.bottom;
    }
    
    public boolean isMouseOver() {
        return this.mouseOver;
    }
}
