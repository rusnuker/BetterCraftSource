// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

public final class Constants
{
    public static final boolean IS_WEB_APP;
    public static final boolean ENABLE_THREADLOCALS;
    public static final int JAVA_MAJOR_VERSION;
    
    private static boolean isClassAvailable(final String className) {
        try {
            return LoaderUtil.loadClass(className) != null;
        }
        catch (final Throwable e) {
            return false;
        }
    }
    
    private Constants() {
    }
    
    private static int getMajorVersion() {
        final String version = System.getProperty("java.version");
        final String[] parts = version.split("-|\\.");
        try {
            final int token = Integer.parseInt(parts[0]);
            final boolean isJEP223 = token != 1;
            if (isJEP223) {
                return token;
            }
            return Integer.parseInt(parts[1]);
        }
        catch (final Exception ex) {
            return 0;
        }
    }
    
    static {
        IS_WEB_APP = PropertiesUtil.getProperties().getBooleanProperty("log4j2.is.webapp", isClassAvailable("javax.servlet.Servlet"));
        ENABLE_THREADLOCALS = (!Constants.IS_WEB_APP && PropertiesUtil.getProperties().getBooleanProperty("log4j2.enable.threadlocals", true));
        JAVA_MAJOR_VERSION = getMajorVersion();
    }
}
