// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.util.concurrent;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import com.google.common.collect.ImmutableCollection;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class CombinedFuture<V> extends AggregateFuture<Object, V>
{
    CombinedFuture(final ImmutableCollection<? extends ListenableFuture<?>> futures, final boolean allMustSucceed, final Executor listenerExecutor, final AsyncCallable<V> callable) {
        this.init(new CombinedFutureRunningState(futures, allMustSucceed, new AsyncCallableInterruptibleTask(callable, listenerExecutor)));
    }
    
    CombinedFuture(final ImmutableCollection<? extends ListenableFuture<?>> futures, final boolean allMustSucceed, final Executor listenerExecutor, final Callable<V> callable) {
        this.init(new CombinedFutureRunningState(futures, allMustSucceed, new CallableInterruptibleTask(callable, listenerExecutor)));
    }
    
    private final class CombinedFutureRunningState extends RunningState
    {
        private CombinedFutureInterruptibleTask task;
        
        CombinedFutureRunningState(final ImmutableCollection<? extends ListenableFuture<?>> futures, final boolean allMustSucceed, final CombinedFutureInterruptibleTask task) {
            super((ImmutableCollection<? extends ListenableFuture<? extends InputT>>)futures, allMustSucceed, false);
            this.task = task;
        }
        
        @Override
        void collectOneValue(final boolean allMustSucceed, final int index, @Nullable final Object returnValue) {
        }
        
        @Override
        void handleAllCompleted() {
            final CombinedFutureInterruptibleTask localTask = this.task;
            if (localTask != null) {
                localTask.execute();
            }
            else {
                Preconditions.checkState(CombinedFuture.this.isDone());
            }
        }
        
        @Override
        void releaseResourcesAfterFailure() {
            super.releaseResourcesAfterFailure();
            this.task = null;
        }
        
        @Override
        void interruptTask() {
            final CombinedFutureInterruptibleTask localTask = this.task;
            if (localTask != null) {
                localTask.interruptTask();
            }
        }
    }
    
    private abstract class CombinedFutureInterruptibleTask extends InterruptibleTask
    {
        private final Executor listenerExecutor;
        volatile boolean thrownByExecute;
        
        public CombinedFutureInterruptibleTask(final Executor listenerExecutor) {
            this.thrownByExecute = true;
            this.listenerExecutor = Preconditions.checkNotNull(listenerExecutor);
        }
        
        @Override
        final void runInterruptibly() {
            this.thrownByExecute = false;
            if (!CombinedFuture.this.isDone()) {
                try {
                    this.setValue();
                }
                catch (final ExecutionException e) {
                    CombinedFuture.this.setException(e.getCause());
                }
                catch (final CancellationException e2) {
                    CombinedFuture.this.cancel(false);
                }
                catch (final Throwable e3) {
                    CombinedFuture.this.setException(e3);
                }
            }
        }
        
        @Override
        final boolean wasInterrupted() {
            return CombinedFuture.this.wasInterrupted();
        }
        
        final void execute() {
            try {
                this.listenerExecutor.execute(this);
            }
            catch (final RejectedExecutionException e) {
                if (this.thrownByExecute) {
                    CombinedFuture.this.setException(e);
                }
            }
        }
        
        abstract void setValue() throws Exception;
    }
    
    private final class AsyncCallableInterruptibleTask extends CombinedFutureInterruptibleTask
    {
        private final AsyncCallable<V> callable;
        
        public AsyncCallableInterruptibleTask(final AsyncCallable<V> callable, final Executor listenerExecutor) {
            super(listenerExecutor);
            this.callable = Preconditions.checkNotNull(callable);
        }
        
        @Override
        void setValue() throws Exception {
            CombinedFuture.this.setFuture(this.callable.call());
        }
    }
    
    private final class CallableInterruptibleTask extends CombinedFutureInterruptibleTask
    {
        private final Callable<V> callable;
        
        public CallableInterruptibleTask(final Callable<V> callable, final Executor listenerExecutor) {
            super(listenerExecutor);
            this.callable = Preconditions.checkNotNull(callable);
        }
        
        @Override
        void setValue() throws Exception {
            CombinedFuture.this.set(this.callable.call());
        }
    }
}
