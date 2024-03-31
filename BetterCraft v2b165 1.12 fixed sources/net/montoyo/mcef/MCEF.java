// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef;

import java.security.cert.Certificate;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.client.ClientProxy;
import javax.net.ssl.SSLSocketFactory;

public class MCEF
{
    public static final String VERSION = "1.11";
    public static boolean ENABLE_EXAMPLE;
    public static boolean SKIP_UPDATES;
    public static boolean WARN_UPDATES;
    public static boolean USE_FORGE_SPLASH;
    public static String FORCE_MIRROR;
    public static String HOME_PAGE;
    public static String[] CEF_ARGS;
    public static boolean CHECK_VRAM_LEAK;
    public static SSLSocketFactory SSL_SOCKET_FACTORY;
    public static boolean SHUTDOWN_JCEF;
    public static boolean SECURE_MIRRORS_ONLY;
    public static ClientProxy PROXY_CLIENT;
    
    static {
        MCEF.FORCE_MIRROR = null;
        MCEF.CEF_ARGS = new String[0];
        MCEF.PROXY_CLIENT = new ClientProxy();
    }
    
    public static void init() {
        onPreInit();
        onInit();
    }
    
    private static void onPreInit() {
        MCEF.SKIP_UPDATES = false;
        MCEF.WARN_UPDATES = true;
        MCEF.USE_FORGE_SPLASH = true;
        MCEF.CEF_ARGS = new String[] { "--disable-gpu" };
        MCEF.SHUTDOWN_JCEF = false;
        MCEF.SECURE_MIRRORS_ONLY = true;
        MCEF.ENABLE_EXAMPLE = true;
        MCEF.HOME_PAGE = "mod://mcef/home.html";
        MCEF.CHECK_VRAM_LEAK = false;
        importLetsEncryptCertificate();
        MCEF.PROXY_CLIENT.onPreInit();
    }
    
    private static void onInit() {
        Log.info("Now initializing MCEF v%s...", "1.11");
        MCEF.PROXY_CLIENT.onInit();
    }
    
    public static void onMinecraftShutdown() {
        Log.info("Minecraft shutdown hook called!", new Object[0]);
        MCEF.PROXY_CLIENT.onShutdown();
    }
    
    private static void importLetsEncryptCertificate() {
        try {
            final CertificateFactory cf = CertificateFactory.getInstance("X.509");
            final Certificate cert = cf.generateCertificate(MCEF.class.getResourceAsStream("/assets/mcef/r3.crt"));
            final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, null);
            ks.setCertificateEntry("r3", cert);
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(ks);
            final SSLContext sslCtx = SSLContext.getInstance("TLS");
            sslCtx.init(null, tmf.getTrustManagers(), new SecureRandom());
            MCEF.SSL_SOCKET_FACTORY = sslCtx.getSocketFactory();
            Log.info("Successfully loaded Let's Encrypt certificate", new Object[0]);
        }
        catch (final Throwable t) {
            Log.error("Could not import Let's Encrypt certificate!! HTTPS downloads WILL fail...", new Object[0]);
            t.printStackTrace();
        }
    }
}
