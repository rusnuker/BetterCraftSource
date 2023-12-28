// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

public class Socks5PasswordAuthStatus implements Comparable<Socks5PasswordAuthStatus>
{
    public static final Socks5PasswordAuthStatus SUCCESS;
    public static final Socks5PasswordAuthStatus FAILURE;
    private final byte byteValue;
    private final String name;
    private String text;
    
    public static Socks5PasswordAuthStatus valueOf(final byte b) {
        switch (b) {
            case 0: {
                return Socks5PasswordAuthStatus.SUCCESS;
            }
            case -1: {
                return Socks5PasswordAuthStatus.FAILURE;
            }
            default: {
                return new Socks5PasswordAuthStatus(b);
            }
        }
    }
    
    public Socks5PasswordAuthStatus(final int byteValue) {
        this(byteValue, "UNKNOWN");
    }
    
    public Socks5PasswordAuthStatus(final int byteValue, final String name) {
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
        return obj instanceof Socks5PasswordAuthStatus && this.byteValue == ((Socks5PasswordAuthStatus)obj).byteValue;
    }
    
    @Override
    public int compareTo(final Socks5PasswordAuthStatus o) {
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
        SUCCESS = new Socks5PasswordAuthStatus(0, "SUCCESS");
        FAILURE = new Socks5PasswordAuthStatus(255, "FAILURE");
    }
}
