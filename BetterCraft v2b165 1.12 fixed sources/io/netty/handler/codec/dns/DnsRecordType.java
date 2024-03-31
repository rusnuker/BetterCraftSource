// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import java.util.HashMap;
import io.netty.util.collection.IntObjectHashMap;
import java.util.Map;

public class DnsRecordType implements Comparable<DnsRecordType>
{
    public static final DnsRecordType A;
    public static final DnsRecordType NS;
    public static final DnsRecordType CNAME;
    public static final DnsRecordType SOA;
    public static final DnsRecordType PTR;
    public static final DnsRecordType MX;
    public static final DnsRecordType TXT;
    public static final DnsRecordType RP;
    public static final DnsRecordType AFSDB;
    public static final DnsRecordType SIG;
    public static final DnsRecordType KEY;
    public static final DnsRecordType AAAA;
    public static final DnsRecordType LOC;
    public static final DnsRecordType SRV;
    public static final DnsRecordType NAPTR;
    public static final DnsRecordType KX;
    public static final DnsRecordType CERT;
    public static final DnsRecordType DNAME;
    public static final DnsRecordType OPT;
    public static final DnsRecordType APL;
    public static final DnsRecordType DS;
    public static final DnsRecordType SSHFP;
    public static final DnsRecordType IPSECKEY;
    public static final DnsRecordType RRSIG;
    public static final DnsRecordType NSEC;
    public static final DnsRecordType DNSKEY;
    public static final DnsRecordType DHCID;
    public static final DnsRecordType NSEC3;
    public static final DnsRecordType NSEC3PARAM;
    public static final DnsRecordType TLSA;
    public static final DnsRecordType HIP;
    public static final DnsRecordType SPF;
    public static final DnsRecordType TKEY;
    public static final DnsRecordType TSIG;
    public static final DnsRecordType IXFR;
    public static final DnsRecordType AXFR;
    public static final DnsRecordType ANY;
    public static final DnsRecordType CAA;
    public static final DnsRecordType TA;
    public static final DnsRecordType DLV;
    private static final Map<String, DnsRecordType> BY_NAME;
    private static final IntObjectHashMap<DnsRecordType> BY_TYPE;
    private static final String EXPECTED;
    private final int intValue;
    private final String name;
    private String text;
    
    public static DnsRecordType valueOf(final int intValue) {
        final DnsRecordType result = DnsRecordType.BY_TYPE.get(intValue);
        if (result == null) {
            return new DnsRecordType(intValue);
        }
        return result;
    }
    
    public static DnsRecordType valueOf(final String name) {
        final DnsRecordType result = DnsRecordType.BY_NAME.get(name);
        if (result == null) {
            throw new IllegalArgumentException("name: " + name + DnsRecordType.EXPECTED);
        }
        return result;
    }
    
    private DnsRecordType(final int intValue) {
        this(intValue, "UNKNOWN");
    }
    
    public DnsRecordType(final int intValue, final String name) {
        if ((intValue & 0xFFFF) != intValue) {
            throw new IllegalArgumentException("intValue: " + intValue + " (expected: 0 ~ 65535)");
        }
        this.intValue = intValue;
        this.name = name;
    }
    
    public String name() {
        return this.name;
    }
    
    public int intValue() {
        return this.intValue;
    }
    
    @Override
    public int hashCode() {
        return this.intValue;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof DnsRecordType && ((DnsRecordType)o).intValue == this.intValue;
    }
    
