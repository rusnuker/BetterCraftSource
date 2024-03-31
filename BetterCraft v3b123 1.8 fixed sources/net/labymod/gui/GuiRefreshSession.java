// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import java.util.Iterator;
import java.util.List;
import net.labymod.core.LabyModCore;
import net.labymod.utils.ModColor;
import net.labymod.main.lang.LanguageManager;
import java.io.IOException;
import net.labymod.account.LauncherProfile;
import net.labymod.main.LabyMod;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.labymod.utils.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiRefreshSession extends GuiScreen
{
    private GuiScreen parentScreen;
    private GuiButton refreshButton;
    private boolean displayError;
    private long cooldown;
    
    public GuiRefreshSession(final GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        this.cooldown = System.currentTimeMillis();
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, GuiRefreshSession.width / 2 - 155, GuiRefreshSession.height / 2 + 10, 125, 20, I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(this.refreshButton = new GuiButton(2, GuiRefreshSession.width / 2 - 25, GuiRefreshSession.height / 2 + 10, 180, 20, ""));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
        }
        if (button.id == 2) {
            this.cooldown = System.currentTimeMillis();
            final boolean validToken = Minecraft.getMinecraft().getSession().getToken() != null && Minecraft.getMinecraft().getSession().getToken().length() > 2;
            final Consumer<Boolean> setSessionCallback = new Consumer<Boolean>() {
                @Override
                public void accept(final Boolean accepted) {
                    if (accepted) {
                        GuiRefreshSession.this.mc.displayGuiScreen(new GuiConnecting(GuiRefreshSession.this.parentScreen, Minecraft.getMinecraft(), Minecraft.getMinecraft().getCurrentServerData()));
                    }
                    else {
                        GuiRefreshSession.access$2(GuiRefreshSession.this, true);
                    }
                }
            };
            final LauncherProfile launcherProfile = LabyMod.getInstance().getAccountManager().getAccount(LabyMod.getInstance().getPlayerUUID());
            if (launcherProfile != null && validToken) {
                launcherProfile.refresh(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean accepted) {
                        if (accepted) {
                            LabyMod.getInstance().getAccountManager().getAccountLoginHandler().setSession(launcherProfile.buildSession(), setSessionCallback);
                        }
                        else {
                            GuiRefreshSession.access$2(GuiRefreshSession.this, true);
                        }
                    }
                });
            }
            else {
                LabyMod.getInstance().getAccountManager().getAccountLoginHandler().setSession(launcherProfile.buildSession(), setSessionCallback);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        final List<String> list = LabyMod.getInstance().getDrawUtils().listFormattedStringToWidth(LanguageManager.translate("refresh_session"), GuiRefreshSession.width / 3, 4);
        int yS = 0;
        for (final String s : list) {
            LabyMod.getInstance().getDrawUtils().drawCenteredString(String.valueOf(ModColor.cl("c")) + s, GuiRefreshSession.width / 2, GuiRefreshSession.height / 2 - 40 + yS);
            yS += 10;
        }
        if (this.displayError) {
            final List<String> lines = LabyMod.getInstance().getDrawUtils().listFormattedStringToWidth(LabyMod.getInstance().getAccountManager().getLastErrorMessage(), GuiRefreshSession.width / 2);
            int y = GuiRefreshSession.height / 2 - 70;
            for (final String line : lines) {
                LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(String.valueOf(ModColor.cl("4")) + line, (float)(GuiRefreshSession.width / 2 - LabyModCore.getMinecraft().getFontRenderer().getStringWidth(line) / 2), (float)y, 16777215);
                y += 10;
            }
        }
        final int cooldownCount = (int)(this.cooldown + 5000L - System.currentTimeMillis()) / 1000;
        final String cooldownCountdown = (cooldownCount > 0) ? (" (" + cooldownCount + ")") : "";
        this.refreshButton.displayString = String.valueOf(LanguageManager.translate("refresh_session_refresh_try")) + cooldownCountdown;
        this.refreshButton.enabled = (cooldownCount <= 0);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    static /* synthetic */ void access$2(final GuiRefreshSession guiRefreshSession, final boolean displayError) {
        guiRefreshSession.displayError = displayError;
    }
}
