// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.multipart;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import java.nio.channels.WritableByteChannel;
import io.netty.handler.codec.http.HttpConstants;
import java.io.FileInputStream;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.EmptyArrays;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.io.FileOutputStream;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.channels.FileChannel;
import java.io.File;
import io.netty.util.internal.logging.InternalLogger;

public abstract class AbstractDiskHttpData extends AbstractHttpData
{
    private static final InternalLogger logger;
    private File file;
    private boolean isRenamed;
    private FileChannel fileChannel;
    
    protected AbstractDiskHttpData(final String name, final Charset charset, final long size) {
        super(name, charset, size);
    }
    
    protected abstract String getDiskFilename();
    
    protected abstract String getPrefix();
    
    protected abstract String getBaseDirectory();
    
    protected abstract String getPostfix();
    
    protected abstract boolean deleteOnExit();
    
    private File tempFile() throws IOException {
        final String diskFilename = this.getDiskFilename();
        String newpostfix;
        if (diskFilename != null) {
            newpostfix = '_' + diskFilename;
        }
        else {
            newpostfix = this.getPostfix();
        }
        File tmpFile;
        if (this.getBaseDirectory() == null) {
            tmpFile = File.createTempFile(this.getPrefix(), newpostfix);
        }
        else {
            tmpFile = File.createTempFile(this.getPrefix(), newpostfix, new File(this.getBaseDirectory()));
        }
        if (this.deleteOnExit()) {
            tmpFile.deleteOnExit();
        }
        return tmpFile;
    }
    
    @Override
    public void setContent(final ByteBuf buffer) throws IOException {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        try {
            this.checkSize(this.size = buffer.readableBytes());
            if (this.definedSize > 0L && this.definedSize < this.size) {
                throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
            }
            if (this.file == null) {
                this.file = this.tempFile();
            }
            if (buffer.readableBytes() == 0) {
                if (!this.file.createNewFile()) {
                    throw new IOException("file exists already: " + this.file);
                }
            }
            else {
                final FileOutputStream outputStream = new FileOutputStream(this.file);
                try {
                    FileChannel localfileChannel;
                    ByteBuffer byteBuffer;
                    int written;
                    for (localfileChannel = outputStream.getChannel(), byteBuffer = buffer.nioBuffer(), written = 0; written < this.size; written += localfileChannel.write(byteBuffer)) {}
                    buffer.readerIndex(buffer.readerIndex() + written);
                    localfileChannel.force(false);
                }
                finally {
                    outputStream.close();
                }
                this.setCompleted();
            }
        }
        finally {
            buffer.release();
        }
    }
    
    @Override
    public void addContent(final ByteBuf buffer, final boolean last) throws IOException {
        if (buffer != null) {
            try {
                final int localsize = buffer.readableBytes();
                this.checkSize(this.size + localsize);
                if (this.definedSize > 0L && this.definedSize < this.size + localsize) {
                    throw new IOException("Out of size: " + (this.size + localsize) + " > " + this.definedSize);
                }
                final ByteBuffer byteBuffer = (buffer.nioBufferCount() == 1) ? buffer.nioBuffer() : buffer.copy().nioBuffer();
                int written = 0;
                if (this.file == null) {
                    this.file = this.tempFile();
                }
                if (this.fileChannel == null) {
                    final FileOutputStream outputStream = new FileOutputStream(this.file);
                    this.fileChannel = outputStream.getChannel();
                }
                while (written < localsize) {
                    written += this.fileChannel.write(byteBuffer);
                }
                this.size += localsize;
                buffer.readerIndex(buffer.readerIndex() + written);
            }
            finally {
                buffer.release();
            }
        }
        if (last) {
            if (this.file == null) {
                this.file = this.tempFile();
            }
            if (this.fileChannel == null) {
                final FileOutputStream outputStream2 = new FileOutputStream(this.file);
                this.fileChannel = outputStream2.getChannel();
            }
            this.fileChannel.force(false);
            this.fileChannel.close();
            this.fileChannel = null;
            this.setCompleted();
        }
        else if (buffer == null) {
            throw new NullPointerException("buffer");
        }
    }
    
    @Override
    public void setContent(final File file) throws IOException {
        if (this.file != null) {
            this.delete();
        }
        this.file = file;
        this.checkSize(this.size = file.length());
        this.isRenamed = true;
        this.setCompleted();
    }
    
