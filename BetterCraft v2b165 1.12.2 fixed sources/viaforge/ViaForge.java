// 
// Decompiled by Procyon v0.6.0
// 

package viaforge;

import java.util.concurrent.ThreadFactory;
import viaforge.loader.VRRewindLoader;
import viaforge.loader.VRBackwardsLoader;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.ViaManager;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import viaforge.platform.VRPlatform;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import viaforge.loader.VRProviderLoader;
import com.viaversion.viaversion.api.platform.ViaInjector;
import viaforge.platform.VRInjector;
import com.viaversion.viaversion.ViaManagerImpl;
import io.netty.channel.local.LocalEventLoopGroup;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import viaforge.utils.JLoggerToLog4j;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import io.netty.channel.EventLoop;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class ViaForge
{
    public static final int SHARED_VERSION = 340;
    private static final ViaForge instance;
    private final Logger jLogger;
    private final CompletableFuture<Void> initFuture;
    private ExecutorService asyncExecutor;
    private EventLoop eventLoop;
    private File file;
    private int version;
    private String lastServer;
    
    static {
        instance = new ViaForge();
    }
    
    public ViaForge() {
        this.jLogger = new JLoggerToLog4j(LogManager.getLogger("ViaForge"));
        this.initFuture = new CompletableFuture<Void>();
    }
    
    public static ViaForge getInstance() {
        return ViaForge.instance;
    }
    
    public void start() {
        final ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ViaForge-%d").build();
        this.asyncExecutor = Executors.newFixedThreadPool(8, factory);
        (this.eventLoop = new LocalEventLoopGroup(1, factory).next()).submit(this.initFuture::join);
        this.setVersion(340);
        this.file = new File("ViaForge");
        if (this.file.mkdir()) {
            this.getjLogger().info("Creating ViaForge Folder");
        }
        Via.init(ViaManagerImpl.builder().injector(new VRInjector()).loader(new VRProviderLoader()).platform(new VRPlatform(this.file)).build());
        MappingDataLoader.enableMappingsCache();
        ((ViaManagerImpl)Via.getManager()).init();
        new VRBackwardsLoader(this.file);
        new VRRewindLoader(this.file);
        this.initFuture.complete(null);
    }
    
    public Logger getjLogger() {
        return this.jLogger;
    }
    
    public CompletableFuture<Void> getInitFuture() {
        return this.initFuture;
    }
    
    public ExecutorService getAsyncExecutor() {
        return this.asyncExecutor;
    }
    
    public EventLoop getEventLoop() {
        return this.eventLoop;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public String getLastServer() {
        return this.lastServer;
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public void setVersion(final int version) {
        this.version = version;
    }
    
    public void setFile(final File file) {
        this.file = file;
    }
    
    public void setLastServer(final String lastServer) {
        this.lastServer = lastServer;
    }
}
