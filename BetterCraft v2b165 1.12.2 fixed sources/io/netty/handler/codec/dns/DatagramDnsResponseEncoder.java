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
public class DatagramDnsResponseEncoder extends MessageToMessageEncoder<AddressedEnvelope<DnsResponse, InetSocketAddress>>
{
    private final DnsRecordEncoder recordEncoder;
    
    public DatagramDnsResponseEncoder() {
        this(DnsRecordEncoder.DEFAULT);
    }
    
    public DatagramDnsResponseEncoder(final DnsRecordEncoder recordEncoder) {
        this.recordEncoder = ObjectUtil.checkNotNull(recordEncoder, "recordEncoder");
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final AddressedEnvelope<DnsResponse, InetSocketAddress> in, final List<Object> out) throws Exception {
        final InetSocketAddress recipient = in.recipient();
        final DnsResponse response = in.content();
        final ByteBuf buf = this.allocateBuffer(ctx, in);
        boolean success = false;
        try {
            encodeHeader(response, buf);
            this.encodeQuestions(response, buf);
            this.encodeRecords(response, DnsSection.ANSWER, buf);
            this.encodeRecords(response, DnsSection.AUTHORITY, buf);
            this.encodeRecords(response, DnsSection.ADDITIONAL, buf);
            success = true;
        }
        finally {
            if (!success) {
                buf.release();
            }
        }
        out.add(new DatagramPacket(buf, recipient, null));
    }
    
    protected ByteBuf allocateBuffer(final ChannelHandlerContext ctx, final AddressedEnvelope<DnsResponse, InetSocketAddress> msg) throws Exception {
        return ctx.alloc().ioBuffer(1024);
    }
    
    private static void encodeHeader(final DnsResponse response, final ByteBuf buf) {
        buf.writeShort(response.id());
        int flags = 32768;
        flags |= (response.opCode().byteValue() & 0xFF) << 11;
        if (response.isAuthoritativeAnswer()) {
            flags |= 0x400;
        }
        if (response.isTruncated()) {
            flags |= 0x200;
        }
        if (response.isRecursionDesired()) {
            flags |= 0x100;
        }
        if (response.isRecursionAvailable()) {
            flags |= 0x80;
        }
        flags |= response.z() << 4;
        flags |= response.code().intValue();
        buf.writeShort(flags);
        buf.writeShort(response.count(DnsSection.QUESTION));
        buf.writeShort(response.count(DnsSection.ANSWER));
        buf.writeShort(response.count(DnsSection.AUTHORITY));
        buf.writeShort(response.count(DnsSection.ADDITIONAL));
    }
    
    private void encodeQuestions(final DnsResponse response, final ByteBuf buf) throws Exception {
        for (int count = response.count(DnsSection.QUESTION), i = 0; i < count; ++i) {
            this.recordEncoder.encodeQuestion(response.recordAt(DnsSection.QUESTION, i), buf);
        }
    }
    
    private void encodeRecords(final DnsResponse response, final DnsSection section, final ByteBuf buf) throws Exception {
        for (int count = response.count(section), i = 0; i < count; ++i) {
            this.recordEncoder.encodeRecord(response.recordAt(section, i), buf);
        }
    }
}
