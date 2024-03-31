// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.core.util.FileUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.nio.channels.FileLock;
import java.nio.channels.FileChannel;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.LoggerContext;
import java.nio.ByteBuffer;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.io.OutputStream;

public class FileManager extends OutputStreamManager
{
    private static final FileManagerFactory FACTORY;
    private final boolean isAppend;
    private final boolean createOnDemand;
    private final boolean isLocking;
    private final String advertiseURI;
    private final int bufferSize;
    
    @Deprecated
    protected FileManager(final String fileName, final OutputStream os, final boolean append, final boolean locking, final String advertiseURI, final Layout<? extends Serializable> layout, final int bufferSize, final boolean writeHeader) {
        this(fileName, os, append, locking, advertiseURI, layout, writeHeader, ByteBuffer.wrap(new byte[bufferSize]));
    }
    
    @Deprecated
    protected FileManager(final String fileName, final OutputStream os, final boolean append, final boolean locking, final String advertiseURI, final Layout<? extends Serializable> layout, final boolean writeHeader, final ByteBuffer buffer) {
        super(os, fileName, layout, writeHeader, buffer);
        this.isAppend = append;
        this.createOnDemand = false;
        this.isLocking = locking;
        this.advertiseURI = advertiseURI;
        this.bufferSize = buffer.capacity();
    }
    
    protected FileManager(final LoggerContext loggerContext, final String fileName, final OutputStream os, final boolean append, final boolean locking, final boolean createOnDemand, final String advertiseURI, final Layout<? extends Serializable> layout, final boolean writeHeader, final ByteBuffer buffer) {
        super(loggerContext, os, fileName, createOnDemand, layout, writeHeader, buffer);
        this.isAppend = append;
        this.createOnDemand = createOnDemand;
        this.isLocking = locking;
        this.advertiseURI = advertiseURI;
        this.bufferSize = buffer.capacity();
    }
    
    public static FileManager getFileManager(final String fileName, final boolean append, boolean locking, final boolean bufferedIo, final boolean createOnDemand, final String advertiseUri, final Layout<? extends Serializable> layout, final int bufferSize, final Configuration configuration) {
        if (locking && bufferedIo) {
            locking = false;
        }
        return (FileManager)OutputStreamManager.getManager(fileName, new FactoryData(append, locking, bufferedIo, bufferSize, createOnDemand, advertiseUri, layout, configuration), FileManager.FACTORY);
    }
    
    @Override
    protected OutputStream createOutputStream() throws FileNotFoundException {
        final String filename = this.getFileName();
        FileManager.LOGGER.debug("Now writing to {} at {}", filename, new Date());
        return new FileOutputStream(filename, this.isAppend);
    }
    
    @Override
    protected synchronized void write(final byte[] bytes, final int offset, final int length, final boolean immediateFlush) {
        if (this.isLocking) {
            try {
                final FileChannel channel = ((FileOutputStream)this.getOutputStream()).getChannel();
                try (final FileLock lock = channel.lock(0L, Long.MAX_VALUE, false)) {
                    super.write(bytes, offset, length, immediateFlush);
                }
                return;
            }
            catch (final IOException ex) {
                throw new AppenderLoggingException("Unable to obtain lock on " + this.getName(), ex);
            }
        }
        super.write(bytes, offset, length, immediateFlush);
    }
    
    @Override
    protected synchronized void writeToDestination(final byte[] bytes, final int offset, final int length) {
        if (this.isLocking) {
            try {
                final FileChannel channel = ((FileOutputStream)this.getOutputStream()).getChannel();
                try (final FileLock lock = channel.lock(0L, Long.MAX_VALUE, false)) {
                    super.writeToDestination(bytes, offset, length);
                }
                return;
            }
            catch (final IOException ex) {
                throw new AppenderLoggingException("Unable to obtain lock on " + this.getName(), ex);
            }
        }
        super.writeToDestination(bytes, offset, length);
    }
    
    public String getFileName() {
        return this.getName();
    }
    
    public boolean isAppend() {
        return this.isAppend;
    }
    
    public boolean isCreateOnDemand() {
        return this.createOnDemand;
    }
    
    public boolean isLocking() {
        return this.isLocking;
    }
    
    public int getBufferSize() {
        return this.bufferSize;
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>(super.getContentFormat());
        result.put("fileURI", this.advertiseURI);
        return result;
    }
    
    static {
        FACTORY = new FileManagerFactory();
    }
    
    private static class FactoryData extends ConfigurationFactoryData
    {
        private final boolean append;
        private final boolean locking;
        private final boolean bufferedIo;
        private final int bufferSize;
        private final boolean createOnDemand;
        private final String advertiseURI;
        private final Layout<? extends Serializable> layout;
        
        public FactoryData(final boolean append, final boolean locking, final boolean bufferedIo, final int bufferSize, final boolean createOnDemand, final String advertiseURI, final Layout<? extends Serializable> layout, final Configuration configuration) {
            super(configuration);
            this.append = append;
            this.locking = locking;
            this.bufferedIo = bufferedIo;
            this.bufferSize = bufferSize;
            this.createOnDemand = createOnDemand;
            this.advertiseURI = advertiseURI;
            this.layout = layout;
        }
    }
    
    private static class FileManagerFactory implements ManagerFactory<FileManager, FactoryData>
    {
        @Override
        public FileManager createManager(final String name, final FactoryData data) {
            final File file = new File(name);
            try {
                FileUtils.makeParentDirs(file);
                final boolean writeHeader = !data.append || !file.exists();
                final int actualSize = data.bufferedIo ? data.bufferSize : Constants.ENCODER_BYTE_BUFFER_SIZE;
                final ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[actualSize]);
                final FileOutputStream fos = data.createOnDemand ? null : new FileOutputStream(file, data.append);
                return new FileManager(data.getLoggerContext(), name, fos, data.append, data.locking, data.createOnDemand, data.advertiseURI, data.layout, writeHeader, byteBuffer);
            }
            catch (final IOException ex) {
                AbstractManager.LOGGER.error("FileManager (" + name + ") " + ex, ex);
                return null;
            }
        }
    }
}
