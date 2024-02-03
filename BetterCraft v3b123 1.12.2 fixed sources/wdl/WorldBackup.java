// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import net.minecraft.client.resources.I18n;
import org.apache.commons.io.FileUtils;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.io.File;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class WorldBackup
{
    private static final DateFormat DATE_FORMAT;
    
    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    }
    
    public static void backupWorld(final File worldFolder, final String worldName, final WorldBackupType type, final IBackupProgressMonitor monitor) throws IOException {
        final String newWorldName = String.valueOf(worldName) + "_" + WorldBackup.DATE_FORMAT.format(new Date());
        switch (type) {
            case NONE: {
                return;
            }
            case FOLDER: {
                final File destination = new File(worldFolder.getParentFile(), newWorldName);
                if (destination.exists()) {
                    throw new IOException("Backup folder (" + destination + ") already exists!");
                }
                copyDirectory(worldFolder, destination, monitor);
                return;
            }
            case ZIP: {
                final File destination = new File(worldFolder.getParentFile(), String.valueOf(newWorldName) + ".zip");
                if (destination.exists()) {
                    throw new IOException("Backup file (" + destination + ") already exists!");
                }
                zipDirectory(worldFolder, destination, monitor);
            }
            default: {}
        }
    }
    
    public static void copyDirectory(final File src, final File destination, final IBackupProgressMonitor monitor) throws IOException {
        monitor.setNumberOfFiles(countFilesInFolder(src));
        copy(src, destination, src.getPath().length() + 1, monitor);
    }
    
    public static void zipDirectory(final File src, final File destination, final IBackupProgressMonitor monitor) throws IOException {
        monitor.setNumberOfFiles(countFilesInFolder(src));
        FileOutputStream outStream = null;
        ZipOutputStream stream = null;
        try {
            outStream = new FileOutputStream(destination);
            try {
                stream = new ZipOutputStream(outStream);
                zipFolder(src, stream, src.getPath().length() + 1, monitor);
            }
            finally {
                stream.close();
            }
            stream.close();
        }
        finally {
            outStream.close();
        }
        outStream.close();
    }
    
    private static void zipFolder(final File folder, final ZipOutputStream stream, final int pathStartIndex, final IBackupProgressMonitor monitor) throws IOException {
        File[] listFiles;
        for (int length = (listFiles = folder.listFiles()).length, i = 0; i < length; ++i) {
            final File file = listFiles[i];
            if (file.isFile()) {
                final String name = file.getPath().substring(pathStartIndex);
                monitor.onNextFile(name);
                final ZipEntry zipEntry = new ZipEntry(name);
                stream.putNextEntry(zipEntry);
                final FileInputStream inputStream = new FileInputStream(file);
                try {
                    IOUtils.copy(inputStream, stream);
                }
                finally {
                    inputStream.close();
                }
                inputStream.close();
                stream.closeEntry();
            }
            else if (file.isDirectory()) {
                zipFolder(file, stream, pathStartIndex, monitor);
            }
        }
    }
    
    private static void copy(final File from, final File to, final int pathStartIndex, final IBackupProgressMonitor monitor) throws IOException {
        if (from.isDirectory()) {
            if (!to.exists()) {
                to.mkdir();
            }
            String[] list;
            for (int length = (list = from.list()).length, i = 0; i < length; ++i) {
                final String fileName = list[i];
                copy(new File(from, fileName), new File(to, fileName), pathStartIndex, monitor);
            }
        }
        else {
            monitor.onNextFile(to.getPath().substring(pathStartIndex));
            FileUtils.copyFile(from, to, true);
        }
    }
    
    private static int countFilesInFolder(final File folder) {
        if (!folder.isDirectory()) {
            return 0;
        }
        int count = 0;
        File[] listFiles;
        for (int length = (listFiles = folder.listFiles()).length, i = 0; i < length; ++i) {
            final File file = listFiles[i];
            if (file.isDirectory()) {
                count += countFilesInFolder(file);
            }
            else {
                ++count;
            }
        }
        return count;
    }
    
    private WorldBackup() {
    }
    
    public enum WorldBackupType
    {
        NONE("NONE", 0, "wdl.backup.none", ""), 
        FOLDER("FOLDER", 1, "wdl.backup.folder", "wdl.saveProgress.backingUp.title.folder"), 
        ZIP("ZIP", 2, "wdl.backup.zip", "wdl.saveProgress.backingUp.title.zip");
        
        public final String descriptionKey;
        public final String titleKey;
        
        private WorldBackupType(final String s, final int n, final String descriptionKey, final String titleKey) {
            this.descriptionKey = descriptionKey;
            this.titleKey = titleKey;
        }
        
        public String getDescription() {
            return I18n.format(this.descriptionKey, new Object[0]);
        }
        
        public String getTitle() {
            return I18n.format(this.titleKey, new Object[0]);
        }
        
        public static WorldBackupType match(final String name) {
            WorldBackupType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final WorldBackupType type = values[i];
                if (type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return WorldBackupType.NONE;
        }
    }
    
    public interface IBackupProgressMonitor
    {
        void setNumberOfFiles(final int p0);
        
        void onNextFile(final String p0);
    }
}
