// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.mojang.inventory;

import net.minecraft.world.World;
import net.minecraft.entity.player.InventoryPlayer;
import net.labymod.mojang.inventory.scale.InventoryScaleChanger;
import net.minecraft.client.gui.inventory.GuiCrafting;

public class GuiCraftingCustom extends GuiCrafting
{
    private InventoryScaleChanger inventoryScaleChanger;
    
    public GuiCraftingCustom(final InventoryPlayer playerInv, final World worldIn) {
        super(playerInv, worldIn);
        this.inventoryScaleChanger = new InventoryScaleChanger();
    }
    
    @Override
    public void initGui() {
        if (this.inventoryScaleChanger.initGui()) {
            GuiCraftingCustom.width = this.inventoryScaleChanger.getScaledWidth();
            GuiCraftingCustom.height = this.inventoryScaleChanger.getScaledHeight();
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
