// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.util.ArrayList;
import java.util.HashSet;
import java.security.SecureRandom;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManager;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.security.KeyException;
import java.security.cert.CertificateException;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import java.security.Security;
import javax.net.ssl.KeyManagerFactory;
import java.io.File;
import javax.net.ssl.SSLEngine;
import io.netty.buffer.ByteBufAllocator;
import javax.net.ssl.SSLSessionContext;
import java.util.Collections;
import java.util.Arrays;
import io.netty.util.internal.ObjectUtil;
import javax.net.ssl.SSLContext;
import java.util.Set;
import java.util.List;
import io.netty.util.internal.logging.InternalLogger;

public class JdkSslContext extends SslContext
{
    private static final InternalLogger logger;
    static final String PROTOCOL = "TLS";
    static final String[] DEFAULT_PROTOCOLS;
    static final List<String> DEFAULT_CIPHERS;
    static final Set<String> SUPPORTED_CIPHERS;
    private final String[] protocols;
    private final String[] cipherSuites;
    private final List<String> unmodifiableCipherSuites;
    private final JdkApplicationProtocolNegotiator apn;
    private final ClientAuth clientAuth;
    private final SSLContext sslContext;
    private final boolean isClient;
    
    private static void addIfSupported(final Set<String> supported, final List<String> enabled, final String... names) {
        for (final String n : names) {
            if (supported.contains(n)) {
                enabled.add(n);
            }
        }
    }
    
    public JdkSslContext(final SSLContext sslContext, final boolean isClient, final ClientAuth clientAuth) {
        this(sslContext, isClient, null, IdentityCipherSuiteFilter.INSTANCE, JdkDefaultApplicationProtocolNegotiator.INSTANCE, clientAuth, null, false);
    }
    
    public JdkSslContext(final SSLContext sslContext, final boolean isClient, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apn, final ClientAuth clientAuth) {
        this(sslContext, isClient, ciphers, cipherFilter, toNegotiator(apn, !isClient), clientAuth, null, false);
    }
    
    JdkSslContext(final SSLContext sslContext, final boolean isClient, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final JdkApplicationProtocolNegotiator apn, final ClientAuth clientAuth, final String[] protocols, final boolean startTls) {
        super(startTls);
        this.apn = ObjectUtil.checkNotNull(apn, "apn");
        this.clientAuth = ObjectUtil.checkNotNull(clientAuth, "clientAuth");
        this.cipherSuites = ObjectUtil.checkNotNull(cipherFilter, "cipherFilter").filterCipherSuites(ciphers, JdkSslContext.DEFAULT_CIPHERS, JdkSslContext.SUPPORTED_CIPHERS);
        this.protocols = ((protocols == null) ? JdkSslContext.DEFAULT_PROTOCOLS : protocols);
        this.unmodifiableCipherSuites = Collections.unmodifiableList((List<? extends String>)Arrays.asList((T[])this.cipherSuites));
        this.sslContext = ObjectUtil.checkNotNull(sslContext, "sslContext");
        this.isClient = isClient;
    }
    
    public final SSLContext context() {
        return this.sslContext;
    }
    
    @Override
    public final boolean isClient() {
        return this.isClient;
    }
    
    @Override
    public final SSLSessionContext sessionContext() {
        if (this.isServer()) {
            return this.context().getServerSessionContext();
        }
        return this.context().getClientSessionContext();
    }
    
    @Override
    public final List<String> cipherSuites() {
        return this.unmodifiableCipherSuites;
    }
    
    @Override
    public final long sessionCacheSize() {
        return this.sessionContext().getSessionCacheSize();
    }
    
    @Override
    public final long sessionTimeout() {
        return this.sessionContext().getSessionTimeout();
    }
    
    @Override
    public final SSLEngine newEngine(final ByteBufAllocator alloc) {
        return this.configureAndWrapEngine(this.context().createSSLEngine());
    }
    
    @Override
    public final SSLEngine newEngine(final ByteBufAllocator alloc, final String peerHost, final int peerPort) {
        return this.configureAndWrapEngine(this.context().createSSLEngine(peerHost, peerPort));
    }
    
    private SSLEngine configureAndWrapEngine(final SSLEngine engine) {
        engine.setEnabledCipherSuites(this.cipherSuites);
        engine.setEnabledProtocols(this.protocols);
        engine.setUseClientMode(this.isClient());
        if (this.isServer()) {
            switch (this.clientAuth) {
                case OPTIONAL: {
                    engine.setWantClientAuth(true);
                    break;
                }
                case REQUIRE: {
                    engine.setNeedClientAuth(true);
                    break;
                }
            }
        }
        return this.apn.wrapperFactory().wrapSslEngine(engine, this.apn, this.isServer());
    }
    
    @Override
    public final JdkApplicationProtocolNegotiator applicationProtocolNegotiator() {
        return this.apn;
    }
    
