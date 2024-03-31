// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import net.minecraft.client.Minecraft;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import org.apache.commons.io.IOUtils;
import java.net.URL;

public class MiscUtils
{
    public static String getUUID(final String name) {
        String uuid = null;
        final String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        try {
            final String UUIDJson = IOUtils.toString(new URL(url));
            if (UUIDJson.isEmpty()) {
                return null;
            }
            final JSONObject UUIDObject = (JSONObject)JSONValue.parse(UUIDJson);
            uuid = UUIDObject.get("id").toString();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        return uuid;
    }
    
    public static String removeColorCodes(String message) {
        final String colorCodes = "0123456789abcdefklmnor";
        final ArrayList<String> colors = new ArrayList<String>();
        char[] charArray;
        for (int length = (charArray = colorCodes.toCharArray()).length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            colors.add(new StringBuilder().append(c).toString());
        }
        final Object object = colors.iterator();
        while (((Scanner)object).hasNext()) {
            final String s = ((Scanner)object).next();
            message = message.replaceAll("§" + s, "");
        }
        return message;
    }
    
    public static String bindString(final String[] s, final int f, final int t) {
        String out = "";
        for (int i = f; i < t; ++i) {
            out = (out.equalsIgnoreCase("") ? s[i] : (String.valueOf(out) + " " + s[i]));
        }
        return out;
    }
    
    public static String bindString(final String[] s, final int f, final int t, final String split) {
        String out = "";
        for (int i = f; i < t; ++i) {
            out = (out.equalsIgnoreCase("") ? s[i] : (String.valueOf(out) + split + s[i]));
        }
        return out;
    }
    
    public static double round(double value, final int places) {
        if (places < 0) {
            return 0.0;
        }
        final long factor = (long)Math.pow(10.0, places);
        final long tmp = Math.round(value *= factor);
        return tmp / (double)factor;
    }
    
    public static int getIntFromRGB(int r, int g, int b) {
        r = (r << 16 & 0xFF0000);
        g = (g << 8 & 0xFF00);
        return 0xFF000000 | r | g | (b &= 0xFF);
    }
    
    public static int getRandomDiff(final int max, final int min) {
        if (max < min || min == 0 || max == 0) {
            return 1;
        }
        if (max == min) {
            return max;
        }
        final Random rndm = new Random();
        return min + rndm.nextInt(max - min);
    }
    
    public static void selectSlot(final int slot) {
        Minecraft.getMinecraft().player.inventory.currentItem = slot;
    }
}
