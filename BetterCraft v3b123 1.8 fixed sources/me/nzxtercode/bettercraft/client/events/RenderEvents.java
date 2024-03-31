// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.events;

import net.minecraft.client.gui.ScaledResolution;
import me.nzxtercode.bettercraft.client.events.types.TypePrePost;
import net.lenni0451.eventapi.events.IEvent;

public class RenderEvents
{
    public static class ToolTip implements IEvent
    {
        private final TypePrePost type;
        private final ScaledResolution scaledResolution;
        private final float partialTicks;
        
        public ToolTip(final TypePrePost type, final ScaledResolution scaledResolution, final float partialTicks) {
            this.type = type;
            this.scaledResolution = scaledResolution;
            this.partialTicks = partialTicks;
        }
        
        public float getPartialTicks() {
            return this.partialTicks;
        }
        
        public TypePrePost getType() {
            return this.type;
        }
        
        public ScaledResolution getScaledResolution() {
            return this.scaledResolution;
        }
    }
    
    public static class GameOverlay implements IEvent
    {
        private final TypePrePost type;
        private final ScaledResolution scaledResolution;
        private final float partialTicks;
        
        public GameOverlay(final TypePrePost type, final ScaledResolution scaledResolution, final float partialTicks) {
            this.type = type;
            this.scaledResolution = scaledResolution;
            this.partialTicks = partialTicks;
        }
        
        public float getPartialTicks() {
            return this.partialTicks;
        }
        
        public TypePrePost getType() {
            return this.type;
        }
        
        public ScaledResolution getScaledResolution() {
            return this.scaledResolution;
        }
    }
}
