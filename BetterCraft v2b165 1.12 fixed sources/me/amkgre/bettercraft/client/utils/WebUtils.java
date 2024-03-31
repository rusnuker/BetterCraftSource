// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URI;
import java.awt.Desktop;

public class WebUtils
{
    public static boolean openLink(final String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
            return true;
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    public static String getInformationsFromWebsite(final String websiteLink) {
        try {
            final StringBuilder stringBuilder = new StringBuilder("");
            final URL url = new URL(websiteLink);
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            return stringBuilder.toString();
        }
        catch (final Exception e) {
            return "error";
        }
    }
}
