// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.altmanager.thealtening;

import java.io.IOException;
import java.net.URLConnection;
import java.net.URL;
import com.google.gson.GsonBuilder;
import me.nzxtercode.bettercraft.client.Config;
import com.google.gson.Gson;

public final class TheAltening
{
    private final String website = "http://api.thealtening.com/v1/";
    private static String apiKey;
    private final Gson gson;
    
    static {
        TheAltening.apiKey = Config.getInstance().getConfig("Alterning").get("apikey").getAsString();
    }
    
    public TheAltening(final String apiKey) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        TheAltening.apiKey = apiKey;
    }
    
    public User getUser() throws IOException {
        this.getClass();
        final URLConnection licenseEndpoint = new URL(this.attach("http://api.thealtening.com/v1/license")).openConnection();
        final String userInfo = new String(Utilities.getInstance().readAllBytes(licenseEndpoint.getInputStream()));
        return this.gson.fromJson(userInfo, User.class);
    }
    
    public AlteningAlt generateAccount(final User user) throws IOException {
        this.getClass();
        final URLConnection generateEndpoint = new URL(this.attach("http://api.thealtening.com/v1/generate")).openConnection();
        final String accountInfo = new String(Utilities.getInstance().readAllBytes(generateEndpoint.getInputStream()));
        if (user.isPremium()) {
            return this.gson.fromJson(accountInfo, AlteningAlt.class);
        }
        return null;
    }
    
    public boolean favoriteAccount(final AlteningAlt account) throws IOException {
        this.getClass();
        final URLConnection favoriteAccount = new URL(this.attachAccount("http://api.thealtening.com/v1/favorite", account)).openConnection();
        final String info = new String(Utilities.getInstance().readAllBytes(favoriteAccount.getInputStream()));
        return info.isEmpty();
    }
    
    public boolean privateAccount(final AlteningAlt account) throws IOException {
        this.getClass();
        final URLConnection privateAccount = new URL(this.attachAccount("http://api.thealtening.com/v1/private", account)).openConnection();
        final String info = new String(Utilities.getInstance().readAllBytes(privateAccount.getInputStream()));
        return info.isEmpty();
    }
    
    private String attach(final String website) {
        return String.valueOf(website) + "?token=" + TheAltening.apiKey;
    }
    
    private String attachAccount(final String website, final AlteningAlt account) {
        return String.valueOf(website) + "?token=" + TheAltening.apiKey + "&acctoken=" + account.getToken();
    }
}
