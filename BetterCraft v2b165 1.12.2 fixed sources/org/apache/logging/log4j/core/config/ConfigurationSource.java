// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config;

import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.net.URI;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.io.InputStream;
import java.net.URL;
import java.io.File;

public class ConfigurationSource
{
    public static final ConfigurationSource NULL_SOURCE;
    private final File file;
    private final URL url;
    private final String location;
    private final InputStream stream;
    private final byte[] data;
    
    public ConfigurationSource(final InputStream stream, final File file) {
        this.stream = Objects.requireNonNull(stream, "stream is null");
        this.file = Objects.requireNonNull(file, "file is null");
        this.location = file.getAbsolutePath();
        this.url = null;
        this.data = null;
    }
    
    public ConfigurationSource(final InputStream stream, final URL url) {
        this.stream = Objects.requireNonNull(stream, "stream is null");
        this.url = Objects.requireNonNull(url, "URL is null");
        this.location = url.toString();
        this.file = null;
        this.data = null;
    }
    
    public ConfigurationSource(final InputStream stream) throws IOException {
        this(toByteArray(stream));
    }
    
    private ConfigurationSource(final byte[] data) {
        this.data = Objects.requireNonNull(data, "data is null");
        this.stream = new ByteArrayInputStream(data);
        this.file = null;
        this.url = null;
        this.location = null;
    }
    
    private static byte[] toByteArray(final InputStream inputStream) throws IOException {
        final int buffSize = Math.max(4096, inputStream.available());
        final ByteArrayOutputStream contents = new ByteArrayOutputStream(buffSize);
        final byte[] buff = new byte[buffSize];
        for (int length = inputStream.read(buff); length > 0; length = inputStream.read(buff)) {
            contents.write(buff, 0, length);
        }
        return contents.toByteArray();
    }
    
    public File getFile() {
        return this.file;
    }
    
    public URL getURL() {
        return this.url;
    }
    
    public URI getURI() {
        URI sourceURI = null;
        if (this.url != null) {
            try {
                sourceURI = this.url.toURI();
            }
            catch (final URISyntaxException ex2) {}
        }
        if (sourceURI == null && this.file != null) {
            sourceURI = this.file.toURI();
        }
        if (sourceURI == null && this.location != null) {
            try {
                sourceURI = new URI(this.location);
            }
            catch (final URISyntaxException ex) {
                try {
                    sourceURI = new URI("file://" + this.location);
                }
                catch (final URISyntaxException ex3) {}
            }
        }
        return sourceURI;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public InputStream getInputStream() {
        return this.stream;
    }
    
    public ConfigurationSource resetInputStream() throws IOException {
        if (this.file != null) {
            return new ConfigurationSource(new FileInputStream(this.file), this.file);
        }
        if (this.url != null) {
            return new ConfigurationSource(this.url.openStream(), this.url);
        }
        return new ConfigurationSource(this.data);
    }
    
    @Override
    public String toString() {
        if (this.location != null) {
            return this.location;
        }
        if (this == ConfigurationSource.NULL_SOURCE) {
            return "NULL_SOURCE";
        }
        final int length = (this.data == null) ? -1 : this.data.length;
        return "stream (" + length + " bytes, unknown location)";
    }
    
    static {
        NULL_SOURCE = new ConfigurationSource(new byte[0]);
    }
}
