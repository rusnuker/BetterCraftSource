// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.client.resources.I18n;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.settings.GameSettings;

public class GuiSnooper extends GuiScreen
{
    private final GuiScreen lastScreen;
    private final GameSettings game_settings_2;
    private final java.util.List<String> keys;
    private final java.util.List<String> values;
    private String title;
    private String[] desc;
    private List list;
    private GuiButton toggleButton;
    
    public GuiSnooper(final GuiScreen p_i1061_1_, final GameSettings p_i1061_2_) {
        this.keys = (java.util.List<String>)Lists.newArrayList();
        this.values = (java.util.List<String>)Lists.newArrayList();
        this.lastScreen = p_i1061_1_;
        this.game_settings_2 = p_i1061_2_;
    }
    
    @Override
    public void initGui() {
        this.title = I18n.format("options.snooper.title", new Object[0]);
        final String s = I18n.format("options.snooper.desc", new Object[0]);
        final java.util.List<String> list = (java.util.List<String>)Lists.newArrayList();
        for (final String s2 : this.fontRendererObj.listFormattedStringToWidth(s, GuiSnooper.width - 30)) {
            list.add(s2);
        }
        this.desc = list.toArray(new String[list.size()]);
        this.keys.clear();
        this.values.clear();
        this.toggleButton = this.addButton(new GuiButton(1, GuiSnooper.width / 2 - 152, GuiSnooper.height - 30, 150, 20, this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED)));
        this.buttonList.add(new GuiButton(2, GuiSnooper.width / 2 + 2, GuiSnooper.height - 30, 150, 20, I18n.format("gui.done", new Object[0])));
        final boolean flag = this.mc.getIntegratedServer() != null && this.mc.getIntegratedServer().getPlayerUsageSnooper() != null;
        for (final Map.Entry<String, String> entry : new TreeMap(this.mc.getPlayerUsageSnooper().getCurrentStats()).entrySet()) {
            this.keys.add(String.valueOf(flag ? "C " : "") + entry.getKey());
            this.values.add(this.fontRendererObj.trimStringToWidth(entry.getValue(), GuiSnooper.width - 220));
        }
        if (flag) {
            for (final Map.Entry<String, String> entry2 : new TreeMap(this.mc.getIntegratedServer().getPlayerUsageSnooper().getCurrentStats()).entrySet()) {
                this.keys.add("S " + entry2.getKey());
                this.values.add(this.fontRendererObj.trimStringToWidth(entry2.getValue(), GuiSnooper.width - 220));
            }
        }
        this.list = new List();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 2) {
                this.game_settings_2.saveOptions();
                this.game_settings_2.saveOptions();
                this.mc.displayGuiScreen(this.lastScreen);
            }
            if (button.id == 1) {
                this.game_settings_2.setOptionValue(GameSettings.Options.SNOOPER_ENABLED, 1);
                this.toggleButton.displayString = this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawCenteredString(this.fontRendererObj, this.title, GuiSnooper.width / 2, 8, 16777215);
        int i = 22;
        String[] desc;
        for (int length = (desc = this.desc).length, j = 0; j < length; ++j) {
            final String s = desc[j];
            Gui.drawCenteredString(this.fontRendererObj, s, GuiSnooper.width / 2, i, 8421504);
            i += this.fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    class List extends GuiSlot
    {
        public List() {
            super(GuiSnooper.this.mc, GuiSnooper.width, GuiSnooper.height, 80, GuiSnooper.height - 40, GuiSnooper.this.fontRendererObj.FONT_HEIGHT + 1);
        }
        
        @Override
        protected int getSize() {
            return GuiSnooper.this.keys.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return false;
        }
        
        @Override
        protected void drawBackground() {
        }
        
        @Override
        protected void func_192637_a(final int p_192637_1_, final int p_192637_2_, final int p_192637_3_, final int p_192637_4_, final int p_192637_5_, final int p_192637_6_, final float p_192637_7_) {
            GuiSnooper.this.fontRendererObj.drawString(GuiSnooper.this.keys.get(p_192637_1_), 10, p_192637_3_, 16777215);
            GuiSnooper.this.fontRendererObj.drawString(GuiSnooper.this.values.get(p_192637_1_), 230, p_192637_3_, 16777215);
        }
        
        @Override
        protected int getScrollBarX() {
            return this.width - 10;
        }
    }
}
