// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.altmanager;

import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.UserAuthentication;
import net.minecraft.util.Session;
import net.minecraft.client.Minecraft;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import java.net.Proxy;

public class Login
{
    public static boolean login(final String email, final String password) throws AuthenticationException {
        login(email, password, Proxy.NO_PROXY);
        AltManager.loggedInName = null;
        return true;
    }
    
    public static void login(final String email, final String password, final Proxy proxy) throws AuthenticationException {
        final YggdrasilAuthenticationService authService = new YggdrasilAuthenticationService(proxy, "");
        final UserAuthentication auth = authService.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(email);
        auth.setPassword(password);
        auth.logIn();
        Minecraft.getMinecraft();
        Minecraft.session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
    }
    
    public static void changeName(String newName) {
        if (newName.equals("MHF_TikTok")) {
            newName = "succ";
        }
        Minecraft.getMinecraft();
        Minecraft.session = new Session(newName, "", "", "mojang");
        AltManager.loggedInName = null;
    }
    
    public static String loginclip(final String email, final String password) {
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
