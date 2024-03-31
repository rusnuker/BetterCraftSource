// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient;

import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RealmsVersion
{
    private static String version;
    
    public static String getVersion() {
        if (RealmsVersion.version != null) {
            return RealmsVersion.version;
        }
        BufferedReader reader = null;
        try {
            final InputStream versionStream = RealmsVersion.class.getResourceAsStream("/version");
            reader = new BufferedReader(new InputStreamReader(versionStream));
            RealmsVersion.version = reader.readLine();
            reader.close();
            return RealmsVersion.version;
        }
        catch (final Exception ex) {}
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (final IOException ex2) {}
            }
        }
        return null;
    }
}
