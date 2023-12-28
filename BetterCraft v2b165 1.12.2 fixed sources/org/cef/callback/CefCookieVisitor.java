// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import org.cef.misc.BoolRef;
import org.cef.network.CefCookie;

public interface CefCookieVisitor
{
    boolean visit(final CefCookie p0, final int p1, final int p2, final BoolRef p3);
}
