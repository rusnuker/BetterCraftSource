// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.service.MixinService;
import org.objectweb.asm.tree.AnnotationNode;
import java.io.OutputStream;
import org.spongepowered.asm.util.Bytecode;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckClass;
import java.util.Iterator;
import java.util.Deque;
import org.objectweb.asm.tree.FieldNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import java.util.List;
import org.objectweb.asm.tree.MethodNode;
import java.util.Set;
import org.spongepowered.asm.mixin.injection.struct.Target;
import java.util.Map;
import java.util.SortedSet;
import org.spongepowered.asm.util.ClassSignature;
import org.spongepowered.asm.mixin.struct.SourceMap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.util.perf.Profiler;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

final class TargetClassContext extends ClassContext implements ITargetClassContext
{
    private static final ILogger logger;
    private final MixinEnvironment env;
    private final Extensions extensions;
    private final Profiler profiler;
    private final String sessionId;
    private final String className;
    private final ClassNode classNode;
    private final ClassInfo classInfo;
    private final SourceMap sourceMap;
    private final ClassSignature signature;
    private final SortedSet<MixinInfo> mixins;
    private final Map<String, Target> targetMethods;
    private final Set<MethodNode> mixinMethods;
    private final List<InvalidMixinException> suppressedExceptions;
    private boolean applied;
    private boolean export;
    private boolean forceExport;
    
    TargetClassContext(final MixinEnvironment env, final Extensions extensions, final String sessionId, final String name, final ClassNode classNode, final SortedSet<MixinInfo> mixins) {
        this.targetMethods = new HashMap<String, Target>();
        this.mixinMethods = new HashSet<MethodNode>();
        this.suppressedExceptions = new ArrayList<InvalidMixinException>();
        this.env = env;
        this.extensions = extensions;
        this.profiler = Profiler.getProfiler("mixin");
        this.sessionId = sessionId;
        this.className = name;
        this.classNode = classNode;
        this.classInfo = ClassInfo.fromClassNode(classNode);
        this.signature = this.classInfo.getSignature();
        this.mixins = mixins;
        (this.sourceMap = new SourceMap(classNode.sourceFile)).addFile(this.classNode);
    }
    
    @Override
    public String toString() {
        return this.className;
    }
    
    boolean isApplied() {
        return this.applied;
    }
    
    boolean isExported() {
        return this.export;
    }
    
    boolean isExportForced() {
        return this.forceExport;
    }
    
    Extensions getExtensions() {
        return this.extensions;
    }
    
    String getSessionId() {
        return this.sessionId;
    }
    
    @Override
    String getClassRef() {
        return this.classNode.name;
    }
    
    String getClassName() {
        return this.className;
    }
    
    @Override
    public ClassNode getClassNode() {
        return this.classNode;
    }
    
    List<MethodNode> getMethods() {
        return this.classNode.methods;
    }
    
    List<FieldNode> getFields() {
        return this.classNode.fields;
    }
    
    @Override
    public ClassInfo getClassInfo() {
        return this.classInfo;
    }
    
    SortedSet<MixinInfo> getMixins() {
        return this.mixins;
    }
    
    SourceMap getSourceMap() {
        return this.sourceMap;
    }
    
    void mergeSignature(final ClassSignature signature) {
        this.signature.merge(signature);
    }
    
    void addMixinMethod(final MethodNode method) {
        this.mixinMethods.add(method);
    }
    
    void methodMerged(final MethodNode method) {
        if (!this.mixinMethods.remove(method)) {
            TargetClassContext.logger.debug("Unexpected: Merged unregistered method {}{} in {}", method.name, method.desc, this);
        }
    }
    
    MethodNode findMethod(final Deque<String> aliases, final String desc) {
        return this.findAliasedMethod(aliases, desc, true);
    }
    
    MethodNode findAliasedMethod(final Deque<String> aliases, final String desc) {
        return this.findAliasedMethod(aliases, desc, false);
    }
    
