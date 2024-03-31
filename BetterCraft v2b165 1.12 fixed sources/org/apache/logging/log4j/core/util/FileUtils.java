// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.Objects;
import java.io.IOException;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.net.URI;
import org.apache.logging.log4j.Logger;

public final class FileUtils
{
    private static final String PROTOCOL_FILE = "file";
    private static final String JBOSS_FILE = "vfsfile";
    private static final Logger LOGGER;
    
    private FileUtils() {
    }
    
    public static File fileFromUri(URI uri) {
        if (uri == null || (uri.getScheme() != null && !"file".equals(uri.getScheme()) && !"vfsfile".equals(uri.getScheme()))) {
            return null;
        }
        if (uri.getScheme() == null) {
            File file = new File(uri.toString());
            if (file.exists()) {
                return file;
            }
            try {
                final String path = uri.getPath();
                file = new File(path);
                if (file.exists()) {
                    return file;
                }
                uri = new File(path).toURI();
            }
            catch (final Exception ex) {
                FileUtils.LOGGER.warn("Invalid URI {}", uri);
                return null;
            }
        }
        final String charsetName = StandardCharsets.UTF_8.name();
        try {
            String fileName = uri.toURL().getFile();
            if (new File(fileName).exists()) {
                return new File(fileName);
            }
            fileName = URLDecoder.decode(fileName, charsetName);
            return new File(fileName);
        }
        catch (final MalformedURLException ex2) {
            FileUtils.LOGGER.warn("Invalid URL {}", uri, ex2);
        }
        catch (final UnsupportedEncodingException uee) {
            FileUtils.LOGGER.warn("Invalid encoding: {}", charsetName, uee);
        }
        return null;
    }
    
    public static boolean isFile(final URL url) {
        return url != null && (url.getProtocol().equals("file") || url.getProtocol().equals("vfsfile"));
    }
    
    public static String getFileExtension(final File file) {
        final String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return null;
    }
    
    public static void mkdir(final File dir, final boolean createDirectoryIfNotExisting) throws IOException {
        if (!dir.exists()) {
            if (!createDirectoryIfNotExisting) {
                throw new IOException("The directory " + dir.getAbsolutePath() + " does not exist.");
            }
            if (!dir.mkdirs()) {
                throw new IOException("Could not create directory " + dir.getAbsolutePath());
            }
        }
        if (!dir.isDirectory()) {
            throw new IOException("File " + dir + " exists and is not a directory. Unable to create directory.");
        }
    }
    
    public static void makeParentDirs(final File file) throws IOException {
        final File parent = Objects.requireNonNull(file, "file").getCanonicalFile().getParentFile();
        if (parent != null) {
            mkdir(parent, true);
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
