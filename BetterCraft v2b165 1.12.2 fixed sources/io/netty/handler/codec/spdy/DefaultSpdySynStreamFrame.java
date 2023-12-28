// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdySynStreamFrame extends DefaultSpdyHeadersFrame implements SpdySynStreamFrame
{
    private int associatedStreamId;
    private byte priority;
    private boolean unidirectional;
    
    public DefaultSpdySynStreamFrame(final int streamId, final int associatedStreamId, final byte priority) {
        this(streamId, associatedStreamId, priority, true);
    }
    
    public DefaultSpdySynStreamFrame(final int streamId, final int associatedStreamId, final byte priority, final boolean validateHeaders) {
        super(streamId, validateHeaders);
        this.setAssociatedStreamId(associatedStreamId);
        this.setPriority(priority);
    }
    
    @Override
    public SpdySynStreamFrame setStreamId(final int streamId) {
        super.setStreamId(streamId);
        return this;
    }
    
    @Override
    public SpdySynStreamFrame setLast(final boolean last) {
        super.setLast(last);
        return this;
    }
    
    @Override
    public SpdySynStreamFrame setInvalid() {
        super.setInvalid();
        return this;
    }
    
    @Override
    public int associatedStreamId() {
        return this.associatedStreamId;
    }
    
    @Override
    public SpdySynStreamFrame setAssociatedStreamId(final int associatedStreamId) {
        if (associatedStreamId < 0) {
            throw new IllegalArgumentException("Associated-To-Stream-ID cannot be negative: " + associatedStreamId);
        }
        this.associatedStreamId = associatedStreamId;
        return this;
    }
    
    @Override
    public byte priority() {
        return this.priority;
    }
    
    @Override
    public SpdySynStreamFrame setPriority(final byte priority) {
        if (priority < 0 || priority > 7) {
            throw new IllegalArgumentException("Priority must be between 0 and 7 inclusive: " + priority);
        }
        this.priority = priority;
        return this;
    }
    
    @Override
    public boolean isUnidirectional() {
        return this.unidirectional;
    }
    
    @Override
    public SpdySynStreamFrame setUnidirectional(final boolean unidirectional) {
        this.unidirectional = unidirectional;
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append("(last: ").append(this.isLast()).append("; unidirectional: ").append(this.isUnidirectional()).append(')').append(StringUtil.NEWLINE).append("--> Stream-ID = ").append(this.streamId()).append(StringUtil.NEWLINE);
        if (this.associatedStreamId != 0) {
            buf.append("--> Associated-To-Stream-ID = ").append(this.associatedStreamId()).append(StringUtil.NEWLINE);
        }
        buf.append("--> Priority = ").append(this.priority()).append(StringUtil.NEWLINE).append("--> Headers:").append(StringUtil.NEWLINE);
        this.appendHeaders(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }
}
