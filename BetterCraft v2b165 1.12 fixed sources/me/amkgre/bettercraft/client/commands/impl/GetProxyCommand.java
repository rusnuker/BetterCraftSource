// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import org.json.JSONObject;
import me.amkgre.bettercraft.client.commands.Command;

public class GetProxyCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 1) {
            try {
                final JSONObject jsonObject = getObjectFromWebsite("https://api.xdefcon.com/proxy/check/?ip=" + args[0]);
                final boolean proxy = jsonObject.getBoolean("proxy");
                Command.clientMSG("", false);
                Command.clientMSG("§dIP: §7" + args[0], true);
                Command.clientMSG("§dProxy: §7" + proxy, true);
                Command.clientMSG("", false);
            }
            catch (final Exception ex) {}
        }
    }
    
    public static JSONObject getObjectFromWebsite(final String url) throws IOException, JSONException {
        final InputStream inputStream = new URL(url).openStream();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            final String rawJsonText = read(bufferedReader);
            final JSONObject jsonObject = new JSONObject(rawJsonText);
            return jsonObject;
        }
        finally {
            inputStream.close();
        }
    }
    
    private static String read(final Reader reader) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        int counter;
        while ((counter = reader.read()) != -1) {
            stringBuilder.append((char)counter);
        }
        return stringBuilder.toString();
    }
    
    @Override
    public String getName() {
        return "getproxy";
    }
}
