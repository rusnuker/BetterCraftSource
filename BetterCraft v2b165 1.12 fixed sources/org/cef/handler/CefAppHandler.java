// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefSchemeRegistrar;
import org.cef.CefApp;
import org.cef.callback.CefCommandLine;

public interface CefAppHandler
{
    void onBeforeCommandLineProcessing(final String p0, final CefCommandLine p1);
    
    boolean onBeforeTerminate();
    
    void stateHasChanged(final CefApp.CefAppState p0);
    
    void onRegisterCustomSchemes(final CefSchemeRegistrar p0);
    
    void onContextInitialized();
    
    CefPrintHandler getPrintHandler();
    
    void onScheduleMessagePumpWork(final long p0);
}
