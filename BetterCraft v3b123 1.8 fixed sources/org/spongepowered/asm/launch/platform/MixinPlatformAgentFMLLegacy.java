// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform;

import org.apache.logging.log4j.core.LogEvent;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import java.util.HashSet;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.util.IConsumer;
import java.util.Collection;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IRemapper;
import java.util.Collections;
import java.lang.reflect.Field;
import java.util.Iterator;
import org.spongepowered.asm.service.mojang.MixinServiceLaunchWrapper;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.lang.reflect.Method;
import java.util.List;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.launch.platform.container.ContainerHandleURI;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import net.minecraft.launchwrapper.ITweaker;
import java.io.File;
import java.util.Set;

public class MixinPlatformAgentFMLLegacy extends MixinPlatformAgentAbstract implements IMixinPlatformServiceAgent
{
    private static final String OLD_LAUNCH_HANDLER_CLASS = "cpw.mods.fml.relauncher.FMLLaunchHandler";
    private static final String NEW_LAUNCH_HANDLER_CLASS = "net.minecraftforge.fml.relauncher.FMLLaunchHandler";
    private static final String CLIENT_TWEAKER_TAIL = ".common.launcher.FMLTweaker";
    private static final String SERVER_TWEAKER_TAIL = ".common.launcher.FMLServerTweaker";
    private static final String GETSIDE_METHOD = "side";
    private static final String LOAD_CORE_MOD_METHOD = "loadCoreMod";
    private static final String GET_REPARSEABLE_COREMODS_METHOD = "getReparseableCoremods";
    private static final String CORE_MOD_MANAGER_CLASS = "net.minecraftforge.fml.relauncher.CoreModManager";
    private static final String CORE_MOD_MANAGER_CLASS_LEGACY = "cpw.mods.fml.relauncher.CoreModManager";
    private static final String GET_IGNORED_MODS_METHOD = "getIgnoredMods";
    private static final String GET_IGNORED_MODS_METHOD_LEGACY = "getLoadedCoremods";
    private static final String FML_REMAPPER_ADAPTER_CLASS = "org.spongepowered.asm.bridge.RemapperAdapterFML";
    private static final String FML_CMDLINE_COREMODS = "fml.coreMods.load";
    private static final String FML_PLUGIN_WRAPPER_CLASS = "FMLPluginWrapper";
    private static final String FML_CORE_MOD_INSTANCE_FIELD = "coreModInstance";
    private static final String MFATT_FORCELOADASMOD = "ForceLoadAsMod";
    private static final String MFATT_FMLCOREPLUGIN = "FMLCorePlugin";
    private static final String MFATT_COREMODCONTAINSMOD = "FMLCorePluginContainsFMLMod";
    private static final String FML_TWEAKER_DEOBF = "FMLDeobfTweaker";
    private static final String FML_TWEAKER_INJECTION = "FMLInjectionAndSortingTweaker";
    private static final String FML_TWEAKER_TERMINAL = "TerminalTweaker";
    private static final Set<String> loadedCoreMods;
    private File file;
    private String fileName;
    private ITweaker coreModWrapper;
    private Class<?> clCoreModManager;
    private boolean initInjectionState;
    static MixinAppender appender;
    static Logger log;
    static Level oldLevel;
    
    @Override
    public IMixinPlatformAgent.AcceptResult accept(final MixinPlatformManager manager, final IContainerHandle handle) {
        if (this.getCoreModManagerClass() == null) {
            return IMixinPlatformAgent.AcceptResult.INVALID;
        }
        if (!(handle instanceof ContainerHandleURI) || super.accept(manager, handle) != IMixinPlatformAgent.AcceptResult.ACCEPTED) {
            return IMixinPlatformAgent.AcceptResult.REJECTED;
        }
        this.file = ((ContainerHandleURI)handle).getFile();
        this.fileName = this.file.getName();
        this.coreModWrapper = this.initFMLCoreMod();
        return (this.coreModWrapper != null) ? IMixinPlatformAgent.AcceptResult.ACCEPTED : IMixinPlatformAgent.AcceptResult.REJECTED;
    }
    
