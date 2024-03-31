// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.elements;

import java.util.Iterator;
import net.labymod.core.LabyModCore;
import net.labymod.utils.ModColor;
import java.awt.Color;
import net.labymod.main.LabyMod;
import net.labymod.utils.Consumer;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;

public class DropDownMenu<T> extends Gui
{
    private static final DropDownEntryDrawer defaultDrawer;
    private String title;
    private T selected;
    private boolean enabled;
    private boolean open;
    private T hoverSelected;
    private int x;
    private int y;
    private int width;
    private int height;
    private int maxY;
    private ArrayList<T> list;
    private DropDownEntryDrawer entryDrawer;
    private Scrollbar scrollbar;
    private Consumer<T> hoverCallback;
    
    static {
        defaultDrawer = new DropDownEntryDrawer() {
            @Override
            public void draw(final Object object, final int x, final int y, final String trimmedEntry) {
                LabyMod.getInstance().getDrawUtils().drawString(trimmedEntry, x, y);
            }
        };
    }
    
    public DropDownMenu(final String title, final int x, final int y, final int width, final int height) {
        this.selected = null;
        this.enabled = true;
        this.hoverSelected = null;
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.maxY = Integer.MAX_VALUE;
        this.list = new ArrayList<T>();
        this.entryDrawer = null;
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public DropDownMenu<T> fill(final T[] values) {
        for (final T value : values) {
            this.list.add(value);
        }
        return this;
    }
    
    public void onScroll() {
        if (this.scrollbar != null) {
            this.scrollbar.mouseInput();
            this.scrollbar.setScrollY((int)(this.scrollbar.getScrollY() / this.scrollbar.getSpeed()) * this.scrollbar.getSpeed());
        }
    }
    
    public void onDrag(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.scrollbar != null) {
            this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
        }
    }
    
    public void onRelease(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.scrollbar != null) {
            this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        }
    }
    
    public boolean onClick(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.scrollbar != null && this.scrollbar.isHoverTotalScrollbar(mouseX, mouseY)) {
            this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
            return true;
        }
        if (!this.enabled || this.list.isEmpty()) {
            return false;
        }
        if (mouseX > this.x - 1 && mouseX < this.x + this.width + 1 && mouseY > this.y - 1 && mouseY < this.y + this.height + 1) {
            this.open = !this.open;
            if (this.open && this.list.size() > 10) {
                (this.scrollbar = new Scrollbar(13)).setSpeed(13);
                this.scrollbar.setListSize(this.list.size());
                this.scrollbar.setPosition(this.x + this.width - 5, this.y + this.height + 1, this.x + this.width, (this.maxY == Integer.MAX_VALUE) ? (this.y + this.height + 1 + 130 - 1) : this.maxY);
            }
            return true;
        }
        if (this.hoverSelected != null) {
            this.selected = this.hoverSelected;
            this.open = false;
            return true;
        }
        this.open = false;
        if (!this.open && this.hoverCallback != null) {
            this.hoverCallback.accept(null);
        }
        return false;
    }
    
    public boolean isMouseOver(final int mouseX, final int mouseY) {
        return mouseX > this.x - 1 && mouseX < this.x + this.width + 1 && mouseY > this.y - 1 && mouseY < this.y + this.height + 1;
    }
    
    public void draw(final int mouseX, final int mouseY) {
        final T prevHover = this.hoverSelected;
        this.hoverSelected = null;
        Gui.drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, Color.GRAY.getRGB());
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, Color.BLACK.getRGB());
        if (this.selected != null) {
            final String trimmedEntry = LabyMod.getInstance().getDrawUtils().trimStringToWidth(String.valueOf(ModColor.cl("f")) + this.selected, this.width - 5);
            ((this.entryDrawer == null) ? DropDownMenu.defaultDrawer : this.entryDrawer).draw(this.selected, this.x + 5, this.y + this.height / 2 - 4, trimmedEntry);
        }
        if (this.enabled && !this.list.isEmpty()) {
            for (int i = 0; i <= 5; ++i) {
                Gui.drawRect(this.x + this.width - 16 + i, this.y + this.height / 2 - 2 + i, this.x + this.width - 5 - i, this.y + this.height / 2 + 1 - 2 + i, Color.LIGHT_GRAY.getRGB());
            }
        }
        if (this.title != null) {
            LabyMod.getInstance().getDrawUtils().drawString(LabyModCore.getMinecraft().getFontRenderer().trimStringToWidth(this.title, this.width), this.x, this.y - 13);
        }
        if (this.open) {
            final int entryHeight = 13;
            final int maxPointY = this.y + this.height + 2 + 13 * this.list.size();
            final boolean buildUp = maxPointY > this.maxY;
            int yPositionList = buildUp ? (this.y - 13 - 1) : (this.y + this.height + 1);
            if (this.scrollbar != null) {
                yPositionList += (int)this.scrollbar.getScrollY();
            }
            for (final T option : this.list) {
                if (this.scrollbar == null || (yPositionList > this.y + 5 && yPositionList + 13 < this.scrollbar.getPosBottom() + 2)) {
                    final boolean hover = mouseX > this.x && mouseX < this.x + this.width && mouseY > yPositionList && mouseY < yPositionList + 13;
                    if (hover) {
                        this.hoverSelected = option;
                    }
                    Gui.drawRect(this.x - 1, yPositionList, this.x + this.width + 1, yPositionList + 13, ModColor.toRGB(0, 30, 70, 250));
                    Gui.drawRect(this.x, yPositionList + (buildUp ? 1 : 0), this.x + this.width, yPositionList + 13 - 1 + (buildUp ? 1 : 0), hover ? ModColor.toRGB(55, 55, 155, 215) : ModColor.toRGB(0, 10, 10, 250));
                    final String trimmedEntry2 = LabyMod.getInstance().getDrawUtils().trimStringToWidth(String.valueOf(ModColor.cl("f")) + option, this.width - 5);
                    ((this.entryDrawer == null) ? DropDownMenu.defaultDrawer : this.entryDrawer).draw(option, this.x + 5, yPositionList + 3, trimmedEntry2);
                }
                yPositionList += (buildUp ? -13 : 13);
            }
            if (this.scrollbar != null) {
                this.scrollbar.draw();
            }
        }
        if (((this.hoverSelected != null && prevHover == null) || (prevHover != null && !prevHover.equals(this.hoverSelected))) && this.hoverCallback != null) {
            this.hoverCallback.accept(this.hoverSelected);
        }
    }
    
    public void clear() {
        this.open = false;
        this.selected = null;
        this.list.clear();
        this.setSelected(null);
    }
    
    public void remove(final T type) {
        this.list.remove(type);
    }
    
    public void addOption(final T option) {
        this.list.add(option);
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public T getSelected() {
        return this.selected;
    }
    
    public void setSelected(final T selected) {
        this.selected = selected;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public T getHoverSelected() {
        return this.hoverSelected;
    }
    
    public void setHoverSelected(final T hoverSelected) {
        this.hoverSelected = hoverSelected;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public void setMaxY(final int maxY) {
        this.maxY = maxY;
    }
    
    public DropDownEntryDrawer getEntryDrawer() {
        return this.entryDrawer;
    }
    
    public void setEntryDrawer(final DropDownEntryDrawer entryDrawer) {
        this.entryDrawer = entryDrawer;
    }
    
    public void setHoverCallback(final Consumer<T> hoverCallback) {
        this.hoverCallback = hoverCallback;
    }
    
    public interface DropDownEntryDrawer
    {
        void draw(final Object p0, final int p1, final int p2, final String p3);
    }
}
