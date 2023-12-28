// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.altmanager;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

public class AltGen
{
    public static String username;
    public static String password;
    
    public static void generate() {
        try {
            final URL url = new URL("https://bettercraft.net/api/alt/alt.php");
            final HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] split = line.split(":");
                AltGen.username = split[0];
                AltGen.password = split[1];
            }
            reader.close();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
