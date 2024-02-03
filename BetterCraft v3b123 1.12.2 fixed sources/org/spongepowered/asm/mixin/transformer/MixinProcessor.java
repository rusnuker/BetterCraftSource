// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import java.util.Locale;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionClassExporter;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.asm.mixin.extensibility.IMixinErrorHandler;
import org.spongepowered.asm.mixin.throwables.MixinPrepareError;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import java.util.Collections;
import java.text.DecimalFormat;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.throwables.MixinApplyError;
import org.objectweb.asm.tree.AnnotationNode;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Mixin;
import java.util.SortedSet;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.spongepowered.asm.mixin.transformer.throwables.IllegalClassLoadError;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelectorDynamic;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import java.util.TreeSet;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.mixin.transformer.throwables.ReEntrantTransformerError;
import org.objectweb.asm.tree.ClassNode;
import java.util.Iterator;
import java.util.Set;
import org.spongepowered.asm.mixin.throwables.ClassAlreadyLoadedException;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.ArrayList;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.logging.Level;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.service.IMixinAuditTrail;
import org.spongepowered.asm.util.perf.Profiler;
import org.spongepowered.asm.mixin.transformer.ext.IHotSwap;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.util.ReEntranceLock;
import java.util.List;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.logging.ILogger;

class MixinProcessor
{
    static final ILogger logger;
    private final IMixinService service;
    private final List<MixinConfig> configs;
    private final List<MixinConfig> pendingConfigs;
    private final ReEntranceLock lock;
    private final String sessionId;
    private final Extensions extensions;
    private final IHotSwap hotSwapper;
    private final MixinCoprocessors coprocessors;
    private final Profiler profiler;
    private final IMixinAuditTrail auditTrail;
    private MixinEnvironment currentEnvironment;
    private Level verboseLoggingLevel;
    private boolean errorState;
    private int transformedCount;
    
    MixinProcessor(final MixinEnvironment environment, final Extensions extensions, final IHotSwap hotSwapper, final MixinCoprocessorNestHost nestHostCoprocessor) {
        this.service = MixinService.getService();
        this.configs = new ArrayList<MixinConfig>();
        this.pendingConfigs = new ArrayList<MixinConfig>();
        this.sessionId = UUID.randomUUID().toString();
        this.coprocessors = new MixinCoprocessors();
        this.verboseLoggingLevel = Level.DEBUG;
        this.errorState = false;
        this.transformedCount = 0;
        this.lock = this.service.getReEntranceLock();
        this.extensions = extensions;
        this.hotSwapper = hotSwapper;
        ((ArrayList<MixinCoprocessorPassthrough>)this.coprocessors).add(new MixinCoprocessorPassthrough());
        ((ArrayList<MixinCoprocessorSyntheticInner>)this.coprocessors).add(new MixinCoprocessorSyntheticInner());
        ((ArrayList<MixinCoprocessorAccessor>)this.coprocessors).add(new MixinCoprocessorAccessor(this.sessionId));
        ((ArrayList<MixinCoprocessorNestHost>)this.coprocessors).add(nestHostCoprocessor);
        this.profiler = Profiler.getProfiler("mixin");
        this.auditTrail = this.service.getAuditTrail();
    }
    
    public void audit(final MixinEnvironment environment) {
        final Set<String> unhandled = new HashSet<String>();
        for (final MixinConfig config : this.configs) {
            unhandled.addAll(config.getUnhandledTargets());
        }
        final ILogger auditLogger = MixinService.getService().getLogger("mixin.audit");
        for (final String target : unhandled) {
            try {
                auditLogger.info("Force-loading class {}", target);
                this.service.getClassProvider().findClass(target, true);
            }
            catch (final ClassNotFoundException ex) {
                auditLogger.error("Could not force-load " + target, ex);
            }
        }
        for (final MixinConfig config2 : this.configs) {
            for (final String target2 : config2.getUnhandledTargets()) {
                final ClassAlreadyLoadedException ex2 = new ClassAlreadyLoadedException(target2 + " was already classloaded");
                auditLogger.error("Could not force-load " + target2, ex2);
            }
        }
        if (environment.getOption(MixinEnvironment.Option.DEBUG_PROFILER)) {
            Profiler.printAuditSummary();
        }
    }
    
