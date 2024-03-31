// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.objectweb.asm.tree.ClassNode;

abstract class MixinCoprocessor implements MixinConfig.IListener
{
    abstract String getName();
    
    @Override
    public void onPrepare(final MixinInfo mixin) {
    }
    
    @Override
    public void onInit(final MixinInfo mixin) {
    }
    
    ProcessResult process(final String className, final ClassNode classNode) {
        return ProcessResult.NONE;
    }
    
    boolean postProcess(final String className, final ClassNode classNode) {
        return false;
    }
    
    enum ProcessResult
    {
        NONE(false, false), 
        TRANSFORMED(false, true), 
        PASSTHROUGH_NONE(true, false), 
        PASSTHROUGH_TRANSFORMED(true, true);
        
        private boolean passthrough;
        private boolean transformed;
        
        private ProcessResult(final boolean passthrough, final boolean transformed) {
            this.passthrough = passthrough;
            this.transformed = transformed;
        }
        
        boolean isPassthrough() {
            return this.passthrough;
        }
        
        boolean isTransformed() {
            return this.transformed;
        }
        
        ProcessResult with(final ProcessResult other) {
            if (other == this) {
                return this;
            }
            return of(this.passthrough || other.passthrough, this.transformed || other.transformed);
        }
        
        static ProcessResult of(final boolean passthrough, final boolean transformed) {
            if (passthrough) {
                return transformed ? ProcessResult.PASSTHROUGH_TRANSFORMED : ProcessResult.PASSTHROUGH_NONE;
            }
            return transformed ? ProcessResult.TRANSFORMED : ProcessResult.NONE;
        }
    }
}
