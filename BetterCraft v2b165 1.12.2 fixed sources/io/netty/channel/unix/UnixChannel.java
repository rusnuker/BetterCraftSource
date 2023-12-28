// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.unix;

import io.netty.channel.Channel;

public interface UnixChannel extends Channel
{
    FileDescriptor fd();
}
