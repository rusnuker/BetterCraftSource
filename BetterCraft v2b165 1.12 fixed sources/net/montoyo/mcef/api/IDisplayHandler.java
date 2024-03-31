// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.api;

public interface IDisplayHandler
{
    void onAddressChange(final IBrowser p0, final String p1);
    
    void onTitleChange(final IBrowser p0, final String p1);
    
    void onTooltip(final IBrowser p0, final String p1);
    
    void onStatusMessage(final IBrowser p0, final String p1);
}
