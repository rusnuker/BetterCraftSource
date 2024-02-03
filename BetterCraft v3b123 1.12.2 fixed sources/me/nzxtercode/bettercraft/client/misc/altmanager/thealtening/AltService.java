// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.altmanager.thealtening;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class AltService
{
    private static final AltService INSTANCE;
    private ReflectionUtility userAuthentication;
    private ReflectionUtility minecraftSession;
    private EnumAltService currentService;
    
    static {
        INSTANCE = new AltService();
    }
    
    public AltService() {
        this.userAuthentication = new ReflectionUtility("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");
        this.minecraftSession = new ReflectionUtility("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService");
    }
    
    public static AltService getInstance() {
        return AltService.INSTANCE;
    }
    
    public void switchService(final EnumAltService enumAltService) throws NoSuchFieldException, IllegalAccessException {
        if (this.currentService == enumAltService) {
            return;
        }
        this.reflectionFields(enumAltService.hostname);
        this.currentService = enumAltService;
    }
    
    private void reflectionFields(final String authServer) throws NoSuchFieldException, IllegalAccessException {
        final HashMap<String, URL> userAuthenticationModifies = new HashMap<String, URL>();
        final String useSecureStart = authServer.contains("thealtening") ? "http" : "https";
        userAuthenticationModifies.put("ROUTE_AUTHENTICATE", this.constantURL(String.valueOf(useSecureStart) + "://authserver." + authServer + ".com/authenticate"));
        userAuthenticationModifies.put("ROUTE_INVALIDATE", this.constantURL(String.valueOf(useSecureStart) + "://authserver" + authServer + "com/invalidate"));
        userAuthenticationModifies.put("ROUTE_REFRESH", this.constantURL(String.valueOf(useSecureStart) + "://authserver." + authServer + ".com/refresh"));
        userAuthenticationModifies.put("ROUTE_VALIDATE", this.constantURL(String.valueOf(useSecureStart) + "://authserver." + authServer + ".com/validate"));
        userAuthenticationModifies.put("ROUTE_SIGNOUT", this.constantURL(String.valueOf(useSecureStart) + "://authserver." + authServer + ".com/signout"));
        userAuthenticationModifies.forEach((key, value) -> {
            try {
                this.userAuthentication.setStaticField(key, value);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            return;
        });
        this.userAuthentication.setStaticField("BASE_URL", String.valueOf(useSecureStart) + "://authserver." + authServer + ".com/");
        this.minecraftSession.setStaticField("BASE_URL", String.valueOf(useSecureStart) + "://sessionserver." + authServer + ".com/session/minecraft/");
        this.minecraftSession.setStaticField("JOIN_URL", this.constantURL(String.valueOf(useSecureStart) + "://sessionserver." + authServer + ".com/session/minecraft/join"));
        this.minecraftSession.setStaticField("CHECK_URL", this.constantURL(String.valueOf(useSecureStart) + "://sessionserver." + authServer + ".com/session/minecraft/hasJoined"));
        this.minecraftSession.setStaticField("WHITELISTED_DOMAINS", new String[] { ".minecraft.net", ".mojang.com", ".thealtening.com" });
    }
    
    private URL constantURL(final String url) {
        try {
            return new URL(url);
        }
        catch (final MalformedURLException ex) {
            throw new Error("Couldn't create constant for " + url, ex);
        }
    }
    
    public EnumAltService getCurrentService() {
        if (this.currentService == null) {
            this.currentService = EnumAltService.MOJANG;
        }
        return this.currentService;
    }
    
    public void switchToMojang() {
        try {
            this.switchService(EnumAltService.MOJANG);
        }
        catch (final NoSuchFieldException e) {
            System.out.println("Couldn't switch to modank altservice");
        }
        catch (final IllegalAccessException e2) {
            System.out.println("Couldn't switch to modank altservice -2");
        }
    }
    
    public void switchToTheAltening() {
        try {
            this.switchService(EnumAltService.THEALTENING);
        }
        catch (final NoSuchFieldException e) {
            System.out.println("Couldn't switch to altening altservice");
        }
        catch (final IllegalAccessException e2) {
            System.out.println("Couldn't switch to altening altservice -2");
        }
    }
    
    public enum EnumAltService
    {
        MOJANG("MOJANG", 0, "mojang"), 
        THEALTENING("THEALTENING", 1, "thealtening");
        
        String hostname;
        
        private EnumAltService(final String s, final int n, final String hostname) {
            this.hostname = hostname;
        }
    }
}
