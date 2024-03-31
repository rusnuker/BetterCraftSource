// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import java.util.Arrays;
import io.netty.channel.socket.InternetProtocolFamily;

public final class DefaultDnsOptEcsRecord extends AbstractDnsOptPseudoRrRecord implements DnsOptEcsRecord
{
    private final int srcPrefixLength;
    private final byte[] address;
    
    public DefaultDnsOptEcsRecord(final int maxPayloadSize, final int extendedRcode, final int version, final int srcPrefixLength, final byte[] address) {
        super(maxPayloadSize, extendedRcode, version);
        this.srcPrefixLength = srcPrefixLength;
        this.address = verifyAddress(address).clone();
    }
    
    public DefaultDnsOptEcsRecord(final int maxPayloadSize, final int srcPrefixLength, final byte[] address) {
        this(maxPayloadSize, 0, 0, srcPrefixLength, address);
    }
    
    public DefaultDnsOptEcsRecord(final int maxPayloadSize, final InternetProtocolFamily protocolFamily) {
        this(maxPayloadSize, 0, 0, 0, protocolFamily.localhost().getAddress());
    }
    
    private static byte[] verifyAddress(final byte[] bytes) {
        if (bytes.length == 4 || bytes.length == 16) {
            return bytes;
        }
        throw new IllegalArgumentException("bytes.length must either 4 or 16");
    }
    
    @Override
    public int sourcePrefixLength() {
        return this.srcPrefixLength;
    }
    
    @Override
    public int scopePrefixLength() {
        return 0;
    }
    
    @Override
    public byte[] address() {
        return this.address.clone();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = this.toStringBuilder();
        sb.setLength(sb.length() - 1);
        return sb.append(" address:").append(Arrays.toString(this.address)).append(" sourcePrefixLength:").append(this.sourcePrefixLength()).append(" scopePrefixLength:").append(this.scopePrefixLength()).append(')').toString();
    }
}
