// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Set;
import java.nio.channels.spi.SelectorProvider;
import java.nio.channels.Selector;

final class SelectedSelectionKeySetSelector extends Selector
{
    private final SelectedSelectionKeySet selectionKeys;
    private final Selector delegate;
    
    SelectedSelectionKeySetSelector(final Selector delegate, final SelectedSelectionKeySet selectionKeys) {
        this.delegate = delegate;
        this.selectionKeys = selectionKeys;
    }
    
    @Override
    public boolean isOpen() {
        return this.delegate.isOpen();
    }
    
    @Override
    public SelectorProvider provider() {
        return this.delegate.provider();
    }
    
    @Override
    public Set<SelectionKey> keys() {
        return this.delegate.keys();
    }
    
    @Override
    public Set<SelectionKey> selectedKeys() {
        return this.delegate.selectedKeys();
    }
    
    @Override
    public int selectNow() throws IOException {
        this.selectionKeys.reset();
        return this.delegate.selectNow();
    }
    
    @Override
    public int select(final long timeout) throws IOException {
        this.selectionKeys.reset();
        return this.delegate.select(timeout);
    }
    
    @Override
    public int select() throws IOException {
        this.selectionKeys.reset();
        return this.delegate.select();
    }
    
    @Override
    public Selector wakeup() {
        return this.delegate.wakeup();
    }
    
    @Override
    public void close() throws IOException {
        this.delegate.close();
    }
}
