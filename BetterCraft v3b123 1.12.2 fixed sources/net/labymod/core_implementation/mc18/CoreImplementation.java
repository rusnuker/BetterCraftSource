// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18;

import net.labymod.core.ProtocolAdapter;
import net.labymod.core.ServerPingerAdapter;
import net.labymod.core.MappingAdapter;
import net.labymod.core.WorldRendererAdapter;
import net.labymod.core.SoundAdapter;
import net.labymod.core.RenderPlayerAdapter;
import net.labymod.core.RenderAdapter;
import net.labymod.core.MinecraftAdapter;
import net.labymod.core.MathAdapter;
import net.labymod.core.ForgeAdapter;
import net.labymod.core.CoreAdapter;

public class CoreImplementation implements CoreAdapter
{
    private ForgeAdapter forgeImpl;
    private MathAdapter mathImpl;
    private MinecraftAdapter minecraftImpl;
    private RenderAdapter renderImpl;
    private RenderPlayerAdapter renderPlayerImpl;
    private SoundAdapter soundImpl;
    private WorldRendererAdapter worldRendererImpl;
    private MappingAdapter mappingImpl;
    private ServerPingerAdapter serverPingerImpl;
    private ProtocolImplementation chunkImpl;
    
    public CoreImplementation() {
        this.forgeImpl = new ForgeImplementation();
        this.mathImpl = new MathImplementation();
        this.minecraftImpl = new MinecraftImplementation();
        this.renderImpl = new RenderImplementation();
        this.renderPlayerImpl = new RenderPlayerImplementation();
        this.soundImpl = new SoundImplementation();
        this.worldRendererImpl = new WorldRendererImplementation();
        this.mappingImpl = new MappingImplementation();
        this.serverPingerImpl = new ServerPingerImplementation();
        this.chunkImpl = new ProtocolImplementation();
    }
    
    @Override
    public ForgeAdapter getForgeImplementation() {
        return this.forgeImpl;
    }
    
    @Override
    public MathAdapter getMathImplementation() {
        return this.mathImpl;
    }
    
    @Override
    public MinecraftAdapter getMinecraftImplementation() {
        return this.minecraftImpl;
    }
    
    @Override
    public RenderAdapter getRenderImplementation() {
        return this.renderImpl;
    }
    
    @Override
    public RenderPlayerAdapter getRenderPlayerImplementation() {
        return this.renderPlayerImpl;
    }
    
    @Override
    public SoundAdapter getSoundImplementation() {
        return this.soundImpl;
    }
    
    @Override
    public WorldRendererAdapter getWorldRendererImplementation() {
        return this.worldRendererImpl;
    }
    
    @Override
    public MappingAdapter getMappingAdapter() {
        return this.mappingImpl;
    }
    
    @Override
    public ServerPingerAdapter getServerPingerImplementation() {
        return this.serverPingerImpl;
    }
    
    @Override
    public ProtocolAdapter getProtocolAdapter() {
        return this.chunkImpl;
    }
}
