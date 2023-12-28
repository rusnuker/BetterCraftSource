// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

public final class Mirror
{
    public static final int FLAG_SECURE = 1;
    public static final int FLAG_LETSENCRYPT = 2;
    public static final int FLAG_FORCED = 4;
    private final String name;
    private final String url;
    private final int flags;
    
    public Mirror(final String name, final String url, final int flags) {
        this.name = name;
        this.url = url;
        this.flags = flags;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getURL() {
        return this.url;
    }
    
    public int getFlags() {
        return this.flags;
    }
    
    public boolean isSecure() {
        return (this.flags & 0x1) != 0x0;
    }
    
    public boolean usesLetsEncryptCertificate() {
        return (this.flags & 0x2) != 0x0;
    }
    
    public boolean isForced() {
        return (this.flags & 0x4) != 0x0;
    }
    
    public String getInformationString() {
        return this.isForced() ? ("Mirror location forced by user to: " + this.url) : ("Selected mirror: " + this.name);
    }
    
    public HttpURLConnection getResource(final String name) throws MalformedURLException, IOException {
        final HttpURLConnection ret = (HttpURLConnection)new URL(String.valueOf(this.url) + '/' + name).openConnection();
        ret.setConnectTimeout(30000);
        ret.setReadTimeout(15000);
        ret.setRequestProperty("User-Agent", "MCEF");
        return ret;
    }
}
