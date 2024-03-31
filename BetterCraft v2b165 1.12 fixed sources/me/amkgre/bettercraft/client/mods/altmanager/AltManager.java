// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.altmanager;

import java.util.Collection;
import java.util.Arrays;
import java.io.Reader;
import java.io.FileReader;
import com.google.gson.Gson;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import me.amkgre.bettercraft.client.utils.FileManagerUtils;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;

public class AltManager
{
    public static String loggedInName;
    private static AltManager altManager;
    private static ArrayList<Alt> alts;
    
    static {
        AltManager.loggedInName = null;
        AltManager.alts = new ArrayList<Alt>();
    }
    
    public static ArrayList<Alt> getAlts() {
        return AltManager.alts;
    }
    
    public static void addAlt(final Alt alt) {
        AltManager.alts.add(alt);
    }
    
    public static void saveAlts() {
        final Alt[] alts = AltManager.alts.toArray(new Alt[0]);
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String json = gson.toJson(alts);
        try {
            Throwable t = null;
            try {
                final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FileManagerUtils.altsFile));
                try {
                    bufferedWriter.write(json);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
                finally {
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t2;
                    t = t2;
                }
                else {
                    final Throwable t2;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (final Exception ex) {}
    }
    
    public static void loadAlts() {
        try {
            final Alt[] alts = new Gson().fromJson(new FileReader(FileManagerUtils.altsFile), Alt[].class);
            AltManager.alts.addAll(Arrays.asList(alts));
        }
        catch (final Exception ex) {}
    }
}
