// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core;

public class LabyModCore
{
    private static CoreAdapter coreAdapter;
    
    public static CoreAdapter getCoreAdapter() {
        return LabyModCore.coreAdapter;
    }
    
    public static void setCoreAdapter(final CoreAdapter coreAdapter) {
        LabyModCore.coreAdapter = coreAdapter;
    }
    
    public static WorldRendererAdapter getWorldRenderer() {
        return LabyModCore.coreAdapter.getWorldRendererImplementation();
    }
    
    public static RenderAdapter getRenderImplementation() {
        return LabyModCore.coreAdapter.getRenderImplementation();
    }
    
    public static RenderPlayerAdapter getRenderPlayerImplementation() {
        return LabyModCore.coreAdapter.getRenderPlayerImplementation();
    }
    
    public static MinecraftAdapter getMinecraft() {
        return LabyModCore.coreAdapter.getMinecraftImplementation();
    }
    
    public static MathAdapter getMath() {
        return LabyModCore.coreAdapter.getMathImplementation();
    }
    
    public static ForgeAdapter getForge() {
        return LabyModCore.coreAdapter.getForgeImplementation();
    }
    
    public static SoundAdapter getSound() {
        return LabyModCore.coreAdapter.getSoundImplementation();
    }
    
    public static MappingAdapter getMappingAdapter() {
        return LabyModCore.coreAdapter.getMappingAdapter();
    }
    
    public static ServerPingerAdapter getServerPinger() {
        return LabyModCore.coreAdapter.getServerPingerImplementation();
    }
}
