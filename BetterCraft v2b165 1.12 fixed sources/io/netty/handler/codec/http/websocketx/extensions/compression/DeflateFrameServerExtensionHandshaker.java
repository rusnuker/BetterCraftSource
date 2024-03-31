// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx.extensions.compression;

import java.util.Collections;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtension;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandshaker;

public final class DeflateFrameServerExtensionHandshaker implements WebSocketServerExtensionHandshaker
{
    static final String X_WEBKIT_DEFLATE_FRAME_EXTENSION = "x-webkit-deflate-frame";
    static final String DEFLATE_FRAME_EXTENSION = "deflate-frame";
    private final int compressionLevel;
    
    public DeflateFrameServerExtensionHandshaker() {
        this(6);
    }
    
    public DeflateFrameServerExtensionHandshaker(final int compressionLevel) {
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        this.compressionLevel = compressionLevel;
    }
    
    @Override
    public WebSocketServerExtension handshakeExtension(final WebSocketExtensionData extensionData) {
        if (!"x-webkit-deflate-frame".equals(extensionData.name()) && !"deflate-frame".equals(extensionData.name())) {
            return null;
        }
        if (extensionData.parameters().isEmpty()) {
            return new DeflateFrameServerExtension(this.compressionLevel, extensionData.name());
        }
        return null;
    }
    
    private static class DeflateFrameServerExtension implements WebSocketServerExtension
    {
        private final String extensionName;
        private final int compressionLevel;
        
        public DeflateFrameServerExtension(final int compressionLevel, final String extensionName) {
            this.extensionName = extensionName;
            this.compressionLevel = compressionLevel;
        }
        
        @Override
        public int rsv() {
            return 4;
        }
        
        @Override
        public WebSocketExtensionEncoder newExtensionEncoder() {
            return new PerFrameDeflateEncoder(this.compressionLevel, 15, false);
        }
        
        @Override
        public WebSocketExtensionDecoder newExtensionDecoder() {
            return new PerFrameDeflateDecoder(false);
        }
        
        @Override
        public WebSocketExtensionData newReponseData() {
            return new WebSocketExtensionData(this.extensionName, Collections.emptyMap());
        }
    }
}
