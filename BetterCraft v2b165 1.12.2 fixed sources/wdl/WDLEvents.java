// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.entity.Entity;
import net.minecraft.world.storage.MapData;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.IMerchant;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import wdl.api.WDLApi;
import wdl.api.IWorldLoadListener;
import wdl.update.WDLUpdateChecker;
import wdl.api.IWDLMessageType;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;

public class WDLEvents
{
    private static final Profiler profiler;
    
    static {
        profiler = Minecraft.getMinecraft().mcProfiler;
    }
    
    public static void onWorldLoad(final WorldClient world) {
        WDLEvents.profiler.startSection("Core");
        if (WDL.minecraft.isIntegratedServerRunning()) {
            return;
        }
        if (WDL.downloading) {
            if (!WDL.saving) {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.INFO, "wdl.messages.generalInfo.worldChanged", new Object[0]);
                WDL.worldLoadingDeferred = true;
                WDL.startSaveThread();
            }
            WDLEvents.profiler.endSection();
            return;
        }
        final boolean sameServer = WDL.loadWorld();
        WDLUpdateChecker.startIfNeeded();
        WDLEvents.profiler.endSection();
        for (final WDLApi.ModInfo<IWorldLoadListener> info : WDLApi.getImplementingExtensions(IWorldLoadListener.class)) {
            WDLEvents.profiler.startSection(info.id);
            info.mod.onWorldLoad(world, sameServer);
            WDLEvents.profiler.endSection();
        }
    }
    
    public static void onChunkNoLongerNeeded(final Chunk unneededChunk) {
        if (!WDL.downloading) {
            return;
        }
        if (unneededChunk == null) {
            return;
        }
        if (WDLPluginChannels.canSaveChunk(unneededChunk)) {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_CHUNK_NO_LONGER_NEEDED, "wdl.messages.onChunkNoLongerNeeded.saved", unneededChunk.xPosition, unneededChunk.zPosition);
            WDL.saveChunk(unneededChunk);
        }
        else {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_CHUNK_NO_LONGER_NEEDED, "wdl.messages.onChunkNoLongerNeeded.didNotSave", unneededChunk.xPosition, unneededChunk.zPosition);
        }
    }
    
    public static void onItemGuiOpened() {
        if (!WDL.downloading) {
            return;
        }
        if (WDL.minecraft.objectMouseOver == null) {
            return;
        }
        if (WDL.minecraft.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
            WDL.lastEntity = WDL.minecraft.objectMouseOver.entityHit;
        }
        else {
            WDL.lastEntity = null;
            WDL.lastClickedBlock = WDL.minecraft.objectMouseOver.getBlockPos();
        }
    }
    
    public static boolean onItemGuiClosed() {
        if (!WDL.downloading) {
            return true;
        }
        String saveName = "";
        if (WDL.thePlayer.getRidingEntity() instanceof AbstractHorse && WDL.windowContainer instanceof ContainerHorseInventory) {
            final AbstractHorse horseInContainer = ReflectionUtils.stealAndGetField(WDL.windowContainer, AbstractHorse.class);
            if (horseInContainer == WDL.thePlayer.getRidingEntity()) {
                if (!WDLPluginChannels.canSaveEntities(horseInContainer.chunkCoordX, horseInContainer.chunkCoordZ)) {
                    WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_GUI_CLOSED_INFO, "wdl.messages.onGuiClosedInfo.cannotSaveEntities", new Object[0]);
                    return true;
                }
                final AbstractHorse entityHorse = (AbstractHorse)WDL.thePlayer.getRidingEntity();
                saveHorse((ContainerHorseInventory)WDL.windowContainer, entityHorse);
                WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_GUI_CLOSED_INFO, "wdl.messages.onGuiClosedInfo.savedRiddenHorse", new Object[0]);
                return true;
            }
        }
        if (WDL.lastEntity != null) {
            if (!WDLPluginChannels.canSaveEntities(WDL.lastEntity.chunkCoordX, WDL.lastEntity.chunkCoordZ)) {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_GUI_CLOSED_INFO, "wdl.messages.onGuiClosedInfo.cannotSaveEntities", new Object[0]);
                return true;
            }
            if (WDL.lastEntity instanceof EntityMinecartChest && WDL.windowContainer instanceof ContainerChest) {
                final EntityMinecartChest emcc = (EntityMinecartChest)WDL.lastEntity;
                for (int i = 0; i < emcc.getSizeInventory(); ++i) {
                    final Slot slot = WDL.windowContainer.getSlot(i);
                    if (slot.getHasStack()) {
                        emcc.setInventorySlotContents(i, slot.getStack());
                    }
                }
                saveName = "storageMinecart";
            }
            else if (WDL.lastEntity instanceof EntityMinecartHopper && WDL.windowContainer instanceof ContainerHopper) {
                final EntityMinecartHopper emch = (EntityMinecartHopper)WDL.lastEntity;
                for (int i = 0; i < emch.getSizeInventory(); ++i) {
                    final Slot slot = WDL.windowContainer.getSlot(i);
                    if (slot.getHasStack()) {
                        emch.setInventorySlotContents(i, slot.getStack());
                    }
                }
                saveName = "hopperMinecart";
            }
            else if (WDL.lastEntity instanceof EntityVillager && WDL.windowContainer instanceof ContainerMerchant) {
                final EntityVillager ev = (EntityVillager)WDL.lastEntity;
                final MerchantRecipeList list = ReflectionUtils.stealAndGetField(WDL.windowContainer, IMerchant.class).getRecipes(WDL.thePlayer);
                ReflectionUtils.stealAndSetField(ev, MerchantRecipeList.class, list);
                saveName = "villager";
            }
            else {
                if (!(WDL.lastEntity instanceof AbstractHorse) || !(WDL.windowContainer instanceof ContainerHorseInventory)) {
                    return false;
                }
                saveHorse((ContainerHorseInventory)WDL.windowContainer, (AbstractHorse)WDL.lastEntity);
                saveName = "horse";
            }
            WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_GUI_CLOSED_INFO, "wdl.messages.onGuiClosedInfo.savedEntity." + saveName, new Object[0]);
            return true;
        }
        else {
            final TileEntity te = WDL.worldClient.getTileEntity(WDL.lastClickedBlock);
            if (te == null) {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_GUI_CLOSED_WARNING, "wdl.messages.onGuiClosedWarning.couldNotGetTE", WDL.lastClickedBlock);
                return true;
            }
            if (!WDLPluginChannels.canSaveContainers(te.getPos().getX() << 4, te.getPos().getZ() << 4)) {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_GUI_CLOSED_INFO, "wdl.messages.onGuiClosedInfo.cannotSaveTileEntities", new Object[0]);
                return true;
            }
            if (WDL.windowContainer instanceof ContainerChest && te instanceof TileEntityChest) {
                if (WDL.windowContainer.inventorySlots.size() > 63) {
                    final BlockPos pos1 = WDL.lastClickedBlock;
                    final TileEntity te2 = te;
                    BlockPos chestPos1 = null;
                    BlockPos chestPos2 = null;
                    TileEntityChest chest1 = null;
                    TileEntityChest chest2 = null;
                    BlockPos pos2 = pos1.add(0, 0, 1);
                    TileEntity te3 = WDL.worldClient.getTileEntity(pos2);
                    if (te3 instanceof TileEntityChest && ((TileEntityChest)te3).getChestType() == ((TileEntityChest)te2).getChestType()) {
                        chest1 = (TileEntityChest)te2;
                        chest2 = (TileEntityChest)te3;
                        chestPos1 = pos1;
                        chestPos2 = pos2;
                    }
                    pos2 = pos1.add(0, 0, -1);
                    te3 = WDL.worldClient.getTileEntity(pos2);
                    if (te3 instanceof TileEntityChest && ((TileEntityChest)te3).getChestType() == ((TileEntityChest)te2).getChestType()) {
                        chest1 = (TileEntityChest)te3;
                        chest2 = (TileEntityChest)te2;
                        chestPos1 = pos2;
                        chestPos2 = pos1;
                    }
                    pos2 = pos1.add(1, 0, 0);
                    te3 = WDL.worldClient.getTileEntity(pos2);
                    if (te3 instanceof TileEntityChest && ((TileEntityChest)te3).getChestType() == ((TileEntityChest)te2).getChestType()) {
                        chest1 = (TileEntityChest)te2;
                        chest2 = (TileEntityChest)te3;
                        chestPos1 = pos1;
                        chestPos2 = pos2;
                    }
                    pos2 = pos1.add(-1, 0, 0);
                    te3 = WDL.worldClient.getTileEntity(pos2);
                    if (te3 instanceof TileEntityChest && ((TileEntityChest)te3).getChestType() == ((TileEntityChest)te2).getChestType()) {
                        chest1 = (TileEntityChest)te3;
                        chest2 = (TileEntityChest)te2;
                        chestPos1 = pos2;
                        chestPos2 = pos1;
                    }
                    if (chest1 == null || chest2 == null || chestPos1 == null || chestPos2 == null) {
                        WDLMessages.chatMessageTranslated(WDLMessageTypes.ERROR, "wdl.messages.onGuiClosedWarning.failedToFindDoubleChest", new Object[0]);
                        return true;
                    }
                    WDL.saveContainerItems(WDL.windowContainer, chest1, 0);
                    WDL.saveContainerItems(WDL.windowContainer, chest2, 27);
                    WDL.saveTileEntity(chestPos1, chest1);
                    WDL.saveTileEntity(chestPos2, chest2);
                    saveName = "doubleChest";
                }
                else {
                    WDL.saveContainerItems(WDL.windowContainer, (IInventory)te, 0);
                    WDL.saveTileEntity(WDL.lastClickedBlock, te);
                    saveName = "singleChest";
                }
            }
            else if (WDL.windowContainer instanceof ContainerChest && te instanceof TileEntityEnderChest) {
                final InventoryEnderChest inventoryEnderChest = WDL.thePlayer.getInventoryEnderChest();
                for (int inventorySize = inventoryEnderChest.getSizeInventory(), containerSize = WDL.windowContainer.inventorySlots.size(), j = 0; j < containerSize && j < inventorySize; ++j) {
                    final Slot slot2 = WDL.windowContainer.getSlot(j);
                    if (slot2.getHasStack()) {
                        inventoryEnderChest.setInventorySlotContents(j, slot2.getStack());
                    }
                }
                saveName = "enderChest";
            }
            else if (WDL.windowContainer instanceof ContainerBrewingStand && te instanceof TileEntityBrewingStand) {
                final IInventory brewingInventory = ReflectionUtils.stealAndGetField(WDL.windowContainer, IInventory.class);
                WDL.saveContainerItems(WDL.windowContainer, (IInventory)te, 0);
                WDL.saveInventoryFields(brewingInventory, (IInventory)te);
                WDL.saveTileEntity(WDL.lastClickedBlock, te);
                saveName = "brewingStand";
            }
            else if (WDL.windowContainer instanceof ContainerDispenser && te instanceof TileEntityDispenser) {
                WDL.saveContainerItems(WDL.windowContainer, (IInventory)te, 0);
                WDL.saveTileEntity(WDL.lastClickedBlock, te);
                saveName = "dispenser";
            }
            else if (WDL.windowContainer instanceof ContainerFurnace && te instanceof TileEntityFurnace) {
                final IInventory furnaceInventory = ReflectionUtils.stealAndGetField(WDL.windowContainer, IInventory.class);
                WDL.saveContainerItems(WDL.windowContainer, (IInventory)te, 0);
                WDL.saveInventoryFields(furnaceInventory, (IInventory)te);
                WDL.saveTileEntity(WDL.lastClickedBlock, te);
                saveName = "furnace";
            }
            else if (WDL.windowContainer instanceof ContainerHopper && te instanceof TileEntityHopper) {
                WDL.saveContainerItems(WDL.windowContainer, (IInventory)te, 0);
                WDL.saveTileEntity(WDL.lastClickedBlock, te);
                saveName = "hopper";
            }
            else {
                if (!(WDL.windowContainer instanceof ContainerBeacon) || !(te instanceof TileEntityBeacon)) {
                    return false;
                }
                final IInventory beaconInventory = ((ContainerBeacon)WDL.windowContainer).getTileEntity();
                final TileEntityBeacon savedBeacon = (TileEntityBeacon)te;
                WDL.saveContainerItems(WDL.windowContainer, savedBeacon, 0);
                WDL.saveInventoryFields(beaconInventory, savedBeacon);
                WDL.saveTileEntity(WDL.lastClickedBlock, te);
                saveName = "beacon";
            }
            WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_GUI_CLOSED_INFO, "wdl.messages.onGuiClosedInfo.savedTileEntity." + saveName, new Object[0]);
            return true;
        }
    }
    
    public static void onBlockEvent(final BlockPos pos, final Block block, final int event, final int param) {
        if (!WDL.downloading) {
            return;
        }
        if (!WDLPluginChannels.canSaveTileEntities(pos.getX() << 4, pos.getZ() << 4)) {
            return;
        }
        if (block == Blocks.NOTEBLOCK) {
            final TileEntityNote newTE = new TileEntityNote();
            newTE.note = (byte)(param % 25);
            WDL.worldClient.setTileEntity(pos, newTE);
            WDL.saveTileEntity(pos, newTE);
            WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_BLOCK_EVENT, "wdl.messages.onBlockEvent.noteblock", pos, param, newTE);
        }
    }
    
    public static void onMapDataLoaded(final int mapID, final MapData mapData) {
        if (!WDL.downloading) {
            return;
        }
        if (!WDLPluginChannels.canSaveMaps()) {
            return;
        }
        WDL.newMapDatas.put(mapID, mapData);
        WDLMessages.chatMessageTranslated(WDLMessageTypes.ON_MAP_SAVED, "wdl.messages.onMapSaved", mapID);
    }
    
    public static void onPluginChannelPacket(final String channel, final byte[] bytes) {
        WDLPluginChannels.onPluginChannelPacket(channel, bytes);
    }
    
    public static void onRemoveEntityFromWorld(final Entity entity) {
    }
    
    public static void onChatMessage(final String msg) {
        if (WDL.downloading && msg.startsWith("Seed: ")) {
            final String seed = msg.substring(6);
            WDL.worldProps.setProperty("RandomSeed", seed);
            if (WDL.worldProps.getProperty("MapGenerator", "void").equals("void")) {
                WDL.worldProps.setProperty("MapGenerator", "default");
                WDL.worldProps.setProperty("GeneratorName", "default");
                WDL.worldProps.setProperty("GeneratorVersion", "1");
                WDL.worldProps.setProperty("GeneratorOptions", "");
                WDLMessages.chatMessageTranslated(WDLMessageTypes.INFO, "wdl.messages.generalInfo.seedAndGenSet", seed);
            }
            else {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.INFO, "wdl.messages.generalInfo.seedSet", seed);
            }
        }
    }
    
    private static void saveHorse(final ContainerHorseInventory container, final AbstractHorse horse) {
        final int PLAYER_INVENTORY_SLOTS = 36;
        final ContainerHorseChest horseInventory = new ContainerHorseChest("HorseChest", container.inventorySlots.size() - 36);
        for (int i = 0; i < horseInventory.getSizeInventory(); ++i) {
            final Slot slot = container.getSlot(i);
            if (slot.getHasStack()) {
                horseInventory.setInventorySlotContents(i, slot.getStack());
            }
        }
        ReflectionUtils.stealAndSetField(horse, ContainerHorseChest.class, horseInventory);
    }
}
