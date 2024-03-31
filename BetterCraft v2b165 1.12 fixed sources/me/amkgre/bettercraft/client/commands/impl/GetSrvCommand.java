// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.Command;

public class GetSrvCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            try {
                if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
                    Command.clientMSG("Use Muliplayer Only", true);
                }
                else {
                    final JSONObject jsonObject = getObjectFromWebsite("https://api.mcsrvstat.us/2/" + Minecraft.getMinecraft().getCurrentServerData().serverIP);
                    final String ip = jsonObject.getString("ip");
                    final int port = jsonObject.getInt("port");
                    final String hostname = jsonObject.getString("hostname");
                    final String software = jsonObject.getString("software");
                    final String version = jsonObject.getString("version");
                    Command.clientMSG("", false);
                    Command.clientMSG("§dIP: §7" + ip, true);
                    Command.clientMSG("§dPort: §7" + port, true);
                    Command.clientMSG("§dHostname: §7" + hostname, true);
                    Command.clientMSG("§dSoftware: §7" + software, true);
                    Command.clientMSG("§dVersion: §7" + version, true);
                    Command.clientMSG("", false);
                }
            }
            catch (final IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        else if (args.length == 1) {
            try {
                final JSONObject jsonObject = getObjectFromWebsite("https://api.mcsrvstat.us/2/" + args[0]);
                final String ip = jsonObject.getString("ip");
                final int port = jsonObject.getInt("port");
                final String hostname = jsonObject.getString("hostname");
                final String software = jsonObject.getString("software");
                final String version = jsonObject.getString("version");
                Command.clientMSG("", false);
                Command.clientMSG("§dIP: §7" + ip, true);
                Command.clientMSG("§dPort: §7" + port, true);
                Command.clientMSG("§dHostname: §7" + hostname, true);
                Command.clientMSG("§dSoftware: §7" + software, true);
                Command.clientMSG("§dVersion: §7" + version, true);
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
        return "getsrv";
    }
}
