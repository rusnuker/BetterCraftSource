// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase;

import java.util.List;
import java.util.Iterator;
import com.viaversion.viaversion.protocol.ProtocolManagerImpl;
import com.viaversion.viaversion.api.ViaManager;
import com.viaversion.viaversion.commands.ViaCommandHandler;
import viamcp.vialoadingbase.platform.viaversion.VLBViaCommandHandler;
import com.viaversion.viaversion.api.platform.ViaInjector;
import viamcp.vialoadingbase.platform.viaversion.VLBViaInjector;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import viamcp.vialoadingbase.platform.viaversion.VLBViaProviders;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import java.util.LinkedHashMap;
import viamcp.vialoadingbase.platform.ViaRewindPlatformImpl;
import com.viaversion.viaversion.api.Via;
import viamcp.vialoadingbase.platform.ViaBackwardsPlatformImpl;
import java.util.Collection;
import viamcp.vialoadingbase.platform.ViaVersionPlatformImpl;
import viamcp.vialoadingbase.util.JLoggerToLog4j;
import org.apache.logging.log4j.LogManager;
import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import java.util.function.Consumer;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.util.function.Supplier;
import java.util.function.BooleanSupplier;
import java.io.File;
import java.util.LinkedList;
import viamcp.vialoadingbase.model.ComparableProtocolVersion;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.Map;
import viamcp.vialoadingbase.model.Platform;
import java.util.logging.Logger;

public class ViaLoadingBase
{
    public static final String VERSION = "${vialoadingbase_version}";
    public static final Logger LOGGER;
    public static final Platform PSEUDO_VIA_VERSION;
    public static final Platform PLATFORM_VIA_BACKWARDS;
    public static final Platform PLATFORM_VIA_REWIND;
    public static final Map<ProtocolVersion, ComparableProtocolVersion> PROTOCOLS;
    private static ViaLoadingBase instance;
    private final LinkedList<Platform> platforms;
    private final File runDirectory;
    private final int nativeVersion;
    private final BooleanSupplier forceNativeVersionCondition;
    private final Supplier<JsonObject> dumpSupplier;
    private final Consumer<ViaProviders> providers;
    private final Consumer<ViaManagerImpl.ViaManagerBuilder> managerBuilderConsumer;
    private final Consumer<ComparableProtocolVersion> onProtocolReload;
    private ComparableProtocolVersion nativeProtocolVersion;
    private ComparableProtocolVersion targetProtocolVersion;
    
    static {
        LOGGER = new JLoggerToLog4j(LogManager.getLogger("ViaLoadingBase"));
        PSEUDO_VIA_VERSION = new Platform("ViaVersion", () -> true, () -> {}, protocolVersions -> protocolVersions.addAll(ViaVersionPlatformImpl.createVersionList()));
        PLATFORM_VIA_BACKWARDS = new Platform("ViaBackwards", () -> inClassPath("com.viaversion.viabackwards.api.ViaBackwardsPlatform"), () -> new ViaBackwardsPlatformImpl(Via.getManager().getPlatform().getDataFolder()));
        PLATFORM_VIA_REWIND = new Platform("ViaRewind", () -> inClassPath("de.gerrygames.viarewind.api.ViaRewindPlatform"), () -> new ViaRewindPlatformImpl(Via.getManager().getPlatform().getDataFolder()));
        PROTOCOLS = new LinkedHashMap<ProtocolVersion, ComparableProtocolVersion>();
    }
    
    public ViaLoadingBase(final LinkedList<Platform> platforms, final File runDirectory, final int nativeVersion, final BooleanSupplier forceNativeVersionCondition, final Supplier<JsonObject> dumpSupplier, final Consumer<ViaProviders> providers, final Consumer<ViaManagerImpl.ViaManagerBuilder> managerBuilderConsumer, final Consumer<ComparableProtocolVersion> onProtocolReload) {
        this.platforms = platforms;
        this.runDirectory = new File(runDirectory, "ViaLoadingBase");
        this.nativeVersion = nativeVersion;
        this.forceNativeVersionCondition = forceNativeVersionCondition;
        this.dumpSupplier = dumpSupplier;
        this.providers = providers;
        this.managerBuilderConsumer = managerBuilderConsumer;
        this.onProtocolReload = onProtocolReload;
        (ViaLoadingBase.instance = this).initPlatform();
    }
    
    public ComparableProtocolVersion getTargetVersion() {
        if (this.forceNativeVersionCondition != null && this.forceNativeVersionCondition.getAsBoolean()) {
            return this.nativeProtocolVersion;
        }
        return this.targetProtocolVersion;
    }
    
    public void reload(final ProtocolVersion protocolVersion) {
        this.reload(fromProtocolVersion(protocolVersion));
    }
    
    public void reload(final ComparableProtocolVersion protocolVersion) {
        this.targetProtocolVersion = protocolVersion;
        if (this.onProtocolReload != null) {
            this.onProtocolReload.accept(this.targetProtocolVersion);
        }
    }
    
