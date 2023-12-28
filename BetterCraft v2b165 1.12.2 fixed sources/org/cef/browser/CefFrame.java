// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.browser;

public interface CefFrame
{
    long getIdentifier();
    
    String getURL();
    
    String getName();
    
    boolean isMain();
    
    boolean isValid();
    
    boolean isFocused();
    
    CefFrame getParent();
    
    void executeJavaScript(final String p0, final String p1, final int p2);
}
