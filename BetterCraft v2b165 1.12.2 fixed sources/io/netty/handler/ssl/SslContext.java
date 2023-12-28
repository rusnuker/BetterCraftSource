// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.util.internal.EmptyArrays;
import java.security.UnrecoverableKeyException;
import java.security.Security;
import io.netty.buffer.ByteBufInputStream;
import java.security.KeyFactory;
import io.netty.buffer.ByteBuf;
import java.security.KeyException;
import java.security.cert.CertificateException;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import javax.crypto.SecretKey;
import java.security.Key;
import javax.crypto.Cipher;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.EncryptedPrivateKeyInfo;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLEngine;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.SSLException;
import java.io.File;
import java.security.cert.CertificateFactory;

public abstract class SslContext
{
    static final CertificateFactory X509_CERT_FACTORY;
    private final boolean startTls;
    
    public static SslProvider defaultServerProvider() {
        return defaultProvider();
    }
    
    public static SslProvider defaultClientProvider() {
        return defaultProvider();
    }
    
    private static SslProvider defaultProvider() {
        if (OpenSsl.isAvailable()) {
            return SslProvider.OPENSSL;
        }
        return SslProvider.JDK;
    }
    
    @Deprecated
    public static SslContext newServerContext(final File certChainFile, final File keyFile) throws SSLException {
        return newServerContext(certChainFile, keyFile, null);
    }
    
    @Deprecated
    public static SslContext newServerContext(final File certChainFile, final File keyFile, final String keyPassword) throws SSLException {
        return newServerContext(null, certChainFile, keyFile, keyPassword);
    }
    
    @Deprecated
    public static SslContext newServerContext(final File certChainFile, final File keyFile, final String keyPassword, final Iterable<String> ciphers, final Iterable<String> nextProtocols, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        return newServerContext(null, certChainFile, keyFile, keyPassword, ciphers, nextProtocols, sessionCacheSize, sessionTimeout);
    }
    
