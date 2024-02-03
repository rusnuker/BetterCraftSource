// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api;

import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.api.data.BackwardsMappings;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public abstract class BackwardsProtocol<CU extends ClientboundPacketType, CM extends ClientboundPacketType, SM extends ServerboundPacketType, SU extends ServerboundPacketType> extends AbstractProtocol<CU, CM, SM, SU>
{
    protected BackwardsProtocol() {
    }
    
    protected BackwardsProtocol(final Class<CU> oldClientboundPacketEnum, final Class<CM> clientboundPacketEnum, final Class<SM> oldServerboundPacketEnum, final Class<SU> serverboundPacketEnum) {
        super(oldClientboundPacketEnum, clientboundPacketEnum, oldServerboundPacketEnum, serverboundPacketEnum);
    }
    
    protected void executeAsyncAfterLoaded(final Class<? extends Protocol> protocolClass, final Runnable runnable) {
        Via.getManager().getProtocolManager().addMappingLoaderFuture(this.getClass(), protocolClass, runnable);
    }
    
    @Override
    protected void registerPackets() {
        super.registerPackets();
        final BackwardsMappings mappingData = this.getMappingData();
        if (mappingData != null && mappingData.getViaVersionProtocolClass() != null) {
            this.executeAsyncAfterLoaded(mappingData.getViaVersionProtocolClass(), this::loadMappingData);
        }
    }
    
    @Override
    public boolean hasMappingDataToLoad() {
        return false;
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return null;
    }
    
    public TranslatableRewriter<CU> getTranslatableRewriter() {
        return null;
    }
}
