// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.mixin.injection.Constant;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.injection.invoke.ModifyConstantInjector;
import org.spongepowered.asm.mixin.injection.code.Injector;
import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.points.BeforeConstant;
import org.objectweb.asm.Type;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@AnnotationType(ModifyConstant.class)
@HandlerPrefix("constant")
public class ModifyConstantInjectionInfo extends InjectionInfo
{
    private static final String CONSTANT_ANNOTATION_CLASS;
    
    public ModifyConstantInjectionInfo(final MixinTargetContext mixin, final MethodNode method, final AnnotationNode annotation) {
        super(mixin, method, annotation, "constant");
    }
    
    @Override
    protected List<AnnotationNode> readInjectionPoints() {
        List<AnnotationNode> ats = super.readInjectionPoints();
        if (ats.isEmpty()) {
            final AnnotationNode c = new AnnotationNode(ModifyConstantInjectionInfo.CONSTANT_ANNOTATION_CLASS);
            c.visit("log", Boolean.TRUE);
            ats = ImmutableList.of(c);
        }
        return ats;
    }
    
    @Override
    protected void parseInjectionPoints(final List<AnnotationNode> ats) {
        final Type returnType = Type.getReturnType(this.method.desc);
        for (final AnnotationNode at : ats) {
            this.injectionPoints.add(new BeforeConstant(this.getMixin(), at, returnType.getDescriptor()));
        }
    }
    
    @Override
    protected Injector parseInjector(final AnnotationNode injectAnnotation) {
        return new ModifyConstantInjector(this);
    }
    
    @Override
    protected String getDescription() {
        return "Constant modifier method";
    }
    
    @Override
    public String getSliceId(final String id) {
        return Strings.nullToEmpty(id);
    }
    
    static {
        CONSTANT_ANNOTATION_CLASS = Constant.class.getName().replace('.', '/');
    }
}
