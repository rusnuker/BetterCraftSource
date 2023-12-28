// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

import java.io.PipedOutputStream;
import java.io.PipedInputStream;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class DataHandler implements Transferable
{
    private final DataSource ds;
    private final DataFlavor flavor;
    private CommandMap commandMap;
    private DataContentHandler dch;
    private DataContentHandlerFactory originalFactory;
    private static DataContentHandlerFactory factory;
    
    public DataHandler(final DataSource ds) {
        synchronized (DataHandler.class) {
            this.originalFactory = DataHandler.factory;
            monitorexit(DataHandler.class);
        }
        this.ds = ds;
        this.flavor = new ActivationDataFlavor(ds.getContentType(), null);
    }
    
    public DataHandler(final Object data, final String type) {
        synchronized (DataHandler.class) {
            this.originalFactory = DataHandler.factory;
            monitorexit(DataHandler.class);
        }
        this.ds = new ObjectDataSource(data, type);
        this.flavor = new ActivationDataFlavor(data.getClass(), null);
    }
    
    public DataHandler(final URL url) {
        synchronized (DataHandler.class) {
            this.originalFactory = DataHandler.factory;
            monitorexit(DataHandler.class);
        }
        this.ds = new URLDataSource(url);
        this.flavor = new ActivationDataFlavor(this.ds.getContentType(), null);
    }
    
    public DataSource getDataSource() {
        return this.ds;
    }
    
    public String getName() {
        return this.ds.getName();
    }
    
    public String getContentType() {
        return this.ds.getContentType();
    }
    
    public InputStream getInputStream() throws IOException {
        return this.ds.getInputStream();
    }
    
    public void writeTo(final OutputStream os) throws IOException {
        if (this.ds instanceof ObjectDataSource) {
            final ObjectDataSource ods = (ObjectDataSource)this.ds;
            final DataContentHandler dch = this.getDataContentHandler();
            if (dch == null) {
                throw new UnsupportedDataTypeException(ods.mimeType);
            }
            dch.writeTo(ods.data, ods.mimeType, os);
        }
        else {
            final byte[] buffer = new byte[1024];
            final InputStream is = this.getInputStream();
            try {
                int count;
                while ((count = is.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                }
            }
            finally {
                is.close();
            }
            is.close();
        }
    }
    
    public OutputStream getOutputStream() throws IOException {
        return this.ds.getOutputStream();
    }
    
    @Override
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return this.getDataContentHandler().getTransferDataFlavors();
    }
    
    @Override
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        final DataFlavor[] flavors = this.getTransferDataFlavors();
        for (int i = 0; i < flavors.length; ++i) {
            final DataFlavor dataFlavor = flavors[i];
            if (dataFlavor.equals(flavor)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        final DataContentHandler dch = this.getDataContentHandler();
        if (dch != null) {
            return dch.getTransferData(flavor, this.ds);
        }
        if (!this.flavor.match(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        if (this.ds instanceof ObjectDataSource) {
            return ((ObjectDataSource)this.ds).data;
        }
        return this.ds.getInputStream();
    }
    
    public CommandInfo[] getPreferredCommands() {
        return this.getCommandMap().getPreferredCommands(this.ds.getContentType());
    }
    
    public CommandInfo[] getAllCommands() {
        return this.getCommandMap().getAllCommands(this.ds.getContentType());
    }
    
    public CommandInfo getCommand(final String cmdName) {
        return this.getCommandMap().getCommand(this.ds.getContentType(), cmdName);
    }
    
    public Object getContent() throws IOException {
        if (this.ds instanceof ObjectDataSource) {
            return ((ObjectDataSource)this.ds).data;
        }
        final DataContentHandler dch = this.getDataContentHandler();
        if (dch != null) {
            return dch.getContent(this.ds);
        }
        return this.ds.getInputStream();
    }
    
    public Object getBean(final CommandInfo cmdinfo) {
        try {
            return cmdinfo.getCommandObject(this, this.getClass().getClassLoader());
        }
        catch (final IOException e) {
            return null;
        }
        catch (final ClassNotFoundException e2) {
            return null;
        }
    }
    
    public synchronized void setCommandMap(final CommandMap commandMap) {
        this.commandMap = commandMap;
        this.dch = null;
    }
    
    private synchronized CommandMap getCommandMap() {
        return (this.commandMap != null) ? this.commandMap : CommandMap.getDefaultCommandMap();
    }
    
    private synchronized DataContentHandler getDataContentHandler() {
        final DataContentHandlerFactory localFactory;
        synchronized (DataHandler.class) {
            if (DataHandler.factory != this.originalFactory) {
                this.dch = null;
                this.originalFactory = DataHandler.factory;
            }
            localFactory = this.originalFactory;
            monitorexit(DataHandler.class);
        }
        if (this.dch == null) {
            final String mimeType = this.getMimeType(this.ds.getContentType());
            if (localFactory != null) {
                this.dch = localFactory.createDataContentHandler(mimeType);
            }
            if (this.dch == null) {
                if (this.commandMap != null) {
                    this.dch = this.commandMap.createDataContentHandler(mimeType);
                }
                else {
                    this.dch = CommandMap.getDefaultCommandMap().createDataContentHandler(mimeType);
                }
            }
        }
        return this.dch;
    }
    
    private String getMimeType(final String contentType) {
        try {
            final MimeType mimeType = new MimeType(contentType);
            return mimeType.getBaseType();
        }
        catch (final MimeTypeParseException ex) {
            return contentType;
        }
    }
    
    public static synchronized void setDataContentHandlerFactory(final DataContentHandlerFactory newFactory) {
        if (DataHandler.factory != null) {
            throw new Error("javax.activation.DataHandler.setDataContentHandlerFactory has already been defined");
        }
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkSetFactory();
        }
        DataHandler.factory = newFactory;
    }
    
    private class ObjectDataSource implements DataSource
    {
        private final Object data;
        private final String mimeType;
        
        public ObjectDataSource(final Object data, final String mimeType) {
            this.data = data;
            this.mimeType = mimeType;
        }
        
        @Override
        public String getName() {
            return null;
        }
        
        @Override
        public String getContentType() {
            return this.mimeType;
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            final DataContentHandler dch = DataHandler.this.getDataContentHandler();
            if (dch == null) {
                throw new UnsupportedDataTypeException(this.mimeType);
            }
            final PipedInputStream is = new PipedInputStream();
            final PipedOutputStream os = new PipedOutputStream(is);
            final Thread thread = new Thread("DataHandler Pipe Pump") {
                @Override
                public void run() {
                    try {
                        try {
                            dch.writeTo(ObjectDataSource.this.data, ObjectDataSource.this.mimeType, os);
                        }
                        finally {
                            os.close();
                        }
                        os.close();
                    }
                    catch (final IOException ex) {}
                }
            };
            thread.start();
            return is;
        }
        
        @Override
        public OutputStream getOutputStream() throws IOException {
            return null;
        }
    }
}
