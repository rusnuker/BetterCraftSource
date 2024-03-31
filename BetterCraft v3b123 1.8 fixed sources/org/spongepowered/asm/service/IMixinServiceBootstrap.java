// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

public interface IMixinServiceBootstrap
{
    String getName();
    
    String getServiceClassName();
    
    void bootstrap();
}
