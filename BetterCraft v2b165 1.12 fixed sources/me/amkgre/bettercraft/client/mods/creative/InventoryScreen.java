// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.NonNullList;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.RenderHelper;
import java.util.ArrayList;
import me.amkgre.bettercraft.client.Client;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import java.util.Collection;
import java.util.Locale;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.item.Item;
import java.io.IOException;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.ClickType;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.inventory.Slot;
import java.util.List;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.InventoryEffectRenderer;

public class InventoryScreen extends InventoryEffectRenderer
{
    private static final ResourceLocation CREATIVE_INVENTORY_TABS;
    private static final InventoryBasic basicInventory;
    private static int selectedTabIndex;
    private float currentScroll;
    private boolean isScrolling;
    private boolean wasClicking;
    private GuiTextField searchField;
    private List<Slot> originalSlots;
    private Slot destroyItemSlot;
    private boolean clearSearch;
    private CreativeCrafting listener;
    
    static {
        CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
        basicInventory = new InventoryBasic("tmp", true, 45);
        InventoryScreen.selectedTabIndex = InventorySection.SONSTIGEITEMS.getTabIndex();
    }
    
    public InventoryScreen(final EntityPlayer player) {
        super(new ContainerCreative(player));
        player.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
    }
    
    @Override
    public void updateScreen() {
        if (!Minecraft.getMinecraft().playerController.isInCreativeMode()) {
            this.mc.displayGuiScreen(new GuiInventory(Minecraft.getMinecraft().player));
        }
    }
    
