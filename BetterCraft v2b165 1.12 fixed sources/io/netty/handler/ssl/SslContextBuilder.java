// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import javax.net.ssl.SSLException;
import io.netty.util.internal.ObjectUtil;
import java.io.InputStream;
import java.io.File;
import javax.net.ssl.KeyManagerFactory;
import java.security.PrivateKey;
import javax.net.ssl.TrustManagerFactory;
import java.security.cert.X509Certificate;

public final class SslContextBuilder
{
    private final boolean forServer;
    private SslProvider provider;
    private X509Certificate[] trustCertCollection;
    private TrustManagerFactory trustManagerFactory;
    private X509Certificate[] keyCertChain;
    private PrivateKey key;
    private String keyPassword;
    private KeyManagerFactory keyManagerFactory;
    private Iterable<String> ciphers;
    private CipherSuiteFilter cipherFilter;
    private ApplicationProtocolConfig apn;
    private long sessionCacheSize;
    private long sessionTimeout;
    private ClientAuth clientAuth;
    private String[] protocols;
    private boolean startTls;
    
    public static SslContextBuilder forClient() {
        return new SslContextBuilder(false);
    }
    
    public static SslContextBuilder forServer(final File keyCertChainFile, final File keyFile) {
        return new SslContextBuilder(true).keyManager(keyCertChainFile, keyFile);
    }
    
    public static SslContextBuilder forServer(final InputStream keyCertChainInputStream, final InputStream keyInputStream) {
        return new SslContextBuilder(true).keyManager(keyCertChainInputStream, keyInputStream);
    }
    
    public static SslContextBuilder forServer(final PrivateKey key, final X509Certificate... keyCertChain) {
        return new SslContextBuilder(true).keyManager(key, keyCertChain);
    }
    
    public static SslContextBuilder forServer(final File keyCertChainFile, final File keyFile, final String keyPassword) {
        return new SslContextBuilder(true).keyManager(keyCertChainFile, keyFile, keyPassword);
    }
    
    public static SslContextBuilder forServer(final InputStream keyCertChainInputStream, final InputStream keyInputStream, final String keyPassword) {
        return new SslContextBuilder(true).keyManager(keyCertChainInputStream, keyInputStream, keyPassword);
    }
    
    public static SslContextBuilder forServer(final PrivateKey key, final String keyPassword, final X509Certificate... keyCertChain) {
        return new SslContextBuilder(true).keyManager(key, keyPassword, keyCertChain);
    }
    
    public static SslContextBuilder forServer(final KeyManagerFactory keyManagerFactory) {
        return new SslContextBuilder(true).keyManager(keyManagerFactory);
    }
    
    private SslContextBuilder(final boolean forServer) {
        this.cipherFilter = IdentityCipherSuiteFilter.INSTANCE;
        this.clientAuth = ClientAuth.NONE;
        this.forServer = forServer;
    }
    
    public SslContextBuilder sslProvider(final SslProvider provider) {
        this.provider = provider;
        return this;
    }
    
    public SslContextBuilder trustManager(final File trustCertCollectionFile) {
        try {
            return this.trustManager(SslContext.toX509Certificates(trustCertCollectionFile));
        }
        catch (final Exception e) {
            throw new IllegalArgumentException("File does not contain valid certificates: " + trustCertCollectionFile, e);
        }
    }
    
    public SslContextBuilder trustManager(final InputStream trustCertCollectionInputStream) {
        try {
            return this.trustManager(SslContext.toX509Certificates(trustCertCollectionInputStream));
        }
        catch (final Exception e) {
            throw new IllegalArgumentException("Input stream does not contain valid certificates.", e);
        }
    }
    
    public SslContextBuilder trustManager(final X509Certificate... trustCertCollection) {
        this.trustCertCollection = (X509Certificate[])((trustCertCollection != null) ? ((X509Certificate[])trustCertCollection.clone()) : null);
        this.trustManagerFactory = null;
        return this;
    }
    
    public SslContextBuilder trustManager(final TrustManagerFactory trustManagerFactory) {
        this.trustCertCollection = null;
        this.trustManagerFactory = trustManagerFactory;
        return this;
    }
    
    public SslContextBuilder keyManager(final File keyCertChainFile, final File keyFile) {
        return this.keyManager(keyCertChainFile, keyFile, null);
    }
    
    public SslContextBuilder keyManager(final InputStream keyCertChainInputStream, final InputStream keyInputStream) {
        return this.keyManager(keyCertChainInputStream, keyInputStream, null);
    }
    
    public SslContextBuilder keyManager(final PrivateKey key, final X509Certificate... keyCertChain) {
        return this.keyManager(key, (String)null, keyCertChain);
    }
    
