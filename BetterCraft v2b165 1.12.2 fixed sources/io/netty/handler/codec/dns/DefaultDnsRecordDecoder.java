// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.CharsetUtil;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.buffer.ByteBuf;

public class DefaultDnsRecordDecoder implements DnsRecordDecoder
{
    static final String ROOT = ".";
    
    protected DefaultDnsRecordDecoder() {
    }
    
    @Override
    public final DnsQuestion decodeQuestion(final ByteBuf in) throws Exception {
        final String name = decodeName(in);
        final DnsRecordType type = DnsRecordType.valueOf(in.readUnsignedShort());
        final int qClass = in.readUnsignedShort();
        return new DefaultDnsQuestion(name, type, qClass);
    }
    
    @Override
    public final <T extends DnsRecord> T decodeRecord(final ByteBuf in) throws Exception {
        final int startOffset = in.readerIndex();
        final String name = decodeName(in);
        final int endOffset = in.writerIndex();
        if (endOffset - startOffset < 10) {
            in.readerIndex(startOffset);
            return null;
        }
        final DnsRecordType type = DnsRecordType.valueOf(in.readUnsignedShort());
        final int aClass = in.readUnsignedShort();
        final long ttl = in.readUnsignedInt();
        final int length = in.readUnsignedShort();
        final int offset = in.readerIndex();
        if (endOffset - offset < length) {
            in.readerIndex(startOffset);
            return null;
        }
        final T record = (T)this.decodeRecord(name, type, aClass, ttl, in, offset, length);
        in.readerIndex(offset + length);
        return record;
    }
    
    protected DnsRecord decodeRecord(final String name, final DnsRecordType type, final int dnsClass, final long timeToLive, final ByteBuf in, final int offset, final int length) throws Exception {
        if (type == DnsRecordType.PTR) {
            return new DefaultDnsPtrRecord(name, dnsClass, timeToLive, this.decodeName0(in.duplicate().setIndex(offset, offset + length)));
        }
        return new DefaultDnsRawRecord(name, type, dnsClass, timeToLive, in.retainedDuplicate().setIndex(offset, offset + length));
    }
    
    protected String decodeName0(final ByteBuf in) {
        return decodeName(in);
    }
    
    public static String decodeName(final ByteBuf in) {
        int position = -1;
        int checked = 0;
        final int end = in.writerIndex();
        final int readable = in.readableBytes();
        if (readable == 0) {
            return ".";
        }
        final StringBuilder name = new StringBuilder(readable << 1);
        while (in.isReadable()) {
            final int len = in.readUnsignedByte();
            final boolean pointer = (len & 0xC0) == 0xC0;
            if (pointer) {
                if (position == -1) {
                    position = in.readerIndex() + 1;
                }
                if (!in.isReadable()) {
                    throw new CorruptedFrameException("truncated pointer in a name");
                }
                final int next = (len & 0x3F) << 8 | in.readUnsignedByte();
                if (next >= end) {
                    throw new CorruptedFrameException("name has an out-of-range pointer");
                }
                in.readerIndex(next);
                checked += 2;
                if (checked >= end) {
                    throw new CorruptedFrameException("name contains a loop.");
                }
                continue;
            }
            else {
                if (len == 0) {
                    break;
                }
                if (!in.isReadable(len)) {
                    throw new CorruptedFrameException("truncated label in a name");
                }
                name.append(in.toString(in.readerIndex(), len, CharsetUtil.UTF_8)).append('.');
                in.skipBytes(len);
            }
        }
        if (position != -1) {
            in.readerIndex(position);
        }
        if (name.length() == 0) {
            return ".";
        }
        if (name.charAt(name.length() - 1) != '.') {
            name.append('.');
        }
        return name.toString();
    }
}
