// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.haproxy;

public enum HAProxyCommand
{
    LOCAL((byte)0), 
    PROXY((byte)1);
    
    private static final byte COMMAND_MASK = 15;
    private final byte byteValue;
    
    private HAProxyCommand(final byte byteValue) {
        this.byteValue = byteValue;
    }
    
    public static HAProxyCommand valueOf(final byte verCmdByte) {
        final int cmd = verCmdByte & 0xF;
        switch ((byte)cmd) {
            case 1: {
                return HAProxyCommand.PROXY;
            }
            case 0: {
                return HAProxyCommand.LOCAL;
            }
            default: {
                throw new IllegalArgumentException("unknown command: " + cmd);
            }
        }
    }
    
    public byte byteValue() {
        return this.byteValue;
    }
}
