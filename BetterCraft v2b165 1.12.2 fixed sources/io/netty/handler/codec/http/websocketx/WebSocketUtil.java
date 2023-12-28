// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx;

import java.security.NoSuchAlgorithmException;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.base64.Base64;
import io.netty.buffer.Unpooled;
import java.security.MessageDigest;
import io.netty.util.concurrent.FastThreadLocal;

final class WebSocketUtil
{
    private static final FastThreadLocal<MessageDigest> MD5;
    private static final FastThreadLocal<MessageDigest> SHA1;
    
    static byte[] md5(final byte[] data) {
        return digest(WebSocketUtil.MD5, data);
    }
    
    static byte[] sha1(final byte[] data) {
        return digest(WebSocketUtil.SHA1, data);
    }
    
    private static byte[] digest(final FastThreadLocal<MessageDigest> digestFastThreadLocal, final byte[] data) {
        final MessageDigest digest = digestFastThreadLocal.get();
        digest.reset();
        return digest.digest(data);
    }
    
    static String base64(final byte[] data) {
        final ByteBuf encodedData = Unpooled.wrappedBuffer(data);
        final ByteBuf encoded = Base64.encode(encodedData);
        final String encodedString = encoded.toString(CharsetUtil.UTF_8);
        encoded.release();
        return encodedString;
    }
    
    static byte[] randomBytes(final int size) {
        final byte[] bytes = new byte[size];
        for (int index = 0; index < size; ++index) {
            bytes[index] = (byte)randomNumber(0, 255);
        }
        return bytes;
    }
    
    static int randomNumber(final int minimum, final int maximum) {
        return (int)(Math.random() * maximum + minimum);
    }
    
    private WebSocketUtil() {
    }
    
    static {
        MD5 = new FastThreadLocal<MessageDigest>() {
            @Override
            protected MessageDigest initialValue() throws Exception {
                try {
                    return MessageDigest.getInstance("MD5");
                }
                catch (final NoSuchAlgorithmException e) {
                    throw new InternalError("MD5 not supported on this platform - Outdated?");
                }
            }
        };
        SHA1 = new FastThreadLocal<MessageDigest>() {
            @Override
            protected MessageDigest initialValue() throws Exception {
                try {
                    return MessageDigest.getInstance("SHA1");
                }
                catch (final NoSuchAlgorithmException e) {
                    throw new InternalError("SHA-1 not supported on this platform - Outdated?");
                }
            }
        };
    }
}
