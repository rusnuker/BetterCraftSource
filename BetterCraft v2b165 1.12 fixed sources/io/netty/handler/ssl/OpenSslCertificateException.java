// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.internal.tcnative.CertificateVerifier;
import java.security.cert.CertificateException;

public final class OpenSslCertificateException extends CertificateException
{
    private static final long serialVersionUID = 5542675253797129798L;
    private final int errorCode;
    
    public OpenSslCertificateException(final int errorCode) {
        this((String)null, errorCode);
    }
    
    public OpenSslCertificateException(final String msg, final int errorCode) {
        super(msg);
        this.errorCode = checkErrorCode(errorCode);
    }
    
    public OpenSslCertificateException(final String message, final Throwable cause, final int errorCode) {
        super(message, cause);
        this.errorCode = checkErrorCode(errorCode);
    }
    
    public OpenSslCertificateException(final Throwable cause, final int errorCode) {
        this(null, cause, errorCode);
    }
    
    public int errorCode() {
        return this.errorCode;
    }
    
    private static int checkErrorCode(final int errorCode) {
        if (errorCode < CertificateVerifier.X509_V_OK || errorCode > CertificateVerifier.X509_V_ERR_DANE_NO_MATCH) {
            throw new IllegalArgumentException("errorCode must be " + CertificateVerifier.X509_V_OK + " => " + CertificateVerifier.X509_V_ERR_DANE_NO_MATCH + ". See https://www.openssl.org/docs/manmaster/apps/verify.html .");
        }
        return errorCode;
    }
}
