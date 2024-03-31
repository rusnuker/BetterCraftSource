// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin;

import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.util.IConsumer;
import org.spongepowered.asm.service.MixinServiceAbstract;
import org.spongepowered.asm.util.perf.Profiler;
import org.spongepowered.asm.service.ITransformerProvider;
import org.spongepowered.asm.service.ITransformer;
import java.util.Iterator;
import java.util.Collections;
import org.spongepowered.asm.mixin.extensibility.IEnvironmentTokenProvider;
import org.spongepowered.asm.logging.Level;
import org.spongepowered.asm.util.asm.ASM;
import org.spongepowered.asm.util.JavaVersion;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.asm.mixin.throwables.MixinException;
import java.util.Locale;
import org.spongepowered.asm.service.MixinService;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import org.spongepowered.asm.obfuscation.RemapperChain;
import java.util.Map;
import java.util.List;
import java.util.Set;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.util.ITokenProvider;

public final class MixinEnvironment implements ITokenProvider
{
    private static MixinEnvironment currentEnvironment;
    private static Phase currentPhase;
    private static CompatibilityLevel compatibility;
    private static boolean showHeader;
    private static final ILogger logger;
    private static IMixinTransformer transformer;
    private final IMixinService service;
    private final Phase phase;
    private final GlobalProperties.Keys configsKey;
    private final boolean[] options;
    private final Set<String> tokenProviderClasses;
    private final List<TokenProviderWrapper> tokenProviders;
    private final Map<String, Integer> internalTokens;
    private final RemapperChain remappers;
    private Side side;
    private String obfuscationContext;
    
    MixinEnvironment(final Phase phase) {
        this.tokenProviderClasses = new HashSet<String>();
        this.tokenProviders = new ArrayList<TokenProviderWrapper>();
        this.internalTokens = new HashMap<String, Integer>();
        this.remappers = new RemapperChain();
        this.obfuscationContext = null;
        this.service = MixinService.getService();
        this.phase = phase;
        this.configsKey = GlobalProperties.Keys.of(GlobalProperties.Keys.CONFIGS + "." + this.phase.name.toLowerCase(Locale.ROOT));
        final Object version = this.getVersion();
        if (version == null || !"0.8.5".equals(version)) {
            throw new MixinException("Environment conflict, mismatched versions or you didn't call MixinBootstrap.init()");
        }
        this.service.checkEnv(this);
        this.options = new boolean[Option.values().length];
        for (final Option option : Option.values()) {
            this.options[option.ordinal()] = option.getBooleanValue();
        }
        if (MixinEnvironment.showHeader) {
            MixinEnvironment.showHeader = false;
            this.printHeader(version);
        }
    }
    
    private void printHeader(final Object version) {
        final String codeSource = this.getCodeSource();
        final String serviceName = this.service.getName();
        final Side side = this.getSide();
        MixinEnvironment.logger.info("SpongePowered MIXIN Subsystem Version={} Source={} Service={} Env={}", version, codeSource, serviceName, side);
        final boolean verbose = this.getOption(Option.DEBUG_VERBOSE);
        if (verbose || this.getOption(Option.DEBUG_EXPORT) || this.getOption(Option.DEBUG_PROFILER)) {
            final PrettyPrinter printer = new PrettyPrinter(32);
            printer.add("SpongePowered MIXIN%s", verbose ? " (Verbose debugging enabled)" : "").centre().hr();
            printer.kv("Code source", (Object)codeSource);
            printer.kv("Internal Version", version);
            printer.kv("Java Version", "%s (supports compatibility %s)", JavaVersion.current(), CompatibilityLevel.getSupportedVersions());
            printer.kv("Default Compatibility Level", getCompatibilityLevel());
            printer.kv("Detected ASM Version", (Object)ASM.getVersionString());
            printer.kv("Detected ASM Supports Java", (Object)ASM.getClassVersionString()).hr();
            printer.kv("Service Name", (Object)serviceName);
            printer.kv("Mixin Service Class", (Object)this.service.getClass().getName());
            printer.kv("Global Property Service Class", (Object)MixinService.getGlobalPropertyService().getClass().getName());
            printer.kv("Logger Adapter Type", (Object)MixinService.getService().getLogger("mixin").getType()).hr();
            for (final Option option : Option.values()) {
                final StringBuilder indent = new StringBuilder();
                for (int i = 0; i < option.depth; ++i) {
                    indent.append("- ");
                }
                printer.kv(option.property, "%s<%s>", indent, option);
            }
            printer.hr().kv("Detected Side", side);
            printer.print(System.err);
        }
    }
    
