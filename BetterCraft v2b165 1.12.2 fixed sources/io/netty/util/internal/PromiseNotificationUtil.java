// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.concurrent.Promise;

public final class PromiseNotificationUtil
{
    private PromiseNotificationUtil() {
    }
    
    public static void tryCancel(final Promise<?> p, final InternalLogger logger) {
        if (!p.cancel(false) && logger != null) {
            final Throwable err = p.cause();
            if (err == null) {
                logger.warn("Failed to cancel promise because it has succeeded already: {}", p);
            }
            else {
                logger.warn("Failed to cancel promise because it has failed already: {}, unnotified cause:", p, err);
            }
        }
    }
    
    public static <V> void trySuccess(final Promise<? super V> p, final V result, final InternalLogger logger) {
        if (!p.trySuccess(result) && logger != null) {
            final Throwable err = p.cause();
            if (err == null) {
                logger.warn("Failed to mark a promise as success because it has succeeded already: {}", p);
            }
            else {
                logger.warn("Failed to mark a promise as success because it has failed already: {}, unnotified cause:", p, err);
            }
        }
    }
    
    public static void tryFailure(final Promise<?> p, final Throwable cause, final InternalLogger logger) {
        if (!p.tryFailure(cause) && logger != null) {
            final Throwable err = p.cause();
            if (err == null) {
                logger.warn("Failed to mark a promise as failure because it has succeeded already: {}", p, cause);
            }
            else {
                logger.warn("Failed to mark a promise as failure because it has failed already: {}, unnotified cause: {}", p, ThrowableUtil.stackTraceToString(err), cause);
            }
        }
    }
}
