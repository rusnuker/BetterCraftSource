// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import java.awt.Dimension;
import org.cef.callback.CefPrintJobCallback;
import org.cef.callback.CefPrintDialogCallback;
import org.cef.misc.CefPrintSettings;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefNative;

public interface CefPrintHandler extends CefNative
{
    void onPrintStart(final CefBrowser p0);
    
    void onPrintSettings(final CefPrintSettings p0, final boolean p1);
    
    boolean onPrintDialog(final boolean p0, final CefPrintDialogCallback p1);
    
    boolean onPrintJob(final String p0, final String p1, final CefPrintJobCallback p2);
    
    void onPrintReset();
    
    Dimension getPdfPaperSize(final int p0);
}
