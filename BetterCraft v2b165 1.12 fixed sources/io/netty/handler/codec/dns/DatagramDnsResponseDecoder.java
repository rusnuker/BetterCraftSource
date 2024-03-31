// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.channel.DefaultAddressedEnvelope;
import java.net.InetSocketAddress;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.buffer.ByteBuf;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

@ChannelHandler.Sharable
public class DatagramDnsResponseDecoder extends MessageToMessageDecoder<DatagramPacket>
{
    private final DnsRecordDecoder recordDecoder;
    
    public DatagramDnsResponseDecoder() {
        this(DnsRecordDecoder.DEFAULT);
    }
    
    public DatagramDnsResponseDecoder(final DnsRecordDecoder recordDecoder) {
        this.recordDecoder = ObjectUtil.checkNotNull(recordDecoder, "recordDecoder");
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final DatagramPacket packet, final List<Object> out) throws Exception {
        final ByteBuf buf = ((DefaultAddressedEnvelope<ByteBuf, A>)packet).content();
        final DnsResponse response = newResponse(packet, buf);
        boolean success = false;
        try {
            final int questionCount = buf.readUnsignedShort();
            final int answerCount = buf.readUnsignedShort();
            final int authorityRecordCount = buf.readUnsignedShort();
            final int additionalRecordCount = buf.readUnsignedShort();
            this.decodeQuestions(response, buf, questionCount);
            this.decodeRecords(response, DnsSection.ANSWER, buf, answerCount);
            this.decodeRecords(response, DnsSection.AUTHORITY, buf, authorityRecordCount);
            this.decodeRecords(response, DnsSection.ADDITIONAL, buf, additionalRecordCount);
            out.add(response);
            success = true;
        }
        finally {
            if (!success) {
                response.release();
            }
        }
    }
    
    private static DnsResponse newResponse(final DatagramPacket packet, final ByteBuf buf) {
        final int id = buf.readUnsignedShort();
        final int flags = buf.readUnsignedShort();
        if (flags >> 15 == 0) {
            throw new CorruptedFrameException("not a response");
        }
        final DnsResponse response = new DatagramDnsResponse(((DefaultAddressedEnvelope<M, InetSocketAddress>)packet).sender(), ((DefaultAddressedEnvelope<M, InetSocketAddress>)packet).recipient(), id, DnsOpCode.valueOf((byte)(flags >> 11 & 0xF)), DnsResponseCode.valueOf((byte)(flags & 0xF)));
        response.setRecursionDesired((flags >> 8 & 0x1) == 0x1);
        response.setAuthoritativeAnswer((flags >> 10 & 0x1) == 0x1);
        response.setTruncated((flags >> 9 & 0x1) == 0x1);
        response.setRecursionAvailable((flags >> 7 & 0x1) == 0x1);
        response.setZ(flags >> 4 & 0x7);
        return response;
    }
    
    private void decodeQuestions(final DnsResponse response, final ByteBuf buf, final int questionCount) throws Exception {
        for (int i = questionCount; i > 0; --i) {
            response.addRecord(DnsSection.QUESTION, (DnsRecord)this.recordDecoder.decodeQuestion(buf));
        }
    }
    
    private void decodeRecords(final DnsResponse response, final DnsSection section, final ByteBuf buf, final int count) throws Exception {
        for (int i = count; i > 0; --i) {
            final DnsRecord r = this.recordDecoder.decodeRecord(buf);
            if (r == null) {
                break;
            }
            response.addRecord(section, r);
        }
    }
}
