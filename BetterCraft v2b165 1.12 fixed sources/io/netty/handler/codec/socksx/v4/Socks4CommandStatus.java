// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v4;

public class Socks4CommandStatus implements Comparable<Socks4CommandStatus>
{
    public static final Socks4CommandStatus SUCCESS;
    public static final Socks4CommandStatus REJECTED_OR_FAILED;
    public static final Socks4CommandStatus IDENTD_UNREACHABLE;
    public static final Socks4CommandStatus IDENTD_AUTH_FAILURE;
    private final byte byteValue;
    private final String name;
    private String text;
    
    public static Socks4CommandStatus valueOf(final byte b) {
        switch (b) {
            case 90: {
                return Socks4CommandStatus.SUCCESS;
            }
            case 91: {
                return Socks4CommandStatus.REJECTED_OR_FAILED;
            }
            case 92: {
                return Socks4CommandStatus.IDENTD_UNREACHABLE;
            }
            case 93: {
                return Socks4CommandStatus.IDENTD_AUTH_FAILURE;
            }
            default: {
                return new Socks4CommandStatus(b);
            }
        }
    }
    
    public Socks4CommandStatus(final int byteValue) {
        this(byteValue, "UNKNOWN");
    }
    
    public Socks4CommandStatus(final int byteValue, final String name) {
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
        return this.byteValue == 90;
    }
    
    @Override
    public int hashCode() {
        return this.byteValue;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Socks4CommandStatus && this.byteValue == ((Socks4CommandStatus)obj).byteValue;
    }
    
    @Override
    public int compareTo(final Socks4CommandStatus o) {
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
        SUCCESS = new Socks4CommandStatus(90, "SUCCESS");
        REJECTED_OR_FAILED = new Socks4CommandStatus(91, "REJECTED_OR_FAILED");
        IDENTD_UNREACHABLE = new Socks4CommandStatus(92, "IDENTD_UNREACHABLE");
        IDENTD_AUTH_FAILURE = new Socks4CommandStatus(93, "IDENTD_AUTH_FAILURE");
    }
}
