// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Set;
import java.security.SignatureException;
import java.security.NoSuchProviderException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.Principal;
import java.math.BigInteger;
import java.util.Date;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;

final class OpenSslX509Certificate extends X509Certificate
{
    private final byte[] bytes;
    private X509Certificate wrapped;
    
    public OpenSslX509Certificate(final byte[] bytes) {
        this.bytes = bytes;
    }
    
    @Override
    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        this.unwrap().checkValidity();
    }
    
    @Override
    public void checkValidity(final Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        this.unwrap().checkValidity(date);
    }
    
    @Override
    public int getVersion() {
        return this.unwrap().getVersion();
    }
    
    @Override
    public BigInteger getSerialNumber() {
        return this.unwrap().getSerialNumber();
    }
    
    @Override
    public Principal getIssuerDN() {
        return this.unwrap().getIssuerDN();
    }
    
    @Override
    public Principal getSubjectDN() {
        return this.unwrap().getSubjectDN();
    }
    
    @Override
    public Date getNotBefore() {
        return this.unwrap().getNotBefore();
    }
    
    @Override
    public Date getNotAfter() {
        return this.unwrap().getNotAfter();
    }
    
    @Override
    public byte[] getTBSCertificate() throws CertificateEncodingException {
        return this.unwrap().getTBSCertificate();
    }
    
    @Override
    public byte[] getSignature() {
        return this.unwrap().getSignature();
    }
    
    @Override
    public String getSigAlgName() {
        return this.unwrap().getSigAlgName();
    }
    
    @Override
    public String getSigAlgOID() {
        return this.unwrap().getSigAlgOID();
    }
    
    @Override
    public byte[] getSigAlgParams() {
        return this.unwrap().getSigAlgParams();
    }
    
    @Override
    public boolean[] getIssuerUniqueID() {
        return this.unwrap().getIssuerUniqueID();
    }
    
    @Override
    public boolean[] getSubjectUniqueID() {
        return this.unwrap().getSubjectUniqueID();
    }
    
    @Override
    public boolean[] getKeyUsage() {
        return this.unwrap().getKeyUsage();
    }
    
    @Override
    public int getBasicConstraints() {
        return this.unwrap().getBasicConstraints();
    }
    
    @Override
    public byte[] getEncoded() {
        return this.bytes.clone();
    }
    
    @Override
    public void verify(final PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        this.unwrap().verify(key);
    }
    
    @Override
    public void verify(final PublicKey key, final String sigProvider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        this.unwrap().verify(key, sigProvider);
    }
    
    @Override
    public String toString() {
        return this.unwrap().toString();
    }
    
    @Override
    public PublicKey getPublicKey() {
        return this.unwrap().getPublicKey();
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        return this.unwrap().hasUnsupportedCriticalExtension();
    }
    
    @Override
    public Set<String> getCriticalExtensionOIDs() {
        return this.unwrap().getCriticalExtensionOIDs();
    }
    
    @Override
    public Set<String> getNonCriticalExtensionOIDs() {
        return this.unwrap().getNonCriticalExtensionOIDs();
    }
    
    @Override
    public byte[] getExtensionValue(final String oid) {
        return this.unwrap().getExtensionValue(oid);
    }
    
    private X509Certificate unwrap() {
        X509Certificate wrapped = this.wrapped;
        if (wrapped == null) {
            try {
                final X509Certificate wrapped2 = (X509Certificate)SslContext.X509_CERT_FACTORY.generateCertificate(new ByteArrayInputStream(this.bytes));
                this.wrapped = wrapped2;
                wrapped = wrapped2;
            }
            catch (final CertificateException e) {
                throw new IllegalStateException(e);
            }
        }
        return wrapped;
    }
}
