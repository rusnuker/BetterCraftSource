// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.resources;

import java.util.IllegalFormatException;
import com.google.common.collect.Iterables;
import org.apache.commons.io.Charsets;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.regex.Pattern;
import com.google.common.base.Splitter;

public class Locale
{
    private static final Splitter splitter;
    private static final Pattern pattern;
    Map<String, String> properties;
    private boolean unicode;
    
    static {
        splitter = Splitter.on('=').limit(2);
        pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    }
    
    public Locale() {
        this.properties = (Map<String, String>)Maps.newHashMap();
    }
    
    public synchronized void loadLocaleDataFiles(final IResourceManager resourceManager, final List<String> languageList) {
        this.properties.clear();
        for (final String s : languageList) {
            final String s2 = String.format("lang/%s.lang", s);
            for (final String s3 : resourceManager.getResourceDomains()) {
                try {
                    this.loadLocaleData(resourceManager.getAllResources(new ResourceLocation(s3, s2)));
                }
                catch (final IOException ex) {}
            }
        }
        this.checkUnicode();
    }
    
    public boolean isUnicode() {
        return this.unicode;
    }
    
    private void checkUnicode() {
        this.unicode = false;
        int i = 0;
        int j = 0;
        for (final String s : this.properties.values()) {
            final int k = s.length();
            j += k;
            for (int l = 0; l < k; ++l) {
                if (s.charAt(l) >= '\u0100') {
                    ++i;
                }
            }
        }
        final float f = i / (float)j;
        this.unicode = (f > 0.1);
    }
    
    private void loadLocaleData(final List<IResource> resourcesList) throws IOException {
        for (final IResource iresource : resourcesList) {
            final InputStream inputstream = iresource.getInputStream();
            try {
                this.loadLocaleData(inputstream);
            }
            finally {
                IOUtils.closeQuietly(inputstream);
            }
            IOUtils.closeQuietly(inputstream);
        }
    }
    
    private void loadLocaleData(final InputStream inputStreamIn) throws IOException {
        for (final String s : IOUtils.readLines(inputStreamIn, Charsets.UTF_8)) {
            if (!s.isEmpty() && s.charAt(0) != '#') {
                final String[] astring = Iterables.toArray(Locale.splitter.split(s), String.class);
                if (astring == null || astring.length != 2) {
                    continue;
                }
                final String s2 = astring[0];
                final String s3 = Locale.pattern.matcher(astring[1]).replaceAll("%$1s");
                this.properties.put(s2, s3);
            }
        }
    }
    
    private String translateKeyPrivate(final String translateKey) {
        final String s = this.properties.get(translateKey);
        return (s == null) ? translateKey : s;
    }
    
    public String formatMessage(final String translateKey, final Object[] parameters) {
        final String s = this.translateKeyPrivate(translateKey);
        try {
            return String.format(s, parameters);
        }
        catch (final IllegalFormatException var5) {
            return "Format error: " + s;
        }
    }
}
