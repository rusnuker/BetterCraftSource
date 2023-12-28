// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.chunk.storage;

import net.minecraft.entity.EntityList;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import java.io.DataOutputStream;
import java.io.DataOutput;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.world.MinecraftException;
import net.minecraft.nbt.NBTBase;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.DataInputStream;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import java.util.Collections;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.datafix.DataFixer;
import java.io.File;
import java.util.Set;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.storage.IThreadedFileIO;

public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO
{
    private static final Logger LOGGER;
    private final Map<ChunkPos, NBTTagCompound> chunksToRemove;
    private final Set<ChunkPos> field_193415_c;
    private final File chunkSaveLocation;
    private final DataFixer field_193416_e;
    private boolean savingExtraData;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public AnvilChunkLoader(final File chunkSaveLocationIn, final DataFixer dataFixerIn) {
        this.chunksToRemove = (Map<ChunkPos, NBTTagCompound>)Maps.newConcurrentMap();
        this.field_193415_c = Collections.newSetFromMap((Map<ChunkPos, Boolean>)Maps.newConcurrentMap());
        this.chunkSaveLocation = chunkSaveLocationIn;
        this.field_193416_e = dataFixerIn;
    }
    
    @Nullable
    @Override
    public Chunk loadChunk(final World worldIn, final int x, final int z) throws IOException {
        final ChunkPos chunkpos = new ChunkPos(x, z);
        NBTTagCompound nbttagcompound = this.chunksToRemove.get(chunkpos);
        if (nbttagcompound == null) {
            final DataInputStream datainputstream = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, x, z);
            if (datainputstream == null) {
                return null;
            }
            nbttagcompound = this.field_193416_e.process(FixTypes.CHUNK, CompressedStreamTools.read(datainputstream));
        }
        return this.checkedReadChunkFromNBT(worldIn, x, z, nbttagcompound);
    }
    
    @Override
    public boolean func_191063_a(final int p_191063_1_, final int p_191063_2_) {
        final ChunkPos chunkpos = new ChunkPos(p_191063_1_, p_191063_2_);
        final NBTTagCompound nbttagcompound = this.chunksToRemove.get(chunkpos);
        return nbttagcompound != null || RegionFileCache.func_191064_f(this.chunkSaveLocation, p_191063_1_, p_191063_2_);
    }
    
    @Nullable
    protected Chunk checkedReadChunkFromNBT(final World worldIn, final int x, final int z, final NBTTagCompound compound) {
        if (!compound.hasKey("Level", 10)) {
            AnvilChunkLoader.LOGGER.error("Chunk file at {},{} is missing level data, skipping", (Object)x, z);
            return null;
        }
        final NBTTagCompound nbttagcompound = compound.getCompoundTag("Level");
        if (!nbttagcompound.hasKey("Sections", 9)) {
            AnvilChunkLoader.LOGGER.error("Chunk file at {},{} is missing block data, skipping", (Object)x, z);
            return null;
        }
        Chunk chunk = this.readChunkFromNBT(worldIn, nbttagcompound);
        if (!chunk.isAtLocation(x, z)) {
            AnvilChunkLoader.LOGGER.error("Chunk file at {},{} is in the wrong location; relocating. (Expected {}, {}, got {}, {})", (Object)x, z, x, z, chunk.xPosition, chunk.zPosition);
            nbttagcompound.setInteger("xPos", x);
            nbttagcompound.setInteger("zPos", z);
            chunk = this.readChunkFromNBT(worldIn, nbttagcompound);
        }
        return chunk;
    }
    
    @Override
    public void saveChunk(final World worldIn, final Chunk chunkIn) throws MinecraftException, IOException {
        worldIn.checkSessionLock();
        try {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            nbttagcompound.setTag("Level", nbttagcompound2);
            nbttagcompound.setInteger("DataVersion", 1343);
            this.writeChunkToNBT(chunkIn, worldIn, nbttagcompound2);
            this.addChunkToPending(chunkIn.getChunkCoordIntPair(), nbttagcompound);
        }
        catch (final Exception exception) {
            AnvilChunkLoader.LOGGER.error("Failed to save chunk", exception);
        }
    }
    
    protected void addChunkToPending(final ChunkPos pos, final NBTTagCompound compound) {
        if (!this.field_193415_c.contains(pos)) {
            this.chunksToRemove.put(pos, compound);
        }
        ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
    }
    
    @Override
    public boolean writeNextIO() {
        if (this.chunksToRemove.isEmpty()) {
            if (this.savingExtraData) {
                AnvilChunkLoader.LOGGER.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", this.chunkSaveLocation.getName());
            }
            return false;
        }
        final ChunkPos chunkpos = this.chunksToRemove.keySet().iterator().next();
        boolean lvt_3_1_;
        try {
            this.field_193415_c.add(chunkpos);
            final NBTTagCompound nbttagcompound = this.chunksToRemove.remove(chunkpos);
            if (nbttagcompound != null) {
                try {
                    this.writeChunkData(chunkpos, nbttagcompound);
                }
                catch (final Exception exception) {
                    AnvilChunkLoader.LOGGER.error("Failed to save chunk", exception);
                }
            }
            lvt_3_1_ = true;
        }
        finally {
            this.field_193415_c.remove(chunkpos);
        }
        this.field_193415_c.remove(chunkpos);
        return lvt_3_1_;
    }
    
    private void writeChunkData(final ChunkPos pos, final NBTTagCompound compound) throws IOException {
        final DataOutputStream dataoutputstream = RegionFileCache.getChunkOutputStream(this.chunkSaveLocation, pos.chunkXPos, pos.chunkZPos);
        CompressedStreamTools.write(compound, dataoutputstream);
        dataoutputstream.close();
    }
    
    @Override
    public void saveExtraChunkData(final World worldIn, final Chunk chunkIn) throws IOException {
    }
    
    @Override
    public void chunkTick() {
    }
    
    @Override
    public void saveExtraData() {
        try {
            this.savingExtraData = true;
            while (this.writeNextIO()) {}
        }
        finally {
            this.savingExtraData = false;
        }
        this.savingExtraData = false;
    }
    
    public static void registerFixes(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.CHUNK, new IDataWalker() {
            @Override
            public NBTTagCompound process(final IDataFixer fixer, final NBTTagCompound compound, final int versionIn) {
                if (compound.hasKey("Level", 10)) {
                    final NBTTagCompound nbttagcompound = compound.getCompoundTag("Level");
                    if (nbttagcompound.hasKey("Entities", 9)) {
                        final NBTTagList nbttaglist = nbttagcompound.getTagList("Entities", 10);
                        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                            nbttaglist.set(i, fixer.process(FixTypes.ENTITY, (NBTTagCompound)nbttaglist.get(i), versionIn));
                        }
                    }
                    if (nbttagcompound.hasKey("TileEntities", 9)) {
                        final NBTTagList nbttaglist2 = nbttagcompound.getTagList("TileEntities", 10);
                        for (int j = 0; j < nbttaglist2.tagCount(); ++j) {
                            nbttaglist2.set(j, fixer.process(FixTypes.BLOCK_ENTITY, (NBTTagCompound)nbttaglist2.get(j), versionIn));
                        }
                    }
                }
                return compound;
            }
        });
    }
    
    private void writeChunkToNBT(final Chunk chunkIn, final World worldIn, final NBTTagCompound compound) {
        compound.setInteger("xPos", chunkIn.xPosition);
        compound.setInteger("zPos", chunkIn.zPosition);
        compound.setLong("LastUpdate", worldIn.getTotalWorldTime());
        compound.setIntArray("HeightMap", chunkIn.getHeightMap());
        compound.setBoolean("TerrainPopulated", chunkIn.isTerrainPopulated());
        compound.setBoolean("LightPopulated", chunkIn.isLightPopulated());
        compound.setLong("InhabitedTime", chunkIn.getInhabitedTime());
        final ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
        final NBTTagList nbttaglist = new NBTTagList();
        final boolean flag = worldIn.provider.func_191066_m();
        ExtendedBlockStorage[] array;
        for (int length = (array = aextendedblockstorage).length, k = 0; k < length; ++k) {
            final ExtendedBlockStorage extendedblockstorage = array[k];
            if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Y", (byte)(extendedblockstorage.getYLocation() >> 4 & 0xFF));
                final byte[] abyte = new byte[4096];
                final NibbleArray nibblearray = new NibbleArray();
                final NibbleArray nibblearray2 = extendedblockstorage.getData().getDataForNBT(abyte, nibblearray);
                nbttagcompound.setByteArray("Blocks", abyte);
                nbttagcompound.setByteArray("Data", nibblearray.getData());
                if (nibblearray2 != null) {
                    nbttagcompound.setByteArray("Add", nibblearray2.getData());
                }
                nbttagcompound.setByteArray("BlockLight", extendedblockstorage.getBlocklightArray().getData());
                if (flag) {
                    nbttagcompound.setByteArray("SkyLight", extendedblockstorage.getSkylightArray().getData());
                }
                else {
                    nbttagcompound.setByteArray("SkyLight", new byte[extendedblockstorage.getBlocklightArray().getData().length]);
                }
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        compound.setTag("Sections", nbttaglist);
        compound.setByteArray("Biomes", chunkIn.getBiomeArray());
        chunkIn.setHasEntities(false);
        final NBTTagList nbttaglist2 = new NBTTagList();
        for (int i = 0; i < chunkIn.getEntityLists().length; ++i) {
            for (final Entity entity : chunkIn.getEntityLists()[i]) {
                final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                if (entity.writeToNBTOptional(nbttagcompound2)) {
                    chunkIn.setHasEntities(true);
                    nbttaglist2.appendTag(nbttagcompound2);
                }
            }
        }
        compound.setTag("Entities", nbttaglist2);
        final NBTTagList nbttaglist3 = new NBTTagList();
        for (final TileEntity tileentity : chunkIn.getTileEntityMap().values()) {
            final NBTTagCompound nbttagcompound3 = tileentity.writeToNBT(new NBTTagCompound());
            nbttaglist3.appendTag(nbttagcompound3);
        }
        compound.setTag("TileEntities", nbttaglist3);
        final List<NextTickListEntry> list = worldIn.getPendingBlockUpdates(chunkIn, false);
        if (list != null) {
            final long j = worldIn.getTotalWorldTime();
            final NBTTagList nbttaglist4 = new NBTTagList();
            for (final NextTickListEntry nextticklistentry : list) {
                final NBTTagCompound nbttagcompound4 = new NBTTagCompound();
                final ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(nextticklistentry.getBlock());
                nbttagcompound4.setString("i", (resourcelocation == null) ? "" : resourcelocation.toString());
                nbttagcompound4.setInteger("x", nextticklistentry.position.getX());
                nbttagcompound4.setInteger("y", nextticklistentry.position.getY());
                nbttagcompound4.setInteger("z", nextticklistentry.position.getZ());
                nbttagcompound4.setInteger("t", (int)(nextticklistentry.scheduledTime - j));
                nbttagcompound4.setInteger("p", nextticklistentry.priority);
                nbttaglist4.appendTag(nbttagcompound4);
            }
            compound.setTag("TileTicks", nbttaglist4);
        }
    }
    
    private Chunk readChunkFromNBT(final World worldIn, final NBTTagCompound compound) {
        final int i = compound.getInteger("xPos");
        final int j = compound.getInteger("zPos");
        final Chunk chunk = new Chunk(worldIn, i, j);
        chunk.setHeightMap(compound.getIntArray("HeightMap"));
        chunk.setTerrainPopulated(compound.getBoolean("TerrainPopulated"));
        chunk.setLightPopulated(compound.getBoolean("LightPopulated"));
        chunk.setInhabitedTime(compound.getLong("InhabitedTime"));
        final NBTTagList nbttaglist = compound.getTagList("Sections", 10);
        final int k = 16;
        final ExtendedBlockStorage[] aextendedblockstorage = new ExtendedBlockStorage[16];
        final boolean flag = worldIn.provider.func_191066_m();
        for (int l = 0; l < nbttaglist.tagCount(); ++l) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(l);
            final int i2 = nbttagcompound.getByte("Y");
            final ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(i2 << 4, flag);
            final byte[] abyte = nbttagcompound.getByteArray("Blocks");
            final NibbleArray nibblearray = new NibbleArray(nbttagcompound.getByteArray("Data"));
            final NibbleArray nibblearray2 = nbttagcompound.hasKey("Add", 7) ? new NibbleArray(nbttagcompound.getByteArray("Add")) : null;
            extendedblockstorage.getData().setDataFromNBT(abyte, nibblearray, nibblearray2);
            extendedblockstorage.setBlocklightArray(new NibbleArray(nbttagcompound.getByteArray("BlockLight")));
            if (flag) {
                extendedblockstorage.setSkylightArray(new NibbleArray(nbttagcompound.getByteArray("SkyLight")));
            }
            extendedblockstorage.removeInvalidBlocks();
            aextendedblockstorage[i2] = extendedblockstorage;
        }
        chunk.setStorageArrays(aextendedblockstorage);
        if (compound.hasKey("Biomes", 7)) {
            chunk.setBiomeArray(compound.getByteArray("Biomes"));
        }
        final NBTTagList nbttaglist2 = compound.getTagList("Entities", 10);
        for (int j2 = 0; j2 < nbttaglist2.tagCount(); ++j2) {
            final NBTTagCompound nbttagcompound2 = nbttaglist2.getCompoundTagAt(j2);
            readChunkEntity(nbttagcompound2, worldIn, chunk);
            chunk.setHasEntities(true);
        }
        final NBTTagList nbttaglist3 = compound.getTagList("TileEntities", 10);
        for (int k2 = 0; k2 < nbttaglist3.tagCount(); ++k2) {
            final NBTTagCompound nbttagcompound3 = nbttaglist3.getCompoundTagAt(k2);
            final TileEntity tileentity = TileEntity.create(worldIn, nbttagcompound3);
            if (tileentity != null) {
                chunk.addTileEntity(tileentity);
            }
        }
        if (compound.hasKey("TileTicks", 9)) {
            final NBTTagList nbttaglist4 = compound.getTagList("TileTicks", 10);
            for (int l2 = 0; l2 < nbttaglist4.tagCount(); ++l2) {
                final NBTTagCompound nbttagcompound4 = nbttaglist4.getCompoundTagAt(l2);
                Block block;
                if (nbttagcompound4.hasKey("i", 8)) {
                    block = Block.getBlockFromName(nbttagcompound4.getString("i"));
                }
                else {
                    block = Block.getBlockById(nbttagcompound4.getInteger("i"));
                }
                worldIn.scheduleBlockUpdate(new BlockPos(nbttagcompound4.getInteger("x"), nbttagcompound4.getInteger("y"), nbttagcompound4.getInteger("z")), block, nbttagcompound4.getInteger("t"), nbttagcompound4.getInteger("p"));
            }
        }
        return chunk;
    }
    
    @Nullable
    public static Entity readChunkEntity(final NBTTagCompound compound, final World worldIn, final Chunk chunkIn) {
        final Entity entity = createEntityFromNBT(compound, worldIn);
        if (entity == null) {
            return null;
        }
        chunkIn.addEntity(entity);
        if (compound.hasKey("Passengers", 9)) {
            final NBTTagList nbttaglist = compound.getTagList("Passengers", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final Entity entity2 = readChunkEntity(nbttaglist.getCompoundTagAt(i), worldIn, chunkIn);
                if (entity2 != null) {
                    entity2.startRiding(entity, true);
                }
            }
        }
        return entity;
    }
    
    @Nullable
    public static Entity readWorldEntityPos(final NBTTagCompound compound, final World worldIn, final double x, final double y, final double z, final boolean attemptSpawn) {
        final Entity entity = createEntityFromNBT(compound, worldIn);
        if (entity == null) {
            return null;
        }
        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
        if (attemptSpawn && !worldIn.spawnEntityInWorld(entity)) {
            return null;
        }
        if (compound.hasKey("Passengers", 9)) {
            final NBTTagList nbttaglist = compound.getTagList("Passengers", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final Entity entity2 = readWorldEntityPos(nbttaglist.getCompoundTagAt(i), worldIn, x, y, z, attemptSpawn);
                if (entity2 != null) {
                    entity2.startRiding(entity, true);
                }
            }
        }
        return entity;
    }
    
    @Nullable
    protected static Entity createEntityFromNBT(final NBTTagCompound compound, final World worldIn) {
        try {
            return EntityList.createEntityFromNBT(compound, worldIn);
        }
        catch (final RuntimeException var3) {
            return null;
        }
    }
    
    public static void spawnEntity(final Entity entityIn, final World worldIn) {
        if (worldIn.spawnEntityInWorld(entityIn) && entityIn.isBeingRidden()) {
            for (final Entity entity : entityIn.getPassengers()) {
                spawnEntity(entity, worldIn);
            }
        }
    }
    
    @Nullable
    public static Entity readWorldEntity(final NBTTagCompound compound, final World worldIn, final boolean p_186051_2_) {
        final Entity entity = createEntityFromNBT(compound, worldIn);
        if (entity == null) {
            return null;
        }
        if (p_186051_2_ && !worldIn.spawnEntityInWorld(entity)) {
            return null;
        }
        if (compound.hasKey("Passengers", 9)) {
            final NBTTagList nbttaglist = compound.getTagList("Passengers", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final Entity entity2 = readWorldEntity(nbttaglist.getCompoundTagAt(i), worldIn, p_186051_2_);
                if (entity2 != null) {
                    entity2.startRiding(entity, true);
                }
            }
        }
        return entity;
    }
}
