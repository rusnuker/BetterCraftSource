// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.sticker;

import net.labymod.support.util.Debug;
import net.labymod.user.sticker.data.StickerData;
import java.util.Scanner;
import net.labymod.main.Source;
import java.net.URL;
import java.net.HttpURLConnection;
import com.google.gson.Gson;

public class StickerLoader extends Thread
{
    private static final Gson GSON;
    private StickerRegistry stickerRegistry;
    
    static {
        GSON = new Gson();
    }
    
    public StickerLoader(final StickerRegistry stickerRegistry) {
        this.stickerRegistry = stickerRegistry;
    }
    
    @Override
    public void run() {
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL("http://dl.labymod.net/stickers.json").openConnection();
            connection.setRequestProperty("User-Agent", Source.getUserAgent());
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(2000);
            connection.connect();
            final int responseCode = connection.getResponseCode();
            if (responseCode / 100 == 2) {
                String jsonString = "";
                final Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNextLine()) {
                    jsonString = String.valueOf(jsonString) + scanner.nextLine();
                }
                scanner.close();
                final StickerData stickerData = StickerLoader.GSON.fromJson(jsonString, StickerData.class);
                this.stickerRegistry.stickerData = stickerData;
            }
            else {
                Debug.log(Debug.EnumDebugMode.EMOTE, "Stickerdata response code is " + responseCode);
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
    }
}
