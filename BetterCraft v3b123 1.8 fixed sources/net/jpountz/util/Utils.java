// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.util;

import java.nio.ByteOrder;

public enum Utils
{
    public static final ByteOrder NATIVE_BYTE_ORDER;
    private static final boolean unalignedAccessAllowed;
    
    public static boolean isUnalignedAccessAllowed() {
        return Utils.unalignedAccessAllowed;
    }
    
    static {
        NATIVE_BYTE_ORDER = ByteOrder.nativeOrder();
        final String arch = System.getProperty("os.arch");
        unalignedAccessAllowed = (arch.equals("i386") || arch.equals("x86") || arch.equals("amd64") || arch.equals("x86_64") || arch.equals("aarch64") || arch.equals("ppc64le"));
    }
}
