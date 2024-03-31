// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol;

import com.viaversion.viaversion.exception.CancelException;
import com.viaversion.viaversion.exception.InformativeException;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import java.util.Arrays;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.logging.Level;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public abstract class AbstractProtocol<C1 extends ClientboundPacketType, C2 extends ClientboundPacketType, S1 extends ServerboundPacketType, S2 extends ServerboundPacketType> implements Protocol<C1, C2, S1, S2>
{
    private final Map<Packet, ProtocolPacket> serverbound;
    private final Map<Packet, ProtocolPacket> clientbound;
    private final Map<Class<?>, Object> storedObjects;
    protected final Class<C1> oldClientboundPacketEnum;
    protected final Class<C2> newClientboundPacketEnum;
    protected final Class<S1> oldServerboundPacketEnum;
    protected final Class<S2> newServerboundPacketEnum;
    private boolean initialized;
    
    protected AbstractProtocol() {
        this(null, null, null, null);
    }
    
    protected AbstractProtocol(final Class<C1> oldClientboundPacketEnum, final Class<C2> clientboundPacketEnum, final Class<S1> oldServerboundPacketEnum, final Class<S2> serverboundPacketEnum) {
        this.serverbound = new HashMap<Packet, ProtocolPacket>();
        this.clientbound = new HashMap<Packet, ProtocolPacket>();
        this.storedObjects = new HashMap<Class<?>, Object>();
        this.oldClientboundPacketEnum = oldClientboundPacketEnum;
        this.newClientboundPacketEnum = clientboundPacketEnum;
        this.oldServerboundPacketEnum = oldServerboundPacketEnum;
        this.newServerboundPacketEnum = serverboundPacketEnum;
    }
    
    @Override
    public final void initialize() {
        Preconditions.checkArgument(!this.initialized);
        this.initialized = true;
        this.registerPackets();
        if (this.oldClientboundPacketEnum != null && this.newClientboundPacketEnum != null && this.oldClientboundPacketEnum != this.newClientboundPacketEnum) {
            this.registerClientboundChannelIdChanges();
        }
        if (this.oldServerboundPacketEnum != null && this.newServerboundPacketEnum != null && this.oldServerboundPacketEnum != this.newServerboundPacketEnum) {
            this.registerServerboundChannelIdChanges();
        }
    }
    
    protected void registerClientboundChannelIdChanges() {
        final ClientboundPacketType[] newConstants = this.newClientboundPacketEnum.getEnumConstants();
        final Map<String, ClientboundPacketType> newClientboundPackets = new HashMap<String, ClientboundPacketType>(newConstants.length);
        for (final ClientboundPacketType newConstant : newConstants) {
            newClientboundPackets.put(newConstant.getName(), newConstant);
        }
        for (final ClientboundPacketType packet : this.oldClientboundPacketEnum.getEnumConstants()) {
            final ClientboundPacketType mappedPacket = newClientboundPackets.get(packet.getName());
            final int oldId = packet.getId();
            if (mappedPacket == null) {
                Preconditions.checkArgument(this.hasRegisteredClientbound(State.PLAY, oldId), (Object)("Packet " + packet + " in " + this.getClass().getSimpleName() + " has no mapping - it needs to be manually cancelled or remapped!"));
            }
            else {
                final int newId = mappedPacket.getId();
                if (!this.hasRegisteredClientbound(State.PLAY, oldId)) {
                    this.registerClientbound(State.PLAY, oldId, newId);
                }
            }
        }
    }
    
    protected void registerServerboundChannelIdChanges() {
        final ServerboundPacketType[] oldConstants = this.oldServerboundPacketEnum.getEnumConstants();
        final Map<String, ServerboundPacketType> oldServerboundConstants = new HashMap<String, ServerboundPacketType>(oldConstants.length);
        for (final ServerboundPacketType oldConstant : oldConstants) {
            oldServerboundConstants.put(oldConstant.getName(), oldConstant);
        }
        for (final ServerboundPacketType packet : this.newServerboundPacketEnum.getEnumConstants()) {
            final ServerboundPacketType mappedPacket = oldServerboundConstants.get(packet.getName());
            final int newId = packet.getId();
            if (mappedPacket == null) {
                Preconditions.checkArgument(this.hasRegisteredServerbound(State.PLAY, newId), (Object)("Packet " + packet + " in " + this.getClass().getSimpleName() + " has no mapping - it needs to be manually cancelled or remapped!"));
            }
            else {
                final int oldId = mappedPacket.getId();
                if (!this.hasRegisteredServerbound(State.PLAY, newId)) {
                    this.registerServerbound(State.PLAY, oldId, newId);
                }
            }
        }
    }
    
    protected void registerPackets() {
    }
    
    @Override
    public final void loadMappingData() {
        this.getMappingData().load();
        this.onMappingDataLoaded();
    }
    
    protected void onMappingDataLoaded() {
    }
    
    protected void addEntityTracker(final UserConnection connection, final EntityTracker tracker) {
        connection.addEntityTracker(this.getClass(), tracker);
    }
    
    @Override
    public void registerServerbound(final State state, final int oldPacketID, final int newPacketID, final PacketRemapper packetRemapper, final boolean override) {
        final ProtocolPacket protocolPacket = new ProtocolPacket(state, oldPacketID, newPacketID, packetRemapper);
        final Packet packet = new Packet(state, newPacketID);
        if (!override && this.serverbound.containsKey(packet)) {
            Via.getPlatform().getLogger().log(Level.WARNING, packet + " already registered! If this override is intentional, set override to true. Stacktrace: ", new Exception());
        }
        this.serverbound.put(packet, protocolPacket);
    }
    
    @Override
    public void cancelServerbound(final State state, final int oldPacketID, final int newPacketID) {
        this.registerServerbound(state, oldPacketID, newPacketID, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(PacketWrapper::cancel);
            }
        });
    }
    
    @Override
    public void cancelClientbound(final State state, final int oldPacketID, final int newPacketID) {
        this.registerClientbound(state, oldPacketID, newPacketID, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(PacketWrapper::cancel);
            }
        });
    }
    
    @Override
    public void registerClientbound(final State state, final int oldPacketID, final int newPacketID, final PacketRemapper packetRemapper, final boolean override) {
        final ProtocolPacket protocolPacket = new ProtocolPacket(state, oldPacketID, newPacketID, packetRemapper);
        final Packet packet = new Packet(state, oldPacketID);
        if (!override && this.clientbound.containsKey(packet)) {
            Via.getPlatform().getLogger().log(Level.WARNING, packet + " already registered! If override is intentional, set override to true. Stacktrace: ", new Exception());
        }
        this.clientbound.put(packet, protocolPacket);
    }
    
    @Override
    public void registerClientbound(final C1 packetType, final PacketRemapper packetRemapper) {
        this.checkPacketType(packetType, packetType.getClass() == this.oldClientboundPacketEnum);
        final ClientboundPacketType mappedPacket = (this.oldClientboundPacketEnum == this.newClientboundPacketEnum) ? packetType : ((C2)Arrays.stream(this.newClientboundPacketEnum.getEnumConstants()).filter(en -> en.getName().equals(packetType.getName())).findAny().orElse(null));
        Preconditions.checkNotNull(mappedPacket, (Object)("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " could not be automatically mapped!"));
        final int oldId = packetType.getId();
        final int newId = mappedPacket.getId();
        this.registerClientbound(State.PLAY, oldId, newId, packetRemapper);
    }
    
    @Override
    public void registerClientbound(final C1 packetType, final C2 mappedPacketType, final PacketRemapper packetRemapper) {
        this.checkPacketType(packetType, packetType.getClass() == this.oldClientboundPacketEnum);
        this.checkPacketType(mappedPacketType, mappedPacketType == null || mappedPacketType.getClass() == this.newClientboundPacketEnum);
        this.registerClientbound(State.PLAY, packetType.getId(), (mappedPacketType != null) ? mappedPacketType.getId() : -1, packetRemapper);
    }
    
    @Override
    public void cancelClientbound(final C1 packetType) {
        this.cancelClientbound(State.PLAY, packetType.getId(), packetType.getId());
    }
    
    @Override
    public void registerServerbound(final S2 packetType, final PacketRemapper packetRemapper) {
        this.checkPacketType(packetType, packetType.getClass() == this.newServerboundPacketEnum);
        final ServerboundPacketType mappedPacket = (this.oldServerboundPacketEnum == this.newServerboundPacketEnum) ? packetType : ((S1)Arrays.stream(this.oldServerboundPacketEnum.getEnumConstants()).filter(en -> en.getName().equals(packetType.getName())).findAny().orElse(null));
        Preconditions.checkNotNull(mappedPacket, (Object)("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " could not be automatically mapped!"));
        final int oldId = mappedPacket.getId();
        final int newId = packetType.getId();
        this.registerServerbound(State.PLAY, oldId, newId, packetRemapper);
    }
    
    @Override
    public void registerServerbound(final S2 packetType, final S1 mappedPacketType, final PacketRemapper packetRemapper) {
        this.checkPacketType(packetType, packetType.getClass() == this.newServerboundPacketEnum);
        this.checkPacketType(mappedPacketType, mappedPacketType == null || mappedPacketType.getClass() == this.oldServerboundPacketEnum);
        this.registerServerbound(State.PLAY, (mappedPacketType != null) ? mappedPacketType.getId() : -1, packetType.getId(), packetRemapper);
    }
    
    @Override
    public void cancelServerbound(final S2 packetType) {
        Preconditions.checkArgument(packetType.getClass() == this.newServerboundPacketEnum);
        this.cancelServerbound(State.PLAY, -1, packetType.getId());
    }
    
    @Override
    public boolean hasRegisteredClientbound(final State state, final int oldPacketID) {
        final Packet packet = new Packet(state, oldPacketID);
        return this.clientbound.containsKey(packet);
    }
    
    @Override
    public boolean hasRegisteredServerbound(final State state, final int newPacketId) {
        final Packet packet = new Packet(state, newPacketId);
        return this.serverbound.containsKey(packet);
    }
    
    @Override
    public void transform(final Direction direction, final State state, final PacketWrapper packetWrapper) throws Exception {
        final Packet statePacket = new Packet(state, packetWrapper.getId());
        final Map<Packet, ProtocolPacket> packetMap = (direction == Direction.CLIENTBOUND) ? this.clientbound : this.serverbound;
        final ProtocolPacket protocolPacket = packetMap.get(statePacket);
        if (protocolPacket == null) {
            return;
        }
        final int oldId = packetWrapper.getId();
        final int newId = (direction == Direction.CLIENTBOUND) ? protocolPacket.getNewID() : protocolPacket.getOldID();
        packetWrapper.setId(newId);
        final PacketRemapper remapper = protocolPacket.getRemapper();
        if (remapper != null) {
            try {
                remapper.remap(packetWrapper);
            }
            catch (final InformativeException e) {
                this.throwRemapError(direction, state, oldId, newId, e);
                return;
            }
            if (packetWrapper.isCancelled()) {
                throw CancelException.generate();
            }
        }
    }
    
    private void throwRemapError(final Direction direction, final State state, final int oldId, final int newId, final InformativeException e) throws InformativeException {
        if (state == State.HANDSHAKE) {
            throw e;
        }
        final Class<? extends PacketType> packetTypeClass = (Class<? extends PacketType>)((state == State.PLAY) ? ((direction == Direction.CLIENTBOUND) ? this.oldClientboundPacketEnum : this.newServerboundPacketEnum) : null);
        if (packetTypeClass != null) {
            final PacketType[] enumConstants = (PacketType[])packetTypeClass.getEnumConstants();
            final PacketType packetType = (oldId < enumConstants.length && oldId >= 0) ? enumConstants[oldId] : null;
            Via.getPlatform().getLogger().warning("ERROR IN " + this.getClass().getSimpleName() + " IN REMAP OF " + packetType + " (" + this.toNiceHex(oldId) + ")");
        }
        else {
            Via.getPlatform().getLogger().warning("ERROR IN " + this.getClass().getSimpleName() + " IN REMAP OF " + this.toNiceHex(oldId) + "->" + this.toNiceHex(newId));
        }
        throw e;
    }
    
    private String toNiceHex(final int id) {
        final String hex = Integer.toHexString(id).toUpperCase();
        return ((hex.length() == 1) ? "0x0" : "0x") + hex;
    }
    
    private void checkPacketType(final PacketType packetType, final boolean isValid) {
        if (!isValid) {
            throw new IllegalArgumentException("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " is taken from the wrong enum");
        }
    }
    
    @Override
    public <T> T get(final Class<T> objectClass) {
        return (T)this.storedObjects.get(objectClass);
    }
    
    @Override
    public void put(final Object object) {
        this.storedObjects.put(object.getClass(), object);
    }
    
    @Override
    public boolean hasMappingDataToLoad() {
        return this.getMappingData() != null;
    }
    
    @Override
    public String toString() {
        return "Protocol:" + this.getClass().getSimpleName();
    }
    
    public static final class Packet
    {
        private final State state;
        private final int packetId;
        
        public Packet(final State state, final int packetId) {
            this.state = state;
            this.packetId = packetId;
        }
        
        public State getState() {
            return this.state;
        }
        
        public int getPacketId() {
            return this.packetId;
        }
        
        @Override
        public String toString() {
            return "Packet{state=" + this.state + ", packetId=" + this.packetId + '}';
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Packet that = (Packet)o;
            return this.packetId == that.packetId && this.state == that.state;
        }
        
        @Override
        public int hashCode() {
            int result = (this.state != null) ? this.state.hashCode() : 0;
            result = 31 * result + this.packetId;
            return result;
        }
    }
    
    public static final class ProtocolPacket
    {
        private final State state;
        private final int oldID;
        private final int newID;
        private final PacketRemapper remapper;
        
        public ProtocolPacket(final State state, final int oldID, final int newID, final PacketRemapper remapper) {
            this.state = state;
            this.oldID = oldID;
            this.newID = newID;
            this.remapper = remapper;
        }
        
        public State getState() {
            return this.state;
        }
        
        public int getOldID() {
            return this.oldID;
        }
        
        public int getNewID() {
            return this.newID;
        }
        
        public PacketRemapper getRemapper() {
            return this.remapper;
        }
    }
}
