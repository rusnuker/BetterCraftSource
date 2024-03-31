// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import net.minecraft.crash.CrashReportCategory;
import java.util.Collection;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import java.io.Writer;
import java.io.FileWriter;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import java.util.List;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.nbt.NBTBase;
import wdl.api.IWorldInfoEditor;
import java.io.OutputStream;
import java.io.FileOutputStream;
import wdl.api.IPlayerInfoEditor;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import java.io.IOException;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.world.MinecraftException;
import wdl.gui.GuiWDLSaveProgress;
import wdl.api.WDLApi;
import wdl.api.ISaveListener;
import wdl.api.IWDLMessageType;
import wdl.gui.GuiWDLOverwriteChanges;
import java.io.InputStream;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.FileInputStream;
import wdl.gui.GuiWDLMultiworld;
import net.minecraft.client.gui.GuiScreen;
import wdl.gui.GuiWDLMultiworldSelect;
import net.minecraft.client.resources.I18n;
import java.io.Reader;
import java.io.FileReader;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Properties;
import net.minecraft.world.storage.MapData;
import com.google.common.collect.HashMultimap;
import net.minecraft.tileentity.TileEntity;
import java.util.Map;
import net.minecraft.world.ChunkCoordIntPair;
import java.util.HashMap;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.inventory.Container;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.Minecraft;

public class WDL
{
    public static final String VERSION = "1.8.9a-beta2";
    public static final String EXPECTED_MINECRAFT_VERSION = "1.8.9";
    public static final String GITHUB_REPO = "Pokechu22/WorldDownloader";
    public static Minecraft minecraft;
    public static WorldClient worldClient;
    public static NetworkManager networkManager;
    public static EntityPlayerSP thePlayer;
    public static Container windowContainer;
    public static BlockPos lastClickedBlock;
    public static Entity lastEntity;
    public static SaveHandler saveHandler;
    public static IChunkLoader chunkLoader;
    public static HashMap<ChunkCoordIntPair, Map<BlockPos, TileEntity>> newTileEntities;
    public static HashMultimap<ChunkCoordIntPair, Entity> newEntities;
    public static HashMap<Integer, MapData> newMapDatas;
    public static boolean downloading;
    public static boolean isMultiworld;
    public static boolean propsFound;
    public static boolean startOnChange;
    public static boolean overrideLastModifiedCheck;
    public static boolean saving;
    public static boolean worldLoadingDeferred;
    public static String worldName;
    public static String baseFolderName;
    public static Properties baseProps;
    public static Properties worldProps;
    public static final Properties globalProps;
    public static final Properties defaultProps;
    private static Logger logger;
    private static final int ANVIL_SAVE_VERSION = 19133;
    
