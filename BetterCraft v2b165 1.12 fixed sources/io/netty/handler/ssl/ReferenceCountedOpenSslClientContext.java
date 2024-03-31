// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.SSLHandshakeException;
import javax.security.auth.x500.X500Principal;
import javax.net.ssl.SSLEngine;
import io.netty.util.internal.logging.InternalLoggerFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.X509KeyManager;
import io.netty.internal.tcnative.CertificateVerifier;
import javax.net.ssl.X509ExtendedTrustManager;
import java.security.KeyStore;
import io.netty.internal.tcnative.CertificateRequestedCallback;
import io.netty.internal.tcnative.SSLContext;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.SSLException;
import java.security.cert.Certificate;
import javax.net.ssl.KeyManagerFactory;
import java.security.PrivateKey;
import javax.net.ssl.TrustManagerFactory;
import java.security.cert.X509Certificate;
import io.netty.util.internal.logging.InternalLogger;

public final class ReferenceCountedOpenSslClientContext extends ReferenceCountedOpenSslContext
{
    private static final InternalLogger logger;
    private final OpenSslSessionContext sessionContext;
    
    ReferenceCountedOpenSslClientContext(final X509Certificate[] trustCertCollection, final TrustManagerFactory trustManagerFactory, final X509Certificate[] keyCertChain, final PrivateKey key, final String keyPassword, final KeyManagerFactory keyManagerFactory, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apn, final String[] protocols, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        super(ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, 0, keyCertChain, ClientAuth.NONE, protocols, false, true);
        boolean success = false;
        try {
            this.sessionContext = newSessionContext(this, this.ctx, this.engineMap, trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory);
            success = true;
        }
        finally {
            if (!success) {
                this.release();
            }
        }
    }
    
    @Override
    OpenSslKeyMaterialManager keyMaterialManager() {
        return null;
    }
    
    @Override
    public OpenSslSessionContext sessionContext() {
        return this.sessionContext;
    }
    
    static OpenSslSessionContext newSessionContext(final ReferenceCountedOpenSslContext thiz, final long ctx, final OpenSslEngineMap engineMap, final X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, final X509Certificate[] keyCertChain, final PrivateKey key, final String keyPassword, KeyManagerFactory keyManagerFactory) throws SSLException {
        if ((key == null && keyCertChain != null) || (key != null && keyCertChain == null)) {
            throw new IllegalArgumentException("Either both keyCertChain and key needs to be null or none of them");
        }
        synchronized (ReferenceCountedOpenSslContext.class) {
            try {
                if (!OpenSsl.useKeyManagerFactory()) {
                    if (keyManagerFactory != null) {
                        throw new IllegalArgumentException("KeyManagerFactory not supported");
                    }
                    if (keyCertChain != null) {
                        ReferenceCountedOpenSslContext.setKeyMaterial(ctx, keyCertChain, key, keyPassword);
                    }
                }
                else {
                    if (keyManagerFactory == null && keyCertChain != null) {
                        keyManagerFactory = SslContext.buildKeyManagerFactory(keyCertChain, key, keyPassword, keyManagerFactory);
                    }
                    if (keyManagerFactory != null) {
                        final X509KeyManager keyManager = ReferenceCountedOpenSslContext.chooseX509KeyManager(keyManagerFactory.getKeyManagers());
                        final OpenSslKeyMaterialManager materialManager = ReferenceCountedOpenSslContext.useExtendedKeyManager(keyManager) ? new OpenSslExtendedKeyMaterialManager((X509ExtendedKeyManager)keyManager, keyPassword) : new OpenSslKeyMaterialManager(keyManager, keyPassword);
                        SSLContext.setCertRequestedCallback(ctx, new OpenSslCertificateRequestedCallback(engineMap, materialManager));
                    }
                }
            }
            catch (final Exception e) {
                throw new SSLException("failed to set certificate and key", e);
            }
            SSLContext.setVerify(ctx, 0, 10);
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
            }
            catch (final Exception e) {
                throw new SSLException("unable to setup trustmanager", e);
            }
        }
        return new OpenSslClientSessionContext(thiz);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ReferenceCountedOpenSslClientContext.class);
    }
    
    static final class OpenSslClientSessionContext extends OpenSslSessionContext
    {
        OpenSslClientSessionContext(final ReferenceCountedOpenSslContext context) {
            super(context);
        }
        
        @Override
        public void setSessionTimeout(final int seconds) {
            if (seconds < 0) {
                throw new IllegalArgumentException();
            }
        }
        
        @Override
        public int getSessionTimeout() {
            return 0;
        }
        
        @Override
        public void setSessionCacheSize(final int size) {
            if (size < 0) {
                throw new IllegalArgumentException();
            }
        }
        
        @Override
        public int getSessionCacheSize() {
            return 0;
        }
        
        @Override
        public void setSessionCacheEnabled(final boolean enabled) {
        }
        
        @Override
        public boolean isSessionCacheEnabled() {
            return false;
        }
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
            this.manager.checkServerTrusted(peerCerts, auth);
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
            this.manager.checkServerTrusted(peerCerts, auth, engine);
        }
    }
    
    private static final class OpenSslCertificateRequestedCallback implements CertificateRequestedCallback
    {
        private final OpenSslEngineMap engineMap;
        private final OpenSslKeyMaterialManager keyManagerHolder;
        
        OpenSslCertificateRequestedCallback(final OpenSslEngineMap engineMap, final OpenSslKeyMaterialManager keyManagerHolder) {
            this.engineMap = engineMap;
            this.keyManagerHolder = keyManagerHolder;
        }
        
        @Override
        public KeyMaterial requested(final long ssl, final byte[] keyTypeBytes, final byte[][] asn1DerEncodedPrincipals) {
            final ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
            try {
                final Set<String> keyTypesSet = supportedClientKeyTypes(keyTypeBytes);
                final String[] keyTypes = keyTypesSet.toArray(new String[keyTypesSet.size()]);
                X500Principal[] issuers;
                if (asn1DerEncodedPrincipals == null) {
                    issuers = null;
                }
                else {
                    issuers = new X500Principal[asn1DerEncodedPrincipals.length];
                    for (int i = 0; i < asn1DerEncodedPrincipals.length; ++i) {
                        issuers[i] = new X500Principal(asn1DerEncodedPrincipals[i]);
                    }
                }
                return this.keyManagerHolder.keyMaterial(engine, keyTypes, issuers);
            }
            catch (final Throwable cause) {
                ReferenceCountedOpenSslClientContext.logger.debug("request of key failed", cause);
                final SSLHandshakeException e = new SSLHandshakeException("General OpenSslEngine problem");
                e.initCause(cause);
                engine.handshakeException = e;
                return null;
            }
        }
        
        private static Set<String> supportedClientKeyTypes(final byte[] clientCertificateTypes) {
            final Set<String> result = new HashSet<String>(clientCertificateTypes.length);
            for (final byte keyTypeCode : clientCertificateTypes) {
                final String keyType = clientKeyType(keyTypeCode);
                if (keyType != null) {
                    result.add(keyType);
                }
            }
            return result;
        }
        
        private static String clientKeyType(final byte clientCertificateType) {
            switch (clientCertificateType) {
                case 1: {
                    return "RSA";
                }
                case 3: {
                    return "DH_RSA";
                }
                case 64: {
                    return "EC";
                }
                case 65: {
                    return "EC_RSA";
                }
                case 66: {
                    return "EC_EC";
                }
                default: {
                    return null;
                }
            }
        }
    }
}
