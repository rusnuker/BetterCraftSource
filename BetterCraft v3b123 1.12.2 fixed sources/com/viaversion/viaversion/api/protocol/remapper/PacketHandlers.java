// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol.remapper;

import java.util.Iterator;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.function.Function;
import com.viaversion.viaversion.api.type.Type;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public abstract class PacketHandlers implements PacketHandler
{
    private final List<PacketHandler> packetHandlers;
    
    protected PacketHandlers() {
        this.packetHandlers = new ArrayList<PacketHandler>();
        this.register();
    }
    
    static PacketHandler fromRemapper(final List<PacketHandler> valueRemappers) {
        final PacketHandlers handlers = new PacketHandlers() {
            public void register() {
            }
        };
        handlers.packetHandlers.addAll(valueRemappers);
        return handlers;
    }
    
    public <T> void map(final Type<T> type) {
        this.handler(wrapper -> wrapper.write(type, (Object)wrapper.read((Type<T>)type)));
    }
    
    public void map(final Type oldType, final Type newType) {
        this.handler(wrapper -> wrapper.write(newType, (Object)wrapper.read((Type<T>)oldType)));
    }
    
    public <T1, T2> void map(final Type<T1> oldType, final Type<T2> newType, final Function<T1, T2> transformer) {
        this.map(oldType, (ValueTransformer<T1, Object>)new ValueTransformer<T1, T2>(newType) {
            @Override
            public T2 transform(final PacketWrapper wrapper, final T1 inputValue) {
                return transformer.apply(inputValue);
            }
        });
    }
    
    public <T1, T2> void map(final ValueTransformer<T1, T2> transformer) {
        if (transformer.getInputType() == null) {
            throw new IllegalArgumentException("Use map(Type<T1>, ValueTransformer<T1, T2>) for value transformers without specified input type!");
        }
        this.map(transformer.getInputType(), transformer);
    }
    
    public <T1, T2> void map(final Type<T1> oldType, final ValueTransformer<T1, T2> transformer) {
        this.map(new TypeRemapper<Object>((Type<Object>)oldType), (ValueWriter<Object>)transformer);
    }
    
    public <T> void map(final ValueReader<T> inputReader, final ValueWriter<T> outputWriter) {
        this.handler(wrapper -> outputWriter.write(wrapper, inputReader.read(wrapper)));
    }
    
    public void handler(final PacketHandler handler) {
        this.packetHandlers.add(handler);
    }
    
    public <T> void create(final Type<T> type, final T value) {
        this.handler(wrapper -> wrapper.write(type, value));
    }
    
    public void read(final Type<?> type) {
        this.handler(wrapper -> wrapper.read((Type<Object>)type));
    }
    
    protected abstract void register();
    
    @Override
    public final void handle(final PacketWrapper wrapper) throws Exception {
        for (final PacketHandler handler : this.packetHandlers) {
            handler.handle(wrapper);
        }
    }
    
    public int handlersSize() {
        return this.packetHandlers.size();
    }
}
