// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.client;

import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.io.FileUtils;
import com.google.common.io.Files;
import com.google.common.hash.Hashing;
import java.awt.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.io.BufferedOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import net.minecraft.realms.Realms;
import java.util.Locale;
import net.minecraft.realms.RealmsLevelSummary;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.realms.RealmsSharedConstants;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import net.minecraft.realms.RealmsAnvilLevelStorageSource;
import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
import com.mojang.realmsclient.dto.WorldDownload;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import java.io.IOException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class FileDownload
{
    private static final Logger LOGGER;
    private volatile boolean cancelled;
    private volatile boolean finished;
    private volatile boolean error;
    private volatile boolean extracting;
    private volatile File tempFile;
    private volatile File resourcePackPath;
    private volatile HttpGet request;
    private Thread currentThread;
    private final RequestConfig requestConfig;
    private static final String[] INVALID_FILE_NAMES;
    
    public FileDownload() {
        this.requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();
    }
    
    public long contentLength(final String downloadLink) {
        CloseableHttpClient client = null;
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(downloadLink);
            client = HttpClientBuilder.create().setDefaultRequestConfig(this.requestConfig).build();
            final CloseableHttpResponse response = client.execute((HttpUriRequest)httpGet);
            return Long.parseLong(response.getFirstHeader("Content-Length").getValue());
        }
        catch (final Throwable ignored) {
            FileDownload.LOGGER.error("Unable to get content length for download");
            return 0L;
        }
        finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
            if (client != null) {
                try {
                    client.close();
                }
                catch (final IOException e) {
                    FileDownload.LOGGER.error("Could not close http client", e);
                }
            }
        }
    }
    
    public void download(final WorldDownload worldDownload, final String worldName, final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus, final RealmsAnvilLevelStorageSource levelStorageSource) {
        if (this.currentThread != null) {
            return;
        }
        (this.currentThread = new Thread() {
            @Override
            public void run() {
                CloseableHttpClient client = null;
                try {
                    FileDownload.this.tempFile = File.createTempFile("backup", ".tar.gz");
                    FileDownload.this.request = new HttpGet(worldDownload.downloadLink);
                    client = HttpClientBuilder.create().setDefaultRequestConfig(FileDownload.this.requestConfig).build();
                    final HttpResponse response = client.execute((HttpUriRequest)FileDownload.this.request);
                    downloadStatus.totalBytes = Long.parseLong(response.getFirstHeader("Content-Length").getValue());
                    if (response.getStatusLine().getStatusCode() != 200) {
                        FileDownload.this.error = true;
                        FileDownload.this.request.abort();
                        return;
                    }
                    final OutputStream os = new FileOutputStream(FileDownload.this.tempFile);
                    final ProgressListener progressListener = new ProgressListener(worldName.trim(), FileDownload.this.tempFile, levelStorageSource, downloadStatus, worldDownload);
                    final DownloadCountingOutputStream dcount = new DownloadCountingOutputStream(os);
                    dcount.setListener(progressListener);
                    IOUtils.copy(response.getEntity().getContent(), dcount);
                }
                catch (final Exception e) {
                    FileDownload.LOGGER.error("Caught exception while downloading: " + e.getMessage());
                    FileDownload.this.error = true;
                    FileDownload.this.request.releaseConnection();
                    if (FileDownload.this.tempFile != null) {
                        FileDownload.this.tempFile.delete();
                    }
                    if (!FileDownload.this.error) {
                        if (!worldDownload.resourcePackUrl.isEmpty() && !worldDownload.resourcePackHash.isEmpty()) {
                            try {
                                FileDownload.this.tempFile = File.createTempFile("resources", ".tar.gz");
                                FileDownload.this.request = new HttpGet(worldDownload.resourcePackUrl);
                                final HttpResponse response = client.execute((HttpUriRequest)FileDownload.this.request);
                                downloadStatus.totalBytes = Long.parseLong(response.getFirstHeader("Content-Length").getValue());
                                if (response.getStatusLine().getStatusCode() != 200) {
                                    FileDownload.this.error = true;
                                    FileDownload.this.request.abort();
                                    return;
                                }
                                final OutputStream os = new FileOutputStream(FileDownload.this.tempFile);
                                final ResourcePackProgressListener progressListener2 = new ResourcePackProgressListener(FileDownload.this.tempFile, downloadStatus, worldDownload);
                                final DownloadCountingOutputStream dcount = new DownloadCountingOutputStream(os);
                                dcount.setListener(progressListener2);
                                IOUtils.copy(response.getEntity().getContent(), dcount);
                            }
                            catch (final Exception e2) {
                                FileDownload.LOGGER.error("Caught exception while downloading: " + e2.getMessage());
                                FileDownload.this.error = true;
                            }
                            finally {
                                FileDownload.this.request.releaseConnection();
                                if (FileDownload.this.tempFile != null) {
                                    FileDownload.this.tempFile.delete();
                                }
                            }
                        }
                        else {
                            FileDownload.this.finished = true;
                        }
                    }
                    if (client != null) {
                        try {
                            client.close();
                        }
                        catch (final IOException ignored) {
                            FileDownload.LOGGER.error("Failed to close Realms download client");
                        }
                    }
                }
                finally {
                    FileDownload.this.request.releaseConnection();
                    if (FileDownload.this.tempFile != null) {
                        FileDownload.this.tempFile.delete();
                    }
                    if (!FileDownload.this.error) {
                        if (!worldDownload.resourcePackUrl.isEmpty() && !worldDownload.resourcePackHash.isEmpty()) {
                            try {
                                FileDownload.this.tempFile = File.createTempFile("resources", ".tar.gz");
                                FileDownload.this.request = new HttpGet(worldDownload.resourcePackUrl);
                                final HttpResponse response2 = client.execute((HttpUriRequest)FileDownload.this.request);
                                downloadStatus.totalBytes = Long.parseLong(response2.getFirstHeader("Content-Length").getValue());
                                if (response2.getStatusLine().getStatusCode() != 200) {
                                    FileDownload.this.error = true;
                                    FileDownload.this.request.abort();
                                    return;
                                }
                                final OutputStream os2 = new FileOutputStream(FileDownload.this.tempFile);
                                final ResourcePackProgressListener progressListener3 = new ResourcePackProgressListener(FileDownload.this.tempFile, downloadStatus, worldDownload);
                                final DownloadCountingOutputStream dcount2 = new DownloadCountingOutputStream(os2);
                                dcount2.setListener(progressListener3);
                                IOUtils.copy(response2.getEntity().getContent(), dcount2);
                            }
                            catch (final Exception e3) {
                                FileDownload.LOGGER.error("Caught exception while downloading: " + e3.getMessage());
                                FileDownload.this.error = true;
                                FileDownload.this.request.releaseConnection();
                                if (FileDownload.this.tempFile != null) {
                                    FileDownload.this.tempFile.delete();
                                }
                            }
                            finally {
                                FileDownload.this.request.releaseConnection();
                                if (FileDownload.this.tempFile != null) {
                                    FileDownload.this.tempFile.delete();
                                }
                            }
                        }
                        else {
                            FileDownload.this.finished = true;
                        }
                    }
                    if (client != null) {
                        try {
                            client.close();
                        }
                        catch (final IOException ignored2) {
                            FileDownload.LOGGER.error("Failed to close Realms download client");
                        }
                    }
                }
            }
        }).start();
    }
    
    public void cancel() {
        if (this.request != null) {
            this.request.abort();
        }
        if (this.tempFile != null) {
            this.tempFile.delete();
        }
        this.cancelled = true;
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    public boolean isError() {
        return this.error;
    }
    
    public boolean isExtracting() {
        return this.extracting;
    }
    
    public static String findAvailableFolderName(String folder) {
        folder = folder.replaceAll("[\\./\"]", "_");
        for (final String invalidName : FileDownload.INVALID_FILE_NAMES) {
            if (folder.equalsIgnoreCase(invalidName)) {
                folder = "_" + folder + "_";
            }
        }
        return folder;
    }
    
    private void untarGzipArchive(String name, final File file, final RealmsAnvilLevelStorageSource levelStorageSource) throws IOException {
        final Pattern namePattern = Pattern.compile(".*-([0-9]+)$");
        int number = 1;
        for (final char replacer : RealmsSharedConstants.ILLEGAL_FILE_CHARACTERS) {
            name = name.replace(replacer, '_');
        }
        if (StringUtils.isEmpty(name)) {
            name = "Realm";
        }
        name = findAvailableFolderName(name);
        try {
            for (final RealmsLevelSummary summary : levelStorageSource.getLevelList()) {
                if (summary.getLevelId().toLowerCase(Locale.ROOT).startsWith(name.toLowerCase(Locale.ROOT))) {
                    final Matcher matcher = namePattern.matcher(summary.getLevelId());
                    if (matcher.matches()) {
                        if (Integer.valueOf(matcher.group(1)) <= number) {
                            continue;
                        }
                        number = Integer.valueOf(matcher.group(1));
                    }
                    else {
                        ++number;
                    }
                }
            }
        }
        catch (final Exception e) {
            FileDownload.LOGGER.error("Error getting level list", e);
            this.error = true;
            return;
        }
        String finalName;
        if (!levelStorageSource.isNewLevelIdAcceptable(name) || number > 1) {
            finalName = name + ((number == 1) ? "" : ("-" + number));
            if (!levelStorageSource.isNewLevelIdAcceptable(finalName)) {
                for (boolean foundName = false; !foundName; foundName = true) {
                    ++number;
                    finalName = name + ((number == 1) ? "" : ("-" + number));
                    if (levelStorageSource.isNewLevelIdAcceptable(finalName)) {}
                }
            }
        }
        else {
            finalName = name;
        }
        TarArchiveInputStream tarIn = null;
        final File saves = new File(Realms.getGameDirectoryPath(), "saves");
        try {
            saves.mkdir();
            tarIn = new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(file))));
            for (TarArchiveEntry tarEntry = tarIn.getNextTarEntry(); tarEntry != null; tarEntry = tarIn.getNextTarEntry()) {
                final File destPath = new File(saves, tarEntry.getName().replace("world", finalName));
                if (tarEntry.isDirectory()) {
                    destPath.mkdirs();
                }
                else {
                    destPath.createNewFile();
                    byte[] btoRead = new byte[1024];
                    final BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(destPath));
                    int len = 0;
                    while ((len = tarIn.read(btoRead)) != -1) {
                        bout.write(btoRead, 0, len);
                    }
                    bout.close();
                    btoRead = null;
                }
            }
        }
        catch (final Exception e2) {
            FileDownload.LOGGER.error("Error extracting world", e2);
            this.error = true;
        }
        finally {
            if (tarIn != null) {
                tarIn.close();
            }
            if (file != null) {
                file.delete();
            }
            final RealmsAnvilLevelStorageSource levelSource = levelStorageSource;
            levelSource.renameLevel(finalName, finalName.trim());
            final File dataFile = new File(saves, finalName + File.separator + "level.dat");
            Realms.deletePlayerTag(dataFile);
            this.resourcePackPath = new File(saves, finalName + File.separator + "resources.zip");
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        INVALID_FILE_NAMES = new String[] { "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9" };
    }
    
    private class ProgressListener implements ActionListener
    {
        private final String worldName;
        private final File tempFile;
        private final RealmsAnvilLevelStorageSource levelStorageSource;
        private final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus;
        private final WorldDownload worldDownload;
        
        private ProgressListener(final String worldName, final File tempFile, final RealmsAnvilLevelStorageSource levelStorageSource, final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus, final WorldDownload worldDownload) {
            this.worldName = worldName;
            this.tempFile = tempFile;
            this.levelStorageSource = levelStorageSource;
            this.downloadStatus = downloadStatus;
            this.worldDownload = worldDownload;
        }
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            this.downloadStatus.bytesWritten = ((DownloadCountingOutputStream)e.getSource()).getByteCount();
            if (this.downloadStatus.bytesWritten >= this.downloadStatus.totalBytes && !FileDownload.this.cancelled && !FileDownload.this.error) {
                try {
                    FileDownload.this.extracting = true;
                    FileDownload.this.untarGzipArchive(this.worldName, this.tempFile, this.levelStorageSource);
                }
                catch (final IOException e2) {
                    FileDownload.LOGGER.error("Error extracting archive", e2);
                    FileDownload.this.error = true;
                }
            }
        }
    }
    
    private class ResourcePackProgressListener implements ActionListener
    {
        private final File tempFile;
        private final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus;
        private final WorldDownload worldDownload;
        
        private ResourcePackProgressListener(final File tempFile, final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus, final WorldDownload worldDownload) {
            this.tempFile = tempFile;
            this.downloadStatus = downloadStatus;
            this.worldDownload = worldDownload;
        }
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            this.downloadStatus.bytesWritten = ((DownloadCountingOutputStream)e.getSource()).getByteCount();
            if (this.downloadStatus.bytesWritten >= this.downloadStatus.totalBytes && !FileDownload.this.cancelled) {
                try {
                    final String actualHash = Hashing.sha1().hashBytes(Files.toByteArray(this.tempFile)).toString();
                    if (actualHash.equals(this.worldDownload.resourcePackHash)) {
                        FileUtils.copyFile(this.tempFile, FileDownload.this.resourcePackPath);
                        FileDownload.this.finished = true;
                    }
                    else {
                        FileDownload.LOGGER.error("Resourcepack had wrong hash (expected " + this.worldDownload.resourcePackHash + ", found " + actualHash + "). Deleting it.");
                        FileUtils.deleteQuietly(this.tempFile);
                        FileDownload.this.error = true;
                    }
                }
                catch (final IOException e2) {
                    FileDownload.LOGGER.error("Error copying resourcepack file", e2.getMessage());
                    FileDownload.this.error = true;
                }
            }
        }
    }
    
    private class DownloadCountingOutputStream extends CountingOutputStream
    {
        private ActionListener listener;
        
        public DownloadCountingOutputStream(final OutputStream out) {
            super(out);
        }
        
        public void setListener(final ActionListener listener) {
            this.listener = listener;
        }
        
        @Override
        protected void afterWrite(final int n) throws IOException {
            super.afterWrite(n);
            if (this.listener != null) {
                this.listener.actionPerformed(new ActionEvent(this, 0, null));
            }
        }
    }
}
