// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.rcon;

import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiRconConnection extends GuiScreen
{
    private GuiTextField nameBox;
    private GuiTextField ipBox;
    private GuiTextField passwordBox;
    private GuiTextField commandBox;
    private GuiTextField portBox;
    private GuiScreen before;
    
    public GuiRconConnection(final GuiScreen before) {
        this.before = before;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiRconConnection.width / 2 - 100, GuiRconConnection.height / 4 + 115, "§aStart"));
        this.buttonList.add(new GuiButton(1, GuiRconConnection.width / 2 - 100, GuiRconConnection.height / 4 + 140, "Back"));
        (this.ipBox = new GuiTextField(1, this.fontRendererObj, GuiRconConnection.width / 2 - 100, GuiRconConnection.height / 40 + 40, 90, 20)).setFocused(false);
        this.ipBox.setText("127.0.0.1");
        (this.portBox = new GuiTextField(1, this.fontRendererObj, GuiRconConnection.width / 2 + 10, GuiRconConnection.height / 40 + 40, 90, 20)).setFocused(false);
        this.portBox.setText("25565");
        (this.commandBox = new GuiTextField(1, this.fontRendererObj, GuiRconConnection.width / 2 - 100, GuiRconConnection.height / 50 + 85, 200, 20)).setFocused(false);
        this.commandBox.setText("/op " + Minecraft.getSession().getUsername());
        (this.nameBox = new GuiTextField(0, this.fontRendererObj, GuiRconConnection.width / 2 - 100, GuiRconConnection.height / 50 + 130, 90, 20)).setMaxStringLength(48);
        this.nameBox.setFocused(true);
        this.nameBox.setText(Minecraft.getSession().getUsername());
        (this.passwordBox = new GuiTextField(1, this.fontRendererObj, GuiRconConnection.width / 2 + 10, GuiRconConnection.height / 50 + 130, 90, 20)).setFocused(false);
        this.passwordBox.setText("Password");
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton clickedButton) {
        if (clickedButton.id == 1) {
            this.mc.displayGuiScreen(this.before);
        }
        else if (clickedButton.id == 0 && !this.nameBox.getText().isEmpty() && !this.passwordBox.getText().isEmpty() && !this.ipBox.getText().isEmpty() && !this.commandBox.getText().isEmpty() && !this.portBox.getText().isEmpty()) {
            Throwable throwable = null;
            final Object var3_4 = null;
            try {
                Throwable t = null;
                try {
                    final RconClient client = RconClient.open(this.ipBox.getText(), Integer.valueOf(this.portBox.getText()), this.passwordBox.getText());
                    try {
                        client.sendCommand(this.commandBox.getText());
                    }
                    finally {
                        if (client != null) {
                            client.close();
                        }
                    }
                }
                finally {
                    if (t == null) {
                        final Throwable t2;
                        t = t2;
                    }
                    else {
                        final Throwable t2;
                        if (t != t2) {
                            t.addSuppressed(t2);
                        }
                    }
                }
            }
            catch (final Throwable throwable2) {
                throwable = throwable2;
            }
        }
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.nameBox.textboxKeyTyped(par1, par2);
        this.passwordBox.textboxKeyTyped(par1, par2);
        this.ipBox.textboxKeyTyped(par1, par2);
        this.commandBox.textboxKeyTyped(par1, par2);
        this.portBox.textboxKeyTyped(par1, par2);
        if (par2 == 28 || par2 == 156) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        super.mouseClicked(par1, par2, par3);
        this.nameBox.mouseClicked(par1, par2, par3);
        this.passwordBox.mouseClicked(par1, par2, par3);
        this.ipBox.mouseClicked(par1, par2, par3);
        this.commandBox.mouseClicked(par1, par2, par3);
        this.portBox.mouseClicked(par1, par2, par3);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawString(this.fontRendererObj, "Server IP", GuiRconConnection.width / 2 - 80, GuiRconConnection.height / 50 + 30, 10526880);
        Gui.drawString(this.fontRendererObj, "Port", GuiRconConnection.width / 2 + 45, GuiRconConnection.height / 50 + 30, 10526880);
        Gui.drawString(this.fontRendererObj, "Command", GuiRconConnection.width / 2 - 20, GuiRconConnection.height / 50 + 75, 10526880);
        Gui.drawString(this.fontRendererObj, "Username", GuiRconConnection.width / 2 - 80, GuiRconConnection.height / 50 + 120, 10526880);
        Gui.drawString(this.fontRendererObj, "Password", GuiRconConnection.width / 2 + 30, GuiRconConnection.height / 50 + 120, 10526880);
        this.nameBox.drawTextBox();
        this.passwordBox.drawTextBox();
        this.ipBox.drawTextBox();
        this.commandBox.drawTextBox();
        this.portBox.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
