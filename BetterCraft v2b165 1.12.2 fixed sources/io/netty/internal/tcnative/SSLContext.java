// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.internal.tcnative;

public final class SSLContext
{
    private SSLContext() {
    }
    
    public static native long make(final int p0, final int p1) throws Exception;
    
    public static native int free(final long p0);
    
    public static native void setContextId(final long p0, final String p1);
    
    public static native void setOptions(final long p0, final int p1);
    
    public static native int getOptions(final long p0);
    
    public static native void clearOptions(final long p0, final int p1);
    
    public static native boolean setCipherSuite(final long p0, final String p1) throws Exception;
    
    public static native boolean setCertificateChainFile(final long p0, final String p1, final boolean p2);
    
    public static native boolean setCertificateChainBio(final long p0, final long p1, final boolean p2);
    
    public static native boolean setCertificate(final long p0, final String p1, final String p2, final String p3) throws Exception;
    
    public static native boolean setCertificateBio(final long p0, final long p1, final long p2, final String p3) throws Exception;
    
    public static native long setSessionCacheSize(final long p0, final long p1);
    
    public static native long getSessionCacheSize(final long p0);
    
    public static native long setSessionCacheTimeout(final long p0, final long p1);
    
    public static native long getSessionCacheTimeout(final long p0);
    
    public static native long setSessionCacheMode(final long p0, final long p1);
    
    public static native long getSessionCacheMode(final long p0);
    
    public static native long sessionAccept(final long p0);
    
    public static native long sessionAcceptGood(final long p0);
    
    public static native long sessionAcceptRenegotiate(final long p0);
    
    public static native long sessionCacheFull(final long p0);
    
    public static native long sessionCbHits(final long p0);
    
    public static native long sessionConnect(final long p0);
    
    public static native long sessionConnectGood(final long p0);
    
    public static native long sessionConnectRenegotiate(final long p0);
    
    public static native long sessionHits(final long p0);
    
    public static native long sessionMisses(final long p0);
    
    public static native long sessionNumber(final long p0);
    
    public static native long sessionTimeouts(final long p0);
    
    public static native long sessionTicketKeyNew(final long p0);
    
    public static native long sessionTicketKeyResume(final long p0);
    
    public static native long sessionTicketKeyRenew(final long p0);
    
    public static native long sessionTicketKeyFail(final long p0);
    
    public static void setSessionTicketKeys(final long ctx, final SessionTicketKey[] keys) {
        if (keys == null || keys.length == 0) {
            throw new IllegalArgumentException("Length of the keys should be longer than 0.");
        }
        final byte[] binaryKeys = new byte[keys.length * 48];
        for (int i = 0; i < keys.length; ++i) {
            final SessionTicketKey key = keys[i];
            int dstCurPos = 48 * i;
            System.arraycopy(key.name, 0, binaryKeys, dstCurPos, 16);
            dstCurPos += 16;
            System.arraycopy(key.hmacKey, 0, binaryKeys, dstCurPos, 16);
            dstCurPos += 16;
            System.arraycopy(key.aesKey, 0, binaryKeys, dstCurPos, 16);
        }
        setSessionTicketKeys0(ctx, binaryKeys);
    }
    
    private static native void setSessionTicketKeys0(final long p0, final byte[] p1);
    
    public static native boolean setCACertificateBio(final long p0, final long p1);
    
    public static native void setVerify(final long p0, final int p1, final int p2);
    
    public static native void setCertVerifyCallback(final long p0, final CertificateVerifier p1);
    
    public static native void setCertRequestedCallback(final long p0, final CertificateRequestedCallback p1);
    
    public static native void setNpnProtos(final long p0, final String[] p1, final int p2);
    
    public static native void setAlpnProtos(final long p0, final String[] p1, final int p2);
    
    public static native void setTmpDHLength(final long p0, final int p1);
    
    public static native boolean setSessionIdContext(final long p0, final byte[] p1);
    
    public static native int setMode(final long p0, final int p1);
    
    public static native int getMode(final long p0);
}
