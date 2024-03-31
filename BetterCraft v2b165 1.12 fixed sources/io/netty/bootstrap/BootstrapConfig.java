// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.bootstrap;

import io.netty.resolver.AddressResolverGroup;
import java.net.SocketAddress;
import io.netty.channel.Channel;

public final class BootstrapConfig extends AbstractBootstrapConfig<Bootstrap, Channel>
{
    BootstrapConfig(final Bootstrap bootstrap) {
        super(bootstrap);
    }
    
    public SocketAddress remoteAddress() {
        return ((Bootstrap)this.bootstrap).remoteAddress();
    }
    
    public AddressResolverGroup<?> resolver() {
        return ((Bootstrap)this.bootstrap).resolver();
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(super.toString());
        buf.setLength(buf.length() - 1);
        buf.append(", resolver: ").append(this.resolver());
        final SocketAddress remoteAddress = this.remoteAddress();
        if (remoteAddress != null) {
            buf.append(", remoteAddress: ").append(remoteAddress);
        }
        return buf.append(')').toString();
    }
}
