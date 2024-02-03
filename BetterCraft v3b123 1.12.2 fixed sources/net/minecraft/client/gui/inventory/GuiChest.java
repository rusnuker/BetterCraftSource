// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Objects;
import java.util.Arrays;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.client.Minecraft;
import java.util.concurrent.Executors;
import net.minecraft.inventory.IInventory;
import java.util.concurrent.ScheduledExecutorService;
import net.minecraft.util.ResourceLocation;

public class GuiChest extends GuiContainer
{
    private static final ResourceLocation CHEST_GUI_TEXTURE;
    private static final ScheduledExecutorService CHEST_STEALER_SERVICE;
    private int speed;
    private IInventory upperChestInventory;
    private IInventory lowerChestInventory;
    private int inventoryRows;
    
    static {
        CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
        CHEST_STEALER_SERVICE = Executors.newSingleThreadScheduledExecutor();
    }
    
    public GuiChest(final IInventory upperInv, final IInventory lowerInv) {
        super(new ContainerChest(upperInv, lowerInv, Minecraft.getMinecraft().thePlayer));
        this.upperChestInventory = upperInv;
        this.lowerChestInventory = lowerInv;
        this.allowUserInput = false;
        final int i = 222;
        final int j = i - 108;
        this.inventoryRows = lowerInv.getSizeInventory() / 9;
        this.ySize = j + this.inventoryRows * 18;
        this.speed = 10;
    }
    
    @Override
    public void initGui() {
        final int x = (GuiChest.width - this.xSize) / 2;
        final int y = (GuiChest.height - this.ySize) / 2;
        final int x2 = (GuiChest.width - this.xSize) / 2 + this.xSize;
        final int y2 = (GuiChest.height - this.ySize) / 2 + this.ySize;
        this.buttonList.add(new GuiButton(0, x2 - 52 - 25, y + 5, 25, 10, "Steal"));
        this.buttonList.add(new GuiButton(1, x2 - 25 - 25, y + 5, 25, 10, "Store"));
        this.buttonList.add(new GuiButton(2, x2 - 20, y + 5, 10, 10, String.valueOf(this.speed)));
        super.initGui();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0 || mouseButton == 1) {
            final GuiButton button = this.buttonList.get(2);
            if (button.mousePressed(this.mc, mouseX, mouseY)) {
                button.playPressSound(this.mc.getSoundHandler());
                if (button.id == 2) {
                    if (mouseButton == 0) {
                        if (this.speed < 20) {
                            ++this.speed;
                        }
                    }
                    else if (mouseButton == 1 && this.speed > 0) {
                        --this.speed;
                    }
                    button.setDisplayString(String.valueOf(this.speed));
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final GuiButton button = this.buttonList.get(2);
        if (button.isMouseOver()) {
            this.drawHoveringText(Arrays.asList("Configurate pickup speed because anticheat", "", "The higher the number, the slower the speed"), mouseX, mouseY);
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0 || button.id == 1) {
            final IInventory inventory;
            final BiConsumer<IInventory, Integer> function = (inventory, index) -> {
                final Slot slot = this.inventorySlots.getSlotFromInventory(inventory, index);
                if (Objects.isNull(slot) || !slot.getHasStack()) {
                    return;
                }
                else {
                    GuiChest.CHEST_STEALER_SERVICE.schedule(() -> this.mc.playerController.windowClick(this.inventorySlots.windowId, slot2.slotNumber, 0, 1, this.mc.thePlayer), this.inventorySlots.inventorySlots.stream().filter(slotObj -> Objects.nonNull(slotObj) && slotObj.getHasStack()).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()).indexOf(slot) * (this.speed * 10), TimeUnit.MILLISECONDS);
                    return;
                }
            };
            inventory = ((button.id == 0) ? this.lowerChestInventory : ((button.id == 1) ? this.upperChestInventory : null));
            IntStream.range(0, inventory.getSizeInventory()).forEach(index -> biConsumer.accept(inventory2, index));
        }
        super.actionPerformed(button);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRendererObj.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiChest.CHEST_GUI_TEXTURE);
        final int i = (GuiChest.width - this.xSize) / 2;
        final int j = (GuiChest.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
