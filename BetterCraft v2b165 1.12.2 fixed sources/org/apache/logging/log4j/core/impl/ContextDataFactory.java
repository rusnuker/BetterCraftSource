// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import java.lang.invoke.MethodType;
import org.apache.logging.log4j.util.LoaderUtil;
import java.lang.invoke.MethodHandle;
import org.apache.logging.log4j.util.StringMap;
import java.lang.invoke.MethodHandles;

public class ContextDataFactory
{
    private static final MethodHandles.Lookup LOOKUP;
    private static final String CLASS_NAME;
    private static final Class<? extends StringMap> CACHED_CLASS;
    private static final MethodHandle DEFAULT_CONSTRUCTOR;
    private static final MethodHandle INITIAL_CAPACITY_CONSTRUCTOR;
    private static final StringMap EMPTY_STRING_MAP;
    
    private static Class<? extends StringMap> createCachedClass(final String className) {
        if (className == null) {
            return null;
        }
        try {
            return LoaderUtil.loadClass(className).asSubclass(StringMap.class);
        }
        catch (final Exception any) {
            return null;
        }
    }
    
    private static MethodHandle createDefaultConstructor(final Class<? extends StringMap> cachedClass) {
        if (cachedClass == null) {
            return null;
        }
        try {
            return ContextDataFactory.LOOKUP.findConstructor(cachedClass, MethodType.methodType(Void.TYPE));
        }
        catch (final NoSuchMethodException | IllegalAccessException ignored) {
            return null;
        }
    }
    
    private static MethodHandle createInitialCapacityConstructor(final Class<? extends StringMap> cachedClass) {
        if (cachedClass == null) {
            return null;
        }
        try {
            return ContextDataFactory.LOOKUP.findConstructor(cachedClass, MethodType.methodType(Void.TYPE, Integer.TYPE));
        }
        catch (final NoSuchMethodException | IllegalAccessException ignored) {
            return null;
        }
    }
    
    public static StringMap createContextData() {
        if (ContextDataFactory.DEFAULT_CONSTRUCTOR == null) {
            return new SortedArrayStringMap();
        }
        try {
            return ContextDataFactory.DEFAULT_CONSTRUCTOR.invoke();
        }
        catch (final Throwable ignored) {
            return new SortedArrayStringMap();
        }
    }
    
    public static StringMap createContextData(final int initialCapacity) {
        if (ContextDataFactory.INITIAL_CAPACITY_CONSTRUCTOR == null) {
            return new SortedArrayStringMap(initialCapacity);
        }
        try {
            return ContextDataFactory.INITIAL_CAPACITY_CONSTRUCTOR.invoke(initialCapacity);
        }
        catch (final Throwable ignored) {
            return new SortedArrayStringMap(initialCapacity);
        }
    }
    
    public static StringMap emptyFrozenContextData() {
        return ContextDataFactory.EMPTY_STRING_MAP;
    }
    
    static {
        LOOKUP = MethodHandles.lookup();
        CLASS_NAME = PropertiesUtil.getProperties().getStringProperty("log4j2.ContextData");
        CACHED_CLASS = createCachedClass(ContextDataFactory.CLASS_NAME);
        DEFAULT_CONSTRUCTOR = createDefaultConstructor(ContextDataFactory.CACHED_CLASS);
        INITIAL_CAPACITY_CONSTRUCTOR = createInitialCapacityConstructor(ContextDataFactory.CACHED_CLASS);
        (EMPTY_STRING_MAP = createContextData(1)).freeze();
    }
}
