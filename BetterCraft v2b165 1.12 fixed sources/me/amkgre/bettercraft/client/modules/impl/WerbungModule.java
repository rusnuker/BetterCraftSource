// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.modules.impl;

import com.darkmagician6.eventapi.EventManager;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.client.network.NetworkPlayerInfo;
import me.amkgre.bettercraft.client.events.ChatMessageSendEvent;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.amkgre.bettercraft.client.commands.impl.WerbungCommand;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.events.EventUpdate;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import me.amkgre.bettercraft.client.modules.Module;

public class WerbungModule extends Module
{
    private int Types;
    Long time;
    List<String> players;
    List<String> accepted;
    int i;
    int update;
    private Random random;
    String[] msg;
    
    public WerbungModule() {
        super("Werbung", "Werbung", 0, Type.OTHER);
        this.Types = 0;
        this.time = System.currentTimeMillis() + 10010L;
        this.players = new ArrayList<String>();
        this.accepted = new ArrayList<String>();
        this.i = 0;
        this.update = 0;
        this.random = new Random();
        this.msg = new String[] { "/msg ", "/mSg ", "/msG ", "/Msg ", "/MSg ", "/MSG " };
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        if (this.mc.getCurrentServerData().serverIP.contains("rewinside")) {
            this.Types = 2;
        }
        else if (this.mc.getCurrentServerData().serverIP.contains("twerion")) {
            this.Types = 5;
        }
        else if (this.mc.getCurrentServerData().serverIP.contains("antiac")) {
            this.Types = 5;
        }
        else if (!this.mc.getCurrentServerData().serverIP.contains("nzxter.tk") && System.currentTimeMillis() > this.time) {
            if (this.players.size() != 0) {
                if (this.i > this.players.size() - 1) {
                    this.updateList();
                    this.time = System.currentTimeMillis() + 1000L;
                    return;
                }
                final int i = this.random.nextInt(this.msg.length);
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage(String.valueOf(this.msg[i]) + this.players.get(this.i) + " " + WerbungCommand.msg));
                this.time = System.currentTimeMillis() + 1000L;
                ++this.i;
            }
            else {
                this.time = System.currentTimeMillis() + 1000L;
            }
            ++this.update;
            if (this.update >= 10) {
                this.update = 0;
                this.updateList();
            }
        }
        if (System.currentTimeMillis() > this.time && this.players.size() != 0) {
            if (this.i > this.players.size() - 1) {
                this.updateList();
                this.time = System.currentTimeMillis() + 2002L;
                return;
            }
            if (this.accepted.size() != 0) {
                final String name = this.accepted.get(0);
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage(String.valueOf(this.msg[this.i]) + name + " " + WerbungCommand.msg));
                this.accepted.remove(name);
                this.time = System.currentTimeMillis() + 2002L;
                return;
            }
            if (!this.mc.getCurrentServerData().serverIP.contains("nzxter.tk")) {
                if (this.i >= 200 && this.i % 200 == 0) {
                    Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/friend clear"));
                }
                else {
                    Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/friend add " + this.players.get(this.i)));
                }
                this.time = System.currentTimeMillis() + 2002L;
                ++this.i;
            }
            else {
                this.time = System.currentTimeMillis() + 2002L;
            }
            this.updateList();
        }
    }
    
    @EventTarget
    public void onChat(final ChatMessageSendEvent ec) {
        if (ec.getMessage().contains("hat deine Freundschaftsanfrage angenommen.") || ec.getMessage().contains("befreundet :)") || ec.getMessage().contains("befreundet.")) {
            final String name = ec.getMessage().split(" ")[this.Types];
            this.accepted.add(name);
        }
    }
    
    public void updateList() {
        int count = 0;
        final Collection<NetworkPlayerInfo> playersC = Minecraft.getMinecraft().getConnection().getPlayerInfoMap();
        for (final NetworkPlayerInfo i : playersC) {
            final String name = i.getGameProfile().getName();
            if (this.players.contains(name)) {
                continue;
            }
            this.players.add(name);
            ++count;
        }
    }
    
    @Override
    public void onEnable() {
        this.time = System.currentTimeMillis() + 2002L;
        final Collection<NetworkPlayerInfo> playersC = Minecraft.getMinecraft().getConnection().getPlayerInfoMap();
        for (final NetworkPlayerInfo i : playersC) {
            final String name = i.getGameProfile().getName();
            this.players.add(name);
        }
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        this.players.clear();
        this.i = 0;
        EventManager.unregister(this);
    }
}