    static JdkApplicationProtocolNegotiator toNegotiator(final ApplicationProtocolConfig config, final boolean isServer) {
        if (config == null) {
            return JdkDefaultApplicationProtocolNegotiator.INSTANCE;
        }
        switch (config.protocol()) {
            case NONE: {
                return JdkDefaultApplicationProtocolNegotiator.INSTANCE;
            }
            case ALPN: {
                if (isServer) {
                    switch (config.selectorFailureBehavior()) {
                        case FATAL_ALERT: {
                            return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
                        }
                        case NO_ADVERTISE: {
                            return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
                        }
                        default: {
                            throw new UnsupportedOperationException("JDK provider does not support " + config.selectorFailureBehavior() + " failure behavior");
                        }
                    }
                }
                else {
                    switch (config.selectedListenerFailureBehavior()) {
                        case ACCEPT: {
                            return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
                        }
                        case FATAL_ALERT: {
                            return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
                        }
                        default: {
                            throw new UnsupportedOperationException("JDK provider does not support " + config.selectedListenerFailureBehavior() + " failure behavior");
                        }
                    }
                }
                break;
            }
            case NPN: {
                if (isServer) {
                    switch (config.selectedListenerFailureBehavior()) {
                        case ACCEPT: {
                            return new JdkNpnApplicationProtocolNegotiator(false, config.supportedProtocols());
                        }
                        case FATAL_ALERT: {
                            return new JdkNpnApplicationProtocolNegotiator(true, config.supportedProtocols());
                        }
                        default: {
                            throw new UnsupportedOperationException("JDK provider does not support " + config.selectedListenerFailureBehavior() + " failure behavior");
                        }
                    }
                }
                else {
                    switch (config.selectorFailureBehavior()) {
                        case FATAL_ALERT: {
                            return new JdkNpnApplicationProtocolNegotiator(true, config.supportedProtocols());
                        }
                        case NO_ADVERTISE: {
                            return new JdkNpnApplicationProtocolNegotiator(false, config.supportedProtocols());
                        }
                        default: {
                            throw new UnsupportedOperationException("JDK provider does not support " + config.selectorFailureBehavior() + " failure behavior");
                        }
                    }
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("JDK provider does not support " + config.protocol() + " protocol");
            }
        }
    }
    
    @Deprecated
    protected static KeyManagerFactory buildKeyManagerFactory(final File certChainFile, final File keyFile, final String keyPassword, final KeyManagerFactory kmf) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, CertificateException, KeyException, IOException {
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }
        return buildKeyManagerFactory(certChainFile, algorithm, keyFile, keyPassword, kmf);
    }
    
    @Deprecated
    protected static KeyManagerFactory buildKeyManagerFactory(final File certChainFile, final String keyAlgorithm, final File keyFile, final String keyPassword, final KeyManagerFactory kmf) throws KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException, CertificateException, KeyException, UnrecoverableKeyException {
        return SslContext.buildKeyManagerFactory(SslContext.toX509Certificates(certChainFile), keyAlgorithm, SslContext.toPrivateKey(keyFile, keyPassword), keyPassword, kmf);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(JdkSslContext.class);
        SSLContext context;
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, null, null);
        }
        catch (final Exception e) {
            throw new Error("failed to initialize the default SSL context", e);
        }
        final SSLEngine engine = context.createSSLEngine();
        final String[] supportedProtocols = engine.getSupportedProtocols();
        final Set<String> supportedProtocolsSet = new HashSet<String>(supportedProtocols.length);
        for (int i = 0; i < supportedProtocols.length; ++i) {
            supportedProtocolsSet.add(supportedProtocols[i]);
        }
        final List<String> protocols = new ArrayList<String>();
        addIfSupported(supportedProtocolsSet, protocols, "TLSv1.2", "TLSv1.1", "TLSv1");
        if (!protocols.isEmpty()) {
            DEFAULT_PROTOCOLS = protocols.toArray(new String[protocols.size()]);
        }
        else {
            DEFAULT_PROTOCOLS = engine.getEnabledProtocols();
        }
        final String[] supportedCiphers = engine.getSupportedCipherSuites();
        SUPPORTED_CIPHERS = new HashSet<String>(supportedCiphers.length);
        for (int i = 0; i < supportedCiphers.length; ++i) {
            JdkSslContext.SUPPORTED_CIPHERS.add(supportedCiphers[i]);
        }
        final List<String> ciphers = new ArrayList<String>();
        addIfSupported(JdkSslContext.SUPPORTED_CIPHERS, ciphers, "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", "TLS_RSA_WITH_AES_128_GCM_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA", "TLS_RSA_WITH_AES_256_CBC_SHA");
        if (ciphers.isEmpty()) {
            for (final String cipher : engine.getEnabledCipherSuites()) {
                if (!cipher.contains("_RC4_")) {
                    ciphers.add(cipher);
                }
            }
        }
        DEFAULT_CIPHERS = Collections.unmodifiableList((List<? extends String>)ciphers);
        if (JdkSslContext.logger.isDebugEnabled()) {
            JdkSslContext.logger.debug("Default protocols (JDK): {} ", Arrays.asList(JdkSslContext.DEFAULT_PROTOCOLS));
            JdkSslContext.logger.debug("Default cipher suites (JDK): {}", JdkSslContext.DEFAULT_CIPHERS);
        }
    }
}
