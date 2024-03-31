// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import java.lang.reflect.Constructor;
import org.spongepowered.asm.mixin.refmap.RemappingReferenceMapper;
import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
import org.spongepowered.asm.util.VersionNumber;
import org.spongepowered.asm.mixin.injection.selectors.TargetSelector;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelectorDynamic;
import java.util.Collection;
import org.objectweb.asm.tree.InsnList;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.spongepowered.asm.logging.Level;
import java.util.Locale;
import org.spongepowered.asm.launch.MixinInitialisationError;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.service.IMixinService;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import org.spongepowered.asm.logging.ILogger;
import java.util.Set;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;

final class MixinConfig implements Comparable<MixinConfig>, IMixinConfig
{
    private static int configOrder;
    private static final Set<String> globalMixinList;
    private final ILogger logger;
    private final transient Map<String, List<MixinInfo>> mixinMapping;
    private final transient Set<String> unhandledTargets;
    private final transient List<MixinInfo> pendingMixins;
    private final transient List<MixinInfo> mixins;
    private transient Config handle;
    private transient MixinConfig parent;
    @SerializedName("parent")
    private String parentName;
    @SerializedName("target")
    private String selector;
    @SerializedName("minVersion")
    private String version;
    @SerializedName("compatibilityLevel")
    private String compatibility;
    @SerializedName("required")
    private Boolean requiredValue;
    private transient boolean required;
    @SerializedName("priority")
    private int priority;
    @SerializedName("mixinPriority")
    private int mixinPriority;
    @SerializedName("package")
    private String mixinPackage;
    @SerializedName("mixins")
    private List<String> mixinClasses;
    @SerializedName("client")
    private List<String> mixinClassesClient;
    @SerializedName("server")
    private List<String> mixinClassesServer;
    @SerializedName("setSourceFile")
    private boolean setSourceFile;
    @SerializedName("refmap")
    private String refMapperConfig;
    @SerializedName("refmapWrapper")
    private String refMapperWrapper;
    @SerializedName("verbose")
    private boolean verboseLogging;
    private final transient int order;
    private final transient List<IListener> listeners;
    private transient IMixinService service;
    private transient MixinEnvironment env;
    private transient String name;
    @SerializedName("plugin")
    private String pluginClassName;
    @SerializedName("injectors")
    private InjectorOptions injectorOptions;
    @SerializedName("overwrites")
    private OverwriteOptions overwriteOptions;
    private transient PluginHandle plugin;
    private transient IReferenceMapper refMapper;
    private transient boolean initialised;
    private transient boolean prepared;
    private transient boolean visited;
    private transient MixinEnvironment.CompatibilityLevel compatibilityLevel;
    private transient int warnedClassVersion;
    private transient Map<String, Object> decorations;
    
    private MixinConfig() {
        this.logger = MixinService.getService().getLogger("mixin");
        this.mixinMapping = new HashMap<String, List<MixinInfo>>();
        this.unhandledTargets = new HashSet<String>();
        this.pendingMixins = new ArrayList<MixinInfo>();
        this.mixins = new ArrayList<MixinInfo>();
        this.priority = -1;
        this.mixinPriority = -1;
        this.setSourceFile = false;
        this.order = MixinConfig.configOrder++;
        this.listeners = new ArrayList<IListener>();
        this.initialised = false;
        this.prepared = false;
        this.visited = false;
        this.compatibilityLevel = MixinEnvironment.CompatibilityLevel.DEFAULT;
        this.warnedClassVersion = 0;
    }
    
