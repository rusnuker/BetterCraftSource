// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc;

import me.amkgre.bettercraft.client.Client;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordRPC;
import me.amkgre.bettercraft.client.mods.discord.rpc.events.ReceivePacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
import net.arikia.dev.drpc.DiscordEventHandlers;
import java.util.Iterator;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.CipherRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.ScreenShareRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.CounterStrikeRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.LiquidBounceRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.BlueStacksRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.PyCharmRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.EclipseRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.GrandTheftAutoVRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.LeagueOfLegendsRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.OsuRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.RainbowSixSiegeRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.LabyModRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.BadlionRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.GarrysModRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.ValorantRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.AmongUsRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.TerrariaRP;
import me.amkgre.bettercraft.client.mods.discord.rpc.impl.BetterCraftRP;

public class DiscordRPRegistry extends Registry<DiscordRP>
{
    private long created;
    private DiscordRPUser discordRPUser;
    
    public DiscordRPRegistry() {
        this.created = 0L;
        ((Registry<BetterCraftRP>)this).register(new BetterCraftRP());
        ((Registry<TerrariaRP>)this).register(new TerrariaRP());
        ((Registry<AmongUsRP>)this).register(new AmongUsRP());
        ((Registry<ValorantRP>)this).register(new ValorantRP());
        ((Registry<GarrysModRP>)this).register(new GarrysModRP());
        ((Registry<BadlionRP>)this).register(new BadlionRP());
        ((Registry<LabyModRP>)this).register(new LabyModRP());
        ((Registry<RainbowSixSiegeRP>)this).register(new RainbowSixSiegeRP());
        ((Registry<OsuRP>)this).register(new OsuRP());
        ((Registry<LeagueOfLegendsRP>)this).register(new LeagueOfLegendsRP());
        ((Registry<GrandTheftAutoVRP>)this).register(new GrandTheftAutoVRP());
        ((Registry<EclipseRP>)this).register(new EclipseRP());
        ((Registry<PyCharmRP>)this).register(new PyCharmRP());
        ((Registry<BlueStacksRP>)this).register(new BlueStacksRP());
        ((Registry<LiquidBounceRP>)this).register(new LiquidBounceRP());
        ((Registry<CounterStrikeRP>)this).register(new CounterStrikeRP());
        ((Registry<ScreenShareRP>)this).register(new ScreenShareRP());
        ((Registry<CipherRP>)this).register(new CipherRP());
    }
    
    public DiscordRP getRPByType(final DiscordRPType type) {
        for (final DiscordRP rp : this.getObjects()) {
            if (rp.getType() == type) {
                return rp;
            }
        }
        return null;
    }
    
    public DiscordRP getRunningRP() {
        for (final DiscordRP rp : this.getObjects()) {
            if (rp.isRunning()) {
                return rp;
            }
        }
        return null;
    }
    
    public DiscordRPUser getDiscordRPUser() {
        return this.discordRPUser;
    }
    
    public void startRPC(final DiscordRPType type) {
        this.created = System.currentTimeMillis();
        final DiscordRP rp = this.getRPByType(type);
        rp.setRunning(true);
        final DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
            @Override
            public void apply(final DiscordUser user) {
                DiscordRPRegistry.access$0(DiscordRPRegistry.this, new DiscordRPUser(user.avatar, user.discriminator, user.userId, user.username));
                DiscordRPRegistry.this.updateLeaveServerRPContent();
                DiscordMain.getInstance().getEventRegistry().registerOnReceivePacketEvent(event -> {
                    final Packet packet = event.getPacket();
                    if (packet instanceof SPacketJoinGame && !Minecraft.getMinecraft().isIntegratedServerRunning()) {
                        DiscordRPRegistry.this.updateJoinServerRPContent();
                    }
                    return;
                });
                DiscordMain.getInstance().getEventRegistry().registerLeaveServerEvent(event -> DiscordRPRegistry.this.updateLeaveServerRPContent());
            }
        }).build();
        DiscordRPC.discordInitialize(rp.getApplicationID(), handlers, true);
        new Thread("Discord RPC CallBack") {
            @Override
            public void run() {
                while (rp.isRunning()) {
                    DiscordRPC.discordRunCallbacks();
                }
            }
        }.start();
    }
    
    public void shutdownRPC() {
        final DiscordRP rp = this.getRunningRP();
        rp.setRunning(false);
        DiscordRPC.discordShutdown();
    }
    
    public void updateJoinServerRPContent() {
        final DiscordRP rp = this.getRunningRP();
        final DiscordRPContent joinServerContent = rp.getJoinServerContent();
        final DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder(joinServerContent.getSecondLine());
        builder.setBigImage("first_image", joinServerContent.getHoverImageFirstLine());
        if (rp.hasSmallImage()) {
            builder.setSmallImage("second_image", joinServerContent.getHoverImageSecondLine());
        }
        builder.setDetails(joinServerContent.getFirstLine());
        builder.setStartTimestamps(this.created);
        DiscordRPC.discordUpdatePresence(builder.build());
    }
    
    public void updateLeaveServerRPContent() {
        final DiscordRP rp = this.getRunningRP();
        final DiscordRPContent leaveServerContent = rp.getLeaveServerContent();
        final DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder(leaveServerContent.getSecondLine());
        builder.setBigImage("first_image", leaveServerContent.getHoverImageFirstLine());
        if (rp.hasSmallImage()) {
            builder.setSmallImage("second_image", leaveServerContent.getHoverImageSecondLine());
        }
        builder.setDetails(leaveServerContent.getFirstLine());
        builder.setStartTimestamps(this.created);
        DiscordRPC.discordUpdatePresence(builder.build());
    }
    
    public static int getRandomInt(final int min, final int max) {
        return (int)(min + Math.random() * (max - min + 1));
    }
    
    public void update(final String firstLine, final String secondLine) {
        final DiscordRichPresence.Builder builder2;
        final DiscordRichPresence.Builder builder = builder2 = new DiscordRichPresence.Builder(firstLine);
        final String key = "second_image";
        final StringBuilder sb = new StringBuilder("Made by ");
        Client.getInstance();
        builder2.setSmallImage(key, sb.append(Client.clientAuthor).toString());
        final DiscordRichPresence.Builder builder3 = builder;
        final String key2 = "first_image";
        Client.getInstance();
        builder3.setBigImage(key2, Client.clientVersion);
        builder.setDetails(firstLine);
        builder.setDetails(secondLine);
        builder.setStartTimestamps(this.created);
        DiscordRPC.discordUpdatePresence(builder.build());
    }
    
    static /* synthetic */ void access$0(final DiscordRPRegistry discordRPRegistry, final DiscordRPUser discordRPUser) {
        discordRPRegistry.discordRPUser = discordRPUser;
    }
}
