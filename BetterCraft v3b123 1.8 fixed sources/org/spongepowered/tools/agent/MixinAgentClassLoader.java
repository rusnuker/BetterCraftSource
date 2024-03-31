// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.agent;

import org.spongepowered.asm.service.MixinService;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import java.util.HashMap;
import java.util.Map;
import org.spongepowered.asm.logging.ILogger;

class MixinAgentClassLoader extends ClassLoader
{
    private static final ILogger logger;
    private Map<Class<?>, byte[]> mixins;
    private Map<String, byte[]> targets;
    
    MixinAgentClassLoader() {
        this.mixins = new HashMap<Class<?>, byte[]>();
        this.targets = new HashMap<String, byte[]>();
    }
    
    void addMixinClass(final String name) {
        MixinAgentClassLoader.logger.debug("Mixin class {} added to class loader", name);
        try {
            final byte[] bytes = this.materialise(name);
            final Class<?> clazz = this.defineClass(name, bytes, 0, bytes.length);
            clazz.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            this.mixins.put(clazz, bytes);
        }
        catch (final Throwable e) {
            MixinAgentClassLoader.logger.catching(e);
        }
    }
    
    void addTargetClass(final String name, final ClassNode classNode) {
        synchronized (this.targets) {
            if (this.targets.containsKey(name)) {
                return;
            }
            try {
                final ClassWriter cw = new ClassWriter(0);
                classNode.accept(cw);
                this.targets.put(name, cw.toByteArray());
            }
            catch (final Exception ex) {
                MixinAgentClassLoader.logger.error("Error storing original class bytecode for {} in mixin hotswap agent. {}: {}", name, ex.getClass().getName(), ex.getMessage());
                MixinAgentClassLoader.logger.debug(ex.toString(), new Object[0]);
            }
        }
    }
    
    byte[] getFakeMixinBytecode(final Class<?> clazz) {
        return this.mixins.get(clazz);
    }
    
    byte[] getOriginalTargetBytecode(final String name) {
        synchronized (this.targets) {
            return this.targets.get(name);
        }
    }
    
    private byte[] materialise(final String name) {
        final ClassWriter cw = new ClassWriter(3);
        cw.visit(MixinEnvironment.getCompatibilityLevel().getClassVersion(), 1, name.replace('.', '/'), null, Type.getInternalName(Object.class), null);
        final MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitMethodInsn(183, Type.getInternalName(Object.class), "<init>", "()V", false);
        mv.visitInsn(177);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        cw.visitEnd();
        return cw.toByteArray();
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin.agent");
    }
}
