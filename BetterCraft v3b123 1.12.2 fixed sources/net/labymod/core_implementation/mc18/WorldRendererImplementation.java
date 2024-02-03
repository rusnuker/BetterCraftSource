// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.labymod.core.WorldRendererAdapter;

public class WorldRendererImplementation implements WorldRendererAdapter
{
    private static WorldRendererImplementation instance;
    private final WorldRenderer worldRenderer;
    
    static {
        WorldRendererImplementation.instance = new WorldRendererImplementation();
    }
    
    public WorldRendererImplementation() {
        this.worldRenderer = Tessellator.getInstance().getWorldRenderer();
    }
    
    public static WorldRendererImplementation getInstance() {
        return WorldRendererImplementation.instance;
    }
    
    @Override
    public void begin(final int glMode, final VertexFormat vertexFormat) {
        this.worldRenderer.begin(glMode, vertexFormat);
    }
    
    @Override
    public WorldRendererAdapter pos(final double x, final double y, final double z) {
        this.worldRenderer.pos(x, y, z);
        return this;
    }
    
    @Override
    public WorldRendererAdapter tex(final double u, final double v) {
        this.worldRenderer.tex(u, v);
        return this;
    }
    
    @Override
    public WorldRendererAdapter color(final float red, final float green, final float blue, final float alpha) {
        this.worldRenderer.color(red, green, blue, alpha);
        return this;
    }
    
    @Override
    public WorldRendererAdapter color(final int red, final int green, final int blue, final int alpha) {
        this.worldRenderer.color(red, green, blue, alpha);
        return this;
    }
    
    @Override
    public void endVertex() {
        this.worldRenderer.endVertex();
    }
}
