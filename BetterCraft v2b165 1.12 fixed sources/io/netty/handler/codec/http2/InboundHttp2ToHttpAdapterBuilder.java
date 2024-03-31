// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

public final class InboundHttp2ToHttpAdapterBuilder extends AbstractInboundHttp2ToHttpAdapterBuilder<InboundHttp2ToHttpAdapter, InboundHttp2ToHttpAdapterBuilder>
{
    public InboundHttp2ToHttpAdapterBuilder(final Http2Connection connection) {
        super(connection);
    }
    
    public InboundHttp2ToHttpAdapterBuilder maxContentLength(final int maxContentLength) {
        return super.maxContentLength(maxContentLength);
    }
    
    public InboundHttp2ToHttpAdapterBuilder validateHttpHeaders(final boolean validate) {
        return super.validateHttpHeaders(validate);
    }
    
    public InboundHttp2ToHttpAdapterBuilder propagateSettings(final boolean propagate) {
        return super.propagateSettings(propagate);
    }
    
    public InboundHttp2ToHttpAdapter build() {
        return super.build();
    }
    
    @Override
    protected InboundHttp2ToHttpAdapter build(final Http2Connection connection, final int maxContentLength, final boolean validateHttpHeaders, final boolean propagateSettings) throws Exception {
        return new InboundHttp2ToHttpAdapter(connection, maxContentLength, validateHttpHeaders, propagateSettings);
    }
}
