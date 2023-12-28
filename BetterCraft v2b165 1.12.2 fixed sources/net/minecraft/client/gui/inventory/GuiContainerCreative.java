// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.util.NonNullList;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.creative.InventoryScreen;
import me.amkgre.bettercraft.client.mods.creative.GuiItemCreator;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import java.util.Map;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.inventory.IInventory;
import com.google.common.collect.Lists;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.init.Items;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import java.util.Collection;
import java.util.Locale;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.item.Item;
import java.io.IOException;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.inventory.IContainerListener;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.ClickType;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Slot;
import java.util.List;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.InventoryEffectRenderer;

public class GuiContainerCreative extends InventoryEffectRenderer
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
        GuiContainerCreative.selectedTabIndex = CreativeTabs.BUILDING_BLOCKS.getTabIndex();
    }
    
    public GuiContainerCreative(final EntityPlayer player) {
        super(new ContainerCreative(player));
        player.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
    }
    
    @Override
    public void updateScreen() {
        if (!this.mc.playerController.isInCreativeMode()) {
            this.mc.displayGuiScreen(new GuiInventory(this.mc.player));
        }
    }
    
    @Override
    protected void handleMouseClick(@Nullable final Slot slotIn, final int slotId, final int mouseButton, ClickType type) {
        this.clearSearch = true;
        final boolean flag = type == ClickType.QUICK_MOVE;
        type = ((slotId == -999 && type == ClickType.PICKUP) ? ClickType.THROW : type);
        if (slotIn == null && GuiContainerCreative.selectedTabIndex != CreativeTabs.INVENTORY.getTabIndex() && type != ClickType.QUICK_CRAFT) {
            final InventoryPlayer inventoryplayer1 = this.mc.player.inventory;
            if (!inventoryplayer1.getItemStack().func_190926_b()) {
                if (mouseButton == 0) {
                    this.mc.player.dropItem(inventoryplayer1.getItemStack(), true);
                    this.mc.playerController.sendPacketDropItem(inventoryplayer1.getItemStack());
                    inventoryplayer1.setItemStack(ItemStack.field_190927_a);
                }
                if (mouseButton == 1) {
                    final ItemStack itemstack6 = inventoryplayer1.getItemStack().splitStack(1);
                    this.mc.player.dropItem(itemstack6, true);
                    this.mc.playerController.sendPacketDropItem(itemstack6);
                }
            }
        }
        else {
            if (slotIn != null && !slotIn.canTakeStack(this.mc.player)) {
                return;
            }
            if (slotIn == this.destroyItemSlot && flag) {
                for (int j = 0; j < this.mc.player.inventoryContainer.getInventory().size(); ++j) {
                    this.mc.playerController.sendSlotPacket(ItemStack.field_190927_a, j);
                }
            }
            else if (GuiContainerCreative.selectedTabIndex == CreativeTabs.INVENTORY.getTabIndex()) {
                if (slotIn == this.destroyItemSlot) {
                    this.mc.player.inventory.setItemStack(ItemStack.field_190927_a);
                }
                else if (type == ClickType.THROW && slotIn != null && slotIn.getHasStack()) {
                    final ItemStack itemstack7 = slotIn.decrStackSize((mouseButton == 0) ? 1 : slotIn.getStack().getMaxStackSize());
                    final ItemStack itemstack8 = slotIn.getStack();
                    this.mc.player.dropItem(itemstack7, true);
                    this.mc.playerController.sendPacketDropItem(itemstack7);
                    this.mc.playerController.sendSlotPacket(itemstack8, ((CreativeSlot)slotIn).slot.slotNumber);
                }
                else if (type == ClickType.THROW && !this.mc.player.inventory.getItemStack().func_190926_b()) {
                    this.mc.player.dropItem(this.mc.player.inventory.getItemStack(), true);
                    this.mc.playerController.sendPacketDropItem(this.mc.player.inventory.getItemStack());
                    this.mc.player.inventory.setItemStack(ItemStack.field_190927_a);
                }
                else {
                    this.mc.player.inventoryContainer.slotClick((slotIn == null) ? slotId : ((CreativeSlot)slotIn).slot.slotNumber, mouseButton, type, this.mc.player);
                    this.mc.player.inventoryContainer.detectAndSendChanges();
                }
            }
            else if (type != ClickType.QUICK_CRAFT && slotIn.inventory == GuiContainerCreative.basicInventory) {
                final InventoryPlayer inventoryplayer2 = this.mc.player.inventory;
                ItemStack itemstack9 = inventoryplayer2.getItemStack();
                final ItemStack itemstack10 = slotIn.getStack();
                if (type == ClickType.SWAP) {
                    if (!itemstack10.func_190926_b() && mouseButton >= 0 && mouseButton < 9) {
                        final ItemStack itemstack11 = itemstack10.copy();
                        itemstack11.func_190920_e(itemstack11.getMaxStackSize());
                        this.mc.player.inventory.setInventorySlotContents(mouseButton, itemstack11);
                        this.mc.player.inventoryContainer.detectAndSendChanges();
                    }
                    return;
                }
                if (type == ClickType.CLONE) {
                    if (inventoryplayer2.getItemStack().func_190926_b() && slotIn.getHasStack()) {
                        final ItemStack itemstack12 = slotIn.getStack().copy();
                        itemstack12.func_190920_e(itemstack12.getMaxStackSize());
                        inventoryplayer2.setItemStack(itemstack12);
                    }
                    return;
                }
                if (type == ClickType.THROW) {
                    if (!itemstack10.func_190926_b()) {
                        final ItemStack itemstack13 = itemstack10.copy();
                        itemstack13.func_190920_e((mouseButton == 0) ? 1 : itemstack13.getMaxStackSize());
                        this.mc.player.dropItem(itemstack13, true);
                        this.mc.playerController.sendPacketDropItem(itemstack13);
                    }
                    return;
                }
                if (!itemstack9.func_190926_b() && !itemstack10.func_190926_b() && itemstack9.isItemEqual(itemstack10) && ItemStack.areItemStackTagsEqual(itemstack9, itemstack10)) {
                    if (mouseButton == 0) {
                        if (flag) {
                            itemstack9.func_190920_e(itemstack9.getMaxStackSize());
                        }
                        else if (itemstack9.func_190916_E() < itemstack9.getMaxStackSize()) {
                            itemstack9.func_190917_f(1);
                        }
                    }
                    else {
                        itemstack9.func_190918_g(1);
                    }
                }
                else if (!itemstack10.func_190926_b() && itemstack9.func_190926_b()) {
                    inventoryplayer2.setItemStack(itemstack10.copy());
                    itemstack9 = inventoryplayer2.getItemStack();
                    if (flag) {
                        itemstack9.func_190920_e(itemstack9.getMaxStackSize());
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
                final ItemStack itemstack14 = (slotIn == null) ? ItemStack.field_190927_a : this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                this.inventorySlots.slotClick((slotIn == null) ? slotId : slotIn.slotNumber, mouseButton, type, this.mc.player);
                if (Container.getDragEvent(mouseButton) == 2) {
                    for (int k = 0; k < 9; ++k) {
                        this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + k).getStack(), 36 + k);
                    }
                }
                else if (slotIn != null) {
                    final ItemStack itemstack15 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                    this.mc.playerController.sendSlotPacket(itemstack15, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    final int i = 45 + mouseButton;
                    if (type == ClickType.SWAP) {
                        this.mc.playerController.sendSlotPacket(itemstack14, i - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    }
                    else if (type == ClickType.THROW && !itemstack14.func_190926_b()) {
                        final ItemStack itemstack16 = itemstack14.copy();
                        itemstack16.func_190920_e((mouseButton == 0) ? 1 : itemstack16.getMaxStackSize());
                        this.mc.player.dropItem(itemstack16, true);
                        this.mc.playerController.sendPacketDropItem(itemstack16);
                    }
                    this.mc.player.inventoryContainer.detectAndSendChanges();
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
        if (this.mc.playerController.isInCreativeMode()) {
            super.initGui();
            this.buttonList.clear();
            this.buttonList.add(new GuiButton(2, this.guiLeft - 2, this.guiTop + 165, "Item Creator"));
            this.buttonList.add(new GuiButton(45, GuiContainerCreative.width / 2 - 95, GuiContainerCreative.height / 2 - 117, 20, 20, "<"));
            this.buttonList.add(new GuiButton(45, GuiContainerCreative.width / 2 + 73, GuiContainerCreative.height / 2 - 117, 20, 20, ">"));
            Keyboard.enableRepeatEvents(true);
            (this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 80, this.fontRendererObj.FONT_HEIGHT)).setMaxStringLength(50);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(false);
            this.searchField.setTextColor(16777215);
            final int i = GuiContainerCreative.selectedTabIndex;
            GuiContainerCreative.selectedTabIndex = -1;
            this.setCurrentCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY[i]);
            this.listener = new CreativeCrafting(this.mc);
            this.mc.player.inventoryContainer.addListener(this.listener);
        }
        else {
            this.mc.displayGuiScreen(new GuiInventory(this.mc.player));
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.mc.player != null && this.mc.player.inventory != null) {
            this.mc.player.inventoryContainer.removeListener(this.listener);
        }
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (GuiContainerCreative.selectedTabIndex != CreativeTabs.SEARCH.getTabIndex()) {
            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
                this.setCurrentCreativeTab(CreativeTabs.SEARCH);
            }
            else {
                super.keyTyped(typedChar, keyCode);
            }
        }
        else {
            if (this.clearSearch) {
                this.clearSearch = false;
                this.searchField.setText("");
            }
            if (!this.checkHotbarKeys(keyCode)) {
                if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
                    this.updateCreativeSearch();
                }
                else {
                    super.keyTyped(typedChar, keyCode);
                }
            }
        }
    }
    
    private void updateCreativeSearch() {
        final ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
        guicontainercreative$containercreative.itemList.clear();
        if (this.searchField.getText().isEmpty()) {
            for (final Item item : Item.REGISTRY) {
                item.getSubItems(CreativeTabs.SEARCH, guicontainercreative$containercreative.itemList);
            }
        }
        else {
            guicontainercreative$containercreative.itemList.addAll((Collection<?>)this.mc.func_193987_a(SearchTreeManager.field_194011_a).func_194038_a(this.searchField.getText().toLowerCase(Locale.ROOT)));
        }
        guicontainercreative$containercreative.scrollTo(this.currentScroll = 0.0f);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[GuiContainerCreative.selectedTabIndex];
        if (creativetabs.drawInForegroundOfTab()) {
            GlStateManager.disableBlend();
            this.fontRendererObj.drawString(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]), 8, 6, 4210752);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            final int i = mouseX - this.guiLeft;
            final int j = mouseY - this.guiTop;
            CreativeTabs[] creative_TAB_ARRAY;
            for (int length = (creative_TAB_ARRAY = CreativeTabs.CREATIVE_TAB_ARRAY).length, k = 0; k < length; ++k) {
                final CreativeTabs creativetabs = creative_TAB_ARRAY[k];
                if (this.isMouseOverTab(creativetabs, i, j)) {
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
            CreativeTabs[] creative_TAB_ARRAY;
            for (int length = (creative_TAB_ARRAY = CreativeTabs.CREATIVE_TAB_ARRAY).length, k = 0; k < length; ++k) {
                final CreativeTabs creativetabs = creative_TAB_ARRAY[k];
                if (this.isMouseOverTab(creativetabs, i, j)) {
                    this.setCurrentCreativeTab(creativetabs);
                    return;
                }
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    private boolean needsScrollBars() {
        return GuiContainerCreative.selectedTabIndex != CreativeTabs.INVENTORY.getTabIndex() && CreativeTabs.CREATIVE_TAB_ARRAY[GuiContainerCreative.selectedTabIndex].shouldHidePlayerInventory() && ((ContainerCreative)this.inventorySlots).canScroll();
    }
    
    private void setCurrentCreativeTab(final CreativeTabs tab) {
        final int i = GuiContainerCreative.selectedTabIndex;
        GuiContainerCreative.selectedTabIndex = tab.getTabIndex();
        final ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
        this.dragSplittingSlots.clear();
        guicontainercreative$containercreative.itemList.clear();
        if (tab == CreativeTabs.field_192395_m) {
            for (int j = 0; j < 9; ++j) {
                final HotbarSnapshot hotbarsnapshot = this.mc.field_191950_u.func_192563_a(j);
                if (hotbarsnapshot.isEmpty()) {
                    for (int k = 0; k < 9; ++k) {
                        if (k == j) {
                            final ItemStack itemstack = new ItemStack(Items.PAPER);
                            itemstack.func_190925_c("CustomCreativeLock");
                            final String s = GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindsHotbar[j].getKeyCode());
                            final String s2 = GameSettings.getKeyDisplayString(this.mc.gameSettings.field_193629_ap.getKeyCode());
                            itemstack.setStackDisplayName(new TextComponentTranslation("inventory.hotbarInfo", new Object[] { s2, s }).getUnformattedText());
                            guicontainercreative$containercreative.itemList.add(itemstack);
                        }
                        else {
                            guicontainercreative$containercreative.itemList.add(ItemStack.field_190927_a);
                        }
                    }
                }
                else {
                    guicontainercreative$containercreative.itemList.addAll((Collection<?>)hotbarsnapshot);
                }
            }
        }
        else if (tab != CreativeTabs.SEARCH) {
            tab.displayAllRelevantItems(guicontainercreative$containercreative.itemList);
        }
        if (tab == CreativeTabs.INVENTORY) {
            final Container container = this.mc.player.inventoryContainer;
            if (this.originalSlots == null) {
                this.originalSlots = guicontainercreative$containercreative.inventorySlots;
            }
            guicontainercreative$containercreative.inventorySlots = (List<Slot>)Lists.newArrayList();
            for (int l = 0; l < container.inventorySlots.size(); ++l) {
                final Slot slot = new CreativeSlot(container.inventorySlots.get(l), l);
                guicontainercreative$containercreative.inventorySlots.add(slot);
                if (l >= 5 && l < 9) {
                    final int j2 = l - 5;
                    final int l2 = j2 / 2;
                    final int j3 = j2 % 2;
                    slot.xDisplayPosition = 54 + l2 * 54;
                    slot.yDisplayPosition = 6 + j3 * 27;
                }
                else if (l >= 0 && l < 5) {
                    slot.xDisplayPosition = -2000;
                    slot.yDisplayPosition = -2000;
                }
                else if (l == 45) {
                    slot.xDisplayPosition = 35;
                    slot.yDisplayPosition = 20;
                }
                else if (l < container.inventorySlots.size()) {
                    final int i2 = l - 9;
                    final int k2 = i2 % 9;
                    final int i3 = i2 / 9;
                    slot.xDisplayPosition = 9 + k2 * 18;
                    if (l >= 36) {
                        slot.yDisplayPosition = 112;
                    }
                    else {
                        slot.yDisplayPosition = 54 + i3 * 18;
                    }
                }
            }
            this.destroyItemSlot = new Slot(GuiContainerCreative.basicInventory, 0, 173, 112);
            guicontainercreative$containercreative.inventorySlots.add(this.destroyItemSlot);
        }
        else if (i == CreativeTabs.INVENTORY.getTabIndex()) {
            guicontainercreative$containercreative.inventorySlots = this.originalSlots;
            this.originalSlots = null;
        }
        if (this.searchField != null) {
            if (tab == CreativeTabs.SEARCH) {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocused(true);
                this.searchField.setText("");
                this.updateCreativeSearch();
            }
            else {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused(false);
            }
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
        Gui.drawString(this.fontRendererObj, "1 / 2", GuiContainerCreative.width / 2 - 15, GuiContainerCreative.height / 2 - 110, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
        CreativeTabs[] creative_TAB_ARRAY;
        for (int length = (creative_TAB_ARRAY = CreativeTabs.CREATIVE_TAB_ARRAY).length, n = 0; n < length; ++n) {
            final CreativeTabs creativetabs = creative_TAB_ARRAY[n];
            if (this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY)) {
                break;
            }
        }
        if (this.destroyItemSlot != null && GuiContainerCreative.selectedTabIndex == CreativeTabs.INVENTORY.getTabIndex() && this.isPointInRegion(this.destroyItemSlot.xDisplayPosition, this.destroyItemSlot.yDisplayPosition, 16, 16, mouseX, mouseY)) {
            this.drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        this.func_191948_b(mouseX, mouseY);
    }
    
    @Override
    protected void renderToolTip(final ItemStack stack, final int x, final int y) {
        if (GuiContainerCreative.selectedTabIndex == CreativeTabs.SEARCH.getTabIndex()) {
            final List<String> list = stack.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
            CreativeTabs creativetabs = stack.getItem().getCreativeTab();
            if (creativetabs == null && stack.getItem() == Items.ENCHANTED_BOOK) {
                final Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
                if (map.size() == 1) {
                    final Enchantment enchantment = map.keySet().iterator().next();
                    CreativeTabs[] creative_TAB_ARRAY;
                    for (int length = (creative_TAB_ARRAY = CreativeTabs.CREATIVE_TAB_ARRAY).length, j = 0; j < length; ++j) {
                        final CreativeTabs creativetabs2 = creative_TAB_ARRAY[j];
                        if (creativetabs2.hasRelevantEnchantmentType(enchantment.type)) {
                            creativetabs = creativetabs2;
                            break;
                        }
                    }
                }
            }
            if (creativetabs != null) {
                list.add(1, new StringBuilder().append(TextFormatting.BOLD).append(TextFormatting.BLUE).append(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0])).toString());
            }
            for (int i = 0; i < list.size(); ++i) {
                if (i == 0) {
                    list.set(i, stack.getRarity().rarityColor + list.get(i));
                }
                else {
                    list.set(i, TextFormatting.GRAY + list.get(i));
                }
            }
            this.drawHoveringText(list, x, y);
        }
        else {
            super.renderToolTip(stack, x, y);
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.enableGUIStandardItemLighting();
        final CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[GuiContainerCreative.selectedTabIndex];
        CreativeTabs[] creative_TAB_ARRAY;
        for (int length = (creative_TAB_ARRAY = CreativeTabs.CREATIVE_TAB_ARRAY).length, l = 0; l < length; ++l) {
            final CreativeTabs creativetabs2 = creative_TAB_ARRAY[l];
            this.mc.getTextureManager().bindTexture(GuiContainerCreative.CREATIVE_INVENTORY_TABS);
            if (creativetabs2.getTabIndex() != GuiContainerCreative.selectedTabIndex) {
                this.drawTab(creativetabs2);
            }
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int i = this.guiLeft + 175;
        final int j = this.guiTop + 18;
        final int k = j + 112;
        this.mc.getTextureManager().bindTexture(GuiContainerCreative.CREATIVE_INVENTORY_TABS);
        if (creativetabs.shouldHidePlayerInventory()) {
            this.drawTexturedModalRect(i, j + (int)((k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }
        this.drawTab(creativetabs);
        if (creativetabs == CreativeTabs.INVENTORY) {
            GuiInventory.drawEntityOnScreen(this.guiLeft + 88, this.guiTop + 45, 20, (float)(this.guiLeft + 88 - mouseX), (float)(this.guiTop + 45 - 30 - mouseY), this.mc.player);
        }
    }
    
    protected boolean isMouseOverTab(final CreativeTabs tab, final int mouseX, final int mouseY) {
        final int i = tab.getTabColumn();
        int j = 28 * i;
        int k = 0;
        if (tab.func_192394_m()) {
            j = this.xSize - 28 * (6 - i) + 2;
        }
        else if (i > 0) {
            j += i;
        }
        if (tab.isTabInFirstRow()) {
            k -= 32;
        }
        else {
            k += this.ySize;
        }
        return mouseX >= j && mouseX <= j + 28 && mouseY >= k && mouseY <= k + 32;
    }
    
    protected boolean renderCreativeInventoryHoveringText(final CreativeTabs tab, final int mouseX, final int mouseY) {
        final int i = tab.getTabColumn();
        int j = 28 * i;
        int k = 0;
        if (tab.func_192394_m()) {
            j = this.xSize - 28 * (6 - i) + 2;
        }
        else if (i > 0) {
            j += i;
        }
        if (tab.isTabInFirstRow()) {
            k -= 32;
        }
        else {
            k += this.ySize;
        }
        if (this.isPointInRegion(j + 3, k + 3, 23, 27, mouseX, mouseY)) {
            this.drawCreativeTabHoveringText(I18n.format(tab.getTranslatedTabLabel(), new Object[0]), mouseX, mouseY);
            return true;
        }
        return false;
    }
    
    protected void drawTab(final CreativeTabs tab) {
        final boolean flag = tab.getTabIndex() == GuiContainerCreative.selectedTabIndex;
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
        GuiContainerCreative.zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        l += 6;
        i2 = i2 + 8 + (flag2 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        final ItemStack itemstack = tab.getIconItemStack();
        this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i2);
        this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, l, i2);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0f;
        GuiContainerCreative.zLevel = 0.0f;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiItemCreator(this));
        }
        if (button.id == 45) {
            this.mc.displayGuiScreen(new InventoryScreen(this.mc.player));
        }
    }
    
    public int getSelectedTabIndex() {
        return GuiContainerCreative.selectedTabIndex;
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
            final String s = GameSettings.getKeyDisplayString(p_192044_0_.gameSettings.keyBindsHotbar[p_192044_1_].getKeyCode());
            final String s2 = GameSettings.getKeyDisplayString(p_192044_0_.gameSettings.field_193630_aq.getKeyCode());
            p_192044_0_.ingameGUI.setRecordPlaying(new TextComponentTranslation("inventory.hotbarSaved", new Object[] { s2, s }), false);
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
                    this.addSlotToContainer(new LockedSlot(GuiContainerCreative.basicInventory, i * 9 + j, 9 + j * 18, 18 + i * 18));
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
                        GuiContainerCreative.basicInventory.setInventorySlotContents(l + k * 9, this.itemList.get(i2));
                    }
                    else {
                        GuiContainerCreative.basicInventory.setInventorySlotContents(l + k * 9, ItemStack.field_190927_a);
                    }
                }
            }
        }
        
        public boolean canScroll() {
            return this.itemList.size() > 45;
        }
        
        @Override
        public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
            if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size()) {
                final Slot slot = this.inventorySlots.get(index);
                if (slot != null && slot.getHasStack()) {
                    slot.putStack(ItemStack.field_190927_a);
                }
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
