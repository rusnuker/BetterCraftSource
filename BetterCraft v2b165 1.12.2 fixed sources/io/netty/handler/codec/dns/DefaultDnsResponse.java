// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.ReferenceCounted;
import io.netty.util.internal.ObjectUtil;

public class DefaultDnsResponse extends AbstractDnsMessage implements DnsResponse
{
    private boolean authoritativeAnswer;
    private boolean truncated;
    private boolean recursionAvailable;
    private DnsResponseCode code;
    
    public DefaultDnsResponse(final int id) {
        this(id, DnsOpCode.QUERY, DnsResponseCode.NOERROR);
    }
    
    public DefaultDnsResponse(final int id, final DnsOpCode opCode) {
        this(id, opCode, DnsResponseCode.NOERROR);
    }
    
    public DefaultDnsResponse(final int id, final DnsOpCode opCode, final DnsResponseCode code) {
        super(id, opCode);
        this.setCode(code);
    }
    
    @Override
    public boolean isAuthoritativeAnswer() {
        return this.authoritativeAnswer;
    }
    
    @Override
    public DnsResponse setAuthoritativeAnswer(final boolean authoritativeAnswer) {
        this.authoritativeAnswer = authoritativeAnswer;
        return this;
    }
    
    @Override
    public boolean isTruncated() {
        return this.truncated;
    }
    
    @Override
    public DnsResponse setTruncated(final boolean truncated) {
        this.truncated = truncated;
        return this;
    }
    
    @Override
    public boolean isRecursionAvailable() {
        return this.recursionAvailable;
    }
    
    @Override
    public DnsResponse setRecursionAvailable(final boolean recursionAvailable) {
        this.recursionAvailable = recursionAvailable;
        return this;
    }
    
    @Override
    public DnsResponseCode code() {
        return this.code;
    }
    
    @Override
    public DnsResponse setCode(final DnsResponseCode code) {
        this.code = ObjectUtil.checkNotNull(code, "code");
        return this;
    }
    
    @Override
    public DnsResponse setId(final int id) {
        return (DnsResponse)super.setId(id);
    }
    
    @Override
    public DnsResponse setOpCode(final DnsOpCode opCode) {
        return (DnsResponse)super.setOpCode(opCode);
    }
    
    @Override
    public DnsResponse setRecursionDesired(final boolean recursionDesired) {
        return (DnsResponse)super.setRecursionDesired(recursionDesired);
    }
    
    @Override
    public DnsResponse setZ(final int z) {
        return (DnsResponse)super.setZ(z);
    }
    
    @Override
    public DnsResponse setRecord(final DnsSection section, final DnsRecord record) {
        return (DnsResponse)super.setRecord(section, record);
    }
    
    @Override
    public DnsResponse addRecord(final DnsSection section, final DnsRecord record) {
        return (DnsResponse)super.addRecord(section, record);
    }
    
    @Override
    public DnsResponse addRecord(final DnsSection section, final int index, final DnsRecord record) {
        return (DnsResponse)super.addRecord(section, index, record);
    }
    
    @Override
    public DnsResponse clear(final DnsSection section) {
        return (DnsResponse)super.clear(section);
    }
    
    @Override
    public DnsResponse clear() {
        return (DnsResponse)super.clear();
    }
    
    @Override
    public DnsResponse touch() {
        return (DnsResponse)super.touch();
    }
    
    @Override
    public DnsResponse touch(final Object hint) {
        return (DnsResponse)super.touch(hint);
    }
    
    @Override
    public DnsResponse retain() {
        return (DnsResponse)super.retain();
    }
    
    @Override
    public DnsResponse retain(final int increment) {
        return (DnsResponse)super.retain(increment);
    }
    
    @Override
    public String toString() {
        return DnsMessageUtil.appendResponse(new StringBuilder(128), this).toString();
    }
}
