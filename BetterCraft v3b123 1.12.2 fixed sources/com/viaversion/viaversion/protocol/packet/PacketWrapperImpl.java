// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocol.packet;

import java.util.Objects;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import io.netty.channel.ChannelFuture;
import java.util.NoSuchElementException;
import com.viaversion.viaversion.util.PipelineUtil;
import com.viaversion.viaversion.exception.CancelException;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import com.viaversion.viaversion.exception.InformativeException;
import java.util.Collection;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.type.TypeConverter;
import java.io.IOException;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import com.viaversion.viaversion.api.type.Type;
import java.util.ArrayList;
import java.util.ArrayDeque;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Deque;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;

public class PacketWrapperImpl implements PacketWrapper
{
    private static final Protocol[] PROTOCOL_ARRAY;
    private final Deque<PacketValue> readableObjects;
    private final List<PacketValue> packetValues;
    private final ByteBuf inputBuffer;
    private final UserConnection userConnection;
    private boolean send;
    private PacketType packetType;
    private int id;
    
    public PacketWrapperImpl(final int packetId, final ByteBuf inputBuffer, final UserConnection userConnection) {
        this.readableObjects = new ArrayDeque<PacketValue>();
        this.packetValues = new ArrayList<PacketValue>();
        this.send = true;
        this.id = packetId;
        this.inputBuffer = inputBuffer;
        this.userConnection = userConnection;
    }
    
    public PacketWrapperImpl(final PacketType packetType, final ByteBuf inputBuffer, final UserConnection userConnection) {
        this.readableObjects = new ArrayDeque<PacketValue>();
        this.packetValues = new ArrayList<PacketValue>();
        this.send = true;
        this.packetType = packetType;
        this.id = ((packetType != null) ? packetType.getId() : -1);
        this.inputBuffer = inputBuffer;
        this.userConnection = userConnection;
    }
    
    @Override
    public <T> T get(final Type<T> type, final int index) throws Exception {
        int currentIndex = 0;
        for (final PacketValue packetValue : this.packetValues) {
            if (packetValue.type() != type) {
                continue;
            }
            if (currentIndex == index) {
                return (T)packetValue.value();
            }
            ++currentIndex;
        }
        throw this.createInformativeException(new ArrayIndexOutOfBoundsException("Could not find type " + type.getTypeName() + " at " + index), type, index);
    }
    
    @Override
    public boolean is(final Type type, final int index) {
        int currentIndex = 0;
        for (final PacketValue packetValue : this.packetValues) {
            if (packetValue.type() != type) {
                continue;
            }
            if (currentIndex == index) {
                return true;
            }
            ++currentIndex;
        }
        return false;
    }
    
    @Override
    public boolean isReadable(final Type type, final int index) {
        int currentIndex = 0;
        for (final PacketValue packetValue : this.readableObjects) {
            if (packetValue.type().getBaseClass() != type.getBaseClass()) {
                continue;
            }
            if (currentIndex == index) {
                return true;
            }
            ++currentIndex;
        }
        return false;
    }
    
    @Override
    public <T> void set(final Type<T> type, final int index, final T value) throws Exception {
        int currentIndex = 0;
        for (final PacketValue packetValue : this.packetValues) {
            if (packetValue.type() != type) {
                continue;
            }
            if (currentIndex == index) {
                packetValue.setValue(this.attemptTransform(type, value));
                return;
            }
            ++currentIndex;
        }
        throw this.createInformativeException(new ArrayIndexOutOfBoundsException("Could not find type " + type.getTypeName() + " at " + index), type, index);
    }
    
    @Override
    public <T> T read(final Type<T> type) throws Exception {
        if (type == Type.NOTHING) {
            return null;
        }
        if (this.readableObjects.isEmpty()) {
            Preconditions.checkNotNull(this.inputBuffer, (Object)"This packet does not have an input buffer.");
            try {
                return type.read(this.inputBuffer);
            }
            catch (final Exception e) {
                throw this.createInformativeException(e, type, this.packetValues.size() + 1);
            }
        }
        final PacketValue readValue = this.readableObjects.poll();
        final Type readType = readValue.type();
        if (readType == type || (type.getBaseClass() == readType.getBaseClass() && type.getOutputClass() == readType.getOutputClass())) {
            return (T)readValue.value();
        }
        if (readType == Type.NOTHING) {
            return (T)this.read((Type<Object>)type);
        }
        throw this.createInformativeException(new IOException("Unable to read type " + type.getTypeName() + ", found " + readValue.type().getTypeName()), type, this.readableObjects.size());
    }
    
