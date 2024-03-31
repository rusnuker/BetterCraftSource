// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import wdl.gui.GuiWDL;
import wdl.gui.GuiWDLAbout;
import wdl.gui.GuiWDLChunkOverrides;
import net.minecraft.client.gui.GuiScreen;
import wdl.gui.GuiWDLPermissions;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.block.Block;
import wdl.api.IBlockEventListener;
import net.minecraft.network.play.server.S24PacketBlockAction;
import wdl.api.IPluginChannelListener;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.World;
import net.minecraft.item.ItemMap;
import net.minecraft.network.play.server.S34PacketMaps;
import wdl.api.IChatMessageListener;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import wdl.api.IWDLMessageType;
import wdl.api.WDLApi;
import wdl.api.IGuiHooksListener;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;

public class WDLHooks
{
    private static final Profiler profiler;
    private static final int WDLs = 1464093811;
    private static final int WDLo = 1464093807;
    
    static {
        profiler = Minecraft.getMinecraft().mcProfiler;
    }
    
    public static void onWorldClientTick(final WorldClient sender) {
        try {
            WDLHooks.profiler.startSection("wdl");
            final List<EntityPlayer> players = (List<EntityPlayer>)ImmutableList.copyOf((Collection<?>)sender.playerEntities);
            if (sender != WDL.worldClient) {
                WDLHooks.profiler.startSection("onWorldLoad");
                if (WDL.worldLoadingDeferred) {
                    return;
                }
                WDLEvents.onWorldLoad(sender);
                WDLHooks.profiler.endSection();
            }
            else {
                WDLHooks.profiler.startSection("inventoryCheck");
                if (WDL.downloading && WDL.thePlayer != null && WDL.thePlayer.openContainer != WDL.windowContainer) {
                    if (WDL.thePlayer.openContainer == WDL.thePlayer.inventoryContainer) {
                        WDLHooks.profiler.startSection("onItemGuiClosed");
                        WDLHooks.profiler.startSection("Core");
                        boolean handled = WDLEvents.onItemGuiClosed();
                        WDLHooks.profiler.endSection();
                        final Container container = WDL.thePlayer.openContainer;
                        if (WDL.lastEntity != null) {
                            final Entity entity = WDL.lastEntity;
                            for (final WDLApi.ModInfo<IGuiHooksListener> info : WDLApi.getImplementingExtensions(IGuiHooksListener.class)) {
                                if (handled) {
                                    break;
                                }
                                WDLHooks.profiler.startSection(info.id);
                                handled = info.mod.onEntityGuiClosed(sender, entity, container);
                                WDLHooks.profiler.endSection();
                            }
                            if (!handled) {
                                WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_GUI_CLOSED_WARNING, "wdl.messages.onGuiClosedWarning.unhandledEntity", entity);
                            }
                        }
                        else {
                            final BlockPos pos = WDL.lastClickedBlock;
                            for (final WDLApi.ModInfo<IGuiHooksListener> info : WDLApi.getImplementingExtensions(IGuiHooksListener.class)) {
                                if (handled) {
                                    break;
                                }
                                WDLHooks.profiler.startSection(info.id);
                                handled = info.mod.onBlockGuiClosed(sender, pos, container);
                                WDLHooks.profiler.endSection();
                            }
                            if (!handled) {
                                WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_GUI_CLOSED_WARNING, "wdl.messages.onGuiClosedWarning.unhandledTileEntity", pos, sender.getTileEntity(pos));
                            }
                        }
                        WDLHooks.profiler.endSection();
                    }
                    else {
                        WDLHooks.profiler.startSection("onItemGuiOpened");
                        WDLHooks.profiler.startSection("Core");
                        WDLEvents.onItemGuiOpened();
                        WDLHooks.profiler.endSection();
                        WDLHooks.profiler.endSection();
                    }
                    WDL.windowContainer = WDL.thePlayer.openContainer;
                }
                WDLHooks.profiler.endSection();
            }
            WDLHooks.profiler.startSection("capes");
            CapeHandler.onWorldTick(players);
            WDLHooks.profiler.endSection();
            WDLHooks.profiler.endSection();
        }
        catch (final Throwable e) {
            WDL.crashed(e, "WDL mod: exception in onWorldClientTick event");
        }
    }
    
    public static void onWorldClientDoPreChunk(final WorldClient sender, final int x, final int z, final boolean loading) {
        try {
            if (!WDL.downloading) {
                return;
            }
            WDLHooks.profiler.startSection("wdl");
            if (!loading) {
                WDLHooks.profiler.startSection("onChunkNoLongerNeeded");
                final Chunk c = sender.getChunkFromChunkCoords(x, z);
                WDLHooks.profiler.startSection("Core");
                WDLEvents.onChunkNoLongerNeeded(c);
                WDLHooks.profiler.endSection();
                WDLHooks.profiler.endSection();
            }
            WDLHooks.profiler.endSection();
        }
        catch (final Throwable e) {
            WDL.crashed(e, "WDL mod: exception in onWorldDoPreChunk event");
        }
    }
    
    public static void onWorldClientRemoveEntityFromWorld(final WorldClient sender, final int eid) {
        try {
            if (!WDL.downloading) {
                return;
            }
            WDLHooks.profiler.startSection("wdl.onRemoveEntityFromWorld");
            final Entity entity = sender.getEntityByID(eid);
            WDLHooks.profiler.startSection("Core");
            WDLEvents.onRemoveEntityFromWorld(entity);
            WDLHooks.profiler.endSection();
            WDLHooks.profiler.endSection();
        }
        catch (final Throwable e) {
            WDL.crashed(e, "WDL mod: exception in onWorldRemoveEntityFromWorld event");
        }
    }
    
    public static void onNHPCHandleChat(final NetHandlerPlayClient sender, final S02PacketChat packet) {
        try {
            if (!Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
                return;
            }
            if (!WDL.downloading) {
                return;
            }
            WDLHooks.profiler.startSection("wdl.onChatMessage");
            final String chatMessage = packet.getChatComponent().getUnformattedText();
            WDLHooks.profiler.startSection("Core");
            WDLEvents.onChatMessage(chatMessage);
            WDLHooks.profiler.endSection();
            for (final WDLApi.ModInfo<IChatMessageListener> info : WDLApi.getImplementingExtensions(IChatMessageListener.class)) {
                WDLHooks.profiler.startSection(info.id);
                info.mod.onChat(WDL.worldClient, chatMessage);
                WDLHooks.profiler.endSection();
            }
            WDLHooks.profiler.endSection();
        }
        catch (final Throwable e) {
            WDL.crashed(e, "WDL mod: exception in onNHPCHandleChat event");
        }
    }
    
    public static void onNHPCHandleMaps(final NetHandlerPlayClient sender, final S34PacketMaps packet) {
        try {
            if (!Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
                return;
            }
            if (!WDL.downloading) {
                return;
            }
            WDLHooks.profiler.startSection("wdl.onMapDataLoaded");
            final int id = packet.getMapId();
            final MapData mapData = ItemMap.loadMapData(packet.getMapId(), WDL.worldClient);
            WDLHooks.profiler.startSection("Core");
            WDLEvents.onMapDataLoaded(id, mapData);
            WDLHooks.profiler.endSection();
            WDLHooks.profiler.endSection();
        }
        catch (final Throwable e) {
            WDL.crashed(e, "WDL mod: exception in onNHPCHandleMaps event");
        }
    }
    
    public static void onNHPCHandleCustomPayload(final NetHandlerPlayClient sender, final S3FPacketCustomPayload packet) {
        try {
            if (!Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
                return;
            }
            if (!packet.getBufferData().isReadable()) {
                return;
            }
            final String channel = packet.getChannelName();
            final byte[] payload = packet.getBufferData().array();
            WDLHooks.profiler.startSection("wdl.onPluginMessage");
            WDLHooks.profiler.startSection("Core");
            WDLEvents.onPluginChannelPacket(channel, payload);
            WDLHooks.profiler.endSection();
            for (final WDLApi.ModInfo<IPluginChannelListener> info : WDLApi.getImplementingExtensions(IPluginChannelListener.class)) {
                WDLHooks.profiler.startSection(info.id);
                info.mod.onPluginChannelPacket(WDL.worldClient, channel, payload);
                WDLHooks.profiler.endSection();
            }
            WDLHooks.profiler.endSection();
        }
        catch (final Throwable e) {
            WDL.crashed(e, "WDL mod: exception in onNHPCHandleCustomPayload event");
        }
    }
    
    public static void onNHPCHandleBlockAction(final NetHandlerPlayClient sender, final S24PacketBlockAction packet) {
        try {
            if (!Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
                return;
            }
            if (!WDL.downloading) {
                return;
            }
            WDLHooks.profiler.startSection("wdl.onBlockEvent");
            final BlockPos pos = packet.getBlockPosition();
            final Block block = packet.getBlockType();
            final int data1 = packet.getData1();
            final int data2 = packet.getData2();
            WDLHooks.profiler.startSection("Core");
            WDLEvents.onBlockEvent(pos, block, data1, data2);
            WDLHooks.profiler.endSection();
            for (final WDLApi.ModInfo<IBlockEventListener> info : WDLApi.getImplementingExtensions(IBlockEventListener.class)) {
                WDLHooks.profiler.startSection(info.id);
                info.mod.onBlockEvent(WDL.worldClient, pos, block, data1, data2);
                WDLHooks.profiler.endSection();
            }
            WDLHooks.profiler.endSection();
        }
        catch (final Throwable e) {
            WDL.crashed(e, "WDL mod: exception in onNHPCHandleBlockAction event");
        }
    }
    
    public static void onCrashReportPopulateEnvironment(final CrashReport report) {
        report.makeCategory("World Downloader Mod").addCrashSectionCallable("Info", new Callable<String>() {
            @Override
            public String call() {
                return WDL.getDebugInfo();
            }
        });
    }
    
    public static void injectWDLButtons(final GuiIngameMenu gui, final List buttonList) {
        int insertAtYPos = 0;
        for (final Object obj : buttonList) {
            final GuiButton btn = (GuiButton)obj;
            if (btn.id == 5) {
                insertAtYPos = btn.yPosition + 24;
                break;
            }
        }
        for (final Object obj : buttonList) {
            final GuiButton btn = (GuiButton)obj;
            if (btn.yPosition >= insertAtYPos) {
                final GuiButton guiButton = btn;
                guiButton.yPosition += 24;
            }
        }
        final GuiButton wdlDownload = new GuiButton(1464093811, GuiIngameMenu.width / 2 - 100, insertAtYPos, 170, 20, null);
        final GuiButton wdlOptions = new GuiButton(1464093807, GuiIngameMenu.width / 2 + 72, insertAtYPos, 28, 20, I18n.format("wdl.gui.ingameMenu.settings", new Object[0]));
        if (WDL.minecraft.isIntegratedServerRunning()) {
            wdlDownload.displayString = I18n.format("wdl.gui.ingameMenu.downloadStatus.singlePlayer", new Object[0]);
            wdlDownload.enabled = false;
        }
        else if (!WDLPluginChannels.canDownloadAtAll()) {
            if (WDLPluginChannels.canRequestPermissions()) {
                wdlDownload.displayString = I18n.format("wdl.gui.ingameMenu.downloadStatus.request", new Object[0]);
            }
            else {
                wdlDownload.displayString = I18n.format("wdl.gui.ingameMenu.downloadStatus.disabled", new Object[0]);
                wdlDownload.enabled = false;
            }
        }
        else if (WDL.saving) {
            wdlDownload.displayString = I18n.format("wdl.gui.ingameMenu.downloadStatus.saving", new Object[0]);
            wdlDownload.enabled = false;
            wdlOptions.enabled = false;
        }
        else if (WDL.downloading) {
            wdlDownload.displayString = I18n.format("wdl.gui.ingameMenu.downloadStatus.stop", new Object[0]);
        }
        else {
            wdlDownload.displayString = I18n.format("wdl.gui.ingameMenu.downloadStatus.start", new Object[0]);
        }
        buttonList.add(wdlDownload);
        buttonList.add(wdlOptions);
    }
    
    public static void handleWDLButtonClick(final GuiIngameMenu gui, final GuiButton button) {
        if (!button.enabled) {
            return;
        }
        if (button.id == 1464093811) {
            if (WDL.minecraft.isIntegratedServerRunning()) {
                return;
            }
            if (WDL.downloading) {
                WDL.stopDownload();
            }
            else {
                if (!WDLPluginChannels.canDownloadAtAll()) {
                    if (WDLPluginChannels.canRequestPermissions()) {
                        WDL.minecraft.displayGuiScreen(new GuiWDLPermissions(gui));
                    }
                    else {
                        button.enabled = false;
                    }
                    return;
                }
                if (WDLPluginChannels.hasChunkOverrides() && !WDLPluginChannels.canDownloadInGeneral()) {
                    WDL.minecraft.displayGuiScreen(new GuiWDLChunkOverrides(gui));
                }
                else {
                    WDL.startDownload();
                }
            }
        }
        else if (button.id == 1464093807) {
            if (WDL.minecraft.isIntegratedServerRunning()) {
                WDL.minecraft.displayGuiScreen(new GuiWDLAbout(gui));
            }
            else {
                WDL.minecraft.displayGuiScreen(new GuiWDL(gui));
            }
        }
        else if (button.id == 1) {
            WDL.stopDownload();
        }
    }
}
