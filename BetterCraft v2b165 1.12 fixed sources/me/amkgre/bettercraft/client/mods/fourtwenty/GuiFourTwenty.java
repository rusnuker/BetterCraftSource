// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.fourtwenty;

import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.util.Session;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import me.amkgre.bettercraft.client.utils.LoginUtils;
import me.amkgre.bettercraft.client.utils.MiscUtils;
import me.amkgre.bettercraft.client.utils.IOUtils;
import java.net.URL;
import me.amkgre.bettercraft.client.mods.altmanager.GuiAltManager;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiFourTwenty extends GuiScreen
{
    private String status;
    private GuiTextField loginField;
    private GuiScreen before;
    public String FTGName;
    public String FTGPwd;
    
    public GuiFourTwenty(final GuiScreen before) {
        this.FTGName = null;
        this.FTGPwd = null;
        this.before = before;
    }
    
    @Override
    public void updateScreen() {
        this.loginField.updateCursorCounter();
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, GuiFourTwenty.width / 2 - 100, GuiFourTwenty.height / 2, "Login Account"));
        this.buttonList.add(new GuiButton(10001, GuiFourTwenty.width / 2 - 100, GuiFourTwenty.height / 2 + 25, "Login Alt"));
        this.buttonList.add(new GuiButton(1, GuiFourTwenty.width / 2 - 100, GuiFourTwenty.height / 2 + 50, "Back"));
        (this.loginField = new GuiTextField(2, this.fontRendererObj, GuiFourTwenty.width / 2 - 100, GuiFourTwenty.height / 4, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
        Keyboard.enableRepeatEvents(true);
        this.loginField.setEnableBackgroundDrawing(true);
        this.loginField.setFocused(true);
        this.status = "§cListening...";
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiAltManager(this.before));
        }
        if (button.id == 10001) {
            final URL url = new URL("https://420gen.eu/Kashed/login.php?email=" + this.FTGName + "&password=" + this.FTGPwd + "&alt=Minecraft");
            final String back = IOUtils.toString(url);
            final String[] split = back.split(":");
            if (split.length >= 2) {
                LoginUtils.loginAlt(split[0], MiscUtils.bindString(split, 1, split.length, ":"));
            }
            this.status = "§aLogged in §7(§4FourTwentyGenerator§7)";
        }
        if (button.id == 0) {
            final String login = this.loginField.getText();
            final String[] split2;
            if (!login.isEmpty() && login.contains(":") && (split2 = login.split(":")).length >= 2) {
                final String name = split2[0];
                final String pwd = MiscUtils.bindString(split2, 1, split2.length, ":");
                try {
                    final URL url2 = new URL("https://420gen.eu/Kashed/login.php?email=" + name + "&password=" + pwd);
                    final String back2 = IOUtils.toString(url2);
                    if (!back2.isEmpty() && back2.equalsIgnoreCase("Login-Daten sind korrekt.")) {
                        this.status = "§aLogged in to your FourTwentyGenerator Account.";
                        this.FTGName = name;
                        this.FTGPwd = pwd;
                        return;
                    }
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            this.status = "§4Can't login!";
        }
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) throws IOException {
        this.loginField.textboxKeyTyped(par1, par2);
        if (par2 == 28 || par2 == 156) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        super.mouseClicked(par1, par2, par3);
        this.loginField.mouseClicked(par1, par2, par3);
    }
    
    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        this.status = "§7Username: §d" + Minecraft.session.getUsername() + " §7(" + ((Minecraft.session.getSessionType() == Session.Type.LEGACY) ? "§cCracked" : "§6Premium") + "§7)";
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.fontRendererObj, this.status, GuiFourTwenty.width / 2, 20, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "420gen.eu", GuiFourTwenty.width / 2, 10, 10526880);
        Gui.drawCenteredString(this.fontRendererObj, "email:password", GuiFourTwenty.width / 2, GuiFourTwenty.height / 4 - 15, 10526880);
        this.loginField.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
}
