// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.haproxy;

public enum HAProxyProtocolVersion
{
    V1((byte)16), 
    V2((byte)32);
    
    private static final byte VERSION_MASK = -16;
    private final byte byteValue;
    
    private HAProxyProtocolVersion(final byte byteValue) {
        this.byteValue = byteValue;
    }
    
    public static HAProxyProtocolVersion valueOf(final byte verCmdByte) {
        final int version = verCmdByte & 0xFFFFFFF0;
        switch ((byte)version) {
            case 32: {
                return HAProxyProtocolVersion.V2;
            }
            case 16: {
                return HAProxyProtocolVersion.V1;
            }
            default: {
                throw new IllegalArgumentException("unknown version: " + version);
            }
        }
    }
    
    public byte byteValue() {
        return this.byteValue;
    }
}
