// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core;

import net.minecraft.client.renderer.vertex.VertexFormat;

public interface WorldRendererAdapter
{
    void begin(final int p0, final VertexFormat p1);
    
    WorldRendererAdapter pos(final double p0, final double p1, final double p2);
    
    WorldRendererAdapter tex(final double p0, final double p1);
    
    WorldRendererAdapter color(final float p0, final float p1, final float p2, final float p3);
    
    WorldRendererAdapter color(final int p0, final int p1, final int p2, final int p3);
    
    void endVertex();
}
