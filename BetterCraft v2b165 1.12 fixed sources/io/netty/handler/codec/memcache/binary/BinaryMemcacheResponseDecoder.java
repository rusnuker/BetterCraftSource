// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public class BinaryMemcacheResponseDecoder extends AbstractBinaryMemcacheDecoder<BinaryMemcacheResponse>
{
    public BinaryMemcacheResponseDecoder() {
        this(8192);
    }
    
    public BinaryMemcacheResponseDecoder(final int chunkSize) {
        super(chunkSize);
    }
    
    @Override
    protected BinaryMemcacheResponse decodeHeader(final ByteBuf in) {
        final DefaultBinaryMemcacheResponse header = new DefaultBinaryMemcacheResponse();
        header.setMagic(in.readByte());
        header.setOpcode(in.readByte());
        header.setKeyLength(in.readShort());
        header.setExtrasLength(in.readByte());
        header.setDataType(in.readByte());
        header.setStatus(in.readShort());
        header.setTotalBodyLength(in.readInt());
        header.setOpaque(in.readInt());
        header.setCas(in.readLong());
        return header;
    }
    
    @Override
    protected BinaryMemcacheResponse buildInvalidMessage() {
        return new DefaultBinaryMemcacheResponse(Unpooled.EMPTY_BUFFER, Unpooled.EMPTY_BUFFER);
    }
}