    @Override
    public <T> void write(final Type<T> type, final T value) {
        this.packetValues.add(new PacketValue((Type)type, this.attemptTransform(type, value)));
    }
    
    private Object attemptTransform(final Type<?> expectedType, final Object value) {
        if (value != null && !expectedType.getOutputClass().isAssignableFrom(value.getClass())) {
            if (expectedType instanceof TypeConverter) {
                return ((TypeConverter)expectedType).from(value);
            }
            Via.getPlatform().getLogger().warning("Possible type mismatch: " + value.getClass().getName() + " -> " + expectedType.getOutputClass());
        }
        return value;
    }
    
    @Override
    public <T> T passthrough(final Type<T> type) throws Exception {
        final T value = (T)this.read((Type<Object>)type);
        this.write(type, value);
        return value;
    }
    
    @Override
    public void passthroughAll() throws Exception {
        this.packetValues.addAll(this.readableObjects);
        this.readableObjects.clear();
        if (this.inputBuffer.isReadable()) {
            this.passthrough(Type.REMAINING_BYTES);
        }
    }
    
    @Override
    public void writeToBuffer(final ByteBuf buffer) throws Exception {
        if (this.id != -1) {
            Type.VAR_INT.writePrimitive(buffer, this.id);
        }
        if (!this.readableObjects.isEmpty()) {
            this.packetValues.addAll(this.readableObjects);
            this.readableObjects.clear();
        }
        int index = 0;
        for (final PacketValue packetValue : this.packetValues) {
            try {
                packetValue.type().write(buffer, packetValue.value());
            }
            catch (final Exception e) {
                throw this.createInformativeException(e, packetValue.type(), index);
            }
            ++index;
        }
        this.writeRemaining(buffer);
    }
    
    private InformativeException createInformativeException(final Exception cause, final Type<?> type, final int index) {
        return new InformativeException(cause).set("Index", index).set("Type", type.getTypeName()).set("Packet ID", this.id).set("Packet Type", this.packetType).set("Data", this.packetValues);
    }
    
    @Override
    public void clearInputBuffer() {
        if (this.inputBuffer != null) {
            this.inputBuffer.clear();
        }
        this.readableObjects.clear();
    }
    
    @Override
    public void clearPacket() {
        this.clearInputBuffer();
        this.packetValues.clear();
    }
    
    private void writeRemaining(final ByteBuf output) {
        if (this.inputBuffer != null) {
            output.writeBytes(this.inputBuffer);
        }
    }
    
    @Override
    public void send(final Class<? extends Protocol> protocol, final boolean skipCurrentPipeline) throws Exception {
        this.send0(protocol, skipCurrentPipeline, true);
    }
    
    @Override
    public void scheduleSend(final Class<? extends Protocol> protocol, final boolean skipCurrentPipeline) throws Exception {
        this.send0(protocol, skipCurrentPipeline, false);
    }
    
    private void send0(final Class<? extends Protocol> protocol, final boolean skipCurrentPipeline, final boolean currentThread) throws Exception {
        if (this.isCancelled()) {
            return;
        }
        final UserConnection connection = this.user();
        if (currentThread) {
            try {
                final ByteBuf output = this.constructPacket(protocol, skipCurrentPipeline, Direction.CLIENTBOUND);
                connection.sendRawPacket(output);
            }
            catch (final Exception e) {
                if (!PipelineUtil.containsCause(e, CancelException.class)) {
                    throw e;
                }
            }
            return;
        }
        connection.getChannel().eventLoop().submit(() -> {
            try {
                final ByteBuf output2 = this.constructPacket(protocol, skipCurrentPipeline, Direction.CLIENTBOUND);
                connection.sendRawPacket(output2);
            }
            catch (final RuntimeException e2) {
                if (!PipelineUtil.containsCause(e2, CancelException.class)) {
                    throw e2;
                }
            }
            catch (final Exception e3) {
                if (!PipelineUtil.containsCause(e3, CancelException.class)) {
                    throw new RuntimeException(e3);
                }
            }
        });
    }
    
