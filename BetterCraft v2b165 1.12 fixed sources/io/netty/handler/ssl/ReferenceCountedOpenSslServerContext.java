// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.X509KeyManager;
import io.netty.internal.tcnative.CertificateVerifier;
import javax.net.ssl.X509ExtendedTrustManager;
import java.security.KeyStore;
import javax.net.ssl.X509ExtendedKeyManager;
import io.netty.util.internal.ObjectUtil;
import io.netty.internal.tcnative.SSLContext;
import java.security.cert.Certificate;
import javax.net.ssl.SSLException;
import javax.net.ssl.KeyManagerFactory;
import java.security.PrivateKey;
import javax.net.ssl.TrustManagerFactory;
import java.security.cert.X509Certificate;

public final class ReferenceCountedOpenSslServerContext extends ReferenceCountedOpenSslContext
{
    private static final byte[] ID;
    private final OpenSslServerSessionContext sessionContext;
    private final OpenSslKeyMaterialManager keyMaterialManager;
    
    ReferenceCountedOpenSslServerContext(final X509Certificate[] trustCertCollection, final TrustManagerFactory trustManagerFactory, final X509Certificate[] keyCertChain, final PrivateKey key, final String keyPassword, final KeyManagerFactory keyManagerFactory, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apn, final long sessionCacheSize, final long sessionTimeout, final ClientAuth clientAuth, final String[] protocols, final boolean startTls) throws SSLException {
        this(trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, ReferenceCountedOpenSslContext.toNegotiator(apn), sessionCacheSize, sessionTimeout, clientAuth, protocols, startTls);
    }
    
    private ReferenceCountedOpenSslServerContext(final X509Certificate[] trustCertCollection, final TrustManagerFactory trustManagerFactory, final X509Certificate[] keyCertChain, final PrivateKey key, final String keyPassword, final KeyManagerFactory keyManagerFactory, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final OpenSslApplicationProtocolNegotiator apn, final long sessionCacheSize, final long sessionTimeout, final ClientAuth clientAuth, final String[] protocols, final boolean startTls) throws SSLException {
        super(ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, 1, keyCertChain, clientAuth, protocols, startTls, true);
        boolean success = false;
        try {
            final ServerContext context = newSessionContext(this, this.ctx, this.engineMap, trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory);
            this.sessionContext = context.sessionContext;
            this.keyMaterialManager = context.keyMaterialManager;
            success = true;
        }
        finally {
            if (!success) {
                this.release();
            }
        }
    }
    
    @Override
    public OpenSslServerSessionContext sessionContext() {
        return this.sessionContext;
    }
    
    @Override
    OpenSslKeyMaterialManager keyMaterialManager() {
        return this.keyMaterialManager;
    }
    
    static ServerContext newSessionContext(final ReferenceCountedOpenSslContext thiz, final long ctx, final OpenSslEngineMap engineMap, final X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, final X509Certificate[] keyCertChain, final PrivateKey key, final String keyPassword, KeyManagerFactory keyManagerFactory) throws SSLException {
        final ServerContext result = new ServerContext();
        synchronized (ReferenceCountedOpenSslContext.class) {
            try {
                SSLContext.setVerify(ctx, 0, 10);
                if (!OpenSsl.useKeyManagerFactory()) {
                    if (keyManagerFactory != null) {
                        throw new IllegalArgumentException("KeyManagerFactory not supported");
                    }
                    ObjectUtil.checkNotNull(keyCertChain, "keyCertChain");
                    ReferenceCountedOpenSslContext.setKeyMaterial(ctx, keyCertChain, key, keyPassword);
                }
                else {
                    if (keyManagerFactory == null) {
                        keyManagerFactory = SslContext.buildKeyManagerFactory(keyCertChain, key, keyPassword, keyManagerFactory);
                    }
                    final X509KeyManager keyManager = ReferenceCountedOpenSslContext.chooseX509KeyManager(keyManagerFactory.getKeyManagers());
                    result.keyMaterialManager = (ReferenceCountedOpenSslContext.useExtendedKeyManager(keyManager) ? new OpenSslExtendedKeyMaterialManager((X509ExtendedKeyManager)keyManager, keyPassword) : new OpenSslKeyMaterialManager(keyManager, keyPassword));
                }
            }
            catch (final Exception e) {
                throw new SSLException("failed to set certificate and key", e);
            }
            try {
                if (trustCertCollection != null) {
                    trustManagerFactory = SslContext.buildTrustManagerFactory(trustCertCollection, trustManagerFactory);
                }
                else if (trustManagerFactory == null) {
                    trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    trustManagerFactory.init((KeyStore)null);
                }
                final X509TrustManager manager = ReferenceCountedOpenSslContext.chooseTrustManager(trustManagerFactory.getTrustManagers());
                if (ReferenceCountedOpenSslContext.useExtendedTrustManager(manager)) {
                    SSLContext.setCertVerifyCallback(ctx, new ExtendedTrustManagerVerifyCallback(engineMap, (X509ExtendedTrustManager)manager));
                }
                else {
                    SSLContext.setCertVerifyCallback(ctx, new TrustManagerVerifyCallback(engineMap, manager));
                }
                final X509Certificate[] issuers = manager.getAcceptedIssuers();
                if (issuers != null && issuers.length > 0) {
                    long bio = 0L;
                    try {
                        bio = ReferenceCountedOpenSslContext.toBIO(issuers);
                        if (!SSLContext.setCACertificateBio(ctx, bio)) {
                            throw new SSLException("unable to setup accepted issuers for trustmanager " + manager);
                        }
                    }
                    finally {
                        ReferenceCountedOpenSslContext.freeBio(bio);
                    }
                }
            }
            catch (final SSLException e2) {
                throw e2;
            }
            catch (final Exception e) {
                throw new SSLException("unable to setup trustmanager", e);
            }
        }
        (result.sessionContext = new OpenSslServerSessionContext(thiz)).setSessionIdContext(ReferenceCountedOpenSslServerContext.ID);
        return result;
    }
    
    static {
        ID = new byte[] { 110, 101, 116, 116, 121 };
    }
    
    static final class ServerContext
    {
        OpenSslServerSessionContext sessionContext;
        OpenSslKeyMaterialManager keyMaterialManager;
    }
    
    private static final class TrustManagerVerifyCallback extends AbstractCertificateVerifier
    {
        private final X509TrustManager manager;
        
        TrustManagerVerifyCallback(final OpenSslEngineMap engineMap, final X509TrustManager manager) {
            super(engineMap);
            this.manager = manager;
        }
        
        @Override
        void verify(final ReferenceCountedOpenSslEngine engine, final X509Certificate[] peerCerts, final String auth) throws Exception {
            this.manager.checkClientTrusted(peerCerts, auth);
        }
    }
    
    private static final class ExtendedTrustManagerVerifyCallback extends AbstractCertificateVerifier
    {
        private final X509ExtendedTrustManager manager;
        
        ExtendedTrustManagerVerifyCallback(final OpenSslEngineMap engineMap, final X509ExtendedTrustManager manager) {
            super(engineMap);
            this.manager = manager;
        }
        
        @Override
        void verify(final ReferenceCountedOpenSslEngine engine, final X509Certificate[] peerCerts, final String auth) throws Exception {
            this.manager.checkClientTrusted(peerCerts, auth, engine);
        }
    }
}
