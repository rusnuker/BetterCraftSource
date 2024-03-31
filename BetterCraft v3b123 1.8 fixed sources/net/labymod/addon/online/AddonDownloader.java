// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addon.online;

import java.io.FileOutputStream;
import java.io.DataInputStream;
import net.labymod.main.Source;
import java.net.URL;
import java.net.HttpURLConnection;
import net.labymod.support.util.Debug;
import net.labymod.addon.AddonLoader;
import net.labymod.addon.online.info.OnlineAddonInfo;
import java.io.File;
import net.labymod.addon.online.info.AddonInfo;

public class AddonDownloader extends Thread
{
    private AddonInfo addonInfo;
    private CallbackAddonDownloadProcess callback;
    private File file;
    
    public AddonDownloader(final OnlineAddonInfo addonInfo, final CallbackAddonDownloadProcess callback) {
        this.addonInfo = addonInfo;
        this.callback = callback;
        if (addonInfo.isIncludeInJar()) {
            this.file = new File("LabyMod/ofhandler/", "optifine.jar");
            this.file.getParentFile().mkdir();
        }
        else {
            this.file = new File(AddonLoader.getAddonsDirectory(), String.valueOf(addonInfo.getName()) + ".jar");
        }
    }
    
    @Override
    public void run() {
        try {
            Debug.log(Debug.EnumDebugMode.ADDON, "Download addon " + this.addonInfo.getName() + " " + this.addonInfo.getDownloadURL());
            final HttpURLConnection connection = (HttpURLConnection)new URL(this.addonInfo.getDownloadURL()).openConnection();
            connection.setRequestProperty("User-Agent", Source.getUserAgent());
            connection.connect();
            final DataInputStream dataInputStream = new DataInputStream(connection.getInputStream());
            final FileOutputStream fileOutputStream = new FileOutputStream(this.file);
            final int total = connection.getContentLength();
            int current = 0;
            final boolean smooth = total < 1000000;
            final byte[] buff = new byte[2048];
            int readBytes = 0;
            while ((readBytes = dataInputStream.read(buff, 0, buff.length)) != -1) {
                fileOutputStream.write(buff, 0, readBytes);
                current += readBytes;
                if (total != -1 && current != total) {
                    this.callback.progress(current / (double)total * 100.0);
                }
                if (smooth) {
                    Thread.sleep(30L);
                }
            }
            dataInputStream.close();
            fileOutputStream.close();
            this.callback.success(this.file);
            this.callback.progress(100.0);
        }
        catch (final Exception e) {
            this.callback.failed(e.getMessage());
            e.printStackTrace();
        }
    }
}
