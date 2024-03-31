// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j;

import java.io.Serializable;

public interface Marker extends Serializable
{
    String getName();
    
    Marker getParent();
    
    boolean isInstanceOf(final Marker p0);
    
    boolean isInstanceOf(final String p0);
}
