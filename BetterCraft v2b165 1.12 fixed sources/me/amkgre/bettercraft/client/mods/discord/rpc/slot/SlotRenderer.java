// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.slot;

import org.lwjgl.input.Mouse;
import me.amkgre.bettercraft.client.utils.RenderUtils;
import net.minecraft.client.gui.Gui;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.GuiScreen;

public abstract class SlotRenderer<V> extends GuiScreen
{
    protected int lastYPosition;
    protected boolean isCreated;
    protected boolean isThreadStartet;
    protected boolean isSelectCooldown;
    protected int slotWidth;
    protected int slotHeight;
    protected Slot<V> selectedSlot;
    protected int scrolledCount;
    protected CopyOnWriteArrayList<Slot<V>> slots;
    
    public abstract List<V> getList();
    
    public abstract int getXPosition();
    
    public abstract int getYPosition();
    
    public void setSlotWidth(final int value) {
        this.slotWidth = value;
    }
    
    public int getSlotWidth() {
        return this.slotWidth;
    }
    
    public void setSlotHeight(final int value) {
        this.slotHeight = value;
    }
    
    public int getSlotHeight() {
        return this.slotHeight;
    }
    
    public void setSelectedSlot(final Slot<V> value) {
        this.selectedSlot = value;
    }
    
    public Slot<V> getSelectedSlot() {
        return this.selectedSlot;
    }
    
    public int getScrolledCount() {
        return this.scrolledCount;
    }
    
    public CopyOnWriteArrayList<Slot<V>> getSlots() {
        return this.slots;
    }
    
    public SlotRenderer() {
        this.lastYPosition = 0;
        this.isCreated = false;
        this.isThreadStartet = false;
        this.isSelectCooldown = false;
        this.slotWidth = 200;
        this.slotHeight = 40;
        this.scrolledCount = 0;
        this.slots = new CopyOnWriteArrayList<Slot<V>>();
        this.getList().forEach(element -> this.getSlots().add(new Slot<V>((V)element)));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        int slotIndex = new Integer(this.getYPosition());
        for (final Slot<V> slot : this.getSlots()) {
            this.drawSlot(this.getXPosition(), slotIndex + this.getScrolledCount(), slot);
            if (this.isCreated) {
                this.mouseHandle(mouseX, mouseY, this.getXPosition(), slotIndex + this.getScrolledCount(), slot);
            }
            if (slot == this.getSlots().get(this.getSlots().size() - 1)) {
                this.lastYPosition = slotIndex;
            }
            slotIndex += this.getSlotHeight() + 1;
        }
        if (!this.isCreated && !this.isThreadStartet) {
            this.isThreadStartet = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200L);
                    }
                    catch (final Throwable t) {}
                    SlotRenderer.this.isCreated = true;
                }
            }).start();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void initGui() {
        this.scrolledCount = 0;
    }
    
    public void drawSlot(final int xPosition, final int yPosition, final Slot<V> slot) {
        Gui.drawRect(xPosition, yPosition, xPosition + this.getSlotWidth(), yPosition + this.getSlotHeight(), Integer.MIN_VALUE);
        if (slot.isSelected()) {
            RenderUtils.drawBorderedRect(xPosition, yPosition, xPosition + this.getSlotWidth(), yPosition + this.getSlotHeight(), Integer.MAX_VALUE, -16777216);
        }
    }
    
    protected void mouseHandle(final int mouseX, final int mouseY, final int xPosition, final int yPosition, final Slot<V> slot) {
        if (mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.getSlotWidth() && mouseY < yPosition + this.getSlotHeight()) {
            final String text = "§4Wait 10 seconds before you switch again";
            RenderUtils.drawBorderedRect(mouseX + 5, mouseY - 2, mouseX + this.mc.fontRendererObj.getStringWidth(text) + 8, mouseY - 10, -16777216, Integer.MIN_VALUE);
            this.mc.fontRendererObj.drawString(text, mouseX + 7, mouseY - 10, -1);
        }
        if (yPosition + this.getSlotHeight() + 20 >= SlotRenderer.height || yPosition < 0) {
            final String mouseScroll = String.valueOf(Mouse.getDWheel());
            if (!mouseScroll.equals("0")) {
                if (mouseScroll.contains("-")) {
                    if (this.lastYPosition + yPosition + this.getSlotHeight() - 20 >= SlotRenderer.height) {
                        this.scrolledCount -= 20;
                    }
                }
                else if (this.getScrolledCount() < 0) {
                    this.scrolledCount += 20;
                }
            }
        }
        if (mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.getSlotWidth() && mouseY < yPosition + this.getSlotHeight()) {
            this.onHoverSlot(slot);
            if (Mouse.isButtonDown(0)) {
                this.onClickSlot(slot, MouseClickType.LEFT);
                if (!this.isSelectCooldown) {
                    if (this.selectedSlot != null) {
                        this.selectedSlot.setSelected(false);
                        if (this.selectedSlot == slot) {
                            this.onDoubleClickSlot(slot);
                        }
                    }
                    slot.setSelected(true);
                    this.selectedSlot = slot;
                    this.isSelectCooldown = true;
                }
            }
            else if (Mouse.isButtonDown(1)) {
                this.onClickSlot(slot, MouseClickType.WHEEL);
            }
            else if (Mouse.isButtonDown(2)) {
                this.onClickSlot(slot, MouseClickType.RIGHT);
            }
            else {
                this.isSelectCooldown = false;
            }
        }
    }
    
    public void onClickSlot(final Slot<V> slot, final MouseClickType clickType) {
    }
    
    public void onDoubleClickSlot(final Slot<V> slot) {
    }
    
    public void onHoverSlot(final Slot<V> slot) {
    }
}
