// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.platform;

import java.util.Iterator;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import com.viaversion.viaversion.bukkit.listeners.protocol1_19to1_18_2.BlockBreakListener;
import com.viaversion.viaversion.bukkit.providers.BukkitAckSequenceProvider;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.provider.AckSequenceProvider;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionData;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.BlockConnectionProvider;
import com.viaversion.viaversion.bukkit.providers.BukkitBlockConnectionProvider;
import com.viaversion.viaversion.bukkit.providers.BukkitInventoryQuickMoveProvider;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.providers.InventoryQuickMoveProvider;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.HandItemProvider;
import com.viaversion.viaversion.bukkit.providers.BukkitViaMovementTransmitter;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import com.viaversion.viaversion.bukkit.listeners.protocol1_19_4To1_19_3.ArmorToggleListener;
import com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8.PaperPatch;
import com.viaversion.viaversion.bukkit.listeners.protocol1_15to1_14_4.EntityToggleGlideListener;
import com.viaversion.viaversion.bukkit.listeners.multiversion.PlayerSneakListener;
import com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8.BlockListener;
import com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8.DeathListener;
import com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8.ArmorListener;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.bukkit.compat.ProtocolSupportCompat;
import org.bukkit.Bukkit;
import com.viaversion.viaversion.bukkit.listeners.JoinListener;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.bukkit.listeners.UpdateListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import java.util.HashSet;
import com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8.HandItemCache;
import com.viaversion.viaversion.ViaVersionPlugin;
import org.bukkit.scheduler.BukkitTask;
import java.util.Set;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;

public class BukkitViaLoader implements ViaPlatformLoader
{
    private final Set<BukkitTask> tasks;
    private final ViaVersionPlugin plugin;
    private HandItemCache handItemCache;
    
    public BukkitViaLoader(final ViaVersionPlugin plugin) {
        this.tasks = new HashSet<BukkitTask>();
        this.plugin = plugin;
    }
    