    synchronized boolean applyMixins(final MixinEnvironment environment, final String name, final ClassNode targetClassNode) {
        if (name == null || this.errorState) {
            return false;
        }
        final boolean locked = this.lock.push().check();
        final Profiler.Section mixinTimer = this.profiler.begin("mixin");
        if (locked) {
            for (final MixinConfig config : this.pendingConfigs) {
                if (config.hasPendingMixinsFor(name)) {
                    final ReEntrantTransformerError error = new ReEntrantTransformerError("Re-entrance error.");
                    MixinProcessor.logger.warn("Re-entrance detected during prepare phase, this will cause serious problems.", error);
                    throw error;
                }
            }
        }
        else {
            try {
                this.checkSelect(environment);
            }
            catch (final Exception ex) {
                this.lock.pop();
                mixinTimer.end();
                throw new MixinException(ex);
            }
        }
        boolean transformed = false;
        try {
            final MixinCoprocessor.ProcessResult result = this.coprocessors.process(name, targetClassNode);
            transformed |= result.isTransformed();
            if (result.isPassthrough()) {
                for (final MixinCoprocessor coprocessor : this.coprocessors) {
                    transformed |= coprocessor.postProcess(name, targetClassNode);
                }
                if (this.auditTrail != null) {
                    this.auditTrail.onPostProcess(name);
                }
                this.extensions.export(environment, name, false, targetClassNode);
                return transformed;
            }
            MixinConfig packageOwnedByConfig = null;
            for (final MixinConfig config2 : this.configs) {
                if (config2.packageMatch(name)) {
                    final int packageLen = (packageOwnedByConfig != null) ? packageOwnedByConfig.getMixinPackage().length() : 0;
                    if (config2.getMixinPackage().length() <= packageLen) {
                        continue;
                    }
                    packageOwnedByConfig = config2;
                }
            }
            if (packageOwnedByConfig == null) {
                SortedSet<MixinInfo> mixins = null;
                for (final MixinConfig config3 : this.configs) {
                    if (config3.hasMixinsFor(name)) {
                        if (mixins == null) {
                            mixins = new TreeSet<MixinInfo>();
                        }
                        mixins.addAll((Collection<?>)config3.getMixinsFor(name));
                    }
                }
                if (mixins != null) {
                    if (locked) {
                        final ReEntrantTransformerError error2 = new ReEntrantTransformerError("Re-entrance error.");
                        MixinProcessor.logger.warn("Re-entrance detected, this will cause serious problems.", error2);
                        throw error2;
                    }
                    if (this.hotSwapper != null) {
                        this.hotSwapper.registerTargetClass(name, targetClassNode);
                    }
                    try {
                        final TargetClassContext context = new TargetClassContext(environment, this.extensions, this.sessionId, name, targetClassNode, mixins);
                        context.applyMixins();
                        transformed |= this.coprocessors.postProcess(name, targetClassNode);
                        if (context.isExported()) {
                            this.extensions.export(environment, context.getClassName(), context.isExportForced(), context.getClassNode());
                        }
                        for (final InvalidMixinException suppressed : context.getSuppressedExceptions()) {
                            this.handleMixinApplyError(context.getClassName(), suppressed, environment);
                        }
                        ++this.transformedCount;
                        transformed = true;
                    }
                    catch (final InvalidMixinException th) {
                        this.dumpClassOnFailure(name, targetClassNode, environment);
                        this.handleMixinApplyError(name, th, environment);
                    }
                }
                else if (this.coprocessors.postProcess(name, targetClassNode)) {
                    transformed = true;
                    this.extensions.export(environment, name, false, targetClassNode);
                }
                return transformed;
            }
            final ClassInfo targetInfo = ClassInfo.fromClassNode(targetClassNode);
            if (targetInfo.hasSuperClass(InjectionPoint.class) || targetInfo.hasSuperClass(ITargetSelectorDynamic.class)) {
                return transformed;
            }
            throw new IllegalClassLoadError(this.getInvalidClassError(name, targetClassNode, packageOwnedByConfig));
        }
        catch (final MixinTransformerError er) {
            throw er;
        }
        catch (final Throwable th2) {
            this.dumpClassOnFailure(name, targetClassNode, environment);
            throw new MixinTransformerError("An unexpected critical error was encountered", th2);
        }
        finally {
            this.lock.pop();
            mixinTimer.end();
        }
        return transformed;
    }
    
