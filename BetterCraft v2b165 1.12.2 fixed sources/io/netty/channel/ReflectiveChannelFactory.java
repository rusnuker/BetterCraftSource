// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.internal.StringUtil;

public class ReflectiveChannelFactory<T extends Channel> implements ChannelFactory<T>
{
    private final Class<? extends T> clazz;
    
    public ReflectiveChannelFactory(final Class<? extends T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        this.clazz = clazz;
    }
    
    @Override
    public T newChannel() {
        try {
            return (T)this.clazz.newInstance();
        }
        catch (final Throwable t) {
            throw new ChannelException("Unable to create Channel from class " + this.clazz, t);
        }
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this.clazz) + ".class";
    }
}
