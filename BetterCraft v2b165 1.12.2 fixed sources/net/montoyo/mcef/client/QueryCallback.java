// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.client;

import org.cef.callback.CefQueryCallback;
import net.montoyo.mcef.api.IJSQueryCallback;

public class QueryCallback implements IJSQueryCallback
{
    private CefQueryCallback cb;
    
    public QueryCallback(final CefQueryCallback cb) {
        this.cb = cb;
    }
    
    @Override
    public void success(final String response) {
        this.cb.success(response);
    }
    
    @Override
    public void failure(final int errId, final String errMsg) {
        this.cb.failure(errId, errMsg);
    }
}
