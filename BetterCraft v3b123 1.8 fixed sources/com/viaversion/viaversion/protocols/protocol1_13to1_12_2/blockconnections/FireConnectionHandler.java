// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.HashSet;
import java.util.Set;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;

public class FireConnectionHandler extends ConnectionHandler
{
    private static final String[] WOOD_TYPES;
    private static final int[] CONNECTED_BLOCKS;
    private static final IntSet FLAMMABLE_BLOCKS;
    
    private static void addWoodTypes(final Set<String> set, final String suffix) {
        for (final String woodType : FireConnectionHandler.WOOD_TYPES) {
            set.add("minecraft:" + woodType + suffix);
        }
    }
    
    static ConnectionData.ConnectorInitAction init() {
        final Set<String> flammabeIds = new HashSet<String>();
        flammabeIds.add("minecraft:tnt");
        flammabeIds.add("minecraft:vine");
        flammabeIds.add("minecraft:bookshelf");
        flammabeIds.add("minecraft:hay_block");
        flammabeIds.add("minecraft:deadbush");
        addWoodTypes(flammabeIds, "_slab");
        addWoodTypes(flammabeIds, "_log");
        addWoodTypes(flammabeIds, "_planks");
        addWoodTypes(flammabeIds, "_leaves");
        addWoodTypes(flammabeIds, "_fence");
        addWoodTypes(flammabeIds, "_fence_gate");
        addWoodTypes(flammabeIds, "_stairs");
        final FireConnectionHandler connectionHandler = new FireConnectionHandler();
        return blockData -> {
            final String key = blockData.getMinecraftKey();
            if (key.contains("_wool") || key.contains("_carpet") || flammabeIds.contains(key)) {
                FireConnectionHandler.FLAMMABLE_BLOCKS.add(blockData.getSavedBlockStateId());
            }
            else if (key.equals("minecraft:fire")) {
                final int id = blockData.getSavedBlockStateId();
                FireConnectionHandler.CONNECTED_BLOCKS[getStates(blockData)] = id;
                ConnectionData.connectionHandlerMap.put(id, connectionHandler);
            }
        };
    }
    
    private static byte getStates(final WrappedBlockData blockData) {
        byte states = 0;
        if (blockData.getValue("east").equals("true")) {
            states |= 0x1;
        }
        if (blockData.getValue("north").equals("true")) {
            states |= 0x2;
        }
        if (blockData.getValue("south").equals("true")) {
            states |= 0x4;
        }
        if (blockData.getValue("up").equals("true")) {
            states |= 0x8;
        }
        if (blockData.getValue("west").equals("true")) {
            states |= 0x10;
        }
        return states;
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        byte states = 0;
        if (FireConnectionHandler.FLAMMABLE_BLOCKS.contains(this.getBlockData(user, position.getRelative(BlockFace.EAST)))) {
            states |= 0x1;
        }
        if (FireConnectionHandler.FLAMMABLE_BLOCKS.contains(this.getBlockData(user, position.getRelative(BlockFace.NORTH)))) {
            states |= 0x2;
        }
        if (FireConnectionHandler.FLAMMABLE_BLOCKS.contains(this.getBlockData(user, position.getRelative(BlockFace.SOUTH)))) {
            states |= 0x4;
        }
        if (FireConnectionHandler.FLAMMABLE_BLOCKS.contains(this.getBlockData(user, position.getRelative(BlockFace.TOP)))) {
            states |= 0x8;
        }
        if (FireConnectionHandler.FLAMMABLE_BLOCKS.contains(this.getBlockData(user, position.getRelative(BlockFace.WEST)))) {
            states |= 0x10;
        }
        return FireConnectionHandler.CONNECTED_BLOCKS[states];
    }
    
    static {
        WOOD_TYPES = new String[] { "oak", "spruce", "birch", "jungle", "acacia", "dark_oak" };
        CONNECTED_BLOCKS = new int[32];
        FLAMMABLE_BLOCKS = new IntOpenHashSet();
    }
}
