// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.browser;

import java.util.Iterator;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetDragEvent;
import org.cef.callback.CefDragData;
import java.awt.dnd.DropTargetListener;

class CefDropTargetListenerOsr implements DropTargetListener
{
    private CefBrowserOsr browser_;
    private CefDragData dragData_;
    private int dragOperations_;
    private int dragModifiers_;
    private int acceptOperations_;
    
    CefDropTargetListenerOsr(final CefBrowserOsr browser) {
        this.dragData_ = null;
        this.dragOperations_ = 1;
        this.dragModifiers_ = 0;
        this.acceptOperations_ = 1;
        this.browser_ = browser;
    }
    
    @Override
    public void dragEnter(final DropTargetDragEvent event) {
        this.CreateDragData(event);
        this.browser_.dragTargetDragEnter(this.dragData_, event.getLocation(), this.dragModifiers_, this.dragOperations_);
    }
    
    @Override
    public void dragExit(final DropTargetEvent event) {
        this.AssertDragData();
        this.browser_.dragTargetDragLeave();
        this.ClearDragData();
    }
    
    @Override
    public void dragOver(final DropTargetDragEvent event) {
        this.AssertDragData();
        this.browser_.dragTargetDragOver(event.getLocation(), this.dragModifiers_, this.dragOperations_);
    }
    
    @Override
    public void dropActionChanged(final DropTargetDragEvent event) {
        this.AssertDragData();
        switch (this.acceptOperations_ = event.getDropAction()) {
            case 1073741824: {
                this.dragOperations_ = 2;
                this.dragModifiers_ = 6;
                break;
            }
            case 1: {
                this.dragOperations_ = 1;
                this.dragModifiers_ = 4;
                break;
            }
            case 2: {
                this.dragOperations_ = 16;
                this.dragModifiers_ = 2;
                break;
            }
            case 0: {
                this.dragOperations_ = 1;
                this.dragModifiers_ = 0;
                this.acceptOperations_ = 1;
                break;
            }
        }
    }
    
    @Override
    public void drop(final DropTargetDropEvent event) {
        this.AssertDragData();
        this.browser_.dragTargetDrop(event.getLocation(), this.dragModifiers_);
        event.acceptDrop(this.acceptOperations_);
        event.dropComplete(true);
        this.ClearDragData();
    }
    
    private void CreateDragData(final DropTargetDragEvent event) {
        assert this.dragData_ == null;
        this.dragData_ = createDragData(event);
        this.dropActionChanged(event);
    }
    
    private void AssertDragData() {
        assert this.dragData_ != null;
    }
    
    private void ClearDragData() {
        this.dragData_ = null;
    }
    
    private static CefDragData createDragData(final DropTargetDragEvent event) {
        final CefDragData dragData = CefDragData.create();
        final Transferable transferable = event.getTransferable();
        final DataFlavor[] flavors = transferable.getTransferDataFlavors();
        DataFlavor[] array;
        for (int length = (array = flavors).length, i = 0; i < length; ++i) {
            final DataFlavor flavor = array[i];
            try {
                if (flavor.isFlavorJavaFileListType()) {
                    final List<File> files = (List<File>)transferable.getTransferData(flavor);
                    for (final File file : files) {
                        dragData.addFile(file.getPath(), file.getName());
                    }
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return dragData;
    }
}
