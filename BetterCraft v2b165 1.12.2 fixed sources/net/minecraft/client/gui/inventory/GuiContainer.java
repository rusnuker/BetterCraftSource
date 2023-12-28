// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import net.minecraft.inventory.ClickType;
import java.util.Iterator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiContainer extends GuiScreen
{
    public static final ResourceLocation INVENTORY_BACKGROUND;
    protected int xSize;
    protected int ySize;
    public Container inventorySlots;
    protected int guiLeft;
    protected int guiTop;
    private Slot theSlot;
    private Slot clickedSlot;
    private boolean isRightMouseClick;
    private ItemStack draggedStack;
    private int touchUpX;
    private int touchUpY;
    private Slot returningStackDestSlot;
    private long returningStackTime;
    private ItemStack returningStack;
    private Slot currentDragTargetSlot;
    private long dragItemDropDelay;
    protected final Set<Slot> dragSplittingSlots;
    protected boolean dragSplitting;
    private int dragSplittingLimit;
    private int dragSplittingButton;
    private boolean ignoreMouseUp;
    private int dragSplittingRemnant;
    private long lastClickTime;
    private Slot lastClickSlot;
    private int lastClickButton;
    private boolean doubleClick;
    private ItemStack shiftClickedSlot;
    
    static {
        INVENTORY_BACKGROUND = new ResourceLocation("textures/gui/container/inventory.png");
    }
    
    public GuiContainer(final Container inventorySlotsIn) {
        this.xSize = 176;
        this.ySize = 166;
        this.draggedStack = ItemStack.field_190927_a;
        this.returningStack = ItemStack.field_190927_a;
        this.dragSplittingSlots = (Set<Slot>)Sets.newHashSet();
        this.shiftClickedSlot = ItemStack.field_190927_a;
        this.inventorySlots = inventorySlotsIn;
        this.ignoreMouseUp = true;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.mc.player.openContainer = this.inventorySlots;
        this.guiLeft = (GuiContainer.width - this.xSize) / 2;
        this.guiTop = (GuiContainer.height - this.ySize) / 2;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final int i = this.guiLeft;
        final int j = this.guiTop;
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)i, (float)j, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableRescaleNormal();
        this.theSlot = null;
        final int k = 240;
        final int l = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        for (int i2 = 0; i2 < this.inventorySlots.inventorySlots.size(); ++i2) {
            final Slot slot = this.inventorySlots.inventorySlots.get(i2);
            if (slot.canBeHovered()) {
                this.drawSlot(slot);
            }
            if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.canBeHovered()) {
                this.theSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                final int j2 = slot.xDisplayPosition;
                final int k2 = slot.yDisplayPosition;
                GlStateManager.colorMask(true, true, true, false);
                this.drawGradientRect(j2, k2, j2 + 16, k2 + 16, -2130706433, -2130706433);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        RenderHelper.enableGUIStandardItemLighting();
        final InventoryPlayer inventoryplayer = this.mc.player.inventory;
        ItemStack itemstack = this.draggedStack.func_190926_b() ? inventoryplayer.getItemStack() : this.draggedStack;
        if (!itemstack.func_190926_b()) {
            final int j3 = 8;
            final int k3 = this.draggedStack.func_190926_b() ? 8 : 16;
            String s = null;
            if (!this.draggedStack.func_190926_b() && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.func_190920_e(MathHelper.ceil(itemstack.func_190916_E() / 2.0f));
            }
            else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.func_190920_e(this.dragSplittingRemnant);
                if (itemstack.func_190926_b()) {
                    s = TextFormatting.YELLOW + "0";
                }
            }
            this.drawItemStack(itemstack, mouseX - i - 8, mouseY - j - k3, s);
        }
        if (!this.returningStack.func_190926_b()) {
            float f = (Minecraft.getSystemTime() - this.returningStackTime) / 100.0f;
            if (f >= 1.0f) {
                f = 1.0f;
                this.returningStack = ItemStack.field_190927_a;
            }
            final int l2 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
            final int i3 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
            final int l3 = this.touchUpX + (int)(l2 * f);
            final int i4 = this.touchUpY + (int)(i3 * f);
            this.drawItemStack(this.returningStack, l3, i4, null);
        }
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
    }
    
    protected void func_191948_b(final int p_191948_1_, final int p_191948_2_) {
        if (this.mc.player.inventory.getItemStack().func_190926_b() && this.theSlot != null && this.theSlot.getHasStack()) {
            this.renderToolTip(this.theSlot.getStack(), p_191948_1_, p_191948_2_);
        }
    }
    
    private void drawItemStack(final ItemStack stack, final int x, final int y, final String altText) {
        GlStateManager.translate(0.0f, 0.0f, 32.0f);
        GuiContainer.zLevel = 200.0f;
        this.itemRender.zLevel = 200.0f;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, stack, x, y - (this.draggedStack.func_190926_b() ? 0 : 8), altText);
        GuiContainer.zLevel = 0.0f;
        this.itemRender.zLevel = 0.0f;
    }
    
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    }
    
    protected abstract void drawGuiContainerBackgroundLayer(final float p0, final int p1, final int p2);
    
    private void drawSlot(final Slot slotIn) {
        final int i = slotIn.xDisplayPosition;
        final int j = slotIn.yDisplayPosition;
        ItemStack itemstack = slotIn.getStack();
        boolean flag = false;
        boolean flag2 = slotIn == this.clickedSlot && !this.draggedStack.func_190926_b() && !this.isRightMouseClick;
        final ItemStack itemstack2 = this.mc.player.inventory.getItemStack();
        String s = null;
        if (slotIn == this.clickedSlot && !this.draggedStack.func_190926_b() && this.isRightMouseClick && !itemstack.func_190926_b()) {
            itemstack = itemstack.copy();
            itemstack.func_190920_e(itemstack.func_190916_E() / 2);
        }
        else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack2.func_190926_b()) {
            if (this.dragSplittingSlots.size() == 1) {
                return;
            }
            if (Container.canAddItemToSlot(slotIn, itemstack2, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
                itemstack = itemstack2.copy();
                flag = true;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack().func_190926_b() ? 0 : slotIn.getStack().func_190916_E());
                final int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));
                if (itemstack.func_190916_E() > k) {
                    s = String.valueOf(TextFormatting.YELLOW.toString()) + k;
                    itemstack.func_190920_e(k);
                }
            }
            else {
                this.dragSplittingSlots.remove(slotIn);
                this.updateDragSplitting();
            }
        }
        GuiContainer.zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        if (itemstack.func_190926_b() && slotIn.canBeHovered()) {
            final String s2 = slotIn.getSlotTexture();
            if (s2 != null) {
                final TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite(s2);
                GlStateManager.disableLighting();
                this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
                GlStateManager.enableLighting();
                flag2 = true;
            }
        }
        if (!flag2) {
            if (flag) {
                Gui.drawRect(i, j, i + 16, j + 16, -2130706433);
            }
            GlStateManager.enableDepth();
            this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
            this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, itemstack, i, j, s);
        }
        this.itemRender.zLevel = 0.0f;
        GuiContainer.zLevel = 0.0f;
    }
    
    private void updateDragSplitting() {
        final ItemStack itemstack = this.mc.player.inventory.getItemStack();
        if (!itemstack.func_190926_b() && this.dragSplitting) {
            if (this.dragSplittingLimit == 2) {
                this.dragSplittingRemnant = itemstack.getMaxStackSize();
            }
            else {
                this.dragSplittingRemnant = itemstack.func_190916_E();
                for (final Slot slot : this.dragSplittingSlots) {
                    final ItemStack itemstack2 = itemstack.copy();
                    final ItemStack itemstack3 = slot.getStack();
                    final int i = itemstack3.func_190926_b() ? 0 : itemstack3.func_190916_E();
                    Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack2, i);
                    final int j = Math.min(itemstack2.getMaxStackSize(), slot.getItemStackLimit(itemstack2));
                    if (itemstack2.func_190916_E() > j) {
                        itemstack2.func_190920_e(j);
                    }
                    this.dragSplittingRemnant -= itemstack2.func_190916_E() - i;
                }
            }
        }
    }
    
    private Slot getSlotAtPosition(final int x, final int y) {
        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i) {
            final Slot slot = this.inventorySlots.inventorySlots.get(i);
            if (this.isMouseOverSlot(slot, x, y) && slot.canBeHovered()) {
                return slot;
            }
        }
        return null;
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final boolean flag = mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
        final Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        final long i = Minecraft.getSystemTime();
        this.doubleClick = (this.lastClickSlot == slot && i - this.lastClickTime < 250L && this.lastClickButton == mouseButton);
        this.ignoreMouseUp = false;
        if (mouseButton == 0 || mouseButton == 1 || flag) {
            final int j = this.guiLeft;
            final int k = this.guiTop;
            final boolean flag2 = this.func_193983_c(mouseX, mouseY, j, k);
            int l = -1;
            if (slot != null) {
                l = slot.slotNumber;
            }
            if (flag2) {
                l = -999;
            }
            if (this.mc.gameSettings.touchscreen && flag2 && this.mc.player.inventory.getItemStack().func_190926_b()) {
                this.mc.displayGuiScreen(null);
                return;
            }
            if (l != -1) {
                if (this.mc.gameSettings.touchscreen) {
                    if (slot != null && slot.getHasStack()) {
                        this.clickedSlot = slot;
                        this.draggedStack = ItemStack.field_190927_a;
                        this.isRightMouseClick = (mouseButton == 1);
                    }
                    else {
                        this.clickedSlot = null;
                    }
                }
                else if (!this.dragSplitting) {
                    if (this.mc.player.inventory.getItemStack().func_190926_b()) {
                        if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                            this.handleMouseClick(slot, l, mouseButton, ClickType.CLONE);
                        }
                        else {
                            final boolean flag3 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                            ClickType clicktype = ClickType.PICKUP;
                            if (flag3) {
                                this.shiftClickedSlot = ((slot != null && slot.getHasStack()) ? slot.getStack().copy() : ItemStack.field_190927_a);
                                clicktype = ClickType.QUICK_MOVE;
                            }
                            else if (l == -999) {
                                clicktype = ClickType.THROW;
                            }
                            this.handleMouseClick(slot, l, mouseButton, clicktype);
                        }
                        this.ignoreMouseUp = true;
                    }
                    else {
                        this.dragSplitting = true;
                        this.dragSplittingButton = mouseButton;
                        this.dragSplittingSlots.clear();
                        if (mouseButton == 0) {
                            this.dragSplittingLimit = 0;
                        }
                        else if (mouseButton == 1) {
                            this.dragSplittingLimit = 1;
                        }
                        else if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                            this.dragSplittingLimit = 2;
                        }
                    }
                }
            }
        }
        this.lastClickSlot = slot;
        this.lastClickTime = i;
        this.lastClickButton = mouseButton;
    }
    
    protected boolean func_193983_c(final int p_193983_1_, final int p_193983_2_, final int p_193983_3_, final int p_193983_4_) {
        return p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        final Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        final ItemStack itemstack = this.mc.player.inventory.getItemStack();
        if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
            if (clickedMouseButton == 0 || clickedMouseButton == 1) {
                if (this.draggedStack.func_190926_b()) {
                    if (slot != this.clickedSlot && !this.clickedSlot.getStack().func_190926_b()) {
                        this.draggedStack = this.clickedSlot.getStack().copy();
                    }
                }
                else if (this.draggedStack.func_190916_E() > 1 && slot != null && Container.canAddItemToSlot(slot, this.draggedStack, false)) {
                    final long i = Minecraft.getSystemTime();
                    if (this.currentDragTargetSlot == slot) {
                        if (i - this.dragItemDropDelay > 500L) {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                            this.handleMouseClick(slot, slot.slotNumber, 1, ClickType.PICKUP);
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                            this.dragItemDropDelay = i + 750L;
                            this.draggedStack.func_190918_g(1);
                        }
                    }
                    else {
                        this.currentDragTargetSlot = slot;
                        this.dragItemDropDelay = i;
                    }
                }
            }
        }
        else if (this.dragSplitting && slot != null && !itemstack.func_190926_b() && (itemstack.func_190916_E() > this.dragSplittingSlots.size() || this.dragSplittingLimit == 2) && Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && this.inventorySlots.canDragIntoSlot(slot)) {
            this.dragSplittingSlots.add(slot);
            this.updateDragSplitting();
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        final Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        final int i = this.guiLeft;
        final int j = this.guiTop;
        final boolean flag = this.func_193983_c(mouseX, mouseY, i, j);
        int k = -1;
        if (slot != null) {
            k = slot.slotNumber;
        }
        if (flag) {
            k = -999;
        }
        if (this.doubleClick && slot != null && state == 0 && this.inventorySlots.canMergeSlot(ItemStack.field_190927_a, slot)) {
            if (isShiftKeyDown()) {
                if (!this.shiftClickedSlot.func_190926_b()) {
                    for (final Slot slot2 : this.inventorySlots.inventorySlots) {
                        if (slot2 != null && slot2.canTakeStack(this.mc.player) && slot2.getHasStack() && slot2.inventory == slot.inventory && Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true)) {
                            this.handleMouseClick(slot2, slot2.slotNumber, state, ClickType.QUICK_MOVE);
                        }
                    }
                }
            }
            else {
                this.handleMouseClick(slot, k, state, ClickType.PICKUP_ALL);
            }
            this.doubleClick = false;
            this.lastClickTime = 0L;
        }
        else {
            if (this.dragSplitting && this.dragSplittingButton != state) {
                this.dragSplitting = false;
                this.dragSplittingSlots.clear();
                this.ignoreMouseUp = true;
                return;
            }
            if (this.ignoreMouseUp) {
                this.ignoreMouseUp = false;
                return;
            }
            if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
                if (state == 0 || state == 1) {
                    if (this.draggedStack.func_190926_b() && slot != this.clickedSlot) {
                        this.draggedStack = this.clickedSlot.getStack();
                    }
                    final boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);
                    if (k != -1 && !this.draggedStack.func_190926_b() && flag2) {
                        this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
                        this.handleMouseClick(slot, k, 0, ClickType.PICKUP);
                        if (this.mc.player.inventory.getItemStack().func_190926_b()) {
                            this.returningStack = ItemStack.field_190927_a;
                        }
                        else {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
                            this.touchUpX = mouseX - i;
                            this.touchUpY = mouseY - j;
                            this.returningStackDestSlot = this.clickedSlot;
                            this.returningStack = this.draggedStack;
                            this.returningStackTime = Minecraft.getSystemTime();
                        }
                    }
                    else if (!this.draggedStack.func_190926_b()) {
                        this.touchUpX = mouseX - i;
                        this.touchUpY = mouseY - j;
                        this.returningStackDestSlot = this.clickedSlot;
                        this.returningStack = this.draggedStack;
                        this.returningStackTime = Minecraft.getSystemTime();
                    }
                    this.draggedStack = ItemStack.field_190927_a;
                    this.clickedSlot = null;
                }
            }
            else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
                this.handleMouseClick(null, -999, Container.getQuickcraftMask(0, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
                for (final Slot slot3 : this.dragSplittingSlots) {
                    this.handleMouseClick(slot3, slot3.slotNumber, Container.getQuickcraftMask(1, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
                }
                this.handleMouseClick(null, -999, Container.getQuickcraftMask(2, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
            }
            else if (!this.mc.player.inventory.getItemStack().func_190926_b()) {
                if (state == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                    this.handleMouseClick(slot, k, state, ClickType.CLONE);
                }
                else {
                    final boolean flag3 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                    if (flag3) {
                        this.shiftClickedSlot = ((slot != null && slot.getHasStack()) ? slot.getStack().copy() : ItemStack.field_190927_a);
                    }
                    this.handleMouseClick(slot, k, state, flag3 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
                }
            }
        }
        if (this.mc.player.inventory.getItemStack().func_190926_b()) {
            this.lastClickTime = 0L;
        }
        this.dragSplitting = false;
    }
    
    private boolean isMouseOverSlot(final Slot slotIn, final int mouseX, final int mouseY) {
        return this.isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
    }
    
    protected boolean isPointInRegion(final int rectX, final int rectY, final int rectWidth, final int rectHeight, int pointX, int pointY) {
        final int i = this.guiLeft;
        final int j = this.guiTop;
        pointX -= i;
        pointY -= j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }
    
    protected void handleMouseClick(final Slot slotIn, int slotId, final int mouseButton, final ClickType type) {
        if (slotIn != null) {
            slotId = slotIn.slotNumber;
        }
        this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, mouseButton, type, this.mc.player);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.player.closeScreen();
        }
        this.checkHotbarKeys(keyCode);
        if (this.theSlot != null && this.theSlot.getHasStack()) {
            if (keyCode == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
                this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, ClickType.CLONE);
            }
            else if (keyCode == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
                this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, GuiScreen.isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
            }
        }
    }
    
    protected boolean checkHotbarKeys(final int keyCode) {
        if (this.mc.player.inventory.getItemStack().func_190926_b() && this.theSlot != null) {
            for (int i = 0; i < 9; ++i) {
                if (keyCode == this.mc.gameSettings.keyBindsHotbar[i].getKeyCode()) {
                    this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, i, ClickType.SWAP);
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void onGuiClosed() {
        if (this.mc.player != null) {
            this.inventorySlots.onContainerClosed(this.mc.player);
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!this.mc.player.isEntityAlive() || this.mc.player.isDead) {
            this.mc.player.closeScreen();
        }
    }
}
