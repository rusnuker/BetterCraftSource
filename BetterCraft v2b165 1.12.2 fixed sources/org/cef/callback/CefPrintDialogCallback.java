// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import org.cef.misc.CefPrintSettings;

public interface CefPrintDialogCallback
{
    void Continue(final CefPrintSettings p0);
    
    void cancel();
}
