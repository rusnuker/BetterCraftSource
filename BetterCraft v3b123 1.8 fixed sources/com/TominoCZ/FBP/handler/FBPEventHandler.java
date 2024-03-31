// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.handler;

import com.TominoCZ.FBP.FBP;
import net.minecraft.client.particle.IParticleFactory;
import com.TominoCZ.FBP.particle.FBPParticleManager;
import com.TominoCZ.FBP.particle.FBPParticleDigging;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

public class FBPEventHandler
{
    Minecraft mc;
    
    public FBPEventHandler() {
        this.mc = Minecraft.getMinecraft();
    }
    
    public void onWorldLoadEvent() {
    }
    
    public void onEntityJoinWorldEvent(final Entity entity, final World world) {
        if (entity == this.mc.thePlayer) {
            FBP.fancyEffectRenderer = new FBPParticleManager(world, this.mc.getRenderManager().renderEngine, new FBPParticleDigging.Factory());
            if (FBP.originalEffectRenderer == null || (FBP.originalEffectRenderer != this.mc.effectRenderer && FBP.originalEffectRenderer != FBP.fancyEffectRenderer)) {
                FBP.originalEffectRenderer = this.mc.effectRenderer;
            }
            this.mc.effectRenderer = FBP.fancyEffectRenderer;
        }
    }
}
