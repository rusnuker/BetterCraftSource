// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.internal.tcnative;

public interface CertificateRequestedCallback
{
    public static final byte TLS_CT_RSA_SIGN = 1;
    public static final byte TLS_CT_DSS_SIGN = 2;
    public static final byte TLS_CT_RSA_FIXED_DH = 3;
    public static final byte TLS_CT_DSS_FIXED_DH = 4;
    public static final byte TLS_CT_ECDSA_SIGN = 64;
    public static final byte TLS_CT_RSA_FIXED_ECDH = 65;
    public static final byte TLS_CT_ECDSA_FIXED_ECDH = 66;
    
    KeyMaterial requested(final long p0, final byte[] p1, final byte[][] p2);
    
    public static class KeyMaterial
    {
        private final long certificateChain;
        private final long privateKey;
        
        public KeyMaterial(final long certificateChain, final long privateKey) {
            this.certificateChain = certificateChain;
            this.privateKey = privateKey;
        }
        
        public final long privateKey() {
            return this.privateKey;
        }
        
        public final long certificateChain() {
            return this.certificateChain;
        }
    }
}
