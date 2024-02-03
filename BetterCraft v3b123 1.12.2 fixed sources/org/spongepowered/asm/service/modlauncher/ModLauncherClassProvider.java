// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.modlauncher;

import cpw.mods.modlauncher.Launcher;
import java.lang.reflect.Method;
import java.net.URL;
import org.spongepowered.asm.service.IClassProvider;

class ModLauncherClassProvider implements IClassProvider
{
    private static final String GET_SYSTEM_CLASS_PATH_METHOD = "getSystemClassPathURLs";
    private static final String JAVA9_CLASS_LOADER_UTIL_CLASS = "cpw.mods.gross.Java9ClassLoaderUtil";
    
    @Deprecated
    @Override
    public URL[] getClassPath() {
        try {
            final Class<?> clJava9ClassLoaderUtil = this.findClass("cpw.mods.gross.Java9ClassLoaderUtil");
            final Method mdGetSystemClassPathURLs = clJava9ClassLoaderUtil.getDeclaredMethod("getSystemClassPathURLs", (Class<?>[])new Class[0]);
            return (URL[])mdGetSystemClassPathURLs.invoke(null, new Object[0]);
        }
        catch (final ReflectiveOperationException ex) {
            return new URL[0];
        }
    }
    
    @Override
    public Class<?> findClass(final String name) throws ClassNotFoundException {
        return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
    }
    
    @Override
    public Class<?> findClass(final String name, final boolean initialize) throws ClassNotFoundException {
        return Class.forName(name, initialize, Thread.currentThread().getContextClassLoader());
    }
    
    @Override
    public Class<?> findAgentClass(final String name, final boolean initialize) throws ClassNotFoundException {
        return Class.forName(name, initialize, Launcher.class.getClassLoader());
    }
}
