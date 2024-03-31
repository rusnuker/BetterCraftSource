// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.altmanager;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltLogin extends GuiScreen
{
    private GuiScreen before;
    private GuiTextField emailField;
    private GuiTextField passwordField;
    
    public GuiAltLogin(final GuiScreen before) {
        this.before = before;
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiButton(0, GuiAltLogin.width / 2 - 100, GuiAltLogin.height / 2 + 60, "Log in"));
        this.buttonList.add(new GuiButton(1, GuiAltLogin.width / 2 - 100, GuiAltLogin.height / 2 + 85, "Back"));
        (this.emailField = new GuiTextField(2, this.fontRendererObj, GuiAltLogin.width / 2 - 100, GuiAltLogin.height / 2 - 10, 200, 20)).setMaxStringLength(254);
        this.emailField.setFocused(true);
        this.passwordField = new GuiTextField(3, this.fontRendererObj, GuiAltLogin.width / 2 - 100, GuiAltLogin.height / 2 + 25, 200, 20);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0) {
            final Alt loginAlt = new Alt(this.emailField.getText(), this.passwordField.getText());
            if (loginAlt.cracked) {
                Login.changeName(loginAlt.name);
                this.mc.displayGuiScreen(new GuiErrorScreen("Success!", "You now switched accounts! (cracked)"));
            }
            else {
                try {
                    Login.login(loginAlt.email, loginAlt.password);
                    this.mc.displayGuiScreen(new GuiErrorScreen("Success!", "You now switched accounts! (premium)"));
                }
                catch (final Exception e) {
                    e.printStackTrace();
                    this.mc.displayGuiScreen(new GuiErrorScreen("Error", "Could not log in..."));
                }
            }
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiAltManager(this.before));
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.emailField.textboxKeyTyped(typedChar, keyCode);
        this.passwordField.textboxKeyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.emailField.mouseClicked(mouseX, mouseY, mouseButton);
        this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void updateScreen() {
        this.emailField.updateCursorCounter();
        this.passwordField.updateCursorCounter();
        super.updateScreen();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.emailField.drawTextBox();
        this.passwordField.drawTextBox();
        this.fontRendererObj.drawString("§7E-mail / Username: ", GuiAltLogin.width / 2 - 100, GuiAltLogin.height / 2 - 21, 16777215);
        this.fontRendererObj.drawString("§7Password: ", GuiAltLogin.width / 2 - 100, GuiAltLogin.height / 2 + 14, 16777215);
        Gui.drawCenteredString(this.fontRendererObj, "§7(Leave password blank for cracked login)", GuiAltLogin.width / 2, GuiAltLogin.height / 2 + 50, 16777215);
        GlStateManager.scale(4.0f, 4.0f, 1.0f);
        GlStateManager.scale(0.25, 0.25, 1.0);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