    private String getCodeSource() {
        try {
            return this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
        }
        catch (final Throwable th) {
            return "Unknown";
        }
    }
    
    private Level getVerboseLoggingLevel() {
        return this.getOption(Option.DEBUG_VERBOSE) ? Level.INFO : Level.DEBUG;
    }
    
    public Phase getPhase() {
        return this.phase;
    }
    
    @Deprecated
    public List<String> getMixinConfigs() {
        List<String> mixinConfigs = GlobalProperties.get(this.configsKey);
        if (mixinConfigs == null) {
            mixinConfigs = new ArrayList<String>();
            GlobalProperties.put(this.configsKey, mixinConfigs);
        }
        return mixinConfigs;
    }
    
    @Deprecated
    public MixinEnvironment addConfiguration(final String config) {
        MixinEnvironment.logger.warn("MixinEnvironment::addConfiguration is deprecated and will be removed. Use Mixins::addConfiguration instead!", new Object[0]);
        Mixins.addConfiguration(config, this);
        return this;
    }
    
    void registerConfig(final String config) {
        final List<String> configs = this.getMixinConfigs();
        if (!configs.contains(config)) {
            configs.add(config);
        }
    }
    
    public MixinEnvironment registerTokenProviderClass(final String providerName) {
        if (!this.tokenProviderClasses.contains(providerName)) {
            try {
                final Class<? extends IEnvironmentTokenProvider> providerClass = (Class<? extends IEnvironmentTokenProvider>)this.service.getClassProvider().findClass(providerName, true);
                final IEnvironmentTokenProvider provider = (IEnvironmentTokenProvider)providerClass.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                this.registerTokenProvider(provider);
            }
            catch (final Throwable th) {
                MixinEnvironment.logger.error("Error instantiating " + providerName, th);
            }
        }
        return this;
    }
    
    public MixinEnvironment registerTokenProvider(final IEnvironmentTokenProvider provider) {
        if (provider != null && !this.tokenProviderClasses.contains(provider.getClass().getName())) {
            final String providerName = provider.getClass().getName();
            final TokenProviderWrapper wrapper = new TokenProviderWrapper(provider, this);
            MixinEnvironment.logger.log(this.getVerboseLoggingLevel(), "Adding new token provider {} to {}", providerName, this);
            this.tokenProviders.add(wrapper);
            this.tokenProviderClasses.add(providerName);
            Collections.sort(this.tokenProviders);
        }
        return this;
    }
    
    @Override
    public Integer getToken(String token) {
        token = token.toUpperCase(Locale.ROOT);
        for (final TokenProviderWrapper provider : this.tokenProviders) {
            final Integer value = provider.getToken(token);
            if (value != null) {
                return value;
            }
        }
        return this.internalTokens.get(token);
    }
    
    @Deprecated
    public Set<String> getErrorHandlerClasses() {
        return Mixins.getErrorHandlerClasses();
    }
    
    public Object getActiveTransformer() {
        return MixinEnvironment.transformer;
    }
    
    public void setActiveTransformer(final IMixinTransformer transformer) {
        if (transformer != null) {
            MixinEnvironment.transformer = transformer;
        }
    }
    
    public MixinEnvironment setSide(final Side side) {
        if (side != null && this.getSide() == Side.UNKNOWN && side != Side.UNKNOWN) {
            this.side = side;
        }
        return this;
    }
    
    public Side getSide() {
        if (this.side == null) {
            for (final Side side : Side.values()) {
                if (side.detect()) {
                    this.side = side;
                    break;
                }
            }
        }
        return (this.side != null) ? this.side : Side.UNKNOWN;
    }
    
    public String getVersion() {
        return GlobalProperties.get(GlobalProperties.Keys.INIT);
    }
    
    public boolean getOption(final Option option) {
        return this.options[option.ordinal()];
    }
    
    public void setOption(final Option option, final boolean value) {
        this.options[option.ordinal()] = value;
    }
    
