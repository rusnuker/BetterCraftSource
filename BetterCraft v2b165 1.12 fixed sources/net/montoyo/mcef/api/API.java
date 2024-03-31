// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.api;

public interface API
{
    IBrowser createBrowser(final String p0, final boolean p1);
    
    IBrowser createBrowser(final String p0);
    
    void registerDisplayHandler(final IDisplayHandler p0);
    
    void registerJSQueryHandler(final IJSQueryHandler p0);
    
    boolean isVirtual();
    
    void openExampleBrowser(final String p0);
    
    String mimeTypeFromExtension(final String p0);
    
    void registerScheme(final String p0, final Class<? extends IScheme> p1, final boolean p2, final boolean p3, final boolean p4, final boolean p5, final boolean p6, final boolean p7, final boolean p8);
    
    boolean isSchemeRegistered(final String p0);
    
    String punycode(final String p0);
}
