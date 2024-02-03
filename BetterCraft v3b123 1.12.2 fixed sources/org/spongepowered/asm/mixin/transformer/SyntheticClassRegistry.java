// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.throwables.MixinError;
import java.util.HashMap;
import org.spongepowered.asm.service.ISyntheticClassInfo;
import java.util.Map;
import org.spongepowered.asm.service.ISyntheticClassRegistry;

class SyntheticClassRegistry implements ISyntheticClassRegistry
{
    private final Map<String, ISyntheticClassInfo> classes;
    
    SyntheticClassRegistry() {
        this.classes = new HashMap<String, ISyntheticClassInfo>();
    }
    
    @Override
    public ISyntheticClassInfo findSyntheticClass(final String name) {
        if (name == null) {
            return null;
        }
        return this.classes.get(name.replace('.', '/'));
    }
    
    void registerSyntheticClass(final ISyntheticClassInfo sci) {
        final String name = sci.getName();
        final ISyntheticClassInfo info = this.classes.get(name);
        if (info == null) {
            this.classes.put(name, sci);
            return;
        }
        if (info == sci) {
            return;
        }
        throw new MixinError("Synthetic class with name " + name + " was already registered by " + info.getMixin() + ". Duplicate being registered by " + sci.getMixin());
    }
}
