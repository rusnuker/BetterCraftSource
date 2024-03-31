// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl.util;

import javax.net.ssl.SSLEngine;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import io.netty.util.internal.ObjectUtil;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

final class X509TrustManagerWrapper extends X509ExtendedTrustManager
{
    private final X509TrustManager delegate;
    
    X509TrustManagerWrapper(final X509TrustManager delegate) {
        this.delegate = ObjectUtil.checkNotNull(delegate, "delegate");
    }
    
    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String s) throws CertificateException {
        this.delegate.checkClientTrusted(chain, s);
    }
    
    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String s, final Socket socket) throws CertificateException {
        this.delegate.checkClientTrusted(chain, s);
    }
    
    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String s, final SSLEngine sslEngine) throws CertificateException {
        this.delegate.checkClientTrusted(chain, s);
    }
    
    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String s) throws CertificateException {
        this.delegate.checkServerTrusted(chain, s);
    }
    
    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String s, final Socket socket) throws CertificateException {
        this.delegate.checkServerTrusted(chain, s);
    }
    
    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String s, final SSLEngine sslEngine) throws CertificateException {
        this.delegate.checkServerTrusted(chain, s);
    }
    
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.delegate.getAcceptedIssuers();
    }
}
