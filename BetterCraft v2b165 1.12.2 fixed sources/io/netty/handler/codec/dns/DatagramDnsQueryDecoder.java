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
public class DatagramDnsQueryDecoder extends MessageToMessageDecoder<DatagramPacket>
{
    private final DnsRecordDecoder recordDecoder;
    
    public DatagramDnsQueryDecoder() {
        this(DnsRecordDecoder.DEFAULT);
    }
    
    public DatagramDnsQueryDecoder(final DnsRecordDecoder recordDecoder) {
        this.recordDecoder = ObjectUtil.checkNotNull(recordDecoder, "recordDecoder");
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final DatagramPacket packet, final List<Object> out) throws Exception {
        final ByteBuf buf = ((DefaultAddressedEnvelope<ByteBuf, A>)packet).content();
        final DnsQuery query = newQuery(packet, buf);
        boolean success = false;
        try {
            final int questionCount = buf.readUnsignedShort();
            final int answerCount = buf.readUnsignedShort();
            final int authorityRecordCount = buf.readUnsignedShort();
            final int additionalRecordCount = buf.readUnsignedShort();
            this.decodeQuestions(query, buf, questionCount);
            this.decodeRecords(query, DnsSection.ANSWER, buf, answerCount);
            this.decodeRecords(query, DnsSection.AUTHORITY, buf, authorityRecordCount);
            this.decodeRecords(query, DnsSection.ADDITIONAL, buf, additionalRecordCount);
            out.add(query);
            success = true;
        }
        finally {
            if (!success) {
                query.release();
            }
        }
    }
    
    private static DnsQuery newQuery(final DatagramPacket packet, final ByteBuf buf) {
        final int id = buf.readUnsignedShort();
        final int flags = buf.readUnsignedShort();
        if (flags >> 15 == 1) {
            throw new CorruptedFrameException("not a query");
        }
        final DnsQuery query = new DatagramDnsQuery(((DefaultAddressedEnvelope<M, InetSocketAddress>)packet).sender(), ((DefaultAddressedEnvelope<M, InetSocketAddress>)packet).recipient(), id, DnsOpCode.valueOf((byte)(flags >> 11 & 0xF)));
        query.setRecursionDesired((flags >> 8 & 0x1) == 0x1);
        query.setZ(flags >> 4 & 0x7);
        return query;
    }
    
    private void decodeQuestions(final DnsQuery query, final ByteBuf buf, final int questionCount) throws Exception {
        for (int i = questionCount; i > 0; --i) {
            query.addRecord(DnsSection.QUESTION, (DnsRecord)this.recordDecoder.decodeQuestion(buf));
        }
    }
    
    private void decodeRecords(final DnsQuery query, final DnsSection section, final ByteBuf buf, final int count) throws Exception {
        for (int i = count; i > 0; --i) {
            final DnsRecord r = this.recordDecoder.decodeRecord(buf);
            if (r == null) {
                break;
            }
            query.addRecord(section, r);
        }
    }
}
