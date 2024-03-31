// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

public class Socks5CommandStatus implements Comparable<Socks5CommandStatus>
{
    public static final Socks5CommandStatus SUCCESS;
    public static final Socks5CommandStatus FAILURE;
    public static final Socks5CommandStatus FORBIDDEN;
    public static final Socks5CommandStatus NETWORK_UNREACHABLE;
    public static final Socks5CommandStatus HOST_UNREACHABLE;
    public static final Socks5CommandStatus CONNECTION_REFUSED;
    public static final Socks5CommandStatus TTL_EXPIRED;
    public static final Socks5CommandStatus COMMAND_UNSUPPORTED;
    public static final Socks5CommandStatus ADDRESS_UNSUPPORTED;
    private final byte byteValue;
    private final String name;
    private String text;
    
    public static Socks5CommandStatus valueOf(final byte b) {
        switch (b) {
            case 0: {
                return Socks5CommandStatus.SUCCESS;
            }
            case 1: {
                return Socks5CommandStatus.FAILURE;
            }
            case 2: {
                return Socks5CommandStatus.FORBIDDEN;
            }
            case 3: {
                return Socks5CommandStatus.NETWORK_UNREACHABLE;
            }
            case 4: {
                return Socks5CommandStatus.HOST_UNREACHABLE;
            }
            case 5: {
                return Socks5CommandStatus.CONNECTION_REFUSED;
            }
            case 6: {
                return Socks5CommandStatus.TTL_EXPIRED;
            }
            case 7: {
                return Socks5CommandStatus.COMMAND_UNSUPPORTED;
            }
            case 8: {
                return Socks5CommandStatus.ADDRESS_UNSUPPORTED;
            }
            default: {
                return new Socks5CommandStatus(b);
            }
        }
    }
    
    public Socks5CommandStatus(final int byteValue) {
        this(byteValue, "UNKNOWN");
    }
    
    public Socks5CommandStatus(final int byteValue, final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.byteValue = (byte)byteValue;
        this.name = name;
    }
    
    public byte byteValue() {
        return this.byteValue;
    }
    
    public boolean isSuccess() {
        return this.byteValue == 0;
    }
    
    @Override
    public int hashCode() {
        return this.byteValue;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Socks5CommandStatus && this.byteValue == ((Socks5CommandStatus)obj).byteValue;
    }
    
    @Override
    public int compareTo(final Socks5CommandStatus o) {
        return this.byteValue - o.byteValue;
    }
    
    @Override
    public String toString() {
        String text = this.text;
        if (text == null) {
            text = (this.text = this.name + '(' + (this.byteValue & 0xFF) + ')');
        }
        return text;
    }
    
    static {
        SUCCESS = new Socks5CommandStatus(0, "SUCCESS");
        FAILURE = new Socks5CommandStatus(1, "FAILURE");
        FORBIDDEN = new Socks5CommandStatus(2, "FORBIDDEN");
        NETWORK_UNREACHABLE = new Socks5CommandStatus(3, "NETWORK_UNREACHABLE");
        HOST_UNREACHABLE = new Socks5CommandStatus(4, "HOST_UNREACHABLE");
        CONNECTION_REFUSED = new Socks5CommandStatus(5, "CONNECTION_REFUSED");
        TTL_EXPIRED = new Socks5CommandStatus(6, "TTL_EXPIRED");
        COMMAND_UNSUPPORTED = new Socks5CommandStatus(7, "COMMAND_UNSUPPORTED");
        ADDRESS_UNSUPPORTED = new Socks5CommandStatus(8, "ADDRESS_UNSUPPORTED");
    }
}