    public void initPlatform() {
        for (final Platform platform : this.platforms) {
            platform.createProtocolPath();
        }
        for (final ProtocolVersion preProtocol : Platform.TEMP_INPUT_PROTOCOLS) {
            ViaLoadingBase.PROTOCOLS.put(preProtocol, new ComparableProtocolVersion(preProtocol.getVersion(), preProtocol.getName(), Platform.TEMP_INPUT_PROTOCOLS.indexOf(preProtocol)));
        }
        this.nativeProtocolVersion = fromProtocolVersion(ProtocolVersion.getProtocol(this.nativeVersion));
        this.targetProtocolVersion = this.nativeProtocolVersion;
        final ViaVersionPlatformImpl viaVersionPlatform = new ViaVersionPlatformImpl(ViaLoadingBase.LOGGER);
        final ViaManagerImpl.ViaManagerBuilder builder = ViaManagerImpl.builder().platform(viaVersionPlatform).loader(new VLBViaProviders()).injector(new VLBViaInjector()).commandHandler(new VLBViaCommandHandler());
        if (this.managerBuilderConsumer != null) {
            this.managerBuilderConsumer.accept(builder);
        }
        Via.init(builder.build());
        final ViaManagerImpl manager = (ViaManagerImpl)Via.getManager();
        manager.addEnableListener(() -> {
            this.platforms.iterator();
            final Iterator iterator3;
            while (iterator3.hasNext()) {
                final Platform platform2 = iterator3.next();
                platform2.build(ViaLoadingBase.LOGGER);
            }
            return;
        });
        manager.init();
        manager.onServerLoaded();
        manager.getProtocolManager().setMaxProtocolPathSize(Integer.MAX_VALUE);
        manager.getProtocolManager().setMaxPathDeltaIncrease(-1);
        ((ProtocolManagerImpl)manager.getProtocolManager()).refreshVersions();
        ViaLoadingBase.LOGGER.info("ViaLoadingBase has loaded " + Platform.COUNT + "/" + this.platforms.size() + " platforms");
    }
    
    public static ViaLoadingBase getInstance() {
        return ViaLoadingBase.instance;
    }
    
    public List<Platform> getSubPlatforms() {
        return this.platforms;
    }
    
    public File getRunDirectory() {
        return this.runDirectory;
    }
    
    public int getNativeVersion() {
        return this.nativeVersion;
    }
    
    public Supplier<JsonObject> getDumpSupplier() {
        return this.dumpSupplier;
    }
    
    public Consumer<ViaProviders> getProviders() {
        return this.providers;
    }
    
    public static boolean inClassPath(final String name) {
        try {
            Class.forName(name);
            return true;
        }
        catch (final Exception ignored) {
            return false;
        }
    }
    
    public static ComparableProtocolVersion fromProtocolVersion(final ProtocolVersion protocolVersion) {
        return ViaLoadingBase.PROTOCOLS.get(protocolVersion);
    }
    
    public static ComparableProtocolVersion fromProtocolId(final int protocolId) {
        return ViaLoadingBase.PROTOCOLS.values().stream().filter(protocol -> protocol.getVersion() == n).findFirst().orElse(null);
    }
    
    public static List<ProtocolVersion> getProtocols() {
        return new LinkedList<ProtocolVersion>(ViaLoadingBase.PROTOCOLS.keySet());
    }
    
    public static class ViaLoadingBaseBuilder
    {
        private final LinkedList<Platform> platforms;
        private File runDirectory;
        private Integer nativeVersion;
        private BooleanSupplier forceNativeVersionCondition;
        private Supplier<JsonObject> dumpSupplier;
        private Consumer<ViaProviders> providers;
        private Consumer<ViaManagerImpl.ViaManagerBuilder> managerBuilderConsumer;
        private Consumer<ComparableProtocolVersion> onProtocolReload;
        
        public ViaLoadingBaseBuilder() {
            (this.platforms = new LinkedList<Platform>()).add(ViaLoadingBase.PSEUDO_VIA_VERSION);
            this.platforms.add(ViaLoadingBase.PLATFORM_VIA_BACKWARDS);
            this.platforms.add(ViaLoadingBase.PLATFORM_VIA_REWIND);
        }
        
        public static ViaLoadingBaseBuilder create() {
            return new ViaLoadingBaseBuilder();
        }
        
        public ViaLoadingBaseBuilder platform(final Platform platform) {
            this.platforms.add(platform);
            return this;
        }
        
        public ViaLoadingBaseBuilder platform(final Platform platform, final int position) {
            this.platforms.add(position, platform);
            return this;
        }
        
        public ViaLoadingBaseBuilder runDirectory(final File runDirectory) {
            this.runDirectory = runDirectory;
            return this;
        }
        
        public ViaLoadingBaseBuilder nativeVersion(final int nativeVersion) {
            this.nativeVersion = nativeVersion;
            return this;
        }
        
        public ViaLoadingBaseBuilder forceNativeVersionCondition(final BooleanSupplier forceNativeVersionCondition) {
            this.forceNativeVersionCondition = forceNativeVersionCondition;
            return this;
        }
        
        public ViaLoadingBaseBuilder dumpSupplier(final Supplier<JsonObject> dumpSupplier) {
            this.dumpSupplier = dumpSupplier;
            return this;
        }
        
        public ViaLoadingBaseBuilder providers(final Consumer<ViaProviders> providers) {
            this.providers = providers;
            return this;
        }
        
        public ViaLoadingBaseBuilder managerBuilderConsumer(final Consumer<ViaManagerImpl.ViaManagerBuilder> managerBuilderConsumer) {
            this.managerBuilderConsumer = managerBuilderConsumer;
            return this;
        }
        
        public ViaLoadingBaseBuilder onProtocolReload(final Consumer<ComparableProtocolVersion> onProtocolReload) {
            this.onProtocolReload = onProtocolReload;
            return this;
        }
        
        public void build() {
            if (ViaLoadingBase.getInstance() != null) {
                ViaLoadingBase.LOGGER.severe("ViaLoadingBase has already started the platform!");
                return;
            }
            if (this.runDirectory == null || this.nativeVersion == null) {
                ViaLoadingBase.LOGGER.severe("Please check your ViaLoadingBaseBuilder arguments!");
                return;
            }
            new ViaLoadingBase(this.platforms, this.runDirectory, this.nativeVersion, this.forceNativeVersionCondition, this.dumpSupplier, this.providers, this.managerBuilderConsumer, this.onProtocolReload);
        }
    }
}
