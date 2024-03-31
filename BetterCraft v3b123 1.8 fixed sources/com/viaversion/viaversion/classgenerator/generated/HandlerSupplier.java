// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.classgenerator.generated;

import com.viaversion.viaversion.bukkit.handlers.BukkitDecodeHandler;
import com.viaversion.viaversion.bukkit.handlers.BukkitEncodeHandler;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageEncoder;
import com.viaversion.viaversion.api.connection.UserConnection;

public interface HandlerSupplier
{
    MessageToMessageEncoder<ByteBuf> newEncodeHandler(final UserConnection p0);
    
    MessageToMessageDecoder<ByteBuf> newDecodeHandler(final UserConnection p0);
    
    public static final class DefaultHandlerSupplier implements HandlerSupplier
    {
        @Override
        public MessageToMessageEncoder<ByteBuf> newEncodeHandler(final UserConnection connection) {
            return new BukkitEncodeHandler(connection);
        }
        
        @Override
        public MessageToMessageDecoder<ByteBuf> newDecodeHandler(final UserConnection connection) {
            return new BukkitDecodeHandler(connection);
        }
    }
}
