// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.InputStream;
import lzma.sdk.ICodeProgress;
import java.io.OutputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import lzma.sdk.lzma.Encoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public class LzmaFrameEncoder extends MessageToByteEncoder<ByteBuf>
{
    private static final InternalLogger logger;
    private static final int MEDIUM_DICTIONARY_SIZE = 65536;
    private static final int MIN_FAST_BYTES = 5;
    private static final int MEDIUM_FAST_BYTES = 32;
    private static final int MAX_FAST_BYTES = 273;
    private static final int DEFAULT_MATCH_FINDER = 1;
    private static final int DEFAULT_LC = 3;
    private static final int DEFAULT_LP = 0;
    private static final int DEFAULT_PB = 2;
    private final Encoder encoder;
    private final byte properties;
    private final int littleEndianDictionarySize;
    private static boolean warningLogged;
    
    public LzmaFrameEncoder() {
        this(65536);
    }
    
    public LzmaFrameEncoder(final int lc, final int lp, final int pb) {
        this(lc, lp, pb, 65536);
    }
    
    public LzmaFrameEncoder(final int dictionarySize) {
        this(3, 0, 2, dictionarySize);
    }
    
    public LzmaFrameEncoder(final int lc, final int lp, final int pb, final int dictionarySize) {
        this(lc, lp, pb, dictionarySize, false, 32);
    }
    
    public LzmaFrameEncoder(final int lc, final int lp, final int pb, final int dictionarySize, final boolean endMarkerMode, final int numFastBytes) {
        if (lc < 0 || lc > 8) {
            throw new IllegalArgumentException("lc: " + lc + " (expected: 0-8)");
        }
        if (lp < 0 || lp > 4) {
            throw new IllegalArgumentException("lp: " + lp + " (expected: 0-4)");
        }
        if (pb < 0 || pb > 4) {
            throw new IllegalArgumentException("pb: " + pb + " (expected: 0-4)");
        }
        if (lc + lp > 4 && !LzmaFrameEncoder.warningLogged) {
            LzmaFrameEncoder.logger.warn("The latest versions of LZMA libraries (for example, XZ Utils) has an additional requirement: lc + lp <= 4. Data which don't follow this requirement cannot be decompressed with this libraries.");
            LzmaFrameEncoder.warningLogged = true;
        }
        if (dictionarySize < 0) {
            throw new IllegalArgumentException("dictionarySize: " + dictionarySize + " (expected: 0+)");
        }
        if (numFastBytes < 5 || numFastBytes > 273) {
            throw new IllegalArgumentException(String.format("numFastBytes: %d (expected: %d-%d)", numFastBytes, 5, 273));
        }
        (this.encoder = new Encoder()).setDictionarySize(dictionarySize);
        this.encoder.setEndMarkerMode(endMarkerMode);
        this.encoder.setMatchFinder(1);
        this.encoder.setNumFastBytes(numFastBytes);
        this.encoder.setLcLpPb(lc, lp, pb);
        this.properties = (byte)((pb * 5 + lp) * 9 + lc);
        this.littleEndianDictionarySize = Integer.reverseBytes(dictionarySize);
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) throws Exception {
        final int length = in.readableBytes();
        InputStream bbIn = null;
        ByteBufOutputStream bbOut = null;
        try {
            bbIn = new ByteBufInputStream(in);
            bbOut = new ByteBufOutputStream(out);
            bbOut.writeByte(this.properties);
            bbOut.writeInt(this.littleEndianDictionarySize);
            bbOut.writeLong(Long.reverseBytes(length));
            this.encoder.code(bbIn, (OutputStream)bbOut, -1L, -1L, (ICodeProgress)null);
        }
        finally {
            if (bbIn != null) {
                bbIn.close();
            }
            if (bbOut != null) {
                bbOut.close();
            }
        }
    }
    
    @Override
    protected ByteBuf allocateBuffer(final ChannelHandlerContext ctx, final ByteBuf in, final boolean preferDirect) throws Exception {
        final int length = in.readableBytes();
        final int maxOutputLength = maxOutputBufferLength(length);
        return ctx.alloc().ioBuffer(maxOutputLength);
    }
    
    private static int maxOutputBufferLength(final int inputLength) {
        double factor;
        if (inputLength < 200) {
            factor = 1.5;
        }
        else if (inputLength < 500) {
            factor = 1.2;
        }
        else if (inputLength < 1000) {
            factor = 1.1;
        }
        else if (inputLength < 10000) {
            factor = 1.05;
        }
        else {
            factor = 1.02;
        }
        return 13 + (int)(inputLength * factor);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(LzmaFrameEncoder.class);
    }
}
