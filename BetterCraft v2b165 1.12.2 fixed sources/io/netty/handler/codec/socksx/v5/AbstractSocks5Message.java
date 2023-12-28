// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.socksx.SocksVersion;
import io.netty.handler.codec.socksx.AbstractSocksMessage;

public abstract class AbstractSocks5Message extends AbstractSocksMessage implements Socks5Message
{
    @Override
    public final SocksVersion version() {
        return SocksVersion.SOCKS5;
    }
}
