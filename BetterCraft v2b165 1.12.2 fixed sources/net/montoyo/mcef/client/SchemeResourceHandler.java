// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.client;

import net.montoyo.mcef.api.ISchemeResponseData;
import net.montoyo.mcef.api.ISchemeResponseHeaders;
import org.cef.misc.StringRef;
import org.cef.misc.IntRef;
import org.cef.network.CefResponse;
import net.montoyo.mcef.api.SchemePreResponse;
import org.cef.callback.CefCallback;
import org.cef.network.CefRequest;
import net.montoyo.mcef.api.IScheme;
import org.cef.handler.CefResourceHandlerAdapter;

public class SchemeResourceHandler extends CefResourceHandlerAdapter
{
    private final IScheme scheme;
    
    public SchemeResourceHandler(final IScheme scm) {
        this.scheme = scm;
    }
    
    @Override
    public boolean processRequest(final CefRequest request, final CefCallback callback) {
        final SchemePreResponse resp = this.scheme.processRequest(request.getURL());
        switch (resp) {
            case HANDLED_CONTINUE: {
                callback.Continue();
                return true;
            }
            case HANDLED_CANCEL: {
                callback.cancel();
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void getResponseHeaders(final CefResponse response, final IntRef response_length, final StringRef redirectUrl) {
        this.scheme.getResponseHeaders(new SchemeResponseHeaders(response, response_length, redirectUrl));
    }
    
    @Override
    public boolean readResponse(final byte[] data_out, final int bytes_to_read, final IntRef bytes_read, final CefCallback callback) {
        return this.scheme.readResponse(new SchemeResponseData(data_out, bytes_to_read, bytes_read));
    }
}
