// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandshaker;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandler;

public class WebSocketServerCompressionHandler extends WebSocketServerExtensionHandler
{
    public WebSocketServerCompressionHandler() {
        super(new WebSocketServerExtensionHandshaker[] { new PerMessageDeflateServerExtensionHandshaker(), new DeflateFrameServerExtensionHandshaker() });
    }
}
