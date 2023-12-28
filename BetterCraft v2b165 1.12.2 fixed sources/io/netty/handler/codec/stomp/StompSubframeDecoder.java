// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.stomp;

import io.netty.util.AsciiString;
import io.netty.handler.codec.Headers;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.internal.AppendableCharSequence;
import io.netty.handler.codec.DecoderException;
import java.util.Locale;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.DecoderResult;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class StompSubframeDecoder extends ReplayingDecoder<State>
{
    private static final int DEFAULT_CHUNK_SIZE = 8132;
    private static final int DEFAULT_MAX_LINE_LENGTH = 1024;
    private final int maxLineLength;
    private final int maxChunkSize;
    private int alreadyReadChunkSize;
    private LastStompContentSubframe lastContent;
    private long contentLength;
    
    public StompSubframeDecoder() {
        this(1024, 8132);
    }
    
    public StompSubframeDecoder(final int maxLineLength, final int maxChunkSize) {
        super(State.SKIP_CONTROL_CHARACTERS);
        this.contentLength = -1L;
        if (maxLineLength <= 0) {
            throw new IllegalArgumentException("maxLineLength must be a positive integer: " + maxLineLength);
        }
        if (maxChunkSize <= 0) {
            throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize);
        }
        this.maxChunkSize = maxChunkSize;
        this.maxLineLength = maxLineLength;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        switch (this.state()) {
            case SKIP_CONTROL_CHARACTERS: {
                skipControlCharacters(in);
                this.checkpoint(State.READ_HEADERS);
            }
            case READ_HEADERS: {
                StompCommand command = StompCommand.UNKNOWN;
                StompHeadersSubframe frame = null;
                try {
                    command = this.readCommand(in);
                    frame = new DefaultStompHeadersSubframe(command);
                    this.checkpoint(this.readHeaders(in, frame.headers()));
                    out.add(frame);
                    break;
                }
                catch (final Exception e) {
                    if (frame == null) {
                        frame = new DefaultStompHeadersSubframe(command);
                    }
                    frame.setDecoderResult(DecoderResult.failure(e));
                    out.add(frame);
                    this.checkpoint(State.BAD_FRAME);
                    return;
                }
            }
            case BAD_FRAME: {
                in.skipBytes(this.actualReadableBytes());
                return;
            }
        }
        try {
            int toRead;
            int remainingLength;
            ByteBuf chunkBuffer;
            int alreadyReadChunkSize;
            int nulIndex;
            switch (this.state()) {
                case READ_CONTENT:
                    Label_0474: {
                        toRead = in.readableBytes();
                        if (toRead == 0) {
                            return;
                        }
                        if (toRead > this.maxChunkSize) {
                            toRead = this.maxChunkSize;
                        }
                        if (this.contentLength >= 0L) {
                            remainingLength = (int)(this.contentLength - this.alreadyReadChunkSize);
                            if (toRead > remainingLength) {
                                toRead = remainingLength;
                            }
                            chunkBuffer = ByteBufUtil.readBytes(ctx.alloc(), in, toRead);
                            alreadyReadChunkSize = this.alreadyReadChunkSize + toRead;
                            this.alreadyReadChunkSize = alreadyReadChunkSize;
                            if (alreadyReadChunkSize >= this.contentLength) {
                                this.lastContent = new DefaultLastStompContentSubframe(chunkBuffer);
                                this.checkpoint(State.FINALIZE_FRAME_READ);
                                break Label_0474;
                            }
                            out.add(new DefaultStompContentSubframe(chunkBuffer));
                            return;
                        }
                        else {
                            nulIndex = ByteBufUtil.indexOf(in, in.readerIndex(), in.writerIndex(), (byte)0);
                            if (nulIndex == in.readerIndex()) {
                                this.checkpoint(State.FINALIZE_FRAME_READ);
                                break Label_0474;
                            }
                            if (nulIndex > 0) {
                                toRead = nulIndex - in.readerIndex();
                            }
                            else {
                                toRead = in.writerIndex() - in.readerIndex();
                            }
                            chunkBuffer = ByteBufUtil.readBytes(ctx.alloc(), in, toRead);
                            this.alreadyReadChunkSize += toRead;
                            if (nulIndex > 0) {
                                this.lastContent = new DefaultLastStompContentSubframe(chunkBuffer);
                                this.checkpoint(State.FINALIZE_FRAME_READ);
                                break Label_0474;
                            }
                            out.add(new DefaultStompContentSubframe(chunkBuffer));
                            return;
                        }
                        break;
                    }
                case FINALIZE_FRAME_READ: {
                    skipNullCharacter(in);
                    if (this.lastContent == null) {
                        this.lastContent = LastStompContentSubframe.EMPTY_LAST_CONTENT;
                    }
                    out.add(this.lastContent);
                    this.resetDecoder();
                    break;
                }
            }
        }
        catch (final Exception e2) {
            final StompContentSubframe errorContent = new DefaultLastStompContentSubframe(Unpooled.EMPTY_BUFFER);
            errorContent.setDecoderResult(DecoderResult.failure(e2));
            out.add(errorContent);
            this.checkpoint(State.BAD_FRAME);
        }
    }
    
    private StompCommand readCommand(final ByteBuf in) {
        String commandStr = readLine(in, this.maxLineLength);
        StompCommand command = null;
        try {
            command = StompCommand.valueOf(commandStr);
        }
        catch (final IllegalArgumentException ex) {}
        if (command == null) {
            commandStr = commandStr.toUpperCase(Locale.US);
            try {
                command = StompCommand.valueOf(commandStr);
            }
            catch (final IllegalArgumentException ex2) {}
        }
        if (command == null) {
            throw new DecoderException("failed to read command from channel");
        }
        return command;
    }
    
    private State readHeaders(final ByteBuf buffer, final StompHeaders headers) {
        while (true) {
            final String line = readLine(buffer, this.maxLineLength);
            if (line.isEmpty()) {
                break;
            }
            final String[] split = line.split(":");
            if (split.length != 2) {
                continue;
            }
            ((Headers<String, String, Headers>)headers).add(split[0], split[1]);
        }
        if (((Headers<AsciiString, V, T>)headers).contains(StompHeaders.CONTENT_LENGTH)) {
            this.contentLength = getContentLength(headers, 0L);
            if (this.contentLength == 0L) {
                return State.FINALIZE_FRAME_READ;
            }
        }
        return State.READ_CONTENT;
    }
    
    private static long getContentLength(final StompHeaders headers, final long defaultValue) {
        final long contentLength = ((Headers<AsciiString, V, T>)headers).getLong(StompHeaders.CONTENT_LENGTH, defaultValue);
        if (contentLength < 0L) {
            throw new DecoderException((Object)StompHeaders.CONTENT_LENGTH + " must be non-negative");
        }
        return contentLength;
    }
    
    private static void skipNullCharacter(final ByteBuf buffer) {
        final byte b = buffer.readByte();
        if (b != 0) {
            throw new IllegalStateException("unexpected byte in buffer " + b + " while expecting NULL byte");
        }
    }
    
    private static void skipControlCharacters(final ByteBuf buffer) {
        byte b;
        do {
            b = buffer.readByte();
        } while (b == 13 || b == 10);
        buffer.readerIndex(buffer.readerIndex() - 1);
    }
    
    private static String readLine(final ByteBuf buffer, final int maxLineLength) {
        final AppendableCharSequence buf = new AppendableCharSequence(128);
        int lineLength = 0;
        while (true) {
            byte nextByte = buffer.readByte();
            if (nextByte == 13) {
                nextByte = buffer.readByte();
                if (nextByte == 10) {
                    return buf.toString();
                }
                continue;
            }
            else {
                if (nextByte == 10) {
                    return buf.toString();
                }
                if (lineLength >= maxLineLength) {
                    throw new TooLongFrameException("An STOMP line is larger than " + maxLineLength + " bytes.");
                }
                ++lineLength;
                buf.append((char)nextByte);
            }
        }
    }
    
    private void resetDecoder() {
        this.checkpoint(State.SKIP_CONTROL_CHARACTERS);
        this.contentLength = -1L;
        this.alreadyReadChunkSize = 0;
        this.lastContent = null;
    }
    
    enum State
    {
        SKIP_CONTROL_CHARACTERS, 
        READ_HEADERS, 
        READ_CONTENT, 
        FINALIZE_FRAME_READ, 
        BAD_FRAME, 
        INVALID_CHUNK;
    }
}
