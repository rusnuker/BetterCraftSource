// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.thealtening;

import net.minecraft.client.gui.Gui;
import java.io.IOException;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.amkgre.bettercraft.client.utils.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.utils.WebUtils;
import me.amkgre.bettercraft.client.mods.altmanager.GuiAltManager;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiButton;
import me.amkgre.bettercraft.client.utils.ClipboardUtils;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiAlterning extends GuiScreen
{
    private String status;
    private GuiTextField loginField;
    private String status2;
    private GuiScreen before;
    public static String apiKey;
    public String startSession;
    public String startName;
    public String startUUID;
    public String startAccountType;
    public String FTGName;
    public String FTGPwd;
    public static String renderText;
    
    static {
        GuiAlterning.renderText = "";
    }
    
    public GuiAlterning(final GuiScreen before) {
        this.startSession = null;
        this.startName = null;
        this.startUUID = null;
        this.startAccountType = null;
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
        TheAlteningAPIKeyCracker.init();
        final String clipboard = ClipboardUtils.getClipboard();
        if (clipboard.startsWith("api-")) {
            GuiAlterning.apiKey = clipboard;
        }
        this.buttonList.add(new GuiButton(0, GuiAlterning.width / 2 - 100, GuiAlterning.height / 2 - 5, "Login"));
        this.buttonList.add(new GuiButton(2, GuiAlterning.width / 2 - 100, GuiAlterning.height / 2 + 20, "Generate (Website)"));
        this.buttonList.add(new GuiButton(1, GuiAlterning.width / 2 - 100, GuiAlterning.height / 2 + 70, "Back"));
        final GuiButton tmp = new GuiButton(10000, GuiAlterning.width / 2 - 100, GuiAlterning.height / 2 + 45, "Generate (TheAltening)");
        if (GuiAlterning.apiKey == null) {
            tmp.enabled = false;
        }
        this.buttonList.add(tmp);
        (this.loginField = new GuiTextField(2, this.fontRendererObj, GuiAlterning.width / 2 - 100, GuiAlterning.height / 4, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
        Keyboard.enableRepeatEvents(true);
        this.loginField.setEnableBackgroundDrawing(true);
        this.loginField.setFocused(true);
        this.status = "§cListening...";
        this.status2 = "";
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
        if (button.id == 2) {
            WebUtils.openLink("https://fastalts.net/");
        }
        if (button.id == 10000) {
            LoginAlterning.loginTheAlteningWithAPIKey(GuiAlterning.apiKey);
        }
        if (button.id == 0) {
            if (this.loginField.getText() != null && !this.loginField.getText().isEmpty()) {
                if (!this.loginField.getText().contains(" ")) {
                    final String text = this.loginField.getText();
                    final String[] loginDetails = text.split(":");
                    if (loginDetails.length == 1) {
                        if (loginDetails[0].contains("@alt.com")) {
                            System.out.println(loginDetails[0]);
                            this.status = (LoginAlterning.loginTheAltening(loginDetails[0]) ? "§aLogged in §7(§4TheAltening§7)" : "§4Can't login!");
                        }
                        else if (this.loginField.getText().startsWith("api-")) {
                            final String back = LoginAlterning.loginTheAlteningWithAPIKey(this.loginField.getText());
                            this.status = (back.equalsIgnoreCase("Success") ? "§aLogged in §7(§4TheAltening§7)" : ("§4" + back));
                        }
                        else {
                            try {
                                Client.getInstance().altService.switchService(AltService.EnumAltService.MOJANG);
                            }
                            catch (final IllegalAccessException | NoSuchFieldException noSuchFieldException) {
                                noSuchFieldException.printStackTrace();
                            }
                            Minecraft.session = new Session(loginDetails[0], "-", "0", "Legacy");
                            this.status = "§aLogged in §7(§cCracked§7)";
                        }
                    }
                    else if (loginDetails.length >= 2) {
                        if (loginDetails[0].contains("@")) {
                            final AltService.EnumAltService as = Client.getInstance().altService.getCurrentService();
                            try {
                                Client.getInstance().altService.switchService(AltService.EnumAltService.MOJANG);
                            }
                            catch (final IllegalAccessException | NoSuchFieldException noSuchFieldException2) {
                                noSuchFieldException2.printStackTrace();
                            }
                            final String pwd = MiscUtils.bindString(loginDetails, 1, loginDetails.length, ":");
                            final YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
                            authentication.setUsername(loginDetails[0]);
                            authentication.setPassword(pwd);
                            try {
                                authentication.logIn();
                                Minecraft.session = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "mojang");
                                this.status = "§aLogged in §7(§6Premium§7)";
                                GuiAlterning.LOGGER.info("Logged in with token: " + authentication.getAuthenticatedToken());
                            }
                            catch (final AuthenticationException e) {
                                try {
                                    Client.getInstance().altService.switchService(as);
                                }
                                catch (final IllegalAccessException | NoSuchFieldException noSuchFieldException3) {
                                    noSuchFieldException3.printStackTrace();
                                }
                                e.printStackTrace();
                                this.status = "§4Can't login!";
                            }
                        }
                        else {
                            try {
                                Client.getInstance().altService.switchService(AltService.EnumAltService.MOJANG);
                            }
                            catch (final IllegalAccessException | NoSuchFieldException noSuchFieldException) {
                                noSuchFieldException.printStackTrace();
                            }
                            final String session = MiscUtils.bindString(loginDetails, 1, loginDetails.length, ":");
                            Minecraft.session = new Session(loginDetails[0], MiscUtils.getUUID(loginDetails[0]), session, "mojang");
                            this.status = "§aLogged in §7(§bSession§7)";
                        }
                    }
                    else {
                        this.status = "§4Can't use input";
                    }
                }
                else {
                    this.status = "§4Spaces aren't allowed";
                }
            }
            else {
                this.status = "§4Text box may not be empty";
            }
        }
        this.status2 = "§aUsername: §e" + Minecraft.session.getUsername() + " §7(" + ((Minecraft.session.getSessionType() == Session.Type.LEGACY) ? "§cCracked" : "§6Premium") + "§7)";
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
        this.status2 = "§7Username: §d" + Minecraft.session.getUsername() + " §7(" + ((Minecraft.session.getSessionType() == Session.Type.LEGACY) ? "§cCracked" : "§6Premium") + "§7)";
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.fontRendererObj, this.status, GuiAlterning.width / 2, 20, 10526880);
        Gui.drawCenteredString(this.fontRendererObj, this.status2, GuiAlterning.width / 2, 20 + this.fontRendererObj.FONT_HEIGHT, 10526880);
        Gui.drawCenteredString(this.fontRendererObj, "email@alt.com", GuiAlterning.width / 2, GuiAlterning.height / 4 - 15, 10526880);
        this.loginField.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
}
