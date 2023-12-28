// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.xml;

import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.TooLongFrameException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class XmlFrameDecoder extends ByteToMessageDecoder
{
    private final int maxFrameLength;
    
    public XmlFrameDecoder(final int maxFrameLength) {
        if (maxFrameLength < 1) {
            throw new IllegalArgumentException("maxFrameLength must be a positive int");
        }
        this.maxFrameLength = maxFrameLength;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        boolean openingBracketFound = false;
        boolean atLeastOneXmlElementFound = false;
        boolean inCDATASection = false;
        long openBracketsCount = 0L;
        int length = 0;
        int leadingWhiteSpaceCount = 0;
        final int bufferLength = in.writerIndex();
        if (bufferLength > this.maxFrameLength) {
            in.skipBytes(in.readableBytes());
            this.fail(bufferLength);
            return;
        }
        for (int i = in.readerIndex(); i < bufferLength; ++i) {
            final byte readByte = in.getByte(i);
            if (!openingBracketFound && Character.isWhitespace(readByte)) {
                ++leadingWhiteSpaceCount;
            }
            else {
                if (!openingBracketFound && readByte != 60) {
                    fail(ctx);
                    in.skipBytes(in.readableBytes());
                    return;
                }
                if (!inCDATASection && readByte == 60) {
                    openingBracketFound = true;
                    if (i < bufferLength - 1) {
                        final byte peekAheadByte = in.getByte(i + 1);
                        if (peekAheadByte == 47) {
                            for (int peekFurtherAheadIndex = i + 2; peekFurtherAheadIndex <= bufferLength - 1; ++peekFurtherAheadIndex) {
                                if (in.getByte(peekFurtherAheadIndex) == 62) {
                                    --openBracketsCount;
                                    break;
                                }
                            }
                        }
                        else if (isValidStartCharForXmlElement(peekAheadByte)) {
                            atLeastOneXmlElementFound = true;
                            ++openBracketsCount;
                        }
                        else if (peekAheadByte == 33) {
                            if (isCommentBlockStart(in, i)) {
                                ++openBracketsCount;
                            }
                            else if (isCDATABlockStart(in, i)) {
                                ++openBracketsCount;
                                inCDATASection = true;
                            }
                        }
                        else if (peekAheadByte == 63) {
                            ++openBracketsCount;
                        }
                    }
                }
                else if (!inCDATASection && readByte == 47) {
                    if (i < bufferLength - 1 && in.getByte(i + 1) == 62) {
                        --openBracketsCount;
                    }
                }
                else if (readByte == 62) {
                    length = i + 1;
                    if (i - 1 > -1) {
                        final byte peekBehindByte = in.getByte(i - 1);
                        if (!inCDATASection) {
                            if (peekBehindByte == 63) {
                                --openBracketsCount;
                            }
                            else if (peekBehindByte == 45 && i - 2 > -1 && in.getByte(i - 2) == 45) {
                                --openBracketsCount;
                            }
                        }
                        else if (peekBehindByte == 93 && i - 2 > -1 && in.getByte(i - 2) == 93) {
                            --openBracketsCount;
                            inCDATASection = false;
                        }
                    }
                    if (atLeastOneXmlElementFound && openBracketsCount == 0L) {
                        break;
                    }
                }
            }
        }
        final int readerIndex = in.readerIndex();
        int xmlElementLength = length - readerIndex;
        if (openBracketsCount == 0L && xmlElementLength > 0) {
            if (readerIndex + xmlElementLength >= bufferLength) {
                xmlElementLength = in.readableBytes();
            }
            final ByteBuf frame = extractFrame(in, readerIndex + leadingWhiteSpaceCount, xmlElementLength - leadingWhiteSpaceCount);
            in.skipBytes(xmlElementLength);
            out.add(frame);
        }
    }
    
    private void fail(final long frameLength) {
        if (frameLength > 0L) {
            throw new TooLongFrameException("frame length exceeds " + this.maxFrameLength + ": " + frameLength + " - discarded");
        }
        throw new TooLongFrameException("frame length exceeds " + this.maxFrameLength + " - discarding");
    }
    
    private static void fail(final ChannelHandlerContext ctx) {
        ctx.fireExceptionCaught((Throwable)new CorruptedFrameException("frame contains content before the xml starts"));
    }
    
    private static ByteBuf extractFrame(final ByteBuf buffer, final int index, final int length) {
        return buffer.copy(index, length);
    }
    
    private static boolean isValidStartCharForXmlElement(final byte b) {
        return (b >= 97 && b <= 122) || (b >= 65 && b <= 90) || b == 58 || b == 95;
    }
    
    private static boolean isCommentBlockStart(final ByteBuf in, final int i) {
        return i < in.writerIndex() - 3 && in.getByte(i + 2) == 45 && in.getByte(i + 3) == 45;
    }
    
    private static boolean isCDATABlockStart(final ByteBuf in, final int i) {
        return i < in.writerIndex() - 8 && in.getByte(i + 2) == 91 && in.getByte(i + 3) == 67 && in.getByte(i + 4) == 68 && in.getByte(i + 5) == 65 && in.getByte(i + 6) == 84 && in.getByte(i + 7) == 65 && in.getByte(i + 8) == 91;
    }
}
