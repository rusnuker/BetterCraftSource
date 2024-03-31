// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import java.util.Vector;

public interface CefFileDialogCallback
{
    void Continue(final int p0, final Vector<String> p1);
    
    void Cancel();
}
