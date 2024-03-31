// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.realms;

import net.minecraft.nbt.NBTTagCompound;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.FileInputStream;
import java.io.File;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.GameSettings;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.world.GameType;
import net.minecraft.client.gui.GuiScreen;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.util.Session;
import java.net.Proxy;
import net.minecraft.client.Minecraft;

public class Realms
{
    public static boolean isTouchScreen() {
        return Minecraft.getMinecraft().gameSettings.touchscreen;
    }
    
    public static Proxy getProxy() {
        return Minecraft.getMinecraft().getProxy();
    }
    
    public static String sessionId() {
        Minecraft.getMinecraft();
        final Session session = Minecraft.getSession();
        return (session == null) ? null : session.getSessionID();
    }
    
    public static String userName() {
        Minecraft.getMinecraft();
        final Session session = Minecraft.getSession();
        return (session == null) ? null : session.getUsername();
    }
    
    public static long currentTimeMillis() {
        return Minecraft.getSystemTime();
    }
    
    public static String getSessionId() {
        Minecraft.getMinecraft();
        return Minecraft.getSession().getSessionID();
    }
    
    public static String getUUID() {
        Minecraft.getMinecraft();
        return Minecraft.getSession().getPlayerID();
    }
    
    public static String getName() {
        Minecraft.getMinecraft();
        return Minecraft.getSession().getUsername();
    }
    
    public static String uuidToName(final String p_uuidToName_0_) {
        return Minecraft.getMinecraft().getSessionService().fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString(p_uuidToName_0_), null), false).getName();
    }
    
    public static void setScreen(final RealmsScreen p_setScreen_0_) {
        Minecraft.getMinecraft().displayGuiScreen(p_setScreen_0_.getProxy());
    }
    
    public static String getGameDirectoryPath() {
        return Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
    }
    
    public static int survivalId() {
        return GameType.SURVIVAL.getID();
    }
    
    public static int creativeId() {
        return GameType.CREATIVE.getID();
    }
    
    public static int adventureId() {
        return GameType.ADVENTURE.getID();
    }
    
    public static int spectatorId() {
        return GameType.SPECTATOR.getID();
    }
    
    public static void setConnectedToRealms(final boolean p_setConnectedToRealms_0_) {
        Minecraft.getMinecraft().setConnectedToRealms(p_setConnectedToRealms_0_);
    }
    
    public static ListenableFuture<Object> downloadResourcePack(final String p_downloadResourcePack_0_, final String p_downloadResourcePack_1_) {
        return Minecraft.getMinecraft().getResourcePackRepository().downloadResourcePack(p_downloadResourcePack_0_, p_downloadResourcePack_1_);
    }
    
    public static void clearResourcePack() {
        Minecraft.getMinecraft().getResourcePackRepository().clearResourcePack();
    }
    
    public static boolean getRealmsNotificationsEnabled() {
        return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS);
    }
    
    public static boolean inTitleScreen() {
        Minecraft.getMinecraft();
        if (Minecraft.currentScreen != null) {
            Minecraft.getMinecraft();
            if (Minecraft.currentScreen instanceof GuiMainMenu) {
                return true;
            }
        }
        return false;
    }
    
    public static void deletePlayerTag(final File p_deletePlayerTag_0_) {
        if (p_deletePlayerTag_0_.exists()) {
            try {
                final NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(p_deletePlayerTag_0_));
                final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Data");
                nbttagcompound2.removeTag("Player");
                CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(p_deletePlayerTag_0_));
            }
            catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
