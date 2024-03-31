// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol;

import com.viaversion.viaversion.exception.InformativeException;
import com.viaversion.viaversion.exception.CancelException;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.logging.Level;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.packet.mapping.PacketMapping;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import java.util.Collections;
import java.util.EnumMap;
import com.viaversion.viaversion.api.protocol.packet.provider.SimplePacketTypesProvider;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.rewriter.Rewriter;
import java.util.Iterator;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import com.viaversion.viaversion.api.protocol.packet.provider.PacketTypeMap;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import com.viaversion.viaversion.api.protocol.packet.mapping.PacketMappings;
import com.viaversion.viaversion.api.protocol.packet.provider.PacketTypesProvider;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public abstract class AbstractProtocol<CU extends ClientboundPacketType, CM extends ClientboundPacketType, SM extends ServerboundPacketType, SU extends ServerboundPacketType> implements Protocol<CU, CM, SM, SU>
{
    protected final Class<CU> unmappedClientboundPacketType;
    protected final Class<CM> mappedClientboundPacketType;
    protected final Class<SM> mappedServerboundPacketType;
    protected final Class<SU> unmappedServerboundPacketType;
    protected final PacketTypesProvider<CU, CM, SM, SU> packetTypesProvider;
    protected final PacketMappings clientboundMappings;
    protected final PacketMappings serverboundMappings;
    private final Map<Class<?>, Object> storedObjects;
    private boolean initialized;
    
    @Deprecated
    protected AbstractProtocol() {
        this(null, null, null, null);
    }
    
    protected AbstractProtocol(final Class<CU> unmappedClientboundPacketType, final Class<CM> mappedClientboundPacketType, final Class<SM> mappedServerboundPacketType, final Class<SU> unmappedServerboundPacketType) {
        this.storedObjects = new HashMap<Class<?>, Object>();
        this.unmappedClientboundPacketType = unmappedClientboundPacketType;
        this.mappedClientboundPacketType = mappedClientboundPacketType;
        this.mappedServerboundPacketType = mappedServerboundPacketType;
        this.unmappedServerboundPacketType = unmappedServerboundPacketType;
        this.packetTypesProvider = this.createPacketTypesProvider();
        this.clientboundMappings = this.createClientboundPacketMappings();
        this.serverboundMappings = this.createServerboundPacketMappings();
    }
    
    @Override
    public final void initialize() {
        Preconditions.checkArgument(!this.initialized, (Object)"Protocol has already been initialized");
        this.initialized = true;
        this.registerPackets();
        if (this.unmappedClientboundPacketType != null && this.mappedClientboundPacketType != null && this.unmappedClientboundPacketType != this.mappedClientboundPacketType) {
            this.registerPacketIdChanges((Map<State, PacketTypeMap<ClientboundPacketType>>)this.packetTypesProvider.unmappedClientboundPacketTypes(), (Map<State, PacketTypeMap<ClientboundPacketType>>)this.packetTypesProvider.mappedClientboundPacketTypes(), this::hasRegisteredClientbound, this::registerClientbound);
        }
        if (this.mappedServerboundPacketType != null && this.unmappedServerboundPacketType != null && this.mappedServerboundPacketType != this.unmappedServerboundPacketType) {
            this.registerPacketIdChanges((Map<State, PacketTypeMap<ServerboundPacketType>>)this.packetTypesProvider.unmappedServerboundPacketTypes(), (Map<State, PacketTypeMap<ServerboundPacketType>>)this.packetTypesProvider.mappedServerboundPacketTypes(), this::hasRegisteredServerbound, this::registerServerbound);
        }
    }
    
    private <U extends PacketType, M extends PacketType> void registerPacketIdChanges(final Map<State, PacketTypeMap<U>> unmappedPacketTypes, final Map<State, PacketTypeMap<M>> mappedPacketTypes, final Predicate<U> registeredPredicate, final BiConsumer<U, M> registerConsumer) {
        for (final Map.Entry<State, PacketTypeMap<M>> entry : mappedPacketTypes.entrySet()) {
            final PacketTypeMap<M> mappedTypes = entry.getValue();
            for (final U unmappedType : unmappedPacketTypes.get(entry.getKey()).types()) {
                final M mappedType = mappedTypes.typeByName(unmappedType.getName());
                if (mappedType == null) {
                    Preconditions.checkArgument(registeredPredicate.test(unmappedType), "Packet %s in %s has no mapping - it needs to be manually cancelled or remapped", unmappedType, this.getClass());
                }
                else {
                    if (unmappedType.getId() == mappedType.getId() || registeredPredicate.test(unmappedType)) {
                        continue;
                    }
                    registerConsumer.accept(unmappedType, mappedType);
                }
            }
        }
    }
    
    @Override
    public final void loadMappingData() {
        this.getMappingData().load();
        this.onMappingDataLoaded();
    }
    
    protected void registerPackets() {
        this.callRegister(this.getEntityRewriter());
        this.callRegister(this.getItemRewriter());
    }
    
    protected void onMappingDataLoaded() {
        this.callOnMappingDataLoaded(this.getEntityRewriter());
        this.callOnMappingDataLoaded(this.getItemRewriter());
    }
    
    private void callRegister(final Rewriter<?> rewriter) {
        if (rewriter != null) {
            rewriter.register();
        }
    }
    
    private void callOnMappingDataLoaded(final Rewriter<?> rewriter) {
        if (rewriter != null) {
            rewriter.onMappingDataLoaded();
        }
    }
    
    protected void addEntityTracker(final UserConnection connection, final EntityTracker tracker) {
        connection.addEntityTracker(this.getClass(), tracker);
    }
    
    protected PacketTypesProvider<CU, CM, SM, SU> createPacketTypesProvider() {
        return new SimplePacketTypesProvider<CU, CM, SM, SU>((Map<State, PacketTypeMap<CU>>)this.packetTypeMap((Class<CU>)this.unmappedClientboundPacketType), (Map<State, PacketTypeMap<CM>>)this.packetTypeMap((Class<CM>)this.mappedClientboundPacketType), (Map<State, PacketTypeMap<SM>>)this.packetTypeMap((Class<SM>)this.mappedServerboundPacketType), (Map<State, PacketTypeMap<SU>>)this.packetTypeMap((Class<SU>)this.unmappedServerboundPacketType));
    }
    
    protected PacketMappings createClientboundPacketMappings() {
        return PacketMappings.arrayMappings();
    }
    
    protected PacketMappings createServerboundPacketMappings() {
        return PacketMappings.arrayMappings();
    }
    
    private <P extends PacketType> Map<State, PacketTypeMap<P>> packetTypeMap(final Class<P> packetTypeClass) {
        if (packetTypeClass != null) {
            final Map<State, PacketTypeMap<P>> map = new EnumMap<State, PacketTypeMap<P>>(State.class);
            map.put(State.PLAY, PacketTypeMap.of(packetTypeClass));
            return map;
        }
        return Collections.emptyMap();
    }
    
    @Override
    public void registerServerbound(final State state, final int unmappedPacketId, final int mappedPacketId, final PacketHandler handler, final boolean override) {
        Preconditions.checkArgument(unmappedPacketId != -1, (Object)"Unmapped packet id cannot be -1");
        final PacketMapping packetMapping = PacketMapping.of(mappedPacketId, handler);
        if (!override && this.serverboundMappings.hasMapping(state, unmappedPacketId)) {
            Via.getPlatform().getLogger().log(Level.WARNING, unmappedPacketId + " already registered! If this override is intentional, set override to true. Stacktrace: ", new Exception());
        }
        this.serverboundMappings.addMapping(state, unmappedPacketId, packetMapping);
    }
    
    @Override
    public void cancelServerbound(final State state, final int unmappedPacketId) {
        this.registerServerbound(state, unmappedPacketId, unmappedPacketId, PacketWrapper::cancel);
    }
    
    @Override
    public void registerClientbound(final State state, final int unmappedPacketId, final int mappedPacketId, final PacketHandler handler, final boolean override) {
        Preconditions.checkArgument(unmappedPacketId != -1, (Object)"Unmapped packet id cannot be -1");
        final PacketMapping packetMapping = PacketMapping.of(mappedPacketId, handler);
        if (!override && this.clientboundMappings.hasMapping(state, unmappedPacketId)) {
            Via.getPlatform().getLogger().log(Level.WARNING, unmappedPacketId + " already registered! If override is intentional, set override to true. Stacktrace: ", new Exception());
        }
        this.clientboundMappings.addMapping(state, unmappedPacketId, packetMapping);
    }
    
    @Override
    public void cancelClientbound(final State state, final int unmappedPacketId) {
        this.registerClientbound(state, unmappedPacketId, unmappedPacketId, PacketWrapper::cancel);
    }
    
    @Override
    public void registerClientbound(final CU packetType, final PacketHandler handler) {
        final PacketTypeMap<CM> mappedPacketTypes = this.packetTypesProvider.mappedClientboundPacketTypes().get(packetType.state());
        final CM mappedPacketType = mappedPacketType(packetType, mappedPacketTypes, this.unmappedClientboundPacketType, this.mappedClientboundPacketType);
        this.registerClientbound(packetType, mappedPacketType, handler);
    }
    
    @Override
    public void registerClientbound(final CU packetType, final CM mappedPacketType, final PacketHandler handler, final boolean override) {
        this.register(this.clientboundMappings, packetType, mappedPacketType, this.unmappedClientboundPacketType, this.mappedClientboundPacketType, handler, override);
    }
    
    @Override
    public void cancelClientbound(final CU packetType) {
        this.registerClientbound(packetType, null, PacketWrapper::cancel);
    }
    
    @Override
    public void registerServerbound(final SU packetType, final PacketHandler handler) {
        final PacketTypeMap<SM> mappedPacketTypes = this.packetTypesProvider.mappedServerboundPacketTypes().get(packetType.state());
        final SM mappedPacketType = mappedPacketType(packetType, mappedPacketTypes, this.unmappedServerboundPacketType, this.mappedServerboundPacketType);
        this.registerServerbound(packetType, mappedPacketType, handler);
    }
    
    @Override
    public void registerServerbound(final SU packetType, final SM mappedPacketType, final PacketHandler handler, final boolean override) {
        this.register(this.serverboundMappings, packetType, mappedPacketType, this.unmappedServerboundPacketType, this.mappedServerboundPacketType, handler, override);
    }
    
    @Override
    public void cancelServerbound(final SU packetType) {
        this.registerServerbound(packetType, null, PacketWrapper::cancel);
    }
    
    private void register(final PacketMappings packetMappings, final PacketType packetType, final PacketType mappedPacketType, final Class<? extends PacketType> unmappedPacketClass, final Class<? extends PacketType> mappedPacketClass, final PacketHandler handler, final boolean override) {
        checkPacketType(packetType, unmappedPacketClass == null || unmappedPacketClass.isInstance(packetType));
        if (mappedPacketType != null) {
            checkPacketType(mappedPacketType, mappedPacketClass == null || mappedPacketClass.isInstance(mappedPacketType));
            Preconditions.checkArgument(packetType.state() == mappedPacketType.state(), (Object)"Packet type state does not match mapped packet type state");
            Preconditions.checkArgument(packetType.direction() == mappedPacketType.direction(), (Object)"Packet type direction does not match mapped packet type state");
        }
        final PacketMapping packetMapping = PacketMapping.of(mappedPacketType, handler);
        if (!override && packetMappings.hasMapping(packetType)) {
            Via.getPlatform().getLogger().log(Level.WARNING, packetType + " already registered! If override is intentional, set override to true. Stacktrace: ", new Exception());
        }
        packetMappings.addMapping(packetType, packetMapping);
    }
    
    private static <U extends PacketType, M extends PacketType> M mappedPacketType(final U packetType, final PacketTypeMap<M> mappedTypes, final Class<U> unmappedPacketTypeClass, final Class<M> mappedPacketTypeClass) {
        Preconditions.checkNotNull(packetType);
        checkPacketType(packetType, unmappedPacketTypeClass == null || unmappedPacketTypeClass.isInstance(packetType));
        if (unmappedPacketTypeClass == mappedPacketTypeClass) {
            return (M)packetType;
        }
        Preconditions.checkNotNull(mappedTypes, "Mapped packet types not provided for state %s of type class %s", packetType.state(), mappedPacketTypeClass);
        final M mappedType = mappedTypes.typeByName(packetType.getName());
        if (mappedType != null) {
            return mappedType;
        }
        throw new IllegalArgumentException("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " could not be automatically mapped!");
    }
    
    @Override
    public boolean hasRegisteredClientbound(final State state, final int unmappedPacketId) {
        return this.clientboundMappings.hasMapping(state, unmappedPacketId);
    }
    
    @Override
    public boolean hasRegisteredServerbound(final State state, final int unmappedPacketId) {
        return this.serverboundMappings.hasMapping(state, unmappedPacketId);
    }
    
    @Override
    public void transform(final Direction direction, final State state, final PacketWrapper packetWrapper) throws Exception {
        final PacketMappings mappings = (direction == Direction.CLIENTBOUND) ? this.clientboundMappings : this.serverboundMappings;
        final int unmappedId = packetWrapper.getId();
        final PacketMapping packetMapping = mappings.mappedPacket(state, unmappedId);
        if (packetMapping == null) {
            return;
        }
        packetMapping.applyType(packetWrapper);
        final PacketHandler handler = packetMapping.handler();
        if (handler != null) {
            try {
                handler.handle(packetWrapper);
            }
            catch (final CancelException e) {
                throw e;
            }
            catch (final InformativeException e2) {
                e2.addSource(handler.getClass());
                this.throwRemapError(direction, state, unmappedId, packetWrapper.getId(), e2);
                return;
            }
            catch (final Exception e3) {
                final InformativeException ex = new InformativeException(e3);
                ex.addSource(handler.getClass());
                this.throwRemapError(direction, state, unmappedId, packetWrapper.getId(), ex);
                return;
            }
            if (packetWrapper.isCancelled()) {
                throw CancelException.generate();
            }
        }
    }
    
    private void throwRemapError(final Direction direction, final State state, final int unmappedPacketId, final int mappedPacketId, final InformativeException e) throws InformativeException {
        if (state != State.PLAY && direction == Direction.SERVERBOUND && !Via.getManager().debugHandler().enabled()) {
            e.setShouldBePrinted(false);
            throw e;
        }
        final PacketType packetType = (direction == Direction.CLIENTBOUND) ? this.unmappedClientboundPacketType(state, unmappedPacketId) : this.unmappedServerboundPacketType(state, unmappedPacketId);
        if (packetType != null) {
            Via.getPlatform().getLogger().warning("ERROR IN " + this.getClass().getSimpleName() + " IN REMAP OF " + packetType + " (" + toNiceHex(unmappedPacketId) + ")");
        }
        else {
            Via.getPlatform().getLogger().warning("ERROR IN " + this.getClass().getSimpleName() + " IN REMAP OF " + toNiceHex(unmappedPacketId) + "->" + toNiceHex(mappedPacketId));
        }
        throw e;
    }
    
    private CU unmappedClientboundPacketType(final State state, final int packetId) {
        final PacketTypeMap<CU> map = this.packetTypesProvider.unmappedClientboundPacketTypes().get(state);
        return (CU)((map != null) ? ((CU)map.typeById(packetId)) : null);
    }
    
    private SU unmappedServerboundPacketType(final State state, final int packetId) {
        final PacketTypeMap<SU> map = this.packetTypesProvider.unmappedServerboundPacketTypes().get(state);
        return (SU)((map != null) ? ((SU)map.typeById(packetId)) : null);
    }
    
    public static String toNiceHex(final int id) {
        final String hex = Integer.toHexString(id).toUpperCase();
        return ((hex.length() == 1) ? "0x0" : "0x") + hex;
    }
    
    private static void checkPacketType(final PacketType packetType, final boolean isValid) {
        if (!isValid) {
            throw new IllegalArgumentException("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " is taken from the wrong packet types class");
        }
    }
    
    @Override
    public PacketTypesProvider<CU, CM, SM, SU> getPacketTypesProvider() {
        return this.packetTypesProvider;
    }
    
    @Override
    public <T> T get(final Class<T> objectClass) {
        return (T)this.storedObjects.get(objectClass);
    }
    
    @Override
    public void put(final Object object) {
        this.storedObjects.put(object.getClass(), object);
    }
    
    public PacketTypesProvider<CU, CM, SM, SU> packetTypesProvider() {
        return this.packetTypesProvider;
    }
    
    @Override
    public String toString() {
        return "Protocol:" + this.getClass().getSimpleName();
    }
}
