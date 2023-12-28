// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.DecoderException;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;
import io.netty.buffer.ByteBuf;

public interface Socks5AddressDecoder
{
    public static final Socks5AddressDecoder DEFAULT = new Socks5AddressDecoder() {
        private static final int IPv6_LEN = 16;
        
        @Override
        public String decodeAddress(final Socks5AddressType addrType, final ByteBuf in) throws Exception {
            if (addrType == Socks5AddressType.IPv4) {
                return NetUtil.intToIpAddress(in.readInt());
            }
            if (addrType == Socks5AddressType.DOMAIN) {
                final int length = in.readUnsignedByte();
                final String domain = in.toString(in.readerIndex(), length, CharsetUtil.US_ASCII);
                in.skipBytes(length);
                return domain;
            }
            if (addrType != Socks5AddressType.IPv6) {
                throw new DecoderException("unsupported address type: " + (addrType.byteValue() & 0xFF));
            }
            if (in.hasArray()) {
                final int readerIdx = in.readerIndex();
                in.readerIndex(readerIdx + 16);
                return NetUtil.bytesToIpAddress(in.array(), in.arrayOffset() + readerIdx, 16);
            }
            final byte[] tmp = new byte[16];
            in.readBytes(tmp);
            return NetUtil.bytesToIpAddress(tmp);
        }
    };
    
    String decodeAddress(final Socks5AddressType p0, final ByteBuf p1) throws Exception;
}
