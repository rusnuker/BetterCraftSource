// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

import java.util.StringTokenizer;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Enumeration;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MimetypesFileTypeMap extends FileTypeMap
{
    private static final String DEFAULT_TYPE = "application/octet-stream";
    private final Map types;
    
    public MimetypesFileTypeMap() {
        this.types = new HashMap();
        try {
            final InputStream is = MimetypesFileTypeMap.class.getResourceAsStream("/META-INF/mimetypes.default");
            if (is != null) {
                try {
                    this.loadStream(is);
                }
                finally {
                    is.close();
                }
                is.close();
            }
        }
        catch (final IOException ex) {}
        try {
            final ClassLoader cl = MimetypesFileTypeMap.class.getClassLoader();
            final Enumeration e = cl.getResources("/META-INF/mime.types");
            while (e.hasMoreElements()) {
                final URL url = e.nextElement();
                try {
                    final InputStream is2 = url.openStream();
                    try {
                        this.loadStream(is2);
                    }
                    finally {
                        is2.close();
                    }
                    is2.close();
                }
                catch (final IOException ex2) {}
            }
        }
        catch (final SecurityException ex3) {}
        catch (final IOException ex4) {}
        try {
            final File file = new File(System.getProperty("java.home"), "lib/mime.types");
            final InputStream is3 = new FileInputStream(file);
            try {
                this.loadStream(is3);
            }
            finally {
                is3.close();
            }
            is3.close();
        }
        catch (final SecurityException ex5) {}
        catch (final FileNotFoundException ex6) {}
        catch (final IOException ex7) {}
        try {
            final File file = new File(System.getProperty("user.home"), ".mime.types");
            final InputStream is3 = new FileInputStream(file);
            try {
                this.loadStream(is3);
            }
            finally {
                is3.close();
            }
            is3.close();
        }
        catch (final SecurityException ex8) {}
        catch (final FileNotFoundException ex9) {}
        catch (final IOException ex10) {}
    }
    
    public MimetypesFileTypeMap(final String mimeTypeFileName) throws IOException {
        this();
        final BufferedReader reader = new BufferedReader(new FileReader(mimeTypeFileName));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                this.addMimeTypes(line);
            }
            reader.close();
        }
        catch (final IOException e) {
            try {
                reader.close();
            }
            catch (final IOException ex) {}
            throw e;
        }
    }
    
    public MimetypesFileTypeMap(final InputStream is) {
        this();
        try {
            this.loadStream(is);
        }
        catch (final IOException ex) {}
    }
    
    private void loadStream(final InputStream is) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            this.addMimeTypes(line);
        }
    }
    
    public synchronized void addMimeTypes(String mime_types) {
        final int hashPos = mime_types.indexOf(35);
        if (hashPos != -1) {
            mime_types = mime_types.substring(0, hashPos);
        }
        final StringTokenizer tok = new StringTokenizer(mime_types);
        if (!tok.hasMoreTokens()) {
            return;
        }
        final String contentType = tok.nextToken();
        while (tok.hasMoreTokens()) {
            final String fileType = tok.nextToken();
            this.types.put(fileType, contentType);
        }
    }
    
    @Override
    public String getContentType(final File f) {
        return this.getContentType(f.getName());
    }
    
    @Override
    public synchronized String getContentType(final String filename) {
        final int index = filename.lastIndexOf(46);
        if (index == -1 || index == filename.length() - 1) {
            return "application/octet-stream";
        }
        final String fileType = filename.substring(index + 1);
        final String contentType = this.types.get(fileType);
        return (contentType == null) ? "application/octet-stream" : contentType;
    }
}
