// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple;

import java.util.Collection;
import java.util.List;

public interface OptionSpec<V>
{
    List<V> values(final OptionSet p0);
    
    V value(final OptionSet p0);
    
    Collection<String> options();
    
    boolean isForHelp();
}
