// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.setup;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class SetupUtil
{
    static boolean tryDelete(final File f) {
        if (!f.exists()) {
            return true;
        }
        if (f.delete()) {
            return true;
        }
        final File dst = new File(f.getParentFile(), String.valueOf(f.getName()) + "_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000.0) + ".tmp");
        if (f.renameTo(dst)) {
            if (!dst.delete()) {
                dst.deleteOnExit();
            }
            return true;
        }
        return false;
    }
    
    static File getSelfJarLocation() {
        try {
            final File ret = new File(SetupUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            if (ret.exists() && ret.isFile()) {
                return ret;
            }
        }
        catch (final Throwable t) {
            System.err.println("Could not locate own JAR (try #1):");
            t.printStackTrace();
        }
        try {
            final File ret = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
            if (ret.exists() && ret.isFile()) {
                return ret;
            }
        }
        catch (final Throwable t) {
            System.err.println("Could not locate own JAR (try #2):");
            t.printStackTrace();
        }
        return null;
    }
    
    static void silentClose(final Object o) {
        try {
            o.getClass().getMethod("close", (Class<?>[])new Class[0]).invoke(o, new Object[0]);
        }
        catch (final Throwable t) {}
    }
    
    static boolean copyFile(final File src, final File dst) {
        final byte[] buf = new byte[65536];
        try {
            final FileInputStream fis = new FileInputStream(src);
            final FileOutputStream fos = new FileOutputStream(dst);
            int read;
            while ((read = fis.read(buf)) > 0) {
                fos.write(buf, 0, read);
            }
            silentClose(fos);
            silentClose(fis);
            return true;
        }
        catch (final Throwable t) {
            System.err.println("Could NOT copy \"" + src.getAbsolutePath() + "\" to \"" + dst.getAbsolutePath() + "\":");
            t.printStackTrace();
            return false;
        }
    }
    
    static boolean areFileEqual(final File a, final File b) {
        if (a == null || b == null) {
            return false;
        }
        try {
            final String ap = a.getCanonicalPath();
            final String bp = b.getCanonicalPath();
            return System.getProperty("os.name").toLowerCase().contains("win") ? ap.equalsIgnoreCase(bp) : ap.equals(bp);
        }
        catch (final IOException e) {
            System.err.println("Could not compare file path, returning non-equal:");
            e.printStackTrace();
            return false;
        }
    }
}