    @Override
    public void setContent(final InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new NullPointerException("inputStream");
        }
        if (this.file != null) {
            this.delete();
        }
        this.file = this.tempFile();
        final FileOutputStream outputStream = new FileOutputStream(this.file);
        int written = 0;
        try {
            final FileChannel localfileChannel = outputStream.getChannel();
            final byte[] bytes = new byte[16384];
            final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            for (int read = inputStream.read(bytes); read > 0; read = inputStream.read(bytes)) {
                byteBuffer.position(read).flip();
                written += localfileChannel.write(byteBuffer);
                this.checkSize(written);
            }
            localfileChannel.force(false);
        }
        finally {
            outputStream.close();
        }
        this.size = written;
        if (this.definedSize > 0L && this.definedSize < this.size) {
            if (!this.file.delete()) {
                AbstractDiskHttpData.logger.warn("Failed to delete: {}", this.file);
            }
            this.file = null;
            throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
        }
        this.isRenamed = true;
        this.setCompleted();
    }
    
    @Override
    public void delete() {
        if (this.fileChannel != null) {
            try {
                this.fileChannel.force(false);
                this.fileChannel.close();
            }
            catch (final IOException e) {
                AbstractDiskHttpData.logger.warn("Failed to close a file.", e);
            }
            this.fileChannel = null;
        }
        if (!this.isRenamed) {
            if (this.file != null && this.file.exists() && !this.file.delete()) {
                AbstractDiskHttpData.logger.warn("Failed to delete: {}", this.file);
            }
            this.file = null;
        }
    }
    
    @Override
    public byte[] get() throws IOException {
        if (this.file == null) {
            return EmptyArrays.EMPTY_BYTES;
        }
        return readFrom(this.file);
    }
    
    @Override
    public ByteBuf getByteBuf() throws IOException {
        if (this.file == null) {
            return Unpooled.EMPTY_BUFFER;
        }
        final byte[] array = readFrom(this.file);
        return Unpooled.wrappedBuffer(array);
    }
    
    @Override
    public ByteBuf getChunk(final int length) throws IOException {
        if (this.file == null || length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        if (this.fileChannel == null) {
            final FileInputStream inputStream = new FileInputStream(this.file);
            this.fileChannel = inputStream.getChannel();
        }
        int read = 0;
        final ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        while (read < length) {
            final int readnow = this.fileChannel.read(byteBuffer);
            if (readnow == -1) {
                this.fileChannel.close();
                this.fileChannel = null;
                break;
            }
            read += readnow;
        }
        if (read == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        byteBuffer.flip();
        final ByteBuf buffer = Unpooled.wrappedBuffer(byteBuffer);
        buffer.readerIndex(0);
        buffer.writerIndex(read);
        return buffer;
    }
    
    @Override
    public String getString() throws IOException {
        return this.getString(HttpConstants.DEFAULT_CHARSET);
    }
    
    @Override
    public String getString(final Charset encoding) throws IOException {
        if (this.file == null) {
            return "";
        }
        if (encoding == null) {
            final byte[] array = readFrom(this.file);
            return new String(array, HttpConstants.DEFAULT_CHARSET.name());
        }
        final byte[] array = readFrom(this.file);
        return new String(array, encoding.name());
    }
    
    @Override
    public boolean isInMemory() {
        return false;
    }
    
    @Override
    public boolean renameTo(final File dest) throws IOException {
        if (dest == null) {
            throw new NullPointerException("dest");
        }
        if (this.file == null) {
            throw new IOException("No file defined so cannot be renamed");
        }
        if (this.file.renameTo(dest)) {
            this.file = dest;
            return this.isRenamed = true;
        }
        IOException exception = null;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        long chunkSize = 8196L;
        long position = 0L;
        try {
            inputStream = new FileInputStream(this.file);
            outputStream = new FileOutputStream(dest);
            for (FileChannel in = inputStream.getChannel(), out = outputStream.getChannel(); position < this.size; position += in.transferTo(position, chunkSize, out)) {
                if (chunkSize < this.size - position) {
                    chunkSize = this.size - position;
                }
            }
        }
        catch (final IOException e) {
            exception = e;
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (final IOException e) {
                    if (exception == null) {
                        exception = e;
                    }
                    else {
                        AbstractDiskHttpData.logger.warn("Multiple exceptions detected, the following will be suppressed {}", e);
                    }
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                }
                catch (final IOException e) {
                    if (exception == null) {
                        exception = e;
                    }
                    else {
                        AbstractDiskHttpData.logger.warn("Multiple exceptions detected, the following will be suppressed {}", e);
                    }
                }
            }
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (final IOException e2) {
                    if (exception == null) {
                        exception = e2;
                    }
                    else {
                        AbstractDiskHttpData.logger.warn("Multiple exceptions detected, the following will be suppressed {}", e2);
                    }
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                }
                catch (final IOException e2) {
                    if (exception == null) {
                        exception = e2;
                    }
                    else {
                        AbstractDiskHttpData.logger.warn("Multiple exceptions detected, the following will be suppressed {}", e2);
                    }
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
        if (position == this.size) {
            if (!this.file.delete()) {
                AbstractDiskHttpData.logger.warn("Failed to delete: {}", this.file);
            }
            this.file = dest;
            return this.isRenamed = true;
        }
        if (!dest.delete()) {
            AbstractDiskHttpData.logger.warn("Failed to delete: {}", dest);
        }
        return false;
    }
    
    private static byte[] readFrom(final File src) throws IOException {
        final long srcsize = src.length();
        if (srcsize > 2147483647L) {
            throw new IllegalArgumentException("File too big to be loaded in memory");
        }
        final FileInputStream inputStream = new FileInputStream(src);
        final byte[] array = new byte[(int)srcsize];
        try {
            final FileChannel fileChannel = inputStream.getChannel();
            final ByteBuffer byteBuffer = ByteBuffer.wrap(array);
            for (int read = 0; read < srcsize; read += fileChannel.read(byteBuffer)) {}
        }
        finally {
            inputStream.close();
        }
        return array;
    }
    
    @Override
    public File getFile() throws IOException {
        return this.file;
    }
    
    @Override
    public HttpData touch() {
        return this;
    }
    
    @Override
    public HttpData touch(final Object hint) {
        return this;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractDiskHttpData.class);
    }
}
