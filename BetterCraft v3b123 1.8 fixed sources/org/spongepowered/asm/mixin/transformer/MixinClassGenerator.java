// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import java.util.Iterator;
import java.util.Locale;
import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.service.IMixinAuditTrail;
import org.spongepowered.asm.util.perf.Profiler;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.logging.ILogger;

public class MixinClassGenerator
{
    static final ILogger logger;
    private final Extensions extensions;
    private final Profiler profiler;
    private final IMixinAuditTrail auditTrail;
    
    MixinClassGenerator(final MixinEnvironment environment, final Extensions extensions) {
        this.extensions = extensions;
        this.profiler = Profiler.getProfiler("generator");
        this.auditTrail = MixinService.getService().getAuditTrail();
    }
    
    synchronized boolean generateClass(final MixinEnvironment environment, final String name, final ClassNode classNode) {
        if (name == null) {
            MixinClassGenerator.logger.warn("MixinClassGenerator tried to generate a class with no name!", new Object[0]);
            return false;
        }
        for (final IClassGenerator generator : this.extensions.getGenerators()) {
            final Profiler.Section genTimer = this.profiler.begin("generator", generator.getClass().getSimpleName().toLowerCase(Locale.ROOT));
            final boolean success = generator.generate(name, classNode);
            genTimer.end();
            if (success) {
                if (this.auditTrail != null) {
                    this.auditTrail.onGenerate(name, generator.getName());
                }
                this.extensions.export(environment, name.replace('.', '/'), false, classNode);
                return true;
            }
        }
        return false;
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
}
