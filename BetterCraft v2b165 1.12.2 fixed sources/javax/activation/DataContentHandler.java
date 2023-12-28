// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

import java.io.OutputStream;
import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.DataFlavor;

public interface DataContentHandler
{
    DataFlavor[] getTransferDataFlavors();
    
    Object getTransferData(final DataFlavor p0, final DataSource p1) throws UnsupportedFlavorException, IOException;
    
    Object getContent(final DataSource p0) throws IOException;
    
    void writeTo(final Object p0, final String p1, final OutputStream p2) throws IOException;
}
