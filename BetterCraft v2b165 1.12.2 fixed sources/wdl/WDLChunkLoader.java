// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import wdl.api.ITileEntityImportationIdentifier;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.BlockNote;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockChest;
import java.io.DataInputStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.chunk.storage.RegionFileCache;
import java.util.Set;
import wdl.api.ITileEntityEditor;
import java.util.HashSet;
import java.util.HashMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.network.datasync.EntityDataManager;
import wdl.api.IWDLMessageType;
import wdl.api.WDLApi;
import wdl.api.IEntityEditor;
import java.util.Collection;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.nbt.NBTTagList;
import java.io.IOException;
import net.minecraft.world.MinecraftException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import net.minecraft.util.datafix.DataFixer;
import java.lang.reflect.Method;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.SaveHandler;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public class WDLChunkLoader extends AnvilChunkLoader
{
    private static Logger logger;
    private final File chunkSaveLocation;
    
    static {
        WDLChunkLoader.logger = LogManager.getLogger();
    }
    
    public static WDLChunkLoader create(final SaveHandler handler, final WorldProvider provider) {
        return new WDLChunkLoader(getWorldSaveFolder(handler, provider));
    }
    
    private static File getWorldSaveFolder(final SaveHandler handler, final WorldProvider provider) {
        final File baseFolder = handler.getWorldDirectory();
        try {
            final Method forgeGetSaveFolderMethod = provider.getClass().getMethod("getSaveFolder", (Class<?>[])new Class[0]);
            final String name = (String)forgeGetSaveFolderMethod.invoke(provider, new Object[0]);
            if (name != null) {
                final File file = new File(baseFolder, name);
                file.mkdirs();
                return file;
            }
            return baseFolder;
        }
        catch (final Exception e) {
            if (provider instanceof WorldProviderHell) {
                final File file2 = new File(baseFolder, "DIM-1");
                file2.mkdirs();
                return file2;
            }
            if (provider instanceof WorldProviderEnd) {
                final File file2 = new File(baseFolder, "DIM1");
                file2.mkdirs();
                return file2;
            }
            return baseFolder;
        }
    }
    
    public WDLChunkLoader(final File file) {
        super(file, null);
        this.chunkSaveLocation = file;
    }
    
    @Override
    public void saveChunk(final World world, final Chunk chunk) throws MinecraftException, IOException {
        world.checkSessionLock();
        final NBTTagCompound levelTag = this.writeChunkToNBT(chunk, world);
        final NBTTagCompound rootTag = new NBTTagCompound();
        rootTag.setTag("Level", levelTag);
        rootTag.setInteger("DataVersion", 510);
        this.addChunkToPending(chunk.getChunkCoordIntPair(), rootTag);
    }
    
    private NBTTagCompound writeChunkToNBT(final Chunk chunk, final World world) {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setByte("V", (byte)1);
        compound.setInteger("xPos", chunk.xPosition);
        compound.setInteger("zPos", chunk.zPosition);
        compound.setLong("LastUpdate", world.getTotalWorldTime());
        compound.setIntArray("HeightMap", chunk.getHeightMap());
        compound.setBoolean("TerrainPopulated", chunk.isTerrainPopulated());
        compound.setBoolean("LightPopulated", chunk.isLightPopulated());
        compound.setLong("InhabitedTime", chunk.getInhabitedTime());
        final ExtendedBlockStorage[] blockStorageArray = chunk.getBlockStorageArray();
        final NBTTagList blockStorageList = new NBTTagList();
        final boolean hasNoSky = !world.provider.getHasNoSky();
        ExtendedBlockStorage[] array;
        for (int length = (array = blockStorageArray).length, i = 0; i < length; ++i) {
            final ExtendedBlockStorage blockStorage = array[i];
            if (blockStorage != null) {
                final NBTTagCompound blockData = new NBTTagCompound();
                blockData.setByte("Y", (byte)(blockStorage.getYLocation() >> 4 & 0xFF));
                final byte[] buffer = new byte[4096];
                final NibbleArray nibblearray = new NibbleArray();
                final NibbleArray nibblearray2 = blockStorage.getData().getDataForNBT(buffer, nibblearray);
                blockData.setByteArray("Blocks", buffer);
                blockData.setByteArray("Data", nibblearray.getData());
                if (nibblearray2 != null) {
                    blockData.setByteArray("Add", nibblearray2.getData());
                }
                blockData.setByteArray("BlockLight", blockStorage.getBlocklightArray().getData());
                if (hasNoSky) {
                    blockData.setByteArray("SkyLight", blockStorage.getSkylightArray().getData());
                }
                else {
                    blockData.setByteArray("SkyLight", new byte[blockStorage.getBlocklightArray().getData().length]);
                }
                blockStorageList.appendTag(blockData);
            }
        }
        compound.setTag("Sections", blockStorageList);
        compound.setByteArray("Biomes", chunk.getBiomeArray());
        chunk.setHasEntities(false);
        final NBTTagList entityList = this.getEntityList(chunk);
        compound.setTag("Entities", entityList);
        final NBTTagList tileEntityList = this.getTileEntityList(chunk);
        compound.setTag("TileEntities", tileEntityList);
        final List<NextTickListEntry> updateList = world.getPendingBlockUpdates(chunk, false);
        if (updateList != null) {
            final long worldTime = world.getTotalWorldTime();
            final NBTTagList entries = new NBTTagList();
            for (final NextTickListEntry entry : updateList) {
                final NBTTagCompound entryTag = new NBTTagCompound();
                final ResourceLocation location = Block.REGISTRY.getNameForObject(entry.getBlock());
                entryTag.setString("i", (location == null) ? "" : location.toString());
                entryTag.setInteger("x", entry.position.getX());
                entryTag.setInteger("y", entry.position.getY());
                entryTag.setInteger("z", entry.position.getZ());
                entryTag.setInteger("t", (int)(entry.scheduledTime - worldTime));
                entryTag.setInteger("p", entry.priority);
                entries.appendTag(entryTag);
            }
            compound.setTag("TileTicks", entries);
        }
        return compound;
    }
    
    public NBTTagList getEntityList(final Chunk chunk) {
        final NBTTagList entityList = new NBTTagList();
        if (!WDLPluginChannels.canSaveEntities(chunk)) {
            return entityList;
        }
        final List<Entity> entities = new ArrayList<Entity>();
        ClassInheritanceMultiMap<Entity>[] entityLists;
        for (int length = (entityLists = chunk.getEntityLists()).length, i = 0; i < length; ++i) {
            final ClassInheritanceMultiMap<Entity> map = entityLists[i];
            entities.addAll(map);
        }
        for (final Entity e : WDL.newEntities.get((Object)chunk.getChunkCoordIntPair())) {
            e.isDead = false;
            entities.add(e);
        }
        for (final Entity entity : entities) {
            if (entity == null) {
                WDLChunkLoader.logger.warn("[WDL] Null entity in chunk at " + chunk.getChunkCoordIntPair());
            }
            else {
                if (!shouldSaveEntity(entity)) {
                    continue;
                }
                for (final WDLApi.ModInfo<IEntityEditor> info : WDLApi.getImplementingExtensions(IEntityEditor.class)) {
                    try {
                        if (!info.mod.shouldEdit(entity)) {
                            continue;
                        }
                        info.mod.editEntity(entity);
                    }
                    catch (final Exception ex) {
                        throw new RuntimeException("Failed to edit entity " + entity + " for chunk at " + chunk.getChunkCoordIntPair() + " with extension " + info, ex);
                    }
                }
                final NBTTagCompound entityData = new NBTTagCompound();
                try {
                    if (!entity.writeToNBTOptional(entityData)) {
                        continue;
                    }
                    chunk.setHasEntities(true);
                    entityList.appendTag(entityData);
                }
                catch (final Exception e2) {
                    WDLMessages.chatMessageTranslated(WDLMessageTypes.ERROR, "wdl.messages.generalError.failedToSaveEntity", entity, chunk.xPosition, chunk.zPosition, e2);
                    WDLChunkLoader.logger.warn("Compound: " + entityData);
                    WDLChunkLoader.logger.warn("Entity metadata dump:");
                    try {
                        final List<EntityDataManager.DataEntry<?>> objects = entity.getDataManager().getAll();
                        if (objects == null) {
                            WDLChunkLoader.logger.warn("No entries (getAllWatched() returned null)");
                        }
                        else {
                            WDLChunkLoader.logger.warn(objects);
                            for (final EntityDataManager.DataEntry<?> obj : objects) {
                                if (obj != null) {
                                    WDLChunkLoader.logger.warn("DataEntry [getValue()=" + obj.getValue() + ", isDirty()=" + obj.isDirty() + ", getKey()=" + "DataParameter [" + "getId()=" + obj.getKey().getId() + ", getSerializer()=" + obj.getKey().getSerializer() + "]]");
                                }
                            }
                        }
                    }
                    catch (final Exception e3) {
                        WDLChunkLoader.logger.warn("Failed to complete dump: ", e2);
                    }
                    WDLChunkLoader.logger.warn("End entity metadata dump");
                }
            }
        }
        return entityList;
    }
    
    public static boolean shouldSaveEntity(final Entity e) {
        if (e instanceof EntityPlayer) {
            return false;
        }
        if (!EntityUtils.isEntityEnabled(e)) {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.REMOVE_ENTITY, "wdl.messages.removeEntity.notSavingUserPreference", e);
            return false;
        }
        return true;
    }
    
    public NBTTagList getTileEntityList(final Chunk chunk) {
        final NBTTagList tileEntityList = new NBTTagList();
        if (!WDLPluginChannels.canSaveTileEntities(chunk)) {
            return tileEntityList;
        }
        final Map<BlockPos, TileEntity> chunkTEMap = chunk.getTileEntityMap();
        final Map<BlockPos, NBTTagCompound> oldTEMap = this.getOldTileEntities(chunk);
        Map<BlockPos, TileEntity> newTEMap = WDL.newTileEntities.get(chunk.getChunkCoordIntPair());
        if (newTEMap == null) {
            newTEMap = new HashMap<BlockPos, TileEntity>();
        }
        final Set<BlockPos> allTELocations = new HashSet<BlockPos>();
        allTELocations.addAll(chunkTEMap.keySet());
        allTELocations.addAll(oldTEMap.keySet());
        allTELocations.addAll(newTEMap.keySet());
        for (final BlockPos pos : allTELocations) {
            if (newTEMap.containsKey(pos)) {
                final NBTTagCompound compound = new NBTTagCompound();
                final TileEntity te = newTEMap.get(pos);
                try {
                    te.writeToNBT(compound);
                }
                catch (final Exception e) {
                    WDLMessages.chatMessageTranslated(WDLMessageTypes.ERROR, "wdl.messages.generalError.failedToSaveTE", te, pos, chunk.xPosition, chunk.zPosition, e);
                    WDLChunkLoader.logger.warn("Compound: " + compound);
                    continue;
                }
                final String entityType = String.valueOf(compound.getString("id")) + " (" + te.getClass().getCanonicalName() + ")";
                WDLMessages.chatMessageTranslated(WDLMessageTypes.LOAD_TILE_ENTITY, "wdl.messages.tileEntity.usingNew", entityType, pos);
                editTileEntity(pos, compound, ITileEntityEditor.TileEntityCreationMode.NEW);
                tileEntityList.appendTag(compound);
            }
            else if (oldTEMap.containsKey(pos)) {
                final NBTTagCompound compound = oldTEMap.get(pos);
                final String entityType2 = compound.getString("id");
                WDLMessages.chatMessageTranslated(WDLMessageTypes.LOAD_TILE_ENTITY, "wdl.messages.tileEntity.usingOld", entityType2, pos);
                editTileEntity(pos, compound, ITileEntityEditor.TileEntityCreationMode.IMPORTED);
                tileEntityList.appendTag(compound);
            }
            else {
                if (!chunkTEMap.containsKey(pos)) {
                    continue;
                }
                final TileEntity te2 = chunkTEMap.get(pos);
                final NBTTagCompound compound2 = new NBTTagCompound();
                try {
                    te2.writeToNBT(compound2);
                }
                catch (final Exception e) {
                    WDLMessages.chatMessageTranslated(WDLMessageTypes.ERROR, "wdl.messages.generalError.failedToSaveTE", te2, pos, chunk.xPosition, chunk.zPosition, e);
                    WDLChunkLoader.logger.warn("Compound: " + compound2);
                    continue;
                }
                editTileEntity(pos, compound2, ITileEntityEditor.TileEntityCreationMode.EXISTING);
                tileEntityList.appendTag(compound2);
            }
        }
        return tileEntityList;
    }
    
    public Map<BlockPos, NBTTagCompound> getOldTileEntities(final Chunk chunk) {
        DataInputStream dis = null;
        final Map<BlockPos, NBTTagCompound> returned = new HashMap<BlockPos, NBTTagCompound>();
        try {
            dis = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, chunk.xPosition, chunk.zPosition);
            if (dis == null) {
                return returned;
            }
            final NBTTagCompound chunkNBT = CompressedStreamTools.read(dis);
            final NBTTagCompound levelNBT = chunkNBT.getCompoundTag("Level");
            final NBTTagList oldList = levelNBT.getTagList("TileEntities", 10);
            if (oldList != null) {
                for (int i = 0; i < oldList.tagCount(); ++i) {
                    final NBTTagCompound oldNBT = oldList.getCompoundTagAt(i);
                    final String entityID = oldNBT.getString("id");
                    final BlockPos pos = new BlockPos(oldNBT.getInteger("x"), oldNBT.getInteger("y"), oldNBT.getInteger("z"));
                    final Block block = chunk.getBlockState(pos).getBlock();
                    if (this.shouldImportTileEntity(entityID, pos, block, oldNBT, chunk)) {
                        returned.put(pos, oldNBT);
                    }
                    else {
                        WDLMessages.chatMessageTranslated(WDLMessageTypes.LOAD_TILE_ENTITY, "wdl.messages.tileEntity.notImporting", entityID, pos);
                    }
                }
            }
        }
        catch (final Exception e) {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.ERROR, "wdl.messages.generalError.failedToImportTE", chunk.xPosition, chunk.zPosition, e);
            if (dis == null) {
                return returned;
            }
            try {
                dis.close();
            }
            catch (final Exception e2) {
                throw new RuntimeException(e2);
            }
        }
        finally {
            if (dis != null) {
                try {
                    dis.close();
                }
                catch (final Exception e2) {
                    throw new RuntimeException(e2);
                }
            }
        }
        if (dis != null) {
            try {
                dis.close();
            }
            catch (final Exception e2) {
                throw new RuntimeException(e2);
            }
        }
        return returned;
    }
    
    public boolean shouldImportTileEntity(final String entityID, final BlockPos pos, final Block block, final NBTTagCompound tileEntityNBT, final Chunk chunk) {
        if (block instanceof BlockChest && entityID.equals("Chest")) {
            return true;
        }
        if (block instanceof BlockDispenser && entityID.equals("Trap")) {
            return true;
        }
        if (block instanceof BlockDropper && entityID.equals("Dropper")) {
            return true;
        }
        if (block instanceof BlockFurnace && entityID.equals("Furnace")) {
            return true;
        }
        if (block instanceof BlockNote && entityID.equals("Music")) {
            return true;
        }
        if (block instanceof BlockBrewingStand && entityID.equals("Cauldron")) {
            return true;
        }
        if (block instanceof BlockHopper && entityID.equals("Hopper")) {
            return true;
        }
        if (block instanceof BlockBeacon && entityID.equals("Beacon")) {
            return true;
        }
        for (final WDLApi.ModInfo<ITileEntityImportationIdentifier> info : WDLApi.getImplementingExtensions(ITileEntityImportationIdentifier.class)) {
            if (info.mod.shouldImportTileEntity(entityID, pos, block, tileEntityNBT, chunk)) {
                return true;
            }
        }
        return false;
    }
    
    public static void editTileEntity(final BlockPos pos, final NBTTagCompound compound, final ITileEntityEditor.TileEntityCreationMode creationMode) {
        for (final WDLApi.ModInfo<ITileEntityEditor> info : WDLApi.getImplementingExtensions(ITileEntityEditor.class)) {
            try {
                if (!info.mod.shouldEdit(pos, compound, creationMode)) {
                    continue;
                }
                info.mod.editTileEntity(pos, compound, creationMode);
                WDLMessages.chatMessageTranslated(WDLMessageTypes.LOAD_TILE_ENTITY, "wdl.messages.tileEntity.edited", pos, info.getDisplayName());
            }
            catch (final Exception ex) {
                throw new RuntimeException("Failed to edit tile entity at " + pos + " with extension " + info + "; NBT is now " + compound + " (this may be the " + "initial value, an edited value, or a partially " + "edited value)", ex);
            }
        }
    }
}
