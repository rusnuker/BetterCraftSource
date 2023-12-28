// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v4;

import io.netty.handler.codec.socksx.SocksVersion;
import io.netty.handler.codec.socksx.AbstractSocksMessage;

public abstract class AbstractSocks4Message extends AbstractSocksMessage implements Socks4Message
{
    @Override
    public final SocksVersion version() {
        return SocksVersion.SOCKS4a;
    }
}
