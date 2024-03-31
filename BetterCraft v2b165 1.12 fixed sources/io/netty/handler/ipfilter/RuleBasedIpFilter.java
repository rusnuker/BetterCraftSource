// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ipfilter;

import java.net.SocketAddress;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import java.net.InetSocketAddress;

@ChannelHandler.Sharable
public class RuleBasedIpFilter extends AbstractRemoteAddressFilter<InetSocketAddress>
{
    private final IpFilterRule[] rules;
    
    public RuleBasedIpFilter(final IpFilterRule... rules) {
        if (rules == null) {
            throw new NullPointerException("rules");
        }
        this.rules = rules;
    }
    
    @Override
    protected boolean accept(final ChannelHandlerContext ctx, final InetSocketAddress remoteAddress) throws Exception {
        for (final IpFilterRule rule : this.rules) {
            if (rule == null) {
                break;
            }
            if (rule.matches(remoteAddress)) {
                return rule.ruleType() == IpFilterRuleType.ACCEPT;
            }
        }
        return true;
    }
}
