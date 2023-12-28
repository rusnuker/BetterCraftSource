// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v4;

public class Socks4CommandType implements Comparable<Socks4CommandType>
{
    public static final Socks4CommandType CONNECT;
    public static final Socks4CommandType BIND;
    private final byte byteValue;
    private final String name;
    private String text;
    
    public static Socks4CommandType valueOf(final byte b) {
        switch (b) {
            case 1: {
                return Socks4CommandType.CONNECT;
            }
            case 2: {
                return Socks4CommandType.BIND;
            }
            default: {
                return new Socks4CommandType(b);
            }
        }
    }
    
    public Socks4CommandType(final int byteValue) {
        this(byteValue, "UNKNOWN");
    }
    
    public Socks4CommandType(final int byteValue, final String name) {
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
        return obj instanceof Socks4CommandType && this.byteValue == ((Socks4CommandType)obj).byteValue;
    }
    
    @Override
    public int compareTo(final Socks4CommandType o) {
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
        CONNECT = new Socks4CommandType(1, "CONNECT");
        BIND = new Socks4CommandType(2, "BIND");
    }
}
