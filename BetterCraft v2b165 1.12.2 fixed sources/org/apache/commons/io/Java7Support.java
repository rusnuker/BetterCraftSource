// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.io;

import java.lang.reflect.Array;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.io.File;
import java.lang.reflect.Method;

class Java7Support
{
    private static final boolean IS_JAVA7;
    private static Method isSymbolicLink;
    private static Method delete;
    private static Method toPath;
    private static Method exists;
    private static Method toFile;
    private static Method readSymlink;
    private static Method createSymlink;
    private static Object emptyLinkOpts;
    private static Object emptyFileAttributes;
    
    public static boolean isSymLink(final File file) {
        try {
            final Object path = Java7Support.toPath.invoke(file, new Object[0]);
            final Boolean result = (Boolean)Java7Support.isSymbolicLink.invoke(null, path);
            return result;
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (final InvocationTargetException e2) {
            throw new RuntimeException(e2);
        }
    }
    
    public static File readSymbolicLink(final File symlink) throws IOException {
        try {
            final Object path = Java7Support.toPath.invoke(symlink, new Object[0]);
            final Object resultPath = Java7Support.readSymlink.invoke(null, path);
            return (File)Java7Support.toFile.invoke(resultPath, new Object[0]);
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (final InvocationTargetException e2) {
            throw new RuntimeException(e2);
        }
    }
    
    private static boolean exists(final File file) throws IOException {
        try {
            final Object path = Java7Support.toPath.invoke(file, new Object[0]);
            final Boolean result = (Boolean)Java7Support.exists.invoke(null, path, Java7Support.emptyLinkOpts);
            return result;
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (final InvocationTargetException e2) {
            throw (RuntimeException)e2.getTargetException();
        }
    }
    
    public static File createSymbolicLink(final File symlink, final File target) throws IOException {
        try {
            if (!exists(symlink)) {
                final Object link = Java7Support.toPath.invoke(symlink, new Object[0]);
                final Object path = Java7Support.createSymlink.invoke(null, link, Java7Support.toPath.invoke(target, new Object[0]), Java7Support.emptyFileAttributes);
                return (File)Java7Support.toFile.invoke(path, new Object[0]);
            }
            return symlink;
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (final InvocationTargetException e2) {
            final Throwable targetException = e2.getTargetException();
            throw (IOException)targetException;
        }
    }
    
    public static void delete(final File file) throws IOException {
        try {
            final Object path = Java7Support.toPath.invoke(file, new Object[0]);
            Java7Support.delete.invoke(null, path);
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (final InvocationTargetException e2) {
            throw (IOException)e2.getTargetException();
        }
    }
    
    public static boolean isAtLeastJava7() {
        return Java7Support.IS_JAVA7;
    }
    
    static {
        boolean isJava7x = true;
        try {
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            final Class<?> files = cl.loadClass("java.nio.file.Files");
            final Class<?> path = cl.loadClass("java.nio.file.Path");
            final Class<?> fa = cl.loadClass("java.nio.file.attribute.FileAttribute");
            final Class<?> linkOption = cl.loadClass("java.nio.file.LinkOption");
            Java7Support.isSymbolicLink = files.getMethod("isSymbolicLink", path);
            Java7Support.delete = files.getMethod("delete", path);
            Java7Support.readSymlink = files.getMethod("readSymbolicLink", path);
            Java7Support.emptyFileAttributes = Array.newInstance(fa, 0);
            Java7Support.createSymlink = files.getMethod("createSymbolicLink", path, path, Java7Support.emptyFileAttributes.getClass());
            Java7Support.emptyLinkOpts = Array.newInstance(linkOption, 0);
            Java7Support.exists = files.getMethod("exists", path, Java7Support.emptyLinkOpts.getClass());
            Java7Support.toPath = File.class.getMethod("toPath", (Class<?>[])new Class[0]);
            Java7Support.toFile = path.getMethod("toFile", (Class<?>[])new Class[0]);
        }
        catch (final ClassNotFoundException e) {
            isJava7x = false;
        }
        catch (final NoSuchMethodException e2) {
            isJava7x = false;
        }
        IS_JAVA7 = isJava7x;
    }
}
