// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import wdl.WDLPluginChannels;
import wdl.update.WDLUpdateChecker;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import wdl.WDL;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDL extends GuiScreen
{
    private String displayedTooltip;
    private String title;
    private GuiScreen parent;
    private GuiTextField worldname;
    private GuiWDLButtonList list;
    
    public GuiWDL(final GuiScreen parent) {
        this.displayedTooltip = null;
        this.title = "";
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        if (WDL.isMultiworld && WDL.worldName.isEmpty()) {
            this.mc.displayGuiScreen(new GuiWDLMultiworldSelect(I18n.format("wdl.gui.multiworldSelect.title.changeOptions", new Object[0]), new GuiWDLMultiworldSelect.WorldSelectionCallback() {
                @Override
                public void onWorldSelected(final String selectedWorld) {
                    WDL.worldName = selectedWorld;
                    WDL.isMultiworld = true;
                    WDL.propsFound = true;
                    WDL.worldProps = WDL.loadWorldProps(selectedWorld);
                    GuiWDL.this.mc.displayGuiScreen(GuiWDL.this);
                }
                
                @Override
                public void onCancel() {
                    GuiWDL.this.mc.displayGuiScreen(null);
                }
            }));
            return;
        }
        if (!WDL.propsFound) {
            this.mc.displayGuiScreen(new GuiWDLMultiworld(new GuiWDLMultiworld.MultiworldCallback() {
                @Override
                public void onSelect(final boolean enableMutliworld) {
                    WDL.isMultiworld = enableMutliworld;
                    if (WDL.isMultiworld) {
                        GuiWDL.this.mc.displayGuiScreen(new GuiWDLMultiworldSelect(I18n.format("wdl.gui.multiworldSelect.title.changeOptions", new Object[0]), new GuiWDLMultiworldSelect.WorldSelectionCallback() {
                            @Override
                            public void onWorldSelected(final String selectedWorld) {
                                WDL.worldName = selectedWorld;
                                WDL.isMultiworld = true;
                                WDL.propsFound = true;
                                WDL.worldProps = WDL.loadWorldProps(selectedWorld);
                                GuiWDL.this.mc.displayGuiScreen(GuiWDL.this);
                            }
                            
                            @Override
                            public void onCancel() {
                                GuiWDL.this.mc.displayGuiScreen(null);
                            }
                        }));
                    }
                    else {
                        WDL.baseProps.setProperty("LinkedWorlds", "");
                        WDL.saveProps();
                        WDL.propsFound = true;
                        GuiWDL.this.mc.displayGuiScreen(GuiWDL.this);
                    }
                }
                
                @Override
                public void onCancel() {
                    GuiWDL.this.mc.displayGuiScreen(null);
                }
            }));
            return;
        }
        this.buttonList.clear();
        this.title = I18n.format("wdl.gui.wdl.title", WDL.baseFolderName.replace('@', ':'));
        if (WDL.baseProps.getProperty("ServerName").isEmpty()) {
            WDL.baseProps.setProperty("ServerName", WDL.getServerName());
        }
        (this.worldname = new GuiTextField(42, this.fontRendererObj, GuiWDL.width / 2 - 155, 19, 150, 18)).setText(WDL.baseProps.getProperty("ServerName"));
        this.buttonList.add(new GuiButton(100, GuiWDL.width / 2 - 100, GuiWDL.height - 29, I18n.format("gui.done", new Object[0])));
        this.list = new GuiWDLButtonList();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        if (!button.enabled) {
            return;
        }
        if (button.id == 100) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    public void onGuiClosed() {
        if (this.worldname != null) {
            WDL.baseProps.setProperty("ServerName", this.worldname.getText());
            WDL.saveProps();
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.list.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.worldname.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.list.mouseReleased(mouseX, mouseY, state)) {
            return;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.worldname.textboxKeyTyped(typedChar, keyCode);
    }
    
    @Override
    public void updateScreen() {
        this.worldname.updateCursorCounter();
        super.updateScreen();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.displayedTooltip = null;
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawCenteredString(this.fontRendererObj, this.title, GuiWDL.width / 2, 8, 16777215);
        final String name = I18n.format("wdl.gui.wdl.worldname", new Object[0]);
        Gui.drawString(this.fontRendererObj, name, this.worldname.xPosition - this.fontRendererObj.getStringWidth(String.valueOf(name) + " "), 26, 16777215);
        this.worldname.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
        Utils.drawGuiInfoBox(this.displayedTooltip, GuiWDL.width, GuiWDL.height, 48);
    }
    
    static /* synthetic */ void access$0(final GuiWDL guiWDL, final String displayedTooltip) {
        guiWDL.displayedTooltip = displayedTooltip;
    }
    
    private class GuiWDLButtonList extends GuiListExtended
    {
        private List<IGuiListEntry> entries;
        final /* synthetic */ GuiWDL this$0;
        
        public GuiWDLButtonList() {
            super(GuiWDL.this.mc, GuiWDL.width, GuiWDL.height, 39, GuiWDL.height - 32, 20);
            this.entries = new ArrayList<IGuiListEntry>() {
                {
                    ((ArrayList<ButtonEntry>)this).add(new ButtonEntry("worldOverrides", new GuiWDLWorld(GuiWDLButtonList.this.this$0), true));
                    ((ArrayList<ButtonEntry>)this).add(new ButtonEntry("generatorOverrides", new GuiWDLGenerator(GuiWDLButtonList.this.this$0), true));
                    ((ArrayList<ButtonEntry>)this).add(new ButtonEntry("playerOverrides", new GuiWDLPlayer(GuiWDLButtonList.this.this$0), true));
                    ((ArrayList<ButtonEntry>)this).add(new ButtonEntry("backupOptions", new GuiWDLBackup(GuiWDLButtonList.this.this$0), true));
                    ((ArrayList<ButtonEntry>)this).add(new ButtonEntry("messageOptions", new GuiWDLMessages(GuiWDLButtonList.this.this$0), false));
                    ((ArrayList<ButtonEntry>)this).add(new ButtonEntry("permissionsInfo", new GuiWDLPermissions(GuiWDLButtonList.this.this$0), false));
                    ((ArrayList<ButtonEntry>)this).add(new ButtonEntry("about", new GuiWDLAbout(GuiWDLButtonList.this.this$0), false));
                    if (WDLUpdateChecker.hasNewVersion()) {
                        ((ArrayList<ButtonEntry>)this).add(0, new ButtonEntry("updates.hasNew", new GuiWDLUpdates(GuiWDLButtonList.this.this$0), false));
                    }
                    else {
                        ((ArrayList<ButtonEntry>)this).add(new ButtonEntry("updates", new GuiWDLUpdates(GuiWDLButtonList.this.this$0), false));
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
        
        private class ButtonEntry implements IGuiListEntry
        {
            private final GuiButton button;
            private final GuiScreen toOpen;
            private final String tooltip;
            
            public ButtonEntry(final String key, final GuiScreen toOpen, final boolean needsPerms) {
                this.button = new GuiButton(0, 0, 0, I18n.format("wdl.gui.wdl." + key + ".name", new Object[0]));
                this.toOpen = toOpen;
                if (needsPerms) {
                    this.button.enabled = WDLPluginChannels.canDownloadAtAll();
                }
                this.tooltip = I18n.format("wdl.gui.wdl." + key + ".description", new Object[0]);
            }
            
            @Override
            public boolean mousePressed(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
                if (this.button.mousePressed(GuiWDLButtonList.this.mc, x, y)) {
                    GuiWDLButtonList.this.mc.displayGuiScreen(this.toOpen);
                    this.button.playPressSound(GuiWDLButtonList.this.mc.getSoundHandler());
                    return true;
                }
                return false;
            }
            
            @Override
            public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
            }
            
            @Override
            public void func_192633_a(final int p_192633_1_, final int p_192633_2_, final int p_192633_3_, final float p_192633_4_) {
            }
            
            @Override
            public void func_192634_a(final int p_192634_1_, final int p_192634_2_, final int p_192634_3_, final int p_192634_4_, final int p_192634_5_, final int p_192634_6_, final int p_192634_7_, final boolean p_192634_8_, final float p_192634_9_) {
                this.button.xPosition = GuiWDL.width / 2 - 100;
                this.button.yPosition = p_192634_3_;
                this.button.drawButton(GuiWDLButtonList.this.mc, GuiWDLButtonList.this.mouseX, GuiWDLButtonList.this.mouseY, p_192634_9_);
                if (this.button.isMouseOver()) {
                    GuiWDL.access$0(GuiWDL.this, this.tooltip);
                }
            }
        }
    }
}
