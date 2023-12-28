// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx.extensions;

public interface WebSocketClientExtensionHandshaker
{
    WebSocketExtensionData newRequestData();
    
    WebSocketClientExtension handshakeExtension(final WebSocketExtensionData p0);
}
