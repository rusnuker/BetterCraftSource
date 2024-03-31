// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ui;

import net.montoyo.mcef.example.ExampleMod;
import me.amkgre.bettercraft.client.mods.notifications.NotificationManager;
import me.amkgre.bettercraft.client.mods.clientchat.InterClienChatConnection;
import me.amkgre.bettercraft.client.utils.TimeHelperUtils;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import me.amkgre.bettercraft.client.gui.GuiClientUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class UIRender extends Gui
{
    private static Minecraft mc;
    
    static {
        UIRender.mc = Minecraft.getMinecraft();
    }
    
    public static void draw() {
        if (GuiClientUI.networksettings) {
            ColorUtils.drawChromaString(String.valueOf("Network Settings"), 1, 1, true);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Remote Adresse: " + UIRender.mc.getConnection().getNetworkManager().getRemoteAddress().toString()), 5, 10, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Server Brand: " + UIRender.mc.player.getServerBrand().replaceAll("git:", "").replaceAll("Bootstrap", "").replaceAll("SNAPSHOT", "").replaceAll(":", "").replaceAll("SkillCord ", "").replaceAll("MelonBungee ", "").replaceAll("unknown", "").replaceAll("<- Wer das liest ist dumm", "")), 5, 20, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Ticks per second: " + Math.round(TimeHelperUtils.lastTps * 10.0) / 10.0), 5, 30, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Lag Meter: " + TimeHelperUtils.getFormattedLag()).replaceAll("Lag Meter: 0", "Lag Meter: No Lag"), 5, 40, 16777215);
            ColorUtils.drawChromaString(String.valueOf("World Settings"), 1, 60, true);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Spawn: " + UIRender.mc.world.getSpawnPoint().toString().replaceAll("BlockPos", "")), 5, 70, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Biom: " + UIRender.mc.world.getBiome(UIRender.mc.player.getPosition()).getBiomeName()), 5, 80, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Time: " + String.format("%02d:%02d", (int)((Math.floor(UIRender.mc.world.getWorldInfo().getWorldTime() / 1000.0) + 6.0) % 24.0), (int)Math.floor(UIRender.mc.world.getWorldInfo().getWorldTime() % 1000L / 1000.0 * 60.0))), 5, 90, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Entitys: " + UIRender.mc.world.loadedEntityList.size()), 5, 110, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Online: " + UIRender.mc.getConnection().getPlayerInfoMap().size()), 5, 120, 16777215);
            ColorUtils.drawChromaString(String.valueOf("Player Settings"), 1, 140, true);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf(String.valueOf(new StringBuilder("» Coords: {x=").append(Math.round(UIRender.mc.player.posX)).append(", y=").append(Math.round(UIRender.mc.player.posY)).append(", z=").append(Math.round(UIRender.mc.player.posZ)).toString())) + "}", 5, 150, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» FPS: " + Minecraft.debugFPS), 5, 160, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Ping: " + Minecraft.getMinecraft().getConnection().getPlayerInfo(Minecraft.getMinecraft().player.getUniqueID()).getResponseTime()), 5, 170, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» CPS: " + CPS.getCPS()), 5, 180, 16777215);
            ColorUtils.drawChromaString(String.valueOf("Chat Settings"), 1, 200, true);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Name: " + Minecraft.getSession().getUsername()), 5, 210, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, "» Status: " + (InterClienChatConnection.hasLostConnection ? "§4Offline" : "§aOnline"), 5, 220, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf(String.valueOf(new StringBuilder("» Chat: §f(§c").append(InterClienChatConnection.msgs.size() - InterClienChatConnection.seenMsgs).toString())) + "§f)", 5, 230, 16777215);
            Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Users: §f(§d" + InterClienChatConnection.onlinePlayers.size() + "§f)"), 5, 240, 16777215);
            try {
                Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf(String.valueOf("» Block: ")) + UIRender.mc.world.getBlockState(UIRender.mc.objectMouseOver.getBlockPos()).getBlock().getLocalizedName().replaceAll("tile.air.name", "Air").replaceAll("tile.skull.skeleton.name", "Skull"), 5, 100, 16777215);
            }
            catch (final NullPointerException e) {
                Gui.drawString(UIRender.mc.fontRendererObj, String.valueOf("» Block: Entity"), 5, 100, 16777215);
            }
        }
        if (GuiClientUI.armorstatus) {
            ArmorStatus.render();
        }
        if (GuiClientUI.keystrokes) {
            Keystrokes.render();
        }
        if (GuiClientUI.skin) {
            Skin.render();
        }
        if (GuiClientUI.radar) {
            Radar.render();
        }
        if (GuiClientUI.uhr) {
            Uhr.render();
        }
        CPS.render();
        NotificationManager.render();
        if (ExampleMod.INSTANCE.hudBrowser != null) {
            ExampleMod.INSTANCE.hudBrowser.drawScreen(0, 0, 0.0f);
        }
    }
}
