// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.util.concurrent;

import com.google.errorprone.annotations.ForOverride;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class AbstractTransformFuture<I, O, F, T> extends TrustedFuture<O> implements Runnable
{
    @Nullable
    ListenableFuture<? extends I> inputFuture;
    @Nullable
    F function;
    
    static <I, O> ListenableFuture<O> create(final ListenableFuture<I> input, final AsyncFunction<? super I, ? extends O> function) {
        final AsyncTransformFuture<I, O> output = new AsyncTransformFuture<I, O>((ListenableFuture<? extends I>)input, function);
        input.addListener(output, MoreExecutors.directExecutor());
        return (ListenableFuture<O>)output;
    }
    
    static <I, O> ListenableFuture<O> create(final ListenableFuture<I> input, final AsyncFunction<? super I, ? extends O> function, final Executor executor) {
        Preconditions.checkNotNull(executor);
        final AsyncTransformFuture<I, O> output = new AsyncTransformFuture<I, O>((ListenableFuture<? extends I>)input, function);
        input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
        return (ListenableFuture<O>)output;
    }
    
    static <I, O> ListenableFuture<O> create(final ListenableFuture<I> input, final Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(function);
        final TransformFuture<I, O> output = new TransformFuture<I, O>((ListenableFuture<? extends I>)input, function);
        input.addListener(output, MoreExecutors.directExecutor());
        return (ListenableFuture<O>)output;
    }
    
    static <I, O> ListenableFuture<O> create(final ListenableFuture<I> input, final Function<? super I, ? extends O> function, final Executor executor) {
        Preconditions.checkNotNull(function);
        final TransformFuture<I, O> output = new TransformFuture<I, O>((ListenableFuture<? extends I>)input, function);
        input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
        return (ListenableFuture<O>)output;
    }
    
    AbstractTransformFuture(final ListenableFuture<? extends I> inputFuture, final F function) {
        this.inputFuture = Preconditions.checkNotNull(inputFuture);
        this.function = Preconditions.checkNotNull(function);
    }
    
    @Override
    public final void run() {
        final ListenableFuture<? extends I> localInputFuture = this.inputFuture;
        final F localFunction = this.function;
        if (this.isCancelled() | localInputFuture == null | localFunction == null) {
            return;
        }
        this.inputFuture = null;
        this.function = null;
        I sourceResult;
        try {
            sourceResult = Futures.getDone((Future<I>)localInputFuture);
        }
        catch (final CancellationException e) {
            this.cancel(false);
            return;
        }
        catch (final ExecutionException e2) {
            this.setException(e2.getCause());
            return;
        }
        catch (final RuntimeException e3) {
            this.setException(e3);
            return;
        }
        catch (final Error e4) {
            this.setException(e4);
            return;
        }
        T transformResult;
        try {
            transformResult = this.doTransform(localFunction, sourceResult);
        }
        catch (final UndeclaredThrowableException e5) {
            this.setException(e5.getCause());
            return;
        }
        catch (final Throwable t) {
            this.setException(t);
            return;
        }
        this.setResult(transformResult);
    }
    
    @Nullable
    @ForOverride
    abstract T doTransform(final F p0, @Nullable final I p1) throws Exception;
    
    @ForOverride
    abstract void setResult(@Nullable final T p0);
    
    @Override
    protected final void afterDone() {
        this.maybePropagateCancellation(this.inputFuture);
        this.inputFuture = null;
        this.function = null;
    }
    
    private static final class AsyncTransformFuture<I, O> extends AbstractTransformFuture<I, O, AsyncFunction<? super I, ? extends O>, ListenableFuture<? extends O>>
    {
        AsyncTransformFuture(final ListenableFuture<? extends I> inputFuture, final AsyncFunction<? super I, ? extends O> function) {
            super(inputFuture, function);
        }
        
        @Override
        ListenableFuture<? extends O> doTransform(final AsyncFunction<? super I, ? extends O> function, @Nullable final I input) throws Exception {
            final ListenableFuture<? extends O> outputFuture = function.apply(input);
            Preconditions.checkNotNull(outputFuture, (Object)"AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)?");
            return outputFuture;
        }
        
        @Override
        void setResult(final ListenableFuture<? extends O> result) {
            this.setFuture((ListenableFuture<? extends O>)result);
        }
    }
    
    private static final class TransformFuture<I, O> extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O>
    {
        TransformFuture(final ListenableFuture<? extends I> inputFuture, final Function<? super I, ? extends O> function) {
            super(inputFuture, function);
        }
        
        @Nullable
        @Override
        O doTransform(final Function<? super I, ? extends O> function, @Nullable final I input) {
            return (O)function.apply(input);
        }
        
        @Override
        void setResult(@Nullable final O result) {
            this.set((O)result);
        }
    }
}
