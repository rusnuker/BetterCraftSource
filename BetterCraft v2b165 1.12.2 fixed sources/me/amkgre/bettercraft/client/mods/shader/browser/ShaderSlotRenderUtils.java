// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.shader.browser;

import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.GuiScreen;

public class ShaderSlotRenderUtils<V> extends GuiScreen
{
    private final CopyOnWriteArrayList<ShaderSlot<V>> slots;
    protected boolean isCoolDowned;
    protected boolean init;
    protected ShaderSlot<V> selectedSlot;
    ShaderSlot<V> slotObject;
    private int count;
    private int yCount;
    private int lastSlotY;
    
    public ShaderSlotRenderUtils() {
        this.slots = new CopyOnWriteArrayList<ShaderSlot<V>>();
        this.isCoolDowned = false;
        this.init = false;
        this.count = 0;
        this.yCount = 0;
        this.lastSlotY = 0;
    }
    
    public void init(final List<V> list) {
        for (final V object : list) {
            this.slots.add(new ShaderSlot<V>(object, false));
        }
    }
    
    public void initSlots() {
        this.count = 0;
    }
    
    public void renderSlot(final int x, final int y, final int width, final int height, final List<V> list, final int mouseX, final int mouseY) {
        if (!this.init) {
            this.init(list);
            this.init = true;
        }
        this.yCount = y + this.count;
        if (this.lastSlotY + y + height < ShaderSlotRenderUtils.height) {
            this.count += 5;
        }
        this.slots.forEach(slot -> {
            if (this.yCount > ShaderSlotRenderUtils.height || this.yCount < 0) {
                this.doScrollbar(y2, height2);
            }
            this.onMouseClick(n, this.yCount, n2, height2, mouseX2, mouseY2, slot);
            if (slot.isSelected()) {
                drawBorderedRect(n, this.yCount, n + n2, this.yCount + height2, -2162267, Integer.MIN_VALUE);
            }
            else {
                Gui.drawRect(n, this.yCount, n + n2, this.yCount + height2, Integer.MIN_VALUE);
            }
            this.renderedSlotObj(n, this.yCount, n2, height2, slot.getObject(), slot);
            if (slot == this.slots.get(this.slots.size() - 1)) {
                this.lastSlotY = this.yCount;
            }
            this.yCount = this.yCount + height2 + 1;
        });
    }
    
    public void doScrollbar(final int y, final int height) {
        final String mouseScroll = String.valueOf(Mouse.getDWheel());
        if (!mouseScroll.equals("0")) {
            if (mouseScroll.contains("-")) {
                if (this.lastSlotY + y + height + 20 > ShaderSlotRenderUtils.height) {
                    this.count -= 20;
                }
            }
            else if (this.count < 0) {
                this.count += 20;
            }
        }
    }
    
    protected void onMouseClick(final int x, final int y, final int width, final int height, final int mouseX, final int mouseY, final ShaderSlot<V> slotObject) {
        this.slotObject = slotObject;
        if (mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height) {
            if (Mouse.isButtonDown(0)) {
                if (!this.isCoolDowned) {
                    if (this.selectedSlot != null) {
                        this.selectedSlot.setSelected(false);
                    }
                    slotObject.setSelected(!slotObject.isSelected());
                    this.isCoolDowned = true;
                    this.selectedSlot = slotObject;
                    this.clickSlotElement(slotObject.getObject());
                }
            }
            else {
                this.isCoolDowned = false;
            }
        }
    }
    
    public void clickSlotElement(final V listObj) {
    }
    
    public void renderedSlotObj(final int x, final int y, final int width, final int height, final V listObj, final ShaderSlot slotObj) {
    }
    
    public CopyOnWriteArrayList<ShaderSlot<V>> getSlots() {
        return this.slots;
    }
    
    public void setInit(final boolean value) {
        this.init = value;
    }
    
    public static void drawBorderedRect(final int x, final int y, final int x2, final int y2, final int borderedColor, final int color) {
        Gui.drawRect(x + 1, y + 1, x2 - 1, y2 - 1, color);
        Gui.drawRect(x, y + 1, x2, y, borderedColor);
        Gui.drawRect(x2 - 1, y, x2, y2, borderedColor);
        Gui.drawRect(x, y2, x2, y2 - 1, borderedColor);
        Gui.drawRect(x, y, x + 1, y2, borderedColor);
    }
}
