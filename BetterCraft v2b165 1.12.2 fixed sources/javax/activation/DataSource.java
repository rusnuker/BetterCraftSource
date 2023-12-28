// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

public interface DataSource
{
    InputStream getInputStream() throws IOException;
    
    OutputStream getOutputStream() throws IOException;
    
    String getContentType();
    
    String getName();
}
