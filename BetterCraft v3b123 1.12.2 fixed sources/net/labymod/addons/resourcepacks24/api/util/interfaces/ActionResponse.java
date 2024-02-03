// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.api.util.interfaces;

public interface ActionResponse<T>
{
    void success(final T p0);
    
    void failed(final String p0);
}
