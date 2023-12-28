// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.ReferenceCounted;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import io.netty.channel.AddressedEnvelope;

public class DatagramDnsQuery extends DefaultDnsQuery implements AddressedEnvelope<DatagramDnsQuery, InetSocketAddress>
{
    private final InetSocketAddress sender;
    private final InetSocketAddress recipient;
    
    public DatagramDnsQuery(final InetSocketAddress sender, final InetSocketAddress recipient, final int id) {
        this(sender, recipient, id, DnsOpCode.QUERY);
    }
    
    public DatagramDnsQuery(final InetSocketAddress sender, final InetSocketAddress recipient, final int id, final DnsOpCode opCode) {
        super(id, opCode);
        if (recipient == null && sender == null) {
            throw new NullPointerException("recipient and sender");
        }
        this.sender = sender;
        this.recipient = recipient;
    }
    
    @Override
    public DatagramDnsQuery content() {
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
    public DatagramDnsQuery setId(final int id) {
        return (DatagramDnsQuery)super.setId(id);
    }
    
    @Override
    public DatagramDnsQuery setOpCode(final DnsOpCode opCode) {
        return (DatagramDnsQuery)super.setOpCode(opCode);
    }
    
    @Override
    public DatagramDnsQuery setRecursionDesired(final boolean recursionDesired) {
        return (DatagramDnsQuery)super.setRecursionDesired(recursionDesired);
    }
    
    @Override
    public DatagramDnsQuery setZ(final int z) {
        return (DatagramDnsQuery)super.setZ(z);
    }
    
    @Override
    public DatagramDnsQuery setRecord(final DnsSection section, final DnsRecord record) {
        return (DatagramDnsQuery)super.setRecord(section, record);
    }
    
    @Override
    public DatagramDnsQuery addRecord(final DnsSection section, final DnsRecord record) {
        return (DatagramDnsQuery)super.addRecord(section, record);
    }
    
    @Override
    public DatagramDnsQuery addRecord(final DnsSection section, final int index, final DnsRecord record) {
        return (DatagramDnsQuery)super.addRecord(section, index, record);
    }
    
    @Override
    public DatagramDnsQuery clear(final DnsSection section) {
        return (DatagramDnsQuery)super.clear(section);
    }
    
    @Override
    public DatagramDnsQuery clear() {
        return (DatagramDnsQuery)super.clear();
    }
    
    @Override
    public DatagramDnsQuery touch() {
        return (DatagramDnsQuery)super.touch();
    }
    
    @Override
    public DatagramDnsQuery touch(final Object hint) {
        return (DatagramDnsQuery)super.touch(hint);
    }
    
    @Override
    public DatagramDnsQuery retain() {
        return (DatagramDnsQuery)super.retain();
    }
    
    @Override
    public DatagramDnsQuery retain(final int increment) {
        return (DatagramDnsQuery)super.retain(increment);
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