    static {
        WDL.networkManager = null;
        WDL.newTileEntities = new HashMap<ChunkCoordIntPair, Map<BlockPos, TileEntity>>();
        WDL.newEntities = HashMultimap.create();
        WDL.newMapDatas = new HashMap<Integer, MapData>();
        WDL.downloading = false;
        WDL.isMultiworld = false;
        WDL.propsFound = false;
        WDL.startOnChange = false;
        WDL.overrideLastModifiedCheck = false;
        WDL.saving = false;
        WDL.worldLoadingDeferred = false;
        WDL.worldName = "WorldDownloaderERROR";
        WDL.baseFolderName = "WorldDownloaderERROR";
        WDL.logger = LogManager.getLogger();
        WDL.minecraft = Minecraft.getMinecraft();
        (defaultProps = new Properties()).setProperty("ServerName", "");
        WDL.defaultProps.setProperty("WorldName", "");
        WDL.defaultProps.setProperty("LinkedWorlds", "");
        WDL.defaultProps.setProperty("Backup", "ZIP");
        WDL.defaultProps.setProperty("AllowCheats", "true");
        WDL.defaultProps.setProperty("GameType", "keep");
        WDL.defaultProps.setProperty("Time", "keep");
        WDL.defaultProps.setProperty("Weather", "keep");
        WDL.defaultProps.setProperty("MapFeatures", "false");
        WDL.defaultProps.setProperty("RandomSeed", "");
        WDL.defaultProps.setProperty("MapGenerator", "void");
        WDL.defaultProps.setProperty("GeneratorName", "flat");
        WDL.defaultProps.setProperty("GeneratorVersion", "0");
        WDL.defaultProps.setProperty("GeneratorOptions", ";0");
        WDL.defaultProps.setProperty("Spawn", "player");
        WDL.defaultProps.setProperty("SpawnX", "8");
        WDL.defaultProps.setProperty("SpawnY", "127");
        WDL.defaultProps.setProperty("SpawnZ", "8");
        WDL.defaultProps.setProperty("PlayerPos", "keep");
        WDL.defaultProps.setProperty("PlayerX", "8");
        WDL.defaultProps.setProperty("PlayerY", "127");
        WDL.defaultProps.setProperty("PlayerZ", "8");
        WDL.defaultProps.setProperty("PlayerHealth", "20");
        WDL.defaultProps.setProperty("PlayerFood", "20");
        WDL.defaultProps.setProperty("Messages.enableAll", "false");
        WDL.defaultProps.setProperty("Entity.TrackDistanceMode", "server");
        WDL.defaultProps.setProperty("Entity.FireworksRocketEntity.Enabled", "false");
        WDL.defaultProps.setProperty("Entity.EnderDragon.Enabled", "false");
        WDL.defaultProps.setProperty("Entity.WitherBoss.Enabled", "false");
        WDL.defaultProps.setProperty("Entity.PrimedTnt.Enabled", "false");
        WDL.defaultProps.setProperty("Entity.null.Enabled", "false");
        WDL.defaultProps.setProperty("EntityGroup.Other.Enabled", "true");
        WDL.defaultProps.setProperty("EntityGroup.Hostile.Enabled", "true");
        WDL.defaultProps.setProperty("EntityGroup.Passive.Enabled", "true");
        WDL.defaultProps.setProperty("LastSaved", "-1");
        WDL.defaultProps.setProperty("TutorialShown", "false");
        WDL.defaultProps.setProperty("UpdateMinecraftVersion", "client");
        WDL.defaultProps.setProperty("UpdateAllowBetas", "true");
        globalProps = new Properties(WDL.defaultProps);
        FileReader reader = null;
        Label_0647: {
            try {
                reader = new FileReader(new File(WDL.minecraft.mcDataDir, "WorldDownloader.txt"));
                WDL.globalProps.load(reader);
            }
            catch (final Exception e) {
                WDL.logger.debug("Failed to load global properties", e);
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (final Exception e2) {
                        WDL.logger.warn("Failed to close global properties reader", e2);
                    }
                }
                break Label_0647;
            }
            finally {
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (final Exception e2) {
                        WDL.logger.warn("Failed to close global properties reader", e2);
                    }
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (final Exception e2) {
                    WDL.logger.warn("Failed to close global properties reader", e2);
                }
            }
        }
        WDL.baseProps = new Properties(WDL.globalProps);
        WDL.worldProps = new Properties(WDL.baseProps);
    }
    
    public static void startDownload() {
        WDL.worldClient = WDL.minecraft.theWorld;
        if (!WDLPluginChannels.canDownloadAtAll()) {
            return;
        }
        if (WDL.isMultiworld && WDL.worldName.isEmpty()) {
            WDL.minecraft.displayGuiScreen(new GuiWDLMultiworldSelect(I18n.format("wdl.gui.multiworldSelect.title.startDownload", new Object[0]), new GuiWDLMultiworldSelect.WorldSelectionCallback() {
                @Override
                public void onWorldSelected(final String selectedWorld) {
                    WDL.worldName = selectedWorld;
                    WDL.isMultiworld = true;
                    WDL.propsFound = true;
                    WDL.minecraft.displayGuiScreen(null);
                    WDL.startDownload();
                }
                
                @Override
                public void onCancel() {
                    WDL.minecraft.displayGuiScreen(null);
                    WDL.cancelDownload();
                }
            }));
            return;
        }
        if (!WDL.propsFound) {
            WDL.minecraft.displayGuiScreen(new GuiWDLMultiworld(new GuiWDLMultiworld.MultiworldCallback() {
                @Override
                public void onSelect(final boolean enableMutliworld) {
                    WDL.isMultiworld = enableMutliworld;
                    if (WDL.isMultiworld) {
                        WDL.minecraft.displayGuiScreen(new GuiWDLMultiworldSelect(I18n.format("wdl.gui.multiworldSelect.title.startDownload", new Object[0]), new GuiWDLMultiworldSelect.WorldSelectionCallback() {
                            @Override
                            public void onWorldSelected(final String selectedWorld) {
                                WDL.worldName = selectedWorld;
                                WDL.isMultiworld = true;
                                WDL.propsFound = true;
                                WDL.minecraft.displayGuiScreen(null);
                                WDL.startDownload();
                            }
                            
                            @Override
                            public void onCancel() {
                                WDL.minecraft.displayGuiScreen(null);
                                WDL.cancelDownload();
                            }
                        }));
                    }
                    else {
                        WDL.baseProps.setProperty("LinkedWorlds", "");
                        WDL.saveProps();
                        WDL.propsFound = true;
                        WDL.minecraft.displayGuiScreen(null);
                        WDL.startDownload();
                    }
                }
                
                @Override
                public void onCancel() {
                    WDL.minecraft.displayGuiScreen(null);
                    WDL.cancelDownload();
                }
            }));
            return;
        }
        WDL.worldProps = loadWorldProps(WDL.worldName);
        WDL.saveHandler = (SaveHandler)WDL.minecraft.getSaveLoader().getSaveLoader(getWorldFolderName(WDL.worldName), true);
        FileInputStream worldDat = null;
        Label_0300: {
            try {
                final long lastSaved = Long.parseLong(WDL.worldProps.getProperty("LastSaved", "-1"));
                worldDat = new FileInputStream(new File(WDL.saveHandler.getWorldDirectory(), "level.dat"));
                final long lastPlayed = CompressedStreamTools.readCompressed(worldDat).getCompoundTag("Data").getLong("LastPlayed");
                if (!WDL.overrideLastModifiedCheck && lastPlayed > lastSaved) {
                    WDL.minecraft.displayGuiScreen(new GuiWDLOverwriteChanges(lastSaved, lastPlayed));
                    return;
                }
            }
            catch (final Exception e) {
                WDL.logger.warn("Error while checking if the map has been played andneeds to be backed up (this is normal if this world has not been saved before): ", e);
                if (worldDat != null) {
                    try {
                        worldDat.close();
                    }
                    catch (final Exception e2) {
                        e2.printStackTrace();
                    }
                }
                break Label_0300;
            }
            finally {
                if (worldDat != null) {
                    try {
                        worldDat.close();
                    }
                    catch (final Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            if (worldDat != null) {
                try {
                    worldDat.close();
                }
                catch (final Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        WDL.minecraft.displayGuiScreen(null);
        WDL.minecraft.setIngameFocus();
        WDL.chunkLoader = WDLChunkLoader.create(WDL.saveHandler, WDL.worldClient.provider);
        WDL.newTileEntities = new HashMap<ChunkCoordIntPair, Map<BlockPos, TileEntity>>();
        WDL.newEntities = HashMultimap.create();
        WDL.newMapDatas = new HashMap<Integer, MapData>();
        if (WDL.baseProps.getProperty("ServerName").isEmpty()) {
            WDL.baseProps.setProperty("ServerName", getServerName());
        }
        WDL.startOnChange = true;
        WDL.downloading = true;
        WDLMessages.chatMessageTranslated(WDLMessageTypes.INFO, "wdl.messages.generalInfo.downloadStarted", new Object[0]);
    }
    
    public static void stopDownload() {
        if (WDL.downloading) {
            WDL.downloading = false;
            WDL.startOnChange = false;
            WDLMessages.chatMessageTranslated(WDLMessageTypes.INFO, "wdl.messages.generalInfo.downloadStopped", new Object[0]);
            startSaveThread();
        }
    }
    
    public static void cancelDownload() {
        final boolean wasDownloading = WDL.downloading;
        if (wasDownloading) {
            WDL.minecraft.getSaveLoader().flushCache();
            WDL.saveHandler.flush();
            WDL.startOnChange = false;
            WDL.saving = false;
            WDL.downloading = false;
            WDL.worldLoadingDeferred = false;
            WDLMessages.chatMessageTranslated(WDLMessageTypes.INFO, "wdl.messages.generalInfo.downloadCanceled", new Object[0]);
        }
    }
    
    static void startSaveThread() {
        WDLMessages.chatMessageTranslated(WDLMessageTypes.INFO, "wdl.messages.generalInfo.saveStarted", new Object[0]);
        WDL.saving = true;
        final Thread thread = new Thread("WDL Save Thread") {
            @Override
            public void run() {
                try {
                    WDL.saveEverything();
                    WDL.saving = false;
                    WDL.onSaveComplete();
                }
                catch (final Throwable e) {
                    WDL.crashed(e, "World Downloader Mod: Saving world");
                }
            }
        };
        thread.start();
    }
    
    public static boolean loadWorld() {
        WDL.worldName = "";
        WDL.worldClient = WDL.minecraft.theWorld;
        WDL.thePlayer = WDL.minecraft.thePlayer;
        WDL.windowContainer = WDL.thePlayer.openContainer;
        WDL.overrideLastModifiedCheck = false;
        final NetworkManager newNM = WDL.thePlayer.sendQueue.getNetworkManager();
        if (WDL.networkManager != newNM) {
            loadBaseProps();
            WDLMessages.onNewServer();
        }
        WDLPluginChannels.onWorldLoad();
        if (WDL.networkManager != newNM) {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_WORLD_LOAD, "wdl.messages.onWorldLoad.differentServer", new Object[0]);
            WDL.networkManager = newNM;
            if (isSpigot()) {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_WORLD_LOAD, "wdl.messages.onWorldLoad.spigot", WDL.thePlayer.getClientBrand());
            }
            else {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_WORLD_LOAD, "wdl.messages.onWorldLoad.vanilla", WDL.thePlayer.getClientBrand());
            }
            WDL.startOnChange = false;
            return true;
        }
        WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_WORLD_LOAD, "wdl.messages.onWorldLoad.sameServer", new Object[0]);
        if (isSpigot()) {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_WORLD_LOAD, "wdl.messages.onWorldLoad.spigot", WDL.thePlayer.getClientBrand());
        }
        else {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_WORLD_LOAD, "wdl.messages.onWorldLoad.vanilla", WDL.thePlayer.getClientBrand());
        }
        if (WDL.startOnChange) {
            startDownload();
        }
        return false;
    }
    
    public static void onSaveComplete() {
        WDL.minecraft.getSaveLoader().flushCache();
        WDL.saveHandler.flush();
        WDL.worldClient = null;
        WDL.worldLoadingDeferred = false;
        if (WDL.downloading) {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.INFO, "wdl.messages.generalInfo.saveComplete.startingAgain", new Object[0]);
            loadWorld();
            return;
        }
        WDLMessages.chatMessageTranslated(WDLMessageTypes.INFO, "wdl.messages.generalInfo.saveComplete.done", new Object[0]);
    }
    
    public static void saveEverything() throws Exception {
        if (!WDLPluginChannels.canDownloadAtAll()) {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.ERROR, "wdl.messages.generalError.forbidden", new Object[0]);
            return;
        }
        final WorldBackup.WorldBackupType backupType = WorldBackup.WorldBackupType.match(WDL.baseProps.getProperty("Backup", "ZIP"));
        final GuiWDLSaveProgress progressScreen = new GuiWDLSaveProgress(I18n.format("wdl.saveProgress.title", new Object[0]), ((backupType != WorldBackup.WorldBackupType.NONE) ? 6 : 5) + WDLApi.getImplementingExtensions(ISaveListener.class).size());
        WDL.minecraft.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                WDL.minecraft.displayGuiScreen(progressScreen);
            }
        });
        saveProps();
        try {
            WDL.saveHandler.checkSessionLock();
        }
        catch (final MinecraftException e) {
            throw new RuntimeException("WorldDownloader: Couldn't get session lock for saving the world!", e);
        }
        final NBTTagCompound playerNBT = savePlayer(progressScreen);
        saveWorldInfo(progressScreen, playerNBT);
        saveMapData(progressScreen);
        saveChunks(progressScreen);
        saveProps();
        for (final WDLApi.ModInfo<ISaveListener> info : WDLApi.getImplementingExtensions(ISaveListener.class)) {
            progressScreen.startMajorTask(I18n.format("wdl.saveProgress.extension.title", info.getDisplayName()), 1);
            info.mod.afterChunksSaved(WDL.saveHandler.getWorldDirectory());
        }
        try {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.SAVING, "wdl.messages.saving.flushingIO", new Object[0]);
            progressScreen.startMajorTask(I18n.format("wdl.saveProgress.flushingIO.title", new Object[0]), 1);
            progressScreen.setMinorTaskProgress(I18n.format("wdl.saveProgress.flushingIO.subtitle", new Object[0]), 1);
            ThreadedFileIOBase.getThreadedIOInstance().waitForFinish();
        }
        catch (final Exception e2) {
            throw new RuntimeException("Threw exception waiting for asynchronous IO to finish. Hmmm.", e2);
        }
        if (backupType != WorldBackup.WorldBackupType.NONE) {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.SAVING, "wdl.messages.saving.backingUp", new Object[0]);
            progressScreen.startMajorTask(backupType.getTitle(), 1);
            progressScreen.setMinorTaskProgress(I18n.format("wdl.saveProgress.backingUp.preparing", new Object[0]), 1);
            try {
                WorldBackup.backupWorld(WDL.saveHandler.getWorldDirectory(), getWorldFolderName(WDL.worldName), backupType, progressScreen);
            }
            catch (final IOException e3) {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.ERROR, "wdl.messages.generalError.failedToBackUp", new Object[0]);
            }
        }
        progressScreen.setDoneWorking();
    }
    
    public static NBTTagCompound savePlayer(final GuiWDLSaveProgress progressScreen) {
        if (!WDLPluginChannels.canDownloadAtAll()) {
            return new NBTTagCompound();
        }
        progressScreen.startMajorTask(I18n.format("wdl.saveProgress.playerData.title", new Object[0]), 3 + WDLApi.getImplementingExtensions(IPlayerInfoEditor.class).size());
        WDLMessages.chatMessageTranslated(WDLMessageTypes.SAVING, "wdl.messages.saving.savingPlayer", new Object[0]);
        progressScreen.setMinorTaskProgress(I18n.format("wdl.saveProgress.playerData.creatingNBT", new Object[0]), 1);
        final NBTTagCompound playerNBT = new NBTTagCompound();
        WDL.thePlayer.writeToNBT(playerNBT);
        progressScreen.setMinorTaskProgress(I18n.format("wdl.saveProgress.playerData.editingNBT", new Object[0]), 2);
        applyOverridesToPlayer(playerNBT);
        int taskNum = 3;
        for (final WDLApi.ModInfo<IPlayerInfoEditor> info : WDLApi.getImplementingExtensions(IPlayerInfoEditor.class)) {
            progressScreen.setMinorTaskProgress(I18n.format("wdl.saveProgress.playerData.extension", info.getDisplayName()), taskNum);
            info.mod.editPlayerInfo(WDL.thePlayer, WDL.saveHandler, playerNBT);
            ++taskNum;
        }
        progressScreen.setMinorTaskProgress(I18n.format("wdl.saveProgress.playerData.writingNBT", new Object[0]), taskNum);
        FileOutputStream stream = null;
        try {
            final File playersDirectory = new File(WDL.saveHandler.getWorldDirectory(), "playerdata");
            final File playerFileTmp = new File(playersDirectory, String.valueOf(WDL.thePlayer.getUniqueID().toString()) + ".dat.tmp");
            final File playerFile = new File(playersDirectory, String.valueOf(WDL.thePlayer.getUniqueID().toString()) + ".dat");
            stream = new FileOutputStream(playerFileTmp);
            CompressedStreamTools.writeCompressed(playerNBT, stream);
            if (playerFile.exists()) {
                playerFile.delete();
            }
            playerFileTmp.renameTo(playerFile);
        }
        catch (final Exception e) {
            throw new RuntimeException("Couldn't save the player!", e);
        }
        finally {
            if (stream != null) {
                try {
                    stream.close();
                }
                catch (final IOException e2) {
                    throw new RuntimeException(e2);
                }
            }
        }
        if (stream != null) {
            try {
                stream.close();
            }
            catch (final IOException e2) {
                throw new RuntimeException(e2);
            }
        }
        WDLMessages.chatMessageTranslated(WDLMessageTypes.SAVING, "wdl.messages.saving.playerSaved", new Object[0]);
        return playerNBT;
    }
    
    public static void saveWorldInfo(final GuiWDLSaveProgress progressScreen, final NBTTagCompound playerInfoNBT) {
        if (!WDLPluginChannels.canDownloadAtAll()) {
            return;
        }
        progressScreen.startMajorTask(I18n.format("wdl.saveProgress.worldMetadata.title", new Object[0]), 3 + WDLApi.getImplementingExtensions(IWorldInfoEditor.class).size());
        WDLMessages.chatMessageTranslated(WDLMessageTypes.SAVING, "wdl.messages.saving.savingWorld", new Object[0]);
        progressScreen.setMinorTaskProgress(I18n.format("wdl.saveProgress.worldMetadata.creatingNBT", new Object[0]), 1);
        WDL.worldClient.getWorldInfo().setSaveVersion(19133);
        final NBTTagCompound worldInfoNBT = WDL.worldClient.getWorldInfo().cloneNBTCompound(playerInfoNBT);
        progressScreen.setMinorTaskProgress(I18n.format("wdl.saveProgress.worldMetadata.editingNBT", new Object[0]), 2);
        applyOverridesToWorldInfo(worldInfoNBT);
        int taskNum = 3;
        for (final WDLApi.ModInfo<IWorldInfoEditor> info : WDLApi.getImplementingExtensions(IWorldInfoEditor.class)) {
            progressScreen.setMinorTaskProgress(I18n.format("wdl.saveProgress.worldMetadata.extension", info.getDisplayName()), taskNum);
            info.mod.editWorldInfo(WDL.worldClient, WDL.worldClient.getWorldInfo(), WDL.saveHandler, worldInfoNBT);
            ++taskNum;
        }
        progressScreen.setMinorTaskProgress(I18n.format("wdl.saveProgress.worldMetadata.writingNBT", new Object[0]), taskNum);
        final File saveDirectory = WDL.saveHandler.getWorldDirectory();
        final NBTTagCompound dataNBT = new NBTTagCompound();
        dataNBT.setTag("Data", worldInfoNBT);
        WDL.worldProps.setProperty("LastSaved", Long.toString(worldInfoNBT.getLong("LastPlayed")));
        FileOutputStream stream = null;
        try {
            final File dataFile = new File(saveDirectory, "level.dat_new");
            final File dataFileBackup = new File(saveDirectory, "level.dat_old");
            final File dataFileOld = new File(saveDirectory, "level.dat");
            stream = new FileOutputStream(dataFile);
            CompressedStreamTools.writeCompressed(dataNBT, stream);
            if (dataFileBackup.exists()) {
                dataFileBackup.delete();
            }
            dataFileOld.renameTo(dataFileBackup);
            if (dataFileOld.exists()) {
                dataFileOld.delete();
            }
            dataFile.renameTo(dataFileOld);
            if (dataFile.exists()) {
                dataFile.delete();
            }
        }
        catch (final Exception e) {
            throw new RuntimeException("Couldn't save the world metadata!", e);
        }
        finally {
            if (stream != null) {
                try {
                    stream.close();
                }
                catch (final IOException e2) {
                    throw new RuntimeException(e2);
                }
            }
        }
        if (stream != null) {
            try {
                stream.close();
            }
            catch (final IOException e2) {
                throw new RuntimeException(e2);
            }
        }
        WDLMessages.chatMessageTranslated(WDLMessageTypes.SAVING, "wdl.messages.saving.worldSaved", new Object[0]);
    }
    
    public static void saveChunks(final GuiWDLSaveProgress progressScreen) throws IllegalArgumentException, IllegalAccessException {
        if (!WDLPluginChannels.canDownloadAtAll()) {
            return;
        }
        WDLMessages.chatMessageTranslated(WDLMessageTypes.SAVING, "wdl.messages.saving.savingChunks", new Object[0]);
        final ChunkProviderClient chunkProvider = (ChunkProviderClient)WDL.worldClient.getChunkProvider();
        final List<?> chunks = ReflectionUtils.stealAndGetField(chunkProvider, List.class);
        progressScreen.startMajorTask(I18n.format("wdl.saveProgress.chunk.title", new Object[0]), chunks.size());
        for (int currentChunk = 0; currentChunk < chunks.size(); ++currentChunk) {
            final Chunk c = (Chunk)chunks.get(currentChunk);
            if (c != null) {
                if (WDLPluginChannels.canSaveChunk(c)) {
                    progressScreen.setMinorTaskProgress(I18n.format("wdl.saveProgress.chunk.saving", c.xPosition, c.zPosition), currentChunk);
                    saveChunk(c);
                }
            }
        }
        WDLMessages.chatMessageTranslated(WDLMessageTypes.SAVING, "wdl.messages.saving.chunksSaved", new Object[0]);
    }
    
    public static void saveChunk(final Chunk c) {
        if (!WDLPluginChannels.canDownloadAtAll()) {
            return;
        }
        if (!WDLPluginChannels.canSaveChunk(c)) {
            return;
        }
        c.setTerrainPopulated(true);
        try {
            WDL.chunkLoader.saveChunk(WDL.worldClient, c);
        }
        catch (final Exception e) {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.ERROR, "wdl.messages.generalError.failedToSaveChunk", c.xPosition, c.zPosition, e);
        }
    }
    
    public static void loadBaseProps() {
        WDL.baseFolderName = getBaseFolderName();
        WDL.baseProps = new Properties(WDL.globalProps);
        FileReader reader = null;
        Label_0183: {
            try {
                final File savesFolder = new File(WDL.minecraft.mcDataDir, "saves");
                final File baseFolder = new File(savesFolder, WDL.baseFolderName);
                reader = new FileReader(new File(baseFolder, "WorldDownloader.txt"));
                WDL.baseProps.load(reader);
                WDL.propsFound = true;
            }
            catch (final Exception e) {
                WDL.propsFound = false;
                WDL.logger.debug("Failed to load base properties", e);
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (final Exception e2) {
                        WDL.logger.warn("Failed to close base properties reader", e2);
                    }
                }
                break Label_0183;
            }
            finally {
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (final Exception e2) {
                        WDL.logger.warn("Failed to close base properties reader", e2);
                    }
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (final Exception e2) {
                    WDL.logger.warn("Failed to close base properties reader", e2);
                }
            }
        }
        if (WDL.baseProps.getProperty("LinkedWorlds").isEmpty()) {
            WDL.isMultiworld = false;
            WDL.worldProps = new Properties(WDL.baseProps);
        }
        else {
            WDL.isMultiworld = true;
        }
    }
    
    public static Properties loadWorldProps(final String theWorldName) {
        final Properties ret = new Properties(WDL.baseProps);
        if (theWorldName.isEmpty()) {
            return ret;
        }
        final File savesDir = new File(WDL.minecraft.mcDataDir, "saves");
        final String folder = getWorldFolderName(theWorldName);
        final File worldFolder = new File(savesDir, folder);
        final FileReader reader = null;
        try {
            ret.load(new FileReader(new File(worldFolder, "WorldDownloader.txt")));
            return ret;
        }
        catch (final Exception e) {
            WDL.logger.debug("Failed to load world props for " + WDL.worldName, e);
            return ret;
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (final Exception e2) {
                    WDL.logger.warn("Failed to close world props reader for " + WDL.worldName, e2);
                }
            }
        }
    }
    
    public static void saveProps() {
        saveProps(WDL.worldName, WDL.worldProps);
    }
    
    public static void saveProps(final String theWorldName, final Properties theWorldProps) {
        final File savesDir = new File(WDL.minecraft.mcDataDir, "saves");
        if (theWorldName.length() > 0) {
            final String folder = getWorldFolderName(theWorldName);
            final File worldFolder = new File(savesDir, folder);
            worldFolder.mkdirs();
            try {
                theWorldProps.store(new FileWriter(new File(worldFolder, "WorldDownloader.txt")), I18n.format("wdl.props.world.title", new Object[0]));
            }
            catch (final Exception ex) {}
        }
        else if (!WDL.isMultiworld) {
            WDL.baseProps.putAll(theWorldProps);
        }
        final File baseFolder = new File(savesDir, WDL.baseFolderName);
        baseFolder.mkdirs();
        try {
            WDL.baseProps.store(new FileWriter(new File(baseFolder, "WorldDownloader.txt")), I18n.format("wdl.props.base.title", new Object[0]));
        }
        catch (final Exception ex2) {}
        saveGlobalProps();
    }
    
    public static void saveGlobalProps() {
        try {
            WDL.globalProps.store(new FileWriter(new File(WDL.minecraft.mcDataDir, "WorldDownloader.txt")), I18n.format("wdl.props.global.title", new Object[0]));
        }
        catch (final Exception ex) {}
    }
    
    public static void applyOverridesToPlayer(final NBTTagCompound playerNBT) {
        final String health = WDL.worldProps.getProperty("PlayerHealth");
        if (!health.equals("keep")) {
            final short h = Short.parseShort(health);
            playerNBT.setShort("Health", h);
        }
        final String food = WDL.worldProps.getProperty("PlayerFood");
        if (!food.equals("keep")) {
            final int f = Integer.parseInt(food);
            playerNBT.setInteger("foodLevel", f);
            playerNBT.setInteger("foodTickTimer", 0);
            if (f == 20) {
                playerNBT.setFloat("foodSaturationLevel", 5.0f);
            }
            else {
                playerNBT.setFloat("foodSaturationLevel", 0.0f);
            }
            playerNBT.setFloat("foodExhaustionLevel", 0.0f);
        }
        final String playerPos = WDL.worldProps.getProperty("PlayerPos");
        if (playerPos.equals("xyz")) {
            final int x = Integer.parseInt(WDL.worldProps.getProperty("PlayerX"));
            final int y = Integer.parseInt(WDL.worldProps.getProperty("PlayerY"));
            final int z = Integer.parseInt(WDL.worldProps.getProperty("PlayerZ"));
            final NBTTagList pos = new NBTTagList();
            pos.appendTag(new NBTTagDouble(x + 0.5));
            pos.appendTag(new NBTTagDouble(y + 0.621));
            pos.appendTag(new NBTTagDouble(z + 0.5));
            playerNBT.setTag("Pos", pos);
            final NBTTagList motion = new NBTTagList();
            motion.appendTag(new NBTTagDouble(0.0));
            motion.appendTag(new NBTTagDouble(-1.0E-4));
            motion.appendTag(new NBTTagDouble(0.0));
            playerNBT.setTag("Motion", motion);
            final NBTTagList rotation = new NBTTagList();
            rotation.appendTag(new NBTTagFloat(0.0f));
            rotation.appendTag(new NBTTagFloat(0.0f));
            playerNBT.setTag("Rotation", rotation);
        }
        if (WDL.thePlayer.capabilities.allowFlying) {
            playerNBT.getCompoundTag("abilities").setBoolean("flying", true);
        }
    }
    
    public static void applyOverridesToWorldInfo(final NBTTagCompound worldInfoNBT) {
        final String baseName = WDL.baseProps.getProperty("ServerName");
        final String worldName = WDL.worldProps.getProperty("WorldName");
        if (worldName.isEmpty()) {
            worldInfoNBT.setString("LevelName", baseName);
        }
        else {
            worldInfoNBT.setString("LevelName", String.valueOf(baseName) + " - " + worldName);
        }
        if (WDL.worldProps.getProperty("AllowCheats").equals("true")) {
            worldInfoNBT.setBoolean("allowCommands", true);
        }
        else {
            worldInfoNBT.setBoolean("allowCommands", false);
        }
        final String gametypeOption = WDL.worldProps.getProperty("GameType");
        if (gametypeOption.equals("keep")) {
            if (WDL.thePlayer.capabilities.isCreativeMode) {
                worldInfoNBT.setInteger("GameType", 1);
            }
            else {
                worldInfoNBT.setInteger("GameType", 0);
            }
        }
        else if (gametypeOption.equals("survival")) {
            worldInfoNBT.setInteger("GameType", 0);
        }
        else if (gametypeOption.equals("creative")) {
            worldInfoNBT.setInteger("GameType", 1);
        }
        else if (gametypeOption.equals("hardcore")) {
            worldInfoNBT.setInteger("GameType", 0);
            worldInfoNBT.setBoolean("hardcore", true);
        }
        final String timeOption = WDL.worldProps.getProperty("Time");
        if (!timeOption.equals("keep")) {
            final long t = Integer.parseInt(timeOption);
            worldInfoNBT.setLong("Time", t);
        }
        final String randomSeed = WDL.worldProps.getProperty("RandomSeed");
        long seed = 0L;
        if (!randomSeed.isEmpty()) {
            try {
                seed = Long.parseLong(randomSeed);
            }
            catch (final NumberFormatException numberformatexception) {
                seed = randomSeed.hashCode();
            }
        }
        worldInfoNBT.setLong("RandomSeed", seed);
        final boolean mapFeatures = Boolean.parseBoolean(WDL.worldProps.getProperty("MapFeatures"));
        worldInfoNBT.setBoolean("MapFeatures", mapFeatures);
        final String generatorName = WDL.worldProps.getProperty("GeneratorName");
        worldInfoNBT.setString("generatorName", generatorName);
        final String generatorOptions = WDL.worldProps.getProperty("GeneratorOptions");
        worldInfoNBT.setString("generatorOptions", generatorOptions);
        final int generatorVersion = Integer.parseInt(WDL.worldProps.getProperty("GeneratorVersion"));
        worldInfoNBT.setInteger("generatorVersion", generatorVersion);
        final String weather = WDL.worldProps.getProperty("Weather");
        if (weather.equals("sunny")) {
            worldInfoNBT.setBoolean("raining", false);
            worldInfoNBT.setInteger("rainTime", 0);
            worldInfoNBT.setBoolean("thundering", false);
            worldInfoNBT.setInteger("thunderTime", 0);
        }
        else if (weather.equals("rain")) {
            worldInfoNBT.setBoolean("raining", true);
            worldInfoNBT.setInteger("rainTime", 24000);
            worldInfoNBT.setBoolean("thundering", false);
            worldInfoNBT.setInteger("thunderTime", 0);
        }
        else if (weather.equals("thunderstorm")) {
            worldInfoNBT.setBoolean("raining", true);
            worldInfoNBT.setInteger("rainTime", 24000);
            worldInfoNBT.setBoolean("thundering", true);
            worldInfoNBT.setInteger("thunderTime", 24000);
        }
        final String spawn = WDL.worldProps.getProperty("Spawn");
        if (spawn.equals("player")) {
            final int x = MathHelper.floor_double(WDL.thePlayer.posX);
            final int y = MathHelper.floor_double(WDL.thePlayer.posY);
            final int z = MathHelper.floor_double(WDL.thePlayer.posZ);
            worldInfoNBT.setInteger("SpawnX", x);
            worldInfoNBT.setInteger("SpawnY", y);
            worldInfoNBT.setInteger("SpawnZ", z);
            worldInfoNBT.setBoolean("initialized", true);
        }
        else if (spawn.equals("xyz")) {
            final int x = Integer.parseInt(WDL.worldProps.getProperty("SpawnX"));
            final int y = Integer.parseInt(WDL.worldProps.getProperty("SpawnY"));
            final int z = Integer.parseInt(WDL.worldProps.getProperty("SpawnZ"));
            worldInfoNBT.setInteger("SpawnX", x);
            worldInfoNBT.setInteger("SpawnY", y);
            worldInfoNBT.setInteger("SpawnZ", z);
            worldInfoNBT.setBoolean("initialized", true);
        }
    }
    
    public static void saveMapData(final GuiWDLSaveProgress progressScreen) {
        if (!WDLPluginChannels.canSaveMaps()) {
            return;
        }
        final File dataDirectory = new File(WDL.saveHandler.getWorldDirectory(), "data");
        dataDirectory.mkdirs();
        progressScreen.startMajorTask(I18n.format("wdl.saveProgress.map.title", new Object[0]), WDL.newMapDatas.size());
        WDLMessages.chatMessageTranslated(WDLMessageTypes.SAVING, "wdl.messages.saving.savingMapItemData", new Object[0]);
        int count = 0;
        for (final Map.Entry<Integer, MapData> e : WDL.newMapDatas.entrySet()) {
            ++count;
            progressScreen.setMinorTaskProgress(I18n.format("wdl.saveProgress.map.saving", e.getKey()), count);
            final File mapFile = new File(dataDirectory, "map_" + e.getKey() + ".dat");
            final NBTTagCompound mapNBT = new NBTTagCompound();
            final NBTTagCompound data = new NBTTagCompound();
            e.getValue().writeToNBT(data);
            mapNBT.setTag("data", data);
            try {
                CompressedStreamTools.writeCompressed(mapNBT, new FileOutputStream(mapFile));
            }
            catch (final IOException ex) {
                throw new RuntimeException("WDL: Exception while writing map data for map " + e.getKey() + "!", ex);
            }
        }
        WDLMessages.chatMessageTranslated(WDLMessageTypes.SAVING, "wdl.messages.saving.mapItemDataSaved", new Object[0]);
    }
    
    public static String getServerName() {
        try {
            if (WDL.minecraft.getCurrentServerData() != null) {
                String name = WDL.minecraft.getCurrentServerData().serverName;
                if (name.equals(I18n.format("selectServer.defaultName", new Object[0]))) {
                    name = WDL.minecraft.getCurrentServerData().serverIP;
                }
                return name;
            }
        }
        catch (final Exception e) {
            WDL.logger.warn("Exception while getting server name: ", e);
        }
        return "Unidentified Server";
    }
    
    public static String getBaseFolderName() {
        return getServerName().replaceAll("\\W+", "_");
    }
    
    public static String getWorldFolderName(final String theWorldName) {
        if (theWorldName.isEmpty()) {
            return WDL.baseFolderName;
        }
        return String.valueOf(WDL.baseFolderName) + " - " + theWorldName;
    }
    
    public static void saveContainerItems(final Container container, final IInventory tileEntity, final int containerStartIndex) {
        for (int containerSize = container.inventorySlots.size(), inventorySize = tileEntity.getSizeInventory(), containerIndex = containerStartIndex, inventoryIndex = 0; containerIndex < containerSize && inventoryIndex < inventorySize; ++inventoryIndex, ++containerIndex) {
            final ItemStack item = container.getSlot(containerIndex).getStack();
            tileEntity.setInventorySlotContents(inventoryIndex, item);
        }
    }
    
    public static void saveInventoryFields(final IInventory inventory, final IInventory tileEntity) {
        for (int i = 0; i < inventory.getFieldCount(); ++i) {
            tileEntity.setField(i, inventory.getField(i));
        }
    }
    
    public static void saveTileEntity(final BlockPos pos, final TileEntity te) {
        final int chunkX = pos.getX() / 16;
        final int chunkZ = pos.getZ() / 16;
        final ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(chunkX, chunkZ);
        if (!WDL.newTileEntities.containsKey(chunkPos)) {
            WDL.newTileEntities.put(chunkPos, new HashMap<BlockPos, TileEntity>());
        }
        WDL.newTileEntities.get(chunkPos).put(pos, te);
    }
    
    public static boolean isSpigot() {
        return WDL.thePlayer != null && WDL.thePlayer.getClientBrand() != null && WDL.thePlayer.getClientBrand().toLowerCase().contains("spigot");
    }
    
    public static String getDebugInfo() {
        final StringBuilder info = new StringBuilder();
        info.append("### CORE INFO\n\n");
        info.append("WDL version: ").append("1.8.9a-beta2").append('\n');
        info.append("Launched version: ").append(Minecraft.getMinecraft().getVersion()).append('\n');
        info.append("Client brand: ").append(ClientBrandRetriever.getClientModName()).append('\n');
        info.append("File location: ");
        try {
            String path = new File(WDL.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            final String username = System.getProperty("user.name");
            path = path.replace(username, "<USERNAME>");
            info.append(path);
        }
        catch (final Exception e) {
            info.append("Unknown (").append(e.toString()).append(')');
        }
        info.append("\n\n### EXTENSIONS\n\n");
        final Map<String, WDLApi.ModInfo<?>> extensions = WDLApi.getWDLMods();
        info.append(extensions.size()).append(" loaded\n");
        for (final Map.Entry<String, WDLApi.ModInfo<?>> e2 : extensions.entrySet()) {
            info.append("\n#### ").append(e2.getKey()).append("\n\n");
            try {
                info.append(e2.getValue().getInfo());
            }
            catch (final Exception ex) {
                info.append("ERROR: ").append(ex).append('\n');
                StackTraceElement[] stackTrace;
                for (int length = (stackTrace = ex.getStackTrace()).length, i = 0; i < length; ++i) {
                    final StackTraceElement elm = stackTrace[i];
                    info.append(elm).append('\n');
                }
            }
        }
        info.append("\n### STATE\n\n");
        info.append("minecraft: ").append(WDL.minecraft).append('\n');
        info.append("worldClient: ").append(WDL.worldClient).append('\n');
        info.append("networkManager: ").append(WDL.networkManager).append('\n');
        info.append("thePlayer: ").append(WDL.thePlayer).append('\n');
        info.append("windowContainer: ").append(WDL.windowContainer).append('\n');
        info.append("lastClickedBlock: ").append(WDL.lastClickedBlock).append('\n');
        info.append("lastEntity: ").append(WDL.lastEntity).append('\n');
        info.append("saveHandler: ").append(WDL.saveHandler).append('\n');
        info.append("chunkLoader: ").append(WDL.chunkLoader).append('\n');
        info.append("newTileEntities: ").append(WDL.newTileEntities).append('\n');
        info.append("newEntities: ").append(WDL.newEntities).append('\n');
        info.append("newMapDatas: ").append(WDL.newMapDatas).append('\n');
        info.append("downloading: ").append(WDL.downloading).append('\n');
        info.append("isMultiworld: ").append(WDL.isMultiworld).append('\n');
        info.append("propsFound: ").append(WDL.propsFound).append('\n');
        info.append("startOnChange: ").append(WDL.startOnChange).append('\n');
        info.append("overrideLastModifiedCheck: ").append(WDL.overrideLastModifiedCheck).append('\n');
        info.append("saving: ").append(WDL.saving).append('\n');
        info.append("worldLoadingDeferred: ").append(WDL.worldLoadingDeferred).append('\n');
        info.append("worldName: ").append(WDL.worldName).append('\n');
        info.append("baseFolderName: ").append(WDL.baseFolderName).append('\n');
        info.append("### CONNECTED SERVER\n\n");
        final ServerData data = Minecraft.getMinecraft().getCurrentServerData();
        if (data == null) {
            info.append("No data\n");
        }
        else {
            info.append("Name: ").append(data.serverName).append('\n');
            info.append("IP: ").append(data.serverIP).append('\n');
        }
        info.append("\n### PROPERTIES\n\n");
        info.append("\n#### BASE\n\n");
        if (WDL.baseProps != null) {
            if (!WDL.baseProps.isEmpty()) {
                for (final Map.Entry<Object, Object> e3 : WDL.baseProps.entrySet()) {
                    info.append(e3.getKey()).append(": ").append(e3.getValue());
                    info.append('\n');
                }
            }
            else {
                info.append("empty\n");
            }
        }
        else {
            info.append("null\n");
        }
        info.append("\n#### WORLD\n\n");
        if (WDL.worldProps != null) {
            if (!WDL.worldProps.isEmpty()) {
                for (final Map.Entry<Object, Object> e3 : WDL.worldProps.entrySet()) {
                    info.append(e3.getKey()).append(": ").append(e3.getValue());
                    info.append('\n');
                }
            }
            else {
                info.append("empty\n");
            }
        }
        else {
            info.append("null\n");
        }
        info.append("\n#### DEFAULT\n\n");
        if (WDL.globalProps != null) {
            if (!WDL.globalProps.isEmpty()) {
                for (final Map.Entry<Object, Object> e3 : WDL.globalProps.entrySet()) {
                    info.append(e3.getKey()).append(": ").append(e3.getValue());
                    info.append('\n');
                }
            }
            else {
                info.append("empty\n");
            }
        }
        else {
            info.append("null\n");
        }
        return info.toString();
    }
    
    public static void crashed(final Throwable t, final String category) {
        CrashReport report;
        if (t instanceof ReportedException) {
            final CrashReport oldReport = ((ReportedException)t).getCrashReport();
            report = CrashReport.makeCrashReport(oldReport.getCrashCause(), String.valueOf(category) + " (" + oldReport.getCauseStackTraceOrString() + ")");
            try {
                final List<CrashReportCategory> crashReportSectionsOld = ReflectionUtils.stealAndGetField(oldReport, List.class);
                final List<CrashReportCategory> crashReportSectionsNew = ReflectionUtils.stealAndGetField(report, List.class);
                crashReportSectionsNew.addAll(crashReportSectionsOld);
            }
            catch (final Exception e) {
                report.makeCategory("An exception occured while trying to copy the origional categories.").addCrashSectionThrowable(":(", e);
            }
        }
        else {
            report = CrashReport.makeCrashReport(t, category);
        }
        WDL.minecraft.crashed(report);
    }
    
    public static String getMinecraftVersion() {
        final Map<?, ?> map = Minecraft.getSessionInfo();
        if (map.containsKey("X-Minecraft-Version")) {
            return (String)map.get("X-Minecraft-Version");
        }
        return "1.8.9";
    }
    
    public static String getMinecraftVersionInfo() {
        final String version = getMinecraftVersion();
        final String launchedVersion = Minecraft.getMinecraft().getVersion();
        final String brand = ClientBrandRetriever.getClientModName();
        return String.format("Minecraft %s (%s/%s)", version, launchedVersion, brand);
    }
}
