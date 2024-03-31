// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import java.util.Locale;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.security.AccessController;
import java.lang.reflect.Method;
import java.security.PrivilegedAction;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.URL;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.io.File;
import io.netty.util.internal.logging.InternalLogger;

public final class NativeLibraryLoader
{
    private static final InternalLogger logger;
    private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
    private static final String OSNAME;
    private static final File WORKDIR;
    private static final boolean DELETE_NATIVE_LIB_AFTER_LOADING;
    
    private static File tmpdir() {
        try {
            File f = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
            if (f != null) {
                NativeLibraryLoader.logger.debug("-Dio.netty.tmpdir: " + f);
                return f;
            }
            f = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
            if (f != null) {
                NativeLibraryLoader.logger.debug("-Dio.netty.tmpdir: " + f + " (java.io.tmpdir)");
                return f;
            }
            if (isWindows()) {
                f = toDirectory(System.getenv("TEMP"));
                if (f != null) {
                    NativeLibraryLoader.logger.debug("-Dio.netty.tmpdir: " + f + " (%TEMP%)");
                    return f;
                }
                final String userprofile = System.getenv("USERPROFILE");
                if (userprofile != null) {
                    f = toDirectory(userprofile + "\\AppData\\Local\\Temp");
                    if (f != null) {
                        NativeLibraryLoader.logger.debug("-Dio.netty.tmpdir: " + f + " (%USERPROFILE%\\AppData\\Local\\Temp)");
                        return f;
                    }
                    f = toDirectory(userprofile + "\\Local Settings\\Temp");
                    if (f != null) {
                        NativeLibraryLoader.logger.debug("-Dio.netty.tmpdir: " + f + " (%USERPROFILE%\\Local Settings\\Temp)");
                        return f;
                    }
                }
            }
            else {
                f = toDirectory(System.getenv("TMPDIR"));
                if (f != null) {
                    NativeLibraryLoader.logger.debug("-Dio.netty.tmpdir: " + f + " ($TMPDIR)");
                    return f;
                }
            }
        }
        catch (final Exception ex) {}
        File f;
        if (isWindows()) {
            f = new File("C:\\Windows\\Temp");
        }
        else {
            f = new File("/tmp");
        }
        NativeLibraryLoader.logger.warn("Failed to get the temporary directory; falling back to: " + f);
        return f;
    }
    
    private static File toDirectory(final String path) {
        if (path == null) {
            return null;
        }
        final File f = new File(path);
        f.mkdirs();
        if (!f.isDirectory()) {
            return null;
        }
        try {
            return f.getAbsoluteFile();
        }
        catch (final Exception ignored) {
            return f;
        }
    }
    
    private static boolean isWindows() {
        return NativeLibraryLoader.OSNAME.startsWith("windows");
    }
    
    private static boolean isOSX() {
        return NativeLibraryLoader.OSNAME.startsWith("macosx") || NativeLibraryLoader.OSNAME.startsWith("osx");
    }
    
    public static void loadFirstAvailable(final ClassLoader loader, final String... names) {
        final int length = names.length;
        int i = 0;
        while (i < length) {
            final String name = names[i];
            try {
                load(name, loader);
                NativeLibraryLoader.logger.debug("Successfully loaded the library: {}", name);
                return;
            }
            catch (final Throwable t) {
                NativeLibraryLoader.logger.debug("Unable to load the library '{}', trying next name...", name, t);
                ++i;
                continue;
            }
            break;
        }
        throw new IllegalArgumentException("Failed to load any of the given libraries: " + Arrays.toString(names));
    }
    
