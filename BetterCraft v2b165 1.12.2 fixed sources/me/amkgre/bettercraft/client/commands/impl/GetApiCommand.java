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

public class GetApiCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            try {
                if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
                    Command.clientMSG("Use Muliplayer Only", true);
                }
                else {
                    final JSONObject jsonObject = getObjectFromWebsite("https://mcapi.xdefcon.com/server/" + Minecraft.getMinecraft().getCurrentServerData().serverIP + "/full/json");
                    final String serverIP = jsonObject.getString("serverip");
                    final String version = jsonObject.getString("version");
                    final String protocol = jsonObject.getString("protocol");
                    final long ping = jsonObject.getLong("ping");
                    final int players = jsonObject.getInt("players");
                    final int maxPlayers = jsonObject.getInt("maxplayers");
                    Command.clientMSG("", false);
                    Command.clientMSG("§dIP: §7" + serverIP, true);
                    Command.clientMSG("§dVersion: §7" + version, true);
                    Command.clientMSG("§dProtocol: §7" + protocol, true);
                    Command.clientMSG("§dPing: §7" + ping, true);
                    Command.clientMSG("§dPlayers: §7" + players, true);
                    Command.clientMSG("§dMax Players: §7" + maxPlayers, true);
                    Command.clientMSG("", false);
                }
            }
            catch (final IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        else if (args.length == 1) {
            try {
                final JSONObject jsonObject = getObjectFromWebsite("https://mcapi.xdefcon.com/server/" + args[0] + "/full/json");
                final String serverIP = jsonObject.getString("serverip");
                final String version = jsonObject.getString("version");
                final String protocol = jsonObject.getString("protocol");
                final long ping = jsonObject.getLong("ping");
                final int players = jsonObject.getInt("players");
                final int maxPlayers = jsonObject.getInt("maxplayers");
                Command.clientMSG("", false);
                Command.clientMSG("§dIP: §7" + serverIP, true);
                Command.clientMSG("§dVersion: §7" + version, true);
                Command.clientMSG("§dProtocol: §7" + protocol, true);
                Command.clientMSG("§dPing: §7" + ping, true);
                Command.clientMSG("§dPlayers: §7" + players, true);
                Command.clientMSG("§dMax Players: §7" + maxPlayers, true);
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
        return "getapi";
    }
}
