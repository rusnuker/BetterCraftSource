// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import java.util.Vector;
import java.util.Map;

public interface CefCommandLine
{
    void reset();
    
    String getProgram();
    
    void setProgram(final String p0);
    
    boolean hasSwitches();
    
    boolean hasSwitch(final String p0);
    
    String getSwitchValue(final String p0);
    
    Map<String, String> getSwitches();
    
    void appendSwitch(final String p0);
    
    void appendSwitchWithValue(final String p0, final String p1);
    
    boolean hasArguments();
    
    Vector<String> getArguments();
    
    void appendArgument(final String p0);
}