    private ITweaker initFMLCoreMod() {
        try {
            if ("true".equalsIgnoreCase(this.handle.getAttribute("ForceLoadAsMod"))) {
                MixinPlatformAgentAbstract.logger.debug("ForceLoadAsMod was specified for {}, attempting force-load", this.fileName);
                this.loadAsMod();
            }
            return this.injectCorePlugin();
        }
        catch (final Exception ex) {
            MixinPlatformAgentAbstract.logger.catching(ex);
            return null;
        }
    }
    
    private void loadAsMod() {
        try {
            getIgnoredMods(this.clCoreModManager).remove(this.fileName);
        }
        catch (final Exception ex) {
            MixinPlatformAgentAbstract.logger.catching(ex);
        }
        if (this.handle.getAttribute("FMLCorePluginContainsFMLMod") != null) {
            if (this.isIgnoredReparseable()) {
                MixinPlatformAgentAbstract.logger.debug("Ignoring request to add {} to reparseable coremod collection - it is a deobfuscated dependency", this.fileName);
                return;
            }
            this.addReparseableJar();
        }
    }
    
    private boolean isIgnoredReparseable() {
        return this.handle.toString().contains("deobfedDeps");
    }
    
    private void addReparseableJar() {
        try {
            final Method mdGetReparsedCoremods = this.clCoreModManager.getDeclaredMethod(GlobalProperties.getString(GlobalProperties.Keys.FML_GET_REPARSEABLE_COREMODS, "getReparseableCoremods"), (Class<?>[])new Class[0]);
            final List<String> reparsedCoremods = (List<String>)mdGetReparsedCoremods.invoke(null, new Object[0]);
            if (!reparsedCoremods.contains(this.fileName)) {
                MixinPlatformAgentAbstract.logger.debug("Adding {} to reparseable coremod collection", this.fileName);
                reparsedCoremods.add(this.fileName);
            }
        }
        catch (final Exception ex) {
            MixinPlatformAgentAbstract.logger.catching(ex);
        }
    }
    
