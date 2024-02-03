// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.objectweb.asm.AnnotationVisitor;
import org.spongepowered.asm.util.asm.ASM;
import org.objectweb.asm.commons.ClassRemapper;
import java.io.IOException;
import org.objectweb.asm.commons.Remapper;
import org.spongepowered.asm.service.MixinService;
import java.util.UUID;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;
import java.util.Iterator;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.BiMap;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import java.util.HashMap;
import java.util.Map;
import org.spongepowered.asm.service.ISyntheticClassInfo;
import org.spongepowered.asm.util.IConsumer;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;

final class InnerClassGenerator implements IClassGenerator
{
    private static final ILogger logger;
    private final IConsumer<ISyntheticClassInfo> registry;
    private final Map<String, String> innerClassNames;
    private final Map<String, InnerClassInfo> innerClasses;
    private final MixinCoprocessorNestHost nestHostCoprocessor;
    
    public InnerClassGenerator(final IConsumer<ISyntheticClassInfo> registry, final MixinCoprocessorNestHost nestHostCoprocessor) {
        this.innerClassNames = new HashMap<String, String>();
        this.innerClasses = new HashMap<String, InnerClassInfo>();
        this.registry = registry;
        this.nestHostCoprocessor = nestHostCoprocessor;
    }
    
    @Override
    public String getName() {
        return "inner";
    }
    
    void registerInnerClass(final MixinInfo owner, final ClassInfo targetClass, final String innerClassName) {
        final String coordinate = String.format("%s:%s:%s", owner, innerClassName, targetClass.getName());
        String uniqueName = this.innerClassNames.get(coordinate);
        if (uniqueName != null) {
            return;
        }
        uniqueName = getUniqueReference(innerClassName, targetClass);
        final ClassInfo nestHost = targetClass.resolveNestHost();
        final InnerClassInfo info = new InnerClassInfo(owner, targetClass, nestHost, innerClassName, uniqueName, owner);
        this.innerClassNames.put(coordinate, uniqueName);
        this.innerClasses.put(uniqueName, info);
        this.registry.accept(info);
        InnerClassGenerator.logger.debug("Inner class {} in {} on {} gets unique name {}", innerClassName, owner.getClassRef(), targetClass, uniqueName);
        this.nestHostCoprocessor.registerNestMember(nestHost.getClassName(), uniqueName);
    }
    
    BiMap<String, String> getInnerClasses(final MixinInfo owner, final String targetName) {
        final BiMap<String, String> innerClasses = (BiMap<String, String>)HashBiMap.create();
        for (final InnerClassInfo innerClass : this.innerClasses.values()) {
            if (innerClass.getMixin() == owner && targetName.equals(innerClass.getTargetName())) {
                innerClasses.put(innerClass.getOriginalName(), innerClass.getName());
            }
        }
        return innerClasses;
    }
    
    @Override
    public boolean generate(final String name, final ClassNode classNode) {
        final String ref = name.replace('.', '/');
        final InnerClassInfo info = this.innerClasses.get(ref);
        return info != null && this.generate(info, classNode);
    }
    
    private boolean generate(final InnerClassInfo info, final ClassNode classNode) {
        try {
            InnerClassGenerator.logger.debug("Generating mapped inner class {} (originally {})", info.getName(), info.getOriginalName());
            info.accept(new InnerClassAdapter(classNode, info));
            return true;
        }
        catch (final InvalidMixinException ex) {
            throw ex;
        }
        catch (final Exception ex2) {
            InnerClassGenerator.logger.catching(ex2);
            return false;
        }
    }
    
    private static String getUniqueReference(final String originalName, final ClassInfo targetClass) {
        String name = originalName.substring(originalName.lastIndexOf(36) + 1);
        if (name.matches("^[0-9]+$")) {
            name = "Anonymous";
        }
        return String.format("%s$%s$%s", targetClass, name, UUID.randomUUID().toString().replace("-", ""));
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
    
    static class InnerClassInfo extends Remapper implements ISyntheticClassInfo
    {
        private final IMixinInfo mixin;
        private final ClassInfo targetClassInfo;
        private final String originalName;
        private final String name;
        private final MixinInfo owner;
        private final String ownerName;
        private final String nestHostName;
        private int loadCounter;
        
        InnerClassInfo(final IMixinInfo mixin, final ClassInfo targetClass, final ClassInfo nestHost, final String originalName, final String name, final MixinInfo owner) {
            this.mixin = mixin;
            this.targetClassInfo = targetClass;
            this.originalName = originalName;
            this.name = name;
            this.owner = owner;
            this.ownerName = owner.getClassRef();
            this.nestHostName = nestHost.getName();
        }
        
        @Override
        public IMixinInfo getMixin() {
            return this.mixin;
        }
        
        @Override
        public boolean isLoaded() {
            return this.loadCounter > 0;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        @Override
        public String getClassName() {
            return this.name.replace('/', '.');
        }
        
        String getOriginalName() {
            return this.originalName;
        }
        
        MixinInfo getOwner() {
            return this.owner;
        }
        
        String getOwnerName() {
            return this.ownerName;
        }
        
        String getTargetName() {
            return this.targetClassInfo.getName();
        }
        
        ClassInfo getTargetClass() {
            return this.targetClassInfo;
        }
        
        String getNestHostName() {
            return this.nestHostName;
        }
        
        void accept(final ClassVisitor classVisitor) throws ClassNotFoundException, IOException {
            final ClassNode classNode = MixinService.getService().getBytecodeProvider().getClassNode(this.originalName);
            classNode.accept(classVisitor);
            ++this.loadCounter;
        }
        
        @Override
        public String mapMethodName(final String owner, final String name, final String desc) {
            if (this.ownerName.equalsIgnoreCase(owner)) {
                final ClassInfo.Method method = this.owner.getClassInfo().findMethod(name, desc, 10);
                if (method != null) {
                    return method.getName();
                }
            }
            return super.mapMethodName(owner, name, desc);
        }
        
        @Override
        public String map(final String key) {
            if (this.originalName.equals(key)) {
                return this.name;
            }
            if (this.ownerName.equals(key)) {
                return this.targetClassInfo.getName();
            }
            return key;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
    
    static class InnerClassAdapter extends ClassRemapper
    {
        private final InnerClassInfo info;
        
        InnerClassAdapter(final ClassVisitor cv, final InnerClassInfo info) {
            super(ASM.API_VERSION, cv, info);
            this.info = info;
        }
        
        @Override
        public void visitNestHost(final String nestHost) {
            this.cv.visitNestHost(this.info.getNestHostName());
        }
        
        @Override
        public void visitSource(final String source, final String debug) {
            super.visitSource(source, debug);
            final AnnotationVisitor av = this.cv.visitAnnotation("Lorg/spongepowered/asm/mixin/transformer/meta/MixinInner;", false);
            av.visit("mixin", this.info.getOwner().toString());
            av.visit("name", this.info.getOriginalName().substring(this.info.getOriginalName().lastIndexOf(47) + 1));
            av.visitEnd();
        }
        
        @Override
        public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
            if (name.startsWith(this.info.getOriginalName() + "$")) {
                throw new InvalidMixinException(this.info.getOwner(), "Found unsupported nested inner class " + name + " in " + this.info.getOriginalName());
            }
            super.visitInnerClass(name, outerName, innerName, access);
        }
    }
}
