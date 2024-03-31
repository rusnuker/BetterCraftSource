// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.agent;

import org.spongepowered.asm.service.IMixinService;
import java.lang.instrument.ClassDefinition;
import org.spongepowered.asm.mixin.transformer.throwables.MixinReloadException;
import java.lang.instrument.IllegalClassFormatException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.spongepowered.asm.transformers.MixinClassReader;
import org.spongepowered.asm.util.asm.ASM;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import org.spongepowered.asm.service.MixinService;
import java.util.Iterator;
import org.objectweb.asm.tree.ClassNode;
import java.lang.instrument.ClassFileTransformer;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import java.util.List;
import java.lang.instrument.Instrumentation;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.transformer.ext.IHotSwap;

public class MixinAgent implements IHotSwap
{
    public static final byte[] ERROR_BYTECODE;
    static final MixinAgentClassLoader classLoader;
    static final ILogger logger;
    static Instrumentation instrumentation;
    private static List<MixinAgent> agents;
    final IMixinTransformer classTransformer;
    
    public MixinAgent(final IMixinTransformer classTransformer) {
        this.classTransformer = classTransformer;
        MixinAgent.agents.add(this);
        if (MixinAgent.instrumentation != null) {
            this.initTransformer();
        }
    }
    
    private void initTransformer() {
        MixinAgent.instrumentation.addTransformer(new Transformer(), true);
    }
    
    @Override
    public void registerMixinClass(final String name) {
        MixinAgent.classLoader.addMixinClass(name);
    }
    
    @Override
    public void registerTargetClass(final String name, final ClassNode classNode) {
        MixinAgent.classLoader.addTargetClass(name, classNode);
    }
    
    public static void init(final Instrumentation instrumentation) {
        MixinAgent.instrumentation = instrumentation;
        if (!MixinAgent.instrumentation.isRedefineClassesSupported()) {
            MixinAgent.logger.error("The instrumentation doesn't support re-definition of classes", new Object[0]);
        }
        for (final MixinAgent agent : MixinAgent.agents) {
            agent.initTransformer();
        }
    }
    
    public static void premain(final String arg, final Instrumentation instrumentation) {
        System.setProperty("mixin.hotSwap", "true");
        init(instrumentation);
    }
    
    public static void agentmain(final String arg, final Instrumentation instrumentation) {
        init(instrumentation);
    }
    
    static {
        ERROR_BYTECODE = new byte[] { 1 };
        classLoader = new MixinAgentClassLoader();
        logger = MixinService.getService().getLogger("mixin.agent");
        MixinAgent.instrumentation = null;
        MixinAgent.agents = new ArrayList<MixinAgent>();
    }
    
    class Transformer implements ClassFileTransformer
    {
        @Override
        public byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined, final ProtectionDomain domain, final byte[] classfileBuffer) throws IllegalClassFormatException {
            if (classBeingRedefined == null) {
                return null;
            }
            final byte[] mixinBytecode = MixinAgent.classLoader.getFakeMixinBytecode(classBeingRedefined);
            if (mixinBytecode != null) {
                final ClassNode classNode = new ClassNode(ASM.API_VERSION);
                final ClassReader cr = new MixinClassReader(classfileBuffer, className);
                cr.accept(classNode, 8);
                final List<String> targets = this.reloadMixin(className, classNode);
                if (targets == null || !this.reApplyMixins(targets)) {
                    return MixinAgent.ERROR_BYTECODE;
                }
                return mixinBytecode;
            }
            else {
                try {
                    MixinAgent.logger.info("Redefining class {}", className);
                    return MixinAgent.this.classTransformer.transformClassBytes(null, className, classfileBuffer);
                }
                catch (final Throwable th) {
                    MixinAgent.logger.error("Error while re-transforming class {}", className, th);
                    return MixinAgent.ERROR_BYTECODE;
                }
            }
        }
        
        private List<String> reloadMixin(final String className, final ClassNode classNode) {
            MixinAgent.logger.info("Redefining mixin {}", className);
            try {
                return MixinAgent.this.classTransformer.reload(className.replace('/', '.'), classNode);
            }
            catch (final MixinReloadException e) {
                MixinAgent.logger.error("Mixin {} cannot be reloaded, needs a restart to be applied: {} ", e.getMixinInfo(), e.getMessage());
            }
            catch (final Throwable th) {
                MixinAgent.logger.error("Error while finding targets for mixin {}", className, th);
            }
            return null;
        }
        
        private boolean reApplyMixins(final List<String> targets) {
            final IMixinService service = MixinService.getService();
            for (final String target : targets) {
                final String targetName = target.replace('/', '.');
                MixinAgent.logger.debug("Re-transforming target class {}", target);
                try {
                    final Class<?> targetClass = service.getClassProvider().findClass(targetName);
                    byte[] targetBytecode = MixinAgent.classLoader.getOriginalTargetBytecode(targetName);
                    if (targetBytecode == null) {
                        MixinAgent.logger.error("Target class {} bytecode is not registered", targetName);
                        return false;
                    }
                    targetBytecode = MixinAgent.this.classTransformer.transformClassBytes(null, targetName, targetBytecode);
                    MixinAgent.instrumentation.redefineClasses(new ClassDefinition(targetClass, targetBytecode));
                }
                catch (final Throwable th) {
                    MixinAgent.logger.error("Error while re-transforming target class {}", target, th);
                    return false;
                }
            }
            return true;
        }
    }
}
