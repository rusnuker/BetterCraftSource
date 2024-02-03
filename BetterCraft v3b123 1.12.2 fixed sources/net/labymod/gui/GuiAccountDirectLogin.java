// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import net.labymod.utils.Consumer;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.labymod.utils.DrawUtils;
import java.awt.Color;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.core.LabyModCore;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiButton;
import net.labymod.gui.elements.ModTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiAccountDirectLogin extends GuiScreen
{
    private final GuiScreen lastScreen;
    private final boolean saveAccount;
    private ModTextField fieldUsername;
    private ModTextField fieldPassword;
    private GuiButton buttonLogin;
    private boolean displayError;
    private long shakingError;
    private String renderHeadName;
    
    public GuiAccountDirectLogin(final GuiScreen lastScreen, final boolean saveAccount) {
        this.displayError = false;
        this.shakingError = 0L;
        this.lastScreen = lastScreen;
        this.saveAccount = saveAccount;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.fieldUsername = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), GuiAccountDirectLogin.width / 2 - 110, GuiAccountDirectLogin.height / 2 - 60, 220, 20);
        (this.fieldPassword = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), GuiAccountDirectLogin.width / 2 - 110, GuiAccountDirectLogin.height / 2 - 10, 220, 20)).setPasswordBox(true);
        this.fieldUsername.setMaxStringLength(90000);
        this.fieldPassword.setMaxStringLength(90000);
        this.buttonList.add(new GuiButton(1, GuiAccountDirectLogin.width / 2 - 110, GuiAccountDirectLogin.height / 2 + 30, 100, 20, LanguageManager.translate("button_cancel")));
        this.buttonList.add(this.buttonLogin = new GuiButton(2, GuiAccountDirectLogin.width / 2, GuiAccountDirectLogin.height / 2 + 30, 110, 20, ""));
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        this.fieldUsername.drawTextBox();
        this.fieldPassword.drawTextBox();
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.drawString(String.valueOf(LanguageManager.translate("username_email")) + ":", GuiAccountDirectLogin.width / 2 - 110, GuiAccountDirectLogin.height / 2 - 75);
        draw.drawString(String.valueOf(LanguageManager.translate("password")) + ":", GuiAccountDirectLogin.width / 2 - 110, GuiAccountDirectLogin.height / 2 - 25);
        final String offlineString = (!this.fieldUsername.getText().isEmpty() && this.fieldPassword.getText().isEmpty()) ? (String.valueOf(ModColor.cl("8")) + " (" + ModColor.cl("c") + LanguageManager.translate("offline") + ModColor.cl("8") + ")") : "";
        this.buttonLogin.displayString = String.valueOf(LanguageManager.translate(this.saveAccount ? "button_add" : "button_login")) + offlineString;
        this.buttonLogin.enabled = !this.fieldUsername.getText().isEmpty();
        if (this.displayError) {
            draw.drawRectangle(0, 0, GuiAccountDirectLogin.width, 16, Color.RED.getRGB());
            final String errorMessage = LabyMod.getInstance().getAccountManager().getLastErrorMessage();
            int shaking = (int)(System.currentTimeMillis() % 10L) - 5;
            if (this.shakingError + 1000L < System.currentTimeMillis()) {
                shaking = 0;
            }
            draw.drawCenteredString(errorMessage, GuiAccountDirectLogin.width / 2 + shaking, 4.0);
        }
        if (this.renderHeadName != null && !this.renderHeadName.contains("@")) {
            LabyMod.getInstance().getDrawUtils().drawPlayerHead(this.renderHeadName, this.fieldUsername.xPosition - this.fieldUsername.height - 4, this.fieldUsername.yPosition, this.fieldUsername.height);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 1: {
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                break;
            }
            case 2: {
                this.login();
                break;
            }
        }
    }
    
    private void login() {
        final String username = this.fieldUsername.getText();
        final String password = this.fieldPassword.getText();
        final Consumer<Boolean> result = new Consumer<Boolean>() {
            @Override
            public void accept(final Boolean result) {
                if (result) {
                    Minecraft.getMinecraft().displayGuiScreen(GuiAccountDirectLogin.this.lastScreen);
                }
                else {
                    GuiAccountDirectLogin.access$1(GuiAccountDirectLogin.this, true);
                    GuiAccountDirectLogin.access$2(GuiAccountDirectLogin.this, System.currentTimeMillis());
                }
            }
        };
        if (password.isEmpty()) {
            LabyMod.getInstance().getAccountManager().getAccountLoginHandler().handleOfflineLogin(username, this.saveAccount, result);
        }
        else {
            LabyMod.getInstance().getAccountManager().getAccountLoginHandler().handleDirectLogin(username, password, this.saveAccount, result);
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final boolean flag = this.fieldUsername.isFocused();
        this.fieldUsername.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.fieldPassword.mouseClicked(mouseX, mouseY, mouseButton) && flag && !this.fieldUsername.isFocused()) {
            this.renderHeadName = this.fieldUsername.getText();
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            return;
        }
        if (keyCode == 28 && this.buttonLogin.enabled) {
            this.actionPerformed(this.buttonLogin);
        }
        if (keyCode == 15) {
            final boolean focusUsername = this.fieldUsername.isFocused();
            final boolean focusPassword = this.fieldPassword.isFocused();
            this.fieldUsername.setFocused(focusPassword);
            this.fieldPassword.setFocused(focusUsername);
            if (this.fieldPassword.isFocused()) {
                this.renderHeadName = this.fieldUsername.getText();
            }
        }
        this.fieldUsername.textboxKeyTyped(typedChar, keyCode);
        this.fieldPassword.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.fieldUsername.updateCursorCounter();
        this.fieldPassword.updateCursorCounter();
    }
    
    static /* synthetic */ void access$1(final GuiAccountDirectLogin guiAccountDirectLogin, final boolean displayError) {
        guiAccountDirectLogin.displayError = displayError;
    }
    
    static /* synthetic */ void access$2(final GuiAccountDirectLogin guiAccountDirectLogin, final long shakingError) {
        guiAccountDirectLogin.shakingError = shakingError;
    }
}
