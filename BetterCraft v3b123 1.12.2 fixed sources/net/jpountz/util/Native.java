// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.util;

import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.File;

public enum Native
{
    private static boolean loaded;
    
    private static String arch() {
        return System.getProperty("os.arch");
    }
    
    private static OS os() {
        final String osName = System.getProperty("os.name");
        if (osName.contains("Linux")) {
            return OS.LINUX;
        }
        if (osName.contains("Mac")) {
            return OS.MAC;
        }
        if (osName.contains("Windows")) {
            return OS.WINDOWS;
        }
        if (osName.contains("Solaris") || osName.contains("SunOS")) {
            return OS.SOLARIS;
        }
        throw new UnsupportedOperationException("Unsupported operating system: " + osName);
    }
    
    private static String resourceName() {
        final OS os = os();
        final String packagePrefix = Native.class.getPackage().getName().replace('.', '/');
        return "/" + packagePrefix + "/" + os.name + "/" + arch() + "/liblz4-java." + os.libExtension;
    }
    
    public static synchronized boolean isLoaded() {
        return Native.loaded;
    }
    
    private static void cleanupOldTempLibs() {
        final String tempFolder = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
        final File dir = new File(tempFolder);
        final File[] tempLibFiles = dir.listFiles(new FilenameFilter() {
            private final String searchPattern = "liblz4-java-";
            
            @Override
            public boolean accept(final File dir, final String name) {
                return name.startsWith("liblz4-java-") && !name.endsWith(".lck");
            }
        });
        if (tempLibFiles != null) {
            for (final File tempLibFile : tempLibFiles) {
                final File lckFile = new File(tempLibFile.getAbsolutePath() + ".lck");
                if (!lckFile.exists()) {
                    try {
                        tempLibFile.delete();
                    }
                    catch (final SecurityException e) {
                        System.err.println("Failed to delete old temp lib" + e.getMessage());
                    }
                }
            }
        }
    }
    
    public static synchronized void load() {
        if (Native.loaded) {
            return;
        }
        cleanupOldTempLibs();
        try {
            System.loadLibrary("lz4-java");
            Native.loaded = true;
        }
        catch (final UnsatisfiedLinkError unsatisfiedLinkError) {
            final String resourceName = resourceName();
            final InputStream is = Native.class.getResourceAsStream(resourceName);
            if (is == null) {
                throw new UnsupportedOperationException("Unsupported OS/arch, cannot find " + resourceName + ". Please try building from source.");
            }
            File tempLib = null;
            File tempLibLock = null;
            try {
                tempLibLock = File.createTempFile("liblz4-java-", "." + os().libExtension + ".lck");
                tempLib = new File(tempLibLock.getAbsolutePath().replaceFirst(".lck$", ""));
                try (final FileOutputStream out = new FileOutputStream(tempLib)) {
                    final byte[] buf = new byte[4096];
                    while (true) {
                        final int read = is.read(buf);
                        if (read == -1) {
                            break;
                        }
                        out.write(buf, 0, read);
                    }
                }
                System.load(tempLib.getAbsolutePath());
                Native.loaded = true;
            }
            catch (final IOException e) {
                throw new ExceptionInInitializerError("Cannot unpack liblz4-java: " + e);
            }
            finally {
                if (!Native.loaded) {
                    if (tempLib != null && tempLib.exists() && !tempLib.delete()) {
                        throw new ExceptionInInitializerError("Cannot unpack liblz4-java / cannot delete a temporary native library " + tempLib);
                    }
                    if (tempLibLock != null && tempLibLock.exists() && !tempLibLock.delete()) {
                        throw new ExceptionInInitializerError("Cannot unpack liblz4-java / cannot delete a temporary lock file " + tempLibLock);
                    }
                }
                else {
                    tempLib.deleteOnExit();
                    tempLibLock.deleteOnExit();
                }
            }
        }
    }
    
    static {
        Native.loaded = false;
    }
    
    private enum OS
    {
        WINDOWS("win32", "so"), 
        LINUX("linux", "so"), 
        MAC("darwin", "dylib"), 
        SOLARIS("solaris", "so");
        
        public final String name;
        public final String libExtension;
        
        private OS(final String name, final String libExtension) {
            this.name = name;
            this.libExtension = libExtension;
        }
    }
}
