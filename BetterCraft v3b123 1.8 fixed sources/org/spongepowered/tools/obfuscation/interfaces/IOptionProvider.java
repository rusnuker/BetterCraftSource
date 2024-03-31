// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.interfaces;

import java.util.List;

public interface IOptionProvider
{
    String getOption(final String p0);
    
    String getOption(final String p0, final String p1);
    
    boolean getOption(final String p0, final boolean p1);
    
    List<String> getOptions(final String p0);
}