    @Override
    protected void handleMouseClick(@Nullable final Slot slotIn, final int slotId, final int mouseButton, ClickType type) {
        this.clearSearch = true;
        final boolean flag = type == ClickType.QUICK_MOVE;
        final ClickType clickType;
        type = (clickType = ((slotId == -999 && type == ClickType.PICKUP) ? ClickType.THROW : type));
        if (slotIn == null && type != ClickType.QUICK_CRAFT) {
            final InventoryPlayer inventoryplayer1 = Minecraft.getMinecraft().player.inventory;
            if (!inventoryplayer1.getItemStack().func_190926_b()) {
                if (mouseButton == 0) {
                    Minecraft.getMinecraft().player.dropItem(inventoryplayer1.getItemStack(), true);
                    Minecraft.getMinecraft().playerController.sendPacketDropItem(inventoryplayer1.getItemStack());
                    inventoryplayer1.setItemStack(ItemStack.field_190927_a);
                }
                if (mouseButton == 1) {
                    final ItemStack itemstack6 = inventoryplayer1.getItemStack().splitStack(1);
                    Minecraft.getMinecraft().player.dropItem(itemstack6, true);
                    Minecraft.getMinecraft().playerController.sendPacketDropItem(itemstack6);
                }
            }
        }
        else {
            if (slotIn != null && !slotIn.canTakeStack(Minecraft.getMinecraft().player)) {
                return;
            }
            if (slotIn == this.destroyItemSlot && flag) {
                for (int j = 0; j < Minecraft.getMinecraft().player.inventoryContainer.getInventory().size(); ++j) {
                    Minecraft.getMinecraft().playerController.sendSlotPacket(ItemStack.field_190927_a, j);
                }
            }
            else if (type != ClickType.QUICK_CRAFT && slotIn.inventory == InventoryScreen.basicInventory) {
                final InventoryPlayer inventoryplayer2 = Minecraft.getMinecraft().player.inventory;
                ItemStack itemstack7 = inventoryplayer2.getItemStack();
                final ItemStack itemstack8 = slotIn.getStack();
                if (type == ClickType.SWAP) {
                    if (!itemstack8.func_190926_b() && mouseButton >= 0 && mouseButton < 9) {
                        final ItemStack itemstack9 = itemstack8.copy();
                        itemstack9.func_190920_e(itemstack9.getMaxStackSize());
                        Minecraft.getMinecraft().player.inventory.setInventorySlotContents(mouseButton, itemstack9);
                        Minecraft.getMinecraft().player.inventoryContainer.detectAndSendChanges();
                    }
                    return;
                }
                if (type == ClickType.CLONE) {
                    if (inventoryplayer2.getItemStack().func_190926_b() && slotIn.getHasStack()) {
                        final ItemStack itemstack10 = slotIn.getStack().copy();
                        itemstack10.func_190920_e(itemstack10.getMaxStackSize());
                        inventoryplayer2.setItemStack(itemstack10);
                    }
                    return;
                }
                if (type == ClickType.THROW) {
                    if (!itemstack8.func_190926_b()) {
                        final ItemStack itemstack11 = itemstack8.copy();
                        itemstack11.func_190920_e((mouseButton == 0) ? 1 : itemstack11.getMaxStackSize());
                        Minecraft.getMinecraft().player.dropItem(itemstack11, true);
                        Minecraft.getMinecraft().playerController.sendPacketDropItem(itemstack11);
                    }
                    return;
                }
                if (!itemstack7.func_190926_b() && !itemstack8.func_190926_b() && itemstack7.isItemEqual(itemstack8) && ItemStack.areItemStackTagsEqual(itemstack7, itemstack8)) {
                    if (mouseButton == 0) {
                        if (flag) {
                            itemstack7.func_190920_e(itemstack7.getMaxStackSize());
                        }
                        else if (itemstack7.func_190916_E() < itemstack7.getMaxStackSize()) {
                            itemstack7.func_190917_f(1);
                        }
                    }
                    else {
                        itemstack7.func_190918_g(1);
                    }
                }
                else if (!itemstack8.func_190926_b() && itemstack7.func_190926_b()) {
                    inventoryplayer2.setItemStack(itemstack8.copy());
                    itemstack7 = inventoryplayer2.getItemStack();
                    if (flag) {
                        itemstack7.func_190920_e(itemstack7.getMaxStackSize());
                    }
                }
                else if (mouseButton == 0) {
                    inventoryplayer2.setItemStack(ItemStack.field_190927_a);
                }
                else {
                    inventoryplayer2.getItemStack().func_190918_g(1);
                }
            }
            else if (this.inventorySlots != null) {
                final ItemStack itemstack12 = (slotIn == null) ? ItemStack.field_190927_a : this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                this.inventorySlots.slotClick((slotIn == null) ? slotId : slotIn.slotNumber, mouseButton, type, Minecraft.getMinecraft().player);
                if (Container.getDragEvent(mouseButton) == 2) {
                    for (int k = 0; k < 9; ++k) {
                        Minecraft.getMinecraft().playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + k).getStack(), 36 + k);
                    }
                }
                else if (slotIn != null) {
                    final ItemStack itemstack13 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                    Minecraft.getMinecraft().playerController.sendSlotPacket(itemstack13, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    final int i = 45 + mouseButton;
                    if (type == ClickType.SWAP) {
                        Minecraft.getMinecraft().playerController.sendSlotPacket(itemstack12, i - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    }
                    else if (type == ClickType.THROW && !itemstack12.func_190926_b()) {
                        final ItemStack itemstack14 = itemstack12.copy();
                        itemstack14.func_190920_e((mouseButton == 0) ? 1 : itemstack14.getMaxStackSize());
                        Minecraft.getMinecraft().player.dropItem(itemstack14, true);
                        Minecraft.getMinecraft().playerController.sendPacketDropItem(itemstack14);
                    }
                    Minecraft.getMinecraft().player.inventoryContainer.detectAndSendChanges();
                }
            }
        }
    }
    
    @Override
    protected void updateActivePotionEffects() {
        final int i = this.guiLeft;
        super.updateActivePotionEffects();
        if (this.searchField != null && this.guiLeft != i) {
            this.searchField.xPosition = this.guiLeft + 82;
        }
    }
    
