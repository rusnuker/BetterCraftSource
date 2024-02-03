// 
// Decompiled by Procyon v0.6.0
// 

package wdl.update;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import wdl.api.IWDLMessageType;
import wdl.WDLMessages;
import wdl.WDLMessageTypes;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.List;
import wdl.WDL;
import net.minecraft.client.Minecraft;
import java.io.File;
import com.google.gson.JsonParser;

public class GithubInfoGrabber
{
    private static final String USER_AGENT;
    private static final JsonParser PARSER;
    private static final String RELEASE_LIST_LOCATION = "https://api.github.com/repos/Pokechu22/WorldDownloader/releases?per_page=100";
    private static final File CACHED_RELEASES_FILE;
    
    static {
        PARSER = new JsonParser();
        CACHED_RELEASES_FILE = new File(Minecraft.getMinecraft().mcDataDir, "WorldDownloader_Update_Cache.json");
        final String mcVersion = WDL.getMinecraftVersionInfo();
        final String wdlVersion = "1.8.9a-beta2";
        USER_AGENT = String.format("WorldDownloader mod by Pokechu22 (Minecraft %s; WDL %s) ", mcVersion, wdlVersion);
    }
    
    public static List<Release> getReleases() throws Exception {
        final JsonArray array = query("https://api.github.com/repos/Pokechu22/WorldDownloader/releases?per_page=100").getAsJsonArray();
        final List<Release> returned = new ArrayList<Release>();
        for (final JsonElement element : array) {
            returned.add(new Release(element.getAsJsonObject()));
        }
        return returned;
    }
    
    public static JsonElement query(final String path) throws Exception {
        InputStream stream = null;
        try {
            final HttpsURLConnection connection = (HttpsURLConnection)new URL(path).openConnection();
            connection.setRequestProperty("User-Agent", GithubInfoGrabber.USER_AGENT);
            connection.setRequestProperty("Accept", "application/vnd.github.v3.full+json");
            if (WDL.globalProps.getProperty("UpdateETag") != null) {
                final String etag = WDL.globalProps.getProperty("UpdateETag");
                if (!etag.isEmpty()) {
                    connection.setRequestProperty("If-None-Match", etag);
                }
            }
            connection.connect();
            if (connection.getResponseCode() == 304) {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG, "wdl.messages.updates.usingCachedUpdates", new Object[0]);
                stream = new FileInputStream(GithubInfoGrabber.CACHED_RELEASES_FILE);
            }
            else {
                if (connection.getResponseCode() != 200) {
                    throw new Exception("Unexpected response while getting " + path + ": " + connection.getResponseCode() + " " + connection.getResponseMessage());
                }
                WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG, "wdl.messages.updates.grabingUpdatesFromGithub", new Object[0]);
                stream = connection.getInputStream();
            }
            InputStreamReader reader = null;
            try {
                reader = new InputStreamReader(stream);
                final JsonElement element = GithubInfoGrabber.PARSER.parse(reader);
                PrintStream output = null;
                String etag2 = null;
                try {
                    output = new PrintStream(GithubInfoGrabber.CACHED_RELEASES_FILE);
                    output.println(element.toString());
                    etag2 = connection.getHeaderField("ETag");
                }
                catch (final Exception e) {
                    etag2 = null;
                    throw e;
                }
                finally {
                    if (output != null) {
                        output.close();
                    }
                    if (etag2 != null) {
                        WDL.globalProps.setProperty("UpdateETag", etag2);
                    }
                    else {
                        WDL.globalProps.remove("UpdateETag");
                    }
                    WDL.saveGlobalProps();
                }
                if (output != null) {
                    output.close();
                }
                if (etag2 != null) {
                    WDL.globalProps.setProperty("UpdateETag", etag2);
                }
                else {
                    WDL.globalProps.remove("UpdateETag");
                }
                WDL.saveGlobalProps();
                return element;
            }
            finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
        finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
