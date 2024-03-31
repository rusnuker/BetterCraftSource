// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public class Sound implements ISoundEventAccessor<Sound>
{
    private final ResourceLocation name;
    private final float volume;
    private final float pitch;
    private final int weight;
    private final Type type;
    private final boolean streaming;
    
    public Sound(final String nameIn, final float volumeIn, final float pitchIn, final int weightIn, final Type typeIn, final boolean p_i46526_6_) {
        this.name = new ResourceLocation(nameIn);
        this.volume = volumeIn;
        this.pitch = pitchIn;
        this.weight = weightIn;
        this.type = typeIn;
        this.streaming = p_i46526_6_;
    }
    
    public ResourceLocation getSoundLocation() {
        return this.name;
    }
    
    public ResourceLocation getSoundAsOggLocation() {
        return new ResourceLocation(this.name.getResourceDomain(), "sounds/" + this.name.getResourcePath() + ".ogg");
    }
    
    public float getVolume() {
        return this.volume;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    @Override
    public int getWeight() {
        return this.weight;
    }
    
    @Override
    public Sound cloneEntry() {
        return this;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public boolean isStreaming() {
        return this.streaming;
    }
    
    public enum Type
    {
        FILE("FILE", 0, "file"), 
        SOUND_EVENT("SOUND_EVENT", 1, "event");
        
        private final String name;
        
        private Type(final String s, final int n, final String nameIn) {
            this.name = nameIn;
        }
        
        public static Type getByName(final String nameIn) {
            Type[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Type sound$type = values[i];
                if (sound$type.name.equals(nameIn)) {
                    return sound$type;
                }
            }
            return null;
        }
    }
}