    private MethodNode findAliasedMethod(final Deque<String> aliases, final String desc, final boolean includeMixinMethods) {
        final String alias = aliases.poll();
        if (alias == null) {
            return null;
        }
        for (final MethodNode target : this.classNode.methods) {
            if (target.name.equals(alias) && target.desc.equals(desc)) {
                return target;
            }
        }
        if (includeMixinMethods) {
            for (final MethodNode target : this.mixinMethods) {
                if (target.name.equals(alias) && target.desc.equals(desc)) {
                    return target;
                }
            }
        }
        return this.findAliasedMethod(aliases, desc);
    }
    
    FieldNode findAliasedField(final Deque<String> aliases, final String desc) {
        final String alias = aliases.poll();
        if (alias == null) {
            return null;
        }
        for (final FieldNode target : this.classNode.fields) {
            if (target.name.equals(alias) && target.desc.equals(desc)) {
                return target;
            }
        }
        return this.findAliasedField(aliases, desc);
    }
    
    Target getTargetMethod(final MethodNode method) {
        if (!this.classNode.methods.contains(method)) {
            throw new IllegalArgumentException("Invalid target method supplied to getTargetMethod()");
        }
        final String targetName = method.name + method.desc;
        Target target = this.targetMethods.get(targetName);
        if (target == null) {
            target = new Target(this.classNode, method);
            this.targetMethods.put(targetName, target);
        }
        return target;
    }
    
    void applyMixins() {
        if (this.applied) {
            throw new IllegalStateException("Mixins already applied to target class " + this.className);
        }
        this.applied = true;
        Profiler.Section timer = this.profiler.begin("preapply");
        this.preApply();
        timer = timer.next("apply");
        this.apply();
        timer = timer.next("postapply");
        this.postApply();
        timer.end();
    }
    
    private void preApply() {
        this.extensions.preApply(this);
    }
    
    private void apply() {
        final MixinApplicatorStandard applicator = this.createApplicator();
        applicator.apply(this.mixins);
        this.applySignature();
        this.upgradeMethods();
        this.checkMerges();
    }
    
    private void postApply() {
        try {
            this.extensions.postApply(this);
            this.export = true;
        }
        catch (final ExtensionCheckClass.ValidationFailedException ex) {
            MixinProcessor.logger.info(ex.getMessage(), new Object[0]);
            this.export |= (this.forceExport || this.env.getOption(MixinEnvironment.Option.DEBUG_EXPORT));
        }
    }
    
    private MixinApplicatorStandard createApplicator() {
        if (this.classInfo.isInterface()) {
            return new MixinApplicatorInterface(this);
        }
        return new MixinApplicatorStandard(this);
    }
    
    private void applySignature() {
        this.classNode.signature = this.signature.toString();
    }
    
    private void checkMerges() {
        for (final MethodNode method : this.mixinMethods) {
            if (!method.name.startsWith("<")) {
                TargetClassContext.logger.debug("Unexpected: Registered method {}{} in {} was not merged", method.name, method.desc, this);
            }
        }
    }
    
    void processDebugTasks() {
        final AnnotationNode classDebugAnnotation = Annotations.getVisible(this.classNode, Debug.class);
        this.forceExport = (classDebugAnnotation != null && Boolean.TRUE.equals(Annotations.getValue(classDebugAnnotation, "export")));
        if (!this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
            return;
        }
        if (classDebugAnnotation != null && Boolean.TRUE.equals(Annotations.getValue(classDebugAnnotation, "print"))) {
            Bytecode.textify(this.classNode, System.err);
        }
        for (final MethodNode method : this.classNode.methods) {
            final AnnotationNode methodDebugAnnotation = Annotations.getVisible(method, Debug.class);
            if (methodDebugAnnotation != null && Boolean.TRUE.equals(Annotations.getValue(methodDebugAnnotation, "print"))) {
                Bytecode.textify(method, System.err);
            }
        }
    }
    
    void addSuppressed(final InvalidMixinException ex) {
        this.suppressedExceptions.add(ex);
    }
    
    List<InvalidMixinException> getSuppressedExceptions() {
        return this.suppressedExceptions;
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
}
