// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.account;

import java.io.File;

public class MCBaseFolder
{
    public static File getWorkingDirectory() {
        return getWorkingDirectory("minecraft");
    }
    
    public static File getWorkingDirectory(final String applicationName) {
        final String userHome = System.getProperty("user.home", ".");
        File workingDirectory = null;
        switch (getPlatform()) {
            case LINUX: {
                workingDirectory = new File(userHome, String.valueOf('.') + applicationName + '/');
                break;
            }
            case UNKNOWN: {
                workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
                break;
            }
            case WINDOWS: {
                final String applicationData = System.getenv("APPDATA");
                if (applicationData != null) {
                    workingDirectory = new File(applicationData, "." + applicationName + '/');
                    break;
                }
                workingDirectory = new File(userHome, String.valueOf('.') + applicationName + '/');
                break;
            }
            case MACOS: {
                workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
                break;
            }
            case SOLARIS: {
                final String applicationDataW = System.getenv("APPDATA");
                if (applicationDataW != null) {
                    workingDirectory = new File(applicationDataW, "." + applicationName + '/');
                    break;
                }
                workingDirectory = new File(userHome, String.valueOf('.') + applicationName + '/');
                break;
            }
            default: {
                workingDirectory = new File(userHome, String.valueOf(applicationName) + '/');
                break;
            }
        }
        if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
            throw new RuntimeException("The working directory could not be created: " + workingDirectory);
        }
        return workingDirectory;
    }
    
    public static OS getPlatform() {
        final String osName = System.getProperty("os.name").toLowerCase();
        OS[] values;
        for (int length = (values = OS.values()).length, i = 0; i < length; ++i) {
            final OS os = values[i];
            if (os != OS.UNKNOWN) {
                String[] osNames;
                for (int length2 = (osNames = os.getOsNames()).length, j = 0; j < length2; ++j) {
                    final String name = osNames[j];
                    if (osName.contains(name)) {
                        return os;
                    }
                }
            }
        }
        return OS.UNKNOWN;
    }
    
    public enum OS
    {
        LINUX("LINUX", 0, new String[] { "linux", "unix" }), 
        SOLARIS("SOLARIS", 1, new String[] { "solaris", "sunos" }), 
        WINDOWS("WINDOWS", 2, new String[] { "win" }), 
        MACOS("MACOS", 3, new String[] { "mac" }), 
        UNKNOWN("UNKNOWN", 4, new String[0]);
        
        private String[] osNames;
        
        private OS(final String s, final int n, final String[] osNames) {
            this.osNames = osNames;
        }
        
        public String[] getOsNames() {
            return this.osNames;
        }
    }
}
