// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

public class Socks5AuthMethod implements Comparable<Socks5AuthMethod>
{
    public static final Socks5AuthMethod NO_AUTH;
    public static final Socks5AuthMethod GSSAPI;
    public static final Socks5AuthMethod PASSWORD;
    public static final Socks5AuthMethod UNACCEPTED;
    private final byte byteValue;
    private final String name;
    private String text;
    
    public static Socks5AuthMethod valueOf(final byte b) {
        switch (b) {
            case 0: {
                return Socks5AuthMethod.NO_AUTH;
            }
            case 1: {
                return Socks5AuthMethod.GSSAPI;
            }
            case 2: {
                return Socks5AuthMethod.PASSWORD;
            }
            case -1: {
                return Socks5AuthMethod.UNACCEPTED;
            }
            default: {
                return new Socks5AuthMethod(b);
            }
        }
    }
    
    public Socks5AuthMethod(final int byteValue) {
        this(byteValue, "UNKNOWN");
    }
    
    public Socks5AuthMethod(final int byteValue, final String name) {
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
        return obj instanceof Socks5AuthMethod && this.byteValue == ((Socks5AuthMethod)obj).byteValue;
    }
    
    @Override
    public int compareTo(final Socks5AuthMethod o) {
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
        NO_AUTH = new Socks5AuthMethod(0, "NO_AUTH");
        GSSAPI = new Socks5AuthMethod(1, "GSSAPI");
        PASSWORD = new Socks5AuthMethod(2, "PASSWORD");
        UNACCEPTED = new Socks5AuthMethod(255, "UNACCEPTED");
    }
}
