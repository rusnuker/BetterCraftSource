// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.world.World;

public interface IParticleFactory
{
    @Nullable
    Particle createParticle(final int p0, final World p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final int... p8);
}