    public String getOptionValue(final Option option) {
        return option.getStringValue();
    }
    
    public <E extends Enum<E>> E getOption(final Option option, final E defaultValue) {
        return option.getEnumValue(defaultValue);
    }
    
    public void setObfuscationContext(final String context) {
        this.obfuscationContext = context;
    }
    
    public String getObfuscationContext() {
        return this.obfuscationContext;
    }
    
    public String getRefmapObfuscationContext() {
        final String overrideObfuscationType = Option.OBFUSCATION_TYPE.getStringValue();
        if (overrideObfuscationType != null) {
            return overrideObfuscationType;
        }
        return this.obfuscationContext;
    }
    
    public RemapperChain getRemappers() {
        return this.remappers;
    }
    
    public void audit() {
        final Object activeTransformer = this.getActiveTransformer();
        if (activeTransformer instanceof IMixinTransformer) {
            ((IMixinTransformer)activeTransformer).audit(this);
        }
    }
    
    @Deprecated
    public List<ITransformer> getTransformers() {
        MixinEnvironment.logger.warn("MixinEnvironment::getTransformers is deprecated!", new Object[0]);
        final ITransformerProvider transformers = this.service.getTransformerProvider();
        return (transformers != null) ? ((List)transformers.getTransformers()) : Collections.emptyList();
    }
    
