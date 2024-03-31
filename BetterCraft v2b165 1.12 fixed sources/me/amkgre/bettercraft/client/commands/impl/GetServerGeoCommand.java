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

public class GetServerGeoCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            try {
                if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
                    final JSONObject jsonObject = getObjectFromWebsite("http://ip-api.com/json/");
                    final String organisation = jsonObject.getString("org");
                    final String country = jsonObject.getString("country");
                    final String city = jsonObject.getString("city");
                    final String region = jsonObject.getString("regionName");
                    final String as = jsonObject.getString("as");
                    final String isp = jsonObject.getString("isp");
                    final String timeZone = jsonObject.getString("timezone");
                    final String ip = jsonObject.getString("query");
                    final String cc = jsonObject.getString("countryCode");
                    Command.clientMSG("", false);
                    Command.clientMSG("§dOrganisation: §7" + organisation, true);
                    Command.clientMSG("§dCountry: §7" + country, true);
                    Command.clientMSG("§dCity: §7" + city, true);
                    Command.clientMSG("§dRegion: §7" + region, true);
                    Command.clientMSG("§dAS: §7" + as, true);
                    Command.clientMSG("§dISP: §7" + isp, true);
                    Command.clientMSG("§dTimezone: §7" + timeZone, true);
                    Command.clientMSG("§dIP: §7" + ip, true);
                    Command.clientMSG("§dCountry Code: §7" + cc, true);
                    Command.clientMSG("", false);
                }
                else {
                    final JSONObject jsonObject = getObjectFromWebsite("http://ip-api.com/json/" + Minecraft.getMinecraft().getCurrentServerData().serverIP);
                    final String organisation = jsonObject.getString("org");
                    final String country = jsonObject.getString("country");
                    final String city = jsonObject.getString("city");
                    final String region = jsonObject.getString("regionName");
                    final String as = jsonObject.getString("as");
                    final String isp = jsonObject.getString("isp");
                    final String timeZone = jsonObject.getString("timezone");
                    final String ip = jsonObject.getString("query");
                    final String cc = jsonObject.getString("countryCode");
                    Command.clientMSG("", false);
                    Command.clientMSG("§dOrganisation: §7" + organisation, true);
                    Command.clientMSG("§dCountry: §7" + country, true);
                    Command.clientMSG("§dCity: §7" + city, true);
                    Command.clientMSG("§dRegion: §7" + region, true);
                    Command.clientMSG("§dAS: §7" + as, true);
                    Command.clientMSG("§dISP: §7" + isp, true);
                    Command.clientMSG("§dTimezone: §7" + timeZone, true);
                    Command.clientMSG("§dIP: §7" + ip, true);
                    Command.clientMSG("§dCountry Code: §7" + cc, true);
                    Command.clientMSG("", false);
                }
            }
            catch (final IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        else if (args.length == 1) {
            try {
                final JSONObject jsonObject = getObjectFromWebsite("http://ip-api.com/json/" + args[0]);
                final String organisation = jsonObject.getString("org");
                final String country = jsonObject.getString("country");
                final String city = jsonObject.getString("city");
                final String region = jsonObject.getString("regionName");
                final String as = jsonObject.getString("as");
                final String isp = jsonObject.getString("isp");
                final String timeZone = jsonObject.getString("timezone");
                final String ip = jsonObject.getString("query");
                final String cc = jsonObject.getString("countryCode");
                Command.clientMSG("", false);
                Command.clientMSG("§dOrganisation: §7" + organisation, true);
                Command.clientMSG("§dCountry: §7" + country, true);
                Command.clientMSG("§dCity: §7" + city, true);
                Command.clientMSG("§dRegion: §7" + region, true);
                Command.clientMSG("§dAS: §7" + as, true);
                Command.clientMSG("§dISP: §7" + isp, true);
                Command.clientMSG("§dTimezone: §7" + timeZone, true);
                Command.clientMSG("§dIP: §7" + ip, true);
                Command.clientMSG("§dCountry Code: §7" + cc, true);
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
        return "getgeo";
    }
}
