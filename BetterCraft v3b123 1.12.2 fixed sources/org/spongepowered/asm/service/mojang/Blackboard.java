// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.mojang;

import org.spongepowered.asm.service.IPropertyKey;
import net.minecraft.launchwrapper.Launch;
import org.spongepowered.asm.service.IGlobalPropertyService;

public class Blackboard implements IGlobalPropertyService
{
    public Blackboard() {
        Launch.classLoader.hashCode();
    }
    
    @Override
    public IPropertyKey resolveKey(final String name) {
        return new Key(name);
    }
    
    @Override
    public final <T> T getProperty(final IPropertyKey key) {
        return (T)Launch.blackboard.get(key.toString());
    }
    
    @Override
    public final void setProperty(final IPropertyKey key, final Object value) {
        Launch.blackboard.put(key.toString(), value);
    }
    
    @Override
    public final <T> T getProperty(final IPropertyKey key, final T defaultValue) {
        final Object value = Launch.blackboard.get(key.toString());
        return (T)((value != null) ? value : defaultValue);
    }
    
    @Override
    public final String getPropertyString(final IPropertyKey key, final String defaultValue) {
        final Object value = Launch.blackboard.get(key.toString());
        return (value != null) ? value.toString() : defaultValue;
    }
    
    class Key implements IPropertyKey
    {
        private final String key;
        
        Key(final String key) {
            this.key = key;
        }
        
        @Override
        public String toString() {
            return this.key;
        }
    }
}
