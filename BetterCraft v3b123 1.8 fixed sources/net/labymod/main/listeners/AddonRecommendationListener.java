// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.main.listeners;

import net.labymod.support.util.Debug;
import java.util.Iterator;
import net.labymod.addon.online.info.AddonInfo;
import net.labymod.addon.AddonLoader;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.labymod.addon.online.AddonInfoManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import net.labymod.utils.ModUtils;
import java.util.Arrays;
import net.labymod.addon.online.info.OnlineAddonInfo;
import java.util.UUID;
import net.labymod.gui.GuiAddonRecommendation;
import java.util.ArrayList;
import com.google.gson.JsonElement;
import net.labymod.main.LabyMod;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.api.events.ServerMessageEvent;

public class AddonRecommendationListener implements ServerMessageEvent, MessageSendEvent
{
    private LabyMod labymod;
    
    public AddonRecommendationListener(final LabyMod labymod) {
        this.labymod = labymod;
    }
    
    @Override
    public void onServerMessage(final String messageKey, final JsonElement serverMessage) {
        if (messageKey.equals("addon_recommendation")) {
            final List<GuiAddonRecommendation.RecommendedAddon> addons = new ArrayList<GuiAddonRecommendation.RecommendedAddon>();
            final JsonObject obj = serverMessage.getAsJsonObject();
            if (obj.has("addons")) {
                final JsonArray array = obj.get("addons").getAsJsonArray();
                for (int i = 0; i < array.size(); ++i) {
                    final JsonObject addonObject = array.get(i).getAsJsonObject();
                    final UUID uuid = UUID.fromString(addonObject.get("uuid").getAsString());
                    final boolean required = addonObject.get("required").getAsBoolean();
                    final GuiAddonRecommendation.RecommendedAddon addon = new GuiAddonRecommendation.RecommendedAddon(uuid, required, null);
                    addons.add(addon);
                }
            }
            if (!addons.isEmpty()) {
                if (this.labymod.getCurrentServerData() != null) {
                    final List<String> list = Arrays.asList(LabyMod.getSettings().ignoredAddonRecommendationServers);
                    if (list.contains(ModUtils.getProfileNameByIp(this.labymod.getCurrentServerData().getIp()))) {
                        sendMissingStatus(addons, true);
                        return;
                    }
                }
                this.openGui(addons);
            }
        }
    }
    
    private void openGui(final List<GuiAddonRecommendation.RecommendedAddon> addons) {
        final AddonInfoManager manager = AddonInfoManager.getInstance();
        manager.init();
        if (sendMissingStatus(addons, false)) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiAddonRecommendation(addons));
                }
            });
        }
    }
    
    public static boolean sendMissingStatus(final List<GuiAddonRecommendation.RecommendedAddon> addons, final boolean isGuiClosed) {
        final AddonInfoManager manager = AddonInfoManager.getInstance();
        final JsonArray missingArray = new JsonArray();
        boolean addonIsMissing = false;
        for (final GuiAddonRecommendation.RecommendedAddon addon : addons) {
            addon.bindAddon(manager);
            if (addon.getOnlineAddonInfo() != null && !AddonLoader.hasInstalled(addon.getOnlineAddonInfo())) {
                addonIsMissing = true;
                final JsonObject addonObject = new JsonObject();
                addonObject.addProperty("uuid", addon.getUuid().toString());
                addonObject.addProperty("name", addon.getOnlineAddonInfo().getName());
                missingArray.add(addonObject);
            }
        }
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("gui_closed", isGuiClosed);
        jsonObject.addProperty("all_installed", !addonIsMissing);
        if (addonIsMissing) {
            jsonObject.add("missing", missingArray);
        }
        LabyMod.getInstance().getLabyModAPI().sendJsonMessageToServer("addon_recommendation", jsonObject);
        return addonIsMissing;
    }
    
    @Override
    public boolean onSend(final String msg) {
        if (Debug.isActive() && msg.startsWith("/recommend")) {
            final JsonObject object = new JsonObject();
            final JsonArray addons = new JsonArray();
            final JsonObject addon = new JsonObject();
            final JsonObject addon2 = new JsonObject();
            new Thread(() -> {
                try {
                    Thread.sleep(10L);
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        element.addProperty("uuid", "2485081f-b50a-47b8-82ed-6aed3952f8ab");
                        element.addProperty("required", false);
                        value.add(element);
                        element2.addProperty("uuid", "3bd2a3c0-0309-47d4-a6f9-320b0e93bce1");
                        element2.addProperty("required", true);
                        value.add(element2);
                        serverMessage.add("addons", value);
                        this.onServerMessage("addon_recommendation", serverMessage);
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
