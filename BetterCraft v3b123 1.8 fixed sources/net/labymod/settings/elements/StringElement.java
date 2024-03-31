// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings.elements;

import java.io.IOException;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.labymod.main.lang.LanguageManager;
import net.labymod.core.LabyModCore;
import net.labymod.settings.PreviewRenderer;
import net.labymod.settings.LabyModModuleEditorGui;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.utils.ModColor;
import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.main.ModSettings;
import net.labymod.gui.elements.ModTextField;
import net.labymod.utils.Consumer;

public class StringElement extends ControlElement
{
    private String currentValue;
    private Consumer<String> changeListener;
    private ModTextField textField;
    private Consumer<String> callback;
    private boolean hoverExpandButton;
    
    public StringElement(final String displayName, final String configEntryName, final IconData iconData) {
        super(displayName, configEntryName, iconData);
        this.hoverExpandButton = false;
        if (!configEntryName.isEmpty()) {
            try {
                this.currentValue = (String)ModSettings.class.getDeclaredField(configEntryName).get(LabyMod.getSettings());
            }
            catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (final NoSuchFieldException e2) {
                e2.printStackTrace();
            }
        }
        if (this.currentValue == null) {
            this.currentValue = "";
        }
        this.changeListener = new Consumer<String>() {
            @Override
            public void accept(final String accepted) {
                try {
                    ModSettings.class.getDeclaredField(configEntryName).set(LabyMod.getSettings(), accepted);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
                if (StringElement.this.callback != null) {
                    StringElement.this.callback.accept(accepted);
                }
            }
        };
        this.createTextfield();
    }
    
    public StringElement(final String displayName, final LabyModAddon addon, final IconData iconData, final String attribute, String currentValue) {
        super(displayName, iconData);
        this.hoverExpandButton = false;
        if (currentValue == null) {
            currentValue = "";
        }
        this.currentValue = currentValue;
        this.changeListener = new Consumer<String>() {
            @Override
            public void accept(final String accepted) {
                addon.getConfig().addProperty(attribute, accepted);
                addon.loadConfig();
                if (StringElement.this.callback != null) {
                    StringElement.this.callback.accept(accepted);
                }
            }
        };
        this.createTextfield();
    }
    
    public StringElement(final String displayName, final IconData iconData, final String currentValue, final Consumer<String> changeListener) {
        super(displayName, iconData);
        this.hoverExpandButton = false;
        this.currentValue = currentValue;
        this.changeListener = changeListener;
        this.createTextfield();
    }
    
    public StringElement(final String configEntryName, final IconData iconData) {
        this(configEntryName, configEntryName, iconData);
    }
    
    public void createTextfield() {
        this.updateValue();
        this.textField.setCursorPositionEnd();
        this.textField.setFocused(false);
    }
    
    private void updateValue() {
        this.textField.setText((this.currentValue == null) ? " " : this.currentValue);
    }
    
    @Override
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        if (this.textField == null) {
            return;
        }
        this.textField.yPosition = y + 1;
        this.textField.drawTextBox();
        LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_EXPAND);
    }
    
    @Override
    public void unfocus(final int mouseX, final int mouseY, final int mouseButton) {
        super.unfocus(mouseX, mouseY, mouseButton);
        if (this.hoverExpandButton) {
            this.hoverExpandButton = false;
            Minecraft.getMinecraft().displayGuiScreen(new ExpandedStringElementGui(this.textField, Minecraft.getMinecraft().currentScreen, new Consumer<ModTextField>() {
                @Override
                public void accept(final ModTextField accepted) {
                    StringElement.this.textField.setText(accepted.getText());
                    StringElement.this.textField.setFocused(true);
                    StringElement.this.textField.setCursorPosition(accepted.getCursorPosition());
                    StringElement.this.textField.setSelectionPos(accepted.getSelectionEnd());
                    StringElement.this.changeListener.accept(StringElement.this.textField.getText());
                }
            }));
        }
        this.textField.setFocused(false);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textField.mouseClicked(mouseX, mouseY, 0);
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.textField.textboxKeyTyped(typedChar, keyCode)) {
            this.changeListener.accept(this.textField.getText());
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.textField.updateCursorCounter();
    }
    
    public StringElement maxLength(final int maxLength) {
        this.textField.setMaxStringLength(maxLength);
        return this;
    }
    
    public StringElement addCallback(final Consumer<String> callback) {
        this.callback = callback;
        return this;
    }
    
    public class ExpandedStringElementGui extends GuiScreen
    {
        private GuiScreen backgroundScreen;
        private Consumer<ModTextField> callback;
        private ModTextField preField;
        private ModTextField expandedField;
        
        public ExpandedStringElementGui(final ModTextField preField, final GuiScreen backgroundScreen, final Consumer<ModTextField> callback) {
            this.backgroundScreen = backgroundScreen;
            this.callback = callback;
            this.preField = preField;
        }
        
        @Override
        public void initGui() {
            super.initGui();
            GuiScreen.width = ExpandedStringElementGui.width;
            GuiScreen.height = ExpandedStringElementGui.height;
            if (this.backgroundScreen instanceof LabyModModuleEditorGui) {
                PreviewRenderer.getInstance().init(ExpandedStringElementGui.class);
            }
            (this.expandedField = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), ExpandedStringElementGui.width / 2 - 150, ExpandedStringElementGui.height / 4 + 45, 300, 20)).setMaxStringLength(this.preField.getMaxStringLength());
            this.expandedField.setFocused(true);
            this.expandedField.setText(this.preField.getText());
            this.expandedField.setCursorPosition(this.preField.getCursorPosition());
            this.expandedField.setSelectionPos(this.preField.getSelectionEnd());
            this.buttonList.add(new GuiButton(1, ExpandedStringElementGui.width / 2 - 50, ExpandedStringElementGui.height / 4 + 85, 100, 20, LanguageManager.translate("button_done")));
        }
        
        @Override
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
            this.backgroundScreen.drawScreen(mouseX, mouseY, partialTicks);
            Gui.drawRect(0, 0, ExpandedStringElementGui.width, ExpandedStringElementGui.height, Integer.MIN_VALUE);
            Gui.drawRect(ExpandedStringElementGui.width / 2 - 165, ExpandedStringElementGui.height / 4 + 35, ExpandedStringElementGui.width / 2 + 165, ExpandedStringElementGui.height / 4 + 120, Integer.MIN_VALUE);
            this.expandedField.drawTextBox();
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
        
        public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            this.expandedField.mouseClicked(mouseX, mouseY, mouseButton);
            this.callback.accept(this.expandedField);
        }
        
        @Override
        protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
            if (keyCode == 1) {
                Minecraft.getMinecraft().displayGuiScreen(this.backgroundScreen);
            }
            if (this.expandedField.textboxKeyTyped(typedChar, keyCode)) {
                this.callback.accept(this.expandedField);
            }
        }
        
        @Override
        public void updateScreen() {
            this.backgroundScreen.updateScreen();
            this.expandedField.updateCursorCounter();
        }
        
        @Override
        protected void actionPerformed(final GuiButton button) throws IOException {
            super.actionPerformed(button);
            if (button.id == 1) {
                Minecraft.getMinecraft().displayGuiScreen(this.backgroundScreen);
            }
        }
        
        public GuiScreen getBackgroundScreen() {
            return this.backgroundScreen;
        }
    }
}
