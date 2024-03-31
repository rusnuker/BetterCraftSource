// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.gui;

import org.lwjgl.opengl.GL11;
import com.mcf.davidee.nbtedit.NBTStringHelper;
import com.mcf.davidee.nbtedit.nbt.NamedNBT;
import com.mcf.davidee.nbtedit.nbt.Node;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;

public class GuiNBTNode extends Gui
{
    public static final ResourceLocation WIDGET_TEXTURE;
    private Minecraft mc;
    private Node<NamedNBT> node;
    private GuiNBTTree tree;
    protected int width;
    protected int height;
    protected int x;
    protected int y;
    private String displayString;
    
    static {
        WIDGET_TEXTURE = new ResourceLocation("nbtedit", "textures/gui/widgets.png");
    }
    
    public GuiNBTNode(final GuiNBTTree tree, final Node<NamedNBT> node, final int x, final int y) {
        this.mc = Minecraft.getMinecraft();
        this.tree = tree;
        this.node = node;
        this.x = x;
        this.y = y;
        this.height = this.mc.fontRendererObj.FONT_HEIGHT;
        this.updateDisplay();
    }
    
    private boolean inBounds(final int mx, final int my) {
        return mx >= this.x && my >= this.y && mx < this.width + this.x && my < this.height + this.y;
    }
    
    private boolean inHideShowBounds(final int mx, final int my) {
        return mx >= this.x - 9 && my >= this.y && mx < this.x && my < this.y + this.height;
    }
    
    public boolean shouldDrawChildren() {
        return this.node.shouldDrawChildren();
    }
    
    public boolean clicked(final int mx, final int my) {
        return this.inBounds(mx, my);
    }
    
    public boolean hideShowClicked(final int mx, final int my) {
        if (this.node.hasChildren() && this.inHideShowBounds(mx, my)) {
            this.node.setDrawChildren(!this.node.shouldDrawChildren());
            return true;
        }
        return false;
    }
    
    public Node<NamedNBT> getNode() {
        return this.node;
    }
    
    public void shift(final int dy) {
        this.y += dy;
    }
    
    public void updateDisplay() {
        this.displayString = NBTStringHelper.getNBTNameSpecial(this.node.getObject());
        this.width = this.mc.fontRendererObj.getStringWidth(this.displayString) + 12;
    }
    
    public void draw(final int mx, final int my) {
        final boolean selected = this.tree.getFocused() == this.node;
        final boolean hover = this.inBounds(mx, my);
        final boolean chHover = this.inHideShowBounds(mx, my);
        final int color = selected ? 255 : (hover ? 16777120 : (this.node.hasParent() ? 14737632 : -6250336));
        this.mc.renderEngine.bindTexture(GuiNBTNode.WIDGET_TEXTURE);
        if (selected) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.drawRect(this.x + 11, this.y, this.x + this.width, this.y + this.height, Integer.MIN_VALUE);
        }
        if (this.node.hasChildren()) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.x - 9, this.y, this.node.shouldDrawChildren() ? 9 : 0, chHover ? this.height : 0, 9, this.height);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(this.x + 1, this.y, (this.node.getObject().getNBT().getId() - 1) * 9, 18, 9, 9);
        this.drawString(this.mc.fontRendererObj, this.displayString, this.x + 11, this.y + (this.height - 8) / 2, color);
    }
    
    public boolean shouldDraw(final int top, final int bottom) {
        return this.y + this.height >= top && this.y <= bottom;
    }
}
