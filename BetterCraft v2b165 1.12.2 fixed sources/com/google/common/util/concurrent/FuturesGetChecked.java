// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.util.concurrent;

import java.util.concurrent.CopyOnWriteArraySet;
import java.lang.ref.WeakReference;
import java.util.Set;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.VisibleForTesting;
import java.util.concurrent.ExecutionException;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Future;
import java.lang.reflect.Constructor;
import com.google.common.collect.Ordering;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
final class FuturesGetChecked
{
    private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST;
    
    @CanIgnoreReturnValue
    static <V, X extends Exception> V getChecked(final Future<V> future, final Class<X> exceptionClass) throws X, Exception {
        return getChecked(bestGetCheckedTypeValidator(), future, exceptionClass);
    }
    
    @CanIgnoreReturnValue
    @VisibleForTesting
    static <V, X extends Exception> V getChecked(final GetCheckedTypeValidator validator, final Future<V> future, final Class<X> exceptionClass) throws X, Exception {
        validator.validateClass(exceptionClass);
        try {
            return future.get();
        }
        catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw newWithCause(exceptionClass, e);
        }
        catch (final ExecutionException e2) {
            wrapAndThrowExceptionOrError(e2.getCause(), exceptionClass);
            throw new AssertionError();
        }
    }
    
    @CanIgnoreReturnValue
    static <V, X extends Exception> V getChecked(final Future<V> future, final Class<X> exceptionClass, final long timeout, final TimeUnit unit) throws X, Exception {
        bestGetCheckedTypeValidator().validateClass(exceptionClass);
        try {
            return future.get(timeout, unit);
        }
        catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw newWithCause(exceptionClass, e);
        }
        catch (final TimeoutException e2) {
            throw newWithCause(exceptionClass, e2);
        }
        catch (final ExecutionException e3) {
            wrapAndThrowExceptionOrError(e3.getCause(), exceptionClass);
            throw new AssertionError();
        }
    }
    
    private static GetCheckedTypeValidator bestGetCheckedTypeValidator() {
        return GetCheckedTypeValidatorHolder.BEST_VALIDATOR;
    }
    
    @VisibleForTesting
    static GetCheckedTypeValidator weakSetValidator() {
        return GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
    }
    
    @VisibleForTesting
    static GetCheckedTypeValidator classValueValidator() {
        return GetCheckedTypeValidatorHolder.ClassValueValidator.INSTANCE;
    }
    
    private static <X extends Exception> void wrapAndThrowExceptionOrError(final Throwable cause, final Class<X> exceptionClass) throws X, Exception {
        if (cause instanceof Error) {
            throw new ExecutionError((Error)cause);
        }
        if (cause instanceof RuntimeException) {
            throw new UncheckedExecutionException(cause);
        }
        throw newWithCause(exceptionClass, cause);
    }
    
    private static boolean hasConstructorUsableByGetChecked(final Class<? extends Exception> exceptionClass) {
        try {
            final Exception unused = newWithCause(exceptionClass, new Exception());
            return true;
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    private static <X extends Exception> X newWithCause(final Class<X> exceptionClass, final Throwable cause) {
        final List<Constructor<X>> constructors = Arrays.asList((Constructor<X>[])exceptionClass.getConstructors());
        for (final Constructor<X> constructor : preferringStrings(constructors)) {
            final X instance = newFromConstructor(constructor, cause);
            if (instance != null) {
                if (instance.getCause() == null) {
                    instance.initCause(cause);
                }
                return instance;
            }
        }
        throw new IllegalArgumentException("No appropriate constructor for exception of type " + exceptionClass + " in response to chained exception", cause);
    }
    
    private static <X extends Exception> List<Constructor<X>> preferringStrings(final List<Constructor<X>> constructors) {
        return FuturesGetChecked.WITH_STRING_PARAM_FIRST.sortedCopy(constructors);
    }
    
    @Nullable
    private static <X> X newFromConstructor(final Constructor<X> constructor, final Throwable cause) {
        final Class<?>[] paramTypes = constructor.getParameterTypes();
        final Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; ++i) {
            final Class<?> paramType = paramTypes[i];
            if (paramType.equals(String.class)) {
                params[i] = cause.toString();
            }
            else {
                if (!paramType.equals(Throwable.class)) {
                    return null;
                }
                params[i] = cause;
            }
        }
        try {
            return constructor.newInstance(params);
        }
        catch (final IllegalArgumentException e) {
            return null;
        }
        catch (final InstantiationException e2) {
            return null;
        }
        catch (final IllegalAccessException e3) {
            return null;
        }
        catch (final InvocationTargetException e4) {
            return null;
        }
    }
    
    @VisibleForTesting
    static boolean isCheckedException(final Class<? extends Exception> type) {
        return !RuntimeException.class.isAssignableFrom(type);
    }
    
    @VisibleForTesting
    static void checkExceptionClassValidity(final Class<? extends Exception> exceptionClass) {
        Preconditions.checkArgument(isCheckedException(exceptionClass), "Futures.getChecked exception type (%s) must not be a RuntimeException", exceptionClass);
        Preconditions.checkArgument(hasConstructorUsableByGetChecked(exceptionClass), "Futures.getChecked exception type (%s) must be an accessible class with an accessible constructor whose parameters (if any) must be of type String and/or Throwable", exceptionClass);
    }
    
    private FuturesGetChecked() {
    }
    
    static {
        WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf((Function<Object, ? extends Comparable>)new Function<Constructor<?>, Boolean>() {
            @Override
            public Boolean apply(final Constructor<?> input) {
                return Arrays.asList(input.getParameterTypes()).contains(String.class);
            }
        }).reverse();
    }
    
    @VisibleForTesting
    static class GetCheckedTypeValidatorHolder
    {
        static final String CLASS_VALUE_VALIDATOR_NAME;
        static final GetCheckedTypeValidator BEST_VALIDATOR;
        
        static GetCheckedTypeValidator getBestValidator() {
            try {
                final Class<?> theClass = Class.forName(GetCheckedTypeValidatorHolder.CLASS_VALUE_VALIDATOR_NAME);
                return (GetCheckedTypeValidator)theClass.getEnumConstants()[0];
            }
            catch (final Throwable t) {
                return FuturesGetChecked.weakSetValidator();
            }
        }
        
        static {
            CLASS_VALUE_VALIDATOR_NAME = GetCheckedTypeValidatorHolder.class.getName() + "$ClassValueValidator";
            BEST_VALIDATOR = getBestValidator();
        }
        
        @IgnoreJRERequirement
        enum ClassValueValidator implements GetCheckedTypeValidator
        {
            INSTANCE;
            
            private static final ClassValue<Boolean> isValidClass;
            
            @Override
            public void validateClass(final Class<? extends Exception> exceptionClass) {
                ClassValueValidator.isValidClass.get(exceptionClass);
            }
            
            static {
                isValidClass = new ClassValue<Boolean>() {
                    @Override
                    protected Boolean computeValue(final Class<?> type) {
                        FuturesGetChecked.checkExceptionClassValidity(type.asSubclass(Exception.class));
                        return true;
                    }
                };
            }
        }
        
        enum WeakSetValidator implements GetCheckedTypeValidator
        {
            INSTANCE;
            
            private static final Set<WeakReference<Class<? extends Exception>>> validClasses;
            
            @Override
            public void validateClass(final Class<? extends Exception> exceptionClass) {
                for (final WeakReference<Class<? extends Exception>> knownGood : WeakSetValidator.validClasses) {
                    if (exceptionClass.equals(knownGood.get())) {
                        return;
                    }
                }
                FuturesGetChecked.checkExceptionClassValidity(exceptionClass);
                if (WeakSetValidator.validClasses.size() > 1000) {
                    WeakSetValidator.validClasses.clear();
                }
                WeakSetValidator.validClasses.add(new WeakReference<Class<? extends Exception>>(exceptionClass));
            }
            
            static {
                validClasses = new CopyOnWriteArraySet<WeakReference<Class<? extends Exception>>>();
            }
        }
    }
    
    @VisibleForTesting
    interface GetCheckedTypeValidator
    {
        void validateClass(final Class<? extends Exception> p0);
    }
}