    @Deprecated
    public void addTransformerExclusion(final String name) {
        MixinEnvironment.logger.warn("MixinEnvironment::addTransformerExclusion is deprecated!", new Object[0]);
        final ITransformerProvider transformers = this.service.getTransformerProvider();
        if (transformers != null) {
            transformers.addTransformerExclusion(name);
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s[%s]", this.getClass().getSimpleName(), this.phase);
    }
    
    private static Phase getCurrentPhase() {
        if (MixinEnvironment.currentPhase == Phase.NOT_INITIALISED) {
            init(Phase.PREINIT);
        }
        return MixinEnvironment.currentPhase;
    }
    
    public static void init(final Phase phase) {
        if (MixinEnvironment.currentPhase == Phase.NOT_INITIALISED) {
            MixinEnvironment.currentPhase = phase;
            final MixinEnvironment env = getEnvironment(phase);
            Profiler.setActive(env.getOption(Option.DEBUG_PROFILER));
            final IMixinService service = MixinService.getService();
            if (service instanceof MixinServiceAbstract) {
                ((MixinServiceAbstract)service).wire(phase, new PhaseConsumer());
            }
        }
    }
    
    public static MixinEnvironment getEnvironment(final Phase phase) {
        if (phase == null) {
            return Phase.DEFAULT.getEnvironment();
        }
        return phase.getEnvironment();
    }
    
    public static MixinEnvironment getDefaultEnvironment() {
        return getEnvironment(Phase.DEFAULT);
    }
    
    public static MixinEnvironment getCurrentEnvironment() {
        if (MixinEnvironment.currentEnvironment == null) {
            MixinEnvironment.currentEnvironment = getEnvironment(getCurrentPhase());
        }
        return MixinEnvironment.currentEnvironment;
    }
    
    public static CompatibilityLevel getCompatibilityLevel() {
        if (MixinEnvironment.compatibility == null) {
            final CompatibilityLevel minLevel = getMinCompatibilityLevel();
            final CompatibilityLevel optionLevel = Option.DEFAULT_COMPATIBILITY_LEVEL.getEnumValue(minLevel);
            MixinEnvironment.compatibility = (optionLevel.isAtLeast(minLevel) ? optionLevel : minLevel);
        }
        return MixinEnvironment.compatibility;
    }
    
    public static CompatibilityLevel getMinCompatibilityLevel() {
        final CompatibilityLevel minLevel = MixinService.getService().getMinCompatibilityLevel();
        return (minLevel == null) ? CompatibilityLevel.DEFAULT : minLevel;
    }
    
    @Deprecated
    public static void setCompatibilityLevel(final CompatibilityLevel level) throws IllegalArgumentException {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (!"org.spongepowered.asm.mixin.transformer.MixinConfig".equals(stackTrace[2].getClassName())) {
            MixinEnvironment.logger.warn("MixinEnvironment::setCompatibilityLevel is deprecated and will be removed. Set level via config instead!", new Object[0]);
        }
        final CompatibilityLevel currentLevel = getCompatibilityLevel();
        if (level != currentLevel && level.isAtLeast(currentLevel)) {
            if (!level.isSupported()) {
                throw new IllegalArgumentException(String.format("The requested compatibility level %s could not be set. Level is not supported by the active JRE or ASM version (Java %s, %s)", level, JavaVersion.current(), ASM.getVersionString()));
            }
            final IMixinService service = MixinService.getService();
            final CompatibilityLevel maxLevel = service.getMaxCompatibilityLevel();
            if (maxLevel != null && maxLevel.isLessThan(level)) {
                MixinEnvironment.logger.warn("The requested compatibility level {} is higher than the level supported by the active subsystem '{}' which supports {}. This is not a supported configuration and instability may occur.", level, service.getName(), maxLevel);
            }
            MixinEnvironment.compatibility = level;
            MixinEnvironment.logger.info("Compatibility level set to {}", level);
        }
    }
    
    @Deprecated
    public static Profiler getProfiler() {
        return Profiler.getProfiler("mixin");
    }
    
    static void gotoPhase(final Phase phase) {
        if (phase == null || phase.ordinal < 0) {
            throw new IllegalArgumentException("Cannot go to the specified phase, phase is null or invalid");
        }
        final IMixinService service = MixinService.getService();
        if (phase.ordinal > getCurrentPhase().ordinal) {
            service.beginPhase();
        }
        MixinEnvironment.currentPhase = phase;
        MixinEnvironment.currentEnvironment = getEnvironment(getCurrentPhase());
        if (service instanceof MixinServiceAbstract && phase == Phase.DEFAULT) {
            ((MixinServiceAbstract)service).unwire();
        }
    }
    
    static {
        MixinEnvironment.currentPhase = Phase.NOT_INITIALISED;
        MixinEnvironment.showHeader = true;
        logger = MixinService.getService().getLogger("mixin");
    }
    
    public static final class Phase
    {
        static final Phase NOT_INITIALISED;
        public static final Phase PREINIT;
        public static final Phase INIT;
        public static final Phase DEFAULT;
        static final List<Phase> phases;
        final int ordinal;
        final String name;
        private MixinEnvironment environment;
        
        private Phase(final int ordinal, final String name) {
            this.ordinal = ordinal;
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static Phase forName(final String name) {
            for (final Phase phase : Phase.phases) {
                if (phase.name.equals(name)) {
                    return phase;
                }
            }
            return null;
        }
        
        MixinEnvironment getEnvironment() {
            if (this.ordinal < 0) {
                throw new IllegalArgumentException("Cannot access the NOT_INITIALISED environment");
            }
            if (this.environment == null) {
                this.environment = new MixinEnvironment(this);
            }
            return this.environment;
        }
        
        static {
            NOT_INITIALISED = new Phase(-1, "NOT_INITIALISED");
            PREINIT = new Phase(0, "PREINIT");
            INIT = new Phase(1, "INIT");
            DEFAULT = new Phase(2, "DEFAULT");
            phases = ImmutableList.of(Phase.PREINIT, Phase.INIT, Phase.DEFAULT);
        }
    }
    
    public enum Side
    {
        UNKNOWN {
            @Override
            protected boolean detect() {
                return false;
            }
        }, 
        CLIENT {
            @Override
            protected boolean detect() {
                final String sideName = MixinService.getService().getSideName();
                return "CLIENT".equals(sideName);
            }
        }, 
        SERVER {
            @Override
            protected boolean detect() {
                final String sideName = MixinService.getService().getSideName();
                return "SERVER".equals(sideName) || "DEDICATEDSERVER".equals(sideName);
            }
        };
        
        protected abstract boolean detect();
    }
    
    public enum Option
    {
        DEBUG_ALL("debug"), 
        DEBUG_EXPORT(Option.DEBUG_ALL, "export"), 
        DEBUG_EXPORT_FILTER(Option.DEBUG_EXPORT, "filter", false), 
        DEBUG_EXPORT_DECOMPILE(Option.DEBUG_EXPORT, Inherit.ALLOW_OVERRIDE, "decompile"), 
        DEBUG_EXPORT_DECOMPILE_THREADED(Option.DEBUG_EXPORT_DECOMPILE, Inherit.ALLOW_OVERRIDE, "async"), 
        DEBUG_EXPORT_DECOMPILE_MERGESIGNATURES(Option.DEBUG_EXPORT_DECOMPILE, Inherit.ALLOW_OVERRIDE, "mergeGenericSignatures"), 
        DEBUG_VERIFY(Option.DEBUG_ALL, "verify"), 
        DEBUG_VERBOSE(Option.DEBUG_ALL, "verbose"), 
        DEBUG_INJECTORS(Option.DEBUG_ALL, "countInjections"), 
        DEBUG_STRICT(Option.DEBUG_ALL, Inherit.INDEPENDENT, "strict"), 
        DEBUG_UNIQUE(Option.DEBUG_STRICT, "unique"), 
        DEBUG_TARGETS(Option.DEBUG_STRICT, "targets"), 
        DEBUG_PROFILER(Option.DEBUG_ALL, Inherit.ALLOW_OVERRIDE, "profiler"), 
        DUMP_TARGET_ON_FAILURE("dumpTargetOnFailure"), 
        CHECK_ALL("checks"), 
        CHECK_IMPLEMENTS(Option.CHECK_ALL, "interfaces"), 
        CHECK_IMPLEMENTS_STRICT(Option.CHECK_IMPLEMENTS, Inherit.ALLOW_OVERRIDE, "strict"), 
        IGNORE_CONSTRAINTS("ignoreConstraints"), 
        HOT_SWAP("hotSwap"), 
        ENVIRONMENT(Inherit.ALWAYS_FALSE, "env"), 
        OBFUSCATION_TYPE(Option.ENVIRONMENT, Inherit.ALWAYS_FALSE, "obf"), 
        DISABLE_REFMAP(Option.ENVIRONMENT, Inherit.INDEPENDENT, "disableRefMap"), 
        REFMAP_REMAP(Option.ENVIRONMENT, Inherit.INDEPENDENT, "remapRefMap"), 
        REFMAP_REMAP_RESOURCE(Option.ENVIRONMENT, Inherit.INDEPENDENT, "refMapRemappingFile", ""), 
        REFMAP_REMAP_SOURCE_ENV(Option.ENVIRONMENT, Inherit.INDEPENDENT, "refMapRemappingEnv", "searge"), 
        REFMAP_REMAP_ALLOW_PERMISSIVE(Option.ENVIRONMENT, Inherit.INDEPENDENT, "allowPermissiveMatch", true, "true"), 
        IGNORE_REQUIRED(Option.ENVIRONMENT, Inherit.INDEPENDENT, "ignoreRequired"), 
        DEFAULT_COMPATIBILITY_LEVEL(Option.ENVIRONMENT, Inherit.INDEPENDENT, "compatLevel"), 
        SHIFT_BY_VIOLATION_BEHAVIOUR(Option.ENVIRONMENT, Inherit.INDEPENDENT, "shiftByViolation", "warn"), 
        INITIALISER_INJECTION_MODE("initialiserInjectionMode", "default");
        
        private static final String PREFIX = "mixin";
        final Option parent;
        final Inherit inheritance;
        final String property;
        final String defaultValue;
        final boolean isFlag;
        final int depth;
        
        private Option(final String property) {
            this(null, property, true);
        }
        
        private Option(final Inherit inheritance, final String property) {
            this(null, inheritance, property, true);
        }
        
        private Option(final String property, final boolean flag) {
            this(null, property, flag);
        }
        
        private Option(final String property, final String defaultStringValue) {
            this(null, Inherit.INDEPENDENT, property, false, defaultStringValue);
        }
        
        private Option(final Option parent, final String property) {
            this(parent, Inherit.INHERIT, property, true);
        }
        
        private Option(final Option parent, final Inherit inheritance, final String property) {
            this(parent, inheritance, property, true);
        }
        
        private Option(final Option parent, final String property, final boolean isFlag) {
            this(parent, Inherit.INHERIT, property, isFlag, null);
        }
        
        private Option(final Option parent, final Inherit inheritance, final String property, final boolean isFlag) {
            this(parent, inheritance, property, isFlag, null);
        }
        
        private Option(final Option parent, final String property, final String defaultStringValue) {
            this(parent, Inherit.INHERIT, property, false, defaultStringValue);
        }
        
        private Option(final Option parent, final Inherit inheritance, final String property, final String defaultStringValue) {
            this(parent, inheritance, property, false, defaultStringValue);
        }
        
        private Option(Option parent, final Inherit inheritance, final String property, final boolean isFlag, final String defaultStringValue) {
            this.parent = parent;
            this.inheritance = inheritance;
            this.property = ((parent != null) ? parent.property : "mixin") + "." + property;
            this.defaultValue = defaultStringValue;
            this.isFlag = isFlag;
            int depth;
            for (depth = 0; parent != null; parent = parent.parent, ++depth) {}
            this.depth = depth;
        }
        
        Option getParent() {
            return this.parent;
        }
        
        String getProperty() {
            return this.property;
        }
        
        @Override
        public String toString() {
            return this.isFlag ? String.valueOf(this.getBooleanValue()) : this.getStringValue();
        }
        
        private boolean getLocalBooleanValue(final boolean defaultValue) {
            return Boolean.parseBoolean(System.getProperty(this.property, Boolean.toString(defaultValue)));
        }
        
        private boolean getInheritedBooleanValue() {
            return this.parent != null && this.parent.getBooleanValue();
        }
        
        final boolean getBooleanValue() {
            if (this.inheritance == Inherit.ALWAYS_FALSE) {
                return false;
            }
            final boolean local = this.getLocalBooleanValue(false);
            if (this.inheritance == Inherit.INDEPENDENT) {
                return local;
            }
            final boolean inherited = local || this.getInheritedBooleanValue();
            return (this.inheritance == Inherit.INHERIT) ? inherited : this.getLocalBooleanValue(inherited);
        }
        
        final String getStringValue() {
            return (this.inheritance == Inherit.INDEPENDENT || this.parent == null || this.parent.getBooleanValue()) ? System.getProperty(this.property, this.defaultValue) : this.defaultValue;
        }
        
         <E extends Enum<E>> E getEnumValue(final E defaultValue) {
            final String value = System.getProperty(this.property, defaultValue.name());
            try {
                return Enum.valueOf(defaultValue.getClass(), value.toUpperCase(Locale.ROOT));
            }
            catch (final IllegalArgumentException ex) {
                return defaultValue;
            }
        }
        
        private enum Inherit
        {
            INHERIT, 
            ALLOW_OVERRIDE, 
            INDEPENDENT, 
            ALWAYS_FALSE;
        }
    }
    
    public enum CompatibilityLevel
    {
        JAVA_6(6, 50, 0), 
        JAVA_7(7, 51, 0) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 1.7;
            }
        }, 
        JAVA_8(8, 52, 3) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 1.8;
            }
        }, 
        JAVA_9(9, 53, 7) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 9.0 && ASM.isAtLeastVersion(6);
            }
        }, 
        JAVA_10(10, 54, 7) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 10.0 && ASM.isAtLeastVersion(6, 1);
            }
        }, 
        JAVA_11(11, 55, 31) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 11.0 && ASM.isAtLeastVersion(7);
            }
        }, 
        JAVA_12(12, 56, 31) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 12.0 && ASM.isAtLeastVersion(7);
            }
        }, 
        JAVA_13(13, 57, 31) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 13.0 && ASM.isAtLeastVersion(7);
            }
        }, 
        JAVA_14(14, 58, 63) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 14.0 && ASM.isAtLeastVersion(8);
            }
        }, 
        JAVA_15(15, 59, 127) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 15.0 && ASM.isAtLeastVersion(9);
            }
        }, 
        JAVA_16(16, 60, 127) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 16.0 && ASM.isAtLeastVersion(9);
            }
        }, 
        JAVA_17(17, 61, 127) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 17.0 && ASM.isAtLeastVersion(9, 1);
            }
        }, 
        JAVA_18(18, 62, 127) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 18.0 && ASM.isAtLeastVersion(9, 2);
            }
        };
        
        public static CompatibilityLevel DEFAULT;
        public static CompatibilityLevel MAX_SUPPORTED;
        private final int ver;
        private final int classVersion;
        private final int languageFeatures;
        private CompatibilityLevel maxCompatibleLevel;
        
        private CompatibilityLevel(final int ver, final int classVersion, final int languageFeatures) {
            this.ver = ver;
            this.classVersion = classVersion;
            this.languageFeatures = languageFeatures;
        }
        
        boolean isSupported() {
            return true;
        }
        
        @Deprecated
        public int classVersion() {
            return this.classVersion;
        }
        
        public int getClassVersion() {
            return this.classVersion;
        }
        
        public int getClassMajorVersion() {
            return this.classVersion & 0xFFFF;
        }
        
        public int getLanguageFeatures() {
            return this.languageFeatures;
        }
        
        @Deprecated
        public boolean supportsMethodsInInterfaces() {
            return (this.languageFeatures & 0x1) != 0x0;
        }
        
        public boolean supports(final int languageFeatures) {
            return (this.languageFeatures & languageFeatures) == languageFeatures;
        }
        
        public boolean isAtLeast(final CompatibilityLevel level) {
            return level == null || this.ver >= level.ver;
        }
        
        public boolean isLessThan(final CompatibilityLevel level) {
            return level == null || this.ver < level.ver;
        }
        
        public boolean canElevateTo(final CompatibilityLevel level) {
            return level == null || this.maxCompatibleLevel == null || level.ver <= this.maxCompatibleLevel.ver;
        }
        
        public boolean canSupport(final CompatibilityLevel level) {
            return level == null || level.canElevateTo(this);
        }
        
        public static CompatibilityLevel requiredFor(final int languageFeatures) {
            for (final CompatibilityLevel level : values()) {
                if (level.supports(languageFeatures)) {
                    return level;
                }
            }
            return null;
        }
        
        static String getSupportedVersions() {
            final StringBuilder sb = new StringBuilder();
            boolean comma = false;
            int rangeStart = 0;
            int rangeEnd = 0;
            for (final CompatibilityLevel level : values()) {
                if (level.isSupported()) {
                    if (level.ver == rangeEnd + 1) {
                        rangeEnd = level.ver;
                    }
                    else {
                        if (rangeStart > 0) {
                            sb.append(comma ? "," : "").append(rangeStart);
                            if (rangeEnd > rangeStart) {
                                sb.append((rangeEnd > rangeStart + 1) ? '-' : ',').append(rangeEnd);
                            }
                            comma = true;
                            rangeEnd = (rangeStart = level.ver);
                        }
                        rangeEnd = (rangeStart = level.ver);
                    }
                }
            }
            if (rangeStart > 0) {
                sb.append(comma ? "," : "").append(rangeStart);
                if (rangeEnd > rangeStart) {
                    sb.append((rangeEnd > rangeStart + 1) ? '-' : ',').append(rangeEnd);
                }
            }
            return sb.toString();
        }
        
        static {
            CompatibilityLevel.DEFAULT = CompatibilityLevel.JAVA_6;
            CompatibilityLevel.MAX_SUPPORTED = CompatibilityLevel.JAVA_13;
        }
    }
    
    static class TokenProviderWrapper implements Comparable<TokenProviderWrapper>
    {
        private static int nextOrder;
        private final int priority;
        private final int order;
        private final IEnvironmentTokenProvider provider;
        private final MixinEnvironment environment;
        
        public TokenProviderWrapper(final IEnvironmentTokenProvider provider, final MixinEnvironment environment) {
            this.provider = provider;
            this.environment = environment;
            this.order = TokenProviderWrapper.nextOrder++;
            this.priority = provider.getPriority();
        }
        
        @Override
        public int compareTo(final TokenProviderWrapper other) {
            if (other == null) {
                return 0;
            }
            if (other.priority == this.priority) {
                return other.order - this.order;
            }
            return other.priority - this.priority;
        }
        
        public IEnvironmentTokenProvider getProvider() {
            return this.provider;
        }
        
        Integer getToken(final String token) {
            return this.provider.getToken(token, this.environment);
        }
        
        static {
            TokenProviderWrapper.nextOrder = 0;
        }
    }
    
    static class PhaseConsumer implements IConsumer<Phase>
    {
        @Override
        public void accept(final Phase phase) {
            MixinEnvironment.gotoPhase(phase);
        }
    }
}
