// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.objectweb.asm.tree.ClassNode;
import java.util.HashSet;
import java.util.Set;

class MixinCoprocessorPassthrough extends MixinCoprocessor
{
    private final Set<String> loadable;
    
    MixinCoprocessorPassthrough() {
        this.loadable = new HashSet<String>();
    }
    
    @Override
    String getName() {
        return "passthrough";
    }
    
    @Override
    public void onPrepare(final MixinInfo mixin) {
        if (mixin.isLoadable()) {
            this.registerLoadable(mixin.getClassName());
        }
    }
    
    void registerLoadable(final String className) {
        this.loadable.add(className);
    }
    
    @Override
    ProcessResult process(final String className, final ClassNode classNode) {
        return this.loadable.contains(className) ? ProcessResult.PASSTHROUGH_NONE : ProcessResult.NONE;
    }
}
