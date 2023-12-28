// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.json;

import io.netty.handler.codec.CorruptedFrameException;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.TooLongFrameException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class JsonObjectDecoder extends ByteToMessageDecoder
{
    private static final int ST_CORRUPTED = -1;
    private static final int ST_INIT = 0;
    private static final int ST_DECODING_NORMAL = 1;
    private static final int ST_DECODING_ARRAY_STREAM = 2;
    private int openBraces;
    private int idx;
    private int state;
    private boolean insideString;
    private final int maxObjectLength;
    private final boolean streamArrayElements;
    
    public JsonObjectDecoder() {
        this(1048576);
    }
    
    public JsonObjectDecoder(final int maxObjectLength) {
        this(maxObjectLength, false);
    }
    
    public JsonObjectDecoder(final boolean streamArrayElements) {
        this(1048576, streamArrayElements);
    }
    
    public JsonObjectDecoder(final int maxObjectLength, final boolean streamArrayElements) {
        if (maxObjectLength < 1) {
            throw new IllegalArgumentException("maxObjectLength must be a positive int");
        }
        this.maxObjectLength = maxObjectLength;
        this.streamArrayElements = streamArrayElements;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (this.state == -1) {
            in.skipBytes(in.readableBytes());
            return;
        }
        int idx = this.idx;
        final int wrtIdx = in.writerIndex();
        if (wrtIdx > this.maxObjectLength) {
            in.skipBytes(in.readableBytes());
            this.reset();
            throw new TooLongFrameException("object length exceeds " + this.maxObjectLength + ": " + wrtIdx + " bytes discarded");
        }
        while (idx < wrtIdx) {
            final byte c = in.getByte(idx);
            if (this.state == 1) {
                this.decodeByte(c, in, idx);
                if (this.openBraces == 0) {
                    final ByteBuf json = this.extractObject(ctx, in, in.readerIndex(), idx + 1 - in.readerIndex());
                    if (json != null) {
                        out.add(json);
                    }
                    in.readerIndex(idx + 1);
                    this.reset();
                }
            }
            else if (this.state == 2) {
                this.decodeByte(c, in, idx);
                if (!this.insideString && ((this.openBraces == 1 && c == 44) || (this.openBraces == 0 && c == 93))) {
                    for (int i = in.readerIndex(); Character.isWhitespace(in.getByte(i)); ++i) {
                        in.skipBytes(1);
                    }
                    int idxNoSpaces;
                    for (idxNoSpaces = idx - 1; idxNoSpaces >= in.readerIndex() && Character.isWhitespace(in.getByte(idxNoSpaces)); --idxNoSpaces) {}
                    final ByteBuf json2 = this.extractObject(ctx, in, in.readerIndex(), idxNoSpaces + 1 - in.readerIndex());
                    if (json2 != null) {
                        out.add(json2);
                    }
                    in.readerIndex(idx + 1);
                    if (c == 93) {
                        this.reset();
                    }
                }
            }
            else if (c == 123 || c == 91) {
                this.initDecoding(c);
                if (this.state == 2) {
                    in.skipBytes(1);
                }
            }
            else {
                if (!Character.isWhitespace(c)) {
                    this.state = -1;
                    throw new CorruptedFrameException("invalid JSON received at byte position " + idx + ": " + ByteBufUtil.hexDump(in));
                }
                in.skipBytes(1);
            }
            ++idx;
        }
        if (in.readableBytes() == 0) {
            this.idx = 0;
        }
        else {
            this.idx = idx;
        }
    }
    
    protected ByteBuf extractObject(final ChannelHandlerContext ctx, final ByteBuf buffer, final int index, final int length) {
        return buffer.retainedSlice(index, length);
    }
    
    private void decodeByte(final byte c, final ByteBuf in, int idx) {
        if ((c == 123 || c == 91) && !this.insideString) {
            ++this.openBraces;
        }
        else if ((c == 125 || c == 93) && !this.insideString) {
            --this.openBraces;
        }
        else if (c == 34) {
            if (!this.insideString) {
                this.insideString = true;
            }
            else {
                int backslashCount = 0;
                --idx;
                while (idx >= 0 && in.getByte(idx) == 92) {
                    ++backslashCount;
                    --idx;
                }
                if (backslashCount % 2 == 0) {
                    this.insideString = false;
                }
            }
        }
    }
    
    private void initDecoding(final byte openingBrace) {
        this.openBraces = 1;
        if (openingBrace == 91 && this.streamArrayElements) {
            this.state = 2;
        }
        else {
            this.state = 1;
        }
    }
    
    private void reset() {
        this.insideString = false;
        this.state = 0;
        this.openBraces = 0;
    }
}
