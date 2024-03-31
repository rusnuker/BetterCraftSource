// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLEngineResult;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLEngine;

class JdkSslEngine extends SSLEngine
{
    private final SSLEngine engine;
    private final JdkSslSession session;
    
    JdkSslEngine(final SSLEngine engine) {
        this.engine = engine;
        this.session = new JdkSslSession(engine);
    }
    
    @Override
    public JdkSslSession getSession() {
        return this.session;
    }
    
    public SSLEngine getWrappedEngine() {
        return this.engine;
    }
    
    @Override
    public void closeInbound() throws SSLException {
        this.engine.closeInbound();
    }
    
    @Override
    public void closeOutbound() {
        this.engine.closeOutbound();
    }
    
    @Override
    public String getPeerHost() {
        return this.engine.getPeerHost();
    }
    
    @Override
    public int getPeerPort() {
        return this.engine.getPeerPort();
    }
    
    @Override
    public SSLEngineResult wrap(final ByteBuffer byteBuffer, final ByteBuffer byteBuffer2) throws SSLException {
        return this.engine.wrap(byteBuffer, byteBuffer2);
    }
    
    @Override
    public SSLEngineResult wrap(final ByteBuffer[] byteBuffers, final ByteBuffer byteBuffer) throws SSLException {
        return this.engine.wrap(byteBuffers, byteBuffer);
    }
    
    @Override
    public SSLEngineResult wrap(final ByteBuffer[] byteBuffers, final int i, final int i2, final ByteBuffer byteBuffer) throws SSLException {
        return this.engine.wrap(byteBuffers, i, i2, byteBuffer);
    }
    
    @Override
    public SSLEngineResult unwrap(final ByteBuffer byteBuffer, final ByteBuffer byteBuffer2) throws SSLException {
        return this.engine.unwrap(byteBuffer, byteBuffer2);
    }
    
    @Override
    public SSLEngineResult unwrap(final ByteBuffer byteBuffer, final ByteBuffer[] byteBuffers) throws SSLException {
        return this.engine.unwrap(byteBuffer, byteBuffers);
    }
    
    @Override
    public SSLEngineResult unwrap(final ByteBuffer byteBuffer, final ByteBuffer[] byteBuffers, final int i, final int i2) throws SSLException {
        return this.engine.unwrap(byteBuffer, byteBuffers, i, i2);
    }
    
    @Override
    public Runnable getDelegatedTask() {
        return this.engine.getDelegatedTask();
    }
    
    @Override
    public boolean isInboundDone() {
        return this.engine.isInboundDone();
    }
    
    @Override
    public boolean isOutboundDone() {
        return this.engine.isOutboundDone();
    }
    
    @Override
    public String[] getSupportedCipherSuites() {
        return this.engine.getSupportedCipherSuites();
    }
    
    @Override
    public String[] getEnabledCipherSuites() {
        return this.engine.getEnabledCipherSuites();
    }
    
    @Override
    public void setEnabledCipherSuites(final String[] strings) {
        this.engine.setEnabledCipherSuites(strings);
    }
    
    @Override
    public String[] getSupportedProtocols() {
        return this.engine.getSupportedProtocols();
    }
    
    @Override
    public String[] getEnabledProtocols() {
        return this.engine.getEnabledProtocols();
    }
    
    @Override
    public void setEnabledProtocols(final String[] strings) {
        this.engine.setEnabledProtocols(strings);
    }
    
    @Override
    public SSLSession getHandshakeSession() {
        return this.engine.getHandshakeSession();
    }
    
    @Override
    public void beginHandshake() throws SSLException {
        this.engine.beginHandshake();
    }
    
    @Override
    public SSLEngineResult.HandshakeStatus getHandshakeStatus() {
        return this.engine.getHandshakeStatus();
    }
    
    @Override
    public void setUseClientMode(final boolean b) {
        this.engine.setUseClientMode(b);
    }
    
    @Override
    public boolean getUseClientMode() {
        return this.engine.getUseClientMode();
    }
    
    @Override
    public void setNeedClientAuth(final boolean b) {
        this.engine.setNeedClientAuth(b);
    }
    
    @Override
    public boolean getNeedClientAuth() {
        return this.engine.getNeedClientAuth();
    }
    
    @Override
    public void setWantClientAuth(final boolean b) {
        this.engine.setWantClientAuth(b);
    }
    
    @Override
    public boolean getWantClientAuth() {
        return this.engine.getWantClientAuth();
    }
    
    @Override
    public void setEnableSessionCreation(final boolean b) {
        this.engine.setEnableSessionCreation(b);
    }
    
    @Override
    public boolean getEnableSessionCreation() {
        return this.engine.getEnableSessionCreation();
    }
    
    @Override
    public SSLParameters getSSLParameters() {
        return this.engine.getSSLParameters();
    }
    
    @Override
    public void setSSLParameters(final SSLParameters sslParameters) {
        this.engine.setSSLParameters(sslParameters);
    }
}
