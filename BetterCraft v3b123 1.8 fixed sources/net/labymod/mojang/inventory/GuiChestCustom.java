// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.mojang.inventory;

import net.minecraft.inventory.IInventory;
import net.labymod.mojang.inventory.scale.InventoryScaleChanger;
import net.minecraft.client.gui.inventory.GuiChest;

public class GuiChestCustom extends GuiChest
{
    private InventoryScaleChanger inventoryScaleChanger;
    
    public GuiChestCustom(final IInventory upperInv, final IInventory lowerInv) {
        super(upperInv, lowerInv);
        this.inventoryScaleChanger = new InventoryScaleChanger();
    }
    
    @Override
    public void initGui() {
        if (this.inventoryScaleChanger.initGui()) {
            GuiChestCustom.width = this.inventoryScaleChanger.getScaledWidth();
            GuiChestCustom.height = this.inventoryScaleChanger.getScaledHeight();
        }
        super.initGui();
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, final float partialTicks) {
        if (this.inventoryScaleChanger.drawScreen(mouseX, mouseY)) {
            mouseX = this.inventoryScaleChanger.getMouseX();
            mouseY = this.inventoryScaleChanger.getMouseY();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
