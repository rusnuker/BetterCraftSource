// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.bootstrap;

import io.netty.util.internal.StringUtil;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.channel.ChannelHandler;
import java.net.SocketAddress;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.Channel;

public abstract class AbstractBootstrapConfig<B extends AbstractBootstrap<B, C>, C extends Channel>
{
    protected final B bootstrap;
    
    protected AbstractBootstrapConfig(final B bootstrap) {
        this.bootstrap = ObjectUtil.checkNotNull(bootstrap, "bootstrap");
    }
    
    public final SocketAddress localAddress() {
        return this.bootstrap.localAddress();
    }
    
    public final ChannelFactory<? extends C> channelFactory() {
        return this.bootstrap.channelFactory();
    }
    
    public final ChannelHandler handler() {
        return this.bootstrap.handler();
    }
    
    public final Map<ChannelOption<?>, Object> options() {
        return this.bootstrap.options();
    }
    
    public final Map<AttributeKey<?>, Object> attrs() {
        return this.bootstrap.attrs();
    }
    
    public final EventLoopGroup group() {
        return this.bootstrap.group();
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append('(');
        final EventLoopGroup group = this.group();
        if (group != null) {
            buf.append("group: ").append(StringUtil.simpleClassName(group)).append(", ");
        }
        final ChannelFactory<? extends C> factory = this.channelFactory();
        if (factory != null) {
            buf.append("channelFactory: ").append(factory).append(", ");
        }
        final SocketAddress localAddress = this.localAddress();
        if (localAddress != null) {
            buf.append("localAddress: ").append(localAddress).append(", ");
        }
        final Map<ChannelOption<?>, Object> options = this.options();
        if (!options.isEmpty()) {
            buf.append("options: ").append(options).append(", ");
        }
        final Map<AttributeKey<?>, Object> attrs = this.attrs();
        if (!attrs.isEmpty()) {
            buf.append("attrs: ").append(attrs).append(", ");
        }
        final ChannelHandler handler = this.handler();
        if (handler != null) {
            buf.append("handler: ").append(handler).append(", ");
        }
        if (buf.charAt(buf.length() - 1) == '(') {
            buf.append(')');
        }
        else {
            buf.setCharAt(buf.length() - 2, ')');
            buf.setLength(buf.length() - 1);
        }
        return buf.toString();
    }
}
