// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public class StatisticsRewriter<C extends ClientboundPacketType>
{
    private static final int CUSTOM_STATS_CATEGORY = 8;
    private final Protocol<C, ?, ?, ?> protocol;
    
    public StatisticsRewriter(final Protocol<C, ?, ?, ?> protocol) {
        this.protocol = protocol;
    }
    
    public void register(final C packetType) {
        this.protocol.registerClientbound(packetType, wrapper -> {
            int newSize;
            final int size = newSize = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
            for (int i = 0; i < size; ++i) {
                final int categoryId = wrapper.read((Type<Integer>)Type.VAR_INT);
                int statisticId = wrapper.read((Type<Integer>)Type.VAR_INT);
                final int value = wrapper.read((Type<Integer>)Type.VAR_INT);
                if (categoryId == 8 && this.protocol.getMappingData().getStatisticsMappings() != null) {
                    statisticId = this.protocol.getMappingData().getStatisticsMappings().getNewId(statisticId);
                    if (statisticId == -1) {
                        --newSize;
                        continue;
                    }
                }
                else {
                    final RegistryType type = this.getRegistryTypeForStatistic(categoryId);
                    final IdRewriteFunction statisticsRewriter;
                    if (type != null && (statisticsRewriter = this.getRewriter(type)) != null) {
                        statisticId = statisticsRewriter.rewrite(statisticId);
                    }
                }
                wrapper.write(Type.VAR_INT, categoryId);
                wrapper.write(Type.VAR_INT, statisticId);
                wrapper.write(Type.VAR_INT, value);
            }
            if (newSize != size) {
                wrapper.set(Type.VAR_INT, 0, newSize);
            }
        });
    }
    
    protected IdRewriteFunction getRewriter(final RegistryType type) {
        switch (type) {
            case BLOCK: {
                return (this.protocol.getMappingData().getBlockMappings() != null) ? (id -> this.protocol.getMappingData().getNewBlockId(id)) : null;
            }
            case ITEM: {
                return (this.protocol.getMappingData().getItemMappings() != null) ? (id -> this.protocol.getMappingData().getNewItemId(id)) : null;
            }
            case ENTITY: {
                return (this.protocol.getEntityRewriter() != null) ? (id -> this.protocol.getEntityRewriter().newEntityId(id)) : null;
            }
            default: {
                throw new IllegalArgumentException("Unknown registry type in statistics packet: " + type);
            }
        }
    }
    
    public RegistryType getRegistryTypeForStatistic(final int statisticsId) {
        switch (statisticsId) {
            case 0: {
                return RegistryType.BLOCK;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                return RegistryType.ITEM;
            }
            case 6:
            case 7: {
                return RegistryType.ENTITY;
            }
            default: {
                return null;
            }
        }
    }
}