    public static void load(final String name, final ClassLoader loader) {
        final String libname = System.mapLibraryName(name);
        final String path = "META-INF/native/" + libname;
        URL url = loader.getResource(path);
        if (url == null && isOSX()) {
            if (path.endsWith(".jnilib")) {
                url = loader.getResource("META-INF/native/lib" + name + ".dynlib");
            }
            else {
                url = loader.getResource("META-INF/native/lib" + name + ".jnilib");
            }
        }
        if (url == null) {
            loadLibrary(loader, name, false);
            return;
        }
        final int index = libname.lastIndexOf(46);
        final String prefix = libname.substring(0, index);
        final String suffix = libname.substring(index, libname.length());
        InputStream in = null;
        OutputStream out = null;
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile(prefix, suffix, NativeLibraryLoader.WORKDIR);
            in = url.openStream();
            out = new FileOutputStream(tmpFile);
            final byte[] buffer = new byte[8192];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            closeQuietly(out);
            out = null;
            loadLibrary(loader, tmpFile.getPath(), true);
        }
        catch (final Exception e) {
            throw (UnsatisfiedLinkError)new UnsatisfiedLinkError("could not load a native library: " + name).initCause(e);
        }
        finally {
            closeQuietly(in);
            closeQuietly(out);
            if (tmpFile != null && (!NativeLibraryLoader.DELETE_NATIVE_LIB_AFTER_LOADING || !tmpFile.delete())) {
                tmpFile.deleteOnExit();
            }
        }
    }
    
    private static void loadLibrary(final ClassLoader loader, final String name, final boolean absolute) {
        try {
            final Class<?> newHelper = tryToLoadClass(loader, NativeLibraryUtil.class);
            loadLibraryByHelper(newHelper, name, absolute);
            return;
        }
        catch (final UnsatisfiedLinkError e) {
            NativeLibraryLoader.logger.debug("Unable to load the library '{}', trying other loading mechanism.", name, e);
        }
        catch (final Exception e2) {
            NativeLibraryLoader.logger.debug("Unable to load the library '{}', trying other loading mechanism.", name, e2);
        }
        NativeLibraryUtil.loadLibrary(name, absolute);
    }
    
    private static void loadLibraryByHelper(final Class<?> helper, final String name, final boolean absolute) throws UnsatisfiedLinkError {
        final Object ret = AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    final Method method = helper.getMethod("loadLibrary", String.class, Boolean.TYPE);
                    method.setAccessible(true);
                    return method.invoke(null, name, absolute);
                }
                catch (final Exception e) {
                    return e;
                }
            }
        });
        if (!(ret instanceof Throwable)) {
            return;
        }
        final Throwable error = (Throwable)ret;
        final Throwable cause = error.getCause();
        if (cause == null) {
            throw new UnsatisfiedLinkError(error.getMessage());
        }
        if (cause instanceof UnsatisfiedLinkError) {
            throw (UnsatisfiedLinkError)cause;
        }
        throw new UnsatisfiedLinkError(cause.getMessage());
    }
    
    private static Class<?> tryToLoadClass(final ClassLoader loader, final Class<?> helper) throws ClassNotFoundException {
        try {
            return loader.loadClass(helper.getName());
        }
        catch (final ClassNotFoundException e) {
            final byte[] classBinary = classToByteArray(helper);
            return AccessController.doPrivileged((PrivilegedAction<Class<?>>)new PrivilegedAction<Class<?>>() {
                @Override
                public Class<?> run() {
                    try {
                        final Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
                        defineClass.setAccessible(true);
                        return (Class)defineClass.invoke(loader, helper.getName(), classBinary, 0, classBinary.length);
                    }
                    catch (final Exception e) {
                        throw new IllegalStateException("Define class failed!", e);
                    }
                }
            });
        }
    }
    
    private static byte[] classToByteArray(final Class<?> clazz) throws ClassNotFoundException {
        String fileName = clazz.getName();
        final int lastDot = fileName.lastIndexOf(46);
        if (lastDot > 0) {
            fileName = fileName.substring(lastDot + 1);
        }
        final URL classUrl = clazz.getResource(fileName + ".class");
        if (classUrl == null) {
            throw new ClassNotFoundException(clazz.getName());
        }
        final byte[] buf = new byte[1024];
        final ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
        InputStream in = null;
        try {
            in = classUrl.openStream();
            int r;
            while ((r = in.read(buf)) != -1) {
                out.write(buf, 0, r);
            }
            return out.toByteArray();
        }
        catch (final IOException ex) {
            throw new ClassNotFoundException(clazz.getName(), ex);
        }
        finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }
    
    private static void closeQuietly(final Closeable c) {
        if (c != null) {
            try {
                c.close();
            }
            catch (final IOException ex) {}
        }
    }
    
    private NativeLibraryLoader() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(NativeLibraryLoader.class);
        OSNAME = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
        final String workdir = SystemPropertyUtil.get("io.netty.native.workdir");
        if (workdir != null) {
            File f = new File(workdir);
            f.mkdirs();
            try {
                f = f.getAbsoluteFile();
            }
            catch (final Exception ex) {}
            WORKDIR = f;
            NativeLibraryLoader.logger.debug("-Dio.netty.native.workdir: " + NativeLibraryLoader.WORKDIR);
        }
        else {
            WORKDIR = tmpdir();
            NativeLibraryLoader.logger.debug("-Dio.netty.native.workdir: " + NativeLibraryLoader.WORKDIR + " (io.netty.tmpdir)");
        }
        DELETE_NATIVE_LIB_AFTER_LOADING = SystemPropertyUtil.getBoolean("io.netty.native.deleteLibAfterLoading", true);
    }
}
