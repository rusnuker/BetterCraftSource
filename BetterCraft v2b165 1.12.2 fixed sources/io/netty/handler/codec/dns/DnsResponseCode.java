// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.internal.ObjectUtil;

public class DnsResponseCode implements Comparable<DnsResponseCode>
{
    public static final DnsResponseCode NOERROR;
    public static final DnsResponseCode FORMERR;
    public static final DnsResponseCode SERVFAIL;
    public static final DnsResponseCode NXDOMAIN;
    public static final DnsResponseCode NOTIMP;
    public static final DnsResponseCode REFUSED;
    public static final DnsResponseCode YXDOMAIN;
    public static final DnsResponseCode YXRRSET;
    public static final DnsResponseCode NXRRSET;
    public static final DnsResponseCode NOTAUTH;
    public static final DnsResponseCode NOTZONE;
    public static final DnsResponseCode BADVERS_OR_BADSIG;
    public static final DnsResponseCode BADKEY;
    public static final DnsResponseCode BADTIME;
    public static final DnsResponseCode BADMODE;
    public static final DnsResponseCode BADNAME;
    public static final DnsResponseCode BADALG;
    private final int code;
    private final String name;
    private String text;
    
    public static DnsResponseCode valueOf(final int responseCode) {
        switch (responseCode) {
            case 0: {
                return DnsResponseCode.NOERROR;
            }
            case 1: {
                return DnsResponseCode.FORMERR;
            }
            case 2: {
                return DnsResponseCode.SERVFAIL;
            }
            case 3: {
                return DnsResponseCode.NXDOMAIN;
            }
            case 4: {
                return DnsResponseCode.NOTIMP;
            }
            case 5: {
                return DnsResponseCode.REFUSED;
            }
            case 6: {
                return DnsResponseCode.YXDOMAIN;
            }
            case 7: {
                return DnsResponseCode.YXRRSET;
            }
            case 8: {
                return DnsResponseCode.NXRRSET;
            }
            case 9: {
                return DnsResponseCode.NOTAUTH;
            }
            case 10: {
                return DnsResponseCode.NOTZONE;
            }
            case 16: {
                return DnsResponseCode.BADVERS_OR_BADSIG;
            }
            case 17: {
                return DnsResponseCode.BADKEY;
            }
            case 18: {
                return DnsResponseCode.BADTIME;
            }
            case 19: {
                return DnsResponseCode.BADMODE;
            }
            case 20: {
                return DnsResponseCode.BADNAME;
            }
            case 21: {
                return DnsResponseCode.BADALG;
            }
            default: {
                return new DnsResponseCode(responseCode);
            }
        }
    }
    
    private DnsResponseCode(final int code) {
        this(code, "UNKNOWN");
    }
    
    public DnsResponseCode(final int code, final String name) {
        if (code < 0 || code > 65535) {
            throw new IllegalArgumentException("code: " + code + " (expected: 0 ~ 65535)");
        }
        this.code = code;
        this.name = ObjectUtil.checkNotNull(name, "name");
    }
    
    public int intValue() {
        return this.code;
    }
    
    @Override
    public int compareTo(final DnsResponseCode o) {
        return this.intValue() - o.intValue();
    }
    
    @Override
    public int hashCode() {
        return this.intValue();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof DnsResponseCode && this.intValue() == ((DnsResponseCode)o).intValue();
    }
    
    @Override
    public String toString() {
        String text = this.text;
        if (text == null) {
            text = (this.text = this.name + '(' + this.intValue() + ')');
        }
        return text;
    }
    
    static {
        NOERROR = new DnsResponseCode(0, "NoError");
        FORMERR = new DnsResponseCode(1, "FormErr");
        SERVFAIL = new DnsResponseCode(2, "ServFail");
        NXDOMAIN = new DnsResponseCode(3, "NXDomain");
        NOTIMP = new DnsResponseCode(4, "NotImp");
        REFUSED = new DnsResponseCode(5, "Refused");
        YXDOMAIN = new DnsResponseCode(6, "YXDomain");
        YXRRSET = new DnsResponseCode(7, "YXRRSet");
        NXRRSET = new DnsResponseCode(8, "NXRRSet");
        NOTAUTH = new DnsResponseCode(9, "NotAuth");
        NOTZONE = new DnsResponseCode(10, "NotZone");
        BADVERS_OR_BADSIG = new DnsResponseCode(16, "BADVERS_OR_BADSIG");
        BADKEY = new DnsResponseCode(17, "BADKEY");
        BADTIME = new DnsResponseCode(18, "BADTIME");
        BADMODE = new DnsResponseCode(19, "BADMODE");
        BADNAME = new DnsResponseCode(20, "BADNAME");
        BADALG = new DnsResponseCode(21, "BADALG");
    }
}
