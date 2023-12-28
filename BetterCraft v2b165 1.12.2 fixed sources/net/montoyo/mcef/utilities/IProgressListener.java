// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.utilities;

public interface IProgressListener
{
    void onProgressed(final double p0);
    
    void onTaskChanged(final String p0);
    
    void onProgressEnd();
}
