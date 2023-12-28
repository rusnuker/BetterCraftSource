// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.thealtening;

import java.io.IOException;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import org.apache.commons.io.IOUtils;
import java.net.URL;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.util.Session;
import net.minecraft.client.Minecraft;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.amkgre.bettercraft.client.Client;

public class LoginAlterning
{
    public static boolean loginTheAltening(final String input) {
        try {
            Client.getInstance().altService.switchService(AltService.EnumAltService.THEALTENING);
            final YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
            authentication.setUsername(input);
            authentication.setPassword("Invalid");
            authentication.logIn();
            Minecraft.getMinecraft();
            Minecraft.session = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "mojang");
        }
        catch (final AuthenticationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            try {
                Client.getInstance().altService.switchService(AltService.EnumAltService.MOJANG);
            }
            catch (final IllegalAccessException | NoSuchFieldException noSuchFieldException) {
                noSuchFieldException.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    public static String loginTheAlteningWithAPIKey(final String api) {
        final String url = "http://api.thealtening.com/v2/generate?key=" + api;
        try {
            final String UUIDJson = IOUtils.toString(new URL(url));
            if (UUIDJson.isEmpty()) {
                return "Not available!";
            }
            final JSONObject UUIDObject = (JSONObject)JSONValue.parse(UUIDJson);
            if (UUIDObject.containsKey("error")) {
                return UUIDObject.get("error");
            }
            final String token = UUIDObject.get("token");
            loginTheAltening(token);
            GuiAlterning.apiKey = api;
            return "Success";
        }
        catch (final IOException iOException) {
            return "ERROR!";
        }
    }
}
