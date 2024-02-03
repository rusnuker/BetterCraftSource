// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import java.util.Iterator;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.util.perf.Profiler;
import java.util.ArrayList;

class MixinCoprocessors extends ArrayList<MixinCoprocessor>
{
    private static final long serialVersionUID = 1L;
    private final Profiler profiler;
    
    MixinCoprocessors() {
        this.profiler = Profiler.getProfiler("mixin");
    }
    
    MixinCoprocessor.ProcessResult process(final String className, final ClassNode classNode) {
        final Profiler.Section timer = this.profiler.begin("coprocessor");
        MixinCoprocessor.ProcessResult result = MixinCoprocessor.ProcessResult.NONE;
        for (final MixinCoprocessor coprocessor : this) {
            result = coprocessor.process(className, classNode).with(result);
        }
        timer.end();
        return result;
    }
    
    boolean postProcess(final String className, final ClassNode classNode) {
        final Profiler.Section timer = this.profiler.begin("coprocessor");
        boolean transformed = false;
        for (final MixinCoprocessor coprocessor : this) {
            transformed |= coprocessor.postProcess(className, classNode);
        }
        timer.end();
        return transformed;
    }
}
