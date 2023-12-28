// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.client.gui.GuiButton;
import java.io.IOException;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;

public class GuiCrafting extends GuiContainer implements IRecipeShownListener
{
    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES;
    private GuiButtonImage field_192049_w;
    private final GuiRecipeBook field_192050_x;
    private boolean field_193112_y;
    
    static {
        CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/crafting_table.png");
    }
    
    public GuiCrafting(final InventoryPlayer playerInv, final World worldIn) {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }
    
    public GuiCrafting(final InventoryPlayer playerInv, final World worldIn, final BlockPos blockPosition) {
        super(new ContainerWorkbench(playerInv, worldIn, blockPosition));
        this.field_192050_x = new GuiRecipeBook();
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.field_193112_y = (GuiCrafting.width < 379);
        this.field_192050_x.func_194303_a(GuiCrafting.width, GuiCrafting.height, this.mc, this.field_193112_y, ((ContainerWorkbench)this.inventorySlots).craftMatrix);
        this.guiLeft = this.field_192050_x.func_193011_a(this.field_193112_y, GuiCrafting.width, this.xSize);
        this.field_192049_w = new GuiButtonImage(10, this.guiLeft + 5, GuiCrafting.height / 2 - 49, 20, 18, 0, 168, 19, GuiCrafting.CRAFTING_TABLE_GUI_TEXTURES);
        this.buttonList.add(this.field_192049_w);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.field_192050_x.func_193957_d();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        if (this.field_192050_x.func_191878_b() && this.field_193112_y) {
            this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
            this.field_192050_x.func_191861_a(mouseX, mouseY, partialTicks);
        }
        else {
            this.field_192050_x.func_191861_a(mouseX, mouseY, partialTicks);
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.field_192050_x.func_191864_a(this.guiLeft, this.guiTop, true, partialTicks);
        }
        this.func_191948_b(mouseX, mouseY);
        this.field_192050_x.func_191876_c(this.guiLeft, this.guiTop, mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiCrafting.CRAFTING_TABLE_GUI_TEXTURES);
        final int i = this.guiLeft;
        final int j = (GuiCrafting.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
    
    @Override
    protected boolean isPointInRegion(final int rectX, final int rectY, final int rectWidth, final int rectHeight, final int pointX, final int pointY) {
        return (!this.field_193112_y || !this.field_192050_x.func_191878_b()) && super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (!this.field_192050_x.func_191862_a(mouseX, mouseY, mouseButton) && (!this.field_193112_y || !this.field_192050_x.func_191878_b())) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    protected boolean func_193983_c(final int p_193983_1_, final int p_193983_2_, final int p_193983_3_, final int p_193983_4_) {
        final boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
        return this.field_192050_x.func_193955_c(p_193983_1_, p_193983_2_, this.guiLeft, this.guiTop, this.xSize, this.ySize) && flag;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 10) {
            this.field_192050_x.func_193014_a(this.field_193112_y, ((ContainerWorkbench)this.inventorySlots).craftMatrix);
            this.field_192050_x.func_191866_a();
            this.guiLeft = this.field_192050_x.func_193011_a(this.field_193112_y, GuiCrafting.width, this.xSize);
            this.field_192049_w.func_191746_c(this.guiLeft + 5, GuiCrafting.height / 2 - 49);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (!this.field_192050_x.func_191859_a(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    protected void handleMouseClick(final Slot slotIn, final int slotId, final int mouseButton, final ClickType type) {
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
        this.field_192050_x.func_191874_a(slotIn);
    }
    
    @Override
    public void func_192043_J_() {
        this.field_192050_x.func_193948_e();
    }
    
    @Override
    public void onGuiClosed() {
        this.field_192050_x.func_191871_c();
        super.onGuiClosed();
    }
    
    @Override
    public GuiRecipeBook func_194310_f() {
        return this.field_192050_x;
    }
}
