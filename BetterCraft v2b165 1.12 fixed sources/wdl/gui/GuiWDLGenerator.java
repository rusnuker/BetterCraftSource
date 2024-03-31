// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.gui.GuiCustomizeWorldScreen;
import net.minecraft.client.gui.GuiCreateFlatWorld;
import net.minecraft.client.gui.GuiFlatPresets;
import wdl.WDL;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLGenerator extends GuiScreen
{
    private String title;
    private GuiScreen parent;
    private GuiTextField seedField;
    private GuiButton generatorBtn;
    private GuiButton generateStructuresBtn;
    private GuiButton settingsPageBtn;
    private String seedText;
    
    public GuiWDLGenerator(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        this.seedText = I18n.format("wdl.gui.generator.seed", new Object[0]);
        final int seedWidth = this.fontRendererObj.getStringWidth(String.valueOf(this.seedText) + " ");
        this.buttonList.clear();
        this.title = I18n.format("wdl.gui.generator.title", WDL.baseFolderName.replace('@', ':'));
        int y = GuiWDLGenerator.height / 4 - 15;
        (this.seedField = new GuiTextField(40, this.fontRendererObj, GuiWDLGenerator.width / 2 - (100 - seedWidth), y, 200 - seedWidth, 18)).setText(WDL.worldProps.getProperty("RandomSeed"));
        y += 22;
        this.generatorBtn = new GuiButton(1, GuiWDLGenerator.width / 2 - 100, y, this.getGeneratorText());
        this.buttonList.add(this.generatorBtn);
        y += 22;
        this.generateStructuresBtn = new GuiButton(2, GuiWDLGenerator.width / 2 - 100, y, this.getGenerateStructuresText());
        this.buttonList.add(this.generateStructuresBtn);
        y += 22;
        this.settingsPageBtn = new GuiButton(3, GuiWDLGenerator.width / 2 - 100, y, "");
        this.updateSettingsButtonVisibility();
        this.buttonList.add(this.settingsPageBtn);
        this.buttonList.add(new GuiButton(100, GuiWDLGenerator.width / 2 - 100, GuiWDLGenerator.height - 29, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button.enabled) {
            if (button.id == 1) {
                this.cycleGenerator();
            }
            else if (button.id == 2) {
                this.cycleGenerateStructures();
            }
            else if (button.id == 3) {
                if (WDL.worldProps.getProperty("MapGenerator", "").equals("flat")) {
                    this.mc.displayGuiScreen(new GuiFlatPresets(new GuiCreateFlatWorldProxy()));
                }
                else if (WDL.worldProps.getProperty("MapGenerator", "").equals("custom")) {
                    this.mc.displayGuiScreen(new GuiCustomizeWorldScreen(new GuiCreateWorldProxy(), WDL.worldProps.getProperty("GeneratorOptions", "")));
                }
            }
            else if (button.id == 100) {
                this.mc.displayGuiScreen(this.parent);
            }
        }
    }
    
    @Override
    public void onGuiClosed() {
        WDL.worldProps.setProperty("RandomSeed", this.seedField.getText());
        WDL.saveProps();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.seedField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.seedField.textboxKeyTyped(typedChar, keyCode);
    }
    
    @Override
    public void updateScreen() {
        this.seedField.updateCursorCounter();
        super.updateScreen();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Utils.drawListBackground(23, 32, 0, 0, GuiWDLGenerator.height, GuiWDLGenerator.width);
        Gui.drawCenteredString(this.fontRendererObj, this.title, GuiWDLGenerator.width / 2, 8, 16777215);
        Gui.drawString(this.fontRendererObj, this.seedText, GuiWDLGenerator.width / 2 - 100, GuiWDLGenerator.height / 4 - 10, 16777215);
        this.seedField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
        String tooltip = null;
        if (Utils.isMouseOverTextBox(mouseX, mouseY, this.seedField)) {
            tooltip = I18n.format("wdl.gui.generator.seed.description", new Object[0]);
        }
        else if (this.generatorBtn.isMouseOver()) {
            tooltip = I18n.format("wdl.gui.generator.generator.description", new Object[0]);
        }
        else if (this.generateStructuresBtn.isMouseOver()) {
            tooltip = I18n.format("wdl.gui.generator.generateStructures.description", new Object[0]);
        }
        Utils.drawGuiInfoBox(tooltip, GuiWDLGenerator.width, GuiWDLGenerator.height, 48);
    }
    
    private void cycleGenerator() {
        final String prop = WDL.worldProps.getProperty("MapGenerator");
        if (prop.equals("void")) {
            WDL.worldProps.setProperty("MapGenerator", "default");
            WDL.worldProps.setProperty("GeneratorName", "default");
            WDL.worldProps.setProperty("GeneratorVersion", "1");
            WDL.worldProps.setProperty("GeneratorOptions", "");
        }
        else if (prop.equals("default")) {
            WDL.worldProps.setProperty("MapGenerator", "flat");
            WDL.worldProps.setProperty("GeneratorName", "flat");
            WDL.worldProps.setProperty("GeneratorVersion", "0");
            WDL.worldProps.setProperty("GeneratorOptions", "");
        }
        else if (prop.equals("flat")) {
            WDL.worldProps.setProperty("MapGenerator", "largeBiomes");
            WDL.worldProps.setProperty("GeneratorName", "largeBiomes");
            WDL.worldProps.setProperty("GeneratorVersion", "0");
            WDL.worldProps.setProperty("GeneratorOptions", "");
        }
        else if (prop.equals("largeBiomes")) {
            WDL.worldProps.setProperty("MapGenerator", "amplified");
            WDL.worldProps.setProperty("GeneratorName", "amplified");
            WDL.worldProps.setProperty("GeneratorVersion", "0");
            WDL.worldProps.setProperty("GeneratorOptions", "");
        }
        else if (prop.equals("amplified")) {
            WDL.worldProps.setProperty("MapGenerator", "custom");
            WDL.worldProps.setProperty("GeneratorName", "custom");
            WDL.worldProps.setProperty("GeneratorVersion", "0");
            WDL.worldProps.setProperty("GeneratorOptions", "");
        }
        else if (prop.equals("custom")) {
            WDL.worldProps.setProperty("MapGenerator", "legacy");
            WDL.worldProps.setProperty("GeneratorName", "default_1_1");
            WDL.worldProps.setProperty("GeneratorVersion", "0");
            WDL.worldProps.setProperty("GeneratorOptions", "");
        }
        else {
            WDL.worldProps.setProperty("MapGenerator", "void");
            WDL.worldProps.setProperty("GeneratorName", "flat");
            WDL.worldProps.setProperty("GeneratorVersion", "0");
            WDL.worldProps.setProperty("GeneratorOptions", ";0");
        }
        this.generatorBtn.displayString = this.getGeneratorText();
        this.updateSettingsButtonVisibility();
    }
    
    private void cycleGenerateStructures() {
        if (WDL.worldProps.getProperty("MapFeatures").equals("true")) {
            WDL.worldProps.setProperty("MapFeatures", "false");
        }
        else {
            WDL.worldProps.setProperty("MapFeatures", "true");
        }
        this.generateStructuresBtn.displayString = this.getGenerateStructuresText();
    }
    
    private void updateSettingsButtonVisibility() {
        if (WDL.worldProps.getProperty("MapGenerator", "").equals("flat")) {
            this.settingsPageBtn.visible = true;
            this.settingsPageBtn.displayString = I18n.format("wdl.gui.generator.flatSettings", new Object[0]);
        }
        else if (WDL.worldProps.getProperty("MapGenerator", "").equals("custom")) {
            this.settingsPageBtn.visible = true;
            this.settingsPageBtn.displayString = I18n.format("wdl.gui.generator.customSettings", new Object[0]);
        }
        else {
            this.settingsPageBtn.visible = false;
        }
    }
    
    private String getGeneratorText() {
        return I18n.format("wdl.gui.generator.generator." + WDL.worldProps.getProperty("MapGenerator"), new Object[0]);
    }
    
    private String getGenerateStructuresText() {
        return I18n.format("wdl.gui.generator.generateStructures." + WDL.worldProps.getProperty("MapFeatures"), new Object[0]);
    }
    
    private class GuiCreateFlatWorldProxy extends GuiCreateFlatWorld
    {
        public GuiCreateFlatWorldProxy() {
            super(null, WDL.worldProps.getProperty("GeneratorOptions", ""));
        }
        
        @Override
        public void initGui() {
            this.mc.displayGuiScreen(GuiWDLGenerator.this);
        }
        
        @Override
        protected void actionPerformed(final GuiButton button) throws IOException {
        }
        
        @Override
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        }
        
        @Override
        public String getPreset() {
            return WDL.worldProps.getProperty("GeneratorOptions", "");
        }
        
        @Override
        public void setPreset(String preset) {
            if (preset == null) {
                preset = "";
            }
            WDL.worldProps.setProperty("GeneratorOptions", preset);
        }
    }
    
    private class GuiCreateWorldProxy extends GuiCreateWorld
    {
        public GuiCreateWorldProxy() {
            super(GuiWDLGenerator.this);
            this.chunkProviderSettingsJson = WDL.worldProps.getProperty("GeneratorOptions", "");
        }
        
        @Override
        public void initGui() {
            this.mc.displayGuiScreen(GuiWDLGenerator.this);
            WDL.worldProps.setProperty("GeneratorOptions", this.chunkProviderSettingsJson);
        }
        
        @Override
        protected void actionPerformed(final GuiButton button) throws IOException {
        }
        
        @Override
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        }
    }
}