    private ITweaker injectCorePlugin() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final String coreModName = this.handle.getAttribute("FMLCorePlugin");
        if (coreModName == null) {
            return null;
        }
        if (this.isAlreadyInjected(coreModName)) {
            MixinPlatformAgentAbstract.logger.debug("{} has core plugin {}. Skipping because it was already injected.", this.fileName, coreModName);
            return null;
        }
        MixinPlatformAgentAbstract.logger.debug("{} has core plugin {}. Injecting it into FML for co-initialisation:", this.fileName, coreModName);
        final Method mdLoadCoreMod = this.clCoreModManager.getDeclaredMethod(GlobalProperties.getString(GlobalProperties.Keys.FML_LOAD_CORE_MOD, "loadCoreMod"), LaunchClassLoader.class, String.class, File.class);
        mdLoadCoreMod.setAccessible(true);
        final ITweaker wrapper = (ITweaker)mdLoadCoreMod.invoke(null, Launch.classLoader, coreModName, this.file);
        if (wrapper == null) {
            MixinPlatformAgentAbstract.logger.debug("Core plugin {} could not be loaded.", coreModName);
            return null;
        }
        this.initInjectionState = isTweakerQueued("FMLInjectionAndSortingTweaker");
        MixinPlatformAgentFMLLegacy.loadedCoreMods.add(coreModName);
        return wrapper;
    }
    
    private boolean isAlreadyInjected(final String coreModName) {
        if (MixinPlatformAgentFMLLegacy.loadedCoreMods.contains(coreModName)) {
            return true;
        }
        try {
            final List<ITweaker> tweakers = GlobalProperties.get(MixinServiceLaunchWrapper.BLACKBOARD_KEY_TWEAKS);
            if (tweakers == null) {
                return false;
            }
            for (final ITweaker tweaker : tweakers) {
                final Class<? extends ITweaker> tweakClass = tweaker.getClass();
                if ("FMLPluginWrapper".equals(tweakClass.getSimpleName())) {
                    final Field fdCoreModInstance = tweakClass.getField("coreModInstance");
                    fdCoreModInstance.setAccessible(true);
                    final Object coreMod = fdCoreModInstance.get(tweaker);
                    if (coreModName.equals(coreMod.getClass().getName())) {
                        return true;
                    }
                    continue;
                }
            }
        }
        catch (final Exception ex) {}
        return false;
    }
    
    @Override
    public String getPhaseProvider() {
        return MixinPlatformAgentFMLLegacy.class.getName() + "$PhaseProvider";
    }
    
    @Override
    public void prepare() {
        this.initInjectionState |= isTweakerQueued("FMLInjectionAndSortingTweaker");
    }
    
    @Override
    public void inject() {
        if (this.coreModWrapper != null && this.checkForCoInitialisation()) {
            MixinPlatformAgentAbstract.logger.debug("FML agent is co-initiralising coremod instance {} for {}", this.coreModWrapper, this.handle);
            this.coreModWrapper.injectIntoClassLoader(Launch.classLoader);
        }
    }
    
    protected final boolean checkForCoInitialisation() {
        final boolean injectionTweaker = isTweakerQueued("FMLInjectionAndSortingTweaker");
        final boolean terminalTweaker = isTweakerQueued("TerminalTweaker");
        if ((this.initInjectionState && terminalTweaker) || injectionTweaker) {
            MixinPlatformAgentAbstract.logger.debug("FML agent is skipping co-init for {} because FML will inject it normally", this.coreModWrapper);
            return false;
        }
        return !isTweakerQueued("FMLDeobfTweaker");
    }
    
    private Class<?> getCoreModManagerClass() {
        if (this.clCoreModManager != null) {
            return this.clCoreModManager;
        }
        try {
            try {
                this.clCoreModManager = Class.forName(GlobalProperties.getString(GlobalProperties.Keys.FML_CORE_MOD_MANAGER, "net.minecraftforge.fml.relauncher.CoreModManager"));
            }
            catch (final ClassNotFoundException ex) {
                this.clCoreModManager = Class.forName("cpw.mods.fml.relauncher.CoreModManager");
            }
        }
        catch (final ClassNotFoundException ex) {
            MixinPlatformAgentAbstract.logger.info("FML platform manager could not load class {}. Proceeding without FML support.", ex.getMessage());
        }
        return this.clCoreModManager;
    }
    
    private static boolean isTweakerQueued(final String tweakerName) {
        for (final String tweaker : GlobalProperties.get(MixinServiceLaunchWrapper.BLACKBOARD_KEY_TWEAKCLASSES)) {
            if (tweaker.endsWith(tweakerName)) {
                return true;
            }
        }
        return false;
    }
    
    private static List<String> getIgnoredMods(final Class<?> clCoreModManager) throws IllegalAccessException, InvocationTargetException {
        Method mdGetIgnoredMods = null;
        try {
            mdGetIgnoredMods = clCoreModManager.getDeclaredMethod(GlobalProperties.getString(GlobalProperties.Keys.FML_GET_IGNORED_MODS, "getIgnoredMods"), (Class<?>[])new Class[0]);
        }
        catch (final NoSuchMethodException ex1) {
            try {
                mdGetIgnoredMods = clCoreModManager.getDeclaredMethod("getLoadedCoremods", (Class<?>[])new Class[0]);
            }
            catch (final NoSuchMethodException ex2) {
                MixinPlatformAgentAbstract.logger.catching(org.spongepowered.asm.logging.Level.DEBUG, ex2);
                return Collections.emptyList();
            }
        }
        return (List)mdGetIgnoredMods.invoke(null, new Object[0]);
    }
    
    @Override
    public void init() {
        if (this.getCoreModManagerClass() != null) {
            this.injectRemapper();
        }
    }
    
    private void injectRemapper() {
        try {
            MixinPlatformAgentAbstract.logger.debug("Creating FML remapper adapter: {}", "org.spongepowered.asm.bridge.RemapperAdapterFML");
            final Class<?> clFmlRemapperAdapter = Class.forName("org.spongepowered.asm.bridge.RemapperAdapterFML", true, Launch.classLoader);
            final Method mdCreate = clFmlRemapperAdapter.getDeclaredMethod("create", (Class<?>[])new Class[0]);
            final IRemapper remapper = (IRemapper)mdCreate.invoke(null, new Object[0]);
            MixinEnvironment.getDefaultEnvironment().getRemappers().add(remapper);
        }
        catch (final Exception ex) {
            MixinPlatformAgentAbstract.logger.debug("Failed instancing FML remapper adapter, things will probably go horribly for notch-obf'd mods!", new Object[0]);
        }
    }
    
    @Override
    public String getSideName() {
        final List<ITweaker> tweakerList = GlobalProperties.get(MixinServiceLaunchWrapper.BLACKBOARD_KEY_TWEAKS);
        if (tweakerList == null) {
            return null;
        }
        for (final ITweaker tweaker : tweakerList) {
            if (tweaker.getClass().getName().endsWith(".common.launcher.FMLServerTweaker")) {
                return "SERVER";
            }
            if (tweaker.getClass().getName().endsWith(".common.launcher.FMLTweaker")) {
                return "CLIENT";
            }
        }
        final String name = MixinPlatformAgentAbstract.invokeStringMethod(Launch.classLoader, "net.minecraftforge.fml.relauncher.FMLLaunchHandler", "side");
        if (name != null) {
            return name;
        }
        return MixinPlatformAgentAbstract.invokeStringMethod(Launch.classLoader, "cpw.mods.fml.relauncher.FMLLaunchHandler", "side");
    }
    
    @Override
    public Collection<IContainerHandle> getMixinContainers() {
        return null;
    }
    
    @Deprecated
    @Override
    public void wire(final MixinEnvironment.Phase phase, final IConsumer<MixinEnvironment.Phase> phaseConsumer) {
        super.wire(phase, phaseConsumer);
        if (phase == MixinEnvironment.Phase.PREINIT) {
            begin(phaseConsumer);
        }
    }
    
    @Deprecated
    @Override
    public void unwire() {
        end();
    }
    
    static void begin(final IConsumer<MixinEnvironment.Phase> delegate) {
        final org.apache.logging.log4j.Logger fmlLog = LogManager.getLogger("FML");
        if (!(fmlLog instanceof Logger)) {
            return;
        }
        MixinPlatformAgentFMLLegacy.log = (Logger)fmlLog;
        MixinPlatformAgentFMLLegacy.oldLevel = MixinPlatformAgentFMLLegacy.log.getLevel();
        (MixinPlatformAgentFMLLegacy.appender = new MixinAppender(delegate)).start();
        MixinPlatformAgentFMLLegacy.log.addAppender(MixinPlatformAgentFMLLegacy.appender);
        MixinPlatformAgentFMLLegacy.log.setLevel(Level.ALL);
    }
    
    static void end() {
        if (MixinPlatformAgentFMLLegacy.log != null) {
            MixinPlatformAgentFMLLegacy.log.removeAppender(MixinPlatformAgentFMLLegacy.appender);
        }
    }
    
    static {
        loadedCoreMods = new HashSet<String>();
        for (final String cmdLineCoreMod : System.getProperty("fml.coreMods.load", "").split(",")) {
            if (!cmdLineCoreMod.isEmpty()) {
                MixinPlatformAgentAbstract.logger.debug("FML platform agent will ignore coremod {} specified on the command line", cmdLineCoreMod);
                MixinPlatformAgentFMLLegacy.loadedCoreMods.add(cmdLineCoreMod);
            }
        }
        MixinPlatformAgentFMLLegacy.oldLevel = null;
    }
    
    static class MixinAppender extends AbstractAppender
    {
        private final IConsumer<MixinEnvironment.Phase> delegate;
        
        MixinAppender(final IConsumer<MixinEnvironment.Phase> delegate) {
            super("MixinLogWatcherAppender", null, null);
            this.delegate = delegate;
        }
        
        @Override
        public void append(final LogEvent event) {
            if (event.getLevel() != Level.DEBUG || !"Validating minecraft".equals(event.getMessage().getFormattedMessage())) {
                return;
            }
            this.delegate.accept(MixinEnvironment.Phase.INIT);
            if (MixinPlatformAgentFMLLegacy.log.getLevel() == Level.ALL) {
                MixinPlatformAgentFMLLegacy.log.setLevel(MixinPlatformAgentFMLLegacy.oldLevel);
            }
        }
    }
}