    private ByteBuf constructPacket(final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline, final Direction direction) throws Exception {
        final Protocol[] protocols = this.user().getProtocolInfo().getPipeline().pipes().toArray(PacketWrapperImpl.PROTOCOL_ARRAY);
        final boolean reverse = direction == Direction.CLIENTBOUND;
        int index = -1;
        for (int i = 0; i < protocols.length; ++i) {
            if (protocols[i].getClass() == packetProtocol) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new NoSuchElementException(packetProtocol.getCanonicalName());
        }
        if (skipCurrentPipeline) {
            index = (reverse ? (index - 1) : (index + 1));
        }
        this.resetReader();
        this.apply(direction, this.user().getProtocolInfo().getState(), index, protocols, reverse);
        final ByteBuf output = (this.inputBuffer == null) ? this.user().getChannel().alloc().buffer() : this.inputBuffer.alloc().buffer();
        try {
            this.writeToBuffer(output);
            return output.retain();
        }
        finally {
            output.release();
        }
    }
    
    @Override
    public ChannelFuture sendFuture(final Class<? extends Protocol> packetProtocol) throws Exception {
        if (!this.isCancelled()) {
            final ByteBuf output = this.constructPacket(packetProtocol, true, Direction.CLIENTBOUND);
            return this.user().sendRawPacketFuture(output);
        }
        return this.user().getChannel().newFailedFuture(new Exception("Cancelled packet"));
    }
    
    @Override
    public void sendRaw() throws Exception {
        this.sendRaw(true);
    }
    
    @Override
    public void scheduleSendRaw() throws Exception {
        this.sendRaw(false);
    }
    
    private void sendRaw(final boolean currentThread) throws Exception {
        if (this.isCancelled()) {
            return;
        }
        final ByteBuf output = (this.inputBuffer == null) ? this.user().getChannel().alloc().buffer() : this.inputBuffer.alloc().buffer();
        try {
            this.writeToBuffer(output);
            if (currentThread) {
                this.user().sendRawPacket(output.retain());
            }
            else {
                this.user().scheduleSendRawPacket(output.retain());
            }
        }
        finally {
            output.release();
        }
    }
    
    @Override
    public PacketWrapperImpl create(final int packetId) {
        return new PacketWrapperImpl(packetId, null, this.user());
    }
    
    @Override
    public PacketWrapperImpl create(final int packetId, final PacketHandler handler) throws Exception {
        final PacketWrapperImpl wrapper = this.create(packetId);
        handler.handle(wrapper);
        return wrapper;
    }
    
    @Override
    public PacketWrapperImpl apply(final Direction direction, final State state, final int index, final List<Protocol> pipeline, final boolean reverse) throws Exception {
        final Protocol[] array = pipeline.toArray(PacketWrapperImpl.PROTOCOL_ARRAY);
        return this.apply(direction, state, reverse ? (array.length - 1) : index, array, reverse);
    }
    
    @Override
    public PacketWrapperImpl apply(final Direction direction, final State state, final int index, final List<Protocol> pipeline) throws Exception {
        return this.apply(direction, state, index, pipeline.toArray(PacketWrapperImpl.PROTOCOL_ARRAY), false);
    }
    
    private PacketWrapperImpl apply(final Direction direction, final State state, final int index, final Protocol[] pipeline, final boolean reverse) throws Exception {
        if (reverse) {
            for (int i = index; i >= 0; --i) {
                pipeline[i].transform(direction, state, this);
                this.resetReader();
            }
        }
        else {
            for (int i = index; i < pipeline.length; ++i) {
                pipeline[i].transform(direction, state, this);
                this.resetReader();
            }
        }
        return this;
    }
    
    @Override
    public boolean isCancelled() {
        return !this.send;
    }
    
    @Override
    public void setCancelled(final boolean cancel) {
        this.send = !cancel;
    }
    
