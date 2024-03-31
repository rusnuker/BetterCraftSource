// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.stomp;

import io.netty.handler.codec.DecoderResult;

public class DefaultStompHeadersSubframe implements StompHeadersSubframe
{
    protected final StompCommand command;
    protected DecoderResult decoderResult;
    protected final StompHeaders headers;
    
    public DefaultStompHeadersSubframe(final StompCommand command) {
        this.decoderResult = DecoderResult.SUCCESS;
        this.headers = new DefaultStompHeaders();
        if (command == null) {
            throw new NullPointerException("command");
        }
        this.command = command;
    }
    
    @Override
    public StompCommand command() {
        return this.command;
    }
    
    @Override
    public StompHeaders headers() {
        return this.headers;
    }
    
    @Override
    public DecoderResult decoderResult() {
        return this.decoderResult;
    }
    
    @Override
    public void setDecoderResult(final DecoderResult decoderResult) {
        this.decoderResult = decoderResult;
    }
    
    @Override
    public String toString() {
        return "StompFrame{command=" + this.command + ", headers=" + this.headers + '}';
    }
}