    private String getInvalidClassError(final String name, final ClassNode targetClassNode, final MixinConfig ownedByConfig) {
        if (ownedByConfig.getClasses().contains(name)) {
            return String.format("Illegal classload request for %s. Mixin is defined in %s and cannot be referenced directly", name, ownedByConfig);
        }
        final AnnotationNode mixin = Annotations.getInvisible(targetClassNode, Mixin.class);
        if (mixin != null) {
            final MixinInfo.Variant variant = MixinInfo.getVariant(targetClassNode);
            if (variant == MixinInfo.Variant.ACCESSOR) {
                return String.format("Illegal classload request for accessor mixin %s. The mixin is missing from %s which owns package %s* and the mixin has not been applied.", name, ownedByConfig, ownedByConfig.getMixinPackage());
            }
        }
        return String.format("%s is in a defined mixin package %s* owned by %s and cannot be referenced directly", name, ownedByConfig.getMixinPackage(), ownedByConfig);
    }
    
    public List<String> reload(final String mixinClass, final ClassNode classNode) {
        if (this.lock.getDepth() > 0) {
            throw new MixinApplyError("Cannot reload mixin if re-entrant lock entered");
        }
        final List<String> targets = new ArrayList<String>();
        for (final MixinConfig config : this.configs) {
            targets.addAll(config.reloadMixin(mixinClass, classNode));
        }
        return targets;
    }
    
    private void checkSelect(final MixinEnvironment environment) {
        if (this.currentEnvironment != environment) {
            this.select(environment);
            return;
        }
        final int unvisitedCount = Mixins.getUnvisitedCount();
        if (unvisitedCount > 0 && this.transformedCount == 0) {
            this.select(environment);
        }
    }
    
    private void select(final MixinEnvironment environment) {
        this.verboseLoggingLevel = (environment.getOption(MixinEnvironment.Option.DEBUG_VERBOSE) ? Level.INFO : Level.DEBUG);
        if (this.transformedCount > 0) {
            MixinProcessor.logger.log(this.verboseLoggingLevel, "Ending {}, applied {} mixins", this.currentEnvironment, this.transformedCount);
        }
        final String action = (this.currentEnvironment == environment) ? "Checking for additional" : "Preparing";
        MixinProcessor.logger.log(this.verboseLoggingLevel, "{} mixins for {}", action, environment);
        Profiler.setActive(true);
        this.profiler.mark(environment.getPhase().toString() + ":prepare");
        final Profiler.Section prepareTimer = this.profiler.begin("prepare");
        this.selectConfigs(environment);
        this.extensions.select(environment);
        final int totalMixins = this.prepareConfigs(environment, this.extensions);
        this.currentEnvironment = environment;
        this.transformedCount = 0;
        prepareTimer.end();
        final long elapsedMs = prepareTimer.getTime();
        final double elapsedTime = prepareTimer.getSeconds();
        if (elapsedTime > 0.25) {
            final long loadTime = this.profiler.get("class.load").getTime();
            final long transformTime = this.profiler.get("class.transform").getTime();
            final long pluginTime = this.profiler.get("mixin.plugin").getTime();
            final String elapsed = new DecimalFormat("###0.000").format(elapsedTime);
            final String perMixinTime = new DecimalFormat("###0.0").format(elapsedMs / (double)totalMixins);
            MixinProcessor.logger.log(this.verboseLoggingLevel, "Prepared {} mixins in {} sec ({}ms avg) ({}ms load, {}ms transform, {}ms plugin)", totalMixins, elapsed, perMixinTime, loadTime, transformTime, pluginTime);
        }
        this.profiler.mark(environment.getPhase().toString() + ":apply");
        Profiler.setActive(environment.getOption(MixinEnvironment.Option.DEBUG_PROFILER));
    }
    
