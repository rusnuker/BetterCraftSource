// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.http.params;

import org.apache.http.util.Args;

@Deprecated
public abstract class HttpAbstractParamBean
{
    protected final HttpParams params;
    
    public HttpAbstractParamBean(final HttpParams params) {
        this.params = Args.notNull(params, "HTTP parameters");
    }
}
