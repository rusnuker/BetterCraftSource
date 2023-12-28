// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.setup;

import java.io.File;

public class Deleter
{
    private static boolean tryDelete(final File f) {
        if (!f.exists()) {
            return true;
        }
        if (f.delete()) {
            return true;
        }
        final File dst = new File(f.getParentFile(), String.valueOf(f.getName()) + "_" + System.currentTimeMillis() % (int)(Math.random() * 10000.0) + ".tmp");
        if (f.renameTo(dst)) {
            if (!dst.delete()) {
                dst.deleteOnExit();
            }
            return true;
        }
        return false;
    }
    
    public static void main(final String[] args) {
        final File f = new File(args[0]);
        final String lowerName = f.getName().toLowerCase();
        if (lowerName.startsWith("mcef") && lowerName.endsWith(".jar")) {
            for (int i = 0; i < 30; ++i) {
                if (tryDelete(f)) {
                    return;
                }
                try {
                    Thread.sleep(3000L);
                }
                catch (final Throwable t) {}
            }
        }
    }
}
