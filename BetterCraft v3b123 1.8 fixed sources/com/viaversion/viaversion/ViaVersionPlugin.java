// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion;

import com.viaversion.viaversion.bukkit.platform.PaperViaInjector;
import org.bukkit.event.EventException;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import java.util.Collections;
import com.viaversion.viaversion.unsupported.UnsupportedPlugin;
import com.viaversion.viaversion.unsupported.UnsupportedServerSoftware;
import com.viaversion.viaversion.api.platform.UnsupportedSoftware;
import java.util.Collection;
import com.viaversion.viaversion.util.GsonUtil;
import java.util.List;
import com.viaversion.viaversion.dump.PluginInfo;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.gson.JsonObject;
import org.bukkit.ChatColor;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import java.util.UUID;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import com.viaversion.viaversion.bukkit.commands.BukkitCommandSender;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import java.util.concurrent.TimeUnit;
import com.viaversion.viaversion.bukkit.platform.BukkitViaTask;
import com.viaversion.viaversion.bukkit.platform.BukkitViaTaskTask;
import com.viaversion.viaversion.api.platform.PlatformTask;
import org.bukkit.event.Event;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import com.viaversion.viaversion.api.ViaManager;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.bukkit.platform.BukkitViaLoader;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.commands.ViaCommandHandler;
import com.viaversion.viaversion.bukkit.platform.BukkitViaInjector;
import com.viaversion.viaversion.bukkit.platform.BukkitViaAPI;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.bukkit.platform.BukkitViaConfig;
import com.viaversion.viaversion.bukkit.commands.BukkitCommandHandler;
import org.bukkit.entity.Player;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import org.bukkit.plugin.java.JavaPlugin;

public class ViaVersionPlugin extends JavaPlugin implements ViaPlatform<Player>
{
    private static final boolean FOLIA;
    private static ViaVersionPlugin instance;
    private final BukkitCommandHandler commandHandler;
    private final BukkitViaConfig conf;
    private final ViaAPI<Player> api;
    private boolean protocolSupport;
    private boolean lateBind;
    
    public ViaVersionPlugin() {
        this.api = new BukkitViaAPI(this);
        ViaVersionPlugin.instance = this;
        this.commandHandler = new BukkitCommandHandler();
        final BukkitViaInjector injector = new BukkitViaInjector();
        Via.init(ViaManagerImpl.builder().platform(this).commandHandler(this.commandHandler).injector(injector).loader(new BukkitViaLoader(this)).build());
        this.conf = new BukkitViaConfig();
    }
    
    public void onLoad() {
        this.protocolSupport = (Bukkit.getPluginManager().getPlugin("ProtocolSupport") != null);
        if (!(this.lateBind = !((BukkitViaInjector)Via.getManager().getInjector()).isBinded())) {
            this.getLogger().info("ViaVersion " + this.getDescription().getVersion() + " is now loaded. Registering protocol transformers and injecting...");
            ((ViaManagerImpl)Via.getManager()).init();
        }
        else {
            this.getLogger().info("ViaVersion " + this.getDescription().getVersion() + " is now loaded. Waiting for boot (late-bind).");
        }
    }
    
