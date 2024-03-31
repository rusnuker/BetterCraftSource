// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol;

import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.google.common.annotations.Beta;
import com.viaversion.viaversion.api.protocol.packet.provider.PacketTypesProvider;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public interface Protocol<CU extends ClientboundPacketType, CM extends ClientboundPacketType, SM extends ServerboundPacketType, SU extends ServerboundPacketType>
{
    default void registerServerbound(final State state, final int unmappedPacketId, final int mappedPacketId) {
        this.registerServerbound(state, unmappedPacketId, mappedPacketId, (PacketHandler)null);
    }
    
    default void registerServerbound(final State state, final int unmappedPacketId, final int mappedPacketId, final PacketHandler handler) {
        this.registerServerbound(state, unmappedPacketId, mappedPacketId, handler, false);
    }
    
    void registerServerbound(final State p0, final int p1, final int p2, final PacketHandler p3, final boolean p4);
    
    void cancelServerbound(final State p0, final int p1);
    
    default void registerClientbound(final State state, final int unmappedPacketId, final int mappedPacketId) {
        this.registerClientbound(state, unmappedPacketId, mappedPacketId, (PacketHandler)null);
    }
    
    default void registerClientbound(final State state, final int unmappedPacketId, final int mappedPacketId, final PacketHandler handler) {
        this.registerClientbound(state, unmappedPacketId, mappedPacketId, handler, false);
    }
    
    void cancelClientbound(final State p0, final int p1);
    
    void registerClientbound(final State p0, final int p1, final int p2, final PacketHandler p3, final boolean p4);
    
    void registerClientbound(final CU p0, final PacketHandler p1);
    
    default void registerClientbound(final CU packetType, final CM mappedPacketType) {
        this.registerClientbound(packetType, mappedPacketType, (PacketHandler)null);
    }
    
    default void registerClientbound(final CU packetType, final CM mappedPacketType, final PacketHandler handler) {
        this.registerClientbound(packetType, mappedPacketType, handler, false);
    }
    
    void registerClientbound(final CU p0, final CM p1, final PacketHandler p2, final boolean p3);
    
    void cancelClientbound(final CU p0);
    
    default void registerServerbound(final SU packetType, final SM mappedPacketType) {
        this.registerServerbound(packetType, mappedPacketType, (PacketHandler)null);
    }
    
    void registerServerbound(final SU p0, final PacketHandler p1);
    
    default void registerServerbound(final SU packetType, final SM mappedPacketType, final PacketHandler handler) {
        this.registerServerbound(packetType, mappedPacketType, handler, false);
    }
    
    void registerServerbound(final SU p0, final SM p1, final PacketHandler p2, final boolean p3);
    
    void cancelServerbound(final SU p0);
    
    default boolean hasRegisteredClientbound(final CU packetType) {
        return this.hasRegisteredClientbound(packetType.state(), packetType.getId());
    }
    
    default boolean hasRegisteredServerbound(final SU packetType) {
        return this.hasRegisteredServerbound(packetType.state(), packetType.getId());
    }
    
    boolean hasRegisteredClientbound(final State p0, final int p1);
    
    boolean hasRegisteredServerbound(final State p0, final int p1);
    
    void transform(final Direction p0, final State p1, final PacketWrapper p2) throws Exception;
    
    @Beta
    PacketTypesProvider<CU, CM, SM, SU> getPacketTypesProvider();
    
     <T> T get(final Class<T> p0);
    
    void put(final Object p0);
    
    void initialize();
    
    default boolean hasMappingDataToLoad() {
        return this.getMappingData() != null;
    }
    
    void loadMappingData();
    
    default void register(final ViaProviders providers) {
    }
    
    default void init(final UserConnection connection) {
    }
    
    default MappingData getMappingData() {
        return null;
    }
    
    default EntityRewriter<?> getEntityRewriter() {
        return null;
    }
    
    default ItemRewriter<?> getItemRewriter() {
        return null;
    }
    
    default boolean isBaseProtocol() {
        return false;
    }
    
    @Deprecated
    default void cancelServerbound(final State state, final int unmappedPacketId, final int mappedPacketId) {
        this.cancelServerbound(state, unmappedPacketId);
    }
    
    @Deprecated
    default void cancelClientbound(final State state, final int unmappedPacketId, final int mappedPacketId) {
        this.cancelClientbound(state, unmappedPacketId);
    }
    
    @Deprecated
    default void registerClientbound(final State state, final int unmappedPacketId, final int mappedPacketId, final PacketRemapper packetRemapper) {
        this.registerClientbound(state, unmappedPacketId, mappedPacketId, packetRemapper.asPacketHandler(), false);
    }
    
    @Deprecated
    default void registerClientbound(final State state, final int unmappedPacketId, final int mappedPacketId, final PacketRemapper packetRemapper, final boolean override) {
        this.registerClientbound(state, unmappedPacketId, mappedPacketId, packetRemapper.asPacketHandler(), override);
    }
    
    @Deprecated
    default void registerClientbound(final CU packetType, final PacketRemapper packetRemapper) {
        this.registerClientbound(packetType, packetRemapper.asPacketHandler());
    }
    
    @Deprecated
    default void registerClientbound(final CU packetType, final CM mappedPacketType, final PacketRemapper packetRemapper) {
        this.registerClientbound(packetType, mappedPacketType, packetRemapper.asPacketHandler(), false);
    }
    
    @Deprecated
    default void registerClientbound(final CU packetType, final CM mappedPacketType, final PacketRemapper packetRemapper, final boolean override) {
        this.registerClientbound(packetType, mappedPacketType, packetRemapper.asPacketHandler(), override);
    }
    
    @Deprecated
    default void registerServerbound(final State state, final int unmappedPacketId, final int mappedPacketId, final PacketRemapper packetRemapper) {
        this.registerServerbound(state, unmappedPacketId, mappedPacketId, packetRemapper.asPacketHandler(), false);
    }
    
    @Deprecated
    default void registerServerbound(final State state, final int unmappedPacketId, final int mappedPacketId, final PacketRemapper packetRemapper, final boolean override) {
        this.registerServerbound(state, unmappedPacketId, mappedPacketId, packetRemapper.asPacketHandler(), override);
    }
    
    @Deprecated
    default void registerServerbound(final SU packetType, final PacketRemapper packetRemapper) {
        this.registerServerbound(packetType, packetRemapper.asPacketHandler());
    }
    
    @Deprecated
    default void registerServerbound(final SU packetType, final SM mappedPacketType, final PacketRemapper packetRemapper) {
        this.registerServerbound(packetType, mappedPacketType, packetRemapper.asPacketHandler(), false);
    }
    
    @Deprecated
    default void registerServerbound(final SU packetType, final SM mappedPacketType, final PacketRemapper packetRemapper, final boolean override) {
        this.registerServerbound(packetType, mappedPacketType, packetRemapper.asPacketHandler(), override);
    }
}
