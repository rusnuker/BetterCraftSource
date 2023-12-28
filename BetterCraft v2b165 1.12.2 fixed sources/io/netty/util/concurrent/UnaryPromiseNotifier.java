// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;

public final class UnaryPromiseNotifier<T> implements FutureListener<T>
{
    private static final InternalLogger logger;
    private final Promise<? super T> promise;
    
    public UnaryPromiseNotifier(final Promise<? super T> promise) {
        this.promise = ObjectUtil.checkNotNull(promise, "promise");
    }
    
    @Override
    public void operationComplete(final Future<T> future) throws Exception {
        cascadeTo(future, this.promise);
    }
    
    public static <X> void cascadeTo(final Future<X> completedFuture, final Promise<? super X> promise) {
        if (completedFuture.isSuccess()) {
            if (!promise.trySuccess(completedFuture.getNow())) {
                UnaryPromiseNotifier.logger.warn("Failed to mark a promise as success because it is done already: {}", promise);
            }
        }
        else if (completedFuture.isCancelled()) {
            if (!promise.cancel(false)) {
                UnaryPromiseNotifier.logger.warn("Failed to cancel a promise because it is done already: {}", promise);
            }
        }
        else if (!promise.tryFailure(completedFuture.cause())) {
            UnaryPromiseNotifier.logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, completedFuture.cause());
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(UnaryPromiseNotifier.class);
    }
}
