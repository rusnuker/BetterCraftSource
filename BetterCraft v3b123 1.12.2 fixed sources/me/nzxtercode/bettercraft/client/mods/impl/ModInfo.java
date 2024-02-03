// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.mods.impl;

import net.minecraft.client.gui.FontRenderer;
import me.nzxtercode.bettercraft.client.misc.irc.IRC;
import net.minecraft.block.Block;
import me.nzxtercode.bettercraft.client.utils.TimeHelperUtils;
import me.nzxtercode.bettercraft.client.utils.ProtocolVersionUtils;
import net.minecraft.client.Minecraft;
import me.nzxtercode.bettercraft.client.utils.ColorUtils;
import me.nzxtercode.bettercraft.client.hud.ScreenPosition;
import me.nzxtercode.bettercraft.client.mods.ModRender;

public class ModInfo extends ModRender
{
    @Override
    public int getWidth() {
        return 0;
    }
    
    @Override
    public int getHeight() {
        return 0;
    }
    
    @Override
    public void render(final ScreenPosition pos) {
        try {
            this.font.drawString("Network Settings", 5, 5, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Remote Adresse: §r" + this.mc.getNetHandler().getNetworkManager().getRemoteAddress(), 10, 15, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Server Brand: §r" + this.mc.thePlayer.getClientBrand().replaceAll("git:", "").replaceAll("Bootstrap", "").replaceAll("SNAPSHOT", "").replaceAll(":", "").replaceAll("SkillCord ", "").replaceAll("MelonBungee ", "").replaceAll("unknown", "").replaceAll("<- Wer das liest ist dumm", "") + " " + ((Minecraft.getMinecraft().getCurrentServerData().gameVersion.split(" ").length > 1) ? (Minecraft.getMinecraft().getCurrentServerData().gameVersion.split(" ")[1].startsWith("1.") ? Minecraft.getMinecraft().getCurrentServerData().gameVersion.split(" ")[1] : ProtocolVersionUtils.getKnownAs(Minecraft.getMinecraft().getCurrentServerData().version)) : ProtocolVersionUtils.getKnownAs(Minecraft.getMinecraft().getCurrentServerData().version)), 10, 25, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Ticks per second: §r" + Math.round(TimeHelperUtils.lastTps * 10.0) / 10.0), 10, 35, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Lag Meter: §r" + TimeHelperUtils.getFormattedLag()).replaceAll("Lag Meter: §r0", "Lag Meter: §rNo Lag"), 10, 45, ColorUtils.rainbowEffect());
            this.font.drawString("World Settings", 5, 65, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Spawn: §r" + this.mc.theWorld.getSpawnPoint().toString().replaceAll("BlockPos", ""), 10, 75, ColorUtils.rainbowEffect());
            try {
                final FontRenderer font = this.font;
                final StringBuilder append = new StringBuilder("§f-> Block: §r").append(this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock().getLocalizedName().replaceAll("tile.air.name", "Air").replaceAll("tile.skull.skeleton.name", "Skull")).append(", ");
                this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock();
                font.drawString(append.append(Block.getIdFromBlock(this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock())).toString(), 10, 85, ColorUtils.rainbowEffect());
            }
            catch (final Exception e) {
                this.font.drawString("§f-> Block: §rEntity", 10, 85, ColorUtils.rainbowEffect());
            }
            this.font.drawString("§f-> Biom: §r" + this.mc.theWorld.getBiomeGenForCoords(this.mc.thePlayer.getPosition()).biomeName, 10, 95, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Time: §r" + String.format("%02d:%02d", (int)((Math.floor(this.mc.theWorld.getWorldInfo().getWorldTime() / 1000.0) + 6.0) % 24.0), (int)Math.floor(this.mc.theWorld.getWorldInfo().getWorldTime() % 1000L / 1000.0 * 60.0))), 10, 105, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Entitys: §r" + this.mc.theWorld.loadedEntityList.size()), 10, 115, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Online: §r" + this.mc.getNetHandler().getPlayerInfoMap().size()), 10, 125, ColorUtils.rainbowEffect());
            this.font.drawString("Chat Settings", 5, 145, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Status: §r" + (IRC.getInstance().isUserConnected(this.mc.getSession().getUsername()) ? "Online" : "Offline"), 10, 155, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Users: §r" + IRC.getInstance().getUsers(IRC.getInstance().currentChannel).length), 10, 165, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Chat: §r" + IRC.getInstance().getUnreadMessages()), 10, 175, ColorUtils.rainbowEffect());
            this.font.drawString("Misc Settings", 5, 195, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Name: §r" + this.mc.getSession().getUsername(), 10, 205, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Ram: §r" + (Object)(Runtime.getRuntime().freeMemory() / 1024L / 1024L) + " / " + (Object)(Runtime.getRuntime().maxMemory() / 1024L / 1024L) + " MB", 10, 215, ColorUtils.rainbowEffect());
        }
        catch (final Exception e) {
            this.font.drawString("Network Settings", 5, 5, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Remote Adresse: §rReading...", 10, 15, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Server Brand: §rReading...", 10, 25, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Ticks per second: §rReading..."), 10, 35, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Lag Meter: §rReading..."), 10, 45, ColorUtils.rainbowEffect());
            this.font.drawString("World Settings", 5, 65, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Spawn: §rReading...", 10, 75, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Block: §rReading...", 10, 85, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Biom: §rReading...", 10, 95, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Time: §rReading..."), 10, 105, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Entitys: §rReading..."), 10, 115, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Online: §rReading..."), 10, 125, ColorUtils.rainbowEffect());
            this.font.drawString("Chat Settings", 5, 145, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Status: §rReading...", 10, 155, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Users: §rReading..."), 10, 165, ColorUtils.rainbowEffect());
            this.font.drawString(String.valueOf("§f-> Chat: §rReading..."), 10, 175, ColorUtils.rainbowEffect());
            this.font.drawString("Misc Settings", 5, 195, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Name: §rReading...", 10, 205, ColorUtils.rainbowEffect());
            this.font.drawString("§f-> Ram: §rReading...", 10, 215, ColorUtils.rainbowEffect());
        }
    }
}
