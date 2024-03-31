// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import java.awt.Dimension;
import org.cef.callback.CefPrintJobCallback;
import org.cef.callback.CefPrintDialogCallback;
import org.cef.misc.CefPrintSettings;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefNativeAdapter;

public abstract class CefPrintHandlerAdapter extends CefNativeAdapter implements CefPrintHandler
{
    @Override
    public void onPrintStart(final CefBrowser browser) {
    }
    
    @Override
    public void onPrintSettings(final CefPrintSettings settings, final boolean get_defaults) {
    }
    
    @Override
    public boolean onPrintDialog(final boolean has_selection, final CefPrintDialogCallback callback) {
        return false;
    }
    
    @Override
    public boolean onPrintJob(final String document_name, final String pdf_file_path, final CefPrintJobCallback callback) {
        return false;
    }
    
    @Override
    public void onPrintReset() {
    }
    
    @Override
    public Dimension getPdfPaperSize(final int deviceUnitsPerInch) {
        final int adjustedWidth = (int)(deviceUnitsPerInch / 300.0 * 2480.0);
        final int adjustedHeight = (int)(deviceUnitsPerInch / 300.0 * 3508.0);
        return new Dimension(adjustedWidth, adjustedHeight);
    }
}
