// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

public interface IMixinAuditTrail
{
    void onApply(final String p0, final String p1);
    
    void onPostProcess(final String p0);
    
    void onGenerate(final String p0, final String p1);
}
