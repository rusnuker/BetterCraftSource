// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.labymod.labyconnect.log.SingleChat;
import java.util.UUID;
import net.labymod.labyconnect.user.ChatUser;
import java.util.ArrayList;
import java.util.Random;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.labymod.labyconnect.log.MessageChatComponent;
import net.labymod.labyconnect.packets.PacketPlayFriendRemove;
import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketPlayRequestAddFriend;
import net.minecraft.client.Minecraft;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.labymod.utils.UUIDFetcher;
import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class LabymodCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.labyModMSG("§m§8----------§r §5LabyMod §m§8----------", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod connect", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod disconnect", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod reconnect", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod friends", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod status", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod info <player>", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod join <player>", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod add <player>", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod remove <player>", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod msg <player> <message>", true);
            Command.labyModMSG("", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod fake", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod spammer1", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod spammer2", true);
            Command.labyModMSG("§d" + CommandManager.syntax + "labymod spammer3", true);
            Command.labyModMSG("§m§8----------§r §5LabyMod §m§8----------", true);
            return;
        }
        if (args.length != 1) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("info")) {
                    if (Client.getInstance().getLabyMod() == null) {
                        Command.labyModMSG("You are §dnot connected §7with §dLabymod", true);
                        return;
                    }
                    try {
                        final UUID uuid = UUIDFetcher.getUUID(args[1]);
                        if (uuid == null) {
                            Command.labyModMSG("This §dplayer §7doesnt exist", true);
                            return;
                        }
                        final ChatUser user3 = Client.getInstance().getLabyMod().getLabyConnect().getChatUserByUUID(uuid);
                        if (user3 == null) {
                            Command.labyModMSG("This §dplayer §7is not your §dfriend", true);
                            return;
                        }
                        Command.labyModMSG("§m§8----------§r §5LabyMod §m§8----------", true);
                        Command.labyModMSG("§dInfo from: §5" + user3.getGameProfile().getName(), true);
                        Command.labyModMSG("", true);
                        if (user3.getCurrentServerInfo() != null) {
                            Command.labyModMSG("§dServer: §5" + user3.getCurrentServerInfo().getDisplayAddress(), true);
                        }
                        Command.labyModMSG("§dFirst Joined: §5" + new SimpleDateFormat("dd.MM.yyyy").format(new Date(user3.getFirstJoined())), true);
                        Command.labyModMSG("§dLast Interaction: §5" + new SimpleDateFormat("dd.MM.yyyy").format(new Date(user3.getLastInteraction())), true);
                        Command.labyModMSG("§dLast Online: §5" + new SimpleDateFormat("ss:mm:HH - dd.MM.yyyy").format(new Date(user3.getLastOnline())), true);
                        Command.labyModMSG("§dContact Amount: §5" + user3.getContactAmount(), true);
                        Command.labyModMSG("§dUnread Messages: §5" + user3.getUnreadMessages(), true);
                        Command.labyModMSG("§dTime Zone: §5" + user3.getTimeZone(), true);
                        Command.labyModMSG("§dStatus Message: §5" + user3.getStatusMessage(), true);
                        Command.labyModMSG("§m§8----------§r §5LabyMod §m§8----------", true);
                        return;
                    }
                    catch (final Throwable t) {
                        Command.labyModMSG("This §dplayer §7doesnt exist", true);
                        return;
                    }
                }
                if (args[0].equalsIgnoreCase("join")) {
                    if (Client.getInstance().getLabyMod() == null) {
                        Command.labyModMSG("You are §dnot connected §7with §dLabymod", true);
                        return;
                    }
                    try {
                        final UUID uuid = UUIDFetcher.getUUID(args[1]);
                        if (uuid == null) {
                            Command.labyModMSG("This §dplayer §7doesnt exist", true);
                            return;
                        }
                        final ChatUser user3 = Client.getInstance().getLabyMod().getLabyConnect().getChatUserByUUID(uuid);
                        if (user3 == null) {
                            Command.labyModMSG("This §dplayer §7is not your §dfriend", true);
                            return;
                        }
                        if (user3.getCurrentServerInfo() != null) {
                            Minecraft.getMinecraft().player.sendChatMessage(".connect " + user3.getCurrentServerInfo().getServerIp() + ":" + user3.getCurrentServerInfo().getServerPort());
                            return;
                        }
                        Command.labyModMSG("This §dplayer §7is not on a §dserver", true);
                        return;
                    }
                    catch (final Throwable t) {
                        Command.labyModMSG("This §dplayer §7doesnt exist", true);
                        return;
                    }
                }
                if (args[0].equalsIgnoreCase("add")) {
                    if (Client.getInstance().getLabyMod() == null) {
                        Command.labyModMSG("You are §dnot connected §7with §dLabymod", true);
                        return;
                    }
                    Client.getInstance().getLabyMod().getLabyConnect().getClientConnection().sendPacket(new PacketPlayRequestAddFriend(args[1]));
                    Command.labyModMSG("Friend request §dsended", true);
                    return;
                }
                else {
                    if (args[0].equalsIgnoreCase("remove")) {
                        try {
                            final UUID uuid = UUIDFetcher.getUUID(args[1]);
                            if (uuid == null) {
                                Command.labyModMSG("This §dplayer §7doesnt exist", true);
                                return;
                            }
                            final ChatUser user3 = Client.getInstance().getLabyMod().getLabyConnect().getChatUserByUUID(uuid);
                            if (user3 == null) {
                                Command.labyModMSG("This §dplayer §7is not your §dfriend", true);
                                return;
                            }
                            Client.getInstance().getLabyMod().getLabyConnect().getClientConnection().sendPacket(new PacketPlayFriendRemove(user3));
                            Command.labyModMSG("Friend §dremoved", true);
                            return;
                        }
                        catch (final Throwable t) {
                            Command.labyModMSG("This §dplayer §7doesnt exist", true);
                            return;
                        }
                    }
                    if (!args[0].equalsIgnoreCase("accept")) {
                        return;
                    }
                    try {
                        final UUID uuid = UUIDFetcher.getUUID(args[1]);
                        if (uuid == null) {
                            Command.labyModMSG("This §dplayer §7doesnt exist", true);
                            return;
                        }
                        final ChatUser user3 = Client.getInstance().getLabyMod().getLabyConnect().getChatUserByUUID(uuid);
                        if (user3 == null) {
                            Command.labyModMSG("This §dplayer §7is not your §dfriend", true);
                            return;
                        }
                        Client.getInstance().getLabyMod().getLabyConnect().getClientConnection().sendPacket(new PacketPlayRequestAddFriend(user3.getGameProfile().getName()));
                        Command.labyModMSG("Friend §daccepted", true);
                        return;
                    }
                    catch (final Throwable t) {
                        Command.labyModMSG("This §dplayer §7doesnt exist", true);
                        return;
                    }
                }
            }
            if (args.length > 2) {
                if (args[0].equalsIgnoreCase("msg") && Client.getInstance().getLabyMod() == null) {
                    Command.labyModMSG("You are §dnot connected §7with §dLabymod", true);
                    return;
                }
                try {
                    final UUID uuid = UUIDFetcher.getUUID(args[1]);
                    if (uuid == null) {
                        Command.labyModMSG("This §dplayer §7doesnt exist", true);
                        return;
                    }
                    final ChatUser user3 = Client.getInstance().getLabyMod().getLabyConnect().getChatUserByUUID(uuid);
                    if (user3 == null) {
                        Command.labyModMSG("This §dplayer §7is not your §dfriend", true);
                        return;
                    }
                    String msg = "";
                    for (int j = 2; j < args.length; ++j) {
                        msg = String.valueOf(String.valueOf(msg)) + " " + args[j];
                    }
                    msg = msg.substring(1);
                    final SingleChat chat = Client.getInstance().getLabyMod().getLabyConnect().getChatlogManager().getChat(user3);
                    chat.addMessage(new MessageChatComponent(Client.getInstance().getLabyMod().getGameProfile().getName(), System.currentTimeMillis(), msg));
                    Command.labyModMSG("Message §dsended", true);
                }
                catch (final Throwable t) {
                    Command.labyModMSG("This §dplayer §7doesnt exist", true);
                }
            }
            return;
        }
        if (args[0].equalsIgnoreCase("fake")) {
            final PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeString("BetterCraft");
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("LMC", buffer));
            Command.labyModMSG("Faking...", true);
            return;
        }
        if (args[0].equalsIgnoreCase("spammer1")) {
            final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.writeString("INFO");
            final JsonObject object = new JsonObject();
            final JsonObject ccpObject = new JsonObject();
            ccpObject.addProperty("version", "You just got fucked.");
            object.add("ccp", ccpObject);
            packetBuffer.writeString(object.toString());
            final CPacketCustomPayload packetCustomPayload = new CPacketCustomPayload("LMC", packetBuffer);
            Minecraft.getMinecraft().player.connection.sendPacket(packetCustomPayload);
            Command.labyModMSG("Spamming...", true);
            return;
        }
        if (args[0].equalsIgnoreCase("spammer2")) {
            final String str = String.valueOf(new Random().nextInt(999999999));
            final JsonObject object = new JsonObject();
            final JsonObject ccpObject = new JsonObject();
            ccpObject.addProperty("version", str);
            object.add("ccp", ccpObject);
            final PacketBuffer packetBuffer2 = new PacketBuffer(Unpooled.buffer());
            packetBuffer2.writeString("INFO");
            packetBuffer2.writeString(object.toString());
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("LMC", packetBuffer2));
            try {
                Thread.sleep(10L);
            }
            catch (final Exception ex) {}
            Command.labyModMSG("Spamming...", true);
            return;
        }
        if (args[0].equalsIgnoreCase("spammer3")) {
            final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.writeString("INFO");
            final JsonObject object = new JsonObject();
            final JsonObject ccpObject = new JsonObject();
            ccpObject.addProperty("version", "Unknown parameter passed at @NotNull with en_US in ServerListPing.java");
            object.add("ccp", ccpObject);
            packetBuffer.writeString(object.toString());
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("LMC", packetBuffer));
            Command.labyModMSG("Spamming...", true);
            return;
        }
        if (args[0].equalsIgnoreCase("disconnect")) {
            if (Client.getInstance().getLabyMod() == null) {
                Command.labyModMSG("You are §dnot connected §7with §dLabymod", true);
                return;
            }
            Client.getInstance().disconnectLabyMod();
            Command.labyModMSG("You are §ddisconnect §7from §dLabymod", true);
        }
        else {
            if (args[0].equalsIgnoreCase("connect")) {
                Client.getInstance().connectLabyMod();
                Command.labyModMSG("You are §dconnected §7with §dLabymod", true);
                return;
            }
            if (args[0].equalsIgnoreCase("reconnect")) {
                Client.getInstance().disconnectLabyMod();
                Client.getInstance().connectLabyMod();
                Command.labyModMSG("You are §dreconnected §7this §dLabymod", true);
                return;
            }
            if (!args[0].equalsIgnoreCase("friends")) {
                if (args[0].equalsIgnoreCase("status")) {
                    if (Client.getInstance().getLabyMod() == null) {
                        Command.labyModMSG("You are §dnot connected §7with §dLabymod", true);
                        return;
                    }
                    Command.labyModMSG("You are §dconnected §7with §dLabymod", true);
                }
                return;
            }
            if (Client.getInstance().getLabyMod() == null) {
                Command.labyModMSG("You are §dnot connected §7with §dLabymod", true);
                return;
            }
            final ArrayList<ChatUser> online = new ArrayList<ChatUser>();
            final ArrayList<ChatUser> offline = new ArrayList<ChatUser>();
            for (int i = 0; i < Client.getInstance().getLabyMod().getLabyConnect().getFriends().size(); ++i) {
                final ChatUser user4 = Client.getInstance().getLabyMod().getLabyConnect().getFriends().get(i);
                if (user4.isOnline()) {
                    online.add(user4);
                }
                else {
                    offline.add(user4);
                }
            }
            Command.labyModMSG("§m§8----------§r §5LabyMod §m§8----------", true);
            Command.labyModMSG("§dFriends: §5" + (offline.size() + online.size()), true);
            Command.labyModMSG("", true);
            Command.labyModMSG("§dOffline: §5" + offline.size(), true);
            offline.forEach(user -> Command.labyModMSG("     §d" + user.getGameProfile().getName(), true));
            Command.labyModMSG("", true);
            Command.labyModMSG("§dOnline: §5" + online.size(), true);
            online.forEach(user -> Command.labyModMSG("     §d" + user.getGameProfile().getName(), true));
            Command.labyModMSG("§m§8----------§r §5LabyMod §m§8----------", true);
        }
    }
    
    @Override
    public String getName() {
        return "labymod";
    }
}
