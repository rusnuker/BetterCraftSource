// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.shader.browser;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;

public class ShaderSaveFile
{
    public static String formattJson(final String json) {
        return json.replace("{", "{\n").replace("}", "\n}").replace("\",", "\",\n").replace("},", "},\n");
    }
    
    public static String unformattJson(final String json) {
        return json.replace("{\n", "{").replace("\n}", "}").replace("\",\n", "\",").replace("},\n", "},");
    }
    
    public static String getTextFromFile(final File file) {
        try {
            FileReader fr = null;
            final int len = (int)file.length();
            final char[] buf = new char[len];
            fr = new FileReader(file);
            fr.read(buf);
            final String text = new String(buf);
            return unformattJson(text);
        }
        catch (final Exception ex) {
            return "";
        }
    }
    
    public static void setTextFromFile(final String text, final File file) {
        try {
            final FileWriter writer = new FileWriter(file);
            final StringBuilder builder = new StringBuilder();
            builder.append(formattJson(text));
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        }
        catch (final Exception ex) {}
    }
}
