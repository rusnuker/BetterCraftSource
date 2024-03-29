// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.code;

import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.AnnotationNode;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import java.util.HashMap;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelector;
import org.spongepowered.asm.mixin.injection.struct.Target;
import java.util.Map;

public class InjectorTarget
{
    private final ISliceContext context;
    private final Map<String, InsnListReadOnly> cache;
    private final Target target;
    private final ITargetSelector selector;
    private final String mergedBy;
    private final int mergedPriority;
    
    public InjectorTarget(final ISliceContext context, final Target target, final ITargetSelector selector) {
        this.cache = new HashMap<String, InsnListReadOnly>();
        this.context = context;
        this.target = target;
        this.selector = selector;
        final AnnotationNode merged = Annotations.getVisible(target.method, MixinMerged.class);
        this.mergedBy = Annotations.getValue(merged, "mixin");
        this.mergedPriority = Annotations.getValue(merged, "priority", 1000);
    }
    
    @Override
    public String toString() {
        return this.target.toString();
    }
    
    public Target getTarget() {
        return this.target;
    }
    
    public MethodNode getMethod() {
        return this.target.method;
    }
    
    public ITargetSelector getSelector() {
        return this.selector;
    }
    
    public boolean isMerged() {
        return this.mergedBy != null;
    }
    
    public String getMergedBy() {
        return this.mergedBy;
    }
    
    public int getMergedPriority() {
        return this.mergedPriority;
    }
    
    public InsnList getSlice(final String id) {
        InsnListReadOnly slice = this.cache.get(id);
        if (slice == null) {
            final MethodSlice sliceInfo = this.context.getSlice(id);
            if (sliceInfo != null) {
                slice = sliceInfo.getSlice(this.target.method);
            }
            else {
                slice = new InsnListReadOnly(this.target.method.instructions);
            }
            this.cache.put(id, slice);
        }
        return slice;
    }
    
    public InsnList getSlice(final InjectionPoint injectionPoint) {
        return this.getSlice(injectionPoint.getSlice());
    }
    
    public void dispose() {
        for (final InsnListReadOnly insns : this.cache.values()) {
            insns.dispose();
        }
        this.cache.clear();
    }
}
