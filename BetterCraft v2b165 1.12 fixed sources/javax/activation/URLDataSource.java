// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

import java.io.OutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.io.IOException;
import java.net.URL;

public class URLDataSource implements DataSource
{
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private final URL url;
    
    public URLDataSource(final URL url) {
        this.url = url;
    }
    
    @Override
    public String getContentType() {
        try {
            final URLConnection connection = this.url.openConnection();
            final String type = connection.getContentType();
            return (type == null) ? "application/octet-stream" : type;
        }
        catch (final IOException e) {
            return "application/octet-stream";
        }
    }
    
    @Override
    public String getName() {
        return this.url.getFile();
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        return this.url.openStream();
    }
    
    @Override
    public OutputStream getOutputStream() throws IOException {
        final URLConnection connection = this.url.openConnection();
        connection.setDoOutput(true);
        return connection.getOutputStream();
    }
    
    public URL getURL() {
        return this.url;
    }
}
