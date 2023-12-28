// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.internal.ObjectUtil;

public class DnsOpCode implements Comparable<DnsOpCode>
{
    public static final DnsOpCode QUERY;
    public static final DnsOpCode IQUERY;
    public static final DnsOpCode STATUS;
    public static final DnsOpCode NOTIFY;
    public static final DnsOpCode UPDATE;
    private final byte byteValue;
    private final String name;
    private String text;
    
    public static DnsOpCode valueOf(final int b) {
        switch (b) {
            case 0: {
                return DnsOpCode.QUERY;
            }
            case 1: {
                return DnsOpCode.IQUERY;
            }
            case 2: {
                return DnsOpCode.STATUS;
            }
            case 4: {
                return DnsOpCode.NOTIFY;
            }
            case 5: {
                return DnsOpCode.UPDATE;
            }
            default: {
                return new DnsOpCode(b);
            }
        }
    }
    
    private DnsOpCode(final int byteValue) {
        this(byteValue, "UNKNOWN");
    }
    
    public DnsOpCode(final int byteValue, final String name) {
        this.byteValue = (byte)byteValue;
        this.name = ObjectUtil.checkNotNull(name, "name");
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
        return this == obj || (obj instanceof DnsOpCode && this.byteValue == ((DnsOpCode)obj).byteValue);
    }
    
    @Override
    public int compareTo(final DnsOpCode o) {
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
        QUERY = new DnsOpCode(0, "QUERY");
        IQUERY = new DnsOpCode(1, "IQUERY");
        STATUS = new DnsOpCode(2, "STATUS");
        NOTIFY = new DnsOpCode(4, "NOTIFY");
        UPDATE = new DnsOpCode(5, "UPDATE");
    }
}
