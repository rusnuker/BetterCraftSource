// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.group;

import java.util.Collections;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import java.util.Iterator;

final class VoidChannelGroupFuture implements ChannelGroupFuture
{
    private static final Iterator<ChannelFuture> EMPTY;
    private final ChannelGroup group;
    
    VoidChannelGroupFuture(final ChannelGroup group) {
        this.group = group;
    }
    
    @Override
    public ChannelGroup group() {
        return this.group;
    }
    
    @Override
    public ChannelFuture find(final Channel channel) {
        return null;
    }
    
    @Override
    public boolean isSuccess() {
        return false;
    }
    
    @Override
    public ChannelGroupException cause() {
        return null;
    }
    
    @Override
    public boolean isPartialSuccess() {
        return false;
    }
    
    @Override
    public boolean isPartialFailure() {
        return false;
    }
    
    @Override
    public ChannelGroupFuture addListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        throw reject();
    }
    
    @Override
    public ChannelGroupFuture addListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        throw reject();
    }
    
    @Override
    public ChannelGroupFuture removeListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        throw reject();
    }
    
    @Override
    public ChannelGroupFuture removeListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        throw reject();
    }
    
    @Override
    public ChannelGroupFuture await() {
        throw reject();
    }
    
    @Override
    public ChannelGroupFuture awaitUninterruptibly() {
        throw reject();
    }
    
    @Override
    public ChannelGroupFuture syncUninterruptibly() {
        throw reject();
    }
    
    @Override
    public ChannelGroupFuture sync() {
        throw reject();
    }
    
    @Override
    public Iterator<ChannelFuture> iterator() {
        return VoidChannelGroupFuture.EMPTY;
    }
    
    @Override
    public boolean isCancellable() {
        return false;
    }
    
    @Override
    public boolean await(final long timeout, final TimeUnit unit) {
        throw reject();
    }
    
    @Override
    public boolean await(final long timeoutMillis) {
        throw reject();
    }
    
    @Override
    public boolean awaitUninterruptibly(final long timeout, final TimeUnit unit) {
        throw reject();
    }
    
    @Override
    public boolean awaitUninterruptibly(final long timeoutMillis) {
        throw reject();
    }
    
    @Override
    public Void getNow() {
        return null;
    }
    
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return false;
    }
    
    @Override
    public boolean isCancelled() {
        return false;
    }
    
    @Override
    public boolean isDone() {
        return false;
    }
    
    @Override
    public Void get() {
        throw reject();
    }
    
    @Override
    public Void get(final long timeout, final TimeUnit unit) {
        throw reject();
    }
    
    private static RuntimeException reject() {
        return new IllegalStateException("void future");
    }
    
    static {
        EMPTY = Collections.emptyList().iterator();
    }
}
