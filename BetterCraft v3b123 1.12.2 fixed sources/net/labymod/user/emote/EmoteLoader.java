// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.emote;

import net.labymod.user.emote.keys.provider.KeyFrameStorage;
import net.labymod.support.util.Debug;
import net.labymod.utils.GZIPCompression;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import net.labymod.main.Source;
import java.net.URL;
import java.net.HttpURLConnection;
import com.google.gson.Gson;

public class EmoteLoader extends Thread
{
    private static final Gson GSON;
    private EmoteRegistry emoteRegistry;
    
    static {
        GSON = new Gson();
    }
    
    public EmoteLoader(final EmoteRegistry emoteRegistry) {
        this.emoteRegistry = emoteRegistry;
    }
    
    @Override
    public void run() {
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL("http://dl.labymod.net/emotes/emotedata").openConnection();
            connection.setRequestProperty("User-Agent", Source.getUserAgent());
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(2000);
            connection.connect();
            final int responseCode = connection.getResponseCode();
            if (responseCode / 100 == 2) {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final DataOutputStream dos = new DataOutputStream(baos);
                final BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
                final byte[] data = new byte[1024];
                int count;
                while ((count = bis.read(data, 0, 1024)) != -1) {
                    dos.write(data, 0, count);
                }
                final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                final DataInputStream dis = new DataInputStream(bais);
                final byte[] compressed = new byte[dis.readInt()];
                dis.read(compressed);
                final byte[] decompressed = GZIPCompression.decompress(compressed);
                this.decompile(decompressed);
            }
            else {
                Debug.log(Debug.EnumDebugMode.EMOTE, "Emotedata response code is " + responseCode);
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
    }
    
    private void decompile(final byte[] decompressed) throws Exception {
        final ByteArrayInputStream bais = new ByteArrayInputStream(decompressed);
        final DataInputStream dis = new DataInputStream(bais);
        final int count = dis.readInt();
        this.emoteRegistry.emoteSources.clear();
        for (int i = 0; i < count; ++i) {
            final short id = dis.readShort();
            final byte[] nameInBytes = new byte[dis.readInt()];
            dis.read(nameInBytes);
            final String name = new String(nameInBytes);
            final byte[] jsonInBytes = new byte[dis.readInt()];
            dis.read(jsonInBytes);
            final String json = new String(jsonInBytes);
            try {
                final KeyFrameStorage storage = EmoteLoader.GSON.fromJson(json, KeyFrameStorage.class);
                storage.setName(name);
                storage.setId(id);
                this.emoteRegistry.emoteSources.put(storage.getId(), storage);
            }
            catch (final Exception error) {
                Debug.log(Debug.EnumDebugMode.EMOTE, "Invalid emote data: " + json);
            }
        }
        Debug.log(Debug.EnumDebugMode.EMOTE, "Loaded " + this.emoteRegistry.emoteSources.size() + " emotes!");
    }
}
