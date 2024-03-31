// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.modlauncher;

import cpw.mods.modlauncher.Launcher;
import java.util.HashMap;
import cpw.mods.modlauncher.api.TypesafeMap;
import org.spongepowered.asm.service.IPropertyKey;
import java.util.Map;
import org.spongepowered.asm.service.IGlobalPropertyService;

public class Blackboard implements IGlobalPropertyService
{
    private final Map<String, IPropertyKey> keys;
    private final TypesafeMap blackboard;
    
    public Blackboard() {
        this.keys = new HashMap<String, IPropertyKey>();
        this.blackboard = Launcher.INSTANCE.blackboard();
    }
    
    @Override
    public IPropertyKey resolveKey(final String name) {
        return this.keys.computeIfAbsent(name, key -> new Key(this.blackboard, key, Object.class));
    }
    
    @Override
    public <T> T getProperty(final IPropertyKey key) {
        return this.getProperty(key, (T)null);
    }
    
    @Override
    public void setProperty(final IPropertyKey key, final Object value) {
        this.blackboard.computeIfAbsent((TypesafeMap.Key)((Key)key).key, k -> value);
    }
    
    @Override
    public String getPropertyString(final IPropertyKey key, final String defaultValue) {
        return this.getProperty(key, defaultValue);
    }
    
    @Override
    public <T> T getProperty(final IPropertyKey key, final T defaultValue) {
        return this.blackboard.get((TypesafeMap.Key)((Key)key).key).orElse(defaultValue);
    }
    
    class Key<V> implements IPropertyKey
    {
        final TypesafeMap.Key<V> key;
        
        public Key(final TypesafeMap owner, final String name, final Class<V> clazz) {
            this.key = (TypesafeMap.Key<V>)TypesafeMap.Key.getOrCreate(owner, name, (Class)clazz);
        }
    }
}