    @Deprecated
    public static SslContext newServerContext(final File certChainFile, final File keyFile, final String keyPassword, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apn, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        return newServerContext(null, certChainFile, keyFile, keyPassword, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
    }
    
    @Deprecated
    public static SslContext newServerContext(final SslProvider provider, final File certChainFile, final File keyFile) throws SSLException {
        return newServerContext(provider, certChainFile, keyFile, null);
    }
    
    @Deprecated
    public static SslContext newServerContext(final SslProvider provider, final File certChainFile, final File keyFile, final String keyPassword) throws SSLException {
        return newServerContext(provider, certChainFile, keyFile, keyPassword, null, IdentityCipherSuiteFilter.INSTANCE, null, 0L, 0L);
    }
    
    @Deprecated
    public static SslContext newServerContext(final SslProvider provider, final File certChainFile, final File keyFile, final String keyPassword, final Iterable<String> ciphers, final Iterable<String> nextProtocols, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        return newServerContext(provider, certChainFile, keyFile, keyPassword, ciphers, IdentityCipherSuiteFilter.INSTANCE, toApplicationProtocolConfig(nextProtocols), sessionCacheSize, sessionTimeout);
    }
    
    @Deprecated
    public static SslContext newServerContext(final SslProvider provider, final File certChainFile, final File keyFile, final String keyPassword, final TrustManagerFactory trustManagerFactory, final Iterable<String> ciphers, final Iterable<String> nextProtocols, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        return newServerContext(provider, null, trustManagerFactory, certChainFile, keyFile, keyPassword, null, ciphers, IdentityCipherSuiteFilter.INSTANCE, toApplicationProtocolConfig(nextProtocols), sessionCacheSize, sessionTimeout);
    }
    
    @Deprecated
    public static SslContext newServerContext(final SslProvider provider, final File certChainFile, final File keyFile, final String keyPassword, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apn, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        return newServerContext(provider, null, null, certChainFile, keyFile, keyPassword, null, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
    }
    
    @Deprecated
    public static SslContext newServerContext(final SslProvider provider, final File trustCertCollectionFile, final TrustManagerFactory trustManagerFactory, final File keyCertChainFile, final File keyFile, final String keyPassword, final KeyManagerFactory keyManagerFactory, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apn, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        try {
            return newServerContextInternal(provider, toX509Certificates(trustCertCollectionFile), trustManagerFactory, toX509Certificates(keyCertChainFile), toPrivateKey(keyFile, keyPassword), keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, ClientAuth.NONE, null, false);
        }
        catch (final Exception e) {
            if (e instanceof SSLException) {
                throw (SSLException)e;
            }
            throw new SSLException("failed to initialize the server-side SSL context", e);
        }
    }
    
    static SslContext newServerContextInternal(SslProvider provider, final X509Certificate[] trustCertCollection, final TrustManagerFactory trustManagerFactory, final X509Certificate[] keyCertChain, final PrivateKey key, final String keyPassword, final KeyManagerFactory keyManagerFactory, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apn, final long sessionCacheSize, final long sessionTimeout, final ClientAuth clientAuth, final String[] protocols, final boolean startTls) throws SSLException {
        if (provider == null) {
            provider = defaultServerProvider();
        }
        switch (provider) {
            case JDK: {
                return new JdkSslServerContext(trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, clientAuth, protocols, startTls);
            }
            case OPENSSL: {
                return new OpenSslServerContext(trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, clientAuth, protocols, startTls);
            }
            case OPENSSL_REFCNT: {
                return new ReferenceCountedOpenSslServerContext(trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, clientAuth, protocols, startTls);
            }
            default: {
                throw new Error(provider.toString());
            }
        }
    }
    
    @Deprecated
    public static SslContext newClientContext() throws SSLException {
        return newClientContext(null, null, null);
    }
    
    @Deprecated
    public static SslContext newClientContext(final File certChainFile) throws SSLException {
        return newClientContext(null, certChainFile);
    }
    
    @Deprecated
    public static SslContext newClientContext(final TrustManagerFactory trustManagerFactory) throws SSLException {
        return newClientContext(null, null, trustManagerFactory);
    }
    
    @Deprecated
    public static SslContext newClientContext(final File certChainFile, final TrustManagerFactory trustManagerFactory) throws SSLException {
        return newClientContext(null, certChainFile, trustManagerFactory);
    }
    
    @Deprecated
    public static SslContext newClientContext(final File certChainFile, final TrustManagerFactory trustManagerFactory, final Iterable<String> ciphers, final Iterable<String> nextProtocols, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        return newClientContext(null, certChainFile, trustManagerFactory, ciphers, nextProtocols, sessionCacheSize, sessionTimeout);
    }
    
    @Deprecated
    public static SslContext newClientContext(final File certChainFile, final TrustManagerFactory trustManagerFactory, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apn, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        return newClientContext(null, certChainFile, trustManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
    }
    
    @Deprecated
    public static SslContext newClientContext(final SslProvider provider) throws SSLException {
        return newClientContext(provider, null, null);
    }
    
    @Deprecated
    public static SslContext newClientContext(final SslProvider provider, final File certChainFile) throws SSLException {
        return newClientContext(provider, certChainFile, null);
    }
    
    @Deprecated
    public static SslContext newClientContext(final SslProvider provider, final TrustManagerFactory trustManagerFactory) throws SSLException {
        return newClientContext(provider, null, trustManagerFactory);
    }
    
    @Deprecated
    public static SslContext newClientContext(final SslProvider provider, final File certChainFile, final TrustManagerFactory trustManagerFactory) throws SSLException {
        return newClientContext(provider, certChainFile, trustManagerFactory, null, IdentityCipherSuiteFilter.INSTANCE, null, 0L, 0L);
    }
    
    @Deprecated
    public static SslContext newClientContext(final SslProvider provider, final File certChainFile, final TrustManagerFactory trustManagerFactory, final Iterable<String> ciphers, final Iterable<String> nextProtocols, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        return newClientContext(provider, certChainFile, trustManagerFactory, null, null, null, null, ciphers, IdentityCipherSuiteFilter.INSTANCE, toApplicationProtocolConfig(nextProtocols), sessionCacheSize, sessionTimeout);
    }
    
    @Deprecated
    public static SslContext newClientContext(final SslProvider provider, final File certChainFile, final TrustManagerFactory trustManagerFactory, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apn, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        return newClientContext(provider, certChainFile, trustManagerFactory, null, null, null, null, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
    }
    
    @Deprecated
    public static SslContext newClientContext(final SslProvider provider, final File trustCertCollectionFile, final TrustManagerFactory trustManagerFactory, final File keyCertChainFile, final File keyFile, final String keyPassword, final KeyManagerFactory keyManagerFactory, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apn, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        try {
            return newClientContextInternal(provider, toX509Certificates(trustCertCollectionFile), trustManagerFactory, toX509Certificates(keyCertChainFile), toPrivateKey(keyFile, keyPassword), keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, null, sessionCacheSize, sessionTimeout);
        }
        catch (final Exception e) {
            if (e instanceof SSLException) {
                throw (SSLException)e;
            }
            throw new SSLException("failed to initialize the client-side SSL context", e);
        }
    }
    
    static SslContext newClientContextInternal(SslProvider provider, final X509Certificate[] trustCert, final TrustManagerFactory trustManagerFactory, final X509Certificate[] keyCertChain, final PrivateKey key, final String keyPassword, final KeyManagerFactory keyManagerFactory, final Iterable<String> ciphers, final CipherSuiteFilter cipherFilter, final ApplicationProtocolConfig apn, final String[] protocols, final long sessionCacheSize, final long sessionTimeout) throws SSLException {
        if (provider == null) {
            provider = defaultClientProvider();
        }
        switch (provider) {
            case JDK: {
                return new JdkSslClientContext(trustCert, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, protocols, sessionCacheSize, sessionTimeout);
            }
            case OPENSSL: {
                return new OpenSslClientContext(trustCert, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, protocols, sessionCacheSize, sessionTimeout);
            }
            case OPENSSL_REFCNT: {
                return new ReferenceCountedOpenSslClientContext(trustCert, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, apn, protocols, sessionCacheSize, sessionTimeout);
            }
            default: {
                throw new Error(provider.toString());
            }
        }
    }
    
    static ApplicationProtocolConfig toApplicationProtocolConfig(final Iterable<String> nextProtocols) {
        ApplicationProtocolConfig apn;
        if (nextProtocols == null) {
            apn = ApplicationProtocolConfig.DISABLED;
        }
        else {
            apn = new ApplicationProtocolConfig(ApplicationProtocolConfig.Protocol.NPN_AND_ALPN, ApplicationProtocolConfig.SelectorFailureBehavior.CHOOSE_MY_LAST_PROTOCOL, ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT, nextProtocols);
        }
        return apn;
    }
    
    protected SslContext() {
        this(false);
    }
    
    protected SslContext(final boolean startTls) {
        this.startTls = startTls;
    }
    
    public final boolean isServer() {
        return !this.isClient();
    }
    
    public abstract boolean isClient();
    
    public abstract List<String> cipherSuites();
    
    public abstract long sessionCacheSize();
    
    public abstract long sessionTimeout();
    
    @Deprecated
    public final List<String> nextProtocols() {
        return this.applicationProtocolNegotiator().protocols();
    }
    
    public abstract ApplicationProtocolNegotiator applicationProtocolNegotiator();
    
    public abstract SSLEngine newEngine(final ByteBufAllocator p0);
    
    public abstract SSLEngine newEngine(final ByteBufAllocator p0, final String p1, final int p2);
    
    public abstract SSLSessionContext sessionContext();
    
    public final SslHandler newHandler(final ByteBufAllocator alloc) {
        return new SslHandler(this.newEngine(alloc), this.startTls);
    }
    
    public final SslHandler newHandler(final ByteBufAllocator alloc, final String peerHost, final int peerPort) {
        return new SslHandler(this.newEngine(alloc, peerHost, peerPort), this.startTls);
    }
    
    protected static PKCS8EncodedKeySpec generateKeySpec(final char[] password, final byte[] key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
        if (password == null) {
            return new PKCS8EncodedKeySpec(key);
        }
        final EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(key);
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryptedPrivateKeyInfo.getAlgName());
        final PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
        final SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
        final Cipher cipher = Cipher.getInstance(encryptedPrivateKeyInfo.getAlgName());
        cipher.init(2, pbeKey, encryptedPrivateKeyInfo.getAlgParameters());
        return encryptedPrivateKeyInfo.getKeySpec(cipher);
    }
    
    static KeyStore buildKeyStore(final X509Certificate[] certChain, final PrivateKey key, final char[] keyPasswordChars) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        final KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, null);
        ks.setKeyEntry("key", key, keyPasswordChars, certChain);
        return ks;
    }
    
    static PrivateKey toPrivateKey(final File keyFile, final String keyPassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, KeyException, IOException {
        if (keyFile == null) {
            return null;
        }
        return getPrivateKeyFromByteBuffer(PemReader.readPrivateKey(keyFile), keyPassword);
    }
    
    static PrivateKey toPrivateKey(final InputStream keyInputStream, final String keyPassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, KeyException, IOException {
        if (keyInputStream == null) {
            return null;
        }
        return getPrivateKeyFromByteBuffer(PemReader.readPrivateKey(keyInputStream), keyPassword);
    }
    
    private static PrivateKey getPrivateKeyFromByteBuffer(final ByteBuf encodedKeyBuf, final String keyPassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, KeyException, IOException {
        final byte[] encodedKey = new byte[encodedKeyBuf.readableBytes()];
        encodedKeyBuf.readBytes(encodedKey).release();
        final PKCS8EncodedKeySpec encodedKeySpec = generateKeySpec((char[])((keyPassword == null) ? null : keyPassword.toCharArray()), encodedKey);
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(encodedKeySpec);
        }
        catch (final InvalidKeySpecException ignore) {
            try {
                return KeyFactory.getInstance("DSA").generatePrivate(encodedKeySpec);
            }
            catch (final InvalidKeySpecException ignore2) {
                try {
                    return KeyFactory.getInstance("EC").generatePrivate(encodedKeySpec);
                }
                catch (final InvalidKeySpecException e) {
                    throw new InvalidKeySpecException("Neither RSA, DSA nor EC worked", e);
                }
            }
        }
    }
    
    @Deprecated
    protected static TrustManagerFactory buildTrustManagerFactory(final File certChainFile, final TrustManagerFactory trustManagerFactory) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
        final X509Certificate[] x509Certs = toX509Certificates(certChainFile);
        return buildTrustManagerFactory(x509Certs, trustManagerFactory);
    }
    
    static X509Certificate[] toX509Certificates(final File file) throws CertificateException {
        if (file == null) {
            return null;
        }
        return getCertificatesFromBuffers(PemReader.readCertificates(file));
    }
    
    static X509Certificate[] toX509Certificates(final InputStream in) throws CertificateException {
        if (in == null) {
            return null;
        }
        return getCertificatesFromBuffers(PemReader.readCertificates(in));
    }
    
    private static X509Certificate[] getCertificatesFromBuffers(final ByteBuf[] certs) throws CertificateException {
        final CertificateFactory cf = CertificateFactory.getInstance("X.509");
        final X509Certificate[] x509Certs = new X509Certificate[certs.length];
        int i = 0;
        try {
            while (i < certs.length) {
                final InputStream is = new ByteBufInputStream(certs[i], true);
                try {
                    x509Certs[i] = (X509Certificate)cf.generateCertificate(is);
                }
                finally {
                    try {
                        is.close();
                    }
                    catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                ++i;
            }
            while (i < certs.length) {
                certs[i].release();
                ++i;
            }
        }
        finally {
            while (i < certs.length) {
                certs[i].release();
                ++i;
            }
        }
        return x509Certs;
    }
    
    static TrustManagerFactory buildTrustManagerFactory(final X509Certificate[] certCollection, TrustManagerFactory trustManagerFactory) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
        final KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, null);
        int i = 1;
        for (final X509Certificate cert : certCollection) {
            final String alias = Integer.toString(i);
            ks.setCertificateEntry(alias, cert);
            ++i;
        }
        if (trustManagerFactory == null) {
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        }
        trustManagerFactory.init(ks);
        return trustManagerFactory;
    }
    
    static PrivateKey toPrivateKeyInternal(final File keyFile, final String keyPassword) throws SSLException {
        try {
            return toPrivateKey(keyFile, keyPassword);
        }
        catch (final Exception e) {
            throw new SSLException(e);
        }
    }
    
    static X509Certificate[] toX509CertificatesInternal(final File file) throws SSLException {
        try {
            return toX509Certificates(file);
        }
        catch (final CertificateException e) {
            throw new SSLException(e);
        }
    }
    
    static KeyManagerFactory buildKeyManagerFactory(final X509Certificate[] certChain, final PrivateKey key, final String keyPassword, final KeyManagerFactory kmf) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }
        return buildKeyManagerFactory(certChain, algorithm, key, keyPassword, kmf);
    }
    
    static KeyManagerFactory buildKeyManagerFactory(final X509Certificate[] certChainFile, final String keyAlgorithm, final PrivateKey key, final String keyPassword, KeyManagerFactory kmf) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, UnrecoverableKeyException {
        final char[] keyPasswordChars = (keyPassword == null) ? EmptyArrays.EMPTY_CHARS : keyPassword.toCharArray();
        final KeyStore ks = buildKeyStore(certChainFile, key, keyPasswordChars);
        if (kmf == null) {
            kmf = KeyManagerFactory.getInstance(keyAlgorithm);
        }
        kmf.init(ks, keyPasswordChars);
        return kmf;
    }
    
    static {
        try {
            X509_CERT_FACTORY = CertificateFactory.getInstance("X.509");
        }
        catch (final CertificateException e) {
            throw new IllegalStateException("unable to instance X.509 CertificateFactory", e);
        }
    }
}
