// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx;

import io.netty.handler.codec.DecoderResult;

public abstract class AbstractSocksMessage implements SocksMessage
{
    private DecoderResult decoderResult;
    
    public AbstractSocksMessage() {
        this.decoderResult = DecoderResult.SUCCESS;
    }
    
    @Override
    public DecoderResult decoderResult() {
        return this.decoderResult;
    }
    
    @Override
    public void setDecoderResult(final DecoderResult decoderResult) {
        if (decoderResult == null) {
            throw new NullPointerException("decoderResult");
        }
        this.decoderResult = decoderResult;
    }
}
