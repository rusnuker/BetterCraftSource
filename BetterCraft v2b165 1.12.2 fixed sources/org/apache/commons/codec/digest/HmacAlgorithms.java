// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.codec.digest;

public enum HmacAlgorithms
{
    HMAC_MD5("HmacMD5"), 
    HMAC_SHA_1("HmacSHA1"), 
    HMAC_SHA_256("HmacSHA256"), 
    HMAC_SHA_384("HmacSHA384"), 
    HMAC_SHA_512("HmacSHA512");
    
    private final String algorithm;
    
    private HmacAlgorithms(final String algorithm) {
        this.algorithm = algorithm;
    }
    
    @Override
    public String toString() {
        return this.algorithm;
    }
}