    public void onEnable() {
        final ViaManagerImpl manager = (ViaManagerImpl)Via.getManager();
        if (this.lateBind) {
            this.getLogger().info("Registering protocol transformers and injecting...");
            manager.init();
        }
        if (ViaVersionPlugin.FOLIA) {
            Class<? extends Event> serverInitEventClass;
            try {
                serverInitEventClass = (Class<? extends Event>)Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
            }
            catch (final ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
            this.getServer().getPluginManager().registerEvent((Class)serverInitEventClass, (Listener)new Listener() {}, EventPriority.HIGHEST, (listener, event) -> manager.onServerLoaded(), (Plugin)this);
        }
        else if (Via.getManager().getInjector().lateProtocolVersionSetting()) {
            this.runSync(manager::onServerLoaded);
        }
        else {
            manager.onServerLoaded();
        }
        this.getCommand("viaversion").setExecutor((CommandExecutor)this.commandHandler);
        this.getCommand("viaversion").setTabCompleter((TabCompleter)this.commandHandler);
    }
    
    public void onDisable() {
        ((ViaManagerImpl)Via.getManager()).destroy();
    }
    
    public String getPlatformName() {
        return Bukkit.getServer().getName();
    }
    
    public String getPlatformVersion() {
        return Bukkit.getServer().getVersion();
    }
    
    public String getPluginVersion() {
        return this.getDescription().getVersion();
    }
    
    public PlatformTask runAsync(final Runnable runnable) {
        if (ViaVersionPlugin.FOLIA) {
            return new BukkitViaTaskTask(Via.getManager().getScheduler().execute(runnable));
        }
        return new BukkitViaTask(this.getServer().getScheduler().runTaskAsynchronously((Plugin)this, runnable));
    }
    
    public PlatformTask runRepeatingAsync(final Runnable runnable, final long ticks) {
        if (ViaVersionPlugin.FOLIA) {
            return new BukkitViaTaskTask(Via.getManager().getScheduler().schedule(runnable, ticks * 50L, TimeUnit.MILLISECONDS));
        }
        return new BukkitViaTask(this.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)this, runnable, 0L, ticks));
    }
    
    public PlatformTask runSync(final Runnable runnable) {
        if (ViaVersionPlugin.FOLIA) {
            return this.runAsync(runnable);
        }
        return new BukkitViaTask(this.getServer().getScheduler().runTask((Plugin)this, runnable));
    }
    
    public PlatformTask runSync(final Runnable runnable, final long delay) {
        return new BukkitViaTask(this.getServer().getScheduler().runTaskLater((Plugin)this, runnable, delay));
    }
    
    public PlatformTask runRepeatingSync(final Runnable runnable, final long period) {
        return new BukkitViaTask(this.getServer().getScheduler().runTaskTimer((Plugin)this, runnable, 0L, period));
    }
    
    public ViaCommandSender[] getOnlinePlayers() {
        final ViaCommandSender[] array = new ViaCommandSender[Bukkit.getOnlinePlayers().size()];
        int i = 0;
        for (final Player player : Bukkit.getOnlinePlayers()) {
            array[i++] = new BukkitCommandSender((CommandSender)player);
        }
        return array;
    }
    
    public void sendMessage(final UUID uuid, final String message) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.sendMessage(message);
        }
    }
    
    public boolean kickPlayer(final UUID uuid, final String message) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.kickPlayer(message);
            return true;
        }
        return false;
    }
    
    public boolean isPluginEnabled() {
        return Bukkit.getPluginManager().getPlugin("ViaVersion").isEnabled();
    }
    
    public ConfigurationProvider getConfigurationProvider() {
        return this.conf;
    }
    
    public void onReload() {
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            this.getLogger().severe("ViaVersion is already loaded, we're going to kick all the players... because otherwise we'll crash because of ProtocolLib.");
            for (final Player player : Bukkit.getOnlinePlayers()) {
                player.kickPlayer(ChatColor.translateAlternateColorCodes('&', this.conf.getReloadDisconnectMsg()));
            }
        }
        else {
            this.getLogger().severe("ViaVersion is already loaded, this should work fine. If you get any console errors, try rebooting.");
        }
    }
    
    public JsonObject getDump() {
        final JsonObject platformSpecific = new JsonObject();
        final List<PluginInfo> plugins = new ArrayList<PluginInfo>();
        for (final Plugin p : Bukkit.getPluginManager().getPlugins()) {
            plugins.add(new PluginInfo(p.isEnabled(), p.getDescription().getName(), p.getDescription().getVersion(), p.getDescription().getMain(), p.getDescription().getAuthors()));
        }
        platformSpecific.add("plugins", GsonUtil.getGson().toJsonTree(plugins));
        return platformSpecific;
    }
    
    public boolean isOldClientsAllowed() {
        return !this.protocolSupport;
    }
    
    public BukkitViaConfig getConf() {
        return this.conf;
    }
    
    public ViaAPI<Player> getApi() {
        return this.api;
    }
    
    public final Collection<UnsupportedSoftware> getUnsupportedSoftwareClasses() {
        final List<UnsupportedSoftware> list = new ArrayList<UnsupportedSoftware>(super.getUnsupportedSoftwareClasses());
        list.add(new UnsupportedServerSoftware.Builder().name("Yatopia").reason("You are using server software that - outside of possibly breaking ViaVersion - can also cause severe damage to your server's integrity as a whole.").addClassName("org.yatopiamc.yatopia.server.YatopiaConfig").addClassName("net.yatopia.api.event.PlayerAttackEntityEvent").addClassName("yatopiamc.org.yatopia.server.YatopiaConfig").addMethod("org.bukkit.Server", "getLastTickTime").build());
        list.add(new UnsupportedPlugin.Builder().name("software to mess with message signing").reason("Instead of doing the obvious (or nothing at all), these kinds of plugins completely break chat message handling, usually then also breaking other plugins.").addPlugin("NoEncryption").addPlugin("NoReport").addPlugin("NoChatReports").addPlugin("NoChatReport").build());
        return (Collection<UnsupportedSoftware>)Collections.unmodifiableList((List<?>)list);
    }
    
    public boolean hasPlugin(final String name) {
        return this.getServer().getPluginManager().getPlugin(name) != null;
    }
    
    public boolean isLateBind() {
        return this.lateBind;
    }
    
    public boolean isProtocolSupport() {
        return this.protocolSupport;
    }
    
    @Deprecated
    public static ViaVersionPlugin getInstance() {
        return ViaVersionPlugin.instance;
    }
    
    static {
        FOLIA = PaperViaInjector.hasClass("io.papermc.paper.threadedregions.RegionizedServer");
    }
}
