// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.main.lang;

import java.io.InputStream;
import net.labymod.utils.ModColor;
import java.io.Reader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Iterator;
import net.labymod.support.util.Debug;
import net.minecraft.client.Minecraft;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class LanguageManager
{
    private static Map<String, Language> lang;
    private static Language language;
    private static Language defaultLanguage;
    public static String lastLocaleCode;
    
    static {
        LanguageManager.lang = new ConcurrentHashMap<String, Language>();
        LanguageManager.lastLocaleCode = "";
    }
    
    public static void updateLang() {
        final String defaultLanguageCode;
        String mcLanguage = defaultLanguageCode = "en_US";
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().getLanguageManager() != null && Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage() != null && Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode() != null) {
            mcLanguage = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
        }
        Language targetLanguage = null;
        for (final Language lang : LanguageManager.lang.values()) {
            if (mcLanguage.equals(lang.getName())) {
                targetLanguage = lang;
            }
        }
        if (targetLanguage == null) {
            Debug.log(Debug.EnumDebugMode.LANGUAGE, String.valueOf(mcLanguage) + " is not loaded! Trying to load it..");
            targetLanguage = load(mcLanguage);
        }
        else {
            Debug.log(Debug.EnumDebugMode.LANGUAGE, "Detected Minecraft language: " + mcLanguage);
        }
        if (LanguageManager.defaultLanguage == null) {
            LanguageManager.defaultLanguage = load(defaultLanguageCode);
        }
        if (targetLanguage == null) {
            Debug.log(Debug.EnumDebugMode.LANGUAGE, String.valueOf(mcLanguage) + " doesn't exists, using default language instead.");
            targetLanguage = LanguageManager.defaultLanguage;
        }
        if (targetLanguage == null) {
            Debug.log(Debug.EnumDebugMode.LANGUAGE, "Using no language!");
        }
        else {
            Debug.log(Debug.EnumDebugMode.LANGUAGE, "Using language " + targetLanguage.getName() + " now.");
        }
        LanguageManager.language = targetLanguage;
        LanguageManager.lastLocaleCode = mcLanguage;
    }
    
    public static Language load(final String name) {
        try {
            final Properties prop = new Properties();
            if (name.contains("_")) {
                final String[] sp = name.split("_");
                final String[] files = { sp[1], name, sp[0] };
                boolean found = false;
                String[] array;
                for (int length = (array = files).length, i = 0; i < length; ++i) {
                    final String fileName = array[i];
                    final InputStream stream = LanguageManager.class.getResourceAsStream("/assets/minecraft/labymod/lang/" + fileName.toUpperCase() + ".properties");
                    if (stream != null) {
                        final InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
                        prop.load(reader);
                        reader.close();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Debug.log(Debug.EnumDebugMode.LANGUAGE, "Cannot find following language: " + name + " (" + sp[0] + ", " + sp[1] + ")");
                    return null;
                }
            }
            final Language lang = new Language(name);
            for (final Map.Entry<Object, Object> s : prop.entrySet()) {
                lang.translations.put(s.getKey().toString(), ModColor.createColors(s.getValue().toString()));
            }
            LanguageManager.lang.put(name, lang);
            return lang;
        }
        catch (final Exception error) {
            error.printStackTrace();
            Debug.log(Debug.EnumDebugMode.LANGUAGE, "Couldn't load language file " + name + " " + error.getMessage());
            return null;
        }
    }
    
    public static String translateString(final String key, final boolean format, final Object... args) {
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().getLanguageManager() != null && Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage() != null && Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode() != null && !LanguageManager.lastLocaleCode.equals(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode())) {
            updateLang();
        }
        if (key == null || LanguageManager.language == null) {
            return key;
        }
        String trans = LanguageManager.language.get(key);
        if (trans == null && (LanguageManager.defaultLanguage == null || (trans = LanguageManager.defaultLanguage.get(key)) == null)) {
            return key;
        }
        if (format && args != null) {
            try {
                trans = String.format(trans, args);
            }
            catch (final Exception error) {
                error.printStackTrace();
            }
        }
        return trans;
    }
    
    public static String translateOrReturnKey(final String key, final Object... args) {
        final String returned = translate(key, args);
        return returned.contains("N/A") ? key : returned;
    }
    
    public static String translate(final String key) {
        return translateString(key, false, new Object[0]);
    }
    
    public static String translate(final String key, final Object... args) {
        return translateString(key, true, args);
    }
    
    public static Language getLanguage() {
        return LanguageManager.language;
    }
}
