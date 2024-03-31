// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.text2speech;

import java.util.Locale;

public interface Narrator
{
    void say(final String p0);
    
    void clear();
    
    boolean active();
    
    default Narrator getNarrator() {
        final String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (osName.contains("linux")) {
            setJNAPath(":");
            return new NarratorLinux();
        }
        if (osName.contains("win")) {
            setJNAPath(";");
            return new NarratorWindows();
        }
        if (osName.contains("mac")) {
            setJNAPath(":");
            return new NarratorOSX();
        }
        return new NarratorDummy();
    }
    
    default void setJNAPath(final String sep) {
        System.setProperty("jna.library.path", System.getProperty("jna.library.path") + sep + "./src/natives/resources/");
        System.setProperty("jna.library.path", System.getProperty("jna.library.path") + sep + System.getProperty("java.library.path"));
    }
}