    @Override
    public UserConnection user() {
        return this.userConnection;
    }
    
    @Override
    public void resetReader() {
        for (int i = this.packetValues.size() - 1; i >= 0; --i) {
            this.readableObjects.addFirst(this.packetValues.get(i));
        }
        this.packetValues.clear();
    }
    
    @Override
    public void sendToServerRaw() throws Exception {
        this.sendToServerRaw(true);
    }
    
    @Override
    public void scheduleSendToServerRaw() throws Exception {
        this.sendToServerRaw(false);
    }
    
    private void sendToServerRaw(final boolean currentThread) throws Exception {
        if (this.isCancelled()) {
            return;
        }
        final ByteBuf output = (this.inputBuffer == null) ? this.user().getChannel().alloc().buffer() : this.inputBuffer.alloc().buffer();
        try {
            this.writeToBuffer(output);
            if (currentThread) {
                this.user().sendRawPacketToServer(output.retain());
            }
            else {
                this.user().scheduleSendRawPacketToServer(output.retain());
            }
        }
        finally {
            output.release();
        }
    }
    
    @Override
    public void sendToServer(final Class<? extends Protocol> protocol, final boolean skipCurrentPipeline) throws Exception {
        this.sendToServer0(protocol, skipCurrentPipeline, true);
    }
    
    @Override
    public void scheduleSendToServer(final Class<? extends Protocol> protocol, final boolean skipCurrentPipeline) throws Exception {
        this.sendToServer0(protocol, skipCurrentPipeline, false);
    }
    
    private void sendToServer0(final Class<? extends Protocol> protocol, final boolean skipCurrentPipeline, final boolean currentThread) throws Exception {
        if (this.isCancelled()) {
            return;
        }
        final UserConnection connection = this.user();
        if (currentThread) {
            try {
                final ByteBuf output = this.constructPacket(protocol, skipCurrentPipeline, Direction.SERVERBOUND);
                connection.sendRawPacketToServer(output);
            }
            catch (final Exception e) {
                if (!PipelineUtil.containsCause(e, CancelException.class)) {
                    throw e;
                }
            }
            return;
        }
        connection.getChannel().eventLoop().submit(() -> {
            try {
                final ByteBuf output2 = this.constructPacket(protocol, skipCurrentPipeline, Direction.SERVERBOUND);
                connection.sendRawPacketToServer(output2);
            }
            catch (final RuntimeException e2) {
                if (!PipelineUtil.containsCause(e2, CancelException.class)) {
                    throw e2;
                }
            }
            catch (final Exception e3) {
                if (!PipelineUtil.containsCause(e3, CancelException.class)) {
                    throw new RuntimeException(e3);
                }
            }
        });
    }
    
    @Override
    public PacketType getPacketType() {
        return this.packetType;
    }
    
    @Override
    public void setPacketType(final PacketType packetType) {
        this.packetType = packetType;
        this.id = ((packetType != null) ? packetType.getId() : -1);
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Deprecated
    @Override
    public void setId(final int id) {
        this.packetType = null;
        this.id = id;
    }
    
    public ByteBuf getInputBuffer() {
        return this.inputBuffer;
    }
    
    @Override
    public String toString() {
        return "PacketWrapper{type=" + this.packetType + ", id=" + this.id + ", values=" + this.packetValues + ", readable=" + this.readableObjects + '}';
    }
    
    static {
        PROTOCOL_ARRAY = new Protocol[0];
    }
    
    public static final class PacketValue
    {
        private final Type type;
        private Object value;
        
        private PacketValue(final Type type, final Object value) {
            this.type = type;
            this.value = value;
        }
        
        public Type type() {
            return this.type;
        }
        
        public Object value() {
            return this.value;
        }
        
        public void setValue(final Object value) {
            this.value = value;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final PacketValue that = (PacketValue)o;
            return this.type.equals(that.type) && Objects.equals(this.value, that.value);
        }
        
        @Override
        public int hashCode() {
            int result = this.type.hashCode();
            result = 31 * result + ((this.value != null) ? this.value.hashCode() : 0);
            return result;
        }
        
        @Override
        public String toString() {
            return "{" + this.type + ": " + this.value + "}";
        }
    }
}
