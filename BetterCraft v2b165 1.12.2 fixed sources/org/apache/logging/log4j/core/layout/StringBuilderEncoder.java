// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.status.StatusLogger;
import java.nio.charset.CodingErrorAction;
import java.util.Objects;
import org.apache.logging.log4j.core.util.Constants;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class StringBuilderEncoder implements Encoder<StringBuilder>
{
    private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;
    private final ThreadLocal<CharBuffer> charBufferThreadLocal;
    private final ThreadLocal<ByteBuffer> byteBufferThreadLocal;
    private final ThreadLocal<CharsetEncoder> charsetEncoderThreadLocal;
    private final Charset charset;
    private final int charBufferSize;
    private final int byteBufferSize;
    
    public StringBuilderEncoder(final Charset charset) {
        this(charset, Constants.ENCODER_CHAR_BUFFER_SIZE, 8192);
    }
    
    public StringBuilderEncoder(final Charset charset, final int charBufferSize, final int byteBufferSize) {
        this.charBufferThreadLocal = new ThreadLocal<CharBuffer>();
        this.byteBufferThreadLocal = new ThreadLocal<ByteBuffer>();
        this.charsetEncoderThreadLocal = new ThreadLocal<CharsetEncoder>();
        this.charBufferSize = charBufferSize;
        this.byteBufferSize = byteBufferSize;
        this.charset = Objects.requireNonNull(charset, "charset");
    }
    
    @Override
    public void encode(final StringBuilder source, final ByteBufferDestination destination) {
        final ByteBuffer temp = this.getByteBuffer();
        temp.clear();
        temp.limit(Math.min(temp.capacity(), destination.getByteBuffer().capacity()));
        final CharsetEncoder charsetEncoder = this.getCharsetEncoder();
        final int estimatedBytes = estimateBytes(source.length(), charsetEncoder.maxBytesPerChar());
        if (temp.remaining() < estimatedBytes) {
            this.encodeSynchronized(this.getCharsetEncoder(), this.getCharBuffer(), source, destination);
        }
        else {
            this.encodeWithThreadLocals(charsetEncoder, this.getCharBuffer(), temp, source, destination);
        }
    }
    
    private void encodeWithThreadLocals(final CharsetEncoder charsetEncoder, final CharBuffer charBuffer, final ByteBuffer temp, final StringBuilder source, final ByteBufferDestination destination) {
        try {
            TextEncoderHelper.encodeTextWithCopy(charsetEncoder, charBuffer, temp, source, destination);
        }
        catch (final Exception ex) {
            this.logEncodeTextException(ex, source, destination);
            TextEncoderHelper.encodeTextFallBack(this.charset, source, destination);
        }
    }
    
    private static int estimateBytes(final int charCount, final float maxBytesPerChar) {
        return (int)(charCount * (double)maxBytesPerChar);
    }
    
    private void encodeSynchronized(final CharsetEncoder charsetEncoder, final CharBuffer charBuffer, final StringBuilder source, final ByteBufferDestination destination) {
        synchronized (destination) {
            try {
                TextEncoderHelper.encodeText(charsetEncoder, charBuffer, destination.getByteBuffer(), source, destination);
            }
            catch (final Exception ex) {
                this.logEncodeTextException(ex, source, destination);
                TextEncoderHelper.encodeTextFallBack(this.charset, source, destination);
            }
        }
    }
    
    private CharsetEncoder getCharsetEncoder() {
        CharsetEncoder result = this.charsetEncoderThreadLocal.get();
        if (result == null) {
            result = this.charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
            this.charsetEncoderThreadLocal.set(result);
        }
        return result;
    }
    
    private CharBuffer getCharBuffer() {
        CharBuffer result = this.charBufferThreadLocal.get();
        if (result == null) {
            result = CharBuffer.wrap(new char[this.charBufferSize]);
            this.charBufferThreadLocal.set(result);
        }
        return result;
    }
    
    private ByteBuffer getByteBuffer() {
        ByteBuffer result = this.byteBufferThreadLocal.get();
        if (result == null) {
            result = ByteBuffer.wrap(new byte[this.byteBufferSize]);
            this.byteBufferThreadLocal.set(result);
        }
        return result;
    }
    
    private void logEncodeTextException(final Exception ex, final StringBuilder text, final ByteBufferDestination destination) {
        StatusLogger.getLogger().error("Recovering from StringBuilderEncoder.encode('{}') error: {}", text, ex, ex);
    }
}
