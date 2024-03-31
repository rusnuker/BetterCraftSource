// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import java.io.IOException;
import net.labymod.utils.DrawUtils;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.labymod.main.LabyMod;
import net.labymod.utils.Consumer;
import net.labymod.main.lang.LanguageManager;
import net.labymod.core.LabyModCore;
import net.minecraft.client.gui.GuiButton;
import net.labymod.utils.manager.SignManager;
import org.lwjgl.input.Keyboard;
import net.labymod.gui.elements.CheckBox;
import net.labymod.gui.elements.ModTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiSignSearch extends GuiScreen
{
    private GuiScreen lastScreen;
    private ModTextField fieldSearch;
    private ModTextField fieldBlacklist;
    private CheckBox checkBoxAdvanced;
    private CheckBox checkBoxFilterFullServer;
    private CheckBox checkBoxNightmode;
    
    public GuiSignSearch(final GuiScreen lastScreen) {
        this.lastScreen = lastScreen;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiSignSearch.width / 2 - 100, GuiSignSearch.height / 4 + (SignManager.getSignSearchSettings().isUseAdvancedOptions() ? 90 : 50), "Done"));
        (this.fieldSearch = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), GuiSignSearch.width / 2 - 100, GuiSignSearch.height / 4 + 20, 200, 20)).setBlackBox(false);
        this.fieldSearch.setText(SignManager.getSignSearchSettings().getSearchString());
        this.fieldSearch.setPlaceHolder(String.valueOf(LanguageManager.translate("search_on_signs")) + "..");
        this.fieldSearch.setFocused(false);
        this.fieldSearch.setMaxStringLength(256);
        (this.fieldBlacklist = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), GuiSignSearch.width / 2 - 20, GuiSignSearch.height / 4 + 50, 120, 20)).setBlackBox(false);
        this.fieldBlacklist.setText(SignManager.getSignSearchSettings().getBlacklistString());
        this.fieldBlacklist.setPlaceHolder(String.valueOf(LanguageManager.translate("blacklist")) + "..");
        this.fieldBlacklist.setVisible(SignManager.getSignSearchSettings().isUseAdvancedOptions());
        this.fieldBlacklist.setMaxStringLength(256);
        (this.checkBoxAdvanced = new CheckBox(LanguageManager.translate("button_advanced"), SignManager.getSignSearchSettings().isUseAdvancedOptions() ? CheckBox.EnumCheckBoxValue.ENABLED : CheckBox.EnumCheckBoxValue.DISABLED, null, GuiSignSearch.width / 2 + 100 + 5, GuiSignSearch.height / 4 + 20, 20, 20)).setUpdateListener(new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept(final CheckBox.EnumCheckBoxValue accepted) {
                SignManager.getSignSearchSettings().setUseAdvancedOptions(accepted == CheckBox.EnumCheckBoxValue.ENABLED);
                GuiSignSearch.this.initGui();
            }
        });
        (this.checkBoxFilterFullServer = new CheckBox(LanguageManager.translate("filter_full_servers"), SignManager.getSignSearchSettings().isFilterFullServer() ? CheckBox.EnumCheckBoxValue.ENABLED : CheckBox.EnumCheckBoxValue.DISABLED, null, GuiSignSearch.width / 2 - 100 + 5, GuiSignSearch.height / 4 + 50, 20, 20)).setVisible(SignManager.getSignSearchSettings().isUseAdvancedOptions());
        this.checkBoxFilterFullServer.setUpdateListener(new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept(final CheckBox.EnumCheckBoxValue accepted) {
                SignManager.getSignSearchSettings().setFilterFullServer(accepted == CheckBox.EnumCheckBoxValue.ENABLED);
                GuiSignSearch.this.initGui();
            }
        });
        this.checkBoxFilterFullServer.setDescription(LanguageManager.translate("filter_full_servers_description"));
        (this.checkBoxNightmode = new CheckBox(LanguageManager.translate("filter_empty_servers"), SignManager.getSignSearchSettings().isFilterEmptyServer() ? CheckBox.EnumCheckBoxValue.ENABLED : CheckBox.EnumCheckBoxValue.DISABLED, null, GuiSignSearch.width / 2 - 100 + 20 + 15 + 5, GuiSignSearch.height / 4 + 50, 20, 20)).setVisible(SignManager.getSignSearchSettings().isUseAdvancedOptions());
        this.checkBoxNightmode.setUpdateListener(new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept(final CheckBox.EnumCheckBoxValue accepted) {
                SignManager.getSignSearchSettings().setFilterEmptyServer(accepted == CheckBox.EnumCheckBoxValue.ENABLED);
                GuiSignSearch.this.initGui();
            }
        });
        this.checkBoxNightmode.setDescription(LanguageManager.translate("filter_empty_servers_description"));
        SignManager.getSignSearchSettings().update();
        super.initGui();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        this.fieldSearch.drawTextBox();
        this.fieldBlacklist.drawTextBox();
        this.checkBoxAdvanced.drawCheckbox(mouseX, mouseY);
        this.checkBoxFilterFullServer.drawCheckbox(mouseX, mouseY);
        this.checkBoxNightmode.drawCheckbox(mouseX, mouseY);
        draw.drawCenteredString(LanguageManager.translate("title_sign_search"), GuiSignSearch.width / 2, GuiSignSearch.height / 4);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_SIGNSEARCH);
        LabyMod.getInstance().getDrawUtils().drawTexture(this.fieldSearch.xPosition - 22, this.fieldSearch.yPosition, 255.0, 255.0, 20.0, 20.0);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0) {
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (!this.fieldSearch.isFocused() && !this.fieldBlacklist.isFocused()) {
            this.fieldSearch.setFocused(true);
        }
        if (this.fieldSearch.textboxKeyTyped(typedChar, keyCode)) {
            SignManager.getSignSearchSettings().setSearchString(this.fieldSearch.getText());
        }
        if (this.fieldBlacklist.textboxKeyTyped(typedChar, keyCode)) {
            SignManager.getSignSearchSettings().setBlacklistString(this.fieldBlacklist.getText());
        }
        SignManager.getSignSearchSettings().update();
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldSearch.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldBlacklist.mouseClicked(mouseX, mouseY, mouseButton);
        this.checkBoxAdvanced.mouseClicked(mouseX, mouseY, mouseButton);
        this.checkBoxFilterFullServer.mouseClicked(mouseX, mouseY, mouseButton);
        this.checkBoxNightmode.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.fieldSearch.updateCursorCounter();
        this.fieldBlacklist.updateCursorCounter();
    }
}
