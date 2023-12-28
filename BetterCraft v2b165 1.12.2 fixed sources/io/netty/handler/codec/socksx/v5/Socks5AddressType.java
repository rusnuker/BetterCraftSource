// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

public class Socks5AddressType implements Comparable<Socks5AddressType>
{
    public static final Socks5AddressType IPv4;
    public static final Socks5AddressType DOMAIN;
    public static final Socks5AddressType IPv6;
    private final byte byteValue;
    private final String name;
    private String text;
    
    public static Socks5AddressType valueOf(final byte b) {
        switch (b) {
            case 1: {
                return Socks5AddressType.IPv4;
            }
            case 3: {
                return Socks5AddressType.DOMAIN;
            }
            case 4: {
                return Socks5AddressType.IPv6;
            }
            default: {
                return new Socks5AddressType(b);
            }
        }
    }
    
    public Socks5AddressType(final int byteValue) {
        this(byteValue, "UNKNOWN");
    }
    
    public Socks5AddressType(final int byteValue, final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.byteValue = (byte)byteValue;
        this.name = name;
    }
    
    public byte byteValue() {
        return this.byteValue;
    }
    
    @Override
    public int hashCode() {
        return this.byteValue;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Socks5AddressType && this.byteValue == ((Socks5AddressType)obj).byteValue;
    }
    
    @Override
    public int compareTo(final Socks5AddressType o) {
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
        IPv4 = new Socks5AddressType(1, "IPv4");
        DOMAIN = new Socks5AddressType(3, "DOMAIN");
        IPv6 = new Socks5AddressType(4, "IPv6");
    }
}
