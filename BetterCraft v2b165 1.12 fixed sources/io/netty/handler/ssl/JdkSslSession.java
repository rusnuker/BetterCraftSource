// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.security.Principal;
import javax.security.cert.X509Certificate;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.security.cert.Certificate;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;

final class JdkSslSession implements SSLSession, ApplicationProtocolAccessor
{
    private final SSLEngine engine;
    private volatile String applicationProtocol;
    
    JdkSslSession(final SSLEngine engine) {
        this.engine = engine;
    }
    
    private SSLSession unwrap() {
        return this.engine.getSession();
    }
    
    @Override
    public String getProtocol() {
        return this.unwrap().getProtocol();
    }
    
    @Override
    public String getApplicationProtocol() {
        return this.applicationProtocol;
    }
    
    void setApplicationProtocol(final String applicationProtocol) {
        this.applicationProtocol = applicationProtocol;
    }
    
    @Override
    public byte[] getId() {
        return this.unwrap().getId();
    }
    
    @Override
    public SSLSessionContext getSessionContext() {
        return this.unwrap().getSessionContext();
    }
    
    @Override
    public long getCreationTime() {
        return this.unwrap().getCreationTime();
    }
    
    @Override
    public long getLastAccessedTime() {
        return this.unwrap().getLastAccessedTime();
    }
    
    @Override
    public void invalidate() {
        this.unwrap().invalidate();
    }
    
    @Override
    public boolean isValid() {
        return this.unwrap().isValid();
    }
    
    @Override
    public void putValue(final String s, final Object o) {
        this.unwrap().putValue(s, o);
    }
    
    @Override
    public Object getValue(final String s) {
        return this.unwrap().getValue(s);
    }
    
    @Override
    public void removeValue(final String s) {
        this.unwrap().removeValue(s);
    }
    
    @Override
    public String[] getValueNames() {
        return this.unwrap().getValueNames();
    }
    
    @Override
    public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
        return this.unwrap().getPeerCertificates();
    }
    
    @Override
    public Certificate[] getLocalCertificates() {
        return this.unwrap().getLocalCertificates();
    }
    
    @Override
    public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
        return this.unwrap().getPeerCertificateChain();
    }
    
    @Override
    public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        return this.unwrap().getPeerPrincipal();
    }
    
    @Override
    public Principal getLocalPrincipal() {
        return this.unwrap().getLocalPrincipal();
    }
    
    @Override
    public String getCipherSuite() {
        return this.unwrap().getCipherSuite();
    }
    
    @Override
    public String getPeerHost() {
        return this.unwrap().getPeerHost();
    }
    
    @Override
    public int getPeerPort() {
        return this.unwrap().getPeerPort();
    }
    
    @Override
    public int getPacketBufferSize() {
        return this.unwrap().getPacketBufferSize();
    }
    
    @Override
    public int getApplicationBufferSize() {
        return this.unwrap().getApplicationBufferSize();
    }
}
