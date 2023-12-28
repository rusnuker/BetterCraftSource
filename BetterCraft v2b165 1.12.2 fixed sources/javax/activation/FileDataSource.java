// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;

public class FileDataSource implements DataSource
{
    private final File file;
    private FileTypeMap fileTypeMap;
    
    public FileDataSource(final File file) {
        this.file = file;
    }
    
    public FileDataSource(final String name) {
        this(new File(name));
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }
    
    @Override
    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(this.file);
    }
    
    @Override
    public String getContentType() {
        if (this.fileTypeMap == null) {
            return FileTypeMap.getDefaultFileTypeMap().getContentType(this.file);
        }
        return this.fileTypeMap.getContentType(this.file);
    }
    
    @Override
    public String getName() {
        return this.file.getName();
    }
    
    public File getFile() {
        return this.file;
    }
    
    public void setFileTypeMap(final FileTypeMap map) {
        this.fileTypeMap = map;
    }
}
