// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

public final class DefaultSelectStrategyFactory implements SelectStrategyFactory
{
    public static final SelectStrategyFactory INSTANCE;
    
    private DefaultSelectStrategyFactory() {
    }
    
    @Override
    public SelectStrategy newSelectStrategy() {
        return DefaultSelectStrategy.INSTANCE;
    }
    
    static {
        INSTANCE = new DefaultSelectStrategyFactory();
    }
}
