// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

public class Socks5CommandType implements Comparable<Socks5CommandType>
{
    public static final Socks5CommandType CONNECT;
    public static final Socks5CommandType BIND;
    public static final Socks5CommandType UDP_ASSOCIATE;
    private final byte byteValue;
    private final String name;
    private String text;
    
    public static Socks5CommandType valueOf(final byte b) {
        switch (b) {
            case 1: {
                return Socks5CommandType.CONNECT;
            }
            case 2: {
                return Socks5CommandType.BIND;
            }
            case 3: {
                return Socks5CommandType.UDP_ASSOCIATE;
            }
            default: {
                return new Socks5CommandType(b);
            }
        }
    }
    
    public Socks5CommandType(final int byteValue) {
        this(byteValue, "UNKNOWN");
    }
    
    public Socks5CommandType(final int byteValue, final String name) {
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
        return obj instanceof Socks5CommandType && this.byteValue == ((Socks5CommandType)obj).byteValue;
    }
    
    @Override
    public int compareTo(final Socks5CommandType o) {
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
        CONNECT = new Socks5CommandType(1, "CONNECT");
        BIND = new Socks5CommandType(2, "BIND");
        UDP_ASSOCIATE = new Socks5CommandType(3, "UDP_ASSOCIATE");
    }
}
