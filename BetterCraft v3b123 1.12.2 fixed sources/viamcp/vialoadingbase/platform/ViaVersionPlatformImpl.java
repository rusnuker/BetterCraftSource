// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.platform;

import com.viaversion.viaversion.api.platform.PlatformTask;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.platform.UnsupportedSoftware;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import java.util.concurrent.TimeUnit;
import com.viaversion.viaversion.api.Via;
import viamcp.vialoadingbase.util.VLBTask;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.ArrayList;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.List;
import java.io.File;
import viamcp.vialoadingbase.ViaLoadingBase;
import viamcp.vialoadingbase.platform.viaversion.VLBViaAPIWrapper;
import viamcp.vialoadingbase.platform.viaversion.VLBViaConfig;
import java.util.logging.Logger;
import com.viaversion.viaversion.api.ViaAPI;
import java.util.UUID;
import com.viaversion.viaversion.api.platform.ViaPlatform;

public class ViaVersionPlatformImpl implements ViaPlatform<UUID>
{
    private final ViaAPI<UUID> api;
    private final Logger logger;
    private final VLBViaConfig config;
    
    public ViaVersionPlatformImpl(final Logger logger) {
        this.api = new VLBViaAPIWrapper();
        this.logger = logger;
        this.config = new VLBViaConfig(new File(ViaLoadingBase.getInstance().getRunDirectory(), "viaversion.yml"));
    }
    
    public static List<ProtocolVersion> createVersionList() {
        final List<ProtocolVersion> versions = new ArrayList<Object>(ProtocolVersion.getProtocols()).stream().filter(protocolVersion -> protocolVersion != ProtocolVersion.unknown && ProtocolVersion.getProtocols().indexOf(protocolVersion) >= 7).collect((Collector<? super Object, ?, List<ProtocolVersion>>)Collectors.toList());
        Collections.reverse(versions);
        return versions;
    }
    
    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        return new ViaCommandSender[0];
    }
    
    @Override
    public void sendMessage(final UUID uuid, final String msg) {
        if (uuid == null) {
            this.getLogger().info(msg);
        }
        else {
            this.getLogger().info("[" + uuid + "] " + msg);
        }
    }
    
    @Override
    public boolean kickPlayer(final UUID uuid, final String s) {
        return false;
    }
    
    @Override
    public boolean disconnect(final UserConnection connection, final String message) {
        return super.disconnect(connection, message);
    }
    
    @Override
    public VLBTask runAsync(final Runnable runnable) {
        return new VLBTask(Via.getManager().getScheduler().execute(runnable));
    }
    
    @Override
    public VLBTask runRepeatingAsync(final Runnable runnable, final long ticks) {
        return new VLBTask(Via.getManager().getScheduler().scheduleRepeating(runnable, 0L, ticks * 50L, TimeUnit.MILLISECONDS));
    }
    
    @Override
    public VLBTask runSync(final Runnable runnable) {
        return this.runAsync(runnable);
    }
    
    @Override
    public VLBTask runSync(final Runnable runnable, final long ticks) {
        return new VLBTask(Via.getManager().getScheduler().schedule(runnable, ticks * 50L, TimeUnit.MILLISECONDS));
    }
    
    @Override
    public VLBTask runRepeatingSync(final Runnable runnable, final long ticks) {
        return this.runRepeatingAsync(runnable, ticks);
    }
    
    @Override
    public boolean isProxy() {
        return true;
    }
    
    @Override
    public void onReload() {
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
    
    @Override
    public ViaVersionConfig getConf() {
        return this.config;
    }
    
    @Override
    public ViaAPI<UUID> getApi() {
        return this.api;
    }
    
    @Override
    public File getDataFolder() {
        return ViaLoadingBase.getInstance().getRunDirectory();
    }
    
    @Override
    public String getPluginVersion() {
        return "4.7.0-1.20-pre4-SNAPSHOT";
    }
    
    @Override
    public String getPlatformName() {
        return "ViaLoadingBase by FlorianMichael";
    }
    
    @Override
    public String getPlatformVersion() {
        return "${vialoadingbase_version}";
    }
    
    @Override
    public boolean isPluginEnabled() {
        return true;
    }
    
    @Override
    public ConfigurationProvider getConfigurationProvider() {
        return this.config;
    }
    
    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }
    
    @Override
    public Collection<UnsupportedSoftware> getUnsupportedSoftwareClasses() {
        return super.getUnsupportedSoftwareClasses();
    }
    
    @Override
    public boolean hasPlugin(final String s) {
        return false;
    }
    
    @Override
    public JsonObject getDump() {
        if (ViaLoadingBase.getInstance().getDumpSupplier() == null) {
            return new JsonObject();
        }
        return ViaLoadingBase.getInstance().getDumpSupplier().get();
    }
}
