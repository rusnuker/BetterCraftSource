// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import wdl.api.IWDLModWithGui;
import net.minecraft.util.text.TextFormatting;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import wdl.api.WDLApi;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLExtensions extends GuiScreen
{
    private int bottomLocation;
    private static final int TOP_HEIGHT = 23;
    private static final int MIDDLE_HEIGHT = 19;
    private static final int BOTTOM_HEIGHT = 32;
    private int selectedModIndex;
    private final GuiScreen parent;
    private ModList list;
    private ModDetailList detailsList;
    private boolean dragging;
    private int dragOffset;
    
    private void updateDetailsList(final WDLApi.ModInfo<?> selectedMod) {
        this.detailsList.clearLines();
        if (selectedMod != null) {
            final String info = selectedMod.getInfo();
            this.detailsList.addLine(info);
        }
    }
    
    public GuiWDLExtensions(final GuiScreen parent) {
        this.selectedModIndex = -1;
        this.dragging = false;
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        this.bottomLocation = GuiWDLExtensions.height - 100;
        this.dragging = false;
        this.list = new ModList();
        this.detailsList = new ModDetailList();
        this.buttonList.add(new GuiButton(0, GuiWDLExtensions.width / 2 - 100, GuiWDLExtensions.height - 29, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
        this.detailsList.handleMouseInput();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseY > this.bottomLocation && mouseY < this.bottomLocation + 19) {
            this.dragging = true;
            this.dragOffset = mouseY - this.bottomLocation;
            return;
        }
        if (this.list.mouseClicked(mouseX, mouseY, mouseButton)) {
            return;
        }
        if (this.detailsList.mouseClicked(mouseX, mouseY, mouseButton)) {
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.dragging = false;
        if (this.list.mouseReleased(mouseX, mouseY, state)) {
            return;
        }
        if (this.detailsList.mouseReleased(mouseX, mouseY, state)) {
            return;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        if (this.dragging) {
            this.bottomLocation = mouseY - this.dragOffset;
        }
        if (this.bottomLocation < 31) {
            this.bottomLocation = 31;
        }
        if (this.bottomLocation > GuiWDLExtensions.height - 32 - 8) {
            this.bottomLocation = GuiWDLExtensions.height - 32 - 8;
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        if (this.bottomLocation < 56) {
            this.bottomLocation = 56;
        }
        if (this.bottomLocation > GuiWDLExtensions.height - 19 - 32 - 33) {
            this.bottomLocation = GuiWDLExtensions.height - 19 - 32 - 33;
        }
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        this.detailsList.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawCenteredString(this.fontRendererObj, I18n.format("wdl.gui.extensions.title", new Object[0]), GuiWDLExtensions.width / 2, 8, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    static /* synthetic */ void access$1(final GuiWDLExtensions guiWDLExtensions, final int selectedModIndex) {
        guiWDLExtensions.selectedModIndex = selectedModIndex;
    }
    
    private class ModList extends GuiListExtended
    {
        private List<IGuiListEntry> entries;
        
        public ModList() {
            super(GuiWDLExtensions.this.mc, GuiWDLExtensions.width, GuiWDLExtensions.this.bottomLocation, 23, GuiWDLExtensions.this.bottomLocation, 22);
            this.entries = new ArrayList<IGuiListEntry>() {
                {
                    for (final WDLApi.ModInfo<?> mod : WDLApi.getWDLMods().values()) {
                        ((ArrayList<ModEntry>)this).add(new ModEntry(mod));
                    }
                }
            };
            this.showSelectionBox = true;
        }
        
        @Override
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
            final int access$5 = GuiWDLExtensions.this.bottomLocation;
            this.bottom = access$5;
            this.height = access$5;
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
        
        @Override
        public IGuiListEntry getListEntry(final int index) {
            return this.entries.get(index);
        }
        
        @Override
        protected int getSize() {
            return this.entries.size();
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return slotIndex == GuiWDLExtensions.this.selectedModIndex;
        }
        
        @Override
        public int getListWidth() {
            return GuiWDLExtensions.width - 20;
        }
        
        @Override
        protected int getScrollBarX() {
            return GuiWDLExtensions.width - 10;
        }
        
        @Override
        public void handleMouseInput() {
            if (this.mouseY < GuiWDLExtensions.this.bottomLocation) {
                super.handleMouseInput();
            }
        }
        
        private class ModEntry implements IGuiListEntry
        {
            public final WDLApi.ModInfo<?> mod;
            private final String modDescription;
            private String label;
            private GuiButton button;
            private GuiButton disableButton;
            
            public ModEntry(final WDLApi.ModInfo<?> mod) {
                this.mod = mod;
                final String name = mod.getDisplayName();
                this.modDescription = I18n.format("wdl.gui.extensions.modVersion", name, mod.version);
                if (!mod.isEnabled()) {
                    this.label = new StringBuilder().append(TextFormatting.GRAY).append(TextFormatting.ITALIC).append(this.modDescription).toString();
                }
                else {
                    this.label = this.modDescription;
                }
                if (mod.mod instanceof IWDLModWithGui) {
                    final IWDLModWithGui guiMod = (IWDLModWithGui)mod.mod;
                    String buttonName = guiMod.getButtonName();
                    if (buttonName == null || buttonName.isEmpty()) {
                        buttonName = I18n.format("wdl.gui.extensions.defaultSettingsButtonText", new Object[0]);
                    }
                    this.button = new GuiButton(0, 0, 0, 80, 20, guiMod.getButtonName());
                }
                this.disableButton = new GuiButton(0, 0, 0, 80, 20, I18n.format("wdl.gui.extensions." + (mod.isEnabled() ? "enabled" : "disabled"), new Object[0]));
            }
            
            @Override
            public boolean mousePressed(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
                if (this.button != null && this.button.mousePressed(ModList.this.mc, x, y)) {
                    if (this.mod.mod instanceof IWDLModWithGui) {
                        ((IWDLModWithGui)this.mod.mod).openGui(GuiWDLExtensions.this);
                    }
                    this.button.playPressSound(ModList.this.mc.getSoundHandler());
                    return true;
                }
                if (this.disableButton.mousePressed(ModList.this.mc, x, y)) {
                    this.mod.toggleEnabled();
                    this.disableButton.playPressSound(ModList.this.mc.getSoundHandler());
                    this.disableButton.displayString = I18n.format("wdl.gui.extensions." + (this.mod.isEnabled() ? "enabled" : "disabled"), new Object[0]);
                    if (!this.mod.isEnabled()) {
                        this.label = new StringBuilder().append(TextFormatting.GRAY).append(TextFormatting.ITALIC).append(this.modDescription).toString();
                    }
                    else {
                        this.label = this.modDescription;
                    }
                    return true;
                }
                if (GuiWDLExtensions.this.selectedModIndex != slotIndex) {
                    GuiWDLExtensions.access$1(GuiWDLExtensions.this, slotIndex);
                    ModList.this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                    GuiWDLExtensions.this.updateDetailsList(this.mod);
                    return true;
                }
                return false;
            }
            
            @Override
            public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
                if (this.button != null) {
                    this.button.mouseReleased(x, y);
                }
            }
            
            @Override
            public void func_192633_a(final int p_192633_1_, final int p_192633_2_, final int p_192633_3_, final float p_192633_4_) {
            }
            
            @Override
            public void func_192634_a(final int p_192634_1_, final int p_192634_2_, final int p_192634_3_, final int p_192634_4_, final int p_192634_5_, final int p_192634_6_, final int p_192634_7_, final boolean p_192634_8_, final float p_192634_9_) {
                if (this.button != null) {
                    this.button.xPosition = GuiWDLExtensions.width - 180;
                    this.button.yPosition = p_192634_3_ - 1;
                    this.button.drawButton(ModList.this.mc, ModList.this.mouseX, ModList.this.mouseY, p_192634_9_);
                }
                this.disableButton.xPosition = GuiWDLExtensions.width - 92;
                this.disableButton.yPosition = p_192634_3_ - 1;
                this.disableButton.drawButton(ModList.this.mc, ModList.this.mouseX, ModList.this.mouseY, p_192634_9_);
                final int centerY = p_192634_3_ + ModList.this.slotHeight / 2 - GuiWDLExtensions.this.fontRendererObj.FONT_HEIGHT / 2;
                GuiWDLExtensions.this.fontRendererObj.drawString(this.label, p_192634_2_, centerY, 16777215);
            }
        }
    }
    
    private class ModDetailList extends TextList
    {
        public ModDetailList() {
            super(GuiWDLExtensions.this.mc, GuiWDLExtensions.width, GuiWDLExtensions.height - GuiWDLExtensions.this.bottomLocation, 19, 32);
        }
        
        @Override
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
            GlStateManager.translate(0.0f, (float)GuiWDLExtensions.this.bottomLocation, 0.0f);
            this.height = GuiWDLExtensions.height - GuiWDLExtensions.this.bottomLocation;
            this.bottom = this.height - 32;
            super.drawScreen(mouseX, mouseY, partialTicks);
            Gui.drawCenteredString(GuiWDLExtensions.this.fontRendererObj, I18n.format("wdl.gui.extensions.detailsCaption", new Object[0]), GuiWDLExtensions.width / 2, 5, 16777215);
            GlStateManager.translate(0.0f, (float)(-GuiWDLExtensions.this.bottomLocation), 0.0f);
        }
        
        @Override
        protected void overlayBackground(final int y1, final int y2, final int alpha1, final int alpha2) {
            if (y1 == 0) {
                super.overlayBackground(y1, y2, alpha1, alpha2);
                return;
            }
            GlStateManager.translate(0.0f, (float)(-GuiWDLExtensions.this.bottomLocation), 0.0f);
            super.overlayBackground(y1 + GuiWDLExtensions.this.bottomLocation, y2 + GuiWDLExtensions.this.bottomLocation, alpha1, alpha2);
            GlStateManager.translate(0.0f, (float)GuiWDLExtensions.this.bottomLocation, 0.0f);
        }
        
        @Override
        public void handleMouseInput() {
            this.mouseY -= GuiWDLExtensions.this.bottomLocation;
            if (this.mouseY > 0) {
                super.handleMouseInput();
            }
            this.mouseY += GuiWDLExtensions.this.bottomLocation;
        }
    }
}
