// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client;

import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordRPC;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.gui.GuiMainMenu;
import java.util.TimerTask;
import java.util.Timer;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.minecraft.client.Minecraft;

public class DiscordRP
{
    private static final Minecraft mc;
    private static final DiscordRP INSTANCE;
    private boolean running;
    private long created;
    
    static {
        mc = Minecraft.getMinecraft();
        INSTANCE = new DiscordRP();
    }
    
    public DiscordRP() {
        this.running = true;
        this.created = 0L;
    }
    
    public static final DiscordRP getInstance() {
        return DiscordRP.INSTANCE;
    }
    
    public void init() {
        this.created = System.currentTimeMillis();
        final DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
            @Override
            public void apply(final DiscordUser user) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        final ServerData serverData = DiscordRP.mc.getCurrentServerData();
                        if (serverData == null || DiscordRP.mc.currentScreen instanceof GuiMainMenu) {
                            DiscordRP.this.update((DiscordRP.mc.thePlayer == null) ? "Main Menu" : ("Nick: " + Minecraft.session.getUsername()), "");
                        }
                        else {
                            DiscordRP.this.update((DiscordRP.mc.getCurrentServerData() == null) ? "Offline" : ("Server: " + DiscordRP.mc.getCurrentServerData().serverIP), "");
                        }
                    }
                }, 0L, 2000L);
            }
        }).build();
        DiscordRPC.discordInitialize("1075338424573231106", handlers, true);
        new Thread("Discord RPC Callback") {
            @Override
            public void run() {
                while (DiscordRP.this.running) {
                    DiscordRPC.discordRunCallbacks();
                }
            }
        }.start();
    }
    
    public void shutdown() {
        this.running = false;
        DiscordRPC.discordShutdown();
    }
    
    public void update(final String firstline, final String secondline) {
        final DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder(secondline);
        builder.setDetails(firstline);
        builder.setStartTimestamps(this.created);
        final DiscordRichPresence.Builder builder2 = builder;
        final String key = "1024";
        BetterCraft.getInstance();
        final StringBuilder append = new StringBuilder(String.valueOf(BetterCraft.clientVersion)).append(" ");
        BetterCraft.getInstance();
        builder2.setBigImage(key, append.append(BetterCraft.clientBuild).toString());
        final DiscordRichPresence.Builder builder3 = builder;
        final String key2 = "512";
        BetterCraft.getInstance();
        builder3.setSmallImage(key2, BetterCraft.clientAuthor);
        DiscordRPC.discordUpdatePresence(builder.build());
    }
}
