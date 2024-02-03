// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.altmanager.impl;

import me.nzxtercode.bettercraft.client.misc.altmanager.GuiAltManager;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import me.nzxtercode.bettercraft.client.misc.altmanager.thealtening.AltService;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.Minecraft;
import me.nzxtercode.bettercraft.client.misc.altmanager.GuiAlteningLogin;

public class AccountLoginThread extends Thread
{
    private String email;
    private String password;
    public static boolean unknownBoolean1;
    private String status;
    
    public AccountLoginThread(final String email, final String password) {
        this.status = "Waiting for login...";
        this.email = email;
        this.password = password;
    }
    
    @Override
    public void run() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiAlteningLogin || GuiDisconnected.useTheAltening) {
            AltService.getInstance().switchToTheAltening();
            AccountLoginThread.unknownBoolean1 = false;
            GuiDisconnected.useTheAltening = false;
        }
        else if (AccountLoginThread.unknownBoolean1) {
            try {
                AltService.getInstance().switchService(AltService.EnumAltService.MOJANG);
            }
            catch (final NoSuchFieldException e) {
                System.out.println("Couldnt switch to modank altservice");
            }
            catch (final IllegalAccessException e2) {
                System.out.println("Couldnt switch to modank altservice -2");
            }
        }
        if (this.password == null || this.password.isEmpty()) {
            Minecraft.getMinecraft();
            Minecraft.session = new Session(this.email, "", "", "mojang");
            this.status = "Logged in as " + EnumChatFormatting.RED.toString() + this.email;
            return;
        }
        AccountLoginThread.unknownBoolean1 = true;
        this.status = "Logging in...";
        try {
            final MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            MicrosoftAuthResult loginWithCredentials;
            if (this.email.contains("@")) {
                loginWithCredentials = authenticator.loginWithCredentials(this.email, this.password);
            }
            else {
                final MinecraftProfile profile;
                loginWithCredentials = new MicrosoftAuthResult(profile, "-", "-");
                profile = new MinecraftProfile("-", this.email, new MinecraftProfile.MinecraftSkin[0]);
            }
            final MicrosoftAuthResult result = loginWithCredentials;
            final Session session = new Session(result.getProfile().getName(), result.getProfile().getId().toString(), result.getAccessToken(), this.email.contains("@") ? "mojang" : "legacy");
            Minecraft.getMinecraft();
            Minecraft.session = session;
            Account account = AccountManager.getInstance().getAccountByEmail(this.email);
            account = ((account == null) ? new Account(this.email, this.password, session.getUsername()) : account);
            account.setName(session.getUsername());
            if (!(Minecraft.getMinecraft().currentScreen instanceof GuiAlteningLogin) && !(Minecraft.getMinecraft().currentScreen instanceof GuiDisconnected)) {
                AccountManager.getInstance().setLastAlt(account);
            }
            AccountManager.getInstance().save();
            GuiAltManager.getInstance().currentAccount = account;
            if (AccountLoginThread.unknownBoolean1) {
                this.status = String.format("Logged in as %s", EnumChatFormatting.RED + account.getName());
            }
        }
        catch (final Exception exception) {
            this.status = exception.getMessage();
        }
    }
    
    public String getStatus() {
        return this.status;
    }
}
