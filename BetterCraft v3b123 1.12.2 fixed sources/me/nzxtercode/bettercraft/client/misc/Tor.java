// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc;

import java.util.zip.ZipEntry;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;
import java.util.Objects;
import net.minecraft.util.Util;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.nio.file.Paths;
import me.nzxtercode.bettercraft.client.BetterCraft;
import java.io.File;

public class Tor
{
    private static final Tor INSTANCE;
    private static Process torProcess;
    private static final File file;
    private static final File output;
    
    static {
        INSTANCE = new Tor();
        BetterCraft.getInstance();
        file = new File(BetterCraft.clientName, "tor.zip");
        BetterCraft.getInstance();
        output = new File(BetterCraft.clientName, "tor");
    }
    
    public static final Tor getInstance() {
        return Tor.INSTANCE;
    }
    
    public static void init() {
        try {
            if (!Tor.output.exists() || !Tor.file.exists()) {
                Files.copy(Tor.class.getResourceAsStream("/assets/minecraft/client/tor.zip"), Paths.get(Tor.file.getPath(), new String[0]), StandardCopyOption.REPLACE_EXISTING);
                if (!Tor.output.exists()) {
                    Tor.output.mkdir();
                    decompress(Tor.file, Tor.output);
                }
                Tor.file.delete();
            }
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
        startTor();
    }
    
    public static void startTor() {
        try {
            if (!isActive()) {
                final boolean isWindows = Util.getOSType() == Util.EnumOS.WINDOWS;
                final ProcessBuilder process = new ProcessBuilder(new String[] { String.format("%s%s%s%s%s%s", Tor.output.getPath(), File.separator, isWindows ? "windows" : "linux", File.separator, isWindows ? "" : "sudo bash ./tor", String.format("tor%s", isWindows ? ".exe" : "")) });
                Tor.torProcess = process.start();
            }
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public static void stopTor() {
        if (Tor.torProcess.isAlive()) {
            Tor.torProcess.destroy();
        }
    }
    
    public static boolean isActive() {
        return Objects.nonNull(Tor.torProcess) && Tor.torProcess.isAlive();
    }
    
    public static void decompress(final File inIn, final File outOut) throws Exception {
        final byte[] buffer = new byte[1024];
        final ZipInputStream zis = new ZipInputStream(new FileInputStream(inIn));
        for (ZipEntry zipEntry = zis.getNextEntry(); zipEntry != null; zipEntry = zis.getNextEntry()) {
            final File newFile = newFile(outOut, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            }
            else {
                final File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }
                final FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
        }
        zis.closeEntry();
        zis.close();
    }
    
    private static File newFile(final File destinationDir, final ZipEntry zipEntry) throws IOException {
        final File destFile = new File(destinationDir, zipEntry.getName());
        final String destDirPath = destinationDir.getCanonicalPath();
        final String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(String.valueOf(destDirPath) + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }
}