    public void registerListener(final Listener listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, (Plugin)this.plugin);
    }
    
    @Deprecated
    public <T extends Listener> T storeListener(final T listener) {
        return listener;
    }
    
    @Override
    public void load() {
        this.registerListener((Listener)new UpdateListener());
        if (Via.getConfig().shouldRegisterUserConnectionOnJoin()) {
            this.registerListener((Listener)new JoinListener());
        }
        final ViaVersionPlugin plugin = (ViaVersionPlugin)Bukkit.getPluginManager().getPlugin("ViaVersion");
        if (plugin.isProtocolSupport() && ProtocolSupportCompat.isMultiplatformPS()) {
            ProtocolSupportCompat.registerPSConnectListener(plugin);
        }
        if (!Via.getAPI().getServerVersion().isKnown()) {
            Via.getPlatform().getLogger().severe("Server version has not been loaded yet, cannot register additional listeners");
            return;
        }
        final int serverProtocolVersion = Via.getAPI().getServerVersion().lowestSupportedVersion();
        if (serverProtocolVersion < ProtocolVersion.v1_9.getVersion()) {
            new ArmorListener((Plugin)plugin).register();
            new DeathListener((Plugin)plugin).register();
            new BlockListener((Plugin)plugin).register();
            if (plugin.getConf().isItemCache()) {
                this.handItemCache = new HandItemCache();
                this.tasks.add(this.handItemCache.runTaskTimerAsynchronously((Plugin)plugin, 1L, 1L));
            }
        }
        Label_0284: {
            if (serverProtocolVersion < ProtocolVersion.v1_14.getVersion()) {
                final boolean use1_9Fix = plugin.getConf().is1_9HitboxFix() && serverProtocolVersion < ProtocolVersion.v1_9.getVersion();
                if (!use1_9Fix) {
                    if (!plugin.getConf().is1_14HitboxFix()) {
                        break Label_0284;
                    }
                }
                try {
                    new PlayerSneakListener(plugin, use1_9Fix, plugin.getConf().is1_14HitboxFix()).register();
                }
                catch (final ReflectiveOperationException e) {
                    Via.getPlatform().getLogger().warning("Could not load hitbox fix - please report this on our GitHub");
                    e.printStackTrace();
                }
            }
        }
        if (serverProtocolVersion < ProtocolVersion.v1_15.getVersion()) {
            try {
                Class.forName("org.bukkit.event.entity.EntityToggleGlideEvent");
                new EntityToggleGlideListener(plugin).register();
            }
            catch (final ClassNotFoundException ex) {}
        }
        if (serverProtocolVersion < ProtocolVersion.v1_12.getVersion() && !Boolean.getBoolean("com.viaversion.ignorePaperBlockPlacePatch")) {
            boolean paper = true;
            try {
                Class.forName("org.github.paperspigot.PaperSpigotConfig");
            }
            catch (final ClassNotFoundException ignored) {
                try {
                    Class.forName("com.destroystokyo.paper.PaperConfig");
                }
                catch (final ClassNotFoundException alsoIgnored) {
                    paper = false;
                }
            }
            if (paper) {
                new PaperPatch((Plugin)plugin).register();
            }
        }
        if (serverProtocolVersion < ProtocolVersion.v1_19_4.getVersion() && plugin.getConf().isArmorToggleFix() && this.hasGetHandMethod()) {
            new ArmorToggleListener(plugin).register();
        }
        if (serverProtocolVersion < ProtocolVersion.v1_9.getVersion()) {
            Via.getManager().getProviders().use((Class<BukkitViaMovementTransmitter>)MovementTransmitterProvider.class, new BukkitViaMovementTransmitter());
            Via.getManager().getProviders().use((Class<BukkitViaLoader$1>)HandItemProvider.class, new HandItemProvider() {
                @Override
                public Item getHandItem(final UserConnection info) {
                    if (BukkitViaLoader.this.handItemCache != null) {
                        return BukkitViaLoader.this.handItemCache.getHandItem(info.getProtocolInfo().getUuid());
                    }
                    try {
                        return Bukkit.getScheduler().callSyncMethod(Bukkit.getPluginManager().getPlugin("ViaVersion"), () -> {
                            final UUID playerUUID = info.getProtocolInfo().getUuid();
                            final Player player = Bukkit.getPlayer(playerUUID);
                            if (player != null) {
                                return HandItemCache.convert(player.getItemInHand());
                            }
                            else {
                                return null;
                            }
                        }).get(10L, TimeUnit.SECONDS);
                    }
                    catch (final Exception e) {
                        Via.getPlatform().getLogger().severe("Error fetching hand item: " + e.getClass().getName());
                        if (Via.getManager().isDebug()) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }
            });
        }
        if (serverProtocolVersion < ProtocolVersion.v1_12.getVersion() && plugin.getConf().is1_12QuickMoveActionFix()) {
            Via.getManager().getProviders().use((Class<BukkitInventoryQuickMoveProvider>)InventoryQuickMoveProvider.class, new BukkitInventoryQuickMoveProvider());
        }
        if (serverProtocolVersion < ProtocolVersion.v1_13.getVersion() && Via.getConfig().getBlockConnectionMethod().equalsIgnoreCase("world")) {
            final BukkitBlockConnectionProvider blockConnectionProvider = new BukkitBlockConnectionProvider();
            Via.getManager().getProviders().use(BlockConnectionProvider.class, blockConnectionProvider);
            ConnectionData.blockConnectionProvider = blockConnectionProvider;
        }
        if (serverProtocolVersion < ProtocolVersion.v1_19.getVersion()) {
            Via.getManager().getProviders().use((Class<BukkitAckSequenceProvider>)AckSequenceProvider.class, new BukkitAckSequenceProvider(plugin));
            new BlockBreakListener(plugin).register();
        }
    }
    
    private boolean hasGetHandMethod() {
        try {
            PlayerInteractEvent.class.getDeclaredMethod("getHand", (Class<?>[])new Class[0]);
            Material.class.getMethod("getEquipmentSlot", (Class<?>[])new Class[0]);
            return true;
        }
        catch (final NoSuchMethodException e) {
            return false;
        }
    }
    
    @Override
    public void unload() {
        for (final BukkitTask task : this.tasks) {
            task.cancel();
        }
        this.tasks.clear();
    }
}
