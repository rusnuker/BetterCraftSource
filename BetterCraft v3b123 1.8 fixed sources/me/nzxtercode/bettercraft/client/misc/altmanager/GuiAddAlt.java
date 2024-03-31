// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.altmanager;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.Gui;
import me.nzxtercode.bettercraft.client.misc.altmanager.impl.AccountManager;
import me.nzxtercode.bettercraft.client.misc.altmanager.impl.Account;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiAddAlt extends GuiScreen
{
    private GuiTextField email;
    private GuiPasswordField password;
    private GuiScreen parent;
    
    public GuiAddAlt(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiAddAlt.width / 2 - 100, GuiAddAlt.height / 4 + 92 + 12, "Add"));
        this.buttonList.add(new GuiButton(1, GuiAddAlt.width / 2 - 100, GuiAddAlt.height / 4 + 116 + 12, "Back"));
        (this.email = new GuiTextField(0, this.fontRendererObj, GuiAddAlt.width / 2 - 100, 70, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
        this.email.setFocused(true);
        (this.password = new GuiPasswordField(this.fontRendererObj, GuiAddAlt.width / 2 - 100, 120, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
    }
    
    public void keyTyped(final char character, final int keyCode) throws IOException {
        this.email.textboxKeyTyped(character, keyCode);
        this.password.textboxKeyTyped(character, keyCode);
        if (keyCode == 15) {
            this.email.setFocused(!this.email.isFocused());
            this.password.setFocused(!this.password.isFocused());
        }
        if (keyCode == 28) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.email.mouseClicked(mouseX, mouseY, mouseButton);
        this.password.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                if (!this.email.getText().isEmpty()) {
                    final Account account = new Account(this.email.getText(), this.password.getText(), this.email.getText());
                    AccountManager.getInstance().getAccounts().add(account);
                    AccountManager.getInstance().save();
                }
                this.mc.displayGuiScreen(this.parent);
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.parent);
                break;
            }
        }
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void updateScreen() {
        this.email.updateCursorCounter();
        this.password.updateCursorCounter();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.mc.fontRendererObj, "Add Alt", GuiAddAlt.width / 2, 20, -1);
        Gui.drawCenteredString(this.mc.fontRendererObj, String.valueOf(EnumChatFormatting.GRAY.toString()) + "Username / Email", GuiAddAlt.width / 2 - 60, 57, -1);
        this.email.drawTextBox();
        Gui.drawCenteredString(this.mc.fontRendererObj, String.valueOf(EnumChatFormatting.GRAY.toString()) + "Password", GuiAddAlt.width / 2 - 75, 107, -1);
        this.password.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