    private boolean onLoad(final IMixinService service, final String name, final MixinEnvironment fallbackEnvironment) {
        this.service = service;
        this.name = name;
        if (!Strings.isNullOrEmpty(this.parentName)) {
            return true;
        }
        this.env = this.parseSelector(this.selector, fallbackEnvironment);
        this.verboseLogging |= this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE);
        this.required = (this.requiredValue != null && this.requiredValue && !this.env.getOption(MixinEnvironment.Option.IGNORE_REQUIRED));
        this.initPriority(1000, 1000);
        if (this.injectorOptions == null) {
            this.injectorOptions = new InjectorOptions();
        }
        if (this.overwriteOptions == null) {
            this.overwriteOptions = new OverwriteOptions();
        }
        return this.postInit();
    }
    
    String getParentName() {
        return this.parentName;
    }
    
    boolean assignParent(final Config parentConfig) {
        if (this.parent != null) {
            throw new MixinInitialisationError("Mixin config " + this.name + " was already initialised");
        }
        if (parentConfig.get() == this) {
            throw new MixinInitialisationError("Mixin config " + this.name + " cannot be its own parent");
        }
        this.parent = parentConfig.get();
        if (!this.parent.initialised) {
            throw new MixinInitialisationError("Mixin config " + this.name + " attempted to assign uninitialised parent config. This probably means that there is an indirect loop in the mixin configs: child -> parent -> child");
        }
        this.env = this.parseSelector(this.selector, this.parent.env);
        this.verboseLogging |= this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE);
        this.required = ((this.requiredValue == null) ? this.parent.required : (this.requiredValue && !this.env.getOption(MixinEnvironment.Option.IGNORE_REQUIRED)));
        this.initPriority(this.parent.priority, this.parent.mixinPriority);
        if (this.injectorOptions == null) {
            this.injectorOptions = this.parent.injectorOptions;
        }
        else {
            this.injectorOptions.mergeFrom(this.parent.injectorOptions);
        }
        if (this.overwriteOptions == null) {
            this.overwriteOptions = this.parent.overwriteOptions;
        }
        else {
            this.overwriteOptions.mergeFrom(this.parent.overwriteOptions);
        }
        this.setSourceFile |= this.parent.setSourceFile;
        this.verboseLogging |= this.parent.verboseLogging;
        return this.postInit();
    }
    
    private void initPriority(final int defaultPriority, final int defaultMixinPriority) {
        if (this.priority < 0) {
            this.priority = defaultPriority;
        }
        if (this.mixinPriority < 0) {
            this.mixinPriority = defaultMixinPriority;
        }
    }
    
    private boolean postInit() throws MixinInitialisationError {
        if (this.initialised) {
            throw new MixinInitialisationError("Mixin config " + this.name + " was already initialised.");
        }
        this.initialised = true;
        this.initCompatibilityLevel();
        this.initExtensions();
        return this.checkVersion();
    }
    
    private void initCompatibilityLevel() {
        this.compatibilityLevel = MixinEnvironment.getCompatibilityLevel();
        if (this.compatibility == null) {
            return;
        }
        final String strCompatibility = this.compatibility.trim().toUpperCase(Locale.ROOT);
        try {
            this.compatibilityLevel = MixinEnvironment.CompatibilityLevel.valueOf(strCompatibility);
        }
        catch (final IllegalArgumentException ex) {
            throw new MixinInitialisationError(String.format("Mixin config %s specifies compatibility level %s which is not recognised", this.name, strCompatibility));
        }
        final MixinEnvironment.CompatibilityLevel currentLevel = MixinEnvironment.getCompatibilityLevel();
        if (this.compatibilityLevel == currentLevel) {
            return;
        }
        if (currentLevel.isAtLeast(this.compatibilityLevel) && !currentLevel.canSupport(this.compatibilityLevel)) {
            throw new MixinInitialisationError(String.format("Mixin config %s requires compatibility level %s which is too old", this.name, this.compatibilityLevel));
        }
        if (!currentLevel.canElevateTo(this.compatibilityLevel)) {
            throw new MixinInitialisationError(String.format("Mixin config %s requires compatibility level %s which is prohibited by %s", this.name, this.compatibilityLevel, currentLevel));
        }
        final MixinEnvironment.CompatibilityLevel minCompatibilityLevel = MixinEnvironment.getMinCompatibilityLevel();
        if (this.compatibilityLevel.isLessThan(minCompatibilityLevel)) {
            this.logger.log(this.verboseLogging ? Level.INFO : Level.DEBUG, "Compatibility level {} specified by {} is lower than the default level supported by the current mixin service ({}).", this.compatibilityLevel, this, minCompatibilityLevel);
        }
        if (MixinEnvironment.CompatibilityLevel.MAX_SUPPORTED.isLessThan(this.compatibilityLevel)) {
            this.logger.log(this.verboseLogging ? Level.WARN : Level.DEBUG, "Compatibility level {} specified by {} is higher than the maximum level supported by this version of mixin ({}).", this.compatibilityLevel, this, MixinEnvironment.CompatibilityLevel.MAX_SUPPORTED);
        }
        MixinEnvironment.setCompatibilityLevel(this.compatibilityLevel);
    }
    
    void checkCompatibilityLevel(final MixinInfo mixin, final int majorVersion, final int minorVersion) {
        if (majorVersion <= this.compatibilityLevel.getClassMajorVersion()) {
            return;
        }
        final Level logLevel = (this.verboseLogging && majorVersion > this.warnedClassVersion) ? Level.WARN : Level.DEBUG;
        final String message = (majorVersion > MixinEnvironment.CompatibilityLevel.MAX_SUPPORTED.getClassMajorVersion()) ? "the current version of Mixin" : "the declared compatibility level";
        this.warnedClassVersion = majorVersion;
        this.logger.log(logLevel, "{}: Class version {} required is higher than the class version supported by {} ({} supports class version {})", mixin, majorVersion, message, this.compatibilityLevel, this.compatibilityLevel.getClassMajorVersion());
    }
    
    private MixinEnvironment parseSelector(final String target, final MixinEnvironment fallbackEnvironment) {
        if (target != null) {
            final String[] split;
            final String[] selectors = split = target.split("[&\\| ]");
            for (String sel : split) {
                sel = sel.trim();
                final Pattern environmentSelector = Pattern.compile("^@env(?:ironment)?\\(([A-Z]+)\\)$");
                final Matcher environmentSelectorMatcher = environmentSelector.matcher(sel);
                if (environmentSelectorMatcher.matches()) {
                    return MixinEnvironment.getEnvironment(MixinEnvironment.Phase.forName(environmentSelectorMatcher.group(1)));
                }
            }
            final MixinEnvironment.Phase phase = MixinEnvironment.Phase.forName(target);
            if (phase != null) {
                return MixinEnvironment.getEnvironment(phase);
            }
        }
        return fallbackEnvironment;
    }
    
    private void initExtensions() {
        if (this.injectorOptions.injectionPoints != null) {
            for (final String injectionPointClassName : this.injectorOptions.injectionPoints) {
                this.initInjectionPoint(injectionPointClassName, this.injectorOptions.namespace);
            }
        }
        if (this.injectorOptions.dynamicSelectors != null) {
            for (final String dynamicSelectorClassName : this.injectorOptions.dynamicSelectors) {
                this.initDynamicSelector(dynamicSelectorClassName, this.injectorOptions.namespace);
            }
        }
    }
    
    private void initInjectionPoint(final String className, final String namespace) {
        try {
            final Class<?> injectionPointClass = this.findExtensionClass(className, InjectionPoint.class, "injection point");
            if (injectionPointClass != null) {
                try {
                    injectionPointClass.getMethod("find", String.class, InsnList.class, Collection.class);
                }
                catch (final NoSuchMethodException cnfe) {
                    this.logger.error("Unable to register injection point {} for {}, the class is not compatible with this version of Mixin", className, this, cnfe);
                    return;
                }
                InjectionPoint.register((Class<? extends InjectionPoint>)injectionPointClass, namespace);
            }
        }
        catch (final Throwable th) {
            this.logger.catching(th);
        }
    }
    
    private void initDynamicSelector(final String className, final String namespace) {
        try {
            final Class<?> dynamicSelectorClass = this.findExtensionClass(className, ITargetSelectorDynamic.class, "dynamic selector");
            if (dynamicSelectorClass != null) {
                TargetSelector.register((Class<? extends ITargetSelectorDynamic>)dynamicSelectorClass, namespace);
            }
        }
        catch (final Throwable th) {
            this.logger.catching(th);
        }
    }
    
    private Class<?> findExtensionClass(final String className, final Class<?> superType, final String extensionType) {
        Class<?> extensionClass = null;
        try {
            extensionClass = this.service.getClassProvider().findClass(className, true);
        }
        catch (final ClassNotFoundException cnfe) {
            this.logger.error("Unable to register {} {} for {}, the specified class was not found", extensionType, className, this, cnfe);
            return null;
        }
        if (!superType.isAssignableFrom(extensionClass)) {
            this.logger.error("Unable to register {} {} for {}, class is not assignable to {}", extensionType, className, this, superType);
            return null;
        }
        return extensionClass;
    }
    
    private boolean checkVersion() throws MixinInitialisationError {
        if (this.version == null) {
            if (this.parent != null && this.parent.version != null) {
                return true;
            }
            this.logger.error("Mixin config {} does not specify \"minVersion\" property", this.name);
        }
        final VersionNumber minVersion = VersionNumber.parse(this.version);
        final VersionNumber curVersion = VersionNumber.parse(this.env.getVersion());
        if (minVersion.compareTo(curVersion) <= 0) {
            return true;
        }
        this.logger.warn("Mixin config {} requires mixin subsystem version {} but {} was found. The mixin config will not be applied.", this.name, minVersion, curVersion);
        if (this.required) {
            throw new MixinInitialisationError("Required mixin config " + this.name + " requires mixin subsystem version " + minVersion);
        }
        return false;
    }
    
    void addListener(final IListener listener) {
        this.listeners.add(listener);
    }
    
    void onSelect() {
        (this.plugin = new PluginHandle(this, this.service, this.pluginClassName)).onLoad(Strings.nullToEmpty(this.mixinPackage));
        if (Strings.isNullOrEmpty(this.mixinPackage)) {
            return;
        }
        if (!this.mixinPackage.endsWith(".")) {
            this.mixinPackage += ".";
        }
        boolean suppressRefMapWarning = false;
        if (this.refMapperConfig == null) {
            this.refMapperConfig = this.plugin.getRefMapperConfig();
            if (this.refMapperConfig == null) {
                suppressRefMapWarning = true;
                this.refMapperConfig = "mixin.refmap.json";
            }
        }
        this.refMapper = ReferenceMapper.read(this.refMapperConfig);
        if (!suppressRefMapWarning && this.refMapper.isDefault() && !this.env.getOption(MixinEnvironment.Option.DISABLE_REFMAP)) {
            this.logger.warn("Reference map '{}' for {} could not be read. If this is a development environment you can ignore this message", this.refMapperConfig, this);
        }
        if (this.env.getOption(MixinEnvironment.Option.REFMAP_REMAP)) {
            this.refMapper = RemappingReferenceMapper.of(this.env, this.refMapper);
        }
        if (this.refMapperWrapper != null) {
            final String wrapperName = this.mixinPackage + this.refMapperWrapper;
            try {
                final Class<IReferenceMapper> wrapperCls = (Class<IReferenceMapper>)this.service.getClassProvider().findClass(wrapperName, true);
                final Constructor<IReferenceMapper> ctr = wrapperCls.getConstructor(MixinEnvironment.class, IReferenceMapper.class);
                this.refMapper = ctr.newInstance(this.env, this.refMapper);
            }
            catch (final ClassNotFoundException e) {
                this.logger.error("Reference map wrapper '{}' could not be found: ", wrapperName, e);
            }
            catch (final ReflectiveOperationException e2) {
                this.logger.error("Reference map wrapper '{}' could not be created: ", wrapperName, e2);
            }
            catch (final SecurityException e3) {
                this.logger.error("Reference map wrapper '{}' could not be created: ", wrapperName, e3);
            }
        }
    }
    
    void prepare(final Extensions extensions) {
        if (this.prepared) {
            return;
        }
        this.prepared = true;
        this.prepareMixins("mixins", this.mixinClasses, false, extensions);
        switch (this.env.getSide()) {
            case CLIENT: {
                this.prepareMixins("client", this.mixinClassesClient, false, extensions);
                break;
            }
            case SERVER: {
                this.prepareMixins("server", this.mixinClassesServer, false, extensions);
                break;
            }
            default: {
                this.logger.warn("Mixin environment was unable to detect the current side, sided mixins will not be applied", new Object[0]);
                break;
            }
        }
    }
    
    void postInitialise(final Extensions extensions) {
        if (this.plugin != null) {
            final List<String> pluginMixins = this.plugin.getMixins();
            this.prepareMixins("companion plugin", pluginMixins, true, extensions);
        }
        final Iterator<MixinInfo> iter = this.mixins.iterator();
        while (iter.hasNext()) {
            final MixinInfo mixin = iter.next();
            try {
                mixin.validate();
                for (final IListener listener : this.listeners) {
                    listener.onInit(mixin);
                }
            }
            catch (final InvalidMixinException ex) {
                this.logger.error(ex.getMixin() + ": " + ex.getMessage(), ex);
                this.removeMixin(mixin);
                iter.remove();
            }
            catch (final Exception ex2) {
                this.logger.error(ex2.getMessage(), ex2);
                this.removeMixin(mixin);
                iter.remove();
            }
        }
    }
    
    private void removeMixin(final MixinInfo remove) {
        for (final List<MixinInfo> mixinsFor : this.mixinMapping.values()) {
            final Iterator<MixinInfo> iter = mixinsFor.iterator();
            while (iter.hasNext()) {
                if (remove == iter.next()) {
                    iter.remove();
                }
            }
        }
    }
    
    private void prepareMixins(final String collectionName, final List<String> mixinClasses, final boolean ignorePlugin, final Extensions extensions) {
        if (mixinClasses == null) {
            return;
        }
        if (Strings.isNullOrEmpty(this.mixinPackage)) {
            if (mixinClasses.size() > 0) {
                this.logger.error("{} declares mixin classes in {} but does not specify a package, {} orphaned mixins will not be loaded: {}", this, collectionName, mixinClasses.size(), mixinClasses);
            }
            return;
        }
        for (final String mixinClass : mixinClasses) {
            final String fqMixinClass = this.mixinPackage + mixinClass;
            if (mixinClass != null) {
                if (MixinConfig.globalMixinList.contains(fqMixinClass)) {
                    continue;
                }
                MixinInfo mixin = null;
                try {
                    this.pendingMixins.add(mixin = new MixinInfo(this.service, this, mixinClass, this.plugin, ignorePlugin, extensions));
                    MixinConfig.globalMixinList.add(fqMixinClass);
                }
                catch (final InvalidMixinException ex) {
                    if (this.required) {
                        throw ex;
                    }
                    this.logger.error(ex.getMessage(), ex);
                }
                catch (final Exception ex2) {
                    if (this.required) {
                        throw new InvalidMixinException(mixin, "Error initialising mixin " + mixin + " - " + ex2.getClass() + ": " + ex2.getMessage(), ex2);
                    }
                    this.logger.error(ex2.getMessage(), ex2);
                }
            }
        }
        for (final MixinInfo mixin2 : this.pendingMixins) {
            try {
                mixin2.parseTargets();
                if (mixin2.getTargetClasses().size() <= 0) {
                    continue;
                }
                for (final String targetClass : mixin2.getTargetClasses()) {
                    final String targetClassName = targetClass.replace('/', '.');
                    this.mixinsFor(targetClassName).add(mixin2);
                    this.unhandledTargets.add(targetClassName);
                }
                for (final IListener listener : this.listeners) {
                    listener.onPrepare(mixin2);
                }
                this.mixins.add(mixin2);
            }
            catch (final InvalidMixinException ex3) {
                if (this.required) {
                    throw ex3;
                }
                this.logger.error(ex3.getMessage(), ex3);
            }
            catch (final Exception ex4) {
                if (this.required) {
                    throw new InvalidMixinException(mixin2, "Error initialising mixin " + mixin2 + " - " + ex4.getClass() + ": " + ex4.getMessage(), ex4);
                }
                this.logger.error(ex4.getMessage(), ex4);
            }
        }
        this.pendingMixins.clear();
    }
    
    void postApply(final String transformedName, final ClassNode targetClass) {
        this.unhandledTargets.remove(transformedName);
    }
    
    public Config getHandle() {
        if (this.handle == null) {
            this.handle = new Config(this);
        }
        return this.handle;
    }
    
    @Override
    public boolean isRequired() {
        return this.required;
    }
    
    @Override
    public MixinEnvironment getEnvironment() {
        return this.env;
    }
    
    MixinConfig getParent() {
        return this.parent;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getMixinPackage() {
        return Strings.nullToEmpty(this.mixinPackage);
    }
    
    @Override
    public int getPriority() {
        return this.priority;
    }
    
    public int getDefaultMixinPriority() {
        return this.mixinPriority;
    }
    
    public int getDefaultRequiredInjections() {
        return this.injectorOptions.defaultRequireValue;
    }
    
    public String getDefaultInjectorGroup() {
        final String defaultGroup = this.injectorOptions.defaultGroup;
        return (defaultGroup != null && !defaultGroup.isEmpty()) ? defaultGroup : "default";
    }
    
    public boolean conformOverwriteVisibility() {
        return this.overwriteOptions.conformAccessModifiers;
    }
    
    public boolean requireOverwriteAnnotations() {
        return this.overwriteOptions.requireOverwriteAnnotations;
    }
    
    public int getMaxShiftByValue() {
        return Math.min(Math.max(this.injectorOptions.maxShiftBy, 0), 5);
    }
    
    public boolean select(final MixinEnvironment environment) {
        this.visited = true;
        return this.env == environment;
    }
    
    boolean isVisited() {
        return this.visited;
    }
    
    int getDeclaredMixinCount() {
        return getCollectionSize(this.mixinClasses, this.mixinClassesClient, this.mixinClassesServer);
    }
    
    int getMixinCount() {
        return this.mixins.size();
    }
    
    public List<String> getClasses() {
        if (Strings.isNullOrEmpty(this.mixinPackage)) {
            return Collections.emptyList();
        }
        final ImmutableList.Builder<String> list = ImmutableList.builder();
        for (final List<String> classes : new List[] { this.mixinClasses, this.mixinClassesClient, this.mixinClassesServer }) {
            if (classes != null) {
                for (final String className : classes) {
                    list.add(this.mixinPackage + className);
                }
            }
        }
        return list.build();
    }
    
    public boolean shouldSetSourceFile() {
        return this.setSourceFile;
    }
    
    public IReferenceMapper getReferenceMapper() {
        if (this.env.getOption(MixinEnvironment.Option.DISABLE_REFMAP)) {
            return ReferenceMapper.DEFAULT_MAPPER;
        }
        this.refMapper.setContext(this.env.getRefmapObfuscationContext());
        return this.refMapper;
    }
    
    String remapClassName(final String className, final String reference) {
        return this.getReferenceMapper().remap(className, reference);
    }
    
    @Override
    public IMixinConfigPlugin getPlugin() {
        return this.plugin.get();
    }
    
    public Set<String> getTargetsSet() {
        return this.mixinMapping.keySet();
    }
    
    @Override
    public Set<String> getTargets() {
        return Collections.unmodifiableSet((Set<? extends String>)this.mixinMapping.keySet());
    }
    
    public Set<String> getUnhandledTargets() {
        return Collections.unmodifiableSet((Set<? extends String>)this.unhandledTargets);
    }
    
    @Override
    public <V> void decorate(final String key, final V value) {
        if (this.decorations == null) {
            this.decorations = new HashMap<String, Object>();
        }
        if (this.decorations.containsKey(key)) {
            throw new IllegalArgumentException(String.format("Decoration with key '%s' already exists on config %s", key, this));
        }
        this.decorations.put(key, value);
    }
    
    @Override
    public boolean hasDecoration(final String key) {
        return this.decorations != null && this.decorations.get(key) != null;
    }
    
    @Override
    public <V> V getDecoration(final String key) {
        return (V)((this.decorations == null) ? null : this.decorations.get(key));
    }
    
    public Level getLoggingLevel() {
        return this.verboseLogging ? Level.INFO : Level.DEBUG;
    }
    
    public boolean isVerboseLogging() {
        return this.verboseLogging;
    }
    
    public boolean packageMatch(final String className) {
        return !Strings.isNullOrEmpty(this.mixinPackage) && className.startsWith(this.mixinPackage);
    }
    
    public boolean hasMixinsFor(final String targetClass) {
        return this.mixinMapping.containsKey(targetClass);
    }
    
    boolean hasPendingMixinsFor(final String targetClass) {
        if (this.packageMatch(targetClass)) {
            return false;
        }
        for (final MixinInfo pendingMixin : this.pendingMixins) {
            if (pendingMixin.hasDeclaredTarget(targetClass)) {
                return true;
            }
        }
        return false;
    }
    
    public List<MixinInfo> getMixinsFor(final String targetClass) {
        return this.mixinsFor(targetClass);
    }
    
    private List<MixinInfo> mixinsFor(final String targetClass) {
        List<MixinInfo> mixins = this.mixinMapping.get(targetClass);
        if (mixins == null) {
            mixins = new ArrayList<MixinInfo>();
            this.mixinMapping.put(targetClass, mixins);
        }
        return mixins;
    }
    
    public List<String> reloadMixin(final String mixinClass, final ClassNode classNode) {
        for (final MixinInfo mixin : this.mixins) {
            if (mixin.getClassName().equals(mixinClass)) {
                mixin.reloadMixin(classNode);
                return mixin.getTargetClasses();
            }
        }
        return Collections.emptyList();
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public int compareTo(final MixinConfig other) {
        if (other == null) {
            return 0;
        }
        if (other.priority == this.priority) {
            return this.order - other.order;
        }
        return this.priority - other.priority;
    }
    
    static Config create(final String configFile, final MixinEnvironment outer) {
        try {
            final IMixinService service = MixinService.getService();
            final InputStream resource = service.getResourceAsStream(configFile);
            if (resource == null) {
                throw new IllegalArgumentException(String.format("The specified resource '%s' was invalid or could not be read", configFile));
            }
            final MixinConfig config = new Gson().fromJson(new InputStreamReader(resource), MixinConfig.class);
            if (config.onLoad(service, configFile, outer)) {
                return config.getHandle();
            }
            return null;
        }
        catch (final IllegalArgumentException ex) {
            throw ex;
        }
        catch (final Exception ex2) {
            throw new IllegalArgumentException(String.format("The specified resource '%s' was invalid or could not be read", configFile), ex2);
        }
    }
    
    private static int getCollectionSize(final Collection<?>... collections) {
        int total = 0;
        for (final Collection<?> collection : collections) {
            if (collection != null) {
                total += collection.size();
            }
        }
        return total;
    }
    
    static {
        MixinConfig.configOrder = 0;
        globalMixinList = new HashSet<String>();
    }
    
    static class InjectorOptions
    {
        @SerializedName("defaultRequire")
        int defaultRequireValue;
        @SerializedName("defaultGroup")
        String defaultGroup;
        @SerializedName("namespace")
        String namespace;
        @SerializedName("injectionPoints")
        List<String> injectionPoints;
        @SerializedName("dynamicSelectors")
        List<String> dynamicSelectors;
        @SerializedName("maxShiftBy")
        int maxShiftBy;
        
        InjectorOptions() {
            this.defaultRequireValue = 0;
            this.defaultGroup = "default";
            this.maxShiftBy = 0;
        }
        
        void mergeFrom(final InjectorOptions parent) {
            if (this.defaultRequireValue == 0) {
                this.defaultRequireValue = parent.defaultRequireValue;
            }
            if ("default".equals(this.defaultGroup)) {
                this.defaultGroup = parent.defaultGroup;
            }
            if (this.maxShiftBy == 0) {
                this.maxShiftBy = parent.maxShiftBy;
            }
        }
    }
    
    static class OverwriteOptions
    {
        @SerializedName("conformVisibility")
        boolean conformAccessModifiers;
        @SerializedName("requireAnnotations")
        boolean requireOverwriteAnnotations;
        
        void mergeFrom(final OverwriteOptions parent) {
            this.conformAccessModifiers |= parent.conformAccessModifiers;
            this.requireOverwriteAnnotations |= parent.requireOverwriteAnnotations;
        }
    }
    
    interface IListener
    {
        void onPrepare(final MixinInfo p0);
        
        void onInit(final MixinInfo p0);
    }
}
