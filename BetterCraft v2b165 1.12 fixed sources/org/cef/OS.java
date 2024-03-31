// 
// Decompiled by Procyon v0.6.0
// 

package org.cef;

public class OS
{
    private static OSType osType;
    
    static {
        OS.osType = OSType.OSUndefined;
    }
    
    public static final boolean isWindows() {
        return getOSType() == OSType.OSWindows;
    }
    
    public static final boolean isMacintosh() {
        return getOSType() == OSType.OSMacintosh;
    }
    
    public static final boolean isLinux() {
        return getOSType() == OSType.OSLinux;
    }
    
    private static final OSType getOSType() {
        if (OS.osType == OSType.OSUndefined) {
            final String os = System.getProperty("os.name").toLowerCase();
            if (os.startsWith("windows")) {
                OS.osType = OSType.OSWindows;
            }
            else if (os.startsWith("linux")) {
                OS.osType = OSType.OSLinux;
            }
            else if (os.startsWith("mac")) {
                OS.osType = OSType.OSMacintosh;
            }
            else {
                OS.osType = OSType.OSUnknown;
            }
        }
        return OS.osType;
    }
    
    private enum OSType
    {
        OSUndefined("OSUndefined", 0), 
        OSLinux("OSLinux", 1), 
        OSWindows("OSWindows", 2), 
        OSMacintosh("OSMacintosh", 3), 
        OSUnknown("OSUnknown", 4);
        
        private OSType(final String s, final int n) {
        }
    }
}
