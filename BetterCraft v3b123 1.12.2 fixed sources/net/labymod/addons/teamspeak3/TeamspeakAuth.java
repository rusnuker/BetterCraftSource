// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.io.IOUtils;
import java.io.FileReader;
import java.io.File;
import java.io.OutputStream;

public class TeamspeakAuth
{
    public static void auth(final OutputStream outputStream) {
        String s = "";
        final String s2 = System.getProperty("os.name").toUpperCase();
        if (s2.contains("MAC")) {
            s = String.valueOf(System.getProperty("user.home")) + "/Library/Application Support/TeamSpeak 3";
        }
        else if (s2.contains("WIN")) {
            s = String.valueOf(System.getenv("AppData")) + "\\TS3Client";
        }
        else {
            s = String.valueOf(System.getProperty("user.home")) + "/.ts3client";
        }
        final File file1 = new File(s, "clientquery.ini");
        if (!file1.exists()) {
            System.err.println("[TeamSpeak] Couldn't find teamspeak's clientquery.ini!");
        }
        else {
            String s3 = null;
            try {
                final String s4 = IOUtils.toString(new FileReader(file1));
                final String[] split;
                final int length = (split = s4.split("\n")).length;
                int i = 0;
                while (i < length) {
                    final String s5 = split[i];
                    if (s5.startsWith("api_key")) {
                        final String s6 = s5.split("api_key=")[1].replace("\r", "").replace("\n", "");
                        if (s6.length() != 29) {
                            System.err.println("[TeamSpeak] Invalid TeamSpeak3 api_key! Length: " + s6.length() + " but it should be 29");
                            break;
                        }
                        s3 = s6;
                        break;
                    }
                    else {
                        ++i;
                    }
                }
            }
            catch (final IOException ioexception) {
                ioexception.printStackTrace();
                return;
            }
            if (s3 != null) {
                final PrintWriter printwriter = new PrintWriter(outputStream, true);
                printwriter.println("auth apikey=" + s3);
                System.out.println("[TeamSpeak] Authed with api-key " + s3);
            }
        }
    }
}
