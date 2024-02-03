// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.altmanager.thealtening;

import javax.net.ssl.SSLSession;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;

public class SSLVerification
{
    private boolean verified;
    
    public SSLVerification() {
        this.verified = false;
    }
    
    public void verify() {
        if (!this.verified) {
            this.bypassSSL();
            this.whitelistTheAltening();
            this.verified = true;
        }
    }
    
    private void bypassSSL() {
        final TrustManager[] trustAllCerts = { new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                
                @Override
                public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
                }
                
                @Override
                public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
                }
            } };
        try {
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (final Exception ex) {}
    }
    
    private void whitelistTheAltening() {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> hostname.equals("authserver.thealtening.com") || hostname.equals("sessionserver.thealtening.com"));
    }
}
