// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.internal.tcnative;

public final class SSL
{
    public static final int SSL_PROTOCOL_NONE = 0;
    public static final int SSL_PROTOCOL_SSLV2 = 1;
    public static final int SSL_PROTOCOL_SSLV3 = 2;
    public static final int SSL_PROTOCOL_TLSV1 = 4;
    public static final int SSL_PROTOCOL_TLSV1_1 = 8;
    public static final int SSL_PROTOCOL_TLSV1_2 = 16;
    public static final int SSL_PROTOCOL_TLS = 30;
    public static final int SSL_PROTOCOL_ALL = 31;
    public static final int SSL_CVERIFY_IGNORED = -1;
    public static final int SSL_CVERIFY_NONE = 0;
    public static final int SSL_CVERIFY_OPTIONAL = 1;
    public static final int SSL_CVERIFY_REQUIRED = 2;
    public static final int SSL_OP_CIPHER_SERVER_PREFERENCE;
    public static final int SSL_OP_NO_SSLv2;
    public static final int SSL_OP_NO_SSLv3;
    public static final int SSL_OP_NO_TLSv1;
    public static final int SSL_OP_NO_TLSv1_1;
    public static final int SSL_OP_NO_TLSv1_2;
    public static final int SSL_OP_NO_TICKET;
    public static final int SSL_OP_NO_COMPRESSION;
    public static final int SSL_MODE_CLIENT = 0;
    public static final int SSL_MODE_SERVER = 1;
    public static final int SSL_MODE_COMBINED = 2;
    public static final long SSL_SESS_CACHE_OFF;
    public static final long SSL_SESS_CACHE_SERVER;
    public static final int SSL_SELECTOR_FAILURE_NO_ADVERTISE = 0;
    public static final int SSL_SELECTOR_FAILURE_CHOOSE_MY_LAST_PROTOCOL = 1;
    public static final int SSL_ST_CONNECT;
    public static final int SSL_ST_ACCEPT;
    public static final int SSL_MODE_ENABLE_PARTIAL_WRITE;
    public static final int SSL_MODE_ACCEPT_MOVING_WRITE_BUFFER;
    public static final int SSL_MODE_RELEASE_BUFFERS;
    public static final int X509_CHECK_FLAG_ALWAYS_CHECK_SUBJECT;
    public static final int X509_CHECK_FLAG_NO_WILD_CARDS;
    public static final int X509_CHECK_FLAG_NO_PARTIAL_WILD_CARDS;
    public static final int X509_CHECK_FLAG_MULTI_LABEL_WILDCARDS;
    public static final int SSL_SENT_SHUTDOWN;
    public static final int SSL_RECEIVED_SHUTDOWN;
    public static final int SSL_ERROR_NONE;
    public static final int SSL_ERROR_SSL;
    public static final int SSL_ERROR_WANT_READ;
    public static final int SSL_ERROR_WANT_WRITE;
    public static final int SSL_ERROR_WANT_X509_LOOKUP;
    public static final int SSL_ERROR_SYSCALL;
    public static final int SSL_ERROR_ZERO_RETURN;
    public static final int SSL_ERROR_WANT_CONNECT;
    public static final int SSL_ERROR_WANT_ACCEPT;
    
    private SSL() {
    }
    
    public static native int version();
    
    public static native String versionString();
    
    static native int initialize(final String p0);
    
    public static native long newMemBIO() throws Exception;
    
    public static native String getLastError();
    
    public static native boolean hasOp(final int p0);
    
    public static native long newSSL(final long p0, final boolean p1);
    
    public static native int getError(final long p0, final int p1);
    
    public static native int bioWrite(final long p0, final long p1, final int p2);
    
    public static native long bioNewByteBuffer(final long p0, final int p1);
    
    public static native void bioSetByteBuffer(final long p0, final long p1, final int p2, final boolean p3);
    
    public static native void bioClearByteBuffer(final long p0);
    
    public static native int bioFlushByteBuffer(final long p0);
    
    public static native int bioLengthByteBuffer(final long p0);
    
    public static native int bioLengthNonApplication(final long p0);
    
    public static native int writeToSSL(final long p0, final long p1, final int p2);
    
    public static native int readFromSSL(final long p0, final long p1, final int p2);
    
    public static native int getShutdown(final long p0);
    
    public static native void setShutdown(final long p0, final int p1);
    
    public static native void freeSSL(final long p0);
    
    public static native void freeBIO(final long p0);
    
    public static native int shutdownSSL(final long p0);
    
    public static native int getLastErrorNumber();
    
    public static native String getCipherForSSL(final long p0);
    
    public static native String getVersion(final long p0);
    
    public static native int doHandshake(final long p0);
    
    public static native int isInInit(final long p0);
    
    public static native String getNextProtoNegotiated(final long p0);
    
    public static native String getAlpnSelected(final long p0);
    
    public static native byte[][] getPeerCertChain(final long p0);
    
    public static native byte[] getPeerCertificate(final long p0);
    
    public static native String getErrorString(final long p0);
    
    public static native long getTime(final long p0);
    
    public static native long getTimeout(final long p0);
    
    public static native long setTimeout(final long p0, final long p1);
    
