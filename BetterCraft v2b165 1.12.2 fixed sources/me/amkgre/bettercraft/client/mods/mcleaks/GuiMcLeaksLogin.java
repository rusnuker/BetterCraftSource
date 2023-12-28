// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.mcleaks;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import me.amkgre.bettercraft.client.utils.WebUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import me.amkgre.bettercraft.client.utils.NameUtils;
import me.amkgre.bettercraft.client.mods.altmanager.GuiAltManager;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiMcLeaksLogin extends GuiScreen
{
    private GuiTextField tokenBox;
    private GuiScreen before;
    private String status2;
    public static String renderText;
    
    static {
        GuiMcLeaksLogin.renderText = "";
    }
    
    public GuiMcLeaksLogin(final GuiScreen before) {
        this.before = before;
    }
    
    @Override
    public void updateScreen() {
        this.tokenBox.updateCursorCounter();
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        GuiMcLeaksLogin.renderText = "";
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, GuiMcLeaksLogin.width / 2 - 100, GuiMcLeaksLogin.height / 4 + 72 + 12, "Set"));
        this.buttonList.add(new GuiButton(2, GuiMcLeaksLogin.width / 2 - 100, GuiMcLeaksLogin.height / 4 + 96 + 12, "Generate Token"));
        this.buttonList.add(new GuiButton(0, GuiMcLeaksLogin.width / 2 - 100, GuiMcLeaksLogin.height / 4 + 120 + 12, "Back"));
        (this.tokenBox = new GuiTextField(0, this.fontRendererObj, GuiMcLeaksLogin.width / 2 - 100, 60, 200, 20)).setMaxStringLength(48);
        this.tokenBox.setFocused(true);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiAltManager(this.before));
                break;
            }
            case 1: {
                GuiMcLeaksLogin.renderText = "§aSuccesful";
                final McLeaksAPI.RedeemedSession sessions_mcLeaksSession;
                final McLeaksAPI.RedeemedSession ses = McLeaksAPI.sessions_mcLeaksSession = (sessions_mcLeaksSession = McLeaksAPI.redeemSession(this.tokenBox.getText()));
                Minecraft.session = new Session(ses.name, NameUtils.getUUID(ses.name).toString(), ses.session, "mojang");
                break;
            }
            case 2: {
                WebUtils.openLink("http://mcleaks.net/get");
                break;
            }
        }
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) throws IOException {
        this.tokenBox.textboxKeyTyped(par1, par2);
        if (par2 == 28 || par2 == 156) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        super.mouseClicked(par1, par2, par3);
        this.tokenBox.mouseClicked(par1, par2, par3);
    }
    
    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        this.drawDefaultBackground();
        this.status2 = "§7Username: §d" + Minecraft.session.getUsername() + " §7(" + ((Minecraft.session.getSessionType() == Session.Type.LEGACY) ? "§cCracked" : "§6Premium") + "§7)";
        Gui.drawString(this.fontRendererObj, "Token", GuiMcLeaksLogin.width / 2 - 18, 47, 10526880);
        Gui.drawCenteredString(this.fontRendererObj, this.status2, GuiMcLeaksLogin.width / 2, 20 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        this.tokenBox.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
}
