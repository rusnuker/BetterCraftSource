// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

final class NativeLibraryUtil
{
    public static void loadLibrary(final String libName, final boolean absolute) {
        if (absolute) {
            System.load(libName);
        }
        else {
            System.loadLibrary(libName);
        }
    }
    
    private NativeLibraryUtil() {
    }
}
