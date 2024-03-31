// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.EncoderException;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;
import io.netty.buffer.ByteBuf;

public interface Socks5AddressEncoder
{
    public static final Socks5AddressEncoder DEFAULT = new Socks5AddressEncoder() {
        @Override
        public void encodeAddress(final Socks5AddressType addrType, final String addrValue, final ByteBuf out) throws Exception {
            final byte typeVal = addrType.byteValue();
            if (typeVal == Socks5AddressType.IPv4.byteValue()) {
                if (addrValue != null) {
                    out.writeBytes(NetUtil.createByteArrayFromIpAddressString(addrValue));
                }
                else {
                    out.writeInt(0);
                }
            }
            else if (typeVal == Socks5AddressType.DOMAIN.byteValue()) {
                if (addrValue != null) {
                    final byte[] bndAddr = addrValue.getBytes(CharsetUtil.US_ASCII);
                    out.writeByte(bndAddr.length);
                    out.writeBytes(bndAddr);
                }
                else {
                    out.writeByte(1);
                    out.writeByte(0);
                }
            }
            else {
                if (typeVal != Socks5AddressType.IPv6.byteValue()) {
                    throw new EncoderException("unsupported addrType: " + (addrType.byteValue() & 0xFF));
                }
                if (addrValue != null) {
                    out.writeBytes(NetUtil.createByteArrayFromIpAddressString(addrValue));
                }
                else {
                    out.writeLong(0L);
                    out.writeLong(0L);
                }
            }
        }
    };
    
    void encodeAddress(final Socks5AddressType p0, final String p1, final ByteBuf p2) throws Exception;
}
