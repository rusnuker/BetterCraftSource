// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import org.apache.commons.lang3.StringUtils;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;

public class ClipboardUtils
{
    private static String getClipboardString() {
        try {
            final Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String)transferable.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch (final Exception var1) {
            var1.printStackTrace();
        }
        return "";
    }
    
    public static String getClipboard() {
        String toReturn = null;
        try {
            toReturn = getClipboardString();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
    
    private static void setClipboardString(final String text) {
        if (!StringUtils.isEmpty(text)) {
            try {
                final StringSelection stringselection = new StringSelection(text);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
            }
            catch (final Exception var2) {
                var2.printStackTrace();
            }
        }
    }
    
    public static void setClipboard(final String text) {
        try {
            setClipboardString(text);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String login(final String email, final String password) {
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(email);
        auth.setPassword(password);
        try {
            auth.logIn();
            Minecraft.session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
            return "§a§lLogin successful: §6" + Minecraft.session.getUsername();
        }
        catch (final AuthenticationUnavailableException e) {
            return "§4§lCannot contact authentication server!";
        }
        catch (final AuthenticationException e2) {
            e2.printStackTrace();
            if (e2.getMessage().contains("Invalid username or password.") || e2.getMessage().toLowerCase().contains("account migrated")) {
                return "§4§lWrong password!";
            }
            return "§4§lCannot contact authentication server!";
        }
        catch (final NullPointerException e3) {
            return "§4§lWrong password!";
        }
    }
}
