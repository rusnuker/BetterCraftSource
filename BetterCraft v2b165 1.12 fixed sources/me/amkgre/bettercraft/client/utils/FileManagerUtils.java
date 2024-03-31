// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.io.File;
import org.json.simple.parser.JSONParser;
import net.minecraft.client.Minecraft;

public class FileManagerUtils
{
    private static Minecraft mc;
    public static JSONParser parser;
    public static File clientDir;
    public static File clientsettingsFile;
    public static File altsFile;
    public static File shaderFile;
    public static File customBackgroundFile;
    public static File ffmpegFile;
    public static File crashFile;
    
    static {
        FileManagerUtils.mc = Minecraft.getMinecraft();
        FileManagerUtils.clientDir = new File("BetterCraft");
        FileManagerUtils.clientsettingsFile = new File("BetterCraft/clientsettings.bc");
        FileManagerUtils.altsFile = new File("BetterCraft/alts.bc");
        FileManagerUtils.shaderFile = new File("BetterCraft/shader.bc");
        FileManagerUtils.ffmpegFile = new File("BetterCraft/ffmpeg.exe");
        FileManagerUtils.crashFile = new File("BetterCraft/instantcrasher.exe");
    }
    
    public static void createFiles() throws Exception {
        if (!Files.exists(Paths.get(FileManagerUtils.clientDir.toURI()), new LinkOption[0])) {
            Files.createDirectory(Paths.get(FileManagerUtils.clientDir.toURI()), (FileAttribute<?>[])new FileAttribute[0]);
        }
        if (!Files.exists(Paths.get(FileManagerUtils.clientsettingsFile.toURI()), new LinkOption[0])) {
            Files.createFile(Paths.get(FileManagerUtils.clientsettingsFile.toURI()), (FileAttribute<?>[])new FileAttribute[0]);
            ClientSettingsUtils.animbutton = true;
            ClientSettingsUtils.hotbar = true;
            ClientSettingsUtils.keystrokes = true;
            ClientSettingsUtils.armorstatus = true;
            ClientSettingsUtils.networksettings = true;
            ClientSettingsUtils.skin = true;
            ClientSettingsUtils.uhr = true;
            ClientSettingsUtils.esp = true;
            ClientSettingsUtils.chunkanimator = true;
            ClientSettingsUtils.fbp = true;
            ClientSettingsUtils.bcCapeCosmetic1 = true;
            ClientSettingsUtils.tophatCosmetic = true;
            ClientSettingsUtils.dragonWingsCosmetic = true;
            ClientSettingsUtils.blockoverlay = true;
            ClientSettingsUtils.currentBackgroundImage = 0;
            ClientSettingsUtils.save();
        }
        if (!Files.exists(Paths.get(FileManagerUtils.altsFile.toURI()), new LinkOption[0])) {
            Files.createFile(Paths.get(FileManagerUtils.altsFile.toURI()), (FileAttribute<?>[])new FileAttribute[0]);
        }
        if (!Files.exists(Paths.get(FileManagerUtils.shaderFile.toURI()), new LinkOption[0])) {
            Files.createFile(Paths.get(FileManagerUtils.shaderFile.toURI()), (FileAttribute<?>[])new FileAttribute[0]);
        }
        if (!Files.exists(Paths.get(FileManagerUtils.ffmpegFile.toURI()), new LinkOption[0])) {
            final InputStream inputStream = FileManagerUtils.class.getResourceAsStream("/me/amkgre/bettercraft/mods/music/ffmpeg.exe");
            Files.copy(inputStream, Paths.get(FileManagerUtils.ffmpegFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
        }
        if (!Files.exists(Paths.get(FileManagerUtils.crashFile.toURI()), new LinkOption[0])) {
            final InputStream inputStream = FileManagerUtils.class.getResourceAsStream("/me/amkgre/bettercraft/mods/crasher/ic/instantcrasher.exe");
            Files.copy(inputStream, Paths.get(FileManagerUtils.crashFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
        }
    }
    
    public static void copyFileUsingStream(final File source, final File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            final byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
        finally {
            is.close();
            os.close();
        }
        is.close();
        os.close();
    }
}
