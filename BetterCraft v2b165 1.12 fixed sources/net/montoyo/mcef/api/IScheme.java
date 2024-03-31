// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.api;

public interface IScheme
{
    SchemePreResponse processRequest(final String p0);
    
    void getResponseHeaders(final ISchemeResponseHeaders p0);
    
    boolean readResponse(final ISchemeResponseData p0);
}
