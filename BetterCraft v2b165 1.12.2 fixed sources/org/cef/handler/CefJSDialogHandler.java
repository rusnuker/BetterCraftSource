// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.misc.BoolRef;
import org.cef.callback.CefJSDialogCallback;
import org.cef.browser.CefBrowser;

public interface CefJSDialogHandler
{
    boolean onJSDialog(final CefBrowser p0, final String p1, final JSDialogType p2, final String p3, final String p4, final CefJSDialogCallback p5, final BoolRef p6);
    
    boolean onBeforeUnloadDialog(final CefBrowser p0, final String p1, final boolean p2, final CefJSDialogCallback p3);
    
    void onResetDialogState(final CefBrowser p0);
    
    void onDialogClosed(final CefBrowser p0);
    
    public enum JSDialogType
    {
        JSDIALOGTYPE_ALERT("JSDIALOGTYPE_ALERT", 0), 
        JSDIALOGTYPE_CONFIRM("JSDIALOGTYPE_CONFIRM", 1), 
        JSDIALOGTYPE_PROMPT("JSDIALOGTYPE_PROMPT", 2);
        
        private JSDialogType(final String s, final int n) {
        }
    }
}
