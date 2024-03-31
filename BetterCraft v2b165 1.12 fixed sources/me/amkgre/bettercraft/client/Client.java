// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client;

import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketPlayFriendPlayingOn;
import me.amkgre.bettercraft.client.mods.notifications.NotificationManager;
import me.amkgre.bettercraft.client.mods.notifications.Notification;
import me.amkgre.bettercraft.client.mods.notifications.NotificationType;
import net.labymod.labyconnect.packets.PacketPlayPlayerOnline;
import me.amkgre.bettercraft.client.commands.Command;
import net.labymod.labyconnect.packets.PacketMessage;
import net.labymod.event.SessionListener;
import net.labymod.labyconnect.Session;
import net.montoyo.mcef.MCEF;
import me.amkgre.bettercraft.client.mods.music.RadioList;
import me.amkgre.bettercraft.client.mods.clientchat.InterClienChatConnection;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordMain;
import me.amkgre.bettercraft.client.mods.altmanager.AltManager;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.mods.chunkanimator.ChunkAnimator;
import com.TominoCZ.FBP.FBP;
import viaforge.ViaForge;
import me.amkgre.bettercraft.client.utils.ClientSettingsUtils;
import me.amkgre.bettercraft.client.utils.FileManagerUtils;
import net.labymod.main.LabyMod;
import me.amkgre.bettercraft.client.mods.ircclient.command.IrcCmdManager;
import me.amkgre.bettercraft.client.mods.ircclient.IrcManager;
import me.amkgre.bettercraft.client.mods.ircclient.IrcBotClient;
import me.amkgre.bettercraft.client.mods.hologram.HologramManager;
import me.amkgre.bettercraft.client.mods.creative.InventoryManager;
import me.amkgre.bettercraft.client.mods.thealtening.AltService;
import me.amkgre.bettercraft.client.modules.ModuleManager;
import net.minecraft.client.Minecraft;

public class Client
{
    Minecraft mc;
    private static final Client Instance;
    public static String clientName;
    public static String clientVersion;
    public static String clientAuthor;
    public static String clientPlayer;
    public static String clientPrefix;
    public static String labymodPrefix;
    public ModuleManager moduleManager;
    public AltService altService;
    public InventoryManager inventoryManager;
    public HologramManager hologramManager;
    public IrcBotClient ircbot;
    public IrcManager ircmanager;
    public IrcCmdManager ircCmdManager;
    public LabyMod labyMod;
    
    static {
        Instance = new Client();
        Client.clientName = "BetterCraft";
        Client.clientVersion = "v2.0.0 b165";
        Client.clientAuthor = "Amkgre";
        Client.clientPlayer = "BC-User";
        Client.clientPrefix = "§8[§5BetterCraft§8] §7";
        Client.labymodPrefix = "§8[§5LabyMod§8] §7";
    }
    
    public Client() {
        this.mc = Minecraft.getMinecraft();
        this.moduleManager = new ModuleManager();
        this.altService = new AltService();
        this.inventoryManager = new InventoryManager();
        this.hologramManager = new HologramManager();
        this.ircbot = new IrcBotClient();
        this.ircmanager = new IrcManager();
        this.ircCmdManager = new IrcCmdManager();
        this.labyMod = new LabyMod();
    }
    
    public static final Client getInstance() {
        return Client.Instance;
    }
    
    public void onEnable() {
        try {
            FileManagerUtils.createFiles();
        }
        catch (final Exception ex) {}
        try {
            ClientSettingsUtils.load();
        }
        catch (final Exception ex2) {}
        try {
            ViaForge.getInstance().start();
        }
        catch (final Exception ex3) {}
        try {
            new FBP();
            FBP.INSTANCE.initialize();
        }
        catch (final Exception ex4) {}
        try {
            new ChunkAnimator();
            ChunkAnimator.INSTANCE.onStart();
        }
        catch (final Exception ex5) {}
        try {
            CommandManager.commands();
        }
        catch (final Exception ex6) {}
        try {
            AltManager.loadAlts();
        }
        catch (final Exception ex7) {}
        try {
            DiscordMain.getInstance().init();
        }
        catch (final Exception ex8) {}
        try {
            this.inventoryManager.loadItems();
        }
        catch (final Exception ex9) {}
        try {
            this.connectLabyMod();
            this.updateLabyMod();
        }
        catch (final Exception ex10) {}
        try {
            InterClienChatConnection.start();
        }
        catch (final Exception ex11) {}
        try {
            RadioList.setup();
        }
        catch (final Exception ex12) {}
        try {
            this.ircCmdManager.addCommands();
        }
        catch (final Exception ex13) {}
        try {
            MCEF.init();
        }
        catch (final Exception e14) {
            e14.printStackTrace();
        }
    }
    