    @Override
    public void initGui() {
        if (Minecraft.getMinecraft().playerController.isInCreativeMode()) {
            super.initGui();
            this.buttonList.clear();
            Keyboard.enableRepeatEvents(true);
            (this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 80, this.fontRendererObj.FONT_HEIGHT)).setMaxStringLength(50);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(false);
            this.searchField.setTextColor(16777215);
            this.buttonList.add(new GuiButton(45, InventoryScreen.width / 2 - 95, InventoryScreen.height / 2 - 117, 20, 20, "<"));
            this.buttonList.add(new GuiButton(45, InventoryScreen.width / 2 + 73, InventoryScreen.height / 2 - 117, 20, 20, ">"));
            final int i = InventoryScreen.selectedTabIndex;
            InventoryScreen.selectedTabIndex = -1;
            this.setCurrentCreativeTab(InventorySection.CREATIVE_TAB_ARRAY[i]);
            this.listener = new CreativeCrafting(this.mc);
            Minecraft.getMinecraft().player.inventoryContainer.addListener(this.listener);
        }
        else {
            this.mc.displayGuiScreen(new GuiInventory(Minecraft.getMinecraft().player));
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.inventory != null) {
            Minecraft.getMinecraft().player.inventoryContainer.removeListener(this.listener);
        }
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (!GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindChat)) {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    private void updateCreativeSearch() {
        final ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
        guicontainercreative$containercreative.itemList.clear();
        if (this.searchField.getText().isEmpty()) {
            for (Item item : Item.REGISTRY) {}
        }
        else {
            guicontainercreative$containercreative.itemList.addAll((Collection<?>)this.mc.func_193987_a(SearchTreeManager.field_194011_a).func_194038_a(this.searchField.getText().toLowerCase(Locale.ROOT)));
        }
        guicontainercreative$containercreative.scrollTo(this.currentScroll = 0.0f);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final InventorySection t = InventorySection.CREATIVE_TAB_ARRAY[InventoryScreen.selectedTabIndex];
        if (t.drawInForegroundOfTab()) {
            GlStateManager.disableBlend();
            this.fontRendererObj.drawString(t.getTabLabel(), 8, 6, 4210752);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            final int i = mouseX - this.guiLeft;
            final int j = mouseY - this.guiTop;
            InventorySection[] creative_TAB_ARRAY;
            for (int length = (creative_TAB_ARRAY = InventorySection.CREATIVE_TAB_ARRAY).length, k = 0; k < length; ++k) {
                final InventorySection InventoryTab = creative_TAB_ARRAY[k];
                if (this.isMouseOverTab(InventoryTab, i, j)) {
                    return;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (state == 0) {
            final int i = mouseX - this.guiLeft;
            final int j = mouseY - this.guiTop;
            InventorySection[] creative_TAB_ARRAY;
            for (int length = (creative_TAB_ARRAY = InventorySection.CREATIVE_TAB_ARRAY).length, k = 0; k < length; ++k) {
                final InventorySection InventoryTab = creative_TAB_ARRAY[k];
                if (this.isMouseOverTab(InventoryTab, i, j)) {
                    this.setCurrentCreativeTab(InventoryTab);
                    return;
                }
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    private boolean needsScrollBars() {
        return InventorySection.CREATIVE_TAB_ARRAY[InventoryScreen.selectedTabIndex].shouldHidePlayerInventory() && ((ContainerCreative)this.inventorySlots).canScroll();
    }
    
    private void setCurrentCreativeTab(final InventorySection tab) {
        final int i = InventoryScreen.selectedTabIndex;
        InventoryScreen.selectedTabIndex = tab.getTabIndex();
        final ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
        this.dragSplittingSlots.clear();
        guicontainercreative$containercreative.itemList.clear();
        tab.displayAllRelevantItems(guicontainercreative$containercreative.itemList);
        if (this.searchField != null) {
            this.searchField.setVisible(false);
            this.searchField.setCanLoseFocus(true);
            this.searchField.setFocused(false);
        }
        guicontainercreative$containercreative.scrollTo(this.currentScroll = 0.0f);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0 && this.needsScrollBars()) {
            final int j = (((ContainerCreative)this.inventorySlots).itemList.size() + 9 - 1) / 9 - 5;
            if (i > 0) {
                i = 1;
            }
            if (i < 0) {
                i = -1;
            }
            this.currentScroll -= (float)(i / (double)j);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        final boolean flag = Mouse.isButtonDown(0);
        final int i = this.guiLeft;
        final int j = this.guiTop;
        final int k = i + 175;
        final int l = j + 18;
        final int i2 = k + 14;
        final int j2 = l + 112;
        if (!this.wasClicking && flag && mouseX >= k && mouseY >= l && mouseX < i2 && mouseY < j2) {
            this.isScrolling = this.needsScrollBars();
        }
        if (!flag) {
            this.isScrolling = false;
        }
        this.wasClicking = flag;
        if (this.isScrolling) {
            this.currentScroll = (mouseY - l - 7.5f) / (j2 - l - 15.0f);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
        Gui.drawString(this.fontRendererObj, "2 / 2", InventoryScreen.width / 2 - 15, InventoryScreen.height / 2 - 110, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
        InventorySection[] creative_TAB_ARRAY;
        for (int length = (creative_TAB_ARRAY = InventorySection.CREATIVE_TAB_ARRAY).length, n = 0; n < length; ++n) {
            final InventorySection InventoryTab = creative_TAB_ARRAY[n];
            if (this.renderCreativeInventoryHoveringText(InventoryTab, mouseX, mouseY)) {
                break;
            }
        }
        if (this.destroyItemSlot != null && this.isPointInRegion(this.destroyItemSlot.xDisplayPosition, this.destroyItemSlot.yDisplayPosition, 16, 16, mouseX, mouseY)) {
            this.drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        this.func_191948_b(mouseX, mouseY);
    }
    
    @Override
    protected void renderToolTip(final ItemStack stack, final int x, final int y) {
        for (final InventoryItem a : Client.getInstance().getInventoryManager().items) {
            final ArrayList<String> lines2 = new ArrayList<String>();
            final String[] lineSplit = a.getToolTip().split("\n");
            for (int i = 0; i < lineSplit.length; ++i) {
                lines2.add(lineSplit[i]);
            }
            if (a.getItem() != stack) {
                continue;
            }
            this.drawHoveringText(lines2, x, y);
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.enableGUIStandardItemLighting();
        final InventorySection t = InventorySection.CREATIVE_TAB_ARRAY[InventoryScreen.selectedTabIndex];
        InventorySection[] creative_TAB_ARRAY;
        for (int length = (creative_TAB_ARRAY = InventorySection.CREATIVE_TAB_ARRAY).length, l = 0; l < length; ++l) {
            final InventorySection InventoryTab1 = creative_TAB_ARRAY[l];
            this.mc.getTextureManager().bindTexture(InventoryScreen.CREATIVE_INVENTORY_TABS);
            if (InventoryTab1.getTabIndex() != InventoryScreen.selectedTabIndex) {
                this.drawTab(InventoryTab1);
            }
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + t.getBackgroundImageName()));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int i = this.guiLeft + 175;
        final int j = this.guiTop + 18;
        final int k = j + 112;
        this.mc.getTextureManager().bindTexture(InventoryScreen.CREATIVE_INVENTORY_TABS);
        if (t.shouldHidePlayerInventory()) {
            this.drawTexturedModalRect(i, j + (int)((k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }
        this.drawTab(t);
    }
    
    protected boolean isMouseOverTab(final InventorySection tab, final int mouseX, final int mouseY) {
        final int i = tab.getTabColumn();
        int j = 28 * i;
        int k = 0;
        if (tab.func_192394_m()) {
            j = this.xSize - 28 * (6 - i) + 2;
        }
        else if (i > 0) {
            j += i;
        }
        int n;
        if (tab.isTabInFirstRow()) {
            k -= 32;
            n = k;
        }
        else {
            n = (k += this.ySize);
        }
        k = n;
        return mouseX >= j && mouseX <= j + 28 && mouseY >= k && mouseY <= k + 32;
    }
    
    protected boolean renderCreativeInventoryHoveringText(final InventorySection tab, final int mouseX, final int mouseY) {
        final int i = tab.getTabColumn();
        int j = 28 * i;
        int k = 0;
        if (tab.func_192394_m()) {
            j = this.xSize - 28 * (6 - i) + 2;
        }
        else if (i > 0) {
            j += i;
        }
        int n;
        if (tab.isTabInFirstRow()) {
            k -= 32;
            n = k;
        }
        else {
            n = (k += this.ySize);
        }
        k = n;
        if (this.isPointInRegion(j + 3, k + 3, 23, 27, mouseX, mouseY)) {
            this.drawCreativeTabHoveringText(tab.getTabLabel(), mouseX, mouseY);
            return true;
        }
        return false;
    }
    
    protected void drawTab(final InventorySection tab) {
        final boolean flag = tab.getTabIndex() == InventoryScreen.selectedTabIndex;
        final boolean flag2 = tab.isTabInFirstRow();
        final int i = tab.getTabColumn();
        final int j = i * 28;
        int k = 0;
        int l = this.guiLeft + 28 * i;
        int i2 = this.guiTop;
        final int j2 = 32;
        if (flag) {
            k += 32;
        }
        if (tab.func_192394_m()) {
            l = this.guiLeft + this.xSize - 28 * (6 - i);
        }
        else if (i > 0) {
            l += i;
        }
        if (flag2) {
            i2 -= 28;
        }
        else {
            k += 64;
            i2 += this.ySize - 4;
        }
        GlStateManager.disableLighting();
        this.drawTexturedModalRect(l, i2, j, k, 28, 32);
        InventoryScreen.zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        i2 = i2 + 8 + (flag2 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        final ItemStack itemstack = tab.getIconItemStack();
        final RenderItem itemRender = this.itemRender;
        final ItemStack stack = itemstack;
        l += 6;
        itemRender.renderItemAndEffectIntoGUI(stack, l, i2);
        this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, l, i2);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0f;
        InventoryScreen.zLevel = 0.0f;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiStats(this, Minecraft.getMinecraft().player.getStatFileWriter()));
        }
        else if (button.id == 45) {
            this.mc.displayGuiScreen(new GuiContainerCreative(Minecraft.getMinecraft().player));
        }
    }
    
    public int getSelectedTabIndex() {
        return InventoryScreen.selectedTabIndex;
    }
    
    public static void func_192044_a(final Minecraft p_192044_0_, final int p_192044_1_, final boolean p_192044_2_, final boolean p_192044_3_) {
        final EntityPlayerSP entityplayersp = p_192044_0_.player;
        final CreativeSettings creativesettings = p_192044_0_.field_191950_u;
        final HotbarSnapshot hotbarsnapshot = creativesettings.func_192563_a(p_192044_1_);
        if (p_192044_2_) {
            for (int i = 0; i < InventoryPlayer.getHotbarSize(); ++i) {
                final ItemStack itemstack = hotbarsnapshot.get(i).copy();
                entityplayersp.inventory.setInventorySlotContents(i, itemstack);
                p_192044_0_.playerController.sendSlotPacket(itemstack, 36 + i);
            }
            entityplayersp.inventoryContainer.detectAndSendChanges();
        }
        else if (p_192044_3_) {
            for (int j = 0; j < InventoryPlayer.getHotbarSize(); ++j) {
                hotbarsnapshot.set(j, entityplayersp.inventory.getStackInSlot(j).copy());
            }
            final String s2 = GameSettings.getKeyDisplayString(p_192044_0_.gameSettings.keyBindsHotbar[p_192044_1_].getKeyCode());
            final String s3 = GameSettings.getKeyDisplayString(p_192044_0_.gameSettings.field_193630_aq.getKeyCode());
            p_192044_0_.ingameGUI.setRecordPlaying(new TextComponentTranslation("inventory.hotbarSaved", new Object[] { s3, s2 }), false);
            creativesettings.func_192564_b();
        }
    }
    
    public static class ContainerCreative extends Container
    {
        public NonNullList<ItemStack> itemList;
        
        public ContainerCreative(final EntityPlayer player) {
            this.itemList = NonNullList.func_191196_a();
            final InventoryPlayer inventoryplayer = player.inventory;
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlotToContainer(new LockedSlot(InventoryScreen.basicInventory, i * 9 + j, 9 + j * 18, 18 + i * 18));
                }
            }
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(inventoryplayer, k, 9 + k * 18, 112));
            }
            this.scrollTo(0.0f);
        }
        
        @Override
        public boolean canInteractWith(final EntityPlayer playerIn) {
            return true;
        }
        
        public void scrollTo(final float p_148329_1_) {
            final int i = (this.itemList.size() + 9 - 1) / 9 - 5;
            int j = (int)(p_148329_1_ * i + 0.5);
            if (j < 0) {
                j = 0;
            }
            for (int k = 0; k < 5; ++k) {
                for (int l = 0; l < 9; ++l) {
                    final int i2 = l + (k + j) * 9;
                    if (i2 >= 0 && i2 < this.itemList.size()) {
                        InventoryScreen.basicInventory.setInventorySlotContents(l + k * 9, this.itemList.get(i2));
                    }
                    else {
                        InventoryScreen.basicInventory.setInventorySlotContents(l + k * 9, ItemStack.field_190927_a);
                    }
                }
            }
        }
        
        public boolean canScroll() {
            return this.itemList.size() > 45;
        }
        
        @Override
        public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index2) {
            final Slot slot;
            if (index2 >= this.inventorySlots.size() - 9 && index2 < this.inventorySlots.size() && (slot = this.inventorySlots.get(index2)) != null && slot.getHasStack()) {
                slot.putStack(ItemStack.field_190927_a);
            }
            return ItemStack.field_190927_a;
        }
        
        @Override
        public boolean canMergeSlot(final ItemStack stack, final Slot slotIn) {
            return slotIn.yDisplayPosition > 90;
        }
        
        @Override
        public boolean canDragIntoSlot(final Slot slotIn) {
            return slotIn.inventory instanceof InventoryPlayer || (slotIn.yDisplayPosition > 90 && slotIn.xDisplayPosition <= 162);
        }
    }
    
    class CreativeSlot extends Slot
    {
        private final Slot slot;
        
        public CreativeSlot(final Slot p_i46313_2_, final int p_i46313_3_) {
            super(p_i46313_2_.inventory, p_i46313_3_, 0, 0);
            this.slot = p_i46313_2_;
        }
        
        @Override
        public ItemStack func_190901_a(final EntityPlayer p_190901_1_, final ItemStack p_190901_2_) {
            this.slot.func_190901_a(p_190901_1_, p_190901_2_);
            return p_190901_2_;
        }
        
        @Override
        public boolean isItemValid(final ItemStack stack) {
            return this.slot.isItemValid(stack);
        }
        
        @Override
        public ItemStack getStack() {
            return this.slot.getStack();
        }
        
        @Override
        public boolean getHasStack() {
            return this.slot.getHasStack();
        }
        
        @Override
        public void putStack(final ItemStack stack) {
            this.slot.putStack(stack);
        }
        
        @Override
        public void onSlotChanged() {
            this.slot.onSlotChanged();
        }
        
        @Override
        public int getSlotStackLimit() {
            return this.slot.getSlotStackLimit();
        }
        
        @Override
        public int getItemStackLimit(final ItemStack stack) {
            return this.slot.getItemStackLimit(stack);
        }
        
        @Nullable
        @Override
        public String getSlotTexture() {
            return this.slot.getSlotTexture();
        }
        
        @Override
        public ItemStack decrStackSize(final int amount) {
            return this.slot.decrStackSize(amount);
        }
        
        @Override
        public boolean isHere(final IInventory inv, final int slotIn) {
            return this.slot.isHere(inv, slotIn);
        }
        
        @Override
        public boolean canBeHovered() {
            return this.slot.canBeHovered();
        }
        
        @Override
        public boolean canTakeStack(final EntityPlayer playerIn) {
            return this.slot.canTakeStack(playerIn);
        }
    }
    
    static class LockedSlot extends Slot
    {
        public LockedSlot(final IInventory p_i47453_1_, final int p_i47453_2_, final int p_i47453_3_, final int p_i47453_4_) {
            super(p_i47453_1_, p_i47453_2_, p_i47453_3_, p_i47453_4_);
        }
        
        @Override
        public boolean canTakeStack(final EntityPlayer playerIn) {
            if (super.canTakeStack(playerIn) && this.getHasStack()) {
                return this.getStack().getSubCompound("CustomCreativeLock") == null;
            }
            return !this.getHasStack();
        }
    }
}
