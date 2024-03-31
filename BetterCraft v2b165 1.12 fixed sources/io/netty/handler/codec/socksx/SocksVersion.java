// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx;

public enum SocksVersion
{
    SOCKS4a((byte)4), 
    SOCKS5((byte)5), 
    UNKNOWN((byte)(-1));
    
    private final byte b;
    
    public static SocksVersion valueOf(final byte b) {
        if (b == SocksVersion.SOCKS4a.byteValue()) {
            return SocksVersion.SOCKS4a;
        }
        if (b == SocksVersion.SOCKS5.byteValue()) {
            return SocksVersion.SOCKS5;
        }
        return SocksVersion.UNKNOWN;
    }
    
    private SocksVersion(final byte b) {
        this.b = b;
    }
    
    public byte byteValue() {
        return this.b;
    }
}
