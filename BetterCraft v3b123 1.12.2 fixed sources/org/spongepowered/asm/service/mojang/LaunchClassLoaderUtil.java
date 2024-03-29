// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.mojang;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.service.IClassTracker;

final class LaunchClassLoaderUtil implements IClassTracker
{
    private static final String CACHED_CLASSES_FIELD = "cachedClasses";
    private static final String INVALID_CLASSES_FIELD = "invalidClasses";
    private static final String CLASS_LOADER_EXCEPTIONS_FIELD = "classLoaderExceptions";
    private static final String TRANSFORMER_EXCEPTIONS_FIELD = "transformerExceptions";
    private final LaunchClassLoader classLoader;
    private final Map<String, Class<?>> cachedClasses;
    private final Set<String> invalidClasses;
    private final Set<String> classLoaderExceptions;
    private final Set<String> transformerExceptions;
    
    LaunchClassLoaderUtil(final LaunchClassLoader classLoader) {
        this.classLoader = classLoader;
        this.cachedClasses = getField(classLoader, "cachedClasses");
        this.invalidClasses = getField(classLoader, "invalidClasses");
        this.classLoaderExceptions = getField(classLoader, "classLoaderExceptions");
        this.transformerExceptions = getField(classLoader, "transformerExceptions");
    }
    
    LaunchClassLoader getClassLoader() {
        return this.classLoader;
    }
    
    @Override
    public boolean isClassLoaded(final String name) {
        return this.cachedClasses.containsKey(name);
    }
    
    @Override
    public String getClassRestrictions(final String className) {
        String restrictions = "";
        if (this.isClassClassLoaderExcluded(className, null)) {
            restrictions = "PACKAGE_CLASSLOADER_EXCLUSION";
        }
        if (this.isClassTransformerExcluded(className, null)) {
            restrictions = ((restrictions.length() > 0) ? (restrictions + ",") : "") + "PACKAGE_TRANSFORMER_EXCLUSION";
        }
        return restrictions;
    }
    
    boolean isClassExcluded(final String name, final String transformedName) {
        return this.isClassClassLoaderExcluded(name, transformedName) || this.isClassTransformerExcluded(name, transformedName);
    }
    
    boolean isClassClassLoaderExcluded(final String name, final String transformedName) {
        for (final String exception : this.getClassLoaderExceptions()) {
            if ((transformedName != null && transformedName.startsWith(exception)) || name.startsWith(exception)) {
                return true;
            }
        }
        return false;
    }
    
    boolean isClassTransformerExcluded(final String name, final String transformedName) {
        for (final String exception : this.getTransformerExceptions()) {
            if ((transformedName != null && transformedName.startsWith(exception)) || name.startsWith(exception)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void registerInvalidClass(final String name) {
        if (this.invalidClasses != null) {
            this.invalidClasses.add(name);
        }
    }
    
    Set<String> getClassLoaderExceptions() {
        if (this.classLoaderExceptions != null) {
            return this.classLoaderExceptions;
        }
        return Collections.emptySet();
    }
    
    Set<String> getTransformerExceptions() {
        if (this.transformerExceptions != null) {
            return this.transformerExceptions;
        }
        return Collections.emptySet();
    }
    
    private static <T> T getField(final LaunchClassLoader classLoader, final String fieldName) {
        try {
            final Field field = LaunchClassLoader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T)field.get(classLoader);
        }
        catch (final Exception ex) {
            return null;
        }
    }
}
