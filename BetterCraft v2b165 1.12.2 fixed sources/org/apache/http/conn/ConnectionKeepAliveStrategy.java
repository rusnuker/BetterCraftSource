// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.http.conn;

import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;

public interface ConnectionKeepAliveStrategy
{
    long getKeepAliveDuration(final HttpResponse p0, final HttpContext p1);
}