    @Override
    public int compareTo(final DnsRecordType o) {
        return this.intValue() - o.intValue();
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
        A = new DnsRecordType(1, "A");
        NS = new DnsRecordType(2, "NS");
        CNAME = new DnsRecordType(5, "CNAME");
        SOA = new DnsRecordType(6, "SOA");
        PTR = new DnsRecordType(12, "PTR");
        MX = new DnsRecordType(15, "MX");
        TXT = new DnsRecordType(16, "TXT");
        RP = new DnsRecordType(17, "RP");
        AFSDB = new DnsRecordType(18, "AFSDB");
        SIG = new DnsRecordType(24, "SIG");
        KEY = new DnsRecordType(25, "KEY");
        AAAA = new DnsRecordType(28, "AAAA");
        LOC = new DnsRecordType(29, "LOC");
        SRV = new DnsRecordType(33, "SRV");
        NAPTR = new DnsRecordType(35, "NAPTR");
        KX = new DnsRecordType(36, "KX");
        CERT = new DnsRecordType(37, "CERT");
        DNAME = new DnsRecordType(39, "DNAME");
        OPT = new DnsRecordType(41, "OPT");
        APL = new DnsRecordType(42, "APL");
        DS = new DnsRecordType(43, "DS");
        SSHFP = new DnsRecordType(44, "SSHFP");
        IPSECKEY = new DnsRecordType(45, "IPSECKEY");
        RRSIG = new DnsRecordType(46, "RRSIG");
        NSEC = new DnsRecordType(47, "NSEC");
        DNSKEY = new DnsRecordType(48, "DNSKEY");
        DHCID = new DnsRecordType(49, "DHCID");
        NSEC3 = new DnsRecordType(50, "NSEC3");
        NSEC3PARAM = new DnsRecordType(51, "NSEC3PARAM");
        TLSA = new DnsRecordType(52, "TLSA");
        HIP = new DnsRecordType(55, "HIP");
        SPF = new DnsRecordType(99, "SPF");
        TKEY = new DnsRecordType(249, "TKEY");
        TSIG = new DnsRecordType(250, "TSIG");
        IXFR = new DnsRecordType(251, "IXFR");
        AXFR = new DnsRecordType(252, "AXFR");
        ANY = new DnsRecordType(255, "ANY");
        CAA = new DnsRecordType(257, "CAA");
        TA = new DnsRecordType(32768, "TA");
        DLV = new DnsRecordType(32769, "DLV");
        BY_NAME = new HashMap<String, DnsRecordType>();
        BY_TYPE = new IntObjectHashMap<DnsRecordType>();
        final DnsRecordType[] all = { DnsRecordType.A, DnsRecordType.NS, DnsRecordType.CNAME, DnsRecordType.SOA, DnsRecordType.PTR, DnsRecordType.MX, DnsRecordType.TXT, DnsRecordType.RP, DnsRecordType.AFSDB, DnsRecordType.SIG, DnsRecordType.KEY, DnsRecordType.AAAA, DnsRecordType.LOC, DnsRecordType.SRV, DnsRecordType.NAPTR, DnsRecordType.KX, DnsRecordType.CERT, DnsRecordType.DNAME, DnsRecordType.OPT, DnsRecordType.APL, DnsRecordType.DS, DnsRecordType.SSHFP, DnsRecordType.IPSECKEY, DnsRecordType.RRSIG, DnsRecordType.NSEC, DnsRecordType.DNSKEY, DnsRecordType.DHCID, DnsRecordType.NSEC3, DnsRecordType.NSEC3PARAM, DnsRecordType.TLSA, DnsRecordType.HIP, DnsRecordType.SPF, DnsRecordType.TKEY, DnsRecordType.TSIG, DnsRecordType.IXFR, DnsRecordType.AXFR, DnsRecordType.ANY, DnsRecordType.CAA, DnsRecordType.TA, DnsRecordType.DLV };
        final StringBuilder expected = new StringBuilder(512);
        expected.append(" (expected: ");
        for (final DnsRecordType type : all) {
            DnsRecordType.BY_NAME.put(type.name(), type);
            DnsRecordType.BY_TYPE.put(type.intValue(), type);
            expected.append(type.name()).append('(').append(type.intValue()).append("), ");
        }
        expected.setLength(expected.length() - 2);
        expected.append(')');
        EXPECTED = expected.toString();
    }
}
