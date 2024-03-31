// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.ReferenceCounted;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import io.netty.channel.AddressedEnvelope;

public class DatagramDnsResponse extends DefaultDnsResponse implements AddressedEnvelope<DatagramDnsResponse, InetSocketAddress>
{
    private final InetSocketAddress sender;
    private final InetSocketAddress recipient;
    
    public DatagramDnsResponse(final InetSocketAddress sender, final InetSocketAddress recipient, final int id) {
        this(sender, recipient, id, DnsOpCode.QUERY, DnsResponseCode.NOERROR);
    }
    
    public DatagramDnsResponse(final InetSocketAddress sender, final InetSocketAddress recipient, final int id, final DnsOpCode opCode) {
        this(sender, recipient, id, opCode, DnsResponseCode.NOERROR);
    }
    
    public DatagramDnsResponse(final InetSocketAddress sender, final InetSocketAddress recipient, final int id, final DnsOpCode opCode, final DnsResponseCode responseCode) {
        super(id, opCode, responseCode);
        if (recipient == null && sender == null) {
            throw new NullPointerException("recipient and sender");
        }
        this.sender = sender;
        this.recipient = recipient;
    }
    
    @Override
    public DatagramDnsResponse content() {
        return this;
    }
    
    @Override
    public InetSocketAddress sender() {
        return this.sender;
    }
    
    @Override
    public InetSocketAddress recipient() {
        return this.recipient;
    }
    
    @Override
    public DatagramDnsResponse setAuthoritativeAnswer(final boolean authoritativeAnswer) {
        return (DatagramDnsResponse)super.setAuthoritativeAnswer(authoritativeAnswer);
    }
    
    @Override
    public DatagramDnsResponse setTruncated(final boolean truncated) {
        return (DatagramDnsResponse)super.setTruncated(truncated);
    }
    
    @Override
    public DatagramDnsResponse setRecursionAvailable(final boolean recursionAvailable) {
        return (DatagramDnsResponse)super.setRecursionAvailable(recursionAvailable);
    }
    
    @Override
    public DatagramDnsResponse setCode(final DnsResponseCode code) {
        return (DatagramDnsResponse)super.setCode(code);
    }
    
    @Override
    public DatagramDnsResponse setId(final int id) {
        return (DatagramDnsResponse)super.setId(id);
    }
    
    @Override
    public DatagramDnsResponse setOpCode(final DnsOpCode opCode) {
        return (DatagramDnsResponse)super.setOpCode(opCode);
    }
    
    @Override
    public DatagramDnsResponse setRecursionDesired(final boolean recursionDesired) {
        return (DatagramDnsResponse)super.setRecursionDesired(recursionDesired);
    }
    
    @Override
    public DatagramDnsResponse setZ(final int z) {
        return (DatagramDnsResponse)super.setZ(z);
    }
    
    @Override
    public DatagramDnsResponse setRecord(final DnsSection section, final DnsRecord record) {
        return (DatagramDnsResponse)super.setRecord(section, record);
    }
    
    @Override
    public DatagramDnsResponse addRecord(final DnsSection section, final DnsRecord record) {
        return (DatagramDnsResponse)super.addRecord(section, record);
    }
    
    @Override
    public DatagramDnsResponse addRecord(final DnsSection section, final int index, final DnsRecord record) {
        return (DatagramDnsResponse)super.addRecord(section, index, record);
    }
    
    @Override
    public DatagramDnsResponse clear(final DnsSection section) {
        return (DatagramDnsResponse)super.clear(section);
    }
    
    @Override
    public DatagramDnsResponse clear() {
        return (DatagramDnsResponse)super.clear();
    }
    
    @Override
    public DatagramDnsResponse touch() {
        return (DatagramDnsResponse)super.touch();
    }
    
    @Override
    public DatagramDnsResponse touch(final Object hint) {
        return (DatagramDnsResponse)super.touch(hint);
    }
    
    @Override
    public DatagramDnsResponse retain() {
        return (DatagramDnsResponse)super.retain();
    }
    
    @Override
    public DatagramDnsResponse retain(final int increment) {
        return (DatagramDnsResponse)super.retain(increment);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof AddressedEnvelope)) {
            return false;
        }
        final AddressedEnvelope<?, SocketAddress> that = (AddressedEnvelope<?, SocketAddress>)obj;
        if (this.sender() == null) {
            if (that.sender() != null) {
                return false;
            }
        }
        else if (!this.sender().equals(that.sender())) {
            return false;
        }
        if (this.recipient() == null) {
            if (that.recipient() != null) {
                return false;
            }
        }
        else if (!this.recipient().equals(that.recipient())) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        if (this.sender() != null) {
            hashCode = hashCode * 31 + this.sender().hashCode();
        }
        if (this.recipient() != null) {
            hashCode = hashCode * 31 + this.recipient().hashCode();
        }
        return hashCode;
    }
}
