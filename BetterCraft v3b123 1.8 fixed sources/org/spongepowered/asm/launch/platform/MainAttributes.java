// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform;

import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.InvalidPathException;
import java.nio.file.FileSystemNotFoundException;
import java.io.BufferedInputStream;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.io.InputStream;
import com.google.common.io.ByteSource;
import java.util.jar.Manifest;
import java.io.IOException;
import java.util.jar.JarFile;
import java.io.File;
import org.spongepowered.asm.util.JavaVersion;
import org.spongepowered.asm.util.Files;
import java.util.jar.Attributes;
import java.net.URI;
import java.util.Map;

public final class MainAttributes
{
    private static final Map<URI, MainAttributes> instances;
    protected final Attributes attributes;
    
    private MainAttributes() {
        this.attributes = new Attributes();
    }
    
    private MainAttributes(final URI codeSource) {
        this.attributes = getAttributes(codeSource);
    }
    
    public final String get(final String name) {
        if (this.attributes != null) {
            return this.attributes.getValue(name);
        }
        return null;
    }
    
    public final String get(final Attributes.Name name) {
        if (this.attributes != null) {
            return this.attributes.getValue(name);
        }
        return null;
    }
    
    private static Attributes getAttributes(final URI codeSource) {
        if (codeSource == null) {
            return null;
        }
        if ("file".equals(codeSource.getScheme())) {
            final File file = Files.toFile(codeSource);
            if (file.isFile()) {
                final Attributes attributes = getJarAttributes(file);
                if (attributes != null) {
                    return attributes;
                }
            }
            else if (file.isDirectory()) {
                final Attributes attributes = getDirAttributes(file);
                if (attributes != null) {
                    return attributes;
                }
            }
        }
        else if (JavaVersion.current() >= 1.7) {
            final Attributes attributes2 = getNioAttributes(codeSource);
            if (attributes2 != null) {
                return attributes2;
            }
        }
        return new Attributes();
    }
    
    private static Attributes getJarAttributes(final File jar) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jar);
            final Manifest manifest = jarFile.getManifest();
            if (manifest != null) {
                return manifest.getMainAttributes();
            }
        }
        catch (final IOException ex) {}
        finally {
            try {
                if (jarFile != null) {
                    jarFile.close();
                }
            }
            catch (final IOException ex2) {}
        }
        return null;
    }
    
    private static Attributes getDirAttributes(final File dir) {
        final File manifestFile = new File(dir, "META-INF/MANIFEST.MF");
        if (manifestFile.isFile()) {
            final ByteSource source = com.google.common.io.Files.asByteSource(manifestFile);
            InputStream inputStream = null;
            try {
                inputStream = source.openBufferedStream();
                final Manifest manifest = new Manifest(inputStream);
                return manifest.getMainAttributes();
            }
            catch (final IOException ex) {}
            finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                catch (final IOException ex2) {}
            }
        }
        return null;
    }
    
    private static Attributes getNioAttributes(final URI uri) {
        try {
            final Path manifestPath = Paths.get(uri).resolve("META-INF/MANIFEST.MF");
            BufferedInputStream inputStream = null;
            try {
                inputStream = new BufferedInputStream(java.nio.file.Files.newInputStream(manifestPath, new OpenOption[0]));
                final Manifest manifest = new Manifest(inputStream);
                return manifest.getMainAttributes();
            }
            catch (final IOException ex3) {}
            finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                catch (final IOException ex4) {}
            }
        }
        catch (final FileSystemNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (final InvalidPathException ex2) {
            ex2.printStackTrace();
        }
        return null;
    }
    
    public static MainAttributes of(final File jar) {
        return of(jar.toURI());
    }
    
    public static MainAttributes of(final URI uri) {
        MainAttributes attributes = MainAttributes.instances.get(uri);
        if (attributes == null) {
            attributes = new MainAttributes(uri);
            MainAttributes.instances.put(uri, attributes);
        }
        return attributes;
    }
    
    static {
        instances = new HashMap<URI, MainAttributes>();
    }
}