    public SslContextBuilder keyManager(final File keyCertChainFile, final File keyFile, final String keyPassword) {
        X509Certificate[] keyCertChain;
        try {
            keyCertChain = SslContext.toX509Certificates(keyCertChainFile);
        }
        catch (final Exception e) {
            throw new IllegalArgumentException("File does not contain valid certificates: " + keyCertChainFile, e);
        }
        PrivateKey key;
        try {
            key = SslContext.toPrivateKey(keyFile, keyPassword);
        }
        catch (final Exception e) {
            throw new IllegalArgumentException("File does not contain valid private key: " + keyFile, e);
        }
        return this.keyManager(key, keyPassword, keyCertChain);
    }
    
    public SslContextBuilder keyManager(final InputStream keyCertChainInputStream, final InputStream keyInputStream, final String keyPassword) {
        X509Certificate[] keyCertChain;
        try {
            keyCertChain = SslContext.toX509Certificates(keyCertChainInputStream);
        }
        catch (final Exception e) {
            throw new IllegalArgumentException("Input stream not contain valid certificates.", e);
        }
        PrivateKey key;
        try {
            key = SslContext.toPrivateKey(keyInputStream, keyPassword);
        }
        catch (final Exception e) {
            throw new IllegalArgumentException("Input stream does not contain valid private key.", e);
        }
        return this.keyManager(key, keyPassword, keyCertChain);
    }
    
    public SslContextBuilder keyManager(final PrivateKey key, final String keyPassword, final X509Certificate... keyCertChain) {
        if (this.forServer) {
            ObjectUtil.checkNotNull(keyCertChain, "keyCertChain required for servers");
            if (keyCertChain.length == 0) {
                throw new IllegalArgumentException("keyCertChain must be non-empty");
            }
            ObjectUtil.checkNotNull(key, "key required for servers");
        }
        if (keyCertChain == null || keyCertChain.length == 0) {
            this.keyCertChain = null;
        }
        else {
            for (final X509Certificate cert : keyCertChain) {
                if (cert == null) {
                    throw new IllegalArgumentException("keyCertChain contains null entry");
                }
            }
            this.keyCertChain = keyCertChain.clone();
        }
        this.key = key;
        this.keyPassword = keyPassword;
        this.keyManagerFactory = null;
        return this;
    }
    
    public SslContextBuilder keyManager(final KeyManagerFactory keyManagerFactory) {
        if (this.forServer) {
            ObjectUtil.checkNotNull(keyManagerFactory, "keyManagerFactory required for servers");
        }
        this.keyCertChain = null;
        this.key = null;
        this.keyPassword = null;
        this.keyManagerFactory = keyManagerFactory;
        return this;
    }
    
    public SslContextBuilder ciphers(final Iterable<String> ciphers) {
        return this.ciphers(ciphers, IdentityCipherSuiteFilter.INSTANCE);
    }
    
    public SslContextBuilder ciphers(final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter) {
        ObjectUtil.checkNotNull(cipherFilter, "cipherFilter");
        this.ciphers = ciphers;
        this.cipherFilter = cipherFilter;
        return this;
    }
    
    public SslContextBuilder applicationProtocolConfig(final ApplicationProtocolConfig apn) {
        this.apn = apn;
        return this;
    }
    
    public SslContextBuilder sessionCacheSize(final long sessionCacheSize) {
        this.sessionCacheSize = sessionCacheSize;
        return this;
    }
    
    public SslContextBuilder sessionTimeout(final long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
        return this;
    }
    
    public SslContextBuilder clientAuth(final ClientAuth clientAuth) {
        this.clientAuth = ObjectUtil.checkNotNull(clientAuth, "clientAuth");
        return this;
    }
    
    public SslContextBuilder protocols(final String... protocols) {
        this.protocols = (String[])((protocols == null) ? null : ((String[])protocols.clone()));
        return this;
    }
    
    public SslContextBuilder startTls(final boolean startTls) {
        this.startTls = startTls;
        return this;
    }
    
    public SslContext build() throws SSLException {
        if (this.forServer) {
            return SslContext.newServerContextInternal(this.provider, this.trustCertCollection, this.trustManagerFactory, this.keyCertChain, this.key, this.keyPassword, this.keyManagerFactory, this.ciphers, this.cipherFilter, this.apn, this.sessionCacheSize, this.sessionTimeout, this.clientAuth, this.protocols, this.startTls);
        }
        return SslContext.newClientContextInternal(this.provider, this.trustCertCollection, this.trustManagerFactory, this.keyCertChain, this.key, this.keyPassword, this.keyManagerFactory, this.ciphers, this.cipherFilter, this.apn, this.protocols, this.sessionCacheSize, this.sessionTimeout);
    }
}
