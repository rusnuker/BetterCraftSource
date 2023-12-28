// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefFileDialogCallback;
import java.util.Vector;
import org.cef.browser.CefBrowser;

public interface CefDialogHandler
{
    boolean onFileDialog(final CefBrowser p0, final FileDialogMode p1, final String p2, final String p3, final Vector<String> p4, final int p5, final CefFileDialogCallback p6);
    
    public enum FileDialogMode
    {
        FILE_DIALOG_OPEN("FILE_DIALOG_OPEN", 0), 
        FILE_DIALOG_OPEN_MULTIPLE("FILE_DIALOG_OPEN_MULTIPLE", 1), 
        FILE_DIALOG_SAVE("FILE_DIALOG_SAVE", 2);
        
        private FileDialogMode(final String s, final int n) {
        }
    }
}
