// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client;

import me.nzxtercode.bettercraft.client.mods.ModInstances;
import me.nzxtercode.bettercraft.client.hud.HUDManager;
import me.nzxtercode.bettercraft.client.commands.CommandManager;
import org.javapluginapi.team.JavaPluginApi;
import me.nzxtercode.bettercraft.client.misc.Tor;
import me.nzxtercode.bettercraft.client.misc.altmanager.impl.AccountManager;
import lumien.chunkanimator.ChunkAnimator;
import com.TominoCZ.FBP.FBP;
import me.nzxtercode.bettercraft.client.misc.irc.IRC;
import net.labymod.main.LabyMod;
import viamcp.ViaMCP;
import net.minecraft.client.Minecraft;

public class BetterCraft
{
    private static final Minecraft mc;
    public static String clientAuthor;
    public static String clientName;
    public static String clientVersion;
    public static String clientBuild;
    public static int clientVersionCheck;
    public static int clientBuildCheck;
    public static String clientPrefix;
    private static final BetterCraft INSTANCE;
    
    static {
        mc = Minecraft.getMinecraft();
        BetterCraft.clientAuthor = "Nzxter";
        BetterCraft.clientName = "BetterCraft";
        BetterCraft.clientVersion = "v3";
        BetterCraft.clientBuild = "b120";
        BetterCraft.clientVersionCheck = 3;
        BetterCraft.clientBuildCheck = 120;
        BetterCraft.clientPrefix = "§8[§f" + BetterCraft.clientName + "§8] §7";
        INSTANCE = new BetterCraft();
    }
    
    public static BetterCraft getInstance() {
        return BetterCraft.INSTANCE;
    }
    
    public void initialize() {
        Config.getInstance();
        Config.init();
        ViaMCP.getInstance();
        ViaMCP.init();
        LabyMod.getInstance().init();
        DiscordRP.getInstance().init();
        IRC.getInstance().init();
        FBP.getInstance().init();
        ChunkAnimator.getInstance().init();
        AccountManager.getInstance().init();
        Tor.getInstance();
        Tor.init();
        JavaPluginApi.getInstance();
        JavaPluginApi.init();
        CommandManager.getInstance().addCommands();
        ModInstances.register(HUDManager.getInstance());
    }
    
    public void terminate() {
        DiscordRP.getInstance().shutdown();
        AccountManager.getInstance().save();
        Tor.getInstance();
        Tor.stopTor();
        JavaPluginApi.getInstance();
        JavaPluginApi.stopPlugin();
    }
}
