// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.internal.tcnative;

import java.io.File;

public final class Library
{
    private static final String[] NAMES;
    private static Library _instance;
    
    private Library() throws Exception {
        boolean loaded = false;
        final String path = System.getProperty("java.library.path");
        final String[] paths = path.split(File.pathSeparator);
        final StringBuilder err = new StringBuilder();
        for (int i = 0; i < Library.NAMES.length; ++i) {
            try {
                System.loadLibrary(Library.NAMES[i]);
                loaded = true;
            }
            catch (final ThreadDeath t) {
                throw t;
            }
            catch (final VirtualMachineError t2) {
                throw t2;
            }
            catch (final Throwable t3) {
                final String name = System.mapLibraryName(Library.NAMES[i]);
                for (int j = 0; j < paths.length; ++j) {
                    final File fd = new File(paths[j], name);
                    if (fd.exists()) {
                        throw new RuntimeException(t3);
                    }
                }
                if (i > 0) {
                    err.append(", ");
                }
                err.append(t3.getMessage());
            }
            if (loaded) {
                break;
            }
        }
        if (!loaded) {
            throw new UnsatisfiedLinkError(err.toString());
        }
    }
    
    private Library(final String libraryName) {
        if (!"provided".equals(libraryName)) {
            System.loadLibrary(libraryName);
        }
    }
    
    private static native boolean initialize0();
    
    private static native boolean has(final int p0);
    
    private static native int version(final int p0);
    
    private static native String aprVersionString();
    
    public static boolean initialize() throws Exception {
        return initialize("provided", null);
    }
    
    public static boolean initialize(final String libraryName, final String engine) throws Exception {
        if (Library._instance == null) {
            if (libraryName == null) {
                Library._instance = new Library();
            }
            else {
                Library._instance = new Library(libraryName);
            }
            final int aprMajor = version(17);
            if (aprMajor < 1) {
                throw new UnsatisfiedLinkError("Unsupported APR Version (" + aprVersionString() + ")");
            }
            final boolean aprHasThreads = has(2);
            if (!aprHasThreads) {
                throw new UnsatisfiedLinkError("Missing APR_HAS_THREADS");
            }
        }
        return initialize0() && SSL.initialize(engine) == 0;
    }
    
    static {
        NAMES = new String[] { "netty-tcnative", "libnetty-tcnative", "netty-tcnative-1", "libnetty-tcnative-1" };
        Library._instance = null;
    }
}
