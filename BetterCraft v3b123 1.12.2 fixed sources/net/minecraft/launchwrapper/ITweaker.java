// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.launchwrapper;

import java.io.File;
import java.util.List;

public interface ITweaker
{
    void acceptOptions(final List<String> p0, final File p1, final File p2, final String p3);
    
    void injectIntoClassLoader(final LaunchClassLoader p0);
    
    String getLaunchTarget();
    
    String[] getLaunchArguments();
}
