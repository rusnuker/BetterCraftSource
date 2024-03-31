// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.layout;

import java.nio.charset.CoderResult;
import java.nio.charset.CharacterCodingException;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class TextEncoderHelper
{
    private TextEncoderHelper() {
    }
    
    static void encodeTextFallBack(final Charset charset, final StringBuilder text, final ByteBufferDestination destination) {
        final byte[] bytes = text.toString().getBytes(charset);
        synchronized (destination) {
            ByteBuffer buffer = destination.getByteBuffer();
            int offset = 0;
            do {
                final int length = Math.min(bytes.length - offset, buffer.remaining());
                buffer.put(bytes, offset, length);
                offset += length;
                if (offset < bytes.length) {
                    buffer = destination.drain(buffer);
                }
            } while (offset < bytes.length);
        }
    }
    
    static void encodeTextWithCopy(final CharsetEncoder charsetEncoder, final CharBuffer charBuf, final ByteBuffer temp, final StringBuilder text, final ByteBufferDestination destination) {
        encodeText(charsetEncoder, charBuf, temp, text, destination);
        copyDataToDestination(temp, destination);
    }
    
    private static void copyDataToDestination(final ByteBuffer temp, final ByteBufferDestination destination) {
        synchronized (destination) {
            ByteBuffer destinationBuffer = destination.getByteBuffer();
            if (destinationBuffer != temp) {
                temp.flip();
                if (temp.remaining() > destinationBuffer.remaining()) {
                    destinationBuffer = destination.drain(destinationBuffer);
                }
                destinationBuffer.put(temp);
                temp.clear();
            }
        }
    }
    
    static void encodeText(final CharsetEncoder charsetEncoder, final CharBuffer charBuf, final ByteBuffer byteBuf, final StringBuilder text, final ByteBufferDestination destination) {
        charsetEncoder.reset();
        ByteBuffer temp = byteBuf;
        int start = 0;
        int todoChars = text.length();
        boolean endOfInput = true;
        do {
            charBuf.clear();
            final int copied = copy(text, start, charBuf);
            start += copied;
            todoChars -= copied;
            endOfInput = (todoChars <= 0);
            charBuf.flip();
            temp = encode(charsetEncoder, charBuf, endOfInput, destination, temp);
        } while (!endOfInput);
    }
    
    @Deprecated
    public static void encodeText(final CharsetEncoder charsetEncoder, final CharBuffer charBuf, final ByteBufferDestination destination) {
        synchronized (destination) {
            charsetEncoder.reset();
            final ByteBuffer byteBuf = destination.getByteBuffer();
            encode(charsetEncoder, charBuf, true, destination, byteBuf);
        }
    }
    
    private static ByteBuffer encode(final CharsetEncoder charsetEncoder, final CharBuffer charBuf, final boolean endOfInput, final ByteBufferDestination destination, ByteBuffer byteBuf) {
        try {
            byteBuf = encodeAsMuchAsPossible(charsetEncoder, charBuf, endOfInput, destination, byteBuf);
            if (endOfInput) {
                byteBuf = flushRemainingBytes(charsetEncoder, destination, byteBuf);
            }
        }
        catch (final CharacterCodingException ex) {
            throw new IllegalStateException(ex);
        }
        return byteBuf;
    }
    
    private static ByteBuffer encodeAsMuchAsPossible(final CharsetEncoder charsetEncoder, final CharBuffer charBuf, final boolean endOfInput, final ByteBufferDestination destination, ByteBuffer temp) throws CharacterCodingException {
        CoderResult result;
        do {
            result = charsetEncoder.encode(charBuf, temp, endOfInput);
            temp = drainIfByteBufferFull(destination, temp, result);
        } while (result.isOverflow());
        if (!result.isUnderflow()) {
            result.throwException();
        }
        return temp;
    }
    
    private static ByteBuffer drainIfByteBufferFull(final ByteBufferDestination destination, ByteBuffer temp, final CoderResult result) {
        if (result.isOverflow()) {
            ByteBuffer destinationBuffer = destination.getByteBuffer();
            if (destinationBuffer != temp) {
                temp.flip();
                destinationBuffer.put(temp);
                temp.clear();
            }
            destinationBuffer = (temp = destination.drain(destinationBuffer));
        }
        return temp;
    }
    
    private static ByteBuffer flushRemainingBytes(final CharsetEncoder charsetEncoder, final ByteBufferDestination destination, ByteBuffer temp) throws CharacterCodingException {
        CoderResult result;
        do {
            result = charsetEncoder.flush(temp);
            temp = drainIfByteBufferFull(destination, temp, result);
        } while (result.isOverflow());
        if (!result.isUnderflow()) {
            result.throwException();
        }
        return temp;
    }
    
    static int copy(final StringBuilder source, final int offset, final CharBuffer destination) {
        final int length = Math.min(source.length() - offset, destination.remaining());
        final char[] array = destination.array();
        final int start = destination.position();
        source.getChars(offset, offset + length, array, start);
        destination.position(start + length);
        return length;
    }
}
