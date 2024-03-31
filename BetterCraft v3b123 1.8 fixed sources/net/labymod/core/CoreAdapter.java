// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core;

public interface CoreAdapter
{
    ForgeAdapter getForgeImplementation();
    
    MathAdapter getMathImplementation();
    
    MinecraftAdapter getMinecraftImplementation();
    
    RenderAdapter getRenderImplementation();
    
    RenderPlayerAdapter getRenderPlayerImplementation();
    
    SoundAdapter getSoundImplementation();
    
    WorldRendererAdapter getWorldRendererImplementation();
    
    MappingAdapter getMappingAdapter();
    
    ServerPingerAdapter getServerPingerImplementation();
    
    ProtocolAdapter getProtocolAdapter();
}