    private void selectConfigs(final MixinEnvironment environment) {
        final Iterator<Config> iter = Mixins.getConfigs().iterator();
        while (iter.hasNext()) {
            final Config handle = iter.next();
            try {
                final MixinConfig config = handle.get();
                if (!config.select(environment)) {
                    continue;
                }
                iter.remove();
                MixinProcessor.logger.log(this.verboseLoggingLevel, "Selecting config {}", config);
                config.onSelect();
                this.pendingConfigs.add(config);
            }
            catch (final Exception ex) {
                MixinProcessor.logger.warn(String.format("Failed to select mixin config: %s", handle), ex);
            }
        }
        Collections.sort(this.pendingConfigs);
    }
    
    private int prepareConfigs(final MixinEnvironment environment, final Extensions extensions) {
        int totalMixins = 0;
        final IHotSwap hotSwapper = this.hotSwapper;
        for (final MixinConfig config : this.pendingConfigs) {
            for (final MixinCoprocessor coprocessor : this.coprocessors) {
                config.addListener(coprocessor);
            }
            if (hotSwapper != null) {
                config.addListener(new MixinConfig.IListener() {
                    @Override
                    public void onPrepare(final MixinInfo mixin) {
                        hotSwapper.registerMixinClass(mixin.getClassName());
                    }
                    
                    @Override
                    public void onInit(final MixinInfo mixin) {
                    }
                });
            }
        }
        for (final MixinConfig config : this.pendingConfigs) {
            try {
                MixinProcessor.logger.log(this.verboseLoggingLevel, "Preparing {} ({})", config, config.getDeclaredMixinCount());
                config.prepare(extensions);
                totalMixins += config.getMixinCount();
            }
            catch (final InvalidMixinException ex) {
                this.handleMixinPrepareError(config, ex, environment);
            }
            catch (final Exception ex2) {
                final String message = ex2.getMessage();
                MixinProcessor.logger.error("Error encountered whilst initialising mixin config '" + config.getName() + "': " + message, ex2);
            }
        }
        for (final MixinConfig config : this.pendingConfigs) {
            final IMixinConfigPlugin plugin = config.getPlugin();
            if (plugin == null) {
                continue;
            }
            final Set<String> otherTargets = new HashSet<String>();
            for (final MixinConfig otherConfig : this.pendingConfigs) {
                if (!otherConfig.equals(config)) {
                    otherTargets.addAll(otherConfig.getTargets());
                }
            }
            plugin.acceptTargets(config.getTargetsSet(), Collections.unmodifiableSet((Set<? extends String>)otherTargets));
        }
        for (final MixinConfig config : this.pendingConfigs) {
            try {
                config.postInitialise(this.extensions);
            }
            catch (final InvalidMixinException ex) {
                this.handleMixinPrepareError(config, ex, environment);
            }
            catch (final Exception ex2) {
                final String message = ex2.getMessage();
                MixinProcessor.logger.error("Error encountered during mixin config postInit step'" + config.getName() + "': " + message, ex2);
            }
        }
        this.configs.addAll(this.pendingConfigs);
        Collections.sort(this.configs);
        this.pendingConfigs.clear();
        return totalMixins;
    }
    
    private void handleMixinPrepareError(final MixinConfig config, final InvalidMixinException ex, final MixinEnvironment environment) throws MixinPrepareError {
        this.handleMixinError(config.getName(), ex, environment, ErrorPhase.PREPARE);
    }
    
    private void handleMixinApplyError(final String targetClass, final InvalidMixinException ex, final MixinEnvironment environment) throws MixinApplyError {
        this.handleMixinError(targetClass, ex, environment, ErrorPhase.APPLY);
    }
    
