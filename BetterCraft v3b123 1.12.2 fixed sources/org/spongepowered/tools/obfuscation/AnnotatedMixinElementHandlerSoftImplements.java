// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation;

import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.mirror.MethodHandle;
import java.util.Iterator;
import java.util.List;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import javax.lang.model.type.DeclaredType;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import javax.lang.model.element.Element;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerEx;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

class AnnotatedMixinElementHandlerSoftImplements extends AnnotatedMixinElementHandler
{
    AnnotatedMixinElementHandlerSoftImplements(final IMixinAnnotationProcessor ap, final AnnotatedMixin mixin) {
        super(ap, mixin);
    }
    
    public void process(final AnnotationHandle implementsAnnotation) {
        if (!this.mixin.remap()) {
            return;
        }
        final List<IAnnotationHandle> interfaces = implementsAnnotation.getAnnotationList("value");
        if (interfaces.size() < 1) {
            this.ap.printMessage(IMessagerEx.MessageType.SOFT_IMPLEMENTS_EMPTY, "Empty @Implements annotation", this.mixin.getMixinElement(), implementsAnnotation.asMirror());
            return;
        }
        for (final IAnnotationHandle interfaceAnnotation : interfaces) {
            final Interface.Remap remap = interfaceAnnotation.getValue("remap", Interface.Remap.ALL);
            if (remap == Interface.Remap.NONE) {
                continue;
            }
            try {
                final TypeHandle iface = new TypeHandle(interfaceAnnotation.getValue("iface"));
                final String prefix = interfaceAnnotation.getValue("prefix");
                this.processSoftImplements(remap, iface, prefix);
            }
            catch (final Exception ex) {
                this.ap.printMessage(IMessagerEx.MessageType.ERROR, "Unexpected error: " + ex.getClass().getName() + ": " + ex.getMessage(), this.mixin.getMixinElement(), ((AnnotationHandle)interfaceAnnotation).asMirror());
            }
        }
    }
    
    private void processSoftImplements(final Interface.Remap remap, final TypeHandle iface, final String prefix) {
        for (final MethodHandle method : iface.getMethods()) {
            this.processMethod(remap, iface, prefix, method);
        }
        for (final TypeHandle superInterface : iface.getInterfaces()) {
            this.processSoftImplements(remap, superInterface, prefix);
        }
    }
    
    private void processMethod(final Interface.Remap remap, final TypeHandle iface, final String prefix, final MethodHandle method) {
        final String name = method.getName();
        final String sig = method.getJavaSignature();
        final String desc = method.getDesc();
        if (remap != Interface.Remap.ONLY_PREFIXED) {
            final MethodHandle mixinMethod = this.mixin.getHandle().findMethod(name, sig);
            if (mixinMethod != null) {
                this.addInterfaceMethodMapping(remap, iface, null, mixinMethod, name, desc);
            }
        }
        if (prefix != null) {
            final MethodHandle prefixedMixinMethod = this.mixin.getHandle().findMethod(prefix + name, sig);
            if (prefixedMixinMethod != null) {
                this.addInterfaceMethodMapping(remap, iface, prefix, prefixedMixinMethod, name, desc);
            }
        }
    }
    
    private void addInterfaceMethodMapping(final Interface.Remap remap, final TypeHandle iface, final String prefix, final MethodHandle method, final String name, final String desc) {
        final MappingMethod mapping = new MappingMethod(iface.getName(), name, desc);
        final ObfuscationData<MappingMethod> obfData = this.obf.getDataProvider().getObfMethod(mapping);
        if (obfData.isEmpty()) {
            if (remap.forceRemap()) {
                this.ap.printMessage(IMessagerEx.MessageType.NO_OBFDATA_FOR_SOFT_IMPLEMENTS, "No obfuscation mapping for soft-implementing method", method.getElement());
            }
            return;
        }
        this.addMethodMappings(method.getName(), desc, this.applyPrefix(obfData, prefix));
    }
    
    private ObfuscationData<MappingMethod> applyPrefix(final ObfuscationData<MappingMethod> data, final String prefix) {
        if (prefix == null) {
            return data;
        }
        final ObfuscationData<MappingMethod> prefixed = new ObfuscationData<MappingMethod>();
        for (final ObfuscationType type : data) {
            final MappingMethod mapping = data.get(type);
            prefixed.put(type, mapping.addPrefix(prefix));
        }
        return prefixed;
    }
}
