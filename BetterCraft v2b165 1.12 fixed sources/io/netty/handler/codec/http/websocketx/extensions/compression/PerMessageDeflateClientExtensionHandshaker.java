// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
import java.util.Iterator;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtension;
import java.util.Map;
import java.util.HashMap;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandshaker;

public final class PerMessageDeflateClientExtensionHandshaker implements WebSocketClientExtensionHandshaker
{
    private final int compressionLevel;
    private final boolean allowClientWindowSize;
    private final int requestedServerWindowSize;
    private final boolean allowClientNoContext;
    private final boolean requestedServerNoContext;
    
    public PerMessageDeflateClientExtensionHandshaker() {
        this(6, ZlibCodecFactory.isSupportingWindowSizeAndMemLevel(), 15, false, false);
    }
    
    public PerMessageDeflateClientExtensionHandshaker(final int compressionLevel, final boolean allowClientWindowSize, final int requestedServerWindowSize, final boolean allowClientNoContext, final boolean requestedServerNoContext) {
        if (requestedServerWindowSize > 15 || requestedServerWindowSize < 8) {
            throw new IllegalArgumentException("requestedServerWindowSize: " + requestedServerWindowSize + " (expected: 8-15)");
        }
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        this.compressionLevel = compressionLevel;
        this.allowClientWindowSize = allowClientWindowSize;
        this.requestedServerWindowSize = requestedServerWindowSize;
        this.allowClientNoContext = allowClientNoContext;
        this.requestedServerNoContext = requestedServerNoContext;
    }
    
    @Override
    public WebSocketExtensionData newRequestData() {
        final HashMap<String, String> parameters = new HashMap<String, String>(4);
        if (this.requestedServerWindowSize != 15) {
            parameters.put("server_no_context_takeover", null);
        }
        if (this.allowClientNoContext) {
            parameters.put("client_no_context_takeover", null);
        }
        if (this.requestedServerWindowSize != 15) {
            parameters.put("server_max_window_bits", Integer.toString(this.requestedServerWindowSize));
        }
        if (this.allowClientWindowSize) {
            parameters.put("client_max_window_bits", null);
        }
        return new WebSocketExtensionData("permessage-deflate", parameters);
    }
    
    @Override
    public WebSocketClientExtension handshakeExtension(final WebSocketExtensionData extensionData) {
        if (!"permessage-deflate".equals(extensionData.name())) {
            return null;
        }
        boolean succeed = true;
        int clientWindowSize = 15;
        int serverWindowSize = 15;
        boolean serverNoContext = false;
        boolean clientNoContext = false;
        final Iterator<Map.Entry<String, String>> parametersIterator = extensionData.parameters().entrySet().iterator();
        while (succeed && parametersIterator.hasNext()) {
            final Map.Entry<String, String> parameter = parametersIterator.next();
            if ("client_max_window_bits".equalsIgnoreCase(parameter.getKey())) {
                if (this.allowClientWindowSize) {
                    clientWindowSize = Integer.parseInt(parameter.getValue());
                }
                else {
                    succeed = false;
                }
            }
            else if ("server_max_window_bits".equalsIgnoreCase(parameter.getKey())) {
                serverWindowSize = Integer.parseInt(parameter.getValue());
                if (clientWindowSize <= 15 && clientWindowSize >= 8) {
                    continue;
                }
                succeed = false;
            }
            else if ("client_no_context_takeover".equalsIgnoreCase(parameter.getKey())) {
                if (this.allowClientNoContext) {
                    clientNoContext = true;
                }
                else {
                    succeed = false;
                }
            }
            else if ("server_no_context_takeover".equalsIgnoreCase(parameter.getKey())) {
                if (this.requestedServerNoContext) {
                    serverNoContext = true;
                }
                else {
                    succeed = false;
                }
            }
            else {
                succeed = false;
            }
        }
        if ((this.requestedServerNoContext && !serverNoContext) || this.requestedServerWindowSize != serverWindowSize) {
            succeed = false;
        }
        if (succeed) {
            return new PermessageDeflateExtension(serverNoContext, serverWindowSize, clientNoContext, clientWindowSize);
        }
        return null;
    }
    
    private final class PermessageDeflateExtension implements WebSocketClientExtension
    {
        private final boolean serverNoContext;
        private final int serverWindowSize;
        private final boolean clientNoContext;
        private final int clientWindowSize;
        
        @Override
        public int rsv() {
            return 4;
        }
        
        public PermessageDeflateExtension(final boolean serverNoContext, final int serverWindowSize, final boolean clientNoContext, final int clientWindowSize) {
            this.serverNoContext = serverNoContext;
            this.serverWindowSize = serverWindowSize;
            this.clientNoContext = clientNoContext;
            this.clientWindowSize = clientWindowSize;
        }
        
        @Override
        public WebSocketExtensionEncoder newExtensionEncoder() {
            return new PerMessageDeflateEncoder(PerMessageDeflateClientExtensionHandshaker.this.compressionLevel, this.serverWindowSize, this.serverNoContext);
        }
        
        @Override
        public WebSocketExtensionDecoder newExtensionDecoder() {
            return new PerMessageDeflateDecoder(this.clientNoContext);
        }
    }
}
