// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBuf;

public class DefaultDnsRecordEncoder implements DnsRecordEncoder
{
    private static final int PREFIX_MASK = 7;
    
    protected DefaultDnsRecordEncoder() {
    }
    
    @Override
    public final void encodeQuestion(final DnsQuestion question, final ByteBuf out) throws Exception {
        this.encodeName(question.name(), out);
        out.writeShort(question.type().intValue());
        out.writeShort(question.dnsClass());
    }
    
    @Override
    public void encodeRecord(final DnsRecord record, final ByteBuf out) throws Exception {
        if (record instanceof DnsQuestion) {
            this.encodeQuestion((DnsQuestion)record, out);
        }
        else if (record instanceof DnsPtrRecord) {
            this.encodePtrRecord((DnsPtrRecord)record, out);
        }
        else if (record instanceof DnsOptEcsRecord) {
            this.encodeOptEcsRecord((DnsOptEcsRecord)record, out);
        }
        else if (record instanceof DnsOptPseudoRecord) {
            this.encodeOptPseudoRecord((DnsOptPseudoRecord)record, out);
        }
        else {
            if (!(record instanceof DnsRawRecord)) {
                throw new UnsupportedMessageTypeException(StringUtil.simpleClassName(record));
            }
            this.encodeRawRecord((DnsRawRecord)record, out);
        }
    }
    
    private void encodeRecord0(final DnsRecord record, final ByteBuf out) throws Exception {
        this.encodeName(record.name(), out);
        out.writeShort(record.type().intValue());
        out.writeShort(record.dnsClass());
        out.writeInt((int)record.timeToLive());
    }
    
    private void encodePtrRecord(final DnsPtrRecord record, final ByteBuf out) throws Exception {
        this.encodeRecord0(record, out);
        this.encodeName(record.hostname(), out);
    }
    
    private void encodeOptPseudoRecord(final DnsOptPseudoRecord record, final ByteBuf out) throws Exception {
        this.encodeRecord0(record, out);
        out.writeShort(0);
    }
    
    private void encodeOptEcsRecord(final DnsOptEcsRecord record, final ByteBuf out) throws Exception {
        this.encodeRecord0(record, out);
        final int sourcePrefixLength = record.sourcePrefixLength();
        final int scopePrefixLength = record.scopePrefixLength();
        final int lowOrderBitsToPreserve = sourcePrefixLength & 0x7;
        final byte[] bytes = record.address();
        final int addressBits = bytes.length << 3;
        if (addressBits < sourcePrefixLength || sourcePrefixLength < 0) {
            throw new IllegalArgumentException(sourcePrefixLength + ": " + sourcePrefixLength + " (expected: 0 >= " + addressBits + ')');
        }
        final short addressNumber = (short)((bytes.length == 4) ? InternetProtocolFamily.IPv4.addressNumber() : InternetProtocolFamily.IPv6.addressNumber());
        final int payloadLength = calculateEcsAddressLength(sourcePrefixLength, lowOrderBitsToPreserve);
        final int fullPayloadLength = 8 + payloadLength;
        out.writeShort(fullPayloadLength);
        out.writeShort(8);
        out.writeShort(fullPayloadLength - 4);
        out.writeShort(addressNumber);
        out.writeByte(sourcePrefixLength);
        out.writeByte(scopePrefixLength);
        if (lowOrderBitsToPreserve > 0) {
            final int bytesLength = payloadLength - 1;
            out.writeBytes(bytes, 0, bytesLength);
            out.writeByte(padWithZeros(bytes[bytesLength], lowOrderBitsToPreserve));
        }
        else {
            out.writeBytes(bytes, 0, payloadLength);
        }
    }
    
    static int calculateEcsAddressLength(final int sourcePrefixLength, final int lowOrderBitsToPreserve) {
        return (sourcePrefixLength >>> 3) + ((lowOrderBitsToPreserve != 0) ? 1 : 0);
    }
    
    private void encodeRawRecord(final DnsRawRecord record, final ByteBuf out) throws Exception {
        this.encodeRecord0(record, out);
        final ByteBuf content = record.content();
        final int contentLen = content.readableBytes();
        out.writeShort(contentLen);
        out.writeBytes(content, content.readerIndex(), contentLen);
    }
    
    protected void encodeName(final String name, final ByteBuf buf) throws Exception {
        if (".".equals(name)) {
            buf.writeByte(0);
            return;
        }
        final String[] split;
        final String[] labels = split = name.split("\\.");
        for (final String label : split) {
            final int labelLen = label.length();
            if (labelLen == 0) {
                break;
            }
            buf.writeByte(labelLen);
            ByteBufUtil.writeAscii(buf, label);
        }
        buf.writeByte(0);
    }
    
    static byte padWithZeros(final byte b, final int lowOrderBitsToPreserve) {
        switch (lowOrderBitsToPreserve) {
            case 0: {
                return 0;
            }
            case 1: {
                return (byte)(0x1 & b);
            }
            case 2: {
                return (byte)(0x3 & b);
            }
            case 3: {
                return (byte)(0x7 & b);
            }
            case 4: {
                return (byte)(0xF & b);
            }
            case 5: {
                return (byte)(0x1F & b);
            }
            case 6: {
                return (byte)(0x3F & b);
            }
            case 7: {
                return (byte)(0x7F & b);
            }
            case 8: {
                return b;
            }
            default: {
                throw new IllegalArgumentException("lowOrderBitsToPreserve: " + lowOrderBitsToPreserve);
            }
        }
    }
}
