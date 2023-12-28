// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.io.File;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.io.FileOutputStream;

public class ClientSettingsUtils
{
    public static boolean firstTime;
    public static boolean animbutton;
    public static boolean transbutton;
    public static boolean oldbutton;
    public static boolean mcbutton;
    public static boolean bcCapeCosmetic1;
    public static boolean bcCapeCosmetic2;
    public static boolean capeCosmetic;
    public static boolean transWingsCosmetic;
    public static boolean dragonWingsCosmetic;
    public static boolean crystalWingsCosmetic;
    public static boolean batWingsCosmetic;
    public static boolean vexWingsCosmetic;
    public static boolean beeWingsCosmetic;
    public static boolean devilWingsCosmetic;
    public static boolean witherCosmetic;
    public static boolean creeperCosmetic;
    public static boolean enchantCosmetic;
    public static boolean blazeCosmetic;
    public static boolean enderCrystalCosmetic;
    public static boolean guardianSpikesCosmetic;
    public static boolean slimeGelCosmetic;
    public static boolean susannoCosmetic;
    public static boolean sixPathCosmetic;
    public static boolean galaxySkinCosmetic;
    public static boolean skinDerpCosmetic;
    public static boolean tophatCosmetic;
    public static boolean witchhatCosmetic;
    public static boolean headsetCosmetic;
    public static boolean crownKingCosmetic;
    public static boolean devilHornsCosmetic;
    public static boolean haloCosmetic;
    public static boolean capCosmetic;
    public static boolean snoxhEyesCosmetic;
    public static boolean villagerNoseCosmetic;
    public static boolean nerdGlassesCosmetic;
    public static boolean bandanaCosmetic;
    public static boolean chatBackground;
    public static boolean scoreboardBackground;
    public static boolean tabBackground;
    public static boolean hotbar;
    public static boolean keystrokes;
    public static boolean armorstatus;
    public static boolean itemsize;
    public static boolean skin;
    public static boolean networksettings;
    public static boolean uhr;
    public static boolean blockoverlay;
    public static boolean esp;
    public static boolean nametags;
    public static boolean radar;
    public static boolean fbp;
    public static boolean chunkanimator;
    public static boolean isCurrentBackgroundImageCustom;
    public static int currentBackgroundImage;
    
    static {
        ClientSettingsUtils.currentBackgroundImage = 0;
    }
    
    public static void save() {
        try {
            final FileOutputStream fos = new FileOutputStream(SaveLocation.location);
            Field[] fields;
            for (int length = (fields = getFields()).length, i = 0; i < length; ++i) {
                final Field f = fields[i];
                fos.write((String.valueOf(fieldToStr(f)) + "\r\n").getBytes());
            }
            fos.flush();
            fos.close();
        }
        catch (final Exception ex) {}
    }
    
    public static void load() {
        try {
            final DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(SaveLocation.location)));
            final Field[] list = getFields();
            String r;
            while ((r = dis.readLine()) != null) {
                final int splitPos;
                if ((splitPos = r.indexOf(61)) != -1) {
                    final String name = r.substring(0, splitPos);
                    final String value = r.substring(splitPos + 1).replace("\r", "");
                    Field[] array;
                    for (int length = (array = list).length, i = 0; i < length; ++i) {
                        final Field f = array[i];
                        if (f.getName().equals(name)) {
                            strToField(f, value);
                            break;
                        }
                    }
                }
            }
            dis.close();
        }
        catch (final Exception ex) {}
    }
    
    private static Field[] getFields() {
        return ClientSettingsUtils.class.getFields();
    }
    
    public static String fieldToStr(final Field f) {
        final String name = String.valueOf(f.getName()) + "=";
        final String type = f.getType().toGenericString();
        try {
            final Object val = f.get(null);
            if (val instanceof String) {
                return String.valueOf(name) + String.valueOf(val).replace("\n", "\\\n");
            }
            return String.valueOf(name) + String.valueOf(val);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public static void strToField(final Field f, final String value) {
        final String name = String.valueOf(f.getName()) + "=";
        final String type = f.getType().toGenericString();
        try {
            final Object val = f.get(null);
            if (val instanceof String) {
                f.set(null, value.replace("\\\n", "\n"));
            }
            else if (type.equals("boolean")) {
                f.set(null, Boolean.parseBoolean(value));
            }
            else if (type.equals("byte")) {
                f.set(null, Byte.parseByte(value));
            }
            else if (type.equals("int")) {
                f.set(null, Integer.parseInt(value));
            }
            else if (type.equals("long")) {
                f.set(null, Long.parseLong(value));
            }
            else if (type.equals("float")) {
                f.set(null, Float.parseFloat(value));
            }
            else if (type.equals("double")) {
                f.set(null, Double.parseDouble(value));
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static class SaveLocation
    {
        public static File location;
        
        static {
            SaveLocation.location = new File("BetterCraft/clientsettings.bc");
        }
    }
}
