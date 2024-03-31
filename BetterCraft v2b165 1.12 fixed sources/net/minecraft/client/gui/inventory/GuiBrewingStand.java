// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBrewingStand extends GuiContainer
{
    private static final ResourceLocation BREWING_STAND_GUI_TEXTURES;
    private static final int[] BUBBLELENGTHS;
    private final InventoryPlayer playerInventory;
    private final IInventory tileBrewingStand;
    
    static {
        BREWING_STAND_GUI_TEXTURES = new ResourceLocation("textures/gui/container/brewing_stand.png");
        BUBBLELENGTHS = new int[] { 29, 24, 20, 16, 11, 6, 0 };
    }
    
    public GuiBrewingStand(final InventoryPlayer playerInv, final IInventory p_i45506_2_) {
        super(new ContainerBrewingStand(playerInv, p_i45506_2_));
        this.playerInventory = playerInv;
        this.tileBrewingStand = p_i45506_2_;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.func_191948_b(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final String s = this.tileBrewingStand.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiBrewingStand.BREWING_STAND_GUI_TEXTURES);
        final int i = (GuiBrewingStand.width - this.xSize) / 2;
        final int j = (GuiBrewingStand.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        final int k = this.tileBrewingStand.getField(1);
        final int l = MathHelper.clamp((18 * k + 20 - 1) / 20, 0, 18);
        if (l > 0) {
            this.drawTexturedModalRect(i + 60, j + 44, 176, 29, l, 4);
        }
        final int i2 = this.tileBrewingStand.getField(0);
        if (i2 > 0) {
            int j2 = (int)(28.0f * (1.0f - i2 / 400.0f));
            if (j2 > 0) {
                this.drawTexturedModalRect(i + 97, j + 16, 176, 0, 9, j2);
            }
            j2 = GuiBrewingStand.BUBBLELENGTHS[i2 / 2 % 7];
            if (j2 > 0) {
                this.drawTexturedModalRect(i + 63, j + 14 + 29 - j2, 185, 29 - j2, 12, j2);
            }
        }
    }
}
