// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl.util;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.OutputStream;
import io.netty.buffer.ByteBuf;
import java.io.FileOutputStream;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.base64.Base64;
import io.netty.buffer.Unpooled;
import java.security.KeyPair;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.io.File;
import java.util.Date;
import io.netty.util.internal.logging.InternalLogger;

public final class SelfSignedCertificate
{
    private static final InternalLogger logger;
    private static final Date DEFAULT_NOT_BEFORE;
    private static final Date DEFAULT_NOT_AFTER;
    private final File certificate;
    private final File privateKey;
    private final X509Certificate cert;
    private final PrivateKey key;
    
    public SelfSignedCertificate() throws CertificateException {
        this(SelfSignedCertificate.DEFAULT_NOT_BEFORE, SelfSignedCertificate.DEFAULT_NOT_AFTER);
    }
    
    public SelfSignedCertificate(final Date notBefore, final Date notAfter) throws CertificateException {
        this("example.com", notBefore, notAfter);
    }
    
    public SelfSignedCertificate(final String fqdn) throws CertificateException {
        this(fqdn, SelfSignedCertificate.DEFAULT_NOT_BEFORE, SelfSignedCertificate.DEFAULT_NOT_AFTER);
    }
    
    public SelfSignedCertificate(final String fqdn, final Date notBefore, final Date notAfter) throws CertificateException {
        this(fqdn, ThreadLocalInsecureRandom.current(), 1024, notBefore, notAfter);
    }
    
    public SelfSignedCertificate(final String fqdn, final SecureRandom random, final int bits) throws CertificateException {
        this(fqdn, random, bits, SelfSignedCertificate.DEFAULT_NOT_BEFORE, SelfSignedCertificate.DEFAULT_NOT_AFTER);
    }
    
    public SelfSignedCertificate(final String fqdn, final SecureRandom random, final int bits, final Date notBefore, final Date notAfter) throws CertificateException {
        KeyPair keypair;
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(bits, random);
            keypair = keyGen.generateKeyPair();
        }
        catch (final NoSuchAlgorithmException e) {
            throw new Error(e);
        }
        String[] paths;
        try {
            paths = OpenJdkSelfSignedCertGenerator.generate(fqdn, keypair, random, notBefore, notAfter);
        }
        catch (final Throwable t) {
            SelfSignedCertificate.logger.debug("Failed to generate a self-signed X.509 certificate using sun.security.x509:", t);
            try {
                paths = BouncyCastleSelfSignedCertGenerator.generate(fqdn, keypair, random, notBefore, notAfter);
            }
            catch (final Throwable t2) {
                SelfSignedCertificate.logger.debug("Failed to generate a self-signed X.509 certificate using Bouncy Castle:", t2);
                throw new CertificateException("No provider succeeded to generate a self-signed certificate. See debug log for the root cause.");
            }
        }
        this.certificate = new File(paths[0]);
        this.privateKey = new File(paths[1]);
        this.key = keypair.getPrivate();
        FileInputStream certificateInput = null;
        try {
            certificateInput = new FileInputStream(this.certificate);
            this.cert = (X509Certificate)CertificateFactory.getInstance("X509").generateCertificate(certificateInput);
        }
        catch (final Exception e2) {
            throw new CertificateEncodingException(e2);
        }
        finally {
            if (certificateInput != null) {
                try {
                    certificateInput.close();
                }
                catch (final IOException e3) {
                    SelfSignedCertificate.logger.warn("Failed to close a file: " + this.certificate, e3);
                }
            }
        }
    }
    
    public File certificate() {
        return this.certificate;
    }
    
    public File privateKey() {
        return this.privateKey;
    }
    
    public X509Certificate cert() {
        return this.cert;
    }
    
    public PrivateKey key() {
        return this.key;
    }
    
    public void delete() {
        safeDelete(this.certificate);
        safeDelete(this.privateKey);
    }
    
    static String[] newSelfSignedCertificate(final String fqdn, final PrivateKey key, final X509Certificate cert) throws IOException, CertificateEncodingException {
        ByteBuf wrappedBuf = Unpooled.wrappedBuffer(key.getEncoded());
        String keyText;
        try {
            final ByteBuf encodedBuf = Base64.encode(wrappedBuf, true);
            try {
                keyText = "-----BEGIN PRIVATE KEY-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII) + "\n-----END PRIVATE KEY-----\n";
            }
            finally {
                encodedBuf.release();
            }
        }
        finally {
            wrappedBuf.release();
        }
        final File keyFile = File.createTempFile("keyutil_" + fqdn + '_', ".key");
        keyFile.deleteOnExit();
        OutputStream keyOut = new FileOutputStream(keyFile);
        try {
            keyOut.write(keyText.getBytes(CharsetUtil.US_ASCII));
            keyOut.close();
            keyOut = null;
        }
        finally {
            if (keyOut != null) {
                safeClose(keyFile, keyOut);
                safeDelete(keyFile);
            }
        }
        wrappedBuf = Unpooled.wrappedBuffer(cert.getEncoded());
        String certText;
        try {
            final ByteBuf encodedBuf = Base64.encode(wrappedBuf, true);
            try {
                certText = "-----BEGIN CERTIFICATE-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII) + "\n-----END CERTIFICATE-----\n";
            }
            finally {
                encodedBuf.release();
            }
        }
        finally {
            wrappedBuf.release();
        }
        final File certFile = File.createTempFile("keyutil_" + fqdn + '_', ".crt");
        certFile.deleteOnExit();
        OutputStream certOut = new FileOutputStream(certFile);
        try {
            certOut.write(certText.getBytes(CharsetUtil.US_ASCII));
            certOut.close();
            certOut = null;
        }
        finally {
            if (certOut != null) {
                safeClose(certFile, certOut);
                safeDelete(certFile);
                safeDelete(keyFile);
            }
        }
        return new String[] { certFile.getPath(), keyFile.getPath() };
    }
    
    private static void safeDelete(final File certFile) {
        if (!certFile.delete()) {
            SelfSignedCertificate.logger.warn("Failed to delete a file: " + certFile);
        }
    }
    
    private static void safeClose(final File keyFile, final OutputStream keyOut) {
        try {
            keyOut.close();
        }
        catch (final IOException e) {
            SelfSignedCertificate.logger.warn("Failed to close a file: " + keyFile, e);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(SelfSignedCertificate.class);
        DEFAULT_NOT_BEFORE = new Date(SystemPropertyUtil.getLong("io.netty.selfSignedCertificate.defaultNotBefore", System.currentTimeMillis() - 31536000000L));
        DEFAULT_NOT_AFTER = new Date(SystemPropertyUtil.getLong("io.netty.selfSignedCertificate.defaultNotAfter", 253402300799000L));
    }
}