    private void handleMixinError(final String context, final InvalidMixinException ex, final MixinEnvironment environment, final ErrorPhase errorPhase) throws Error {
        this.errorState = true;
        final IMixinInfo mixin = ex.getMixin();
        if (mixin == null) {
            MixinProcessor.logger.error("InvalidMixinException has no mixin!", ex);
            throw ex;
        }
        final IMixinConfig config = mixin.getConfig();
        final MixinEnvironment.Phase phase = mixin.getPhase();
        IMixinErrorHandler.ErrorAction action = config.isRequired() ? IMixinErrorHandler.ErrorAction.ERROR : IMixinErrorHandler.ErrorAction.WARN;
        if (environment.getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
            new PrettyPrinter().wrapTo(160).add("Invalid Mixin").centre().hr('-').kvWidth(10).kv("Action", (Object)errorPhase.name()).kv("Mixin", (Object)mixin.getClassName()).kv("Config", (Object)config.getName()).kv("Phase", phase).hr('-').add("    %s", ex.getClass().getName()).hr('-').addWrapped("    %s", ex.getMessage()).hr('-').add(ex, 8).log(action.logLevel);
        }
        for (final IMixinErrorHandler handler : this.getErrorHandlers(mixin.getPhase())) {
            final IMixinErrorHandler.ErrorAction newAction = errorPhase.onError(handler, context, ex, mixin, action);
            if (newAction != null) {
                action = newAction;
            }
        }
        MixinProcessor.logger.log(action.logLevel, errorPhase.getLogMessage(context, ex, mixin), ex);
        this.errorState = false;
        if (action == IMixinErrorHandler.ErrorAction.ERROR) {
            throw new MixinApplyError(errorPhase.getErrorMessage(mixin, config, phase), ex);
        }
    }
    
    private List<IMixinErrorHandler> getErrorHandlers(final MixinEnvironment.Phase phase) {
        final List<IMixinErrorHandler> handlers = new ArrayList<IMixinErrorHandler>();
        for (final String handlerClassName : Mixins.getErrorHandlerClasses()) {
            try {
                MixinProcessor.logger.info("Instancing error handler class {}", handlerClassName);
                final Class<?> handlerClass = this.service.getClassProvider().findClass(handlerClassName, true);
                final IMixinErrorHandler handler = (IMixinErrorHandler)handlerClass.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                if (handler == null) {
                    continue;
                }
                handlers.add(handler);
            }
            catch (final Throwable t) {}
        }
        return handlers;
    }
    
    private void dumpClassOnFailure(final String className, final ClassNode classNode, final MixinEnvironment env) {
        if (env.getOption(MixinEnvironment.Option.DUMP_TARGET_ON_FAILURE)) {
            final ExtensionClassExporter exporter = this.extensions.getExtension(ExtensionClassExporter.class);
            exporter.dumpClass(className.replace('.', '/') + ".target", classNode);
        }
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
    
    enum ErrorPhase
    {
        PREPARE {
            @Override
            IMixinErrorHandler.ErrorAction onError(final IMixinErrorHandler handler, final String context, final InvalidMixinException ex, final IMixinInfo mixin, final IMixinErrorHandler.ErrorAction action) {
                try {
                    return handler.onPrepareError(mixin.getConfig(), ex, mixin, action);
                }
                catch (final AbstractMethodError ame) {
                    return action;
                }
            }
            
            @Override
            protected String getContext(final IMixinInfo mixin, final String context) {
                return String.format("preparing %s in %s", mixin.getName(), context);
            }
        }, 
        APPLY {
            @Override
            IMixinErrorHandler.ErrorAction onError(final IMixinErrorHandler handler, final String context, final InvalidMixinException ex, final IMixinInfo mixin, final IMixinErrorHandler.ErrorAction action) {
                try {
                    return handler.onApplyError(context, ex, mixin, action);
                }
                catch (final AbstractMethodError ame) {
                    return action;
                }
            }
            
            @Override
            protected String getContext(final IMixinInfo mixin, final String context) {
                return String.format("%s -> %s", mixin, context);
            }
        };
        
        private final String text;
        
        private ErrorPhase() {
            this.text = this.name().toLowerCase(Locale.ROOT);
        }
        
        abstract IMixinErrorHandler.ErrorAction onError(final IMixinErrorHandler p0, final String p1, final InvalidMixinException p2, final IMixinInfo p3, final IMixinErrorHandler.ErrorAction p4);
        
        protected abstract String getContext(final IMixinInfo p0, final String p1);
        
        public String getLogMessage(final String context, final InvalidMixinException ex, final IMixinInfo mixin) {
            return String.format("Mixin %s failed %s: %s %s", this.text, this.getContext(mixin, context), ex.getClass().getName(), ex.getMessage());
        }
        
        public String getErrorMessage(final IMixinInfo mixin, final IMixinConfig config, final MixinEnvironment.Phase phase) {
            return String.format("Mixin [%s] from phase [%s] in config [%s] FAILED during %s", mixin, phase, config, this.name());
        }
    }
}
