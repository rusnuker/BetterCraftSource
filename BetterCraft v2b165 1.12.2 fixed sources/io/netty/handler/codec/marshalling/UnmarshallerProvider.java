// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.marshalling;

import org.jboss.marshalling.Unmarshaller;
import io.netty.channel.ChannelHandlerContext;

public interface UnmarshallerProvider
{
    Unmarshaller getUnmarshaller(final ChannelHandlerContext p0) throws Exception;
}
