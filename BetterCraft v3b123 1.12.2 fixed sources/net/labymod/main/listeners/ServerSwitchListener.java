// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.main.listeners;

import net.labymod.support.util.Debug;
import java.util.List;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.labymod.utils.ModUtils;
import java.util.Arrays;
import com.google.gson.JsonElement;
import net.labymod.main.LabyMod;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.gui.GuiSwitchServer;
import net.labymod.utils.ServerData;
import net.labymod.utils.Consumer;
import net.labymod.api.events.ServerMessageEvent;

public class ServerSwitchListener implements ServerMessageEvent, Consumer<ServerData>, GuiSwitchServer.Result, MessageSendEvent
{
    private LabyMod labymod;
    private String destinationAddressToTrust;
    private boolean sessionTrusted;
    
    public ServerSwitchListener(final LabyMod labymod) {
        this.sessionTrusted = false;
        this.labymod = labymod;
    }
    
    @Override
    public void onServerMessage(final String messageKey, final JsonElement serverMessage) {
        if (messageKey.equals("server_switch")) {
            final JsonObject obj = serverMessage.getAsJsonObject();
            if (obj.has("title") && obj.has("address")) {
                final String title = obj.get("title").getAsString();
                final String address = obj.get("address").getAsString();
                final boolean preview = obj.has("preview") && obj.get("preview").getAsBoolean();
                if (this.labymod.getCurrentServerData() != null) {
                    if (this.sessionTrusted) {
                        this.notify(address, true, true);
                        this.labymod.switchServer(address, true);
                        return;
                    }
                    final List<String> list = Arrays.asList(LabyMod.getSettings().trustedServers);
                    if (list.contains(ModUtils.getProfileNameByIp(this.labymod.getCurrentServerData().getIp()))) {
                        this.notify(address, true, true);
                        this.labymod.switchServer(address, true);
                        return;
                    }
                }
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        Minecraft.getMinecraft().displayGuiScreen(new GuiSwitchServer(title, address, preview, ServerSwitchListener.this));
                    }
                });
            }
        }
    }
    
    @Override
    public void notify(final String address, final boolean accepted, final boolean trusted) {
        if (accepted) {
            this.destinationAddressToTrust = address;
        }
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("accepted", accepted);
        this.labymod.getLabyModAPI().sendJsonMessageToServer("server_switch", jsonObject);
    }
    
    @Override
    public void accept(final ServerData accepted) {
        if (accepted == null) {
            return;
        }
        if (this.destinationAddressToTrust != null && accepted.getIp().equalsIgnoreCase(this.destinationAddressToTrust)) {
            this.sessionTrusted = true;
        }
        else {
            this.sessionTrusted = false;
            this.destinationAddressToTrust = null;
        }
    }
    
    @Override
    public boolean onSend(final String msg) {
        if (Debug.isActive() && msg.startsWith("/connect") && msg.contains(" ")) {
            final String ip = msg.split(" ")[1];
            final JsonObject object = new JsonObject();
            final String s = " ";
            new Thread(() -> {
                try {
                    Thread.sleep(10L);
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        serverMessage.addProperty("title", "LabyMod Server Switcher");
                        serverMessage.addProperty("address", " ");
                        serverMessage.addProperty("preview", true);
                        this.onServerMessage("server_switch", serverMessage);
                    });
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
                return;
            }).start();
            return true;
        }
        return false;
    }
}
