// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import java.util.Iterator;
import wdl.api.IWDLMessageType;
import java.util.Collection;
import wdl.MessageTypeCategory;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiYesNo;
import wdl.WDL;
import wdl.WDLMessages;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLMessages extends GuiScreen
{
    private String hoveredButtonDescription;
    private GuiScreen parent;
    private GuiMessageTypeList list;
    private GuiButton enableAllButton;
    private GuiButton resetButton;
    
    public GuiWDLMessages(final GuiScreen parent) {
        this.hoveredButtonDescription = null;
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        this.enableAllButton = new GuiButton(100, GuiWDLMessages.width / 2 - 155, 18, 150, 20, this.getAllEnabledText());
        this.buttonList.add(this.enableAllButton);
        this.resetButton = new GuiButton(101, GuiWDLMessages.width / 2 + 5, 18, 150, 20, I18n.format("wdl.gui.messages.reset", new Object[0]));
        this.buttonList.add(this.resetButton);
        this.list = new GuiMessageTypeList();
        this.buttonList.add(new GuiButton(102, GuiWDLMessages.width / 2 - 100, GuiWDLMessages.height - 29, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (!button.enabled) {
            return;
        }
        if (button.id == 100) {
            WDLMessages.enableAllMessages ^= true;
            WDL.baseProps.setProperty("Messages.enableAll", Boolean.toString(WDLMessages.enableAllMessages));
            button.displayString = this.getAllEnabledText();
        }
        else if (button.id == 101) {
            this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("wdl.gui.messages.reset.confirm.title", new Object[0]), I18n.format("wdl.gui.messages.reset.confirm.subtitle", new Object[0]), 101));
        }
        else if (button.id == 102) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (result && id == 101) {
            WDLMessages.resetEnabledToDefaults();
        }
        this.mc.displayGuiScreen(this);
    }
    
    @Override
    public void onGuiClosed() {
        WDL.saveProps();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (this.list.mouseClicked(mouseX, mouseY, mouseButton)) {
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.list.mouseReleased(mouseX, mouseY, state)) {
            return;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.hoveredButtonDescription = null;
        this.drawDefaultBackground();
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawCenteredString(this.fontRendererObj, I18n.format("wdl.gui.messages.message.title", new Object[0]), GuiWDLMessages.width / 2, 8, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.hoveredButtonDescription != null) {
            Utils.drawGuiInfoBox(this.hoveredButtonDescription, GuiWDLMessages.width, GuiWDLMessages.height, 48);
        }
        else if (this.enableAllButton.isMouseOver()) {
            Utils.drawGuiInfoBox(I18n.format("wdl.gui.messages.all.description", new Object[0]), GuiWDLMessages.width, GuiWDLMessages.height, 48);
        }
        else if (this.resetButton.isMouseOver()) {
            Utils.drawGuiInfoBox(I18n.format("wdl.gui.messages.reset.description", new Object[0]), GuiWDLMessages.width, GuiWDLMessages.height, 48);
        }
    }
    
    private String getAllEnabledText() {
        return I18n.format("wdl.gui.messages.all." + WDLMessages.enableAllMessages, new Object[0]);
    }
    
    static /* synthetic */ void access$1(final GuiWDLMessages guiWDLMessages, final String hoveredButtonDescription) {
        guiWDLMessages.hoveredButtonDescription = hoveredButtonDescription;
    }
    
    private class GuiMessageTypeList extends GuiListExtended
    {
        private List<IGuiListEntry> entries;
        
        public GuiMessageTypeList() {
            super(GuiWDLMessages.this.mc, GuiWDLMessages.width, GuiWDLMessages.height, 39, GuiWDLMessages.height - 32, 20);
            this.entries = new ArrayList<IGuiListEntry>() {
                {
                    final Map<MessageTypeCategory, Collection<IWDLMessageType>> map = WDLMessages.getTypes().asMap();
                    for (final Map.Entry<MessageTypeCategory, Collection<IWDLMessageType>> e : map.entrySet()) {
                        ((ArrayList<CategoryEntry>)this).add(new CategoryEntry(e.getKey()));
                        for (final IWDLMessageType type : e.getValue()) {
                            ((ArrayList<MessageTypeEntry>)this).add(new MessageTypeEntry(type, e.getKey()));
                        }
                    }
                }
            };
        }
        
        @Override
        public IGuiListEntry getListEntry(final int index) {
            return this.entries.get(index);
        }
        
        @Override
        protected int getSize() {
            return this.entries.size();
        }
        
        private class CategoryEntry implements IGuiListEntry
        {
            private final GuiButton button;
            private final MessageTypeCategory category;
            
            public CategoryEntry(final MessageTypeCategory category) {
                this.category = category;
                this.button = new GuiButton(0, 0, 0, 80, 20, "");
            }
            
            @Override
            public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {
            }
            
            @Override
            public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
                Gui.drawCenteredString(GuiWDLMessages.this.fontRendererObj, this.category.getDisplayName(), GuiWDLMessages.width / 2 - 40, y + slotHeight - GuiMessageTypeList.this.mc.fontRendererObj.FONT_HEIGHT - 1, 16777215);
                this.button.xPosition = GuiWDLMessages.width / 2 + 20;
                this.button.yPosition = y;
                this.button.displayString = I18n.format("wdl.gui.messages.group." + WDLMessages.isGroupEnabled(this.category), new Object[0]);
                this.button.enabled = WDLMessages.enableAllMessages;
                this.button.drawButton(GuiMessageTypeList.this.mc, mouseX, mouseY);
            }
            
            @Override
            public boolean mousePressed(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
                if (this.button.mousePressed(GuiMessageTypeList.this.mc, x, y)) {
                    WDLMessages.toggleGroupEnabled(this.category);
                    this.button.playPressSound(GuiMessageTypeList.this.mc.getSoundHandler());
                    return true;
                }
                return false;
            }
            
            @Override
            public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
            }
        }
        
        private class MessageTypeEntry implements IGuiListEntry
        {
            private final GuiButton button;
            private final IWDLMessageType type;
            private final MessageTypeCategory category;
            
            public MessageTypeEntry(final IWDLMessageType type, final MessageTypeCategory category) {
                this.type = type;
                this.button = new GuiButton(0, 0, 0, type.toString());
                this.category = category;
            }
            
            @Override
            public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {
            }
            
            @Override
            public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
                this.button.xPosition = GuiWDLMessages.width / 2 - 100;
                this.button.yPosition = y;
                this.button.displayString = I18n.format("wdl.gui.messages.message." + WDLMessages.isEnabled(this.type), this.type.getDisplayName());
                this.button.enabled = (WDLMessages.enableAllMessages && WDLMessages.isGroupEnabled(this.category));
                this.button.drawButton(GuiMessageTypeList.this.mc, mouseX, mouseY);
                if (this.button.isMouseOver()) {
                    GuiWDLMessages.access$1(GuiWDLMessages.this, this.type.getDescription());
                }
            }
            
            @Override
            public boolean mousePressed(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
                if (this.button.mousePressed(GuiMessageTypeList.this.mc, x, y)) {
                    WDLMessages.toggleEnabled(this.type);
                    this.button.playPressSound(GuiMessageTypeList.this.mc.getSoundHandler());
                    return true;
                }
                return false;
            }
            
            @Override
            public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
            }
        }
    }
}