    public static native void setVerify(final long p0, final int p1, final int p2);
    
    public static native void setOptions(final long p0, final int p1);
    
    public static native void clearOptions(final long p0, final int p1);
    
    public static native int getOptions(final long p0);
    
    public static native String[] getCiphers(final long p0);
    
    public static native boolean setCipherSuites(final long p0, final String p1) throws Exception;
    
    public static native byte[] getSessionId(final long p0);
    
    public static native int getHandshakeCount(final long p0);
    
    public static native void clearError();
    
    public static native int renegotiate(final long p0);
    
    public static native void setState(final long p0, final int p1);
    
    public static native void setTlsExtHostName(final long p0, final String p1);
    
    public static native void setHostNameValidation(final long p0, final int p1, final String p2);
    
    public static native String[] authenticationMethods(final long p0);
    
    public static native void setCertificateChainBio(final long p0, final long p1, final boolean p2);
    
    public static native void setCertificateBio(final long p0, final long p1, final long p2, final String p3) throws Exception;
    
    public static native long parsePrivateKey(final long p0, final String p1) throws Exception;
    
    public static native void freePrivateKey(final long p0);
    
    public static native long parseX509Chain(final long p0) throws Exception;
    
    public static native void freeX509Chain(final long p0);
    
    static {
        SSL_OP_CIPHER_SERVER_PREFERENCE = NativeStaticallyReferencedJniMethods.sslOpCipherServerPreference();
        SSL_OP_NO_SSLv2 = NativeStaticallyReferencedJniMethods.sslOpNoSSLv2();
        SSL_OP_NO_SSLv3 = NativeStaticallyReferencedJniMethods.sslOpNoSSLv3();
        SSL_OP_NO_TLSv1 = NativeStaticallyReferencedJniMethods.sslOpNoTLSv1();
        SSL_OP_NO_TLSv1_1 = NativeStaticallyReferencedJniMethods.sslOpNoTLSv11();
        SSL_OP_NO_TLSv1_2 = NativeStaticallyReferencedJniMethods.sslOpNoTLSv12();
        SSL_OP_NO_TICKET = NativeStaticallyReferencedJniMethods.sslOpNoTicket();
        SSL_OP_NO_COMPRESSION = NativeStaticallyReferencedJniMethods.sslOpNoCompression();
        SSL_SESS_CACHE_OFF = NativeStaticallyReferencedJniMethods.sslSessCacheOff();
        SSL_SESS_CACHE_SERVER = NativeStaticallyReferencedJniMethods.sslSessCacheServer();
        SSL_ST_CONNECT = NativeStaticallyReferencedJniMethods.sslStConnect();
        SSL_ST_ACCEPT = NativeStaticallyReferencedJniMethods.sslStAccept();
        SSL_MODE_ENABLE_PARTIAL_WRITE = NativeStaticallyReferencedJniMethods.sslModeEnablePartialWrite();
        SSL_MODE_ACCEPT_MOVING_WRITE_BUFFER = NativeStaticallyReferencedJniMethods.sslModeAcceptMovingWriteBuffer();
        SSL_MODE_RELEASE_BUFFERS = NativeStaticallyReferencedJniMethods.sslModeReleaseBuffers();
        X509_CHECK_FLAG_ALWAYS_CHECK_SUBJECT = NativeStaticallyReferencedJniMethods.x509CheckFlagAlwaysCheckSubject();
        X509_CHECK_FLAG_NO_WILD_CARDS = NativeStaticallyReferencedJniMethods.x509CheckFlagDisableWildCards();
        X509_CHECK_FLAG_NO_PARTIAL_WILD_CARDS = NativeStaticallyReferencedJniMethods.x509CheckFlagNoPartialWildCards();
        X509_CHECK_FLAG_MULTI_LABEL_WILDCARDS = NativeStaticallyReferencedJniMethods.x509CheckFlagMultiLabelWildCards();
        SSL_SENT_SHUTDOWN = NativeStaticallyReferencedJniMethods.sslSendShutdown();
        SSL_RECEIVED_SHUTDOWN = NativeStaticallyReferencedJniMethods.sslReceivedShutdown();
        SSL_ERROR_NONE = NativeStaticallyReferencedJniMethods.sslErrorNone();
        SSL_ERROR_SSL = NativeStaticallyReferencedJniMethods.sslErrorSSL();
        SSL_ERROR_WANT_READ = NativeStaticallyReferencedJniMethods.sslErrorWantRead();
        SSL_ERROR_WANT_WRITE = NativeStaticallyReferencedJniMethods.sslErrorWantWrite();
        SSL_ERROR_WANT_X509_LOOKUP = NativeStaticallyReferencedJniMethods.sslErrorWantX509Lookup();
        SSL_ERROR_SYSCALL = NativeStaticallyReferencedJniMethods.sslErrorSyscall();
        SSL_ERROR_ZERO_RETURN = NativeStaticallyReferencedJniMethods.sslErrorZeroReturn();
        SSL_ERROR_WANT_CONNECT = NativeStaticallyReferencedJniMethods.sslErrorWantConnect();
        SSL_ERROR_WANT_ACCEPT = NativeStaticallyReferencedJniMethods.sslErrorWantAccept();
    }
}
