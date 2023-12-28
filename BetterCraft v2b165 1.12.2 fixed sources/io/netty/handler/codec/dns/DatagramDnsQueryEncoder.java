// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.ChannelHandler;
import java.net.InetSocketAddress;
import io.netty.channel.AddressedEnvelope;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public class DatagramDnsQueryEncoder extends MessageToMessageEncoder<AddressedEnvelope<DnsQuery, InetSocketAddress>>
{
    private final DnsRecordEncoder recordEncoder;
    
    public DatagramDnsQueryEncoder() {
        this(DnsRecordEncoder.DEFAULT);
    }
    
    public DatagramDnsQueryEncoder(final DnsRecordEncoder recordEncoder) {
        this.recordEncoder = ObjectUtil.checkNotNull(recordEncoder, "recordEncoder");
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final AddressedEnvelope<DnsQuery, InetSocketAddress> in, final List<Object> out) throws Exception {
        final InetSocketAddress recipient = in.recipient();
        final DnsQuery query = in.content();
        final ByteBuf buf = this.allocateBuffer(ctx, in);
        boolean success = false;
        try {
            encodeHeader(query, buf);
            this.encodeQuestions(query, buf);
            this.encodeRecords(query, DnsSection.ADDITIONAL, buf);
            success = true;
        }
        finally {
            if (!success) {
                buf.release();
            }
        }
        out.add(new DatagramPacket(buf, recipient, null));
    }
    
    protected ByteBuf allocateBuffer(final ChannelHandlerContext ctx, final AddressedEnvelope<DnsQuery, InetSocketAddress> msg) throws Exception {
        return ctx.alloc().ioBuffer(1024);
    }
    
    private static void encodeHeader(final DnsQuery query, final ByteBuf buf) {
        buf.writeShort(query.id());
        int flags = 0;
        flags |= (query.opCode().byteValue() & 0xFF) << 14;
        if (query.isRecursionDesired()) {
            flags |= 0x100;
        }
        buf.writeShort(flags);
        buf.writeShort(query.count(DnsSection.QUESTION));
        buf.writeShort(0);
        buf.writeShort(0);
        buf.writeShort(query.count(DnsSection.ADDITIONAL));
    }
    
    private void encodeQuestions(final DnsQuery query, final ByteBuf buf) throws Exception {
        for (int count = query.count(DnsSection.QUESTION), i = 0; i < count; ++i) {
            this.recordEncoder.encodeQuestion(query.recordAt(DnsSection.QUESTION, i), buf);
        }
    }
    
    private void encodeRecords(final DnsQuery query, final DnsSection section, final ByteBuf buf) throws Exception {
        for (int count = query.count(section), i = 0; i < count; ++i) {
            this.recordEncoder.encodeRecord(query.recordAt(section, i), buf);
        }
    }
}
