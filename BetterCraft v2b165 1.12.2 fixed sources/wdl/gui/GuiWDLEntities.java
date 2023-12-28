// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import java.util.Iterator;
import com.google.common.collect.Multimap;
import wdl.api.IWDLMessageType;
import wdl.WDLMessages;
import wdl.WDLMessageTypes;
import java.util.Collections;
import java.util.Collection;
import wdl.EntityUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import wdl.WDLPluginChannels;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import wdl.WDL;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLEntities extends GuiScreen
{
    private GuiEntityList entityList;
    private GuiScreen parent;
    private GuiButton rangeModeButton;
    private GuiButton presetsButton;
    private String mode;
    
    public GuiWDLEntities(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(200, GuiWDLEntities.width / 2 - 100, GuiWDLEntities.height - 29, "OK"));
        this.rangeModeButton = new GuiButton(100, GuiWDLEntities.width / 2 - 155, 18, 150, 20, this.getRangeModeText());
        this.presetsButton = new GuiButton(101, GuiWDLEntities.width / 2 + 5, 18, 150, 20, I18n.format("wdl.gui.entities.rangePresets", new Object[0]));
        this.mode = WDL.worldProps.getProperty("Entity.TrackDistanceMode");
        this.presetsButton.enabled = this.shouldEnablePresetsButton();
        this.buttonList.add(this.rangeModeButton);
        this.buttonList.add(this.presetsButton);
        this.entityList = new GuiEntityList();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.entityList.handleMouseInput();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 100) {
            this.cycleRangeMode();
        }
        if (button.id == 101 && button.enabled) {
            this.mc.displayGuiScreen(new GuiWDLEntityRangePresets(this));
        }
        if (button.id == 200) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    public void onGuiClosed() {
        WDL.saveProps();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (this.entityList.mouseClicked(mouseX, mouseY, mouseButton)) {
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.entityList.mouseReleased(mouseX, mouseY, state)) {
            return;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.entityList.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawCenteredString(this.fontRendererObj, I18n.format("wdl.gui.entities.title", new Object[0]), GuiWDLEntities.width / 2, 8, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    private void cycleRangeMode() {
        if (this.mode.equals("default")) {
            if (WDLPluginChannels.hasServerEntityRange()) {
                this.mode = "server";
            }
            else {
                this.mode = "user";
            }
        }
        else if (this.mode.equals("server")) {
            this.mode = "user";
        }
        else {
            this.mode = "default";
        }
        WDL.worldProps.setProperty("Entity.TrackDistanceMode", this.mode);
        this.rangeModeButton.displayString = this.getRangeModeText();
        this.presetsButton.enabled = this.shouldEnablePresetsButton();
    }
    
    private String getRangeModeText() {
        final String mode = WDL.worldProps.getProperty("Entity.TrackDistanceMode");
        return I18n.format("wdl.gui.entities.trackDistanceMode." + mode, new Object[0]);
    }
    
    private boolean shouldEnablePresetsButton() {
        return this.mode.equals("user");
    }
    
    private class GuiEntityList extends GuiListExtended
    {
        private int largestWidth;
        private int totalWidth;
        private List<IGuiListEntry> entries;
        final /* synthetic */ GuiWDLEntities this$0;
        
        public GuiEntityList() {
            super(GuiWDLEntities.this.mc, GuiWDLEntities.width, GuiWDLEntities.height, 39, GuiWDLEntities.height - 32, 20);
            this.entries = new ArrayList<IGuiListEntry>() {
                {
                    try {
                        int largestWidthSoFar = 0;
                        final Multimap<String, String> entities = EntityUtils.getEntitiesByGroup();
                        final List<String> categories = new ArrayList<String>(entities.keySet());
                        categories.remove("Passive");
                        categories.remove("Hostile");
                        categories.remove("Other");
                        Collections.sort(categories);
                        categories.add(0, "Hostile");
                        categories.add(1, "Passive");
                        categories.add("Other");
                        for (final String category : categories) {
                            final CategoryEntry categoryEntry = new CategoryEntry(category);
                            ((ArrayList<CategoryEntry>)this).add(categoryEntry);
                            final List<String> categoryEntities = new ArrayList<String>(entities.get(category));
                            Collections.sort(categoryEntities);
                            for (final String entity : categoryEntities) {
                                ((ArrayList<EntityEntry>)this).add(new EntityEntry(categoryEntry, entity));
                                final int width = GuiEntityList.this.this$0.fontRendererObj.getStringWidth(entity);
                                if (width > largestWidthSoFar) {
                                    largestWidthSoFar = width;
                                }
                            }
                        }
                        GuiEntityList.access$0(GuiEntityList.this, largestWidthSoFar);
                        GuiEntityList.access$2(GuiEntityList.this, GuiEntityList.this.largestWidth + 255);
                    }
                    catch (final Exception e) {
                        WDLMessages.chatMessageTranslated(WDLMessageTypes.ERROR, "wdl.messages.generalError.failedToSetUpEntityUI", e);
                        Minecraft.getMinecraft().displayGuiScreen(null);
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
        
        @Override
        protected int getScrollBarX() {
            return GuiWDLEntities.width / 2 + this.totalWidth / 2 + 10;
        }
        
        static /* synthetic */ void access$0(final GuiEntityList list, final int largestWidth) {
            list.largestWidth = largestWidth;
        }
        
        static /* synthetic */ void access$2(final GuiEntityList list, final int totalWidth) {
            list.totalWidth = totalWidth;
        }
        
        private class CategoryEntry implements IGuiListEntry
        {
            private final String group;
            private final int labelWidth;
            private final GuiButton enableGroupButton;
            private boolean groupEnabled;
            
            public CategoryEntry(final String group) {
                this.group = group;
                this.labelWidth = GuiEntityList.this.mc.fontRendererObj.getStringWidth(group);
                this.groupEnabled = WDL.worldProps.getProperty("EntityGroup." + group + ".Enabled", "true").equals("true");
                this.enableGroupButton = new GuiButton(0, 0, 0, 90, 18, this.getButtonText());
            }
            
            @Override
            public boolean mousePressed(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
                if (this.enableGroupButton.mousePressed(GuiEntityList.this.mc, x, y)) {
                    this.groupEnabled ^= true;
                    this.enableGroupButton.playPressSound(GuiEntityList.this.mc.getSoundHandler());
                    this.enableGroupButton.displayString = this.getButtonText();
                    WDL.worldProps.setProperty("EntityGroup." + this.group + ".Enabled", Boolean.toString(this.groupEnabled));
                    return true;
                }
                return false;
            }
            
            @Override
            public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
            }
            
            boolean isGroupEnabled() {
                return this.groupEnabled;
            }
            
            private String getButtonText() {
                if (this.groupEnabled) {
                    return I18n.format("wdl.gui.entities.group.enabled", new Object[0]);
                }
                return I18n.format("wdl.gui.entities.group.disabled", new Object[0]);
            }
            
            @Override
            public void func_192633_a(final int p_192633_1_, final int p_192633_2_, final int p_192633_3_, final float p_192633_4_) {
            }
            
            @Override
            public void func_192634_a(final int p_192634_1_, final int p_192634_2_, final int p_192634_3_, final int p_192634_4_, final int p_192634_5_, final int p_192634_6_, final int p_192634_7_, final boolean p_192634_8_, final float p_192634_9_) {
                GuiEntityList.this.mc.fontRendererObj.drawString(this.group, p_192634_2_ + 55 - this.labelWidth / 2, p_192634_3_ + GuiEntityList.this.slotHeight - GuiEntityList.this.mc.fontRendererObj.FONT_HEIGHT - 1, 16777215);
                this.enableGroupButton.xPosition = p_192634_2_ + 110;
                this.enableGroupButton.yPosition = p_192634_3_;
                this.enableGroupButton.displayString = this.getButtonText();
                this.enableGroupButton.drawButton(GuiEntityList.this.mc, GuiEntityList.this.mouseX, GuiEntityList.this.mouseY, p_192634_9_);
            }
        }
        
        private class EntityEntry implements IGuiListEntry
        {
            private final CategoryEntry category;
            private final String entity;
            private final GuiButton onOffButton;
            private final GuiSlider rangeSlider;
            private boolean entityEnabled;
            private int range;
            private String cachedMode;
            
            public EntityEntry(final CategoryEntry category, final String entity) {
                this.category = category;
                this.entity = entity;
                this.entityEnabled = WDL.worldProps.getProperty("Entity." + entity + ".Enabled", "true").equals("true");
                this.range = EntityUtils.getEntityTrackDistance(entity);
                this.onOffButton = new GuiButton(0, 0, 0, 75, 18, this.getButtonText());
                this.onOffButton.enabled = category.isGroupEnabled();
                this.rangeSlider = new GuiSlider(1, 0, 0, 150, 18, "wdl.gui.entities.trackDistance", this.range, 256);
                this.cachedMode = GuiEntityList.this.this$0.mode;
                this.rangeSlider.enabled = this.cachedMode.equals("user");
            }
            
            @Override
            public boolean mousePressed(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
                if (this.onOffButton.mousePressed(GuiEntityList.this.mc, x, y)) {
                    this.entityEnabled ^= true;
                    this.onOffButton.playPressSound(GuiEntityList.this.mc.getSoundHandler());
                    this.onOffButton.displayString = this.getButtonText();
                    WDL.worldProps.setProperty("Entity." + this.entity + ".Enabled", Boolean.toString(this.entityEnabled));
                    return true;
                }
                if (this.rangeSlider.mousePressed(GuiEntityList.this.mc, x, y)) {
                    this.range = this.rangeSlider.getValue();
                    WDL.worldProps.setProperty("Entity." + this.entity + ".TrackDistance", Integer.toString(this.range));
                    return true;
                }
                return false;
            }
            
            @Override
            public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
                this.rangeSlider.mouseReleased(x, y);
                if (this.cachedMode.equals("user")) {
                    this.range = this.rangeSlider.getValue();
                    WDL.worldProps.setProperty("Entity." + this.entity + ".TrackDistance", Integer.toString(this.range));
                }
            }
            
            private String getButtonText() {
                if (this.category.isGroupEnabled() && this.entityEnabled) {
                    return I18n.format("wdl.gui.entities.entity.included", new Object[0]);
                }
                return I18n.format("wdl.gui.entities.entity.ignored", new Object[0]);
            }
            
            @Override
            public void func_192633_a(final int p_192633_1_, final int p_192633_2_, final int p_192633_3_, final float p_192633_4_) {
            }
            
            @Override
            public void func_192634_a(final int p_192634_1_, final int p_192634_2_, final int p_192634_3_, final int p_192634_4_, final int p_192634_5_, final int p_192634_6_, final int p_192634_7_, final boolean p_192634_8_, final float p_192634_9_) {
                final int center = GuiWDLEntities.width / 2 - GuiEntityList.this.totalWidth / 2 + GuiEntityList.this.largestWidth + 10;
                GuiEntityList.this.mc.fontRendererObj.drawString(this.entity, center - GuiEntityList.this.largestWidth - 10, p_192634_3_ + GuiEntityList.this.slotHeight / 2 - GuiEntityList.this.mc.fontRendererObj.FONT_HEIGHT / 2, 16777215);
                this.onOffButton.xPosition = center;
                this.onOffButton.yPosition = p_192634_3_;
                this.onOffButton.enabled = this.category.isGroupEnabled();
                this.onOffButton.displayString = this.getButtonText();
                this.rangeSlider.xPosition = center + 85;
                this.rangeSlider.yPosition = p_192634_3_;
                if (!this.cachedMode.equals(GuiWDLEntities.this.mode)) {
                    this.cachedMode = GuiWDLEntities.this.mode;
                    this.rangeSlider.enabled = this.cachedMode.equals("user");
                    this.rangeSlider.setValue(EntityUtils.getEntityTrackDistance(this.entity));
                }
                this.onOffButton.drawButton(GuiEntityList.this.mc, GuiEntityList.this.mouseX, GuiEntityList.this.mouseY, p_192634_9_);
                this.rangeSlider.drawButton(GuiEntityList.this.mc, GuiEntityList.this.mouseX, GuiEntityList.this.mouseY, p_192634_9_);
            }
        }
    }
}
