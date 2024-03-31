// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx;

public class WebSocket13FrameDecoder extends WebSocket08FrameDecoder
{
    public WebSocket13FrameDecoder(final boolean expectMaskedFrames, final boolean allowExtensions, final int maxFramePayloadLength) {
        this(expectMaskedFrames, allowExtensions, maxFramePayloadLength, false);
    }
    
    public WebSocket13FrameDecoder(final boolean expectMaskedFrames, final boolean allowExtensions, final int maxFramePayloadLength, final boolean allowMaskMismatch) {
        super(expectMaskedFrames, allowExtensions, maxFramePayloadLength, allowMaskMismatch);
    }
}
