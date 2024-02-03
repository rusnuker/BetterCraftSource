// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui.views;

import net.labymod.addons.resourcepacks24.Resourcepacks24;
import net.labymod.addons.resourcepacks24.gui.elements.basement.PackElement;
import net.labymod.utils.ModColor;
import net.labymod.utils.DrawUtils;
import net.labymod.main.LabyMod;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.addons.resourcepacks24.gui.views.shared.SharedView;
import net.labymod.addons.resourcepacks24.gui.GuiResourcepacks24;

public abstract class View
{
    public static final int PADDING = 3;
    protected final GuiResourcepacks24 gui;
    protected final SharedView shared;
    protected String title;
    public double x;
    public double y;
    public double width;
    public double height;
    protected double margin;
    protected double postRenderCursorY;
    protected Scrollbar scrollbar;
    private boolean lastMouseOver;
    protected boolean loaded;
    protected boolean pendingScrollbarUpdate;
    
    public View(final GuiResourcepacks24 gui, final String title) {
        this.postRenderCursorY = 0.0;
        this.scrollbar = new Scrollbar(1);
        this.loaded = false;
        this.pendingScrollbarUpdate = false;
        this.gui = gui;
        this.shared = gui.getSharedView();
        this.title = title;
    }
    
    public void init(final double x, final double y, final double width, final double height, final double margin) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.margin = margin;
        this.scrollbar.update(1);
        this.scrollbar.setPosition(x + width - 5.0, y, x + width, y + height);
        this.scrollbar.setSpeed(25);
        this.pendingScrollbarUpdate = true;
    }
    
    public void renderPre(final int mouseX, final int mouseY) {
        this.lastMouseOver = (mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height);
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
    }
    
    public void renderPost(final int mouseX, final int mouseY) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.drawCenteredString(String.valueOf(ModColor.cl('l')) + ModColor.cl('n') + this.title, this.x + this.width / 2.0, this.y - 12.0);
        draw.drawGradientShadowTop(this.y, this.x, this.x + this.width);
        draw.drawGradientShadowBottom(this.y + this.height, this.x, this.x + this.width);
        if (this.loaded) {
            if (this.shared.draggingElement != null && this.lastMouseOver && this.shared.hoverElement == null) {
                draw.drawRectBorder(this.x, this.y, this.x + this.width, this.y + this.height, ModColor.toRGB(255, 255, 255, 100), 1.0);
                this.shared.hoverView = this;
            }
            if (this.pendingScrollbarUpdate) {
                this.pendingScrollbarUpdate = false;
                this.scrollbar.setEntryHeight(this.postRenderCursorY - this.y);
            }
            this.scrollbar.draw(mouseX, mouseY);
        }
        else {
            draw.drawCenteredString("Loading..", this.x + this.width / 2.0, this.y + this.height / 2.0);
        }
    }
    
    protected void drawPack(final PackElement element, final double x, final double y, final double width, final double height, final int mouseX, final int mouseY) {
        this.drawPack(element, x, y, width, height, false, -1, -1, mouseX, mouseY);
    }
    
    protected void drawPack(final PackElement element, final double x, final double y, final double width, final double height, final boolean selected, final int index, final int total, final int mouseX, final int mouseY) {
        final boolean visible = this.shared.draggingElement == null || !this.shared.draggingElement.isValid() || this.shared.draggingElement.getElement() != element;
        final boolean inView = y + height > 0.0 && y < GuiResourcepacks24.height;
        if (inView && visible && element.draw(x + 3.0, y, width - 6.0, height, mouseX, mouseY) && (index != -1 || !selected)) {
            element.drawControls(x + 3.0, y, width, height, index == 0, index == total - 1, selected, mouseX, mouseY, this.gui);
            this.shared.hoverElement = element;
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
    }
    
    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
    }
    
    public void handleMouseInput() {
        if (this.lastMouseOver) {
            this.scrollbar.mouseInput();
        }
    }
    
    public boolean isInsideView(final int mouseX, final int mouseY) {
        return mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
    }
    
    protected void throwError(final String message) {
        this.shared.lastErrorMessage = message;
    }
    
    public void onLoaded(final Class<? extends View> clazz) {
        if (clazz.equals(this.getClass())) {
            this.loaded = true;
            this.pendingScrollbarUpdate = true;
        }
    }
    
    public void loadRepository(final Resourcepacks24 context) {
    }
}