    public void onDisable() {
        try {
            ClientSettingsUtils.save();
        }
        catch (final Exception ex) {}
        try {
            DiscordMain.getInstance().shutdown();
        }
        catch (final Exception ex2) {}
        try {
            AltManager.saveAlts();
        }
        catch (final Exception ex3) {}
        try {
            InterClienChatConnection.stop();
        }
        catch (final Exception ex4) {}
        try {
            this.disconnectLabyMod();
        }
        catch (final Exception ex5) {}
        try {
            RadioList.disable();
        }
        catch (final Exception ex6) {}
        try {
            this.ircbot.disconnectBot();
        }
        catch (final Exception ex7) {}
    }
    
    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }
    
    public HologramManager getHologramManager() {
        return this.hologramManager;
    }
    
    public IrcCmdManager getIrcCmdManager() {
        return this.ircCmdManager;
    }
    
    public LabyMod getLabyMod() {
        return this.labyMod;
    }
    
    public void setLabyMod(final LabyMod labyMod) {
        this.labyMod = labyMod;
    }
    
    public void disconnectLabyMod() {
        if (this.labyMod != null) {
            try {
                this.labyMod.getLabyConnect().getClientConnection().disconnect(false);
            }
            catch (final Throwable t) {}
        }
    }
    
    public void connectLabyMod() {
        this.disconnectLabyMod();
        try {
            Minecraft.getMinecraft();
            final net.minecraft.util.Session mcSession = Minecraft.getSession();
            this.labyMod.login(new Session(mcSession.getUsername(), mcSession.getProfile().getId().toString(), mcSession.getToken(), "mojang"));
            this.labyMod.getLabyConnect().getClientConnection().connect();
        }
        catch (final Throwable t) {}
    }
    
    public void updateLabyMod() {
        this.labyMod.getLabyConnect().getClientConnection().setEventListener(new SessionListener() {
            @Override
            public void onPacketOut(final PacketEvent event) {
            }
            
            @Override
            public void onPacketIn(final PacketEvent event) {
                final Packet packet = event.getPacket();
                if (packet instanceof PacketMessage) {
                    final PacketMessage msg = (PacketMessage)packet;
                    Command.labyModMSG(String.valueOf(String.valueOf(new StringBuilder("§d").append(msg.getSender().getGameProfile().getName()).toString())) + " §8>" + " §f" + msg.getMessage(), true);
                }
                if (packet instanceof PacketPlayPlayerOnline) {
                    final PacketPlayPlayerOnline pack = (PacketPlayPlayerOnline)packet;
                    if (pack.getPlayer().isOnline()) {
                        NotificationManager.show(new Notification(NotificationType.INFO, "§d" + pack.getPlayer().getGameProfile().getName(), " §7is now §aOnline", 5));
                    }
                }
                if (packet instanceof PacketPlayPlayerOnline) {
                    final PacketPlayPlayerOnline pack2 = (PacketPlayPlayerOnline)packet;
                    if (!pack2.getPlayer().isOnline()) {
                        NotificationManager.show(new Notification(NotificationType.INFO, "§d" + pack2.getPlayer().getGameProfile().getName(), " §7is now §cOffline", 5));
                    }
                }
                if (packet instanceof PacketPlayFriendPlayingOn) {
                    final PacketPlayFriendPlayingOn pack3 = (PacketPlayFriendPlayingOn)packet;
                    if (pack3.getGameModeName() != null && !pack3.getGameModeName().isEmpty()) {
                        NotificationManager.show(new Notification(NotificationType.INFO, "§d" + pack3.getPlayer().getGameProfile().getName() + " §7play on", "§d" + pack3.getGameModeName(), 5));
                    }
                }
            }
            
            @Override
            public void onDisconnected(final boolean kicked, final String lastKickMessage) {
            }
            
            @Override
            public void onConnected() {
            }
        });
    }
}
